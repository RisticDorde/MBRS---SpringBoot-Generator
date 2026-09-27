package launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.acceleo.engine.service.AbstractAcceleoGenerator;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;

public class MbrsOneClickLauncher extends AbstractAcceleoGenerator {
    
    public MbrsOneClickLauncher(URI modelURI, File targetFolder, List<? extends Object> arguments) throws IOException {
        initialize(modelURI, targetFolder, arguments);
    }

    public MbrsOneClickLauncher(EObject model, File targetFolder, List<? extends Object> arguments) throws IOException {
        initialize(model, targetFolder, arguments);
    }
    
    @Override
    public void initialize(URI modelURI, File folder, List<?> arguments) throws IOException {
        System.out.println("DEBUG: initialize called with URI: " + modelURI);
        super.initialize(modelURI, folder, arguments);
        if (this.model != null) {
            System.out.println("DEBUG: model eClass: " + this.model.eClass().getName());
        } else {
            System.out.println("DEBUG: model is NULL! Failed to load.");
        }
    }

    @Override
    public String getModuleName() {
        return "/generate/generate";
    }
    
    @Override
    protected java.net.URL findModuleURL(String moduleName) {
        try {
            java.io.File emtl = new java.io.File("bin/generate/generate.emtl");
            if (!emtl.exists()) emtl = new java.io.File("../bin/generate/generate.emtl");
            if (emtl.exists()) {
                return emtl.toURI().toURL();
            }
        } catch(Exception e) {}
        return super.findModuleURL(moduleName);
    }

    @Override
    public String[] getTemplateNames() {
        return new String[] { "generateAll" };
    }

    @Override
    public void registerPackages(ResourceSet resourceSet) {
        super.registerPackages(resourceSet);
        resourceSet.getPackageRegistry().put(org.eclipse.emf.ecore.EcorePackage.eNS_URI, org.eclipse.emf.ecore.EcorePackage.eINSTANCE);
        resourceSet.getPackageRegistry().put(org.eclipse.uml2.uml.UMLPackage.eNS_URI, org.eclipse.uml2.uml.UMLPackage.eINSTANCE);
        org.eclipse.emf.ecore.EPackage.Registry.INSTANCE.put(org.eclipse.uml2.uml.UMLPackage.eNS_URI, org.eclipse.uml2.uml.UMLPackage.eINSTANCE);
        org.eclipse.uml2.uml.profile.standard.StandardPackage.eINSTANCE.eClass();

        try {
            File profileFile = new File("../profile/mbrs.profile.uml");
            if (profileFile.exists()) {
                URI profileURI = URI.createFileURI(profileFile.getAbsolutePath());
                org.eclipse.emf.ecore.resource.Resource profileResource = resourceSet.getResource(profileURI, true);
                if (profileResource != null && !profileResource.getContents().isEmpty()) {
                    org.eclipse.uml2.uml.Profile profile = (org.eclipse.uml2.uml.Profile) profileResource.getContents().get(0);
                    org.eclipse.emf.ecore.EPackage ePackage = profile.getDefinition();
                    if (ePackage == null) {
                        ePackage = profile.define();
                    }
                    if (ePackage != null) {
                        resourceSet.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
                        URI profileElementURI = profileURI.appendFragment(profile.eResource().getURIFragment(profile));
                        org.eclipse.uml2.uml.UMLPlugin.getEPackageNsURIToProfileLocationMap().put(ePackage.getNsURI(), profileElementURI);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerResourceFactories(ResourceSet resourceSet) {
        super.registerResourceFactories(resourceSet);
        org.eclipse.uml2.uml.resources.util.UMLResourcesUtil.init(resourceSet);
    }

    private static void copyDirectory(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }
            String[] files = source.list();
            if (files != null) {
                for (String file : files) {
                    if (file.equals("target") || file.equals(".git") || file.equals(".settings") || file.equals("bin")) continue;
                    File srcFile = new File(source, file);
                    File destFile = new File(destination, file);
                    copyDirectory(srcFile, destFile);
                }
            }
        } else {
            java.nio.file.Files.copy(source.toPath(), destination.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("Arguments not valid : {model, folder}.");
            } else {
                File modelFile = new File(args[0]);
                if (!modelFile.exists()) {
                    modelFile = new File("..", args[0]);
                }
                URI modelURI = URI.createFileURI(modelFile.getAbsolutePath());
                
                File folder = new File(args[1]);
                if (!folder.isAbsolute() && args[1].startsWith("/")) {
                    folder = new File("..", args[1]);
                }
                
                System.out.println("=====================================================");
                System.out.println("1. Copying base framework to target directory...");
                File frameworkDir = new File("../framework");
                if (frameworkDir.exists()) {
                    copyDirectory(frameworkDir, folder);
                    System.out.println("   Framework copied successfully!");
                } else {
                    System.out.println("   WARNING: Base framework folder not found!");
                }
                
                System.out.println("2. Generating code from UML model...");
                List<String> arguments = new ArrayList<String>();
                for (int i = 2; i < args.length; i++) {
                    arguments.add(args[i]);
                }
                MbrsOneClickLauncher generator = new MbrsOneClickLauncher(modelURI, folder, arguments);
                generator.doGenerate(new BasicMonitor());
                
                System.out.println("=====================================================");
                System.out.println("SUCCESS: Application is fully scaffolded and generated!");
                System.out.println("Target: " + folder.getAbsolutePath());
                System.out.println("You can now open this project in your IDE and run it.");
                System.out.println("=====================================================");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
