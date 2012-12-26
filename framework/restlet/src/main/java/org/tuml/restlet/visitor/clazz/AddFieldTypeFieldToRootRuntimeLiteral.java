package org.tuml.restlet.visitor.clazz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.tuml.java.metamodel.OJConstructor;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.annotation.OJEnum;
import org.tuml.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.Condition;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.util.TumlModelOperations;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.restlet.util.TumlRestletGenerationUtil;

public class AddFieldTypeFieldToRootRuntimeLiteral extends BaseVisitor implements Visitor<Model> {

	public AddFieldTypeFieldToRootRuntimeLiteral(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model model) {
		
		OJAnnotatedClass annotatedClass = this.workspace.findOJClass("org.tuml.root.Root");
		OJEnum ojEnum = annotatedClass.findEnum("RootRuntimePropertyEnum");

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

		@SuppressWarnings("unchecked")
		List<Class> result = (List<Class>) TumlModelOperations.findElements(model, new Condition() {
			@Override
			public boolean evaluateOn(Element e) {
				if (!(e instanceof Class)) {
					return false;
				}
				Class clazz = (Class) e;
				return !clazz.isAbstract() && !TumlClassOperations.hasCompositeOwner(clazz);
			}
		});
		
		for (Class clazz : result) {
			OJEnumLiteral literal = ojEnum.findLiteral(StringUtils.uncapitalize(TumlClassOperations.className(clazz)));			
			addFieldTypePropertyToLiteral(literal);
		}
		
		addFieldTypePropertyToLiteral(ojEnum.findLiteral("id"));
//		addFieldTypePropertyToLiteral(ojEnum.findLiteral("uri"));
		addFieldTypePropertyToLiteral(ojEnum.findLiteral(model.getName()));
		
	}

	static void addFieldTypePropertyToLiteral(OJEnumLiteral literal) {
		String uri = "FieldType.Date";
		OJField uriAttribute = new OJField();
		uriAttribute.setType(TumlRestletGenerationUtil.FieldType);
		uriAttribute.setInitExp(uri);
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
