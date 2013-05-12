package org.tuml.restlet.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.tuml.framework.ModelLoader;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.java.metamodel.OJConstructor;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.OJSimpleStatement;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.annotation.OJEnum;
import org.tuml.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.javageneration.util.Condition;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.util.TumlModelOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

import java.util.List;

public class AddTumlMetaDataUriFieldToRootRuntimePropertyEnum extends BaseVisitor implements Visitor<Model> {

	public AddTumlMetaDataUriFieldToRootRuntimePropertyEnum(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model model) {
		OJAnnotatedClass annotatedClass = this.workspace.findOJClass("org.tuml.root.Root");
		OJEnum ojEnum = annotatedClass.findEnum("RootRuntimePropertyEnum");
		
		OJField uriPrimitiveField = new OJField();
		uriPrimitiveField.setType(new OJPathName("String"));
		uriPrimitiveField.setName("tumlMetaDataUri");
		ojEnum.addToFields(uriPrimitiveField);

        OJAnnotatedOperation getter = new OJAnnotatedOperation("get"
				+ StringUtils.capitalize(uriPrimitiveField.getName()), uriPrimitiveField.getType());
		getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
		ojEnum.addToOperations(getter);

        OJConstructor constructor = ojEnum.getConstructors().iterator().next();
		constructor.addParam(uriPrimitiveField.getName(), uriPrimitiveField.getType());
		constructor.getBody().addToStatements("this." + uriPrimitiveField.getName() + " = " + uriPrimitiveField.getName());

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
                uri = "\"/" + contextPath + "/" + TumlClassOperations.getPathName(clazz).getLast().toLowerCase() + "MetaData\"";
            } else {
                uri = "\"\"";
            }

            OJField uriAttribute = new OJField();
			uriAttribute.setType(new OJPathName("String"));
			uriAttribute.setInitExp(uri);
            uriAttribute.setName("tumlMetaDataUri");
			literal.addToAttributeValues(uriAttribute);


            OJField jsonField = literal.findAttributeValue("json");
			StringBuilder sb = new StringBuilder();
			sb.append(", \\\"tumlMetaDataUri\\\": \\");
			sb.append(uri.substring(0, uri.length() - 1) + "\\\"");
			String initExp = jsonField.getInitExp();
			int indexOf = initExp.indexOf("}");
			initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";
			jsonField.setInitExp(initExp);

        }

	}
	
	@Override
	public void visitAfter(Model element) {

	}

}
