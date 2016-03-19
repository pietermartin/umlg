package org.umlg.restlet.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.VisitSubclasses;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgAssociationClassOperations;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.Set;

public class AddUmlgMetaDataUriFieldToRuntimePropertyEnum extends BaseVisitor implements Visitor<Class> {

    public AddUmlgMetaDataUriFieldToRuntimePropertyEnum(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        OJEnum ojEnum = annotatedClass.findEnum(UmlgClassOperations.propertyEnumName(clazz));

        OJField uriPrimitiveField = new OJField();
        uriPrimitiveField.setType(new OJPathName("String"));
        uriPrimitiveField.setName("tumlMetaDataUri");
        ojEnum.addToFields(uriPrimitiveField);

        OJAnnotatedOperation getter = new OJAnnotatedOperation("getTumlMetaDataUri", uriPrimitiveField.getType());
        getter.getBody().addToStatements("return this." + uriPrimitiveField.getName());
        ojEnum.addToOperations(getter);

        OJConstructor constructor = ojEnum.getConstructors().iterator().next();
        constructor.addParam(uriPrimitiveField.getName(), uriPrimitiveField.getType());
        constructor.getBody().addToStatements("this." + uriPrimitiveField.getName() + " = " + uriPrimitiveField.getName());

        Set<Property> properties = UmlgClassOperations.getAllProperties(clazz);
        for (Property property : properties) {
            PropertyWrapper pWrap = new PropertyWrapper(property);
//            if (!(pWrap.isDerived() || pWrap.isDerivedUnion())) {
                OJEnumLiteral literal = ojEnum.findLiteral(pWrap.fieldname());
                addTumlMetaDataUriToLiteral(clazz, pWrap, literal);

                if (pWrap.isMemberOfAssociationClass() && !(UmlgAssociationClassOperations.extendsAssociationClass(clazz))) {
                    literal = ojEnum.findLiteral(pWrap.getAssociationClassFakePropertyName());
                    addTumlMetaDataUriToLiteral(clazz, pWrap, literal, true);
                }
//            }
        }
        addTumlMetaDataUriToLiteral(clazz, null, ojEnum.findLiteral("id"));
        // This is for root objects that have a literal to to model
        OJEnumLiteral modelLiteral = ojEnum.findLiteral(clazz.getModel().getName());
        if (modelLiteral != null) {
            addTumlMetaDataUriToLiteral(clazz, null, modelLiteral);
        }
    }

    private void addTumlMetaDataUriToLiteral(Class clazz, PropertyWrapper pWrap, OJEnumLiteral literal) {
        addTumlMetaDataUriToLiteral(clazz, pWrap, literal, false);
    }

    private void addTumlMetaDataUriToLiteral(Class clazz, PropertyWrapper pWrap, OJEnumLiteral literal, boolean asAssociationClass) {
        String uri;
        String contextPath;
        if (clazz != null && pWrap != null) {
            contextPath = ModelLoader.INSTANCE.getModel().getName();
            if (!asAssociationClass) {
                uri = "\"/" + contextPath + "/" + StringUtils.uncapitalize(pWrap.getType().getName()) + "MetaData\"";
            } else {
                uri = "\"/" + contextPath + "/" + StringUtils.uncapitalize(pWrap.getAssociationClass().getName()) + "MetaData\"";
            }
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
        int indexOf = initExp.lastIndexOf("}");
        initExp = initExp.substring(0, indexOf) + sb.toString() + "}\"";

        jsonField.setInitExp(initExp);
    }

    @Override
    public void visitAfter(Class element) {

    }

}
