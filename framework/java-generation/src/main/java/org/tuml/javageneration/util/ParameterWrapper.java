package org.tuml.javageneration.util;

import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.ParameterEffectKind;
import org.eclipse.uml2.uml.ParameterSet;
import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.StringExpression;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;
import org.opaeum.java.metamodel.OJPathName;
import org.tuml.javageneration.naming.Namer;
import org.tuml.javageneration.ocl.util.TumlCollectionKindEnum;

public class ParameterWrapper extends MultiplicityWrapper implements Parameter {

	private Parameter parameter;

	public ParameterWrapper(Parameter parameter) {
		super(parameter);
		this.parameter = parameter;
	}
	
	public OJPathName javaTumlTypePath() {
		OJPathName fieldType;
		if (isOrdered() && isUnique()) {
			fieldType = TumlCollectionKindEnum.ORDERED_SET.getInterfacePathName();
		} else if (isOrdered() && !isUnique()) {
			fieldType = TumlCollectionKindEnum.SEQUENCE.getInterfacePathName();
		} else if (!isOrdered() && !isUnique()) {
			fieldType = TumlCollectionKindEnum.BAG.getInterfacePathName();
		} else if (!isOrdered() && isUnique()) {
			fieldType = TumlCollectionKindEnum.SET.getInterfacePathName();
		} else {
			throw new RuntimeException("wtf");
		}
		fieldType.addToGenerics(javaBaseTypePath());
		return fieldType;
	}

	public OJPathName javaBaseTypePath() {
		return new OJPathName(Namer.name(parameter.getType().getNearestPackage()) + "." + Namer.name(parameter.getType()));
	}

	@Override
	public EList<ConnectorEnd> getEnds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setType(Type value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
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
		return false;
	}

	@Override
	public VisibilityKind getVisibility() {
		// TODO Auto-generated method stub
		return null;
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
		return false;
	}

	@Override
	public String getQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EList<Dependency> getClientDependencies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dependency getClientDependency(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dependency getClientDependency(String name, boolean ignoreCase, EClass eClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Namespace getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringExpression getNameExpression() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNameExpression(StringExpression value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StringExpression createNameExpression(String name, Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validateHasNoQualifiedName(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateHasQualifiedName(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateVisibilityNeedsOwnership(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Dependency createDependency(NamedElement supplier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel(boolean localize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usage createUsage(NamedElement supplier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EList<Namespace> allNamespaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDistinguishableFrom(NamedElement n, Namespace ns) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String separator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EList<Package> allOwningPackages() {
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
	public EList<ParameterSet> getParameterSets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParameterSet getParameterSet(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParameterSet getParameterSet(String name, boolean ignoreCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Operation getOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParameterDirectionKind getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDirection(ParameterDirectionKind value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDefault() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefault(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsetDefault() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSetDefault() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ValueSpecification getDefaultValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultValue(ValueSpecification value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ValueSpecification createDefaultValue(String name, Type type, EClass eClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isException() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsException(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isStream() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsStream(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ParameterEffectKind getEffect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEffect(ParameterEffectKind value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsetEffect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSetEffect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateConnectorEnd(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateStreamAndException(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateNotException(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateReentrantBehaviors(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateInAndOut(DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBooleanDefaultValue(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIntegerDefaultValue(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStringDefaultValue(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUnlimitedNaturalDefaultValue(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNullDefaultValue() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRealDefaultValue(double value) {
		// TODO Auto-generated method stub
		
	}

}
