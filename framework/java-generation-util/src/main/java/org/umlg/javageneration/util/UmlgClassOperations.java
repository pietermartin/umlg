package org.umlg.javageneration.util;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ocl.uml.CollectionType;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.internal.operations.ClassOperations;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.framework.ModelLoader;

public class UmlgClassOperations extends ClassOperations {

    public static boolean hasLookupProperty(org.eclipse.uml2.uml.Class clazz) {
        Set<Property> properties = getAllOwnedProperties(clazz);
        for (Property property : properties) {
            if (new PropertyWrapper(property).hasLookup()) {
                return true;
            }
        }
        return false;
    }

    public static Set<Property> getChildPropertiesToDelete(org.eclipse.uml2.uml.Class clazz) {
        Set<Property> result = new HashSet<Property>();
        Set<Property> ownedProperties = getAllOwnedProperties(clazz);
        for (Property p : ownedProperties) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!pWrap.isDerived() && (pWrap.isComposite() || (!pWrap.isPrimitive() && !pWrap.isEnumeration() && pWrap.getOtherEnd() == null))) {
                result.add(p);
            }
        }
        return result;
    }

    public static Set<Property> getPropertiesToClearOnDeletion(org.eclipse.uml2.uml.Class clazz) {
        Set<Property> result = new HashSet<Property>();
        Set<Property> ownedProperties = getAllOwnedProperties(clazz);
        for (Property p : ownedProperties) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!pWrap.isDerived() && !pWrap.isComposite() && !pWrap.isPrimitive() && !pWrap.isEnumeration()) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Returns all properties that are to be persisted on the clazz's vertex.
     * i.e. only simple types and enums
     */
    public static Set<Property> getOnePrimitiveOrEnumProperties(org.eclipse.uml2.uml.Class clazz) {
        Set<Property> result = new HashSet<Property>();
        for (Property p : clazz.getAttributes()) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!pWrap.isDerived() && pWrap.isOne() && (pWrap.isPrimitive() || pWrap.isEnumeration())) {
                result.add(p);
            }
        }
        return result;
    }

//    public static Set<Property> getPrimitiveOrEnumOrComponentsProperties(org.eclipse.uml2.uml.Class clazz) {
//        Set<Property> result = new HashSet<Property>();
//        for (Property p : getAllOwnedProperties(clazz)) {
//            PropertyWrapper pWrap = new PropertyWrapper(p);
//            if ((pWrap.isOne() && !pWrap.isDerived() && !pWrap.isQualifier())
//                    || (!pWrap.isDerived() && pWrap.isMany() && (pWrap.isPrimitive() || pWrap.isEnumeration())) ||
//                    pWrap.isComponent()) {
//                result.add(p);
//            }
//        }
//        return result;
//    }

    public static Set<Property> getPrimitiveOrEnumOrComponentsExcludeOneProperties(org.eclipse.uml2.uml.Class clazz) {
        Set<Property> result = new HashSet<Property>();
        for (Property p : getAllOwnedProperties(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!pWrap.isDerived() && !pWrap.isQualifier()) {
                if (pWrap.isDataType() || pWrap.isComponent()) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    public static Set<Property> getPropertiesThatHaveAndEdge(Class clazz) {
        Set<Property> result = new HashSet<Property>();
        for (Property p : getAllOwnedProperties(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!pWrap.isDerived() && !pWrap.isQualifier()) {
                if (pWrap.isMany() || !pWrap.isPrimitive()) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    public static Set<Property> getOneProperties(org.eclipse.uml2.uml.Class clazz) {
        Set<Property> result = new HashSet<Property>();
        for (Property p : getAllOwnedProperties(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!pWrap.isDerived() && !pWrap.isQualifier()) {
                if (!pWrap.isDataType() && (pWrap.isComponent() || pWrap.isOne())) {
                    //Skip composite owner
                    if (pWrap.isOne() && pWrap.getOtherEnd() != null && pWrap.getOtherEnd().isComposite()) {
                        continue;
                    }
                    result.add(p);
                }
            }
        }
        return result;
    }

    public static Set<Property> getNonCompositeProperties(org.eclipse.uml2.uml.Class clazz) {
        Set<Property> result = new HashSet<Property>();
        for (Property p : getAllOwnedProperties(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!pWrap.isDerived() && !pWrap.isQualifier() && !pWrap.isComposite()) {
                result.add(p);
            }
        }
        return result;
    }

    public static Set<Property> getPropertiesForToJson(org.eclipse.uml2.uml.Class clazz) {
        Set<Property> result = new HashSet<Property>();
        for (Property p : getAllOwnedProperties(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!pWrap.isQualified()) {
                if (pWrap.isComponent() || pWrap.isDataType()) {
                    result.add(p);
                } else if (!pWrap.isDataType() && pWrap.isOne()) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    public static Set<Property> getPropertiesForToJsonExcludingCompositeParent(org.eclipse.uml2.uml.Class clazz) {
        Set<Property> result = new HashSet<Property>();
        for (Property p : getAllOwnedProperties(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!pWrap.isQualified()) {
                if (pWrap.isComponent() || pWrap.isDataType()) {
                    if (!(pWrap.getOtherEnd() != null && pWrap.getOtherEnd().isComposite())) {
                        result.add(p);
                    }
                } else if (!pWrap.isDataType() && pWrap.isOne()) {
                    if (!(pWrap.getOtherEnd() != null && pWrap.getOtherEnd().isComposite())) {
                        result.add(p);
                    }
                }
            }
        }
        return result;
    }

    /*
     * These include all properties that are on the other end of an association.
     * It does not include inherited properties
     */
    public static Set<Property> getAllOwnedProperties(org.eclipse.uml2.uml.Class clazz) {

        Set<Property> result = new HashSet<Property>(clazz.getAttributes());
        List<Association> associations = clazz.getAssociations();
        for (Association association : associations) {
            List<Property> memberEnds = association.getMemberEnds();
            //For the case of an association to itself both ends are owned.
            //In particular to generate the collection initialization
            if (memberEnds.get(0).getType().equals(clazz) && memberEnds.get(1).getType().equals(clazz)) {
                result.addAll(memberEnds);
            } else {
                for (Property property : memberEnds) {
                    if (property.getType() != clazz) {
                        result.add(property);
                    }
                }
            }
        }
        result.addAll(getPropertiesOnRealizedInterfaces(clazz));
        return result;
    }

    /*
     * These include all properties that are on the other end of an association.
     * It includes inherited properties
     */
    public static Set<Property> getAllProperties(org.eclipse.uml2.uml.Class clazz) {
        Set<Property> result = new HashSet<Property>(clazz.getAllAttributes());
        Set<Association> associations = getAllAssociations(clazz);
        for (Association association : associations) {
            List<Property> memberEnds = association.getMemberEnds();
            Property memberEnd1 = memberEnds.get(0);
            Property memberEnd2 = memberEnds.get(1);

            // This is for the case of hierarchies, i.e association to itself
            if (isSpecializationOf(clazz, memberEnd1.getType()) && isSpecializationOf(clazz, memberEnd2.getType())) {
                result.add(memberEnd1);
                result.add(memberEnd2);
            }
            // This is to prevent getting the near side of an association
            if (!isSpecializationOf(clazz, memberEnd1.getType())) {
                result.add(memberEnd1);
            }
            // This is to prevent getting the near side of an association
            if (!isSpecializationOf(clazz, memberEnd2.getType())) {
                result.add(memberEnd2);
            }
        }
        result.addAll(getPropertiesOnRealizedInterfaces(clazz));
        return result;
    }

    public static Set<Property> getPropertiesOnRealizedInterfaces(org.eclipse.uml2.uml.Class clazz) {
        Set<Property> result = new HashSet<Property>();
        List<Interface> interfaces = clazz.getImplementedInterfaces();
        for (Interface inf : interfaces) {
            Set<Property> properties = UmlgInterfaceOperations.getAllProperties(inf);
            for (Property p : properties) {
                result.add(p);
            }
        }
        return result;
    }

    public static Set<Property> getOtherEndToComposite(Classifier classifier) {
        Set<Property> compositeOwners  = new HashSet<Property>();
        Set<Association> associations = getAllAssociations(classifier);
        for (Association association : associations) {
            List<Property> memberEnds = association.getMemberEnds();
            for (Property property : memberEnds) {
                if (!property.isComposite() /*&& property.getType() != classifier*/ && property.getOtherEnd().isComposite()
                        && isSpecializationOf(classifier, property.getOtherEnd().getType())) {
                    compositeOwners.add(property);
                }
            }
        }
        return compositeOwners;
    }

    /*
     * Only BehavioredClassifier can realize interfaces
     */
    public static boolean isSpecializationOf(Classifier special, Type type) {
        if (special == type) {
            return true;
        }
        if ((special instanceof BehavioredClassifier) && ((BehavioredClassifier) special).getAllImplementedInterfaces().contains(type)) {
            return true;
        }
        for (Classifier general : special.getGenerals()) {
            if (isSpecializationOf(general, type)) {
                return true;
            }
        }
        return false;
    }

    public static Set<Association> getAllAssociations(Classifier classifier) {
        Set<Association> result = new HashSet<Association>();
        if (classifier instanceof Class) {
            for (Interface implementedInterface : ((Class) classifier).getAllImplementedInterfaces()) {
                result.addAll(implementedInterface.getAssociations());
            }
        }
        getAllAssociationsFromGenerals(classifier, result);
        return result;
    }

    public static void getAllAssociationsFromGenerals(Classifier classifier, Set<Association> result) {
        result.addAll(classifier.getAssociations());
        for (Classifier general : classifier.getGenerals()) {
            getAllAssociationsFromGenerals(general, result);
        }
    }

    public static Set<OJPathName> getOtherEndToCompositePathName(Class clazz) {
        Set<OJPathName> result = new HashSet<OJPathName>();
        Set<Property> endsToComposite = getOtherEndToComposite(clazz);
        for (Property p : endsToComposite) {
            result.add(getPathName(p.getType()));
        }
        return result;
    }

    public static OJPathName getPathName(Type type) {
        String className = Namer.name(type);
        String fullPackageName = Namer.name(type.getNearestPackage());
        OJPathName ojPathName = new OJPathName(fullPackageName + "." + className);
        return ojPathName;
    }

    public static boolean hasSupertype(Class clazz) {
        return !getConcreteGenerals(clazz).isEmpty();
    }

    public static List<Class> getConcreteGenerals(Classifier clazz) {
        List<Class> result = new ArrayList<Class>();
        List<Classifier> generals = clazz.getGenerals();
        for (Classifier classifier : generals) {
            if (classifier instanceof Class) {
                result.add((Class) classifier);
            }
        }
        return result;
    }

    private static void getConcreteImplementations(Set<Classifier> result, Classifier clazz) {
        if (!clazz.isAbstract() && !(clazz instanceof Interface)) {
            result.add(clazz);
        }
        List<Generalization> generalizations = ModelLoader.INSTANCE.getSpecifics(clazz);
        for (Generalization generalization : generalizations) {
            Classifier specific = generalization.getSpecific();
            getConcreteImplementations(result, specific);
        }
    }

    private static void getSpecializations(Set<Classifier> result, Classifier clazz) {
        result.add(clazz);
        List<Generalization> generalizations = ModelLoader.INSTANCE.getSpecifics(clazz);
        for (Generalization generalization : generalizations) {
            Classifier specific = generalization.getSpecific();
            getSpecializations(result, specific);
        }
    }

    public static Set<Classifier> getRealizationWithCompositeOwner(Interface inf) {
        Set<Classifier> result = new HashSet<Classifier>();
        List<InterfaceRealization> interfaceRealizations = ModelLoader.INSTANCE.getInterfaceRealization(inf);
        for (InterfaceRealization interfaceRealization : interfaceRealizations) {
            BehavioredClassifier c = interfaceRealization.getImplementingClassifier();
            if (hasCompositeOwner(c)) {
                result.add(c);
            }
        }
        return result;
    }

    public static Set<Classifier> getConcreteRealization(Interface inf) {
        Set<Classifier> result = new HashSet<Classifier>();
        List<InterfaceRealization> interfaceRealizations = ModelLoader.INSTANCE.getInterfaceRealization(inf);
        for (InterfaceRealization interfaceRealization : interfaceRealizations) {
            BehavioredClassifier c = interfaceRealization.getImplementingClassifier();
            result.add(c);
        }
        return result;
    }

    public static Set<Classifier> getRealizationWithoutCompositeOwner(Interface inf) {
        Set<Classifier> result = new HashSet<Classifier>();
        List<InterfaceRealization> interfaceRealizations = ModelLoader.INSTANCE.getInterfaceRealization(inf);
        for (InterfaceRealization interfaceRealization : interfaceRealizations) {
            BehavioredClassifier c = interfaceRealization.getImplementingClassifier();
            if (!hasCompositeOwner(c)) {
                result.add(c);
            }
        }
        return result;
    }

    public static Set<Classifier> getSpecializationWithCompositeOwner(Classifier clazz) {
        Set<Classifier> result = new HashSet<Classifier>();
        Set<Classifier> specializations = getSpecializations(clazz);
        for (Classifier c : specializations) {
            if (hasCompositeOwner(c)) {
                result.add(c);
            }
        }
        return result;
    }

    public static Set<Classifier> getConcreteSpecializationsWithoutCompositeOwner(Classifier clazz) {
        Set<Classifier> result = new HashSet<Classifier>();
        Set<Classifier> specializations = getSpecializations(clazz);
        for (Classifier c : specializations) {
            if (!c.isAbstract() && c instanceof Class && !hasCompositeOwner((Class) c)) {
                result.add((Class) c);
            }
        }
        return result;
    }

    public static Set<Classifier> getSpecializations(Classifier clazz) {
        Set<Classifier> result = new HashSet<Classifier>();
        getSpecializations(result, clazz);
        result.remove(clazz);
        return result;
    }

    /**
     * Returns all concrete implementations sorted by the clissifier's name
     * @param clazz
     * @return
     */
    public static SortedSet<Classifier> getConcreteImplementations(Classifier clazz) {
        SortedSet<Classifier> result = new TreeSet<Classifier>(new Comparator<Classifier>() {
            @Override
            public int compare(Classifier o1, Classifier o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        getConcreteImplementations(result, clazz);
        if (clazz instanceof Interface) {
            result.addAll(getConcreteRealization((Interface) clazz));
        }
        return result;
    }

    public static boolean hasCompositeOwner(Classifier classifier) {
        return !getOtherEndToComposite(classifier).isEmpty();
    }

    public static String className(Classifier clazz) {
        if (clazz instanceof CollectionType) {
            CollectionType collectionType = (CollectionType) clazz;
            StringBuilder sb = new StringBuilder();
            sb.append(UmlgCollectionKindEnum.from(collectionType.getKind()).getOjPathName().getLast());
            sb.append("<");
            sb.append(className(collectionType.getElementType()));
            sb.append(">");
            return sb.toString();
        } else {
            return Namer.name(clazz);
        }
    }

    public static String getMetaClassName(Classifier clazz) {
        if (clazz instanceof CollectionType) {
            CollectionType collectionType = (CollectionType) clazz;
            StringBuilder sb = new StringBuilder();
            sb.append(UmlgCollectionKindEnum.from(collectionType.getKind()).getOjPathName().getLast());
            sb.append("<");
            sb.append(className(collectionType.getElementType()));
            sb.append(">");
            return sb.toString();
        } else {
            return Namer.getMetaName(clazz);
        }
    }

    public static OJPathName getMetaClassPathName(Classifier clazz) {
        OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".meta");
        OJPathName result = ojPackage.getPathName().append(UmlgClassOperations.getMetaClassName(clazz));
        return result;

    }

    public static String propertyEnumName(Type type) {
        return Namer.name(type) + "RuntimePropertyEnum";
    }

    public static OJVisibilityKind getVisibility(VisibilityKind visibility) {
        switch (visibility) {
            case PRIVATE_LITERAL:
                return OJVisibilityKind.PRIVATE;
            case PROTECTED_LITERAL:
                return OJVisibilityKind.PROTECTED;
            case PUBLIC_LITERAL:
                return OJVisibilityKind.PUBLIC;
            case PACKAGE_LITERAL:
                return OJVisibilityKind.DEFAULT;
            default:
                throw new RuntimeException("Not supported");
        }
    }

    public static boolean isRoot(Class clazz) {
        return false;
    }

    public static OJPathName getAuditPathName(Class c) {
        OJPathName pathName = getPathName(c);
        return pathName.renameLast(pathName.getLast() + "Audit");
    }

    public static Property getAttribute(Class c, String name) {
        for (Property p : c.getAllAttributes()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public static boolean isOnInterface(PropertyWrapper pWrap) {
        return pWrap.getOwningType() instanceof Interface;
    }

    public static boolean isHierarchy(org.eclipse.uml2.uml.Class clazz) {
        if (realizesHierarchy(clazz)) {
            return true;
        }
        List<Classifier> generals = getGenerals(clazz);
        for (Classifier general : generals) {
            if (realizesHierarchy((Class) general)) {
                return true;
            }
        }
        return false;
    }

    private static boolean realizesHierarchy(org.eclipse.uml2.uml.Class clazz) {
        List<Interface> realizedInterfaces = clazz.getImplementedInterfaces();
        for (Interface interface1 : realizedInterfaces) {
            if (interface1.getQualifiedName().equals("umlglib::org::umlg::hierarchy::Hierarchy")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEnumeration(Type owningType) {
        return owningType instanceof Enumeration;
    }

    public static String checkClassConstraintName(Constraint constraint) {
        return "checkClassConstraint" + StringUtils.capitalize(constraint.getName());
    }

    public static boolean isAssociationClass(Class clazz) {
        return clazz instanceof AssociationClass;
    }

    public static List<Classifier> getGeneralizationHierarchy(Class clazz) {
        List<Classifier> result = new ArrayList<Classifier>();
        result.add(clazz);
        getGeneralizationHierarchy(result, clazz);
        return result;
    }

    private static void getGeneralizationHierarchy(List<Classifier> hierarchy, Classifier clazz) {
        List<Classifier> generals = clazz.getGenerals();
        if (generals.size() > 1) {
            throw new IllegalStateException(
                    String.format("Multiple inheritance is not supported! Class %s has more than on generalization.", clazz.getName()));
        }
        if (!generals.isEmpty()) {
            hierarchy.add(generals.get(0));
            getGeneralizationHierarchy(hierarchy, generals.get(0));
        }
    }
}
