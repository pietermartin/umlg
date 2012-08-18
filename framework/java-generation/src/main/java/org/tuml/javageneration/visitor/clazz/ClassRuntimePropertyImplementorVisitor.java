package org.tuml.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJSimpleStatement;
import org.opaeum.java.metamodel.OJSwitchCase;
import org.opaeum.java.metamodel.OJSwitchStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ClassRuntimePropertyImplementorVisitor extends BaseVisitor implements Visitor<Class> {

	public ClassRuntimePropertyImplementorVisitor(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		addInitialiseProperty(annotatedClass, clazz);
		RuntimePropertyImplementor.addTumlRuntimePropertyEnum(annotatedClass, TumlClassOperations.propertyEnumName(clazz), TumlClassOperations.className(clazz),
				TumlClassOperations.getAllOwnedProperties(clazz), TumlClassOperations.hasCompositeOwner(clazz), clazz.getModel().getName());
		addGetQualifiers(annotatedClass, clazz);
		addGetSize(annotatedClass, clazz);
	}

	@Override
	public void visitAfter(Class clazz) {
	}

	private void addInitialiseProperty(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation initialiseProperty = new OJAnnotatedOperation("initialiseProperty");
		TinkerGenerationUtil.addOverrideAnnotation(initialiseProperty);
		initialiseProperty.addParam("tumlRuntimeProperty", TinkerGenerationUtil.tumlRuntimePropertyPathName.getCopy());
		if (!clazz.getGeneralizations().isEmpty()) {
			initialiseProperty.getBody().addToStatements("super.initialiseProperty(tumlRuntimeProperty)");
		}
		annotatedClass.addToOperations(initialiseProperty);

		OJSwitchStatement ojSwitchStatement = new OJSwitchStatement();
		ojSwitchStatement.setCondition("(" + TumlClassOperations.propertyEnumName(clazz) + ".fromLabel(tumlRuntimeProperty.getLabel()))");
		initialiseProperty.getBody().addToStatements(ojSwitchStatement);

		for (Property p : TumlClassOperations.getAllOwnedProperties(clazz)) {
			PropertyWrapper pWrap = new PropertyWrapper(p);
			if (!(pWrap.isDerived() || pWrap.isDerivedUnion())) {
				OJSwitchCase ojSwitchCase = new OJSwitchCase();
				ojSwitchCase.setLabel(pWrap.fieldname());
				OJSimpleStatement statement = new OJSimpleStatement("this." + pWrap.fieldname() + " = " + pWrap.javaDefaultInitialisation(clazz));
				statement.setName(pWrap.fieldname());
				ojSwitchCase.getBody().addToStatements(statement);
				annotatedClass.addToImports(pWrap.javaImplTypePath());
				ojSwitchStatement.addToCases(ojSwitchCase);
			}
		}
	}

	private void addGetQualifiers(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation getQualifiers = new OJAnnotatedOperation("getQualifiers");
		TinkerGenerationUtil.addOverrideAnnotation(getQualifiers);
		getQualifiers.setComment("getQualifiers is called from the collection in order to update the index used to implement the qualifier");
		getQualifiers.addParam("tumlRuntimeProperty", TinkerGenerationUtil.tumlRuntimePropertyPathName.getCopy());
		getQualifiers.addParam("node", TinkerGenerationUtil.TINKER_NODE);
		getQualifiers.setReturnType(new OJPathName("java.util.List").addToGenerics(TinkerGenerationUtil.TINKER_QUALIFIER_PATHNAME));
		annotatedClass.addToOperations(getQualifiers);

		OJField result = null;
		if (!clazz.getGeneralizations().isEmpty()) {
			result = new OJField(getQualifiers.getBody(), "result", getQualifiers.getReturnType(), "super.getQualifiers(tumlRuntimeProperty, node)");
		} else {
			result = new OJField(getQualifiers.getBody(), "result", getQualifiers.getReturnType(), "Collections.emptyList()");
		}

		OJField runtimeProperty = new OJField(getQualifiers.getBody(), "runtimeProperty", new OJPathName(TumlClassOperations.propertyEnumName(clazz)));
		runtimeProperty.setInitExp(TumlClassOperations.propertyEnumName(clazz) + ".fromLabel(tumlRuntimeProperty.getLabel())");

		OJIfStatement ifRuntimePropertyNotNull = new OJIfStatement(runtimeProperty.getName() + " != null && result.isEmpty()");
		getQualifiers.getBody().addToStatements(ifRuntimePropertyNotNull);

		OJSwitchStatement ojSwitchStatement = new OJSwitchStatement();
		ojSwitchStatement.setCondition("runtimeProperty");
		ifRuntimePropertyNotNull.addToThenPart(ojSwitchStatement);

		for (Property p : TumlClassOperations.getAllOwnedProperties(clazz)) {
			PropertyWrapper pWrap = new PropertyWrapper(p);
			if (pWrap.isQualified()) {
				OJSwitchCase ojSwitchCase = new OJSwitchCase();
				ojSwitchCase.setLabel(pWrap.fieldname());
				OJSimpleStatement statement = new OJSimpleStatement("result = " + pWrap.getQualifiedGetterName() + "((" + pWrap.getType().getName() + ")node)");
				statement.setName(pWrap.fieldname());
				ojSwitchCase.getBody().addToStatements(statement);
				annotatedClass.addToImports(pWrap.javaImplTypePath());
				ojSwitchStatement.addToCases(ojSwitchCase);
			}
		}
		OJSwitchCase ojSwitchCase = new OJSwitchCase();
		ojSwitchCase.getBody().addToStatements("result = Collections.emptyList()");
		ojSwitchStatement.setDefCase(ojSwitchCase);

		getQualifiers.getBody().addToStatements("return " + result.getName());
		annotatedClass.addToImports("java.util.Collections");
	}

	private void addGetSize(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation getQualifiers = new OJAnnotatedOperation("getSize");
		TinkerGenerationUtil.addOverrideAnnotation(getQualifiers);
		getQualifiers.setComment("getSize is called from the collection in order to update the index used to implement a sequence's index");
		getQualifiers.addParam("tumlRuntimeProperty", TinkerGenerationUtil.tumlRuntimePropertyPathName.getCopy());
		getQualifiers.setReturnType(new OJPathName("int"));
		annotatedClass.addToOperations(getQualifiers);

		OJField result = null;
		if (!clazz.getGeneralizations().isEmpty()) {
			result = new OJField(getQualifiers.getBody(), "result", getQualifiers.getReturnType(), "super.getSize(tumlRuntimeProperty)");
		} else {
			result = new OJField(getQualifiers.getBody(), "result", getQualifiers.getReturnType(), "0");
		}

		OJField runtimeProperty = new OJField(getQualifiers.getBody(), "runtimeProperty", new OJPathName(TumlClassOperations.propertyEnumName(clazz)));
		runtimeProperty.setInitExp(TumlClassOperations.propertyEnumName(clazz) + ".fromLabel(tumlRuntimeProperty.getLabel())");

		OJIfStatement ifRuntimePropertyNotNull = new OJIfStatement(runtimeProperty.getName() + " != null && result == 0");
		getQualifiers.getBody().addToStatements(ifRuntimePropertyNotNull);

		OJSwitchStatement ojSwitchStatement = new OJSwitchStatement();
		ojSwitchStatement.setCondition("runtimeProperty");
		ifRuntimePropertyNotNull.addToThenPart(ojSwitchStatement);

		for (Property p : TumlClassOperations.getAllOwnedProperties(clazz)) {
			PropertyWrapper pWrap = new PropertyWrapper(p);
			if (!pWrap.isDerived()) {
				OJSwitchCase ojSwitchCase = new OJSwitchCase();
				ojSwitchCase.setLabel(pWrap.fieldname());
				OJSimpleStatement statement = new OJSimpleStatement("result = " + pWrap.fieldname() + ".size()");
				statement.setName(pWrap.fieldname());
				ojSwitchCase.getBody().addToStatements(statement);
				annotatedClass.addToImports(pWrap.javaImplTypePath());
				ojSwitchStatement.addToCases(ojSwitchCase);
			}
		}
		OJSwitchCase ojSwitchCase = new OJSwitchCase();
		ojSwitchCase.getBody().addToStatements("result = 0");
		ojSwitchStatement.setDefCase(ojSwitchCase);

		getQualifiers.getBody().addToStatements("return " + result.getName());
		annotatedClass.addToImports("java.util.Collections");
	}

}
