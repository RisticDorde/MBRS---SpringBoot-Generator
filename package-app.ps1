Write-Host "Cleaning up generated-app..."
if (Test-Path generated-app) { Remove-Item -Recurse -Force generated-app }

Write-Host "Copying base framework..."
Copy-Item -Path framework -Destination generated-app -Recurse

Write-Host "Merging generated code..."
Copy-Item -Path examples/out1/* -Destination generated-app -Recurse -Force

Write-Host "====================================================="
Write-Host "SUCCESS: App packaged to generated-app!"
Write-Host "You can now run the app by opening generated-app in your IDE or running:"
Write-Host "cd generated-app; mvn spring-boot:run"
Write-Host "====================================================="
