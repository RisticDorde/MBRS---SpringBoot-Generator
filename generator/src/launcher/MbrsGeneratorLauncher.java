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

public class MbrsGeneratorLauncher extends AbstractAcceleoGenerator {
    
    public MbrsGeneratorLauncher(URI modelURI, File targetFolder, List<? extends Object> arguments) throws IOException {
        initialize(modelURI, targetFolder, arguments);
    }

    public MbrsGeneratorLauncher(EObject model, File targetFolder, List<? extends Object> arguments) throws IOException {
        initialize(model, targetFolder, arguments);
    }
    
    @Override
    public void initialize(URI modelURI, File folder, List<?> arguments) throws IOException {
        System.out.println("DEBUG: initialize called with URI: " + modelURI);
        super.initialize(modelURI, folder, arguments);
        System.out.println("DEBUG: model after super.initialize: " + this.model);
        if (this.model != null) {
            System.out.println("DEBUG: model eClass: " + this.model.eClass().getName());
        } else {
            System.out.println("DEBUG: model is NULL! Failed to load.");
        }
        System.out.println("DEBUG: Module URL: " + findModuleURL(getModuleName()));
        System.out.println("DEBUG: Trying to manually load EMTL...");
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
                System.out.println("DEBUG: Forcefully loaded module from disk: " + emtl.getAbsolutePath());
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
        // Force register UML and Ecore for standalone execution
        resourceSet.getPackageRegistry().put(org.eclipse.emf.ecore.EcorePackage.eNS_URI, org.eclipse.emf.ecore.EcorePackage.eINSTANCE);
        resourceSet.getPackageRegistry().put(org.eclipse.uml2.uml.UMLPackage.eNS_URI, org.eclipse.uml2.uml.UMLPackage.eINSTANCE);
        // CRITICAL: Put it in the GLOBAL registry as well, so Acceleo's internal load matches the same instance!
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
                        System.out.println("DEBUG: Profile has no definition. Defining it now...");
                        ePackage = profile.define();
                    }
                    if (ePackage != null) {
                        System.out.println("DEBUG: Profile EPackage successfully loaded/defined: " + ePackage.getNsURI());
                        resourceSet.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
                        
                        // CRITICAL FOR UML2 STANDALONE: Register profile location so getAppliedStereotypes() works!
                        URI profileElementURI = profileURI.appendFragment(profile.eResource().getURIFragment(profile));
                        org.eclipse.uml2.uml.UMLPlugin.getEPackageNsURIToProfileLocationMap().put(ePackage.getNsURI(), profileElementURI);
                        System.out.println("DEBUG: Profile registered in UMLPlugin map.");
                    } else {
                        System.out.println("DEBUG: Profile EPackage is STILL NULL after define()!");
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
                
                List<String> arguments = new ArrayList<String>();
                for (int i = 2; i < args.length; i++) {
                    arguments.add(args[i]);
                }
                MbrsGeneratorLauncher generator = new MbrsGeneratorLauncher(modelURI, folder, arguments);
                generator.doGenerate(new BasicMonitor());
                
                System.out.println("=====================================================");
                System.out.println("SUCCESS: Generation completed! Check output at: " + folder.getAbsolutePath());
                System.out.println("=====================================================");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
