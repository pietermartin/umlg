package org.tuml.restlet.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.tuml.java.metamodel.*;
import org.tuml.java.metamodel.annotation.*;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.Namer;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.restlet.util.TumlRestletGenerationUtil;

public class EntityServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Class> {

    public EntityServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Class clazz) {
        if (!clazz.isAbstract()) {
            OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(getServerResourceName(clazz));
            OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".restlet");
            annotatedInf.setMyPackage(ojPackage);
            addToSource(annotatedInf);
            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(getServerResourceImplName(clazz));
            annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
            annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
            annotatedClass.setMyPackage(ojPackage);
            annotatedClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));
            addToSource(annotatedClass);
            addPrivateIdVariable(clazz, annotatedClass);
            addDefaultConstructor(annotatedClass);
            addGetRepresentation(clazz, annotatedInf, annotatedClass);
            if (!clazz.isAbstract()) {
                addPutRepresentation(clazz, annotatedInf, annotatedClass);
                addDeleteRepresentation(clazz, annotatedInf, annotatedClass);
            }
            addToRouterEnum(clazz, annotatedClass);
        }
    }

    @Override
    public void visitAfter(Class clazz) {
    }

    private void addDeleteRepresentation(Class clazz, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation deleteInf = new OJAnnotatedOperation("delete", TumlRestletGenerationUtil.Representation);
        deleteInf.addParam("entity", TumlRestletGenerationUtil.Representation);
        annotatedInf.addToOperations(deleteInf);
        deleteInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Delete, "json"));

        OJAnnotatedOperation delete = new OJAnnotatedOperation("delete", TumlRestletGenerationUtil.Representation);
        delete.addParam("entity", TumlRestletGenerationUtil.Representation);
        delete.addToThrows(TumlRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
        TinkerGenerationUtil.addOverrideAnnotation(delete);

        //Check if transaction needs resuming
        checkIfTransactionSuspended(delete);

        delete.getBody().addToStatements(
                "this." + getIdFieldName(clazz) + "= Long.valueOf((Integer)getRequestAttributes().get(\"" + getIdFieldName(clazz) + "\"))");
        delete.getBody().addToStatements(
                TumlClassOperations.className(clazz) + " c = new " + TumlClassOperations.className(clazz) + "(GraphDb.getDb().getVertex(this."
                        + getIdFieldName(clazz) + "))");
        annotatedClass.addToImports(TumlClassOperations.getPathName(clazz));

        OJPathName parentPathName = TumlClassOperations.getOtherEndToCompositePathName(clazz);
        if (parentPathName != null) {
            annotatedClass.addToImports(parentPathName);
            Property parentProperty = TumlClassOperations.getOtherEndToComposite(clazz);
            PropertyWrapper parentWrap = new PropertyWrapper(parentProperty);
            delete.getBody().addToStatements(parentPathName.getLast() + " " + parentWrap.fieldname() + " = c." + parentWrap.getter() + "()");
        }

        OJTryStatement ojTry = new OJTryStatement();
        ojTry.getTryPart().addToStatements("c.delete()");
//        ojTry.getTryPart().addToStatements("GraphDb.getDb().commit()");
        commitIfNotFromResume(ojTry.getTryPart());

        ojTry.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));

        ojTry.getCatchPart().addToStatements("GraphDb.getDb().rollback()");
        ojTry.getCatchPart().addToStatements(TumlRestletGenerationUtil.TumlExceptionUtilFactory.getLast() + ".getTumlExceptionUtil().handle(e)");
        annotatedClass.addToImports(TumlRestletGenerationUtil.TumlExceptionUtilFactory);
        delete.getBody().addToStatements(ojTry);

        if (parentPathName != null) {
            Property parentProperty = TumlClassOperations.getOtherEndToComposite(clazz);
            PropertyWrapper parentWrap = new PropertyWrapper(parentProperty);
            addGetParentRepresentation((Class) parentProperty.getType(), annotatedClass);
            delete.getBody().addToStatements("return getParent(" + parentWrap.fieldname() + ")");
        } else {
            delete.getBody().addToStatements("return new JsonRepresentation(\"\")");
        }

        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(delete);
    }

    private void addPutRepresentation(Class clazz, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {

        OJAnnotatedOperation putInf = new OJAnnotatedOperation("put", TumlRestletGenerationUtil.Representation);
        putInf.addParam("entity", TumlRestletGenerationUtil.Representation);
        annotatedInf.addToOperations(putInf);
        putInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Put, "json"));

        OJAnnotatedOperation put = new OJAnnotatedOperation("put", TumlRestletGenerationUtil.Representation);
        put.addParam("entity", TumlRestletGenerationUtil.Representation);
        put.addToThrows(TumlRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
        TinkerGenerationUtil.addOverrideAnnotation(put);

        //Check if transaction needs resuming
        checkIfTransactionSuspended(put);

        put.getBody().addToStatements(
                "this." + getIdFieldName(clazz) + "= Long.valueOf((Integer)getRequestAttributes().get(\"" + getIdFieldName(clazz) + "\"))");
        put.getBody().addToStatements(
                TumlClassOperations.className(clazz) + " c = new " + TumlClassOperations.className(clazz) + "(GraphDb.getDb().getVertex(this."
                        + getIdFieldName(clazz) + "))");
        annotatedClass.addToImports(TumlClassOperations.getPathName(clazz));
        OJTryStatement ojTry = new OJTryStatement();

        OJAnnotatedField entityText = new OJAnnotatedField("entityText", "String");
        entityText.setInitExp("entity.getText()");
        ojTry.getTryPart().addToLocals(entityText);

        ojTry.getTryPart().addToStatements("c.fromJson(" + entityText.getName() + ");");

//        ojTry.getTryPart().addToStatements("GraphDb.getDb().commit()");
        commitIfNotFromResume(ojTry.getTryPart());


        ojTry.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));

        ojTry.getCatchPart().addToStatements("GraphDb.getDb().rollback()");
        OJIfStatement ifRuntime = new OJIfStatement("e instanceof RuntimeException");
        ifRuntime.addToThenPart("throw (RuntimeException)e");
        ojTry.getCatchPart().addToStatements(ifRuntime);
        ojTry.getCatchPart().addToStatements("throw new RuntimeException(e)");
        put.getBody().addToStatements(ojTry);

        put.getBody().addToStatements("return get()");

        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(put);
    }

    private void addGetRepresentation(Class clazz, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {

        OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
        annotatedInf.addToOperations(getInf);
        getInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Get, "json"));

        OJAnnotatedOperation get = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
        get.addToThrows(TumlRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
        TinkerGenerationUtil.addOverrideAnnotation(get);

        OJTryStatement tryStatement = new OJTryStatement();

        tryStatement.getTryPart().addToStatements("StringBuilder json = new StringBuilder()");
        OJIfStatement ojIfStatement = new OJIfStatement("!getReference().getLastSegment().endsWith(\"MetaData\")");
        ojIfStatement.addToThenPart(
                "this." + getIdFieldName(clazz) + "= Long.valueOf((Integer)getRequestAttributes().get(\"" + getIdFieldName(clazz) + "\"))");
        ojIfStatement.addToThenPart(
                TumlClassOperations.className(clazz) + " c = new " + TumlClassOperations.className(clazz) + "(GraphDb.getDb().getVertex(this."
                        + getIdFieldName(clazz) + "))");
        annotatedClass.addToImports(TumlClassOperations.getPathName(clazz));
        ojIfStatement.addToThenPart("json.append(\"[{\\\"data\\\": [\")");
        ojIfStatement.addToThenPart("json.append(" + "c.toJson())");
        ojIfStatement.addToElsePart("json.append(\"[{\\\"data\\\": [\")");

        tryStatement.getTryPart().addToStatements(ojIfStatement);
        tryStatement.getTryPart().addToStatements("meta", "json.append(\"], \\\"meta\\\" : {\")");

        tryStatement.getTryPart().addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + clazz.getQualifiedName() + "\\\"\")");
        tryStatement.getTryPart().addToStatements("json.append(\", \\\"to\\\": \")");
        tryStatement.getTryPart().addToStatements("json.append(" + TumlClassOperations.propertyEnumName(clazz) + ".asJson())");
        annotatedClass.addToImports(TumlClassOperations.getPathName(clazz).append(TumlClassOperations.propertyEnumName(clazz)));
        tryStatement.getTryPart().addToStatements("json.append(\"}}]\")");
        tryStatement.getTryPart().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

        get.getBody().addToStatements(tryStatement);
        tryStatement.setCatchPart(null);
        tryStatement.getFinallyPart().addToStatements(TinkerGenerationUtil.graphDbAccess + ".rollback()");

        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }

    private void addGetParentRepresentation(Class clazz, OJAnnotatedClass annotatedClass) {

        OJAnnotatedOperation getParent = new OJAnnotatedOperation("getParent", TumlRestletGenerationUtil.Representation);
        getParent.addParam("parent", TumlClassOperations.getPathName(clazz));
        getParent.addToThrows(TumlRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);

        getParent.getBody().addToStatements("StringBuilder json = new StringBuilder()");
        annotatedClass.addToImports(TumlClassOperations.getPathName(clazz));
        getParent.getBody().addToStatements("json.append(\"[{\\\"data\\\": [\")");
        getParent.getBody().addToStatements("json.append(" + "parent.toJson())");

        getParent.getBody().addToStatements("meta", "json.append(\"], \\\"meta\\\" : {\")");

        getParent.getBody().addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + clazz.getQualifiedName() + "\\\"\")");
        getParent.getBody().addToStatements("json.append(\", \\\"to\\\": \")");
        getParent.getBody().addToStatements("json.append(" + TumlClassOperations.propertyEnumName(clazz) + ".asJson())");
        annotatedClass.addToImports(TumlClassOperations.getPathName(clazz).append(TumlClassOperations.propertyEnumName(clazz)));
        getParent.getBody().addToStatements("json.append(\"}}]\")");
        getParent.getBody().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(getParent);
    }

    private void addToRouterEnum(Class clazz, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");
        OJEnumLiteral ojLiteral = new OJEnumLiteral(TumlClassOperations.className(clazz).toUpperCase());

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + TumlClassOperations.className(clazz).toLowerCase() + "s/{" + TumlClassOperations.className(clazz).toLowerCase() + "Id}\"");
        ojLiteral.addToAttributeValues(uri);

        OJEnumLiteral ojLiteralMeta = new OJEnumLiteral(TumlClassOperations.className(clazz).toUpperCase() + "_META");
        OJField uriMeta = new OJField();
        uriMeta.setType(new OJPathName("String"));
        uriMeta.setInitExp("\"/" + StringUtils.uncapitalize(TumlClassOperations.className(clazz)) + "MetaData\"");
        ojLiteralMeta.addToAttributeValues(uriMeta);

        OJField serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(annotatedClass.getName() + ".class");
        ojLiteralMeta.addToAttributeValues(serverResourceClassField);
        ojLiteral.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(annotatedClass.getPathName());
        routerEnum.addToImports(TumlRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);
        routerEnum.addToLiterals(ojLiteralMeta);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", TumlRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteralMeta.getName() + ".attach(router)");
    }

    private void addPrivateIdVariable(Class clazz, OJAnnotatedClass annotatedClass) {
        OJField privateId = new OJField(getIdFieldName(clazz), new OJPathName("Long"));
        privateId.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToFields(privateId);
    }

    private String getIdFieldName(Class clazz) {
        return StringUtils.uncapitalize(TumlClassOperations.className(clazz)).toLowerCase() + "Id";
    }

}
