package org.tuml.javageneration.visitor.clazz;

import java.util.Set;

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
import org.opaeum.java.metamodel.annotation.OJEnum;
import org.opaeum.java.metamodel.annotation.OJEnumLiteral;
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
		addTumlRuntimePropertyEnum(annotatedClass, clazz);
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

	private void addTumlRuntimePropertyEnum(OJAnnotatedClass annotatedClass, Class clazz) {
		OJEnum ojEnum = new OJEnum(TumlClassOperations.propertyEnumName(clazz));
		ojEnum.setStatic(true);
		ojEnum.addToImplementedInterfaces(TinkerGenerationUtil.tumlRuntimePropertyPathName.getCopy());
		annotatedClass.addInnerEnum(ojEnum);

		OJField isOnePrimitiveField = new OJField();
		isOnePrimitiveField.setType(new OJPathName("boolean"));
		isOnePrimitiveField.setName("onePrimitive");
		ojEnum.addToFields(isOnePrimitiveField);

		OJField inverseField = new OJField();
		inverseField.setType(new OJPathName("boolean"));
		inverseField.setName("controllingSide");
		ojEnum.addToFields(inverseField);

		OJField compositeField = new OJField();
		compositeField.setType(new OJPathName("boolean"));
		compositeField.setName("composite");
		ojEnum.addToFields(compositeField);

		OJField labelField = new OJField();
		labelField.setType(new OJPathName("String"));
		labelField.setName("label");
		ojEnum.addToFields(labelField);

		OJField isOneToOneField = new OJField();
		isOneToOneField.setType(new OJPathName("boolean"));
		isOneToOneField.setName("oneToOne");
		ojEnum.addToFields(isOneToOneField);

		OJField isOneToManyField = new OJField();
		isOneToManyField.setType(new OJPathName("boolean"));
		isOneToManyField.setName("oneToMany");
		ojEnum.addToFields(isOneToManyField);

		OJField isManyToOneField = new OJField();
		isManyToOneField.setType(new OJPathName("boolean"));
		isManyToOneField.setName("manyToOne");
		ojEnum.addToFields(isManyToOneField);

		OJField isManyToManyField = new OJField();
		isManyToManyField.setType(new OJPathName("boolean"));
		isManyToManyField.setName("manyToMany");
		ojEnum.addToFields(isManyToManyField);

		OJField upperField = new OJField();
		upperField.setType(new OJPathName("int"));
		upperField.setName("upper");
		ojEnum.addToFields(upperField);

		OJField lowerField = new OJField();
		lowerField.setType(new OJPathName("int"));
		lowerField.setName("lower");
		ojEnum.addToFields(lowerField);

		OJField qualifiedField = new OJField();
		qualifiedField.setType(new OJPathName("boolean"));
		qualifiedField.setName("qualified");
		ojEnum.addToFields(qualifiedField);

		OJField inverseQualifiedField = new OJField();
		inverseQualifiedField.setType(new OJPathName("boolean"));
		inverseQualifiedField.setName("inverseQualified");
		ojEnum.addToFields(inverseQualifiedField);

		OJField orderedField = new OJField();
		orderedField.setType(new OJPathName("boolean"));
		orderedField.setName("ordered");
		ojEnum.addToFields(orderedField);

		OJField inverseOrderedField = new OJField();
		inverseOrderedField.setType(new OJPathName("boolean"));
		inverseOrderedField.setName("inverseOrdered");
		ojEnum.addToFields(inverseOrderedField);

		OJField uniqueField = new OJField();
		uniqueField.setType(new OJPathName("boolean"));
		uniqueField.setName("unique");
		ojEnum.addToFields(uniqueField);

		OJField jsonField = new OJField();
		jsonField.setType(new OJPathName("String"));
		jsonField.setName("json");
		ojEnum.addToFields(jsonField);

		ojEnum.implementGetter();
		ojEnum.createConstructorFromFields();

		OJAnnotatedOperation fromLabel = new OJAnnotatedOperation("fromLabel", new OJPathName(TumlClassOperations.propertyEnumName(clazz)));
		fromLabel.addParam("label", new OJPathName("String"));
		fromLabel.setStatic(true);
		ojEnum.addToOperations(fromLabel);

		OJAnnotatedOperation isValid = new OJAnnotatedOperation("isValid", new OJPathName("boolean"));
		TinkerGenerationUtil.addOverrideAnnotation(isValid);
		isValid.addParam("elementCount", new OJPathName("int"));
		OJIfStatement ifQualified = new OJIfStatement("isQualified()");
		ifQualified.addToThenPart("return elementCount >= getLower()");
		ifQualified.addToElsePart("return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower()");
		isValid.getBody().addToStatements(ifQualified);
		ojEnum.addToOperations(isValid);

		OJAnnotatedOperation toJson = new OJAnnotatedOperation("toJson", new OJPathName("String"));
		TinkerGenerationUtil.addOverrideAnnotation(toJson);
		toJson.getBody().addToStatements("return getJson()");
		ojEnum.addToOperations(toJson);

		OJAnnotatedOperation asJson = new OJAnnotatedOperation("asJson", new OJPathName("String"));
		asJson.setStatic(true);
		asJson.getBody().addToStatements("StringBuilder sb = new StringBuilder();");
		asJson.getBody().addToStatements("sb.append(\"{\\\"" + TumlClassOperations.className(clazz) + "\\\": [\")");
		ojEnum.addToOperations(asJson);

		Set<Property> allOwnedProperties = TumlClassOperations.getAllOwnedProperties(clazz);
		int count = 0;
		for (Property p : allOwnedProperties) {
			count++;
			PropertyWrapper pWrap = new PropertyWrapper(p);
			if (!(pWrap.isDerived() || pWrap.isDerivedUnion())) {

				asJson.getBody().addToStatements("sb.append(" + ojEnum.getName() + "." + pWrap.fieldname() + ".toJson())");
				if (count != allOwnedProperties.size()) {
					asJson.getBody().addToStatements("sb.append(\",\")");
				}

				OJIfStatement ifLabelEquals = new OJIfStatement(pWrap.fieldname() + ".getLabel().equals(label)");
				// Do not make upper case, leave with java case sensitive
				// semantics
				ifLabelEquals.addToThenPart("return " + pWrap.fieldname());
				fromLabel.getBody().addToStatements(ifLabelEquals);

				OJEnumLiteral ojLiteral = new OJEnumLiteral(pWrap.fieldname());

				OJField propertyOnePrimitiveField = new OJField();
				propertyOnePrimitiveField.setType(new OJPathName("boolean"));
				// A one primitive property is a isManyToOne. Seeing as the
				// opposite end is null it defaults to many
				propertyOnePrimitiveField.setInitExp(Boolean.toString(pWrap.isPrimitive() && pWrap.isManyToOne()));
				ojLiteral.addToAttributeValues(propertyOnePrimitiveField);

				OJField propertyControllingSideField = new OJField();
				propertyControllingSideField.setType(new OJPathName("boolean"));
				propertyControllingSideField.setInitExp(Boolean.toString(pWrap.isControllingSide()));
				ojLiteral.addToAttributeValues(propertyControllingSideField);

				OJField compositeLabelField = new OJField();
				compositeLabelField.setType(new OJPathName("boolean"));
				compositeLabelField.setInitExp(Boolean.toString(pWrap.isComposite()));
				ojLiteral.addToAttributeValues(compositeLabelField);

				OJField propertyLabelField = new OJField();
				propertyLabelField.setType(new OJPathName("String"));
				propertyLabelField.setInitExp("\"" + TinkerGenerationUtil.getEdgeName(p) + "\"");
				ojLiteral.addToAttributeValues(propertyLabelField);

				OJField isOneToOneAttribute = new OJField();
				isOneToOneAttribute.setType(new OJPathName("boolean"));
				isOneToOneAttribute.setInitExp(Boolean.toString(pWrap.isOneToOne()));
				ojLiteral.addToAttributeValues(isOneToOneAttribute);

				OJField isOneToManyAttribute = new OJField();
				isOneToManyAttribute.setType(new OJPathName("boolean"));
				isOneToManyAttribute.setInitExp(Boolean.toString(pWrap.isOneToMany()));
				ojLiteral.addToAttributeValues(isOneToManyAttribute);

				OJField isManyToOneAttribute = new OJField();
				isManyToOneAttribute.setType(new OJPathName("boolean"));
				isManyToOneAttribute.setInitExp(Boolean.toString(pWrap.isManyToOne()));
				ojLiteral.addToAttributeValues(isManyToOneAttribute);

				OJField isManyToManyAttribute = new OJField();
				isManyToManyAttribute.setType(new OJPathName("boolean"));
				isManyToManyAttribute.setInitExp(Boolean.toString(pWrap.isManyToMany()));
				ojLiteral.addToAttributeValues(isManyToManyAttribute);

				OJField upperAttribute = new OJField();
				upperAttribute.setType(new OJPathName("int"));
				upperAttribute.setInitExp(Integer.toString(pWrap.getUpper()));
				ojLiteral.addToAttributeValues(upperAttribute);

				OJField lowerAttribute = new OJField();
				lowerAttribute.setType(new OJPathName("int"));
				lowerAttribute.setInitExp(Integer.toString(pWrap.getLower()));
				ojLiteral.addToAttributeValues(lowerAttribute);

				OJField qualifiedAttribute = new OJField();
				qualifiedAttribute.setType(new OJPathName("boolean"));
				qualifiedAttribute.setInitExp(Boolean.toString(pWrap.isQualified()));
				ojLiteral.addToAttributeValues(qualifiedAttribute);

				OJField inverseQualifiedAttribute = new OJField();
				inverseQualifiedAttribute.setType(new OJPathName("boolean"));
				inverseQualifiedAttribute.setInitExp(Boolean.toString(pWrap.isInverseQualified()));
				ojLiteral.addToAttributeValues(inverseQualifiedAttribute);

				OJField orderedAttribute = new OJField();
				orderedAttribute.setType(new OJPathName("boolean"));
				orderedAttribute.setInitExp(Boolean.toString(pWrap.isOrdered()));
				ojLiteral.addToAttributeValues(orderedAttribute);

				OJField inverseOrderedAttribute = new OJField();
				inverseOrderedAttribute.setType(new OJPathName("boolean"));
				inverseOrderedAttribute.setInitExp(Boolean.toString(pWrap.isInverseOrdered()));
				ojLiteral.addToAttributeValues(inverseOrderedAttribute);

				OJField uniqueAttribute = new OJField();
				uniqueAttribute.setType(new OJPathName("boolean"));
				uniqueAttribute.setInitExp(Boolean.toString(pWrap.isUnique()));
				ojLiteral.addToAttributeValues(uniqueAttribute);

				OJField jsonAttribute = new OJField();
				jsonAttribute.setType(new OJPathName("String"));

				StringBuilder sb = new StringBuilder();
				sb.append("{\\\"");
				sb.append(pWrap.fieldname());
				sb.append("\\\": ");
				sb.append("{\\\"");
				sb.append("onePrimitive\\\": ");
				sb.append(propertyOnePrimitiveField.getInitExp());
				sb.append(", ");
				sb.append("\\\"controllingSide\\\": ");
				sb.append(propertyControllingSideField.getInitExp());
				sb.append(", ");
				sb.append("\\\"composite\\\": ");
				sb.append(compositeLabelField.getInitExp());
				sb.append(", ");
				sb.append("\\\"oneToOne\\\": ");
				sb.append(isOneToOneAttribute.getInitExp());
				sb.append(", ");
				sb.append("\\\"oneToMany\\\": ");
				sb.append(isOneToManyAttribute.getInitExp());
				sb.append(", ");
				sb.append("\\\"manyToOne\\\": ");
				sb.append(isManyToOneAttribute.getInitExp());
				sb.append(", ");
				sb.append("\\\"manyToMany\\\": ");
				sb.append(isManyToManyAttribute.getInitExp());
				sb.append(", ");
				sb.append("\\\"upper\\\": ");
				sb.append(upperAttribute.getInitExp());
				sb.append(", ");
				sb.append("\\\"lower\\\": ");
				sb.append(lowerAttribute.getInitExp());
				sb.append(", ");
				sb.append("\\\"label\\\": \\");
				sb.append(propertyLabelField.getInitExp().subSequence(0, propertyLabelField.getInitExp().length() - 1));
				sb.append("\\\", ");
				sb.append("\\\"qualified\\\": ");
				sb.append(qualifiedAttribute.getInitExp());
				sb.append(", ");
				sb.append("\\\"inverseQualified\\\": ");
				sb.append(inverseQualifiedAttribute.getInitExp());
				sb.append(", ");
				sb.append("\\\"inverseOrdered\\\": ");
				sb.append(orderedAttribute.getInitExp());
				sb.append(", ");
				sb.append("\\\"unique\\\": ");
				sb.append(uniqueAttribute.getInitExp());
				sb.append("}}");
				jsonAttribute.setInitExp("\"" + sb.toString() + "\"");
				ojLiteral.addToAttributeValues(jsonAttribute);

				ojEnum.addToLiterals(ojLiteral);
			}
		}
		asJson.getBody().addToStatements("sb.append(\"]}\")");
		asJson.getBody().addToStatements("return sb.toString()");
		fromLabel.getBody().addToStatements("return null");
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
		getQualifiers.setComment("getSize is called from the collection in order to update the index used to implement a sequance's index");
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
