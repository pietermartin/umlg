package org.umlg.restlet.visitor.clazz;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.*;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;

public class EntityForLookupServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Class> {

    public EntityForLookupServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        if (!clazz.isAbstract()) {
            OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()));
            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(getLookupServerResourceImplName(clazz));
            annotatedClass.setSuperclass(UmlgRestletGenerationUtil.ServerResource);
            annotatedClass.setMyPackage(ojPackage);
            annotatedClass.setVisibility(UmlgClassOperations.getVisibility(clazz.getVisibility()));
            addToSource(annotatedClass);
            addPrivateIdVariable(clazz, annotatedClass);
            addDefaultConstructor(annotatedClass);
            if (!clazz.isAbstract()) {
                addPutRepresentation(clazz, annotatedClass);
            }
            addToRouterEnum(clazz, annotatedClass);
        }
    }

    @Override
    public void visitAfter(Class clazz) {
    }

    private void addPutRepresentation(Class clazz, OJAnnotatedClass annotatedClass) {

        OJAnnotatedOperation put = new OJAnnotatedOperation("put", UmlgRestletGenerationUtil.Representation);
        put.addParam("entity", UmlgRestletGenerationUtil.Representation);
        put.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(put);

        put.getBody().addToStatements(
                "this." + getIdFieldName(clazz) + "= " + UmlgRestletGenerationUtil.UmlgURLDecoder.getLast() + ".decode((String)getRequestAttributes().get(\"" + getIdFieldName(clazz) + "\"))");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLDecoder);
        put.getBody().addToStatements(
                UmlgClassOperations.className(clazz) + " c = new " + UmlgClassOperations.className(clazz) + "(" + UmlgGenerationUtil.UMLGAccess + ".getVertex(this."
                        + getIdFieldName(clazz) + "))");
        annotatedClass.addToImports(UmlgClassOperations.getPathName(clazz));
        OJTryStatement ojTry = new OJTryStatement();

        OJAnnotatedField entityText = new OJAnnotatedField("entityText", "String");
        entityText.setInitExp("entity.getText()");
        ojTry.getTryPart().addToLocals(entityText);

        ojTry.getTryPart().addToStatements("c.fromJson(" + entityText.getName() + ")");

        //get the lookup uri
        ojTry.getTryPart().addToStatements("String lookupUri = " + UmlgRestletGenerationUtil.UmlgURLEncoder.getLast() + ".decode(getReference().getQueryAsForm(false).getFirstValue(\"lookupUri\"))");
        ojTry.getTryPart().addToStatements("lookupUri = \"riap://host\" + lookupUri");
        ojTry.getTryPart().addToStatements("int fakeIdIndex = lookupUri.indexOf(\"fake\")");
        OJIfStatement ifFakeId = new OJIfStatement("fakeIdIndex != -1");
        ifFakeId.addToThenPart("int indexOfForwardSlash = lookupUri.indexOf(\"/\", fakeIdIndex)");
        ifFakeId.addToThenPart("String fakeId = lookupUri.substring(fakeIdIndex, indexOfForwardSlash)");
        ifFakeId.addToThenPart("Object id = " + UmlgGenerationUtil.UmlgTmpIdManager.getLast() + ".INSTANCE.get(fakeId)");
        ifFakeId.addToThenPart("lookupUri = lookupUri.replace(fakeId, " + UmlgRestletGenerationUtil.UmlgURLDecoder.getLast() + ".encode(id.toString()))");
        ojTry.getTryPart().addToStatements(ifFakeId);
        annotatedClass.addToImports(UmlgGenerationUtil.UmlgTmpIdManager);

        ojTry.getTryPart().addToStatements(UmlgRestletGenerationUtil.ClientResource.getLast() + " cr = new ClientResource(lookupUri)");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ClientResource);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLEncoder);
        ojTry.getTryPart().addToStatements(UmlgRestletGenerationUtil.Representation.getLast() + " result = cr.get()");
        ojTry.getTryPart().addToStatements("return result");

        ojTry.getFinallyPart().addToStatements(UmlgGenerationUtil.UmlgTmpIdManager.getLast() + ".INSTANCE.remove()");
        ojTry.getFinallyPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");

        ojTry.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));

        OJIfStatement ifRuntime = new OJIfStatement("e instanceof RuntimeException");
        ifRuntime.addToThenPart("throw (RuntimeException)e");
        ojTry.getCatchPart().addToStatements(ifRuntime);
        ojTry.getCatchPart().addToStatements("throw new RuntimeException(e)");
        put.getBody().addToStatements(ojTry);


        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToOperations(put);
    }

    private void addToRouterEnum(Class clazz, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());
        OJEnumLiteral ojLiteral = new OJEnumLiteral(UmlgClassOperations.className(clazz).toUpperCase() + "_forwardToLookup");

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + UmlgClassOperations.className(clazz).toLowerCase() + "s/{" + UmlgClassOperations.className(clazz).toLowerCase() + "Id}/forwardToLookup\"");
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
    }

}
