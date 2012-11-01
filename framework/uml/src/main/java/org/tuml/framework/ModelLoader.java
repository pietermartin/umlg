package org.tuml.framework;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.uml.UMLEnvironment;
import org.eclipse.ocl.uml.UMLEnvironmentFactory;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.util.UMLUtil;

public class ModelLoader {

	public static final ResourceSet RESOURCE_SET = new ResourceSetImpl();
	private static Model model;
	private static Profile tumlValidationProfile;

	public static Model loadModel(File modelFile) {
		registerResourceFactories();
		URLClassLoader loader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
		URI uri = URI.createURI(findLocation(loader, true, "org/eclipse/uml2/uml/resources", "org.eclipse.uml2.uml.resources"));
		registerPathmaps(uri);
		File dir = modelFile.getParentFile();
		URI dirUri = URI.createFileURI(dir.getAbsolutePath());
		model = (Model) load(dirUri.appendSegment(modelFile.getName()));
		tumlValidationProfile = model.getAppliedProfile("Tuml::Validation");
		return model;
	}

	public static Model getModel() {
		return model;
	}

	public static Stereotype findStereotype(String name) {
		if (tumlValidationProfile != null) {
			return tumlValidationProfile.getOwnedStereotype(name);
		} else {
			return null;
		}
	}

	public static NamedElement findNamedElement(String qualifiedName) {
		Collection<NamedElement> results = UMLUtil.findNamedElements(RESOURCE_SET, qualifiedName);
		if (results.size() > 1) {
			throw new RuntimeException("This method must only be called for named elements that are unique in the system!");
		}
		if (results.isEmpty()) {
			return null;
		} else {
			return results.iterator().next();
		}
	}

	public static List<Generalization> getSpecifics(final Classifier c) {
		List<Generalization> results = new ArrayList<Generalization>();
		filter(results, model, new Filter() {
			
			@Override
			public boolean filter(Element e) {
				return e instanceof Generalization && ((Generalization)e).getGeneral() == c;
			}
		});
		
		return results;
	}

	public static List<Stereotype> getStereotypes() {
		if (tumlValidationProfile != null) {
			return tumlValidationProfile.getOwnedStereotypes();
		} else {
			return Collections.emptyList();
		}
	}

	protected static org.eclipse.uml2.uml.Package load(URI uri) {
		org.eclipse.uml2.uml.Package package_ = null;
		Resource resource = RESOURCE_SET.getResource(uri, true);
		EcoreUtil.resolveAll(RESOURCE_SET);
		EcoreUtil.resolveAll(resource); 
		package_ = (org.eclipse.uml2.uml.Package) resource.getContents().get(0);
		return package_;
	}

	protected static void registerResourceFactories() {
		UMLEnvironment umlEnvironment = new UMLEnvironmentFactory(RESOURCE_SET).createEnvironment();
		Environment.Registry.INSTANCE.registerEnvironment(umlEnvironment);
		RESOURCE_SET.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
		RESOURCE_SET.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
	}

	protected static void registerPathmaps(URI uri) {
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), uri.appendSegment("libraries").appendSegment(""));
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), uri.appendSegment("metamodels").appendSegment(""));
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP), uri.appendSegment("profiles").appendSegment(""));
	}

	public static String findLocation(URLClassLoader s, boolean jar, String... names) {
		try {
			URL[] urls = s.getURLs();
			String location = null;
			outer: for (URL url : urls) {
				for (String string : names) {
					String ext = url.toExternalForm();
					if (ext.contains(string)) {
						File file = new File(url.getFile());
						if (ext.endsWith(".jar")) {
							if (jar) {
								location = "jar:file:///" + file.getAbsolutePath().replace('\\', '/') + "!/";
							}
						} else {
							if (!jar) {
								location = "file:///" + file.getAbsolutePath().replace('\\', '/');
							}
						}
						break outer;
					}
				}
			}
			if (location == null && s.getParent() instanceof URLClassLoader && s.getParent() != s) {
				location = findLocation((URLClassLoader) s.getParent(), jar, names);
			}
			return location;
		} catch (Throwable t) {
			System.out.println(t.toString());
			return null;
		}
	}
	

	private static <T extends Element> void  filter(List<T> result, Element element, Filter f) {
		if (f.filter(element)) {
			result.add((T) element);
		}
		for (Element e : element.getOwnedElements()) {
			filter(result, e, f);
		}
	}
	
	private static interface Filter {
		boolean filter(Element e);
	}
}
