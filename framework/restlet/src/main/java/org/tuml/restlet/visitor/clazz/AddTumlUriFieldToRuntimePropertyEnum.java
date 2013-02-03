package org.tuml.restlet.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.tuml.framework.ModelLoader;
import org.tuml.java.metamodel.OJConstructor;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.OJSimpleStatement;
import org.tuml.java.metamodel.OJVisibilityKind;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.annotation.OJEnum;
import org.tuml.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.javageneration.visitor.clazz.ToFromJsonCreator;

public class AddTumlUriFieldToRuntimePropertyEnum extends BaseVisitor implements Visitor<Class> {

	public AddTumlUriFieldToRuntimePropertyEnum(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJEnum ojEnum = annotatedClass.findEnum(TumlClassOperations.propertyEnumName(clazz));

		addUriToObject(clazz, ojEnum);
		addUriToToJson(clazz, annotatedClass);

		OJField uriPrimitiveField = new OJField();
		uriPrimitiveField.setType(new OJPathName("String"));
		uriPrimitiveField.setName("tumlUri");
		ojEnum.addToFields(uriPrimitiveField);

        OJField transactionalUriPrimitiveField = new OJField();
        transactionalUriPrimitiveField.setType(new OJPathName("String"));
        transactionalUriPrimitiveField.setName("tumlTransactionalUri");
        ojEnum.addToFields(transactionalUriPrimitiveField);

        OJAnnotatedOperation getter = new OJAnnotatedOperation("getTumlUri", uriPrimitiveField.getType());
		getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
		ojEnum.addToOperations(getter);

        OJAnnotatedOperation transactionalGetter = new OJAnnotatedOperation("getTumlTransactionalUri", transactionalUriPrimitiveField.getType());
        transactionalGetter.getBody().addToStatements("return this." + transactionalUriPrimitiveField.getName());
        ojEnum.addToOperations(transactionalGetter);

        OJConstructor constructor = ojEnum.getConstructors().iterator().next();
		constructor.addParam(uriPrimitiveField.getName(), uriPrimitiveField.getType());
		constructor.getBody().addToStatements("this." + uriPrimitiveField.getName() + " = " + uriPrimitiveField.getName());

        constructor.addParam(transactionalUriPrimitiveField.getName(), transactionalUriPrimitiveField.getType());
        constructor.getBody().addToStatements("this." + transactionalUriPrimitiveField.getName() + " = " + transactionalUriPrimitiveField.getName());

        Set<Property> properties = TumlClassOperations.getAllProperties(clazz);
		for (Property property : properties) {
			PropertyWrapper pWrap = new PropertyWrapper(property);
			if (!(pWrap.isDerived() || pWrap.isDerivedUnion())) {
				OJEnumLiteral literal = ojEnum.findLiteral(pWrap.fieldname());
				addTumlUriToLiteral(clazz, pWrap, literal);
                addTumlTransactionalUriToLiteral(clazz, pWrap, literal);
			}
		}
		addTumlUriToLiteral(clazz, null, ojEnum.findLiteral("id"));
        addTumlTransactionalUriToLiteral(clazz, null, ojEnum.findLiteral("id"));
		// This is for root objects that have a literal to to model
		OJEnumLiteral modelLiteral = ojEnum.findLiteral(clazz.getModel().getName());
		if (modelLiteral != null) {
			addTumlUriToLiteral(clazz, null, modelLiteral);
            addTumlTransactionalUriToLiteral(clazz, null, modelLiteral);
		}
	}

	private void addTumlUriToLiteral(Class clazz, PropertyWrapper pWrap, OJEnumLiteral literal) {
		String uri;
		if (literal.getName().equals(clazz.getModel().getName())) {
			uri = "\"/" + clazz.getModel().getName() + "\"";
		} else {
			if (clazz != null && pWrap != null) {
                String contextPath;
                    contextPath = ModelLoader.INSTANCE.getModel().getName();
				uri = "\"/" + contextPath + "/" + pWrap.getOwningType().getName().toLowerCase() + "s/{"
						+ pWrap.getOwningType().getName().toLowerCase() + "Id}/" + literal.getName() + "\"";
			} else {
				uri = "\"\"";
			}
		}
		OJField uriAttribute = new OJField();
		uriAttribute.setType(new OJPathName("String"));
		uriAttribute.setInitExp(uri);
        uriAttribute.setName("tumlUri");
		literal.addToAttributeValues(uriAttribute);

		OJField jsonField = literal.findAttributeValue("json");
		StringBuilder sb = new StringBuilder();
		sb.append(", \\\"tumlUri\\\": \\");
		sb.append(uri.substring(0, uri.length() - 1) + "\\\"");
		String initExp = jsonField.getInitExp();
		int indexOf = initExp.lastIndexOf("}");
		initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";

		jsonField.setInitExp(initExp);
	}

    private void addTumlTransactionalUriToLiteral(Class clazz, PropertyWrapper pWrap, OJEnumLiteral literal) {
        String uri;
        if (literal.getName().equals(clazz.getModel().getName())) {
            uri = "\"/" + clazz.getModel().getName() + "\"";
        } else {
            if (clazz != null && pWrap != null) {
                String contextPath;
                contextPath = ModelLoader.INSTANCE.getModel().getName();
                uri = "\"/" + contextPath + "/transactional/{transactionUid}/" + pWrap.getOwningType().getName().toLowerCase() + "s/{"
                        + pWrap.getOwningType().getName().toLowerCase() + "Id}/" + literal.getName() + "\"";
            } else {
                uri = "\"\"";
            }
        }
        OJField uriAttribute = new OJField();
        uriAttribute.setType(new OJPathName("String"));
        uriAttribute.setInitExp(uri);
        uriAttribute.setName("tumlTransactionalUri");
        literal.addToAttributeValues(uriAttribute);

        OJField jsonField = literal.findAttributeValue("json");
        StringBuilder sb = new StringBuilder();
        sb.append(", \\\"tumlTransactionalUri\\\": \\");
        sb.append(uri.substring(0, uri.length() - 1) + "\\\"");
        String initExp = jsonField.getInitExp();
        int indexOf = initExp.lastIndexOf("}");
        initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";

        jsonField.setInitExp(initExp);
    }

    private void addUriToToJson(Class clazz, OJAnnotatedClass annotatedClass) {
		if (clazz.getGeneralizations().isEmpty()) {
			OJAnnotatedOperation toJson = annotatedClass.findOperation("toJson");
			OJSimpleStatement s = (OJSimpleStatement) toJson.getBody().findStatement(ToFromJsonCreator.URI_FOR_RESTFULL);
			s.setExpression("sb.append(\"\\\"uri\\\": \" + getUri())");
			OJAnnotatedOperation toJsonWithoutCompositeParent = annotatedClass.findOperation("toJsonWithoutCompositeParent");
			s = (OJSimpleStatement) toJsonWithoutCompositeParent.getBody().findStatement("uri");
			s.setExpression("sb.append(\"\\\"uri\\\": \" + getUri())");
		}
		if (!clazz.isAbstract()) {
			OJAnnotatedOperation getUri = new OJAnnotatedOperation("getUri");
			TinkerGenerationUtil.addOverrideAnnotation(getUri);
			getUri.setReturnType(new OJPathName("String"));
			getUri.setVisibility(OJVisibilityKind.PUBLIC);
			getUri.getBody().addToStatements("return (\"\\\"\" + " + TumlClassOperations.propertyEnumName(clazz) + ".getUriToObject() + \"\\\"\")");
			annotatedClass.addToOperations(getUri);
		}
	}

	private void addUriToObject(Class clazz, OJEnum ojEnum) {
		OJAnnotatedOperation getUriToObject = new OJAnnotatedOperation("getUriToObject", new OJPathName("String"));
		getUriToObject.setStatic(true);
		getUriToObject.getBody().addToStatements(
				"return " + "\"/" + this.workspace.getModel().getName() + "/" + TumlClassOperations.getPathName(clazz).getLast().toLowerCase() + "s/{"
						+ TumlClassOperations.getPathName(clazz).getLast().toLowerCase() + "Id}\"");
		ojEnum.addToOperations(getUriToObject);

		OJAnnotatedOperation asJson = ojEnum.findOperation("asJson");
		OJSimpleStatement s = (OJSimpleStatement) asJson.getBody().findStatement("uri");
		s.setExpression("sb.append(\"\\\"uri\\\": \\\"\" + getUriToObject() + \"\\\", \")");
	}

	@Override
	public void visitAfter(Class element) {

	}

}
