package org.umlg.javageneration.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.framework.ModelLoader;
import org.umlg.javageneration.validation.Email;
import org.umlg.javageneration.validation.Max;
import org.umlg.javageneration.validation.MaxLength;
import org.umlg.javageneration.validation.Min;
import org.umlg.javageneration.validation.MinLength;
import org.umlg.javageneration.validation.Range;
import org.umlg.javageneration.validation.RangeLength;
import org.umlg.javageneration.validation.Url;
import org.umlg.javageneration.validation.Validation;

public class PropertyWrapper extends MultiplicityWrapper implements Property {

    private Property property;

    public PropertyWrapper(Property property) {
        super(property);
        this.property = property;
    }

    public boolean isInverseUnique() {
        if (getOtherEnd() != null) {
            return new PropertyWrapper(getOtherEnd()).isUnique();
        } else {
            return false;
        }
    }

    public boolean isDataType() {
        return getType() instanceof DataType;
    }

    public boolean needsLookup() {
        return !isComposite() && isNavigable() && !(getType() instanceof Enumeration)
                && !isDerived() && !isQualifier() && getOtherEnd() != null
                && !(getOtherEnd().getType() instanceof Enumeration) && !getOtherEnd().isComposite();
    }

    public boolean hasLookup() {
        if (!isComposite() && isNavigable() && !(getType() instanceof Enumeration) && !isDerived() && !isQualifier() && getOtherEnd() != null && !(getOtherEnd().getType() instanceof Enumeration)
                && !getOtherEnd().isComposite()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInverseComposite() {
        if (getOtherEnd() != null) {
            return new PropertyWrapper(getOtherEnd()).isComposite();
        } else {
            return false;
        }
    }

    public boolean hasQualifiers() {
        return !this.property.getQualifiers().isEmpty();
    }

    public boolean isQualified() {
        return hasQualifiers();
    }

    public boolean isInverseQualified() {
        if (getOtherEnd() != null) {
            return new PropertyWrapper(getOtherEnd()).isQualified();
        } else {
            return false;
        }
    }

    public boolean isQualifier() {
        Element owner = this.property.getOwner();
        return owner instanceof Property && ((Property) owner).getQualifiers().contains(this.property);
    }

    public boolean hasOtherEnd() {
        return getOtherEnd() != null;
    }

    public String getOclValue() {
        if (!this.property.isDerived()) {
            throw new IllegalStateException("getOclValue can only be called on a derived property");
        }
        return this.property.getDefaultValue().stringValue();
    }

    public boolean hasOclDefaultValue() {
        ValueSpecification v = getDefaultValue();
        if (v instanceof OpaqueExpression) {
            OpaqueExpression expr = (OpaqueExpression) v;
            return expr.getLanguages().contains("ocl") || expr.getLanguages().contains("OCL");
        } else {
            return false;
        }
    }

    public boolean hasJavaDefaultValue() {
        ValueSpecification v = getDefaultValue();
        if (v instanceof OpaqueExpression) {
            OpaqueExpression expr = (OpaqueExpression) v;
            return expr.getLanguages().contains("java") || expr.getLanguages().contains("JAVA");
        } else {
            return false;
        }
    }

    public String getOclDerivedValue() {
        if (!hasOclDefaultValue()) {
            throw new IllegalStateException(String.format("Property %s does not have a default value", new Object[]{this.getName()}));
        }
        StringBuilder sb = new StringBuilder();
        if (isQualified()) {
            // Find the derived property on the qualified context
            Property owner = (Property) getOwner();
            Property derived = null;
            for (Element e : owner.getType().getOwnedElements()) {
                if (e instanceof Property && ((NamedElement) e).getName().equals(getName())) {
                    derived = (Property) e;
                }
            }
            sb.append(PropertyWrapper.getOclDerivedValue(derived));
        } else {
            sb.append(PropertyWrapper.getOclDerivedValue(this.property));
        }
        return sb.toString();
    }

    private static String getOclDerivedValue(Property p) {
        PropertyWrapper pWrap = new PropertyWrapper(p);
        StringBuilder sb = new StringBuilder();
        sb.append("package ");
        sb.append(Namer.nameIncludingModel(pWrap.getOwningType().getNearestPackage()).replace(".", "::"));
        sb.append("\n    context ");
        sb.append(pWrap.getOwningType().getName());
        sb.append("::");
        sb.append(pWrap.getName());
        sb.append(" : ");

        if (pWrap.isMany()) {
            sb.append(UmlgGenerationUtil.getCollectionInterface(pWrap));
            sb.append("(");
            sb.append(pWrap.getType().getName());
            sb.append(")");
        } else {
            sb.append(pWrap.getType().getName());
        }

        sb.append("\n");
        sb.append("    derive: ");
        sb.append(pWrap.getDefaultValue().stringValue());
        sb.append("\n");
        sb.append("endpackage");
        return sb.toString();
    }

    public String getJavaDefaultValue() {
        return getDefaultValue().toString();
    }

    public String getOclDefaultValue() {
        if (!hasOclDefaultValue()) {
            throw new IllegalStateException(String.format("Property %s does not have a default value", new Object[]{this.getName()}));
        }
        StringBuilder sb = new StringBuilder();
        if (isQualified()) {
            // Find the derived property on the qualified context
            Property owner = (Property) getOwner();
            Property derived = null;
            for (Element e : owner.getType().getOwnedElements()) {
                if (e instanceof Property && ((NamedElement) e).getName().equals(getName())) {
                    derived = (Property) e;
                }
            }
            sb.append(getOclDefaultValue(derived));
        } else {
            sb.append(getOclDefaultValue(this.property));
        }
        return sb.toString();
    }

    private String getOclDefaultValue(Property p) {
        PropertyWrapper pWrap = new PropertyWrapper(p);
        StringBuilder sb = new StringBuilder();
        sb.append("package ");
        sb.append(Namer.nameIncludingModel(pWrap.getOwningType().getNearestPackage()).replace(".", "::"));
        sb.append("\ncontext ");
        sb.append(pWrap.getOwningType().getName());
        sb.append("::");
        sb.append(getName());
        sb.append("\n");
        sb.append("init: ");
        sb.append(pWrap.getDefaultValue().stringValue());
        sb.append("\n");
        sb.append("endpackage");
        return sb.toString();
    }

    public Property getQualifierCorrespondingDerivedProperty() {
        if (!isQualifier()) {
            throw new IllegalStateException("getCorrespondingDerivedProperty can only be called on a qualifier");
        }
        Property owner = (Property) getOwner();
        for (Element e : owner.getType().getOwnedElements()) {
            if (e instanceof Property && ((Property) e).isDerived() && ((Property) e).getName().equals(getName())) {
                return (Property) e;
            }
        }
        return null;
    }

    public OJPathName getQualifierContextPathName() {
        if (isQualifier()) {
            throw new IllegalStateException("getQualifierContextPathName can not only be called on a qualifier");
        }
        if (!hasQualifiers()) {
            throw new IllegalStateException("getQualifierContextPathName can not only be called on a qualified property");
        }
        return UmlgClassOperations.getPathName(getType());
    }

    public Type getQualifierContext() {
        if (!isQualifier()) {
            throw new IllegalStateException("getQualifierContext can only be called on a qualifier");
        }
        Property owner = (Property) getOwner();
        return owner.getType();
    }

    public boolean haveQualifierCorrespondingDerivedProperty() {
        if (!isQualifier()) {
            throw new IllegalStateException("getCorrespondingDerivedProperty can only be called on a qualifier");
        }
        return getQualifierCorrespondingDerivedProperty() != null;
    }

    public String getQualifiedGetterName() {
        return "getQualifierFor" + StringUtils.capitalize(getName());
    }

    public String getQualifiedNameFor(List<PropertyWrapper> qualifers) {
        StringBuilder sb = new StringBuilder();
        for (PropertyWrapper q : qualifers) {
            sb.append(StringUtils.capitalize(q.getName()));
        }
        return getter() + "For" + sb.toString();
    }

    public Property getProperty() {
        return property;
    }

    public String getTumlRuntimePropertyEnum() {
        return UmlgClassOperations.propertyEnumName(getOwningType()) + "." + fieldname();
    }

    public String getDefaultValueAsString() {
        ValueSpecification v = getDefaultValue();
        if (v instanceof OpaqueExpression) {
            if (hasOclDefaultValue()) {
                return getOclDefaultValue();
            } else {
                return getDefaultValue().stringValue();
            }
        } else if (v instanceof LiteralString) {
            if (!UmlgPropertyOperations.isString(getType())) {
                //TODO move validation to some collector to show the user all validation failures in one go
                throw new IllegalStateException(String.format("Property %s has a LiteralString default value but is of type %s. This is illegal!", getQualifiedName(), getType().getQualifiedName()));
            }
            LiteralString expr = (LiteralString) v;
            String result = expr.getValue();
            if (result != null) {
                return result.replaceAll("^\"|\"$", "");
            } else {
                return "\"\"";
            }
        } else if (v instanceof LiteralReal) {
            LiteralReal literalReal = (LiteralReal)v;
            return String.valueOf(literalReal.getValue() + "D");
        } else if (v instanceof LiteralInteger) {
            LiteralInteger literalInteger = (LiteralInteger)v;
            return String.valueOf(literalInteger.getValue());
        } else if (v instanceof LiteralUnlimitedNatural) {
            LiteralUnlimitedNatural literalUnlimitedNatural = (LiteralUnlimitedNatural)v;
            return String.valueOf(literalUnlimitedNatural.getValue());
        } else if (v instanceof LiteralBoolean) {
            LiteralBoolean literalBoolean = (LiteralBoolean)v;
            return String.valueOf(literalBoolean.isValue());
        } else if (v instanceof LiteralNull) {
            return "null";
        } else {
            throw new RuntimeException(String.format("ValueSpecification %s not supported", v.getClass().getSimpleName()));
        }
    }

    public boolean isForQualifier() {
        return !this.property.getOwner().getAppliedStereotypes().isEmpty();
    }

    public String toJson() {
        if (isMany()) {
            return "\\\"" + getName() + "\\\": \\\"\" + " + getter() + "() + \"\\\"";
        } else {
            return "\\\"" + getName() + "\\\": \\\"\" + " + getter() + "() + \"\\\"";
        }
    }

    public boolean isComponent() {
        return !this.property.isDerived() && this.property.getType() instanceof Classifier && this.property.isComposite()
                && this.property.getLower() >= 1;
    }

    public boolean isInverseOrdered() {
        return getOtherEnd() != null && getOtherEnd().isOrdered();
    }

    public Type getOwningType() {
        return UmlgPropertyOperations.getOwningType(this.property);

    }

    public boolean isControllingSide() {
        return UmlgPropertyOperations.isControllingSide(this.property);
    }

    public String fieldname() {
        return this.property.getName();
    }

    public String associationClassGetter() {
        if (!isMemberOfAssociationClass()) {
            throw new IllegalStateException("Can not call associationClassGetter on a property that is not a member end of an association class. Property = " + getQualifiedName());
        }
        return "get" + getAssociationClassPathName().getLast();
    }

    public String getter() {
        return UmlgPropertyOperations.getter(this.property);
    }

    public String setter() {
        return UmlgPropertyOperations.setter(this.property);
    }

    public String validator() {
        return UmlgPropertyOperations.validator(this.property);
    }

    public String checkConstraint(Constraint constraint) {
        return UmlgPropertyOperations.checkConstraint(this.property, constraint);
    }

    public boolean isPrimitive() {
        return UmlgPropertyOperations.isPrimitive(this.property);
    }

    public boolean isEnumeration() {
        return UmlgPropertyOperations.isEnumeration(this.property);
    }

    public boolean isACUnique() {
        return isACManyToOne() || isACOneToOne();
    }

    public boolean isACMany() {
        return false;
    }

    public boolean isACOneToMany() {
        return false;
    }

    public boolean isACManyToMany() {
        return false;
    }

    public boolean isACManyToOne() {
        return UmlgPropertyOperations.isManyToOne(this.property);
    }

    public boolean isACOneToOne() {
        //Association classes have a manyToOne relationship with bags and lists
        return ((isOneToMany() && isUnique()) || isOneToOne());
    }

    public boolean isOneToMany() {
        return UmlgPropertyOperations.isOneToMany(this.property);
    }

    public boolean isManyToOne() {
        return UmlgPropertyOperations.isManyToOne(this.property);
    }

    public boolean isManyToMany() {
        return UmlgPropertyOperations.isManyToMany(this.property);
    }

    public boolean isOneToOne() {
        return UmlgPropertyOperations.isOneToOne(this.property);
    }

    public String adder() {
        return UmlgPropertyOperations.adder(this.property);
    }

    public String remover() {
        return UmlgPropertyOperations.remover(this.property);
    }

    public String clearer() {
        return UmlgPropertyOperations.clearer(this.property);
    }

    public String internalRemover() {
        return UmlgPropertyOperations.internalRemover(this.property);
    }

    public String internalAdder() {
        return UmlgPropertyOperations.internalAdder(this.property);
    }

    public OJPathName javaBaseTypePath() {
        return UmlgPropertyOperations.getTypePath(this.property);
    }

    public OJPathName javaAuditBaseTypePath() {
        if (!isPrimitive() && !isEnumeration()) {
            return UmlgPropertyOperations.getTypePath(this.property).appendToTail("Audit");
        } else {
            return UmlgPropertyOperations.getTypePath(this.property);
        }
    }

    public OJPathName javaImplTypePath() {
        return UmlgPropertyOperations.getDefaultTinkerCollection(this.property);
    }

    /*
     * Attempting set semantics so the path is always a collection Call
     * javaBaseTypePath to get the type of set This method return umlg special
     * collection interface
     */
    public OJPathName javaTumlTypePath() {
        return javaTumlTypePath(false);
    }

    /*
     * Attempting set semantics so the path is always a collection Call
     * javaBaseTypePath to get the type of set This method return umlg special
     * collection interface
     */
    public OJPathName javaTumlTypePath(boolean ignoreAssociationClass) {
        OJPathName fieldType;
        if (isOrdered() && isUnique()) {
            if (hasQualifiers()) {
                fieldType = UmlgCollectionKindEnum.QUALIFIED_ORDERED_SET.getInterfacePathName();
            } else {
                if (ignoreAssociationClass || !isMemberOfAssociationClass()) {
                    fieldType = UmlgCollectionKindEnum.ORDERED_SET.getInterfacePathName();
                } else {
                    fieldType = UmlgCollectionKindEnum.ASSOCIATION_CLASS_ORDERED_SET.getInterfacePathName();
                }
            }
        } else if (isOrdered() && !isUnique()) {
            if (hasQualifiers()) {
                fieldType = UmlgCollectionKindEnum.QUALIFIED_SEQUENCE.getInterfacePathName();
            } else {
                if (ignoreAssociationClass || !isMemberOfAssociationClass()) {
                    fieldType = UmlgCollectionKindEnum.SEQUENCE.getInterfacePathName();
                } else {
                    fieldType = UmlgCollectionKindEnum.ASSOCIATION_CLASS_SEQUENCE.getInterfacePathName();
                }
            }
        } else if (!isOrdered() && !isUnique()) {
            if (hasQualifiers()) {
                fieldType = UmlgCollectionKindEnum.QUALIFIED_BAG.getInterfacePathName();
            } else {
                if (ignoreAssociationClass || !isMemberOfAssociationClass()) {
                    fieldType = UmlgCollectionKindEnum.BAG.getInterfacePathName();
                } else {
                    fieldType = UmlgCollectionKindEnum.ASSOCIATION_CLASS_BAG.getInterfacePathName();
                }
            }
        } else if (!isOrdered() && isUnique()) {
            if (hasQualifiers()) {
                fieldType = UmlgCollectionKindEnum.QUALIFIED_SET.getInterfacePathName();
            } else {
                if (ignoreAssociationClass || !isMemberOfAssociationClass()) {
                    fieldType = UmlgCollectionKindEnum.SET.getInterfacePathName();
                } else {
                    fieldType = UmlgCollectionKindEnum.ASSOCIATION_CLASS_SET.getInterfacePathName();
                }
            }
        } else {
            throw new RuntimeException("wtf");
        }
        fieldType.addToGenerics(javaBaseTypePath());
        if (!ignoreAssociationClass && isMemberOfAssociationClass()) {
            fieldType.addToGenerics(getAssociationClassPathName());
        }
        return fieldType;
    }

    /*
     * Attempting set semantics so the path is always a collection Call
     * javaBaseTypePath to get the type of set
     */
    public OJPathName javaTypePath() {
        OJPathName fieldType;
        if (!isOrdered() && isUnique()) {
            fieldType = UmlgCollectionKindEnum.SET.getInterfacePathName();
        } else if (isOrdered() && !isUnique()) {
            fieldType = UmlgCollectionKindEnum.SEQUENCE.getInterfacePathName();
        } else if (!isOrdered() && !isUnique()) {
            fieldType = UmlgCollectionKindEnum.BAG.getInterfacePathName();
        } else if (isOrdered() && isUnique()) {
            fieldType = UmlgCollectionKindEnum.ORDERED_SET.getInterfacePathName();
        } else {
            throw new RuntimeException("wtf");
        }
        fieldType.addToGenerics(javaBaseTypePath());
        return fieldType;
    }

    public OJPathName javaTypePathWithAssociationClass() {
        OJPathName fieldType;
        if (!isOrdered() && isUnique()) {
            fieldType = UmlgCollectionKindEnum.SET.getInterfacePathName();
        } else if (isOrdered() && !isUnique()) {
            fieldType = UmlgCollectionKindEnum.SEQUENCE.getInterfacePathName();
        } else if (!isOrdered() && !isUnique()) {
            fieldType = UmlgCollectionKindEnum.BAG.getInterfacePathName();
        } else if (isOrdered() && isUnique()) {
            fieldType = UmlgCollectionKindEnum.ORDERED_SET.getInterfacePathName();
        } else {
            throw new RuntimeException("wtf");
        }
        fieldType.addToGenerics(getAssociationClassPair());
        return fieldType;
    }

    public OJPathName getAssociationClassPair() {
        OJPathName pair = UmlgGenerationUtil.Pair.getCopy();
        pair.addToGenerics(javaBaseTypePath()).addToGenerics(UmlgClassOperations.getPathName(getAssociationClass()));
        return pair;
    }

    public String getAssociationClassFakePropertyName() {
        if (!isMemberOfAssociationClass()) {
            throw new IllegalStateException("Can not call getAssociationClassFakePropertyName on a property that does not belong to an AssociationClass!");
        }
        return fieldname() + "_" + new PropertyWrapper(getOtherEnd()).fieldname() + "_" + UmlgClassOperations.getPathName(getAssociationClass()).getLast();
    }

    public OJPathName javaTumlMemoryTypePath() {
        OJPathName memoryCollectionPathName = UmlgCollectionKindEnum.from(this).getMemoryCollection();
        memoryCollectionPathName.addToGenerics(UmlgClassOperations.getPathName(this.getType()));
        return memoryCollectionPathName;
    }

    public OJPathName javaAuditTypePath() {
        OJPathName fieldType;
        if (!isOrdered() && isUnique()) {
            fieldType = UmlgCollectionKindEnum.SET.getInterfacePathName();
        } else if (isOrdered() && !isUnique()) {
            fieldType = UmlgCollectionKindEnum.SEQUENCE.getInterfacePathName();
        } else if (!isOrdered() && !isUnique()) {
            fieldType = UmlgCollectionKindEnum.BAG.getInterfacePathName();
        } else if (isOrdered() && isUnique()) {
            fieldType = UmlgCollectionKindEnum.ORDERED_SET.getInterfacePathName();
        } else {
            throw new RuntimeException("wtf");
        }
        fieldType.addToGenerics(javaAuditBaseTypePath());
        return fieldType;
    }

    public String emptyCollection() {
        if (!isOrdered() && isUnique()) {
            return "UmlgCollections.emptySet()";
        } else if (isOrdered() && !isUnique()) {
            return "UmlgCollections.emptySequence()";
        } else if (!isOrdered() && !isUnique()) {
            return "UmlgCollections.emptyBag()";
        } else if (isOrdered() && isUnique()) {
            return "UmlgCollections.emptyOrderedSet()";
        } else {
            throw new RuntimeException("wtf");
        }
    }

    public OJPathName javaClosableIteratorTypePath() {
        OJPathName fieldType;
        if (!isOrdered() && isUnique()) {
            fieldType = UmlgCollectionKindEnum.SET.getClosableIteratorPathName();
        } else if (isOrdered() && !isUnique()) {
            fieldType = UmlgCollectionKindEnum.SEQUENCE.getClosableIteratorPathName();
        } else if (!isOrdered() && !isUnique()) {
            fieldType = UmlgCollectionKindEnum.BAG.getClosableIteratorPathName();
        } else if (isOrdered() && isUnique()) {
            fieldType = UmlgCollectionKindEnum.ORDERED_SET.getClosableIteratorPathName();
        } else {
            throw new RuntimeException("wtf");
        }
        fieldType.addToGenerics(javaBaseTypePath());
        return fieldType;
    }

    // public OJPathName javaAuditImplTypePath() {
    // OJPathName impl = javaImplTypePath().getCopy();
    // if (!impl.getGenerics().isEmpty()) {
    // impl.getGenerics().get(0).appendToTail("Audit");
    // }
    // return impl;
    // }

    public String javaDefaultInitialisation(BehavioredClassifier propertyConcreteOwner) {
        return javaDefaultInitialisation(propertyConcreteOwner, false);
    }

    /*
     * The property might be owned by an interface but the initialisation is for
     * a realization on a BehavioredClassifier
     */
    public String javaDefaultInitialisation(BehavioredClassifier propertyConcreteOwner, boolean ignoreAssociationClass) {
        return UmlgPropertyOperations.getDefaultTinkerCollectionInitalisation(this.property, propertyConcreteOwner, ignoreAssociationClass).getExpression();
    }

    public String javaDefaultInitialisationForAssociationClass(BehavioredClassifier propertyConcreteOwner) {
        return UmlgPropertyOperations.getDefaultTinkerCollectionInitalisationForAssociationClass(this.property, propertyConcreteOwner).getExpression();
    }

    public boolean isOne() {
        return UmlgPropertyOperations.isOne(this.property);
    }

    public boolean isUnqualifiedOne() {
        return UmlgPropertyOperations.isUnqualifiedOne(this.property);
    }

    public boolean isMany() {
        return UmlgPropertyOperations.isMany(this.property);
    }

    public boolean isUnqualifiedMany() {
        return UmlgPropertyOperations.isUnqualifiedMany(this.property);
    }

    public List<PropertyWrapper> getQualifiersAsPropertyWrappers() {
        List<PropertyWrapper> result = new ArrayList<PropertyWrapper>();
        for (Property q : this.property.getQualifiers()) {
            result.add(new PropertyWrapper(q));
        }
        return result;
    }

    @Override
    public boolean isReadOnly() {
        return this.property.isReadOnly();
    }

    @Override
    public void setIsReadOnly(boolean value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean isStatic() {
        return this.property.isStatic();
    }

    @Override
    public void setIsStatic(boolean value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public EList<Classifier> getFeaturingClassifiers() {
        return this.property.getFeaturingClassifiers();
    }

    @Override
    public Classifier getFeaturingClassifier(String name) {
        return this.property.getFeaturingClassifier(name);
    }

    @Override
    public Classifier getFeaturingClassifier(String name, boolean ignoreCase, EClass eClass) {
        return this.getFeaturingClassifier(name, ignoreCase, eClass);
    }

    @Override
    public boolean isLeaf() {
        return this.property.isLeaf();
    }

    @Override
    public void setIsLeaf(boolean value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public EList<RedefinableElement> getRedefinedElements() {
        return this.property.getRedefinedElements();
    }

    @Override
    public RedefinableElement getRedefinedElement(String name) {
        return this.property.getRedefinedElement(name);
    }

    @Override
    public RedefinableElement getRedefinedElement(String name, boolean ignoreCase, EClass eClass) {
        return this.getRedefinedElement(name, ignoreCase, eClass);
    }

    @Override
    public EList<Classifier> getRedefinitionContexts() {
        return this.property.getRedefinitionContexts();
    }

    @Override
    public Classifier getRedefinitionContext(String name) {
        return this.getRedefinitionContext(name);
    }

    @Override
    public Classifier getRedefinitionContext(String name, boolean ignoreCase, EClass eClass) {
        return this.property.getRedefinitionContext(name, ignoreCase, eClass);
    }

    @Override
    public boolean validateRedefinitionContextValid(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return this.property.validateRedefinitionContextValid(diagnostics, context);
    }

    @Override
    public boolean validateRedefinitionConsistent(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return this.property.validateRedefinitionConsistent(diagnostics, context);
    }

    @Override
    public boolean isRedefinitionContextValid(RedefinableElement redefined) {
        return this.property.isRedefinitionContextValid(redefined);
    }

    @Override
    public boolean isConsistentWith(RedefinableElement redefinee) {
        return this.property.isConsistentWith(redefinee);
    }

    @Override
    public String getName() {
        return this.property.getName();
    }

    @Override
    public void setName(String value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void unsetName() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean isSetName() {
        return this.property.isSetName();
    }

    @Override
    public VisibilityKind getVisibility() {
        return this.property.getVisibility();
    }

    @Override
    public void setVisibility(VisibilityKind value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void unsetVisibility() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean isSetVisibility() {
        return this.property.isSetVisibility();
    }

    @Override
    public String getQualifiedName() {
        if (isQualifier()) {
            return ((NamedElement)this.property.getOwner()).getQualifiedName() + "::" + this.property.getQualifiedName();
        } else {
            return this.property.getQualifiedName();
        }
    }

    public String getInverseName() {
        if (getOtherEnd() != null) {
            return getOtherEnd().getName();
        } else {
            return "inverseOf::" + this.property.getName();
        }
    }

    public String getInverseQualifiedName() {
        if (getOtherEnd() != null) {
            return getOtherEnd().getQualifiedName();
        } else {
            return "inverseOf::" + this.property.getQualifiedName();
        }
    }

    @Override
    public EList<Dependency> getClientDependencies() {
        return this.property.getClientDependencies();
    }

    @Override
    public Dependency getClientDependency(String name) {
        return this.property.getClientDependency(name);
    }

    @Override
    public Dependency getClientDependency(String name, boolean ignoreCase, EClass eClass) {
        return this.property.getClientDependency(name, ignoreCase, eClass);
    }

    @Override
    public Namespace getNamespace() {
        return this.property.getNamespace();
    }

    @Override
    public StringExpression getNameExpression() {
        return this.property.getNameExpression();
    }

    @Override
    public void setNameExpression(StringExpression value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public StringExpression createNameExpression(String name, Type type) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateHasNoQualifiedName(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return this.property.validateHasNoQualifiedName(diagnostics, context);
    }

    @Override
    public boolean validateHasQualifiedName(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateVisibilityNeedsOwnership(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public Dependency createDependency(NamedElement supplier) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public String getLabel() {
        return this.property.getLabel();
    }

    @Override
    public String getLabel(boolean localize) {
        return this.getLabel(localize);
    }

    @Override
    public Usage createUsage(NamedElement supplier) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public EList<Namespace> allNamespaces() {
        return this.property.allNamespaces();
    }

    @Override
    public boolean isDistinguishableFrom(NamedElement n, Namespace ns) {
        return this.property.isDistinguishableFrom(n, ns);
    }

    @Override
    public String separator() {
        return this.property.separator();
    }

    @Override
    public EList<Package> allOwningPackages() {
        return this.property.allOwningPackages();
    }

    @Override
    public Type getType() {
        return this.property.getType();
    }

    @Override
    public void setType(Type value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public EList<ConnectorEnd> getEnds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TemplateParameter getTemplateParameter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTemplateParameter(TemplateParameter value) {
        // TODO Auto-generated method stub
    }

    @Override
    public TemplateParameter getOwningTemplateParameter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOwningTemplateParameter(TemplateParameter value) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isCompatibleWith(ParameterableElement p) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTemplateParameter() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public EList<Deployment> getDeployments() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Deployment createDeployment(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Deployment getDeployment(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Deployment getDeployment(String name, boolean ignoreCase, boolean createOnDemand) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<PackageableElement> getDeployedElements() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PackageableElement getDeployedElement(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PackageableElement getDeployedElement(String name, boolean ignoreCase, EClass eClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DataType getDatatype() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDatatype(DataType value) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isDerived() {
        return this.property.isDerived();
    }

    @Override
    public void setIsDerived(boolean value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean isDerivedUnion() {
        return this.property.isDerivedUnion();
    }

    @Override
    public void setIsDerivedUnion(boolean value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public String getDefault() {
        return this.property.getDefault();
    }

    @Override
    public void setDefault(String value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void unsetDefault() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean isSetDefault() {
        return this.property.isSetDefault();
    }

    @Override
    public AggregationKind getAggregation() {
        return this.property.getAggregation();
    }

    @Override
    public void setAggregation(AggregationKind value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean isComposite() {
        return this.property.isComposite();
    }

    @Override
    public void setIsComposite(boolean value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public Class getClass_() {
        return this.property.getClass_();
    }

    @Override
    public EList<Property> getRedefinedProperties() {
        return this.property.getRedefinedProperties();
    }

    @Override
    public Property getRedefinedProperty(String name, Type type) {
        return this.property.getRedefinedProperty(name, type);
    }

    @Override
    public Property getRedefinedProperty(String name, Type type, boolean ignoreCase, EClass eClass) {
        return this.property.getRedefinedProperty(name, type, ignoreCase, eClass);
    }

    @Override
    public Association getOwningAssociation() {
        return this.property.getOwningAssociation();
    }

    @Override
    public void setOwningAssociation(Association value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public Association getAssociation() {
        return this.property.getAssociation();
    }

    @Override
    public void setAssociation(Association value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public ValueSpecification getDefaultValue() {
        return this.property.getDefaultValue();
    }

    @Override
    public void setDefaultValue(ValueSpecification value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public ValueSpecification createDefaultValue(String name, Type type, EClass eClass) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public Property getOpposite() {
        return this.property.getOpposite();
    }

    @Override
    public void setOpposite(Property value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public EList<Property> getSubsettedProperties() {
        return this.property.getSubsettedProperties();
    }

    @Override
    public Property getSubsettedProperty(String name, Type type) {
        return this.property.getSubsettedProperty(name, type);
    }

    @Override
    public Property getSubsettedProperty(String name, Type type, boolean ignoreCase, EClass eClass) {
        return this.property.getSubsettedProperty(name, type, ignoreCase, eClass);
    }

    @Override
    public EList<Property> getQualifiers() {
        return this.property.getQualifiers();
    }

    @Override
    public Property createQualifier(String name, Type type, EClass eClass) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public Property createQualifier(String name, Type type) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public Property getQualifier(String name, Type type) {
        return this.property.getQualifier(name, type);
    }

    @Override
    public Property getQualifier(String name, Type type, boolean ignoreCase, EClass eClass, boolean createOnDemand) {
        return this.property.getQualifier(name, type, ignoreCase, eClass, createOnDemand);
    }

    @Override
    public Property getAssociationEnd() {
        return this.property.getAssociationEnd();
    }

    @Override
    public void setAssociationEnd(Property value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateMultiplicityOfComposite(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateSubsettingContextConforms(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateRedefinedPropertyInherited(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateSubsettingRules(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateDerivedUnionIsDerived(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateDerivedUnionIsReadOnly(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateSubsettedPropertyNames(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateDeploymentTarget(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateBindingToAttribute(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void setIsNavigable(boolean isNavigable) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public Property getOtherEnd() {
        return this.property.getOtherEnd();
    }

    @Override
    public void setBooleanDefaultValue(boolean value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void setIntegerDefaultValue(int value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void setStringDefaultValue(String value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void setUnlimitedNaturalDefaultValue(int value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void setNullDefaultValue() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean isAttribute(Property p) {
        return this.property.isAttribute(p);
    }

    @Override
    public EList<Type> subsettingContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isNavigable() {
        return this.property.isNavigable();
    }

    public boolean isNumber() {
        return getType() instanceof PrimitiveType && (getType().getName().equals("Integer") || getType().getName().equals("UnlimitedNatural"));
    }

    public boolean isBoolean() {
        return getType() instanceof PrimitiveType && (getType().getName().equals("Boolean"));
    }

    public boolean isInteger() {
        return getType() instanceof PrimitiveType && (getType().getName().equals("Integer"));
    }

    public boolean isLong() {
        return getType() instanceof PrimitiveType && (getType().getName().equals("UnlimitedNatural"));
    }

    public String toString() {
        return this.property.toString();
    }

    public String lookup() {
        if (getOtherEnd() != null) {
            return "lookupFor_" + getOtherEnd().getName() + "_" + getName();
        } else {
            return "lookupFor_" + "_" + getName();
        }
    }

    public String lookupGetter() {
        return "getLookupFor_" + getOtherEnd().getName() + "_" + getName();
    }

    public String lookupOnCompositeParent() {
        if (getOtherEnd() != null) {
            return "lookupOnCompositeParentFor_" + getOtherEnd().getName() + "_" + this.property.getName();
        } else {
            return "lookupOnCompositeParentFor_" + "_" + this.property.getName();
        }
    }

    public String lookupOnCompositeParentGetter() {
        return "getLookupOnCompositeParentFor_" + getOtherEnd().getName() + "_" + this.property.getName();
    }

    public String lookupCompositeParent() {
        return "lookupFor_" + new PropertyWrapper(getOtherEnd()).getName() + "_" + getName() + "_CompositeParent";
    }

    public boolean isDate() {
        return getType() instanceof DataType && UmlgDataTypeOperation.isDate((DataType) getType());
    }

    public boolean isEmail() {
        return getType() instanceof DataType && UmlgDataTypeOperation.isEmail((DataType) getType());
    }

    public boolean isInternationalPhoneNumber() {
        return getType() instanceof DataType && UmlgDataTypeOperation.isInternationalPhoneNumber((DataType) getType());
    }

    public boolean isLocalPhoneNumber() {
        return getType() instanceof DataType && UmlgDataTypeOperation.isLocalPhoneNumber((DataType) getType());
    }

    public boolean isVideo() {
        return getType() instanceof DataType && UmlgDataTypeOperation.isVideo((DataType) getType());
    }

    public boolean isAudio() {
        return getType() instanceof DataType && UmlgDataTypeOperation.isAudio((DataType) getType());
    }

    public boolean isImage() {
        return getType() instanceof DataType && UmlgDataTypeOperation.isImage((DataType) getType());
    }

    public boolean isDateTime() {
        return getType() instanceof DataType && UmlgDataTypeOperation.isDateTime((DataType) getType());
    }

    public boolean isTime() {
        return getType() instanceof DataType && UmlgDataTypeOperation.isTime((DataType) getType());
    }

    public DataTypeEnum getDataTypeEnum() {
        if (getType() instanceof DataType && !(getType() instanceof PrimitiveType) && !(getType() instanceof Enumeration)) {
            return UmlgDataTypeOperation.getDataTypeEnum((DataType) getType());
        } else {
            return null;
        }
    }

    public boolean hasMaxLength() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.MaxLength.name());
        return property.isStereotypeApplied(stereotype);
    }

    public boolean hasValidation(UmlgValidationEnum umlgValidationEnum) {
        switch (umlgValidationEnum) {
            case MaxLength:
                return hasMaxLength();
            case MinLength:
                return hasMinLength();
            case RangeLength:
                return hasRangeLength();
            case Min:
                return hasMin();
            case Max:
                return hasMax();
            case Range:
                return hasRange();
            case URL:
                return hasUrl();
            case Email:
                return isEmail();
            default:
                break;
        }
        return false;

    }

    public List<Validation> getValidations() {
        List<Validation> result = new ArrayList<Validation>();
        List<Stereotype> stereoTypes = ModelLoader.INSTANCE.getStereotypes();
        for (Stereotype stereotype : stereoTypes) {
            if (property.isStereotypeApplied(stereotype)) {
                result.add(UmlgValidationEnum.fromStereotype(stereotype, this.property));
            }
        }
        if (getType() instanceof DataType) {
            Validation v = UmlgValidationEnum.fromDataType((DataType) getType());
            if (v != null) {
                result.add(v);
            }
        }
        return result;
    }

    public Validation getValidation(UmlgValidationEnum umlgValidationEnum) {
        switch (umlgValidationEnum) {
            case MaxLength:
                return getMaxLength();
            case MinLength:
                return getMinLength();
            case RangeLength:
                return getRangeLength();
            case Min:
                return getMin();
            case Max:
                return getMax();
            case Range:
                return getRange();
            case URL:
                return getUrl();
            case Email:
                return getEmail();
            default:
                break;
        }
        return null;
    }

    public MaxLength getMaxLength() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.MaxLength.name());
        return new MaxLength((Integer) property.getValue(stereotype, "length"));
    }

    public MinLength getMinLength() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.MinLength.name());
        return new MinLength((Integer) property.getValue(stereotype, "length"));
    }

    public RangeLength getRangeLength() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.RangeLength.name());
        return new RangeLength((Integer) property.getValue(stereotype, "min"), (Integer) property.getValue(stereotype, "max"));
    }

    public Max getMax() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.Max.name());
        return new Max((Integer) property.getValue(stereotype, "value"));
    }

    public Min getMin() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.Min.name());
        return new Min((Integer) property.getValue(stereotype, "value"));
    }

    public Range getRange() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.Range.name());
        return new Range((Integer) property.getValue(stereotype, "min"), (Integer) property.getValue(stereotype, "max"));
    }

    public Url getUrl() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.URL.name());
        return new Url((String) property.getValue(stereotype, "protocol"), (String) property.getValue(stereotype, "host"), (Integer) property.getValue(stereotype, "port"),
                (String) property.getValue(stereotype, "regexp"), (String) property.getValue(stereotype, "flags"));
    }

    public Email getEmail() {
        return new Email();
    }

    public boolean hasMinLength() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.MinLength.name());
        return property.isStereotypeApplied(stereotype);
    }

    public boolean hasRangeLength() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.RangeLength.name());
        return property.isStereotypeApplied(stereotype);
    }

    public boolean hasMin() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.Min.name());
        return property.isStereotypeApplied(stereotype);
    }

    public boolean hasMax() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.Max.name());
        return property.isStereotypeApplied(stereotype);
    }

    public boolean hasRange() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.Range.name());
        return property.isStereotypeApplied(stereotype);
    }

    public boolean hasUrl() {
        Stereotype stereotype = ModelLoader.INSTANCE.findStereotype(UmlgValidationEnum.URL.name());
        return property.isStereotypeApplied(stereotype);
    }

    @Override
    public boolean validateNonLeafRedefinition(DiagnosticChain arg0, Map<Object, Object> arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Interface getInterface() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isID() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setInterface(Interface arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setIsID(boolean arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setRealDefaultValue(double arg0) {
        // TODO Auto-generated method stub

    }

    public boolean isOtherEndComposite() {
        if (getOtherEnd() != null) {
            PropertyWrapper otherEnd = new PropertyWrapper(getOtherEnd());
            return otherEnd.isComposite();
        } else {
            return false;
        }
    }

    public boolean isMemberOfAssociationClass() {
        Element owner = this.property.getOwner();
        if (owner instanceof AssociationClass) {
            AssociationClass ownerClass = (AssociationClass) owner;
            List<Property> memberEnds = ownerClass.getMemberEnds();
            for (Property p : memberEnds) {
                if (p == this.property) {
                    return true;
                }
            }
            return false;
        } else if (owner instanceof Class) {
            Class ownerClass = (Class) owner;
            List<Association> associations = ownerClass.getAssociations();
            for (Association association : associations) {
                List<Property> memberEnds = association.getMemberEnds();
                for (Property p : memberEnds) {
                    if (p == this.property && (association instanceof AssociationClass)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public AssociationClass getAssociationClass() {
        Element owner = this.property.getOwner();
        if (owner instanceof AssociationClass) {
            return (AssociationClass) owner;
        } else if (owner instanceof Class) {
            Class ownerClass = (Class) owner;
            List<Association> associations = ownerClass.getAssociations();
            for (Association association : associations) {
                List<Property> memberEnds = association.getMemberEnds();
                for (Property p : memberEnds) {
                    if (p == this.property && (association instanceof AssociationClass)) {
                        return (AssociationClass) association;
                    }
                }
            }
            return null;
        }
        return null;
    }

    public OJPathName getAssociationClassPathName() {
        return UmlgClassOperations.getPathName(getAssociationClass());
    }

    public OJPathName getAssociationClassJavaTumlTypePath() {
        OJPathName fieldType;
        if (isOrdered() && isUnique()) {
            if (hasQualifiers()) {
                fieldType = UmlgCollectionKindEnum.QUALIFIED_ORDERED_SET.getInterfacePathName();
            } else {
                fieldType = UmlgCollectionKindEnum.ORDERED_SET.getInterfacePathName();
            }
        } else if (isOrdered() && !isUnique()) {
            if (hasQualifiers()) {
                fieldType = UmlgCollectionKindEnum.QUALIFIED_SEQUENCE.getInterfacePathName();
            } else {
                fieldType = UmlgCollectionKindEnum.SEQUENCE.getInterfacePathName();
            }
        } else if (!isOrdered() && !isUnique()) {
            if (hasQualifiers()) {
                fieldType = UmlgCollectionKindEnum.QUALIFIED_BAG.getInterfacePathName();
            } else {
                fieldType = UmlgCollectionKindEnum.BAG.getInterfacePathName();
            }
        } else if (!isOrdered() && isUnique()) {
            if (hasQualifiers()) {
                fieldType = UmlgCollectionKindEnum.QUALIFIED_SET.getInterfacePathName();
            } else {
                fieldType = UmlgCollectionKindEnum.SET.getInterfacePathName();
            }
        } else {
            throw new RuntimeException("wtf");
        }
        fieldType.addToGenerics(getAssociationClassPathName());
        return fieldType;
    }
}
