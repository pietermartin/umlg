package org.tuml.restlet.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.tuml.framework.ModelLoader;
import org.tuml.java.metamodel.*;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.annotation.OJEnum;
import org.tuml.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

import java.util.Set;

public class AddTumlMetaDataUriFieldToRuntimePropertyEnum extends BaseVisitor implements Visitor<Class> {

    public AddTumlMetaDataUriFieldToRuntimePropertyEnum(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        OJEnum ojEnum = annotatedClass.findEnum(TumlClassOperations.propertyEnumName(clazz));

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

        Set<Property> properties = TumlClassOperations.getAllProperties(clazz);
        for (Property property : properties) {
            PropertyWrapper pWrap = new PropertyWrapper(property);
            if (!(pWrap.isDerived() || pWrap.isDerivedUnion())) {
                OJEnumLiteral literal = ojEnum.findLiteral(pWrap.fieldname());
                addTumlMetaDataUriToLiteral(clazz, pWrap, literal);
            }
        }
        addTumlMetaDataUriToLiteral(clazz, null, ojEnum.findLiteral("id"));
        // This is for root objects that have a literal to to model
        OJEnumLiteral modelLiteral = ojEnum.findLiteral(clazz.getModel().getName());
        if (modelLiteral != null) {
            addTumlMetaDataUriToLiteral(clazz, null, modelLiteral);
        }
    }

    private void addTumlMetaDataUriToLiteral(Class clazz, PropertyWrapper pWrap, OJEnumLiteral literal) {
        String uri;
        String contextPath;
        if (clazz != null && pWrap != null) {
//            if (ModelLoader.getImportedModelLibraries().contains(pWrap.getModel())) {
//                contextPath = ModelLoader.getModel().getName() + "/" + pWrap.getModel().getName();
//            } else {
                contextPath = ModelLoader.INSTANCE.getModel().getName();
//            }
            uri = "\"/" + contextPath + "/" + StringUtils.uncapitalize(pWrap.getType().getName()) + "MetaData\"";
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
