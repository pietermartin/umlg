package org.tuml.javageneration.util;

import java.lang.reflect.InvocationTargetException;
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
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.ValueSpecification;

public class MultiplicityWrapper implements MultiplicityElement {

	private MultiplicityElement multiplicityElement;

	public MultiplicityWrapper(MultiplicityElement multiplicityElement) {
		super();
		this.multiplicityElement = multiplicityElement;
	}

	public boolean isOne() {
		return TumlMultiplicityOperations.isOne(this.multiplicityElement);
	}

	public boolean isMany() {
		return TumlMultiplicityOperations.isMany(this.multiplicityElement);
	}

	@Override
	public EList<Element> getOwnedElements() {
		return this.multiplicityElement.getOwnedElements();
	}

	@Override
	public Element getOwner() {
		return this.multiplicityElement.getOwner();
	}

	@Override
	public EList<Comment> getOwnedComments() {
		return this.multiplicityElement.getOwnedComments();
	}

	@Override
	public Comment createOwnedComment() {
		return this.multiplicityElement.createOwnedComment();
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
		throw new RuntimeException("Not supported");
	}

	@Override
	public EObject getStereotypeApplication(Stereotype stereotype) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public EList<Stereotype> getRequiredStereotypes() {
		throw new RuntimeException("Not supported");
	}

	@Override
	public Stereotype getRequiredStereotype(String qualifiedName) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public EList<Stereotype> getAppliedStereotypes() {
		throw new RuntimeException("Not supported");
	}

	@Override
	public Stereotype getAppliedStereotype(String qualifiedName) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public EList<Stereotype> getAppliedSubstereotypes(Stereotype stereotype) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public Stereotype getAppliedSubstereotype(Stereotype stereotype, String qualifiedName) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public boolean hasValue(Stereotype stereotype, String propertyName) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public Object getValue(Stereotype stereotype, String propertyName) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public void setValue(Stereotype stereotype, String propertyName, Object newValue) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public EAnnotation createEAnnotation(String source) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public EList<Relationship> getRelationships() {
		throw new RuntimeException("Not supported");
	}

	@Override
	public EList<Relationship> getRelationships(EClass eClass) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public EList<DirectedRelationship> getSourceDirectedRelationships() {
		throw new RuntimeException("Not supported");
	}

	@Override
	public EList<DirectedRelationship> getSourceDirectedRelationships(EClass eClass) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public EList<DirectedRelationship> getTargetDirectedRelationships() {
		throw new RuntimeException("Not supported");
	}

	@Override
	public EList<DirectedRelationship> getTargetDirectedRelationships(EClass eClass) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public EList<String> getKeywords() {
		throw new RuntimeException("Not supported");
	}

	@Override
	public boolean addKeyword(String keyword) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public boolean removeKeyword(String keyword) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public Package getNearestPackage() {
		return this.multiplicityElement.getNearestPackage();
	}

	@Override
	public Model getModel() {
		return this.multiplicityElement.getModel();
	}

	@Override
	public boolean isStereotypeApplicable(Stereotype stereotype) {
		return this.multiplicityElement.isStereotypeApplicable(stereotype);
	}

	@Override
	public boolean isStereotypeRequired(Stereotype stereotype) {
		return this.multiplicityElement.isStereotypeRequired(stereotype);
	}

	@Override
	public boolean isStereotypeApplied(Stereotype stereotype) {
		return this.multiplicityElement.isStereotypeApplied(stereotype);
	}

	@Override
	public EObject applyStereotype(Stereotype stereotype) {
		return this.multiplicityElement.applyStereotype(stereotype);
	}

	@Override
	public EObject unapplyStereotype(Stereotype stereotype) {
		return this.multiplicityElement.unapplyStereotype(stereotype);
	}

	@Override
	public EList<Stereotype> getApplicableStereotypes() {
		return this.multiplicityElement.getApplicableStereotypes();
	}

	@Override
	public Stereotype getApplicableStereotype(String qualifiedName) {
		return this.multiplicityElement.getApplicableStereotype(qualifiedName);
	}

	@Override
	public boolean hasKeyword(String keyword) {
		return this.multiplicityElement.hasKeyword(keyword);
	}

	@Override
	public void destroy() {
		this.multiplicityElement.destroy();
	}

	@Override
	public EList<Element> allOwnedElements() {
		return this.multiplicityElement.allOwnedElements();
	}

	@Override
	public boolean mustBeOwned() {
		return this.multiplicityElement.mustBeOwned();
	}

	@Override
	public EAnnotation getEAnnotation(String arg0) {
		return this.multiplicityElement.getEAnnotation(arg0);
	}

	@Override
	public EList<EAnnotation> getEAnnotations() {
		return this.multiplicityElement.getEAnnotations();
	}

	@Override
	public TreeIterator<EObject> eAllContents() {
		return this.multiplicityElement.eAllContents();
	}

	@Override
	public EClass eClass() {
		return this.multiplicityElement.eClass();
	}

	@Override
	public EObject eContainer() {
		return this.multiplicityElement.eContainer();
	}

	@Override
	public EStructuralFeature eContainingFeature() {
		return this.multiplicityElement.eContainingFeature();
	}

	@Override
	public EReference eContainmentFeature() {
		return this.multiplicityElement.eContainmentFeature();
	}

	@Override
	public EList<EObject> eContents() {
		return this.multiplicityElement.eContents();
	}

	@Override
	public EList<EObject> eCrossReferences() {
		return this.multiplicityElement.eCrossReferences();
	}

	@Override
	public Object eGet(EStructuralFeature arg0) {
		return this.multiplicityElement.eGet(arg0);
	}

	@Override
	public Object eGet(EStructuralFeature arg0, boolean arg1) {
		return this.multiplicityElement.eGet(arg0, arg1);
	}

	@Override
	public Object eInvoke(EOperation arg0, EList<?> arg1) throws InvocationTargetException {
		return this.multiplicityElement.eInvoke(arg0, arg1);
	}

	@Override
	public boolean eIsProxy() {
		return this.multiplicityElement.eIsProxy();
	}

	@Override
	public boolean eIsSet(EStructuralFeature arg0) {
		return this.multiplicityElement.eIsSet(arg0);
	}

	@Override
	public Resource eResource() {
		return this.multiplicityElement.eResource();
	}

	@Override
	public void eSet(EStructuralFeature arg0, Object arg1) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public void eUnset(EStructuralFeature arg0) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public EList<Adapter> eAdapters() {
		return this.multiplicityElement.eAdapters();
	}

	@Override
	public boolean eDeliver() {
		return this.multiplicityElement.eDeliver();
	}

	@Override
	public void eNotify(Notification arg0) {
		this.multiplicityElement.eNotify(arg0);
	}

	@Override
	public void eSetDeliver(boolean arg0) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public boolean isOrdered() {
		return this.multiplicityElement.isOrdered();
	}

	@Override
	public void setIsOrdered(boolean value) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public boolean isUnique() {
		return this.multiplicityElement.isUnique();
	}

	@Override
	public void setIsUnique(boolean value) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public int getUpper() {
		return this.multiplicityElement.getUpper();
	}

	@Override
	public void setUpper(int value) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public int getLower() {
		return this.multiplicityElement.getLower();
	}

	@Override
	public void setLower(int value) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public ValueSpecification getUpperValue() {
		return this.multiplicityElement.getUpperValue();
	}

	@Override
	public void setUpperValue(ValueSpecification value) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public ValueSpecification createUpperValue(String name, Type type, EClass eClass) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public ValueSpecification getLowerValue() {
		return this.multiplicityElement.getLowerValue();
	}

	@Override
	public void setLowerValue(ValueSpecification value) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public ValueSpecification createLowerValue(String name, Type type, EClass eClass) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public boolean validateLowerGe0(DiagnosticChain diagnostics, Map<Object, Object> context) {
		return this.multiplicityElement.validateLowerGe0(diagnostics, context);
	}

	@Override
	public boolean validateUpperGeLower(DiagnosticChain diagnostics, Map<Object, Object> context) {
		return this.multiplicityElement.validateUpperGeLower(diagnostics, context);
	}

	@Override
	public boolean validateValueSpecificationNoSideEffects(DiagnosticChain diagnostics, Map<Object, Object> context) {
		return this.multiplicityElement.validateValueSpecificationNoSideEffects(diagnostics, context);
	}

	@Override
	public boolean validateValueSpecificationConstant(DiagnosticChain diagnostics, Map<Object, Object> context) {
		return this.multiplicityElement.validateValueSpecificationConstant(diagnostics, context);
	}

	@Override
	public boolean isMultivalued() {
		return this.multiplicityElement.isMultivalued();
	}

	@Override
	public boolean includesCardinality(int C) {
		return this.multiplicityElement.includesCardinality(C);
	}

	@Override
	public boolean includesMultiplicity(MultiplicityElement M) {
		return this.multiplicityElement.includesMultiplicity(M);
	}

	@Override
	public int lowerBound() {
		return this.multiplicityElement.lowerBound();
	}

	@Override
	public int upperBound() {
		return this.multiplicityElement.upperBound();
	}

	@Override
	public boolean compatibleWith(MultiplicityElement other) {
		return this.multiplicityElement.compatibleWith(other);
	}

	@Override
	public boolean is(int lowerbound, int upperbound) {
		return this.multiplicityElement.is(lowerbound, upperbound);
	}

}
