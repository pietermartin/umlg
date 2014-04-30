package org.umlg.restlet.visitor.clazz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.umlg.java.metamodel.OJConstructor;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Condition;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgModelOperations;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;

public class AddFieldTypeFieldToRootRuntimeLiteral extends BaseVisitor implements Visitor<Model> {

	public AddFieldTypeFieldToRootRuntimeLiteral(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model model) {
		
		OJAnnotatedClass annotatedClass = this.workspace.findOJClass(UmlgGenerationUtil.UmlgRootPackage.toJavaString() + "." + StringUtils.capitalize(model.getName()));
		OJEnum ojEnum = annotatedClass.findEnum(StringUtils.capitalize(model.getName()) + "RuntimePropertyEnum");

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

		@SuppressWarnings("unchecked")
		List<Class> result = (List<Class>) UmlgModelOperations.findElements(model, new Condition() {
            @Override
            public boolean evaluateOn(Element e) {
                if (!(e instanceof Class)) {
                    return false;
                }
                Class clazz = (Class) e;
                return !clazz.isAbstract() && !UmlgClassOperations.hasCompositeOwner(clazz) && !(clazz instanceof AssociationClass);
            }
        });
		
		for (Class clazz : result) {
			OJEnumLiteral literal = ojEnum.findLiteral(StringUtils.uncapitalize(UmlgClassOperations.className(clazz)));
			addFieldTypePropertyToLiteral(literal);
		}
		
		addFieldTypePropertyToLiteral(ojEnum.findLiteral("id"));
		addFieldTypePropertyToLiteral(ojEnum.findLiteral(model.getName()));
		
	}

	static void addFieldTypePropertyToLiteral(OJEnumLiteral literal) {
		String uri = "FieldType.Date";
		OJField uriAttribute = new OJField();
		uriAttribute.setType(UmlgRestletGenerationUtil.FieldType);
		uriAttribute.setInitExp(uri);
        uriAttribute.setName("fieldType");
		literal.addToAttributeValues(uriAttribute);

		OJField jsonField = literal.findAttributeValue("json");
		StringBuilder sb = new StringBuilder();
		sb.append(", \\\"fieldType\\\": \\\"\" + FieldType.Date + \"\\\"}\"");
		String initExp = jsonField.getInitExp();
		int indexOf = initExp.lastIndexOf("}");
		initExp = initExp.substring(0, indexOf) + sb.toString();

		jsonField.setInitExp(initExp);
	}

	@Override
	public void visitAfter(Model model) {
		
	}

}
