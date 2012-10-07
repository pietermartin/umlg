package org.tuml.restlet.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJEnum;
import org.opaeum.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.restlet.util.TumlRestletGenerationUtil;

public class AddFieldTypePropertyToRuntimeLiteral extends BaseVisitor implements Visitor<Class> {

	public AddFieldTypePropertyToRuntimeLiteral(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJEnum ojEnum = annotatedClass.findEnum(TumlClassOperations.propertyEnumName(clazz));
		OJField fieldTypeField = new OJField();
		fieldTypeField.setType(TumlRestletGenerationUtil.FieldType);
		fieldTypeField.setName("fieldType");
		ojEnum.addToFields(fieldTypeField);

		OJAnnotatedOperation getter = new OJAnnotatedOperation("getFieldType", TumlRestletGenerationUtil.FieldType);
		getter.getBody().addToStatements("return this.fieldType");
		ojEnum.addToOperations(getter);

		OJConstructor constructor = ojEnum.getConstructors().iterator().next();
		constructor.addParam("fieldType", TumlRestletGenerationUtil.FieldType);
		constructor.getBody().addToStatements("this.fieldType = fieldType");
		
		Set<Property> properties = TumlClassOperations.getAllOwnedProperties(clazz);
		for (Property property : properties) {
			OJEnumLiteral literal = ojEnum.findLiteral(new PropertyWrapper(property).fieldname());
			addFieldTypePropertyToLiteral(literal, TumlRestletGenerationUtil.getFieldTypeForProperty(property));
		}

		addFieldTypePropertyToLiteral(ojEnum.findLiteral("id"), "FieldType.Integer");
//		addFieldTypePropertyToLiteral(ojEnum.findLiteral("uri"), "FieldType.String");

		if (!TumlClassOperations.hasCompositeOwner(clazz)) {
			// This is for the fake property to Root
			addFieldTypePropertyToLiteral(ojEnum.findLiteral(clazz.getModel().getName()), "FieldType.String");
		}

	}

//	public static void addFieldTypePropertyToLiteral(Property property, OJEnumLiteral literal) {
//		String fieldTypeAstring = TumlRestletGenerationUtil.getFieldTypeForProperty(property);
//		OJField uriAttribute = new OJField();
//		uriAttribute.setType(TumlRestletGenerationUtil.FieldType);
//		uriAttribute.setInitExp(fieldTypeAstring);
//		literal.addToAttributeValues(uriAttribute);
//
//		OJField jsonField = literal.findAttributeValue("json");
//		StringBuilder sb = new StringBuilder();
//		sb.append(", \\\"fieldType\\\": \\\"\" + " + fieldTypeAstring + " + \"\\\"}\"");
//		String initExp = jsonField.getInitExp();
//		int indexOf = initExp.lastIndexOf("}");
//		initExp = initExp.substring(0, indexOf) + sb.toString();
//
//		jsonField.setInitExp(initExp);
//	}

	static void addFieldTypePropertyToLiteral(OJEnumLiteral literal, String fieldType) {
		OJField uriAttribute = new OJField();
		uriAttribute.setType(TumlRestletGenerationUtil.FieldType);
		uriAttribute.setInitExp(fieldType);
		literal.addToAttributeValues(uriAttribute);

		OJField jsonField = literal.findAttributeValue("json");
		StringBuilder sb = new StringBuilder();
		sb.append(", \\\"fieldType\\\": \\\"\" + " + fieldType + " + \"\\\"}\"");
		String initExp = jsonField.getInitExp();
		int indexOf = initExp.lastIndexOf("}");
		initExp = initExp.substring(0, indexOf) + sb.toString();

		jsonField.setInitExp(initExp);
	}

	@Override
	public void visitAfter(Class element) {

	}

}
