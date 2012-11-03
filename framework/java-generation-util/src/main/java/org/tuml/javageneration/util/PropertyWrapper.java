package org.tuml.javageneration.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Deployment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.RedefinableElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StringExpression;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;
import org.opaeum.java.metamodel.OJPathName;
import org.tuml.framework.ModelLoader;
import org.tuml.javageneration.validation.Max;
import org.tuml.javageneration.validation.MaxLength;
import org.tuml.javageneration.validation.Min;
import org.tuml.javageneration.validation.MinLength;
import org.tuml.javageneration.validation.Range;
import org.tuml.javageneration.validation.RangeLength;
import org.tuml.javageneration.validation.Url;
import org.tuml.javageneration.validation.Validation;

public class PropertyWrapper extends MultiplicityWrapper implements Property {

	private Property property;

	public PropertyWrapper(Property property) {
		super(property);
		this.property = property;
	}

	public boolean isDataType() {
		return getType() instanceof DataType;
	}

	public boolean hasLookup() {
		if (!isComposite() && !(getType() instanceof Enumeration) && !isDerived() && !isQualifier() && getOtherEnd() != null && !(getOtherEnd().getType() instanceof Enumeration)
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
			throw new IllegalStateException(String.format("Property %s does not have a default value", new Object[] { this.getName() }));
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
			sb.append(TinkerGenerationUtil.getCollectionInterface(pWrap));
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
			throw new IllegalStateException(String.format("Property %s does not have a default value", new Object[] { this.getName() }));
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
		return TumlClassOperations.propertyEnumName(getOwningType()) + "." + fieldname();
	}

	public String getInitValue() {
		ValueSpecification v = getDefaultValue();
		if (v instanceof OpaqueExpression) {
			if (hasOclDefaultValue()) {
				return getOclDefaultValue();
			} else {
				return getDefaultValue().stringValue();
			}
		} else if (v instanceof LiteralString) {
			LiteralString expr = (LiteralString) v;
			String result = expr.getValue();
			return result.replaceAll("^\"|\"$", "");
		} else {
			throw new RuntimeException("Not supported");
		}
	}

	/**
	 * 
	 * @param stereotype
	 * @return Returns true is the property's owner is an association
	 *         stereotyped with <<QualifierAssociation>>
	 */
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
		return !this.property.isDerived() && this.property.getType() instanceof org.eclipse.uml2.uml.Class && isOne() && this.property.isComposite()
				&& this.property.getLower() == 1;
	}

	public boolean isInverseOrdered() {
		return getOtherEnd() != null && getOtherEnd().isOrdered();
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

	public String lookupOnCompositeParent() {
		return getOtherEnd().getName() + "_" + this.property.getName();
	}

	public String getter() {
		return TumlPropertyOperations.getter(this.property);
	}

	public String setter() {
		return TumlPropertyOperations.setter(this.property);
	}

	public String validator() {
		return TumlPropertyOperations.validator(this.property);
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

	public OJPathName javaAuditBaseTypePath() {
		if (!isPrimitive() && !isEnumeration()) {
			return TumlPropertyOperations.getTypePath(this.property).appendToTail("Audit");
		} else {
			return TumlPropertyOperations.getTypePath(this.property);
		}
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
				fieldType = TumlCollectionKindEnum.QUALIFIED_ORDERED_SET.getInterfacePathName();
			} else {
				fieldType = TumlCollectionKindEnum.ORDERED_SET.getInterfacePathName();
			}
		} else if (isOrdered() && !isUnique()) {
			if (hasQualifiers()) {
				fieldType = TumlCollectionKindEnum.QUALIFIED_SEQUENCE.getInterfacePathName();
			} else {
				fieldType = TumlCollectionKindEnum.SEQUENCE.getInterfacePathName();
			}
		} else if (!isOrdered() && !isUnique()) {
			if (hasQualifiers()) {
				fieldType = TumlCollectionKindEnum.QUALIFIED_BAG.getInterfacePathName();
			} else {
				fieldType = TumlCollectionKindEnum.BAG.getInterfacePathName();
			}
		} else if (!isOrdered() && isUnique()) {
			if (hasQualifiers()) {
				fieldType = TumlCollectionKindEnum.QUALIFIED_SET.getInterfacePathName();
			} else {
				fieldType = TumlCollectionKindEnum.SET.getInterfacePathName();
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
			fieldType = TumlCollectionKindEnum.SET.getInterfacePathName();
		} else if (isOrdered() && !isUnique()) {
			fieldType = TumlCollectionKindEnum.SEQUENCE.getInterfacePathName();
		} else if (!isOrdered() && !isUnique()) {
			fieldType = TumlCollectionKindEnum.BAG.getInterfacePathName();
		} else if (isOrdered() && isUnique()) {
			fieldType = TumlCollectionKindEnum.ORDERED_SET.getInterfacePathName();
		} else {
			throw new RuntimeException("wtf");
		}
		fieldType.addToGenerics(javaBaseTypePath());
		return fieldType;
	}

	public OJPathName javaTumlMemoryTypePath() {
		OJPathName memoryCollectionPathName = TumlCollectionKindEnum.from(this).getMemoryCollection();
		memoryCollectionPathName.addToGenerics(TumlClassOperations.getPathName(this.getType()));
		return memoryCollectionPathName;
	}

	public OJPathName javaAuditTypePath() {
		OJPathName fieldType;
		if (!isOrdered() && isUnique()) {
			fieldType = TumlCollectionKindEnum.SET.getInterfacePathName();
		} else if (isOrdered() && !isUnique()) {
			fieldType = TumlCollectionKindEnum.SEQUENCE.getInterfacePathName();
		} else if (!isOrdered() && !isUnique()) {
			fieldType = TumlCollectionKindEnum.BAG.getInterfacePathName();
		} else if (isOrdered() && isUnique()) {
			fieldType = TumlCollectionKindEnum.ORDERED_SET.getInterfacePathName();
		} else {
			throw new RuntimeException("wtf");
		}
		fieldType.addToGenerics(javaAuditBaseTypePath());
		return fieldType;
	}

	public String emptyCollection() {
		if (!isOrdered() && isUnique()) {
			return "TumlCollections.emptySet()";
		} else if (isOrdered() && !isUnique()) {
			return "TumlCollections.emptySequence()";
		} else if (!isOrdered() && !isUnique()) {
			return "TumlCollections.emptyBag()";
		} else if (isOrdered() && isUnique()) {
			return "TumlCollections.emptyOrderedSet()";
		} else {
			throw new RuntimeException("wtf");
		}
	}

	public OJPathName javaClosableIteratorTypePath() {
		OJPathName fieldType;
		if (!isOrdered() && isUnique()) {
			fieldType = TumlCollectionKindEnum.SET.getClosableIteratorPathName();
		} else if (isOrdered() && !isUnique()) {
			fieldType = TumlCollectionKindEnum.SEQUENCE.getClosableIteratorPathName();
		} else if (!isOrdered() && !isUnique()) {
			fieldType = TumlCollectionKindEnum.BAG.getClosableIteratorPathName();
		} else if (isOrdered() && isUnique()) {
			fieldType = TumlCollectionKindEnum.ORDERED_SET.getClosableIteratorPathName();
		} else {
			throw new RuntimeException("wtf");
		}
		fieldType.addToGenerics(javaBaseTypePath());
		return fieldType;
	}

	public OJPathName javaImplTypePath() {
		return TumlPropertyOperations.getDefaultTinkerCollection(this.property);
	}

	// public OJPathName javaAuditImplTypePath() {
	// OJPathName impl = javaImplTypePath().getCopy();
	// if (!impl.getGenerics().isEmpty()) {
	// impl.getGenerics().get(0).appendToTail("Audit");
	// }
	// return impl;
	// }

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

	public boolean isUnqualifiedOne() {
		return TumlPropertyOperations.isUnqualifiedOne(this.property);
	}

	public boolean isMany() {
		return TumlPropertyOperations.isMany(this.property);
	}

	public boolean isUnqualifiedMany() {
		return TumlPropertyOperations.isUnqualifiedMany(this.property);
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
		return this.property.getQualifiedName();
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
		return "lookupFor_" + new PropertyWrapper(getOtherEnd()).getName() + "_" + getName();
	}

	public String lookupCompositeParent() {
		return "lookupFor_" + new PropertyWrapper(getOtherEnd()).getName() + "_" + getName() + "_CompositeParent";
	}

	public boolean isDate() {
		return getType() instanceof DataType && TumlDataTypeOperation.isDate((DataType) getType());
	}

	public boolean isEmail() {
		return getType() instanceof DataType && TumlDataTypeOperation.isEmail((DataType) getType());
	}

	public boolean isInternationalPhoneNumber() {
		return getType() instanceof DataType && TumlDataTypeOperation.isInternationalPhoneNumber((DataType) getType());
	}

	public boolean isLocalPhoneNumber() {
		return getType() instanceof DataType && TumlDataTypeOperation.isLocalPhoneNumber((DataType) getType());
	}

	public boolean isVideo() {
		return getType() instanceof DataType && TumlDataTypeOperation.isVideo((DataType) getType());
	}

	public boolean isAudio() {
		return getType() instanceof DataType && TumlDataTypeOperation.isAudio((DataType) getType());
	}

	public boolean isImage() {
		return getType() instanceof DataType && TumlDataTypeOperation.isImage((DataType) getType());
	}

	public boolean isDateTime() {
		return getType() instanceof DataType && TumlDataTypeOperation.isDateTime((DataType) getType());
	}

	public boolean isTime() {
		return getType() instanceof DataType && TumlDataTypeOperation.isTime((DataType) getType());
	}

	public DataTypeEnum getDataTypeEnum() {
		if (getType() instanceof DataType && !(getType() instanceof PrimitiveType) && !(getType() instanceof Enumeration)) {
			return TumlDataTypeOperation.getDataTypeEnum((DataType) getType());
		} else {
			return null;
		}
	}

	public boolean hasMaxLength() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.MaxLength.name());
		return property.isStereotypeApplied(stereotype);
	}

	public boolean hasValidation(TumlValidationEnum tumlValidationEnum) {
		switch (tumlValidationEnum) {
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
		default:
			break;
		}
		return false;

	}

	public List<Validation> getValidations() {
		List<Validation> result = new ArrayList<Validation>();
		List<Stereotype> stereoTypes = ModelLoader.getStereotypes();
		for (Stereotype stereotype : stereoTypes) {
			if (property.isStereotypeApplied(stereotype)) {
				result.add(TumlValidationEnum.fromStereotype(stereotype, this.property));
			}
		}
		return result;
	}

	public Validation getValidation(TumlValidationEnum tumlValidationEnum) {
		switch (tumlValidationEnum) {
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
		default:
			break;
		}
		return null;
	}

	public MaxLength getMaxLength() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.MaxLength.name());
		return new MaxLength((Integer) property.getValue(stereotype, "length"));
	}

	public MinLength getMinLength() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.MinLength.name());
		return new MinLength((Integer) property.getValue(stereotype, "length"));
	}

	public RangeLength getRangeLength() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.RangeLength.name());
		return new RangeLength((Integer) property.getValue(stereotype, "min"), (Integer) property.getValue(stereotype, "max"));
	}

	public Max getMax() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.Max.name());
		return new Max((Integer) property.getValue(stereotype, "value"));
	}

	public Min getMin() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.Min.name());
		return new Min((Integer) property.getValue(stereotype, "value"));
	}

	public Range getRange() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.Range.name());
		return new Range((Integer) property.getValue(stereotype, "min"), (Integer) property.getValue(stereotype, "max"));
	}

	public Url getUrl() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.URL.name());
		return new Url((String) property.getValue(stereotype, "protocol"), (String) property.getValue(stereotype, "host"), (Integer) property.getValue(stereotype, "port"),
				(String) property.getValue(stereotype, "regexp"), (String) property.getValue(stereotype, "flags"));
	}

	public boolean hasMinLength() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.MinLength.name());
		return property.isStereotypeApplied(stereotype);
	}

	public boolean hasRangeLength() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.RangeLength.name());
		return property.isStereotypeApplied(stereotype);
	}

	public boolean hasMin() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.Min.name());
		return property.isStereotypeApplied(stereotype);
	}

	public boolean hasMax() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.Max.name());
		return property.isStereotypeApplied(stereotype);
	}

	public boolean hasRange() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.Range.name());
		return property.isStereotypeApplied(stereotype);
	}

	public boolean hasUrl() {
		Stereotype stereotype = ModelLoader.findStereotype(TumlValidationEnum.URL.name());
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

}
