import os
import re
import subprocess

GH_EXEC = r"C:\Program Files\GitHub CLI\gh.exe"
ISSUES_FILE = r"C:\mbrs\MBRS_ISSUES.md"

def main():
    with open(ISSUES_FILE, 'r', encoding='utf-8') as f:
        content = f.read()

    # Find all labels to create them first
    # Suggested labels: `epic`, `setup`, `profile`, ...
    all_labels = set()
    label_matches = re.findall(r'\*\*Labels:\*\* (.*)', content)
    for match in label_matches:
        labels = re.findall(r'`(.*?)`', match)
        all_labels.update(labels)
        
    print(f"Ensuring {len(all_labels)} labels exist...")
    for label in all_labels:
        try:
            # We don't care if it fails (e.g. already exists)
            subprocess.run([GH_EXEC, "label", "create", label], capture_output=True, text=True)
        except Exception:
            pass

    # Split the file by issues. An issue starts with ### #<number> 
    issues = re.split(r'\n### #\d+\s+', '\n' + content)
    
    if len(issues) < 2:
        print("No issues found to parse.")
        return

    for issue_block in issues[1:]:
        lines = issue_block.strip().split('\n')
        title = lines[0].strip()
        
        labels = []
        body_lines = []
        
        for i, line in enumerate(lines[1:]):
            if line.startswith("**Labels:**"):
                labels = re.findall(r'`(.*?)`', line)
            elif line.startswith("---") or line.startswith("## Milestone") or line.startswith("## Cross-cutting"):
                break
            else:
                body_lines.append(line)
        
        body = '\n'.join(body_lines).strip()
        
        cmd = [GH_EXEC, "issue", "create", "--title", title, "--body", body]
        for label in labels:
            cmd.extend(["--label", label])
        
        print(f"Creating issue: {title}")
        try:
            result = subprocess.run(cmd, capture_output=True, text=True, check=True)
            print(f"Success: {result.stdout.strip()}")
        except subprocess.CalledProcessError as e:
            print(f"Failed to create issue '{title}'. Error:\n{e.stderr}")

if __name__ == "__main__":
    main()
