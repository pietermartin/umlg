package org.tuml.javageneration.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.CallConcurrencyKind;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterSet;
import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.RedefinableElement;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StringExpression;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.VisibilityKind;
import org.opaeum.java.metamodel.OJParameter;
import org.opaeum.java.metamodel.OJPathName;

public class OperationWrapper implements Operation {

	private Operation operation;

	public OperationWrapper(Operation oper) {
		this.operation = oper;
	}

	@Override
	public EList<Parameter> getOwnedParameters() {
		return this.operation.getOwnedParameters();
	}

	@Override
	public Parameter createOwnedParameter(String name, Type type) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Parameter getOwnedParameter(String name, Type type) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Parameter getOwnedParameter(String name, Type type, boolean ignoreCase, boolean createOnDemand) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean isAbstract() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setIsAbstract(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public EList<Behavior> getMethods() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Behavior getMethod(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Behavior getMethod(String name, boolean ignoreCase, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public CallConcurrencyKind getConcurrency() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setConcurrency(CallConcurrencyKind value) {
		// TODO Auto-generated method stub

	}

	@Override
	public EList<Type> getRaisedExceptions() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Type getRaisedException(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Type getRaisedException(String name, boolean ignoreCase, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<ParameterSet> getOwnedParameterSets() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public ParameterSet createOwnedParameterSet(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public ParameterSet getOwnedParameterSet(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public ParameterSet getOwnedParameterSet(String name, boolean ignoreCase, boolean createOnDemand) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Parameter createReturnResult(String name, Type type) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<ElementImport> getElementImports() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public ElementImport createElementImport(PackageableElement importedElement) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public ElementImport getElementImport(PackageableElement importedElement) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public ElementImport getElementImport(PackageableElement importedElement, boolean createOnDemand) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<PackageImport> getPackageImports() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public PackageImport createPackageImport(Package importedPackage) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public PackageImport getPackageImport(Package importedPackage) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public PackageImport getPackageImport(Package importedPackage, boolean createOnDemand) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Constraint> getOwnedRules() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint createOwnedRule(String name, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint createOwnedRule(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint getOwnedRule(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint getOwnedRule(String name, boolean ignoreCase, EClass eClass, boolean createOnDemand) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<NamedElement> getMembers() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public NamedElement getMember(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public NamedElement getMember(String name, boolean ignoreCase, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<PackageableElement> getImportedMembers() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public PackageableElement getImportedMember(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public PackageableElement getImportedMember(String name, boolean ignoreCase, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<NamedElement> getOwnedMembers() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public NamedElement getOwnedMember(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public NamedElement getOwnedMember(String name, boolean ignoreCase, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean validateMembersDistinguishable(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public ElementImport createElementImport(PackageableElement element, VisibilityKind visibility) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public PackageImport createPackageImport(Package package_, VisibilityKind visibility) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<PackageableElement> getImportedElements() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Package> getImportedPackages() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<String> getNamesOfMember(NamedElement element) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean membersAreDistinguishable() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<PackageableElement> importMembers(EList<PackageableElement> imps) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<PackageableElement> excludeCollisions(EList<PackageableElement> imps) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public String getName() {
		return this.operation.getName();
	}

	@Override
	public void setName(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsetName() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSetName() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public VisibilityKind getVisibility() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setVisibility(VisibilityKind value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsetVisibility() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSetVisibility() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public String getQualifiedName() {
		return this.operation.getQualifiedName();
	}

	@Override
	public EList<Dependency> getClientDependencies() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Dependency getClientDependency(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Dependency getClientDependency(String name, boolean ignoreCase, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Namespace getNamespace() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public StringExpression getNameExpression() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setNameExpression(StringExpression value) {
		// TODO Auto-generated method stub

	}

	@Override
	public StringExpression createNameExpression(String name, Type type) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean validateHasNoQualifiedName(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean validateHasQualifiedName(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean validateVisibilityNeedsOwnership(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Dependency createDependency(NamedElement supplier) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public String getLabel(boolean localize) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Usage createUsage(NamedElement supplier) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Namespace> allNamespaces() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean isDistinguishableFrom(NamedElement n, Namespace ns) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public String separator() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Package> allOwningPackages() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Element> getOwnedElements() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Element getOwner() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Comment> getOwnedComments() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Comment createOwnedComment() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean validateNotOwnSelf(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean validateHasOwner(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<EObject> getStereotypeApplications() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EObject getStereotypeApplication(Stereotype stereotype) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Stereotype> getRequiredStereotypes() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Stereotype getRequiredStereotype(String qualifiedName) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Stereotype> getAppliedStereotypes() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Stereotype getAppliedStereotype(String qualifiedName) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Stereotype> getAppliedSubstereotypes(Stereotype stereotype) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Stereotype getAppliedSubstereotype(Stereotype stereotype, String qualifiedName) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean hasValue(Stereotype stereotype, String propertyName) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Object getValue(Stereotype stereotype, String propertyName) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setValue(Stereotype stereotype, String propertyName, Object newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public EAnnotation createEAnnotation(String source) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Relationship> getRelationships() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Relationship> getRelationships(EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<DirectedRelationship> getSourceDirectedRelationships() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<DirectedRelationship> getSourceDirectedRelationships(EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<DirectedRelationship> getTargetDirectedRelationships() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<DirectedRelationship> getTargetDirectedRelationships(EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<String> getKeywords() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean addKeyword(String keyword) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean removeKeyword(String keyword) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Package getNearestPackage() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Model getModel() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean isStereotypeApplicable(Stereotype stereotype) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean isStereotypeRequired(Stereotype stereotype) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean isStereotypeApplied(Stereotype stereotype) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EObject applyStereotype(Stereotype stereotype) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EObject unapplyStereotype(Stereotype stereotype) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Stereotype> getApplicableStereotypes() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Stereotype getApplicableStereotype(String qualifiedName) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean hasKeyword(String keyword) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public EList<Element> allOwnedElements() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean mustBeOwned() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EAnnotation getEAnnotation(String arg0) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<EAnnotation> getEAnnotations() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public TreeIterator<EObject> eAllContents() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EClass eClass() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EObject eContainer() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EStructuralFeature eContainingFeature() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EReference eContainmentFeature() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<EObject> eContents() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<EObject> eCrossReferences() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Object eGet(EStructuralFeature arg0) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Object eGet(EStructuralFeature arg0, boolean arg1) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Object eInvoke(EOperation arg0, EList<?> arg1) throws InvocationTargetException {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean eIsProxy() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean eIsSet(EStructuralFeature arg0) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Resource eResource() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
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
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean eDeliver() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
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
	public boolean isStatic() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setIsStatic(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public EList<Classifier> getFeaturingClassifiers() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Classifier getFeaturingClassifier(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Classifier getFeaturingClassifier(String name, boolean ignoreCase, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setIsLeaf(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public EList<RedefinableElement> getRedefinedElements() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public RedefinableElement getRedefinedElement(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public RedefinableElement getRedefinedElement(String name, boolean ignoreCase, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Classifier> getRedefinitionContexts() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Classifier getRedefinitionContext(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Classifier getRedefinitionContext(String name, boolean ignoreCase, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean validateRedefinitionContextValid(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean validateRedefinitionConsistent(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean isRedefinitionContextValid(RedefinableElement redefined) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean isConsistentWith(RedefinableElement redefinee) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public TemplateParameter getTemplateParameter() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setTemplateParameter(TemplateParameter value) {
		// TODO Auto-generated method stub

	}

	@Override
	public TemplateParameter getOwningTemplateParameter() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setOwningTemplateParameter(TemplateParameter value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCompatibleWith(ParameterableElement p) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean isTemplateParameter() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<TemplateBinding> getTemplateBindings() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public TemplateBinding createTemplateBinding(TemplateSignature signature) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public TemplateBinding getTemplateBinding(TemplateSignature signature) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public TemplateBinding getTemplateBinding(TemplateSignature signature, boolean createOnDemand) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public TemplateSignature getOwnedTemplateSignature() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setOwnedTemplateSignature(TemplateSignature value) {
		// TODO Auto-generated method stub

	}

	@Override
	public TemplateSignature createOwnedTemplateSignature(EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public TemplateSignature createOwnedTemplateSignature() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<ParameterableElement> parameterableElements() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean isTemplate() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean isQuery() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setIsQuery(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOrdered() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setIsOrdered(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isUnique() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setIsUnique(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLower() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLower(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getUpper() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setUpper(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Class getClass_() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setClass_(Class value) {
		// TODO Auto-generated method stub

	}

	@Override
	public EList<Constraint> getPreconditions() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint createPrecondition(String name, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint createPrecondition(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint getPrecondition(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint getPrecondition(String name, boolean ignoreCase, EClass eClass, boolean createOnDemand) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Constraint> getPostconditions() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint createPostcondition(String name, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint createPostcondition(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint getPostcondition(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint getPostcondition(String name, boolean ignoreCase, EClass eClass, boolean createOnDemand) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public EList<Operation> getRedefinedOperations() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Operation getRedefinedOperation(String name, EList<String> ownedParameterNames, EList<Type> ownedParameterTypes) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Operation getRedefinedOperation(String name, EList<String> ownedParameterNames, EList<Type> ownedParameterTypes, boolean ignoreCase) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public DataType getDatatype() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setDatatype(DataType value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Constraint getBodyCondition() {
		return this.operation.getBodyCondition();
	}

	@Override
	public void setBodyCondition(Constraint value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Constraint createBodyCondition(String name, EClass eClass) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Constraint createBodyCondition(String name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setType(Type value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Interface getInterface() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void setInterface(Interface value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validateAtMostOneReturn(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean validateOnlyBodyForQuery(DiagnosticChain diagnostics, Map<Object, Object> context) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Parameter getReturnResult() {
		return this.operation.getReturnResult();
	}

	@Override
	public EList<Parameter> returnResult() {
		return this.operation.returnResult();
	}

	public OJPathName getReturnParamPathName() {
		ParameterWrapper parameterWrapper = new ParameterWrapper(getReturnResult());
		if (parameterWrapper.isMany()) {
			return parameterWrapper.javaTumlTypePath();
		} else {
			return parameterWrapper.javaBaseTypePath();
		}
	}

	public List<OJParameter> getOJParametersExceptReturn() {
		List<OJParameter> result = new ArrayList<OJParameter>();
		for (Parameter parameter : TumlOperationOperations.getParametersExceptReturn(this.operation)) {
			result.add(new OJParameter(parameter.getName(), TumlClassOperations.getPathName(parameter.getType())));
		}
		return result;
	}

	public String getOclBodyCondition() {
		if (getBodyCondition() == null) {
			throw new IllegalStateException(String.format("Property %s does not have a body condtion", new Object[] { this.getName() }));
		}
		Constraint bodyCondition = getBodyCondition();
		String ocl = TumlConstraintOperations.getAsOcl(bodyCondition);
		return ocl;
	}

	@Override
	public boolean validateNonLeafRedefinition(DiagnosticChain arg0, Map<Object, Object> arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
