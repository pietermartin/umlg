package org.tuml.framework;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

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
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.ResourcesPlugin;
import org.eclipse.uml2.uml.util.UMLUtil;

public class ModelLoader {

    public final static ModelLoader INSTANCE = new ModelLoader();
    private ResourceSet RESOURCE_SET;
    private Model model;
    private Profile tumlValidationProfile;
    private List<Model> importedModelLibraries = new ArrayList<Model>();
    private List<ModelLoadedEvent> events = new ArrayList<ModelLoadedEvent>();
    private final Logger logger = Logger.getLogger(ModelLoader.class.getPackage().getName());

    private ModelLoader() {
        this.RESOURCE_SET = new ResourceSetImpl();
    }

    public void subscribeModelLoaderEvent(ModelLoadedEvent e) {
        this.events.add(e);
    }

    public boolean isLoaded() {
        return this.model != null;
    }

    public synchronized Model loadModel(File modelFile) {
        if (this.model == null) {
            logger.info(String.format("Loading model %s", modelFile.getName()));
            registerResourceFactories();
            URLClassLoader loader = (URLClassLoader) getClass().getClassLoader();
            String location = findPathJar(ResourcesPlugin.class);
            location = "jar:file:///" + location + "!/";
            URI uri = URI.createURI(location);
            registerPathmaps(uri);
            File dir = modelFile.getParentFile();
            URI dirUri = URI.createFileURI(dir.getAbsolutePath());
            model = (Model) load(dirUri.appendSegment(modelFile.getName()));
            tumlValidationProfile = model.getAppliedProfile("Tuml::Validation");
            for (PackageImport pi : model.getPackageImports()) {
                if (pi.getImportedPackage() instanceof Model) {
                    importedModelLibraries.add((Model) pi.getImportedPackage());
                }
            }
            logger.info(String.format("Done loading the model"));
            for (ModelLoadedEvent e : this.events) {
                e.loaded(this.model);
            }
            return model;
        } else {
            logger.fine("Returning model, its already loaded");
            return this.model;
        }
    }

    public Model getModel() {
        return this.model;
    }

    public List<Model> getImportedModelLibraries() {
        return this.importedModelLibraries;
    }

    public boolean isTumlLibIncluded() {
        for (Model importedModels : this.importedModelLibraries) {
            if (importedModels.getQualifiedName().equals("tumllib")) {
                return true;
            }
        }
        return false;
    }

    public Stereotype findStereotype(String name) {
        if (this.tumlValidationProfile != null) {
            return this.tumlValidationProfile.getOwnedStereotype(name);
        } else {
            return null;
        }
    }

    public NamedElement findNamedElement(String qualifiedName) {
        Collection<NamedElement> results = UMLUtil.findNamedElements(this.RESOURCE_SET, qualifiedName);
        if (results.size() > 1) {
            throw new RuntimeException("This method must only be called for named elements that are unique in the system!");
        }
        if (results.isEmpty()) {
            return null;
        } else {
            return results.iterator().next();
        }
    }

    public List<Generalization> getSpecifics(final Classifier c) {
        List<Generalization> results = new ArrayList<Generalization>();
        filter(results, this.model, new Filter() {

            @Override
            public boolean filter(Element e) {
                //For debug
//				if (e instanceof NamedElement) {
//					NamedElement namedElement = (NamedElement)e;
//					if (namedElement.getName() != null) {
//						if (namedElement.getName().equals("Query")) {
//							System.out.println();
//						}
//					}
//				}
//				if (e instanceof Generalization && ((Generalization)e).getGeneral() == c) {
//					Classifier specific = ((Generalization)e).getSpecific();
//					if (specific.getName().equals("Query")) {
//						System.out.println();
//					}
//					return true;
//				} else {
//					return false;
//				}
                return e instanceof Generalization && ((Generalization) e).getGeneral() == c;
            }
        });

        return results;
    }

    public List<InterfaceRealization> getInterfaceRealization(final Interface inf) {
        List<InterfaceRealization> results = new ArrayList<InterfaceRealization>();
        filter(results, this.model, new Filter() {

            @Override
            public boolean filter(Element e) {
                return e instanceof InterfaceRealization && ((InterfaceRealization) e).getContract() == inf;
            }
        });

        return results;
    }

    public List<Stereotype> getStereotypes() {
        if (this.tumlValidationProfile != null) {
            return this.tumlValidationProfile.getOwnedStereotypes();
        } else {
            return Collections.emptyList();
        }
    }

    protected org.eclipse.uml2.uml.Package load(URI uri) {
        org.eclipse.uml2.uml.Package package_ = null;
        Resource resource = this.RESOURCE_SET.getResource(uri, true);
        EcoreUtil.resolveAll(this.RESOURCE_SET);
        EcoreUtil.resolveAll(resource);
        package_ = (org.eclipse.uml2.uml.Package) resource.getContents().get(0);
        return package_;
    }

    protected void registerResourceFactories() {
        UMLEnvironment umlEnvironment = new UMLEnvironmentFactory(this.RESOURCE_SET).createEnvironment();
        Environment.Registry.INSTANCE.registerEnvironment(umlEnvironment);
        this.RESOURCE_SET.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
        this.RESOURCE_SET.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
    }

    protected void registerPathmaps(URI uri) {
        URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), uri.appendSegment("libraries").appendSegment(""));
        URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), uri.appendSegment("metamodels").appendSegment(""));
        URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP), uri.appendSegment("profiles").appendSegment(""));
    }

    public String findPathJar(Class<?> context) throws IllegalStateException {
        URL location = context.getResource('/' + context.getName().replace(".", "/")
                + ".class");
        String jarPath = location.getPath();
        return jarPath.substring("file:".length(), jarPath.lastIndexOf("!"));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Element> void filter(List<T> result, Element element, Filter f) {
        if (f.filter(element)) {
            result.add((T) element);
        }
        if (element instanceof PackageImport) {
            PackageImport pi = (PackageImport) element;
            org.eclipse.uml2.uml.Package p = pi.getImportedPackage();
            for (Element e : p.getOwnedElements()) {
                filter(result, e, f);
            }
        } else {
            for (Element e : element.getOwnedElements()) {
                filter(result, e, f);
            }
        }
    }

    public void clear() {
        this.model = null;
        this.RESOURCE_SET = new ResourceSetImpl();
        this.tumlValidationProfile = null;
        this.importedModelLibraries = new ArrayList<Model>();
    }

    public ResourceSet getRESOURCE_SET() {
        return RESOURCE_SET;
    }

    private static interface Filter {
        boolean filter(Element e);
    }
}
