import java.io.File;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class TestEMF {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting EMF Test...");
        ResourceSet resourceSet = new ResourceSetImpl();
        org.eclipse.emf.ecore.EcorePackage.eINSTANCE.eClass();
        org.eclipse.uml2.uml.UMLPackage.eINSTANCE.eClass();
        org.eclipse.uml2.uml.resources.util.UMLResourcesUtil.init(resourceSet);
        
        File f = new File("../examples/sample-model.uml");
        System.out.println("File exists: " + f.exists() + " at " + f.getAbsolutePath());
        URI uri = URI.createFileURI(f.getAbsolutePath());
        Resource res = resourceSet.getResource(uri, true);
        System.out.println("Loaded resource with " + res.getContents().size() + " elements.");
        for(EObject obj : res.getContents()) {
            System.out.println("Root object: " + obj.eClass().getName());
            if (obj instanceof org.eclipse.uml2.uml.Model) {
                System.out.println("  It IS a org.eclipse.uml2.uml.Model!");
            }
        }
    }
}
