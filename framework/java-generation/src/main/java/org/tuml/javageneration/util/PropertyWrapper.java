package org.tuml.javageneration.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Deployment;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.RedefinableElement;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StringExpression;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;
import org.opaeum.java.metamodel.OJPathName;
import org.tuml.javageneration.naming.Namer;
import org.tuml.javageneration.ocl.TumlOcl2Java;

public class PropertyWrapper implements Property {

    private Property property;

    public PropertyWrapper(Property property) {
        super();
        this.property = property;
    }

    public Type getOwningType() {
        return TumlPropertyOperations.getOwningType(this.property);

    }

    public boolean isControllingSide() {
        return TumlPropertyOperations.isControllingSide(this.property);
    }

    public String fieldname() {
        return this.property.getName();
    }

    public String getter() {
        return TumlPropertyOperations.getter(this.property);
    }

    public String setter() {
        return TumlPropertyOperations.setter(this.property);
    }

    public boolean isPrimitive() {
        return TumlPropertyOperations.isPrimitive(this.property);
    }

    public boolean isEnumeration() {
        return TumlPropertyOperations.isEnumeration(this.property);
    }

    public boolean isOneToMany() {
        return TumlPropertyOperations.isOneToMany(this.property);
    }

    public boolean isManyToOne() {
        return TumlPropertyOperations.isManyToOne(this.property);
    }

    public boolean isManyToMany() {
        return TumlPropertyOperations.isManyToMany(this.property);
    }

    public boolean isOneToOne() {
        return TumlPropertyOperations.isOneToOne(this.property);
    }

    public String adder() {
        return TumlPropertyOperations.adder(this.property);
    }

    public String remover() {
        return TumlPropertyOperations.remover(this.property);
    }

    public String clearer() {
        return TumlPropertyOperations.clearer(this.property);
    }

    public String internalRemover() {
        return TumlPropertyOperations.internalRemover(this.property);
    }

    public String internalAdder() {
        return TumlPropertyOperations.internalAdder(this.property);
    }

    public OJPathName javaBaseTypePath() {
        return TumlPropertyOperations.getTypePath(this.property);
    }

    /*
     * Attempting set semantics so the path is always a collection Call
     * javaBaseTypePath to get the type of set This method return tuml special
     * collection interface
     */
    public OJPathName javaTumlTypePath() {
        OJPathName fieldType;
        if (isOrdered() && isUnique()) {
            if (hasQualifiers()) {
                fieldType = TinkerGenerationUtil.tinkerQualifiedOrderedSet.getCopy();
            } else {
                fieldType = TinkerGenerationUtil.tinkerOrderedSet.getCopy();
            }
        } else if (isOrdered() && !isUnique()) {
            if (hasQualifiers()) {
                fieldType = TinkerGenerationUtil.tinkerQualifiedSequence.getCopy();
            } else {
                fieldType = TinkerGenerationUtil.tinkerSequence.getCopy();
            }
        } else if (!isOrdered() && !isUnique()) {
            if (hasQualifiers()) {
                fieldType = TinkerGenerationUtil.tinkerQualifiedBag.getCopy();
            } else {
                fieldType = TinkerGenerationUtil.tinkerBag.getCopy();
            }
        } else if (!isOrdered() && isUnique()) {
            if (hasQualifiers()) {
                fieldType = TinkerGenerationUtil.tinkerQualifiedSet.getCopy();
            } else {
                fieldType = TinkerGenerationUtil.tinkerSet.getCopy();
            }
        } else {
            throw new RuntimeException("wtf");
        }
        fieldType.addToGenerics(javaBaseTypePath());
        return fieldType;
    }

    /*
     * Attempting set semantics so the path is always a collection Call
     * javaBaseTypePath to get the type of set
     */
    public OJPathName javaTypePath() {
        OJPathName fieldType;
        if (!isOrdered() && isUnique()) {
            fieldType = TinkerGenerationUtil.tinkerSet.getCopy();
        } else if (isOrdered() && !isUnique()) {
            fieldType = TinkerGenerationUtil.tinkerSequence.getCopy();
        } else if (!isOrdered() && !isUnique()) {
            fieldType = TinkerGenerationUtil.tinkerBag.getCopy();
        } else if (isOrdered() && isUnique()) {
            fieldType = TinkerGenerationUtil.tinkerOrderedSet.getCopy();
        } else {
            throw new RuntimeException("wtf");
        }
        fieldType.addToGenerics(javaBaseTypePath());
        return fieldType;
    }

    public OJPathName javaImplTypePath() {
        return TumlPropertyOperations.getDefaultTinkerCollection(this.property);
    }

    /*
     * The property might be owned by an interface but the initialisation is for
     * a realization on a BehavioredClassifier
     */
    public String javaDefaultInitialisation(BehavioredClassifier propertyConcreteOwner) {
        return TumlPropertyOperations.getDefaultTinkerCollectionInitalisation(this.property, propertyConcreteOwner).getExpression();
    }

    public boolean isOne() {
        return TumlPropertyOperations.isOne(this.property);
    }

    public boolean isMany() {
        return TumlPropertyOperations.isMany(this.property);
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
        return this.property.getQualifiedName();
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
    public EList<Element> getOwnedElements() {
        return this.property.getOwnedElements();
    }

    @Override
    public Element getOwner() {
        return this.property.getOwner();
    }

    @Override
    public EList<Comment> getOwnedComments() {
        return this.property.getOwnedComments();
    }

    @Override
    public Comment createOwnedComment() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateNotOwnSelf(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean validateHasOwner(DiagnosticChain diagnostics, Map<Object, Object> context) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public EList<EObject> getStereotypeApplications() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EObject getStereotypeApplication(Stereotype stereotype) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<Stereotype> getRequiredStereotypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Stereotype getRequiredStereotype(String qualifiedName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<Stereotype> getAppliedStereotypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Stereotype getAppliedStereotype(String qualifiedName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<Stereotype> getAppliedSubstereotypes(Stereotype stereotype) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Stereotype getAppliedSubstereotype(Stereotype stereotype, String qualifiedName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasValue(Stereotype stereotype, String propertyName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object getValue(Stereotype stereotype, String propertyName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setValue(Stereotype stereotype, String propertyName, Object newValue) {
        // TODO Auto-generated method stub
    }

    @Override
    public EAnnotation createEAnnotation(String source) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public EList<Relationship> getRelationships() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<Relationship> getRelationships(EClass eClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<DirectedRelationship> getSourceDirectedRelationships() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<DirectedRelationship> getSourceDirectedRelationships(EClass eClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<DirectedRelationship> getTargetDirectedRelationships() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<DirectedRelationship> getTargetDirectedRelationships(EClass eClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<String> getKeywords() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addKeyword(String keyword) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeKeyword(String keyword) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Package getNearestPackage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Model getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isStereotypeApplicable(Stereotype stereotype) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isStereotypeRequired(Stereotype stereotype) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isStereotypeApplied(Stereotype stereotype) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public EObject applyStereotype(Stereotype stereotype) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EObject unapplyStereotype(Stereotype stereotype) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<Stereotype> getApplicableStereotypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Stereotype getApplicableStereotype(String qualifiedName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasKeyword(String keyword) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public EList<Element> allOwnedElements() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean mustBeOwned() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public EAnnotation getEAnnotation(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<EAnnotation> getEAnnotations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TreeIterator<EObject> eAllContents() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EClass eClass() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EObject eContainer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EStructuralFeature eContainingFeature() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EReference eContainmentFeature() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<EObject> eContents() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EList<EObject> eCrossReferences() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object eGet(EStructuralFeature arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object eGet(EStructuralFeature arg0, boolean arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object eInvoke(EOperation arg0, EList<?> arg1) throws InvocationTargetException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean eIsProxy() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean eIsSet(EStructuralFeature arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Resource eResource() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void eSet(EStructuralFeature arg0, Object arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void eUnset(EStructuralFeature arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public EList<Adapter> eAdapters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean eDeliver() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void eNotify(Notification arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void eSetDeliver(boolean arg0) {
        // TODO Auto-generated method stub
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
    public boolean isOrdered() {
        return this.property.isOrdered();
    }

    public boolean isInverseOrdered() {
        return getOtherEnd() != null && getOtherEnd().isOrdered();
    }

    @Override
    public void setIsOrdered(boolean value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean isUnique() {
        return this.property.isUnique();
    }

    @Override
    public void setIsUnique(boolean value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public int getUpper() {
        return this.property.getUpper();
    }

    @Override
    public void setUpper(int value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public int getLower() {
        return this.property.getLower();
    }

    @Override
    public void setLower(int value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public ValueSpecification getUpperValue() {
        return null;
    }

    @Override
    public void setUpperValue(ValueSpecification value) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public ValueSpecification createUpperValue(String name, Type type, EClass eClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValueSpecification getLowerValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLowerValue(ValueSpecification value) {
        // TODO Auto-generated method stub
    }

    @Override
    public ValueSpecification createLowerValue(String name, Type type, EClass eClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean validateLowerGe0(DiagnosticChain diagnostics, Map<Object, Object> context) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean validateUpperGeLower(DiagnosticChain diagnostics, Map<Object, Object> context) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean validateValueSpecificationNoSideEffects(DiagnosticChain diagnostics, Map<Object, Object> context) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean validateValueSpecificationConstant(DiagnosticChain diagnostics, Map<Object, Object> context) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMultivalued() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean includesCardinality(int C) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean includesMultiplicity(MultiplicityElement M) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int lowerBound() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int upperBound() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean compatibleWith(MultiplicityElement other) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean is(int lowerbound, int upperbound) {
        // TODO Auto-generated method stub
        return false;
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
            sb.append(getOclDerivedValue(derived));
        } else {
            sb.append(getOclDerivedValue(this.property));
        }
        return sb.toString();
    }

    private String getOclDerivedValue(Property p) {
        PropertyWrapper pWrap = new PropertyWrapper(p);
        StringBuilder sb = new StringBuilder();
        sb.append("package ");
        sb.append(Namer.nameIncludingModel(pWrap.getOwningType().getNearestPackage()).replace(".", "::"));
        sb.append("\n    context ");
        sb.append(pWrap.getOwningType().getName());
        sb.append("::");
        sb.append(getName());
        sb.append(" : ");

        if (pWrap.isMany()) {
            sb.append(TumlOcl2Java.getCollectionInterface(pWrap));
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
        return TumlClassOperations.getPathName(getType());
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

    public String getQualifiedNameFor(PropertyWrapper qualifer) {
    	return getter() + "For" + StringUtils.capitalize(qualifer.getName());
    }

    public String emptyCollection() {
        if (isUnique()) {
            return "Collections.emptySet()";
        } else {
            return "Collections.emptyList()";
        }
    }

    public Property getProperty() {
        return property;
    }

    public String getTumlRuntimePropertyEnum() {
        return TumlClassOperations.propertyEnumName(getOwningType()) + "." + fieldname();
    }

    @Override
    public boolean validateNonLeafRedefinition(DiagnosticChain arg0, Map<Object, Object> arg1) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public Interface getInterface() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean isID() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void setInterface(Interface arg0) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void setIsID(boolean arg0) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void setRealDefaultValue(double arg0) {
        throw new RuntimeException("Not supported");
    }

    public String getInitValue() {
        ValueSpecification v = getDefaultValue();
        if (v instanceof OpaqueExpression) {
            return getOclDefaultValue();
        } else if (v instanceof LiteralString) {
            LiteralString expr = (LiteralString) v;
            String result = expr.getValue();
            return result.replaceAll("^\"|\"$", "");
        } else {
            throw new RuntimeException("Not supported");
        }
    }
}
