package org.tuml.restlet.visitor.clazz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Class;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJSimpleStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJEnum;
import org.opaeum.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class AddUriToRuntimePropertyEnum extends BaseVisitor implements Visitor<Class> {

	public AddUriToRuntimePropertyEnum(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJEnum ojEnum = annotatedClass.findEnum(TumlClassOperations.propertyEnumName(clazz));
		
		addUriToObject(clazz, ojEnum);
		
		OJField uriPrimitiveField = new OJField();
		uriPrimitiveField.setType(new OJPathName("String"));
		uriPrimitiveField.setName("tumlUri");
		ojEnum.addToFields(uriPrimitiveField);

		OJAnnotatedOperation getter = new OJAnnotatedOperation((uriPrimitiveField.getType().getLast().equals("boolean") ? "is" : "get")
				+ StringUtils.capitalize(uriPrimitiveField.getName()), uriPrimitiveField.getType());
		getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
		ojEnum.addToOperations(getter);

		OJConstructor constructor = ojEnum.getConstructors().iterator().next();
		constructor.addParam(uriPrimitiveField.getName(), uriPrimitiveField.getType());
		constructor.getBody().addToStatements("this." + uriPrimitiveField.getName() + " = " + uriPrimitiveField.getName());

		List<OJEnumLiteral> literals = ojEnum.getLiterals();
		for (OJEnumLiteral literal : literals) {
			String uri;
			//TODO add in a literal property for isRoot
			if (literal.getName().equals(clazz.getModel().getName())) {
				uri = "\"/" + clazz.getModel().getName() + "\"";
			} else {
				uri = "\"/" + clazz.getModel().getName() + "/" + TumlClassOperations.getPathName(clazz).getLast().toLowerCase() + "s/{"
						+ TumlClassOperations.getPathName(clazz).getLast().toLowerCase() + "Id}/" + literal.getName() + "\"";
			}
			OJField uriAttribute = new OJField();
			uriAttribute.setType(new OJPathName("String"));
			uriAttribute.setInitExp(uri);
			literal.addToAttributeValues(uriAttribute);

			OJField jsonField = literal.findAttributeValue("json");
			StringBuilder sb = new StringBuilder();
			sb.append(", \\\"tumlUri\\\": \\");
			sb.append(uri.substring(0, uri.length() - 1) + "\\\"");
			String initExp = jsonField.getInitExp();
			int indexOf = initExp.indexOf("}");
			initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";

			jsonField.setInitExp(initExp);
		}

	}

	private void addUriToObject(Class clazz, OJEnum ojEnum) {
		OJAnnotatedOperation asJson =  ojEnum.findOperation("asJson");
		OJSimpleStatement s =  (OJSimpleStatement) asJson.getBody().findStatement("uri");
		s.setExpression("sb.append(\"\\\"uri\\\": \\\"" + TumlClassOperations.getPathName(clazz).getLast().toLowerCase() + "s/{" + TumlClassOperations.getPathName(clazz).getLast().toLowerCase() + "Id}\\\", \")");
	}

	@Override
	public void visitAfter(Class element) {

	}

}
