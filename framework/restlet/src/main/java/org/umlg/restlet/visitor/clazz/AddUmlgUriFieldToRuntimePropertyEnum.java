package org.umlg.restlet.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.VisitSubclasses;
import org.umlg.java.metamodel.OJConstructor;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJSimpleStatement;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.javageneration.visitor.clazz.ToFromJsonCreator;

public class AddUmlgUriFieldToRuntimePropertyEnum extends BaseVisitor implements Visitor<Class> {

	public AddUmlgUriFieldToRuntimePropertyEnum(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
    @VisitSubclasses({Class.class, AssociationClass.class})
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJEnum ojEnum = annotatedClass.findEnum(TumlClassOperations.propertyEnumName(clazz));

		add_getUriToObject(clazz, ojEnum);
		add_getUriToObject_to_ToJson(clazz, annotatedClass);

		OJField uriPrimitiveField = new OJField();
		uriPrimitiveField.setType(new OJPathName("String"));
		uriPrimitiveField.setName("tumlUri");
		ojEnum.addToFields(uriPrimitiveField);

        OJField overloadedPostUriPrimitiveField = new OJField();
        overloadedPostUriPrimitiveField.setType(new OJPathName("String"));
        overloadedPostUriPrimitiveField.setName("tumlOverloadedPostUri");
        ojEnum.addToFields(overloadedPostUriPrimitiveField);

        OJAnnotatedOperation getter = new OJAnnotatedOperation("getTumlUri", uriPrimitiveField.getType());
		getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
		ojEnum.addToOperations(getter);

        OJAnnotatedOperation overloadedPostUriGetter = new OJAnnotatedOperation("getTumlOverloadedPostUri", overloadedPostUriPrimitiveField.getType());
        overloadedPostUriGetter.getBody().addToStatements("return this." + overloadedPostUriPrimitiveField.getName());
        ojEnum.addToOperations(overloadedPostUriGetter);

        OJConstructor constructor = ojEnum.getConstructors().iterator().next();
		constructor.addParam(uriPrimitiveField.getName(), uriPrimitiveField.getType());
		constructor.getBody().addToStatements("this." + uriPrimitiveField.getName() + " = " + uriPrimitiveField.getName());

        constructor.addParam(overloadedPostUriPrimitiveField.getName(), overloadedPostUriPrimitiveField.getType());
        constructor.getBody().addToStatements("this." + overloadedPostUriPrimitiveField.getName() + " = " + overloadedPostUriPrimitiveField.getName());

        Set<Property> properties = TumlClassOperations.getAllProperties(clazz);
		for (Property property : properties) {
			PropertyWrapper pWrap = new PropertyWrapper(property);
			if (!(pWrap.isDerived() || pWrap.isDerivedUnion())) {
				OJEnumLiteral literal = ojEnum.findLiteral(pWrap.fieldname());
				addTumlUriToLiteral(clazz, pWrap, literal);
                addTumlOverloadedPostUriToLiteral(clazz, pWrap, literal);

                //For association classes
                if (pWrap.isAssociationClass() && !(clazz instanceof AssociationClass)) {

                    literal = ojEnum.findLiteral(pWrap.getAssociationClassFakePropertyName());
                    addTumlUriToLiteral(clazz, pWrap, literal, true);
                    addTumlOverloadedPostUriToLiteral(clazz, pWrap, literal, true);

                }


            }
		}
		addTumlUriToLiteral(clazz, null, ojEnum.findLiteral("id"));
        addTumlOverloadedPostUriToLiteral(clazz, null, ojEnum.findLiteral("id"));
		// This is for root objects that have a literal to to model
		OJEnumLiteral modelLiteral = ojEnum.findLiteral(clazz.getModel().getName());
		if (modelLiteral != null) {
			addTumlUriToLiteral(clazz, null, modelLiteral);
            addTumlOverloadedPostUriToLiteral(clazz, null, modelLiteral);
		}
	}

    private void addTumlUriToLiteral(Class clazz, PropertyWrapper pWrap, OJEnumLiteral literal) {
        addTumlUriToLiteral(clazz, pWrap, literal, false);
    }

    private void addTumlUriToLiteral(Class clazz, PropertyWrapper pWrap, OJEnumLiteral literal, boolean asAssociationClass) {
		String uri;
		if (literal.getName().equals(clazz.getModel().getName())) {
			uri = "\"/" + clazz.getModel().getName() + "\"";
		} else {
			if (clazz != null && pWrap != null) {
                String contextPath;
                    contextPath = ModelLoader.INSTANCE.getModel().getName();
                if (!asAssociationClass) {
                    uri = "\"/" + contextPath + "/" + pWrap.getOwningType().getName().toLowerCase() + "s/{"
                            + pWrap.getOwningType().getName().toLowerCase() + "Id}/" + literal.getName() + "\"";
                } else {
                    uri = "\"/" + contextPath + "/" + pWrap.getAssociationClass().getName().toLowerCase() + "s/{"
                            + pWrap.getAssociationClass().getName().toLowerCase() + "Id}/" + literal.getName() + "\"";
                }
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

    private void addTumlOverloadedPostUriToLiteral(Class clazz, PropertyWrapper pWrap, OJEnumLiteral literal) {
        addTumlOverloadedPostUriToLiteral(clazz, pWrap, literal, false);
    }

    private void addTumlOverloadedPostUriToLiteral(Class clazz, PropertyWrapper pWrap, OJEnumLiteral literal, boolean asAssociationClas) {
        String uri;
        if (literal.getName().equals(clazz.getModel().getName())) {
            uri = "\"/" + clazz.getModel().getName() + "\"";
        } else {
            if (clazz != null && pWrap != null) {
                String contextPath;
                contextPath = ModelLoader.INSTANCE.getModel().getName();

                if (!asAssociationClas) {
                    uri = "\"/" + contextPath + "/overloadedpost/" + pWrap.getOwningType().getName().toLowerCase() + "s/{"
                            + pWrap.getOwningType().getName().toLowerCase() + "Id}/" + literal.getName() + "\"";
                } else {
                    uri = "\"/" + contextPath + "/overloadedpost/" + pWrap.getAssociationClass().getName().toLowerCase() + "s/{"
                            + pWrap.getAssociationClass().getName().toLowerCase() + "Id}/" + literal.getName() + "\"";
                }

            } else {
                uri = "\"\"";
            }
        }
        OJField uriAttribute = new OJField();
        uriAttribute.setType(new OJPathName("String"));
        uriAttribute.setInitExp(uri);
        uriAttribute.setName("tumlOverloadedPostUri");
        literal.addToAttributeValues(uriAttribute);

        OJField jsonField = literal.findAttributeValue("json");
        StringBuilder sb = new StringBuilder();
        sb.append(", \\\"tumlOverloadedPostUri\\\": \\");
        sb.append(uri.substring(0, uri.length() - 1) + "\\\"");
        String initExp = jsonField.getInitExp();
        int indexOf = initExp.lastIndexOf("}");
        initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";

        jsonField.setInitExp(initExp);
    }

    private void add_getUriToObject_to_ToJson(Class clazz, OJAnnotatedClass annotatedClass) {
		if (clazz.getGeneralizations().isEmpty()) {
			OJAnnotatedOperation toJson = annotatedClass.findOperation("toJson", new OJPathName("Boolean"));
			OJSimpleStatement s = (OJSimpleStatement) toJson.getBody().findStatement(ToFromJsonCreator.URI_FOR_RESTFULL);
			s.setExpression("sb.append(\"\\\"uri\\\": \" + getUri())");
			OJAnnotatedOperation toJsonWithoutCompositeParent = annotatedClass.findOperation("toJsonWithoutCompositeParent", new OJPathName("Boolean"));
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

	private void add_getUriToObject(Class clazz, OJEnum ojEnum) {
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
