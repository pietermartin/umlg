package org.tuml.framework;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

public class ModelLoader {

	protected static final ResourceSet RESOURCE_SET = new ResourceSetImpl();

	public static Model loadModel(File modelFile) {
		registerResourceFactories();
		URLClassLoader loader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
		URI uri = URI.createURI(findLocation(loader, true, "org/eclipse/uml2/uml/resources", "org.eclipse.uml2.uml.resources"));
		registerPathmaps(uri);
		File dir = modelFile.getParentFile();
		URI dirUri = URI.createFileURI(dir.getAbsolutePath());
		return (Model) load(dirUri.appendSegment(modelFile.getName()));
	}

	protected static org.eclipse.uml2.uml.Package load(URI uri) {
		org.eclipse.uml2.uml.Package package_ = null;
		Resource resource = RESOURCE_SET.getResource(uri, true);
		package_ = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
		return package_;
	}

	protected static void registerResourceFactories() {
		RESOURCE_SET.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
		RESOURCE_SET.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
	}

	protected static void registerPathmaps(URI uri) {
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), uri.appendSegment("libraries").appendSegment(""));
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), uri.appendSegment("metamodels").appendSegment(""));
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP), uri.appendSegment("profiles").appendSegment(""));
	}

	public static String findLocation(URLClassLoader s,boolean jar,String...names){
		try{
			URL[] urls = s.getURLs();
			String location = null;
			outer:for(URL url:urls){
				for(String string:names){
					String ext = url.toExternalForm();
					if(ext.contains(string)){
						File file = new File(url.getFile());
						if(ext.endsWith(".jar")){
							if(jar){
								System.out.println(ext);
								location = "jar:file:///" + file.getAbsolutePath().replace('\\', '/') + "!/";
							}
						}else{
							if(!jar){
								System.out.println(ext);
								location = "file:///" + file.getAbsolutePath().replace('\\', '/');
							}
						}
						break outer;
					}
				}
			}
			if(location == null && s.getParent() instanceof URLClassLoader && s.getParent() != s){
				location = findLocation((URLClassLoader) s.getParent(), jar, names);
			}
			return location;
		}catch(Throwable t){
			System.out.println(t.toString());
			return null;
		}
	}

}
