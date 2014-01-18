package org.umlg.restlet.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.VisitSubclasses;
import org.umlg.java.metamodel.OJConstructor;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;

public class AddFieldTypeFieldToRuntimeLiteral extends BaseVisitor implements Visitor<Class> {

	public AddFieldTypeFieldToRuntimeLiteral(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
    @VisitSubclasses({Class.class, AssociationClass.class})
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJEnum ojEnum = annotatedClass.findEnum(UmlgClassOperations.propertyEnumName(clazz));
		OJField fieldTypeField = new OJField();
		fieldTypeField.setType(UmlgRestletGenerationUtil.FieldType);
		fieldTypeField.setName("fieldType");
		ojEnum.addToFields(fieldTypeField);

		OJAnnotatedOperation getter = new OJAnnotatedOperation("getFieldType", UmlgRestletGenerationUtil.FieldType);
		getter.getBody().addToStatements("return this.fieldType");
		ojEnum.addToOperations(getter);

		OJConstructor constructor = ojEnum.getConstructors().iterator().next();
		constructor.addParam("fieldType", UmlgRestletGenerationUtil.FieldType);
		constructor.getBody().addToStatements("this.fieldType = fieldType");
		
		Set<Property> properties = UmlgClassOperations.getAllProperties(clazz);
		for (Property property : properties) {
            PropertyWrapper propertyWrapper = new PropertyWrapper(property);
			OJEnumLiteral literal = ojEnum.findLiteral(propertyWrapper.fieldname());
			addFieldTypePropertyToLiteral(literal, UmlgRestletGenerationUtil.getFieldTypeForProperty(property));

            if (propertyWrapper.isMemberOfAssociationClass() && !(clazz instanceof AssociationClass)) {
                literal = ojEnum.findLiteral(propertyWrapper.getAssociationClassFakePropertyName());
                addFieldTypePropertyToLiteral(literal, UmlgRestletGenerationUtil.getFieldTypeForProperty(property));
            }
		}

		addFieldTypePropertyToLiteral(ojEnum.findLiteral("id"), "FieldType.Integer");

		if (!UmlgClassOperations.hasCompositeOwner(clazz)) {
			// This is for the fake property to Root
			addFieldTypePropertyToLiteral(ojEnum.findLiteral(clazz.getModel().getName()), "FieldType.String");
		}

	}

    static void addFieldTypePropertyToLiteral(OJEnumLiteral literal, String fieldType) {
		OJField uriAttribute = new OJField();
		uriAttribute.setType(UmlgRestletGenerationUtil.FieldType);
		uriAttribute.setInitExp(fieldType);
        uriAttribute.setName("fieldType");
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
