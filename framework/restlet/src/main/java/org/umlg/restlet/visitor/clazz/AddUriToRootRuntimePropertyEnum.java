package org.umlg.restlet.visitor.clazz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.umlg.framework.ModelLoader;
import org.umlg.java.metamodel.OJConstructor;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJSimpleStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Condition;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.util.TumlModelOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class AddUriToRootRuntimePropertyEnum extends BaseVisitor implements Visitor<Model> {

	public AddUriToRootRuntimePropertyEnum(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model model) {
		OJAnnotatedClass annotatedClass = this.workspace.findOJClass("org.umlg.root.Root");
		OJEnum ojEnum = annotatedClass.findEnum("RootRuntimePropertyEnum");
		
		addUriToObject(model, ojEnum);
		
		OJField uriPrimitiveField = new OJField();
		uriPrimitiveField.setType(new OJPathName("String"));
		uriPrimitiveField.setName("tumlUri");
		ojEnum.addToFields(uriPrimitiveField);

        OJField overloadedPostUriPrimitiveField = new OJField();
        overloadedPostUriPrimitiveField.setType(new OJPathName("String"));
        overloadedPostUriPrimitiveField.setName("tumlOverloadedPostUri");
        ojEnum.addToFields(overloadedPostUriPrimitiveField);

        OJAnnotatedOperation getter = new OJAnnotatedOperation("get"
				+ StringUtils.capitalize(uriPrimitiveField.getName()), uriPrimitiveField.getType());
		getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
		ojEnum.addToOperations(getter);

        OJAnnotatedOperation overloadedPostGetter = new OJAnnotatedOperation("get"
                + StringUtils.capitalize(overloadedPostUriPrimitiveField.getName()), overloadedPostUriPrimitiveField.getType());
        overloadedPostGetter.getBody().addToStatements("return this." + overloadedPostUriPrimitiveField.getName());
        ojEnum.addToOperations(overloadedPostGetter);

        OJConstructor constructor = ojEnum.getConstructors().iterator().next();
		constructor.addParam(uriPrimitiveField.getName(), uriPrimitiveField.getType());
		constructor.getBody().addToStatements("this." + uriPrimitiveField.getName() + " = " + uriPrimitiveField.getName());

        constructor = ojEnum.getConstructors().iterator().next();
        constructor.addParam(overloadedPostUriPrimitiveField.getName(), overloadedPostUriPrimitiveField.getType());
        constructor.getBody().addToStatements("this." + overloadedPostUriPrimitiveField.getName() + " = " + overloadedPostUriPrimitiveField.getName());

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

		List<OJEnumLiteral> literals = ojEnum.getLiterals();
		for (OJEnumLiteral literal : literals) {
			Class clazz = null;
			for (Class c : result) {
				if (StringUtils.uncapitalize(TumlClassOperations.className(c)).equals(literal.getName())) {
					clazz = c;
					break;
				}
			}
			String uri;
			if (clazz != null) {
                String contextPath = ModelLoader.INSTANCE.getModel().getName();
				uri = "\"/" + contextPath + "/" + TumlClassOperations.getPathName(clazz).getLast().toLowerCase() + "s\"";
			} else {
				uri = "\"/" + model.getName() + "\"";
			}

            String transactionalUri;
            if (clazz != null) {
                String contextPath = ModelLoader.INSTANCE.getModel().getName();
                transactionalUri = "\"/" + contextPath + "/overloadedpost/" + TumlClassOperations.getPathName(clazz).getLast().toLowerCase() + "s\"";
            } else {
                transactionalUri = "\"/" + model.getName() + "\"";
            }

            OJField uriAttribute = new OJField();
			uriAttribute.setType(new OJPathName("String"));
			uriAttribute.setInitExp(uri);
            uriAttribute.setName("tumlUri");
			literal.addToAttributeValues(uriAttribute);

            OJField transactionalUriAttribute = new OJField();
            transactionalUriAttribute.setType(new OJPathName("String"));
            transactionalUriAttribute.setInitExp(transactionalUri);
            transactionalUriAttribute.setName("tumlOverloadedPostUri");
            literal.addToAttributeValues(transactionalUriAttribute);

            OJField jsonField = literal.findAttributeValue("json");
			StringBuilder sb = new StringBuilder();
			sb.append(", \\\"tumlUri\\\": \\");
			sb.append(uri.substring(0, uri.length() - 1) + "\\\"");
			String initExp = jsonField.getInitExp();
			int indexOf = initExp.indexOf("}");
			initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";
			jsonField.setInitExp(initExp);

            sb = new StringBuilder();
            sb.append(", \\\"tumlOverloadedPostUri\\\": \\");
            sb.append(transactionalUri.substring(0, transactionalUri.length() - 1) + "\\\"");
            initExp = jsonField.getInitExp();
            indexOf = initExp.indexOf("}");
            initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";
            jsonField.setInitExp(initExp);

        }

	}
	
	@Override
	public void visitAfter(Model element) {

	}
	
	private void addUriToObject(Model model, OJEnum ojEnum) {
		OJAnnotatedOperation asJson =  ojEnum.findOperation("asJson");
		OJSimpleStatement s =  (OJSimpleStatement) asJson.getBody().findStatement("uri");
		s.setExpression("sb.append(\"\\\"uri\\\": \\\"/" + model.getName() + "\\\", \")");
	}

}
