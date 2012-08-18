package org.tuml.javageneration.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJEnum;
import org.opaeum.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;

public class RuntimePropertyImplementor {

	public static OJEnum addTumlRuntimePropertyEnum(OJAnnotatedClass annotatedClass, String enumName, String className, Set<Property> allOwnedProperties, boolean hasCompositeOwner, String modelName) {
		OJEnum ojEnum = new OJEnum(enumName);
		ojEnum.setStatic(true);
		ojEnum.addToImplementedInterfaces(TinkerGenerationUtil.tumlRuntimePropertyPathName.getCopy());
		annotatedClass.addInnerEnum(ojEnum);

		OJField isOnePrimitiveField = new OJField();
		isOnePrimitiveField.setType(new OJPathName("boolean"));
		isOnePrimitiveField.setName("onePrimitive");
		ojEnum.addToFields(isOnePrimitiveField);

		OJField isManyPrimitiveField = new OJField();
		isManyPrimitiveField.setType(new OJPathName("boolean"));
		isManyPrimitiveField.setName("manyPrimitive");
		ojEnum.addToFields(isManyPrimitiveField);

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

		OJAnnotatedOperation fromLabel = new OJAnnotatedOperation("fromLabel", new OJPathName(enumName));
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
		asJson.getBody().addToStatements("name", "sb.append(\"{\\\"name\\\": \\\"" + className + "\\\", \")");
		asJson.getBody().addToStatements("uri", "sb.append(\"\\\"uri\\\": \\\"TODO\\\", \")");
		asJson.getBody().addToStatements("properties", "sb.append(\"\\\"properties\\\": [\")");
		ojEnum.addToOperations(asJson);

		int count = 0;
		for (Property p : allOwnedProperties) {
			count++;
			PropertyWrapper pWrap = new PropertyWrapper(p);
			if (!(pWrap.isDerived() || pWrap.isDerivedUnion())) {
				asJson.getBody().addToStatements("sb.append(" + ojEnum.getName() + "." + pWrap.fieldname() + ".toJson())");
				if (count != allOwnedProperties.size()) {
					asJson.getBody().addToStatements("sb.append(\",\")");
				}
				addEnumLiteral(ojEnum, fromLabel, pWrap.fieldname(), pWrap.isPrimitive(), pWrap.isManyToOne(), pWrap.isMany(), pWrap.isControllingSide(), pWrap.isComposite(),
						pWrap.isOneToOne(), pWrap.isOneToMany(), pWrap.isManyToMany(), pWrap.getUpper(), pWrap.getLower(), pWrap.isQualified(), pWrap.isInverseQualified(),
						pWrap.isOrdered(), pWrap.isInverseOrdered(), pWrap.isUnique(), TinkerGenerationUtil.getEdgeName(pWrap.getProperty()));
			}
		}

		if (!hasCompositeOwner) {
			//Add in fake property to root
			asJson.getBody().addToStatements("sb.append(\",\")");
			asJson.getBody().addToStatements("sb.append(" + ojEnum.getName() + "." + modelName + ".toJson())");
			addEnumLiteral(ojEnum, fromLabel, modelName, false, true, false, false, true, false, false, false, -1, 0, false, false, false, false, false, "root" + className);
		}
		asJson.getBody().addToStatements("sb.append(\"]}\")");
		asJson.getBody().addToStatements("return sb.toString()");
		fromLabel.getBody().addToStatements("return null");
		return ojEnum;
	}

	public static void addEnumLiteral(OJEnum ojEnum, OJAnnotatedOperation fromLabel, String fieldName, boolean isPrimitive, boolean isManyToOne, boolean isMany,
			boolean isControllingSide, boolean isComposite, boolean isOneToOne, boolean isOneToMany, boolean isManyToMany, int getUpper, int getLower, boolean isQualified,
			boolean isInverseQualified, boolean isOrdered, boolean isInverseOrdered, boolean isUnique, String edgeName) {

		OJIfStatement ifLabelEquals = new OJIfStatement(fieldName + ".getLabel().equals(label)");
		// Do not make upper case, leave with java case sensitive
		// semantics
		ifLabelEquals.addToThenPart("return " + fieldName);
		fromLabel.getBody().addToStatements(ifLabelEquals);

		OJEnumLiteral ojLiteral = new OJEnumLiteral(fieldName);

		OJField propertyOnePrimitiveField = new OJField();
		propertyOnePrimitiveField.setType(new OJPathName("boolean"));
		// A one primitive property is a isManyToOne. Seeing as the
		// opposite end is null it defaults to many
		propertyOnePrimitiveField.setInitExp(Boolean.toString(isPrimitive && isManyToOne));
		ojLiteral.addToAttributeValues(propertyOnePrimitiveField);

		OJField propertyManyPrimitiveField = new OJField();
		propertyManyPrimitiveField.setType(new OJPathName("boolean"));
		propertyManyPrimitiveField.setInitExp(Boolean.toString(isPrimitive && isMany));
		ojLiteral.addToAttributeValues(propertyManyPrimitiveField);

		OJField propertyControllingSideField = new OJField();
		propertyControllingSideField.setType(new OJPathName("boolean"));
		propertyControllingSideField.setInitExp(Boolean.toString(isControllingSide));
		ojLiteral.addToAttributeValues(propertyControllingSideField);

		OJField compositeLabelField = new OJField();
		compositeLabelField.setType(new OJPathName("boolean"));
		compositeLabelField.setInitExp(Boolean.toString(isComposite));
		ojLiteral.addToAttributeValues(compositeLabelField);

		OJField propertyLabelField = new OJField();
		propertyLabelField.setType(new OJPathName("String"));
		propertyLabelField.setInitExp("\"" + edgeName + "\"");
		ojLiteral.addToAttributeValues(propertyLabelField);

		OJField isOneToOneAttribute = new OJField();
		isOneToOneAttribute.setType(new OJPathName("boolean"));
		isOneToOneAttribute.setInitExp(Boolean.toString(isOneToOne));
		ojLiteral.addToAttributeValues(isOneToOneAttribute);

		OJField isOneToManyAttribute = new OJField();
		isOneToManyAttribute.setType(new OJPathName("boolean"));
		isOneToManyAttribute.setInitExp(Boolean.toString(isOneToMany));
		ojLiteral.addToAttributeValues(isOneToManyAttribute);

		OJField isManyToOneAttribute = new OJField();
		isManyToOneAttribute.setType(new OJPathName("boolean"));
		isManyToOneAttribute.setInitExp(Boolean.toString(isManyToOne));
		ojLiteral.addToAttributeValues(isManyToOneAttribute);

		OJField isManyToManyAttribute = new OJField();
		isManyToManyAttribute.setType(new OJPathName("boolean"));
		isManyToManyAttribute.setInitExp(Boolean.toString(isManyToMany));
		ojLiteral.addToAttributeValues(isManyToManyAttribute);

		OJField upperAttribute = new OJField();
		upperAttribute.setType(new OJPathName("int"));
		upperAttribute.setInitExp(Integer.toString(getUpper));
		ojLiteral.addToAttributeValues(upperAttribute);

		OJField lowerAttribute = new OJField();
		lowerAttribute.setType(new OJPathName("int"));
		lowerAttribute.setInitExp(Integer.toString(getLower));
		ojLiteral.addToAttributeValues(lowerAttribute);

		OJField qualifiedAttribute = new OJField();
		qualifiedAttribute.setType(new OJPathName("boolean"));
		qualifiedAttribute.setInitExp(Boolean.toString(isQualified));
		ojLiteral.addToAttributeValues(qualifiedAttribute);

		OJField inverseQualifiedAttribute = new OJField();
		inverseQualifiedAttribute.setType(new OJPathName("boolean"));
		inverseQualifiedAttribute.setInitExp(Boolean.toString(isInverseQualified));
		ojLiteral.addToAttributeValues(inverseQualifiedAttribute);

		OJField orderedAttribute = new OJField();
		orderedAttribute.setType(new OJPathName("boolean"));
		orderedAttribute.setInitExp(Boolean.toString(isOrdered));
		ojLiteral.addToAttributeValues(orderedAttribute);

		OJField inverseOrderedAttribute = new OJField();
		inverseOrderedAttribute.setType(new OJPathName("boolean"));
		inverseOrderedAttribute.setInitExp(Boolean.toString(isInverseOrdered));
		ojLiteral.addToAttributeValues(inverseOrderedAttribute);

		OJField uniqueAttribute = new OJField();
		uniqueAttribute.setType(new OJPathName("boolean"));
		uniqueAttribute.setInitExp(Boolean.toString(isUnique));
		ojLiteral.addToAttributeValues(uniqueAttribute);

		OJField jsonAttribute = new OJField();
		jsonAttribute.setName("json");
		jsonAttribute.setType(new OJPathName("String"));

		StringBuilder sb = new StringBuilder();
		sb.append("{\\\"name\\\": \\\"");
		sb.append(fieldName);
		sb.append("\\\", ");
		sb.append("\\\"onePrimitive\\\": ");
		sb.append(propertyOnePrimitiveField.getInitExp());
		sb.append(", ");
		sb.append("\\\"manyPrimitive\\\": ");
		sb.append(propertyManyPrimitiveField.getInitExp());
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
		sb.append("}");
		jsonAttribute.setInitExp("\"" + sb.toString() + "\"");
		ojLiteral.addToAttributeValues(jsonAttribute);

		ojEnum.addToLiterals(ojLiteral);
	}

	
}
