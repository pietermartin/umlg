package org.umlg.framework;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.uml.UMLEnvironment;
import org.eclipse.ocl.uml.UMLEnvironmentFactory;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.ResourcesPlugin;
import org.eclipse.uml2.uml.util.UMLUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ModelLoader {

    public final static ModelLoader INSTANCE = new ModelLoader();
    private ResourceSet RESOURCE_SET;
    private Model model;
    private Profile umlgValidationProfile;
    private Profile umlgProfile;
    private List<Model> importedModelLibraries = new ArrayList<Model>();
    private List<ModelLoadedEvent> events = new ArrayList<ModelLoadedEvent>();
    private final Logger logger = Logger.getLogger(ModelLoader.class.getPackage().getName());
    private List<Property> allIndexedFields;

    private final List<Generalization> GENERALIZATION_CACHE = new ArrayList<>();
    private final List<Abstraction> ABSTRACTION_CACHE = new ArrayList<>();

    private ModelLoader() {
        this.RESOURCE_SET = new ResourceSetImpl();
    }

    public void subscribeModelLoaderEvent(ModelLoadedEvent e) {
        this.events.add(e);
    }

    public boolean isLoaded() {
        return this.model != null;
    }

    public synchronized Model loadModel(java.net.URI modelFile) {
        if (this.model == null) {
            logger.info(String.format("Loading model %s", modelFile.toString()));
            registerResourceFactories();
            String location = findPathJar(ResourcesPlugin.class);
            location = "jar:file:///" + location + "!/";
            URI uri = URI.createURI(location);
            registerPathmaps(uri);
            URI modelUri = URI.createURI(modelFile.toString());
            model = (Model) load(modelUri);
            umlgProfile = model.getAppliedProfile("Umlg::Profile");
            umlgValidationProfile = model.getAppliedProfile("Umlg::Validation");
            for (PackageImport pi : model.getPackageImports()) {
                if (pi.getImportedPackage() instanceof Model) {
                    importedModelLibraries.add((Model) pi.getImportedPackage());
                    EcoreUtil.resolveAll(pi);
                    EcoreUtil.resolveAll(pi.getImportedPackage());
                }
            }
            logger.info("Done loading the model");
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

    public boolean isUmlGLibIncluded() {
        for (Model importedModels : this.importedModelLibraries) {
            if (importedModels.getQualifiedName().equals("umlglib")) {
                return true;
            }
        }
        return false;
    }

    public List<Property> findSubsettingProperties(final Property subsettedProperty) {
        List<Property> results = new ArrayList();
        filter(results, this.model, new Filter() {
            @Override
            public boolean filter(Element e) {
                return e instanceof Property && ((Property) e).getSubsettedProperties().contains(subsettedProperty);
            }
        });
        return results;
    }

    public List<Slot> findSlotsForThisDataType(EnumerationLiteral enumerationLiteral) {
        List<Slot> results = new ArrayList();
        filter(results, this.model, new Filter() {
            @Override
            public boolean filter(Element e) {
                if (e instanceof Slot) {
                    Slot slot = (Slot)e;
                    for (ValueSpecification valueSpecification : slot.getValues()) {
                        if (valueSpecification instanceof InstanceValue) {
                            InstanceValue instanceValue = (InstanceValue)valueSpecification;
                            InstanceSpecification instanceSpecification = instanceValue.getInstance();
                            if (instanceSpecification == enumerationLiteral) {
                                return true;
                            }
                        }
                    }
                }
                return false;
//                return e instanceof Slot && ((Slot) e).getDefiningFeature().getType().equals(enumerationLiteral.getOwner());
            }
        });
        return results;
    }

    public Stereotype findStereotype(String name) {
        Stereotype result = null;
        if (this.umlgProfile != null) {
            result = this.umlgProfile.getOwnedStereotype(name);
            if (result == null) {
                if (this.umlgValidationProfile != null) {
                    result = this.umlgValidationProfile.getOwnedStereotype(name);
                }
            }
        } else {
            if (this.umlgValidationProfile != null) {
                result = this.umlgValidationProfile.getOwnedStereotype(name);
            }
        }
        return result;
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

    public List<Constraint> getConstraints(final Element element) {
        List<Constraint> results = new ArrayList<>();
        filter(results, this.model, new Filter() {
            @Override
            public boolean filter(Element e) {
                return e instanceof Constraint && ((Constraint) e).getContext().equals(element);
            }
        });
        return results;
    }

    public List<Constraint> getConstraintsForConstrainedElement(final Element element) {
        List<Constraint> results = new ArrayList<>();
        filter(results, this.model, new Filter() {
            @Override
            public boolean filter(Element e) {
                return e instanceof Constraint && ((Constraint) e).getConstrainedElements().contains(element);
            }
        });
        return results;
    }

    /**
     * Filter out Behaviors for now
     *
     * @return
     */
    public List<Class> getAllConcreteClasses() {
        List<Class> results = new ArrayList<Class>();
        filter(results, this.model, new Filter() {
            @Override
            public boolean filter(Element e) {
                return e instanceof Class && !((Class) e).isAbstract() && !(e instanceof Behavior);
            }
        });

        return results;
    }

    public List<Class> getAllClasses() {
        List<Class> results = new ArrayList<Class>();
        filter(results, this.model, new Filter() {
            @Override
            public boolean filter(Element e) {
                return e instanceof Class && !(e instanceof Behavior);
            }
        });

        return results;
    }

    public List<Package> getAllPackages() {
        List<Package> results = new ArrayList<Package>();
        filter(results, this.model, new Filter() {
            @Override
            public boolean filter(Element e) {
                return e instanceof Package;
            }
        });
        return results;
    }

    public List<Property> getAllProperties() {
        List<Property> results = new ArrayList<Property>();
        filter(results, this.model, new Filter() {
            @Override
            public boolean filter(Element e) {
                return e instanceof Property;
            }
        });
        return results;
    }


    /**
     * @return
     */
    public List<Property> getAllQualifiers() {
        List<Property> results = new ArrayList<Property>();
        filter(results, this.model, new Filter() {
            @Override
            public boolean filter(Element e) {
                if (e instanceof Property) {
                    Property qualifier = (Property) e;
                    Element owner = qualifier.getOwner();
                    if (owner instanceof Property) {
                        Property qualifierOwner = (Property) owner;
                        return qualifierOwner.getQualifiers().contains(qualifier);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });

        return results;
    }

    public List<Property> getAllIndexedFields() {
        final Stereotype stereotype = ModelLoader.INSTANCE.findStereotype("Index");
        List<Property> results = new ArrayList<Property>();
        filter(results, this.model, new Filter() {
            @Override
            public boolean filter(Element e) {
                if (e instanceof Property && e.isStereotypeApplied(stereotype)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        return results;
    }

    public List<Generalization> getSpecifics(final Classifier c) {
        List<Generalization> results = new ArrayList<>();
        if (GENERALIZATION_CACHE.isEmpty()) {
            populateGeneralizations();
        }
        for (Generalization generalization : GENERALIZATION_CACHE) {
            if (generalization.getGeneral() == c) {
                results.add(generalization);
            }
        }
//        filter(results, this.model, e -> {
//            //For debug
////				if (e instanceof NamedElement) {
////					NamedElement namedElement = (NamedElement)e;
////					if (namedElement.getName() != null) {
////						if (namedElement.getName().equals("Query")) {
////							System.out.println();
////						}
////					}
////				}
////				if (e instanceof Generalization && ((Generalization)e).getGeneral() == c) {
////					Classifier specific = ((Generalization)e).getSpecific();
////					if (specific.getName().equals("Query")) {
////						System.out.println();
////					}
////					return true;
////				} else {
////					return false;
////				}
//            return e instanceof Generalization && ((Generalization) e).getGeneral() == c;
//        });
        return results;
    }

    private void populateGeneralizations() {
        if (!GENERALIZATION_CACHE.isEmpty()) {
            throw new IllegalStateException("populateGeneralizations must be called on an empty cache");
        }
        filter(GENERALIZATION_CACHE, this.model, e -> e instanceof Generalization);
    }

    public List<Abstraction> getOriginalAbstractionForRefinedAssociation(final Association association) {
        List<Abstraction> results = new ArrayList<>();
        List<Abstraction> abstractions = getAbstractions();
        for (Abstraction a : abstractions) {
            for (NamedElement supplier : a.getClients()) {
                if (supplier.equals(association)) {
                    results.add(a);
                }
            }
        }
        return results;
    }

    public boolean isRefinedAssociation(final Association association) {
        List<Abstraction> abstractions = getAbstractions();
        for (Abstraction a : abstractions) {
            for (NamedElement supplier : a.getClients()) {
                if (supplier.equals(association)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Abstraction> getRefinedAbstraction(final Association association) {
        List<Abstraction> results = new ArrayList<>();
        List<Abstraction> abstractions = getAbstractions();
        for (Abstraction a : abstractions) {
            for (NamedElement supplier : a.getSuppliers()) {
                if (supplier.equals(association)) {
                    results.add(a);
                }
            }
        }
        return results;
    }

    public List<Abstraction> getAbstractions() {
//        List<Abstraction> results = new ArrayList<>();
        if (ABSTRACTION_CACHE.isEmpty()) {
            filter(ABSTRACTION_CACHE, this.model, e -> e instanceof Abstraction);

        }
        return Collections.unmodifiableList(ABSTRACTION_CACHE);
//        filter(results, this.model, new Filter() {
//            @Override
//            public boolean filter(Element e) {
//                return e instanceof Abstraction;
//            }
//        });
//
//        return results;
    }


    public List<InterfaceRealization> getInterfaceRealization(final Interface inf) {
        List<InterfaceRealization> results = new ArrayList<InterfaceRealization>();
        getInterfaceRealization(results, inf);
        return results;
    }

    private void getInterfaceRealization(List<InterfaceRealization> results, final Interface inf) {
        filter(results, this.model, new Filter() {
            @Override
            public boolean filter(Element e) {
                return e instanceof InterfaceRealization && ((InterfaceRealization) e).getContract() == inf;
            }
        });
        //Check if interface is extended
        List<Generalization> generalizations = getSpecifics(inf);
        for (Generalization generalization : generalizations) {
            Classifier specific = generalization.getSpecific();
            getInterfaceRealization(results, (Interface)specific);
        }
    }

    public List<Stereotype> getValidationStereotypes() {
        if (this.umlgValidationProfile != null) {
            return this.umlgValidationProfile.getOwnedStereotypes();
        } else {
            return Collections.emptyList();
        }
    }

    protected org.eclipse.uml2.uml.Package load(URI uri) {
        org.eclipse.uml2.uml.Package package_;
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

    public String findPathJar(java.lang.Class<?> context) throws IllegalStateException {
        URL location = context.getResource('/' + context.getName().replace(".", "/")
                + ".class");
        String jarPath = location.getPath();
        return jarPath.substring("file:".length(), jarPath.lastIndexOf("!"));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Element> void filter(List<T> result, Element element, Filter f) {
        if (f.filter(element)) {
            result.add((T) element);
        }
        if (element instanceof PackageImport) {
            PackageImport pi = (PackageImport) element;
            org.eclipse.uml2.uml.Package p = pi.getImportedPackage();
            if (!(p instanceof Profile)) {
                for (Element e : p.getOwnedElements()) {
                    filter(result, e, f);
                }
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
        this.umlgProfile = null;
        this.umlgValidationProfile = null;
        this.importedModelLibraries = new ArrayList<Model>();
    }

    public ResourceSet getRESOURCE_SET() {
        return RESOURCE_SET;
    }

    public static interface Filter {
        boolean filter(Element e);
    }
}
