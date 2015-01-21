package org.umlg.restlet.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.*;
import org.umlg.java.metamodel.generated.OJVisibilityKindGEN;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;

import java.util.Set;
import java.util.SortedSet;

public class EntityServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Classifier> {

    public EntityServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    @VisitSubclasses({Interface.class, Class.class, AssociationClass.class})
    public void visitBefore(Classifier classifier) {
        if (classifier instanceof Interface || classifier instanceof org.eclipse.uml2.uml.Class) {
            OJPackage ojPackage = new OJPackage(Namer.name(classifier.getNearestPackage()));
            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(getServerResourceImplName(classifier));
            annotatedClass.setSuperclass(UmlgRestletGenerationUtil.ServerResource);
            annotatedClass.setMyPackage(ojPackage);
            annotatedClass.setVisibility(UmlgClassOperations.getVisibility(classifier.getVisibility()));
            addToSource(annotatedClass);
            addDefaultConstructor(annotatedClass);
            //Interfaces and abstract classes can not have a get put or delete
            if (!(classifier instanceof Interface) && !classifier.isAbstract()) {
                addPrivateIdVariable(classifier, annotatedClass);
                addGetRepresentation((Class) classifier, annotatedClass);
                addPutRepresentation((Class) classifier, annotatedClass);
                addDeleteRepresentation((Class) classifier, annotatedClass);
            }
            //OPTIONS apply to abstract classes and interfaces
            addOptionsRepresentation(classifier, annotatedClass);
            addToRouterEnum(classifier, annotatedClass);
        }
    }

    @Override
    public void visitAfter(Classifier classifier) {
    }

    private void addDeleteRepresentation(Class clazz, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation delete = new OJAnnotatedOperation("delete", UmlgRestletGenerationUtil.Representation);
        delete.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(delete);

        delete.getBody().addToStatements(
                "this." + getIdFieldName(clazz) + "= " + UmlgRestletGenerationUtil.UmlgURLDecoder.getLast() + ".decode((String)getRequestAttributes().get(\"" + getIdFieldName(clazz) + "\"))");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLDecoder);
        delete.getBody().addToStatements(
                UmlgClassOperations.className(clazz) + " c = new " + UmlgClassOperations.className(clazz) + "(" + UmlgGenerationUtil.UMLGAccess + ".V(this."
                        + getIdFieldName(clazz) + ").next())");
        annotatedClass.addToImports(UmlgClassOperations.getPathName(clazz));

        OJTryStatement ojTry = new OJTryStatement();
        Set<Property> parentProperties = UmlgClassOperations.getOtherEndToComposite(clazz);
        for (Property parentProperty : parentProperties) {
            OJPathName parentPathName = UmlgClassOperations.getPathName(parentProperty.getType());
            annotatedClass.addToImports(parentPathName);
            PropertyWrapper parentWrap = new PropertyWrapper(parentProperty);
            delete.getBody().addToStatements(parentPathName.getLast() + " " + parentWrap.fieldname() + " = c." + parentWrap.getter() + "()");

        }
        ojTry.getTryPart().addToStatements("c.delete()");
        ojTry.getTryPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".commit()");
        ojTry.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));

        ojTry.getCatchPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");
        ojTry.getCatchPart().addToStatements("throw " + UmlgRestletGenerationUtil.UmlgExceptionUtilFactory.getLast() + ".getTumlExceptionUtil().handle(e)");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgExceptionUtilFactory);
        delete.getBody().addToStatements(ojTry);

        delete.getBody().addToStatements("return new " + UmlgRestletGenerationUtil.EmptyRepresentation.getLast() + "()");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.EmptyRepresentation);
        //TODO can not remember why I return the parent's representation
//        if (parentPathName != null) {
//            Property parentProperty = UmlgClassOperations.getOtherEndToComposite(clazz);
//            PropertyWrapper parentWrap = new PropertyWrapper(parentProperty);
//            addGetParentRepresentation((Class) parentProperty.getType(), annotatedClass);
//            delete.getBody().addToStatements("return getParent(" + parentWrap.fieldname() + ")");
//        } else {
//            delete.getBody().addToStatements("return new JsonRepresentation(\"\")");
//        }

        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(delete);
    }

    private void addPutRepresentation(Class clazz, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation put = new OJAnnotatedOperation("put", UmlgRestletGenerationUtil.Representation);
        put.addParam("entity", UmlgRestletGenerationUtil.Representation);
        put.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(put);

        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);

        put.getBody().addToStatements(
                "this." + getIdFieldName(clazz) + "= " + UmlgRestletGenerationUtil.UmlgURLDecoder.getLast() + ".decode((String)getRequestAttributes().get(\"" + getIdFieldName(clazz) + "\"))");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLDecoder);

        put.getBody().addToStatements(
                UmlgClassOperations.className(clazz) + " c = new " + UmlgClassOperations.className(clazz) + "(" + UmlgGenerationUtil.UMLGAccess + ".V(this."
                        + getIdFieldName(clazz) + ").next())");
        annotatedClass.addToImports(UmlgClassOperations.getPathName(clazz));
        OJTryStatement ojTry = new OJTryStatement();

        OJAnnotatedField entityText = new OJAnnotatedField("entityText", "String");
        entityText.setInitExp("entity.getText()");
        ojTry.getTryPart().addToLocals(entityText);

        ojTry.getTryPart().addToStatements("c.fromJson(" + entityText.getName() + ")");

        ojTry.getTryPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".commit()");

        //Now build the json to return, including meta data
        ojTry.getTryPart().addToStatements("StringBuilder json = new StringBuilder()");
        ojTry.getTryPart().addToStatements("json.append(\"[{\\\"data\\\": \")");
        ojTry.getTryPart().addToStatements("json.append(" + "c.toJsonWithoutCompositeParent())");
        ojTry.getTryPart().addToStatements("meta", "json.append(\", \\\"meta\\\" : {\")");
        ojTry.getTryPart().addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + clazz.getQualifiedName() + "\\\"\")");
        ojTry.getTryPart().addToStatements("json.append(\", \\\"to\\\": \")");
        ojTry.getTryPart().addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(clazz) + ".asJson())");
        annotatedClass.addToImports(UmlgClassOperations.getPathName(clazz).append(UmlgClassOperations.propertyEnumName(clazz)));
        ojTry.getTryPart().addToStatements("json.append(\"}}]\")");
        ojTry.getTryPart().addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");


        ojTry.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        ojTry.getCatchPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");
        OJIfStatement ifRuntime = new OJIfStatement("e instanceof RuntimeException");
        ifRuntime.addToThenPart("throw (RuntimeException)e");
        ojTry.getCatchPart().addToStatements(ifRuntime);
        ojTry.getCatchPart().addToStatements("throw new RuntimeException(e)");
        put.getBody().addToStatements(ojTry);
        ojTry.getFinallyPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");

        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(put);
    }

    private void addGetRepresentation(Class clazz, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation get = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
        get.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(get);

        OJTryStatement tryStatement = new OJTryStatement();
        tryStatement.getTryPart().addToStatements("StringBuilder json = new StringBuilder()");
        tryStatement.getTryPart().addToStatements(
                "this." + getIdFieldName(clazz) + "= " + UmlgRestletGenerationUtil.UmlgURLDecoder.getLast() + ".decode((String)getRequestAttributes().get(\"" + getIdFieldName(clazz) + "\"))");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLDecoder);

        tryStatement.getTryPart().addToStatements(
                UmlgClassOperations.className(clazz) + " c = new " + UmlgClassOperations.className(clazz) + "(" + UmlgGenerationUtil.UMLGAccess + ".V(this."
                        + getIdFieldName(clazz) + ").next())");
        annotatedClass.addToImports(UmlgClassOperations.getPathName(clazz));
        tryStatement.getTryPart().addToStatements("json.append(\"[{\\\"data\\\": \")");
        tryStatement.getTryPart().addToStatements("json.append(" + "c.toJsonWithoutCompositeParent())");

        tryStatement.getTryPart().addToStatements("meta", "json.append(\", \\\"meta\\\" : {\")");

        tryStatement.getTryPart().addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + clazz.getQualifiedName() + "\\\"\")");
        tryStatement.getTryPart().addToStatements("json.append(\"}}]\")");
        tryStatement.getTryPart().addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

        get.getBody().addToStatements(tryStatement);
        tryStatement.setCatchPart(null);
        tryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");

        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }

    private void addOptionsRepresentation(Classifier clazz, OJAnnotatedClass annotatedClass) {

        OJAnnotatedOperation options = new OJAnnotatedOperation("options", UmlgRestletGenerationUtil.Representation);
        options.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(options);

        OJTryStatement tryStatement = new OJTryStatement();
        SortedSet<Classifier> sortedConcreteImplementations = UmlgClassOperations.getConcreteImplementations(clazz);
        OJBlock block = tryStatement.getTryPart();
        block.addToStatements("StringBuilder json = new StringBuilder()");
        block.addToStatements("json.append(\"[\")");

        int count = 1;
        for (Classifier classifier : sortedConcreteImplementations) {
            block.addToStatements("json.append(\"{\\\"data\\\": null,\")");
            block.addToStatements("json.append(\" \\\"meta\\\" : {\")");

            block.addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + clazz.getQualifiedName() + "\\\"\")");
            block.addToStatements("json.append(\", \\\"to\\\": \")");
            block.addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(classifier) + ".asJson())");
//            block.addToStatements("json.append(\", \\\"from\\\": \")");
//            block.addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(clazz) + ".asJson())");
            block.addToStatements("json.append(\"} \")");
            annotatedClass.addToImports(UmlgClassOperations.getPathName(classifier).append(UmlgClassOperations.propertyEnumName(classifier)));
            if (count++ != sortedConcreteImplementations.size()) {
                block.addToStatements("json.append(\"}, \")");
            }
        }
        annotatedClass.addToImports(UmlgClassOperations.getPathName(clazz).append(UmlgClassOperations.propertyEnumName(clazz)));
        block.addToStatements("json.append(\"}]\")");

        annotatedClass.addToImports(UmlgClassOperations.getPathName(clazz));
        tryStatement.getTryPart().addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

        options.getBody().addToStatements(tryStatement);
        tryStatement.setCatchPart(null);
        tryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");

        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(options);
    }

    private void addGetParentRepresentation(Class clazz, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation getParent = new OJAnnotatedOperation("getParent", UmlgRestletGenerationUtil.Representation);
        getParent.addParam("parent", UmlgClassOperations.getPathName(clazz));
        getParent.setVisibility(OJVisibilityKindGEN.PRIVATE);
        getParent.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);

        getParent.getBody().addToStatements("StringBuilder json = new StringBuilder()");
        annotatedClass.addToImports(UmlgClassOperations.getPathName(clazz));
        getParent.getBody().addToStatements("json.append(\"[{\\\"data\\\": \")");
        getParent.getBody().addToStatements("json.append(" + "parent.toJsonWithoutCompositeParent())");

        getParent.getBody().addToStatements("meta", "json.append(\", \\\"meta\\\" : {\")");

        getParent.getBody().addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + clazz.getQualifiedName() + "\\\"\")");
        getParent.getBody().addToStatements("json.append(\", \\\"to\\\": \")");
        getParent.getBody().addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(clazz) + ".asJson())");
        annotatedClass.addToImports(UmlgClassOperations.getPathName(clazz).append(UmlgClassOperations.propertyEnumName(clazz)));
        getParent.getBody().addToStatements("json.append(\"}}]\")");
        getParent.getBody().addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(getParent);
    }

    private void addToRouterEnum(Classifier clazz, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());
        OJEnumLiteral ojLiteral = new OJEnumLiteral(UmlgClassOperations.className(clazz).toUpperCase());

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + UmlgClassOperations.className(clazz).toLowerCase() + "s/{" + UmlgClassOperations.className(clazz).toLowerCase() + "Id}\"");
        ojLiteral.addToAttributeValues(uri);

        OJField serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(annotatedClass.getName() + ".class");
        ojLiteral.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(annotatedClass.getPathName());
        routerEnum.addToImports(UmlgRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", UmlgRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");

        //Add meta data property url
        OJEnumLiteral ojLiteralMeta = new OJEnumLiteral(UmlgClassOperations.className(clazz).toUpperCase() + "_META");
        OJField uriMeta = new OJField();
        uriMeta.setType(new OJPathName("String"));
        uriMeta.setInitExp("\"/" + StringUtils.uncapitalize(UmlgClassOperations.className(clazz)) + "MetaData\"");
        ojLiteralMeta.addToAttributeValues(uriMeta);
        ojLiteralMeta.addToAttributeValues(serverResourceClassField);

        routerEnum.addToImports(annotatedClass.getPathName());
        routerEnum.addToImports(UmlgRestletGenerationUtil.ServerResource);
        routerEnum.addToLiterals(ojLiteralMeta);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteralMeta.getName() + ".attach(router)");
    }

}
