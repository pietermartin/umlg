package org.tuml.restlet.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.java.metamodel.*;
import org.tuml.java.metamodel.annotation.*;
import org.tuml.javageneration.util.Namer;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.restlet.util.TumlRestletGenerationUtil;

public class RootOverLoadedPostForLookupResourceServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Class> {


    public RootOverLoadedPostForLookupResourceServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Class clazz) {
        if (!clazz.isAbstract() && !TumlClassOperations.hasCompositeOwner(clazz)) {

            OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(TumlClassOperations.className(clazz) + "s_LookupServerResource");
            OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".restlet");
            annotatedInf.setMyPackage(ojPackage);
            addToSource(annotatedInf);

            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.className(clazz) + "s_LookupServerResourceImpl");
            annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
            annotatedClass.setMyPackage(ojPackage);
            annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
            annotatedClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));
            addToSource(annotatedClass);

            addDefaultConstructor(annotatedClass);
            addPostObjectRepresentation(clazz, annotatedInf, annotatedClass);
            addToRouterEnum(clazz, annotatedClass);

        }
    }

    @Override
    public void visitAfter(Class clazz) {
    }

    private void addPostObjectRepresentation(Classifier concreteClassifier, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation postInf = new OJAnnotatedOperation("post", TumlRestletGenerationUtil.Representation);

        postInf.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
        annotatedInf.addToOperations(postInf);
        postInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Post, "json"));

        OJAnnotatedOperation post = new OJAnnotatedOperation("post", TumlRestletGenerationUtil.Representation);

        post.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
        post.addToThrows(TumlRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
        TinkerGenerationUtil.addOverrideAnnotation(post);
        TinkerGenerationUtil.addSuppressWarning(post);

        OJPathName parentPathName = TumlClassOperations.getPathName(concreteClassifier);

        OJTryStatement ojTryStatement = new OJTryStatement();
        OJField mapper = new OJField("mapper", TinkerGenerationUtil.ObjectMapper);
        mapper.setInitExp("new ObjectMapper()");
        ojTryStatement.getTryPart().addToLocals(mapper);

        OJAnnotatedField entityText = new OJAnnotatedField("entityText", "String");
        entityText.setInitExp("entity.getText()");
        ojTryStatement.getTryPart().addToLocals(entityText);

        OJPathName pathName = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJAnnotatedField overloaded = new OJAnnotatedField("overloaded", pathName);
        overloaded.setInitExp("mapper.readValue(" + entityText.getName() + ", Map.class)");
        ojTryStatement.getTryPart().addToLocals(overloaded);

        //Insert
        ojTryStatement.getTryPart().addToStatements("Object o = overloaded.get(\"insert\")");
        OJIfStatement ifInsert = new OJIfStatement("o != null");
        ojTryStatement.getTryPart().addToStatements(ifInsert);
        OJIfStatement ifArrayForInsert = new OJIfStatement("o instanceof ArrayList");
        OJPathName genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJField insertArray = new OJField("array", new OJPathName("java.util.List").addToGenerics(genericsForArray));
        insertArray.setInitExp("(ArrayList<Map<String, Object>>)o");
        annotatedClass.addToImports("java.util.ArrayList");
        ifArrayForInsert.getThenPart().addToLocals(insertArray);
        ifInsert.getThenPart().addToStatements(ifArrayForInsert);
        OJForStatement insertForArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForInsert.addToThenPart(insertForArray);
        insertForArray.getBody().addToStatements("add(map)");
        OJField insertMap = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        insertMap.setInitExp("(Map<String, Object>) o");
        ifArrayForInsert.setElsePart(new OJBlock());
        ifArrayForInsert.getElsePart().addToLocals(insertMap);
        ifArrayForInsert.getElsePart().addToStatements("add(map)");

        //update
        ojTryStatement.getTryPart().addToStatements("o = overloaded.get(\"update\")");
        OJIfStatement ifUpdate = new OJIfStatement("o != null");
        ojTryStatement.getTryPart().addToStatements(ifUpdate);
        OJIfStatement ifArrayForUpdate = new OJIfStatement("o instanceof ArrayList");
        genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJField updateArray = new OJField("array", new OJPathName("java.util.List").addToGenerics(genericsForArray));
        updateArray.setInitExp("(ArrayList<Map<String, Object>>)o");
        annotatedClass.addToImports("java.util.ArrayList");
        ifArrayForUpdate.getThenPart().addToLocals(updateArray);
        ifUpdate.getThenPart().addToStatements(ifArrayForUpdate);
        OJForStatement updateForArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForUpdate.addToThenPart(updateForArray);
        updateForArray.getBody().addToStatements("put(map)");
        OJField updateMap = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        updateMap.setInitExp("(Map<String, Object>) o");
        ifArrayForUpdate.setElsePart(new OJBlock());
        ifArrayForUpdate.getElsePart().addToLocals(insertMap);
        ifArrayForUpdate.getElsePart().addToStatements("put(map)");

        //delete
        ojTryStatement.getTryPart().addToStatements("o = overloaded.get(\"delete\")");
        OJIfStatement ifDelete = new OJIfStatement("o != null");
        ojTryStatement.getTryPart().addToStatements(ifDelete);
        OJIfStatement ifArrayForDelete = new OJIfStatement("o instanceof ArrayList");
        genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJField deleteArray = new OJField("array", new OJPathName("java.util.List").addToGenerics(genericsForArray));
        deleteArray.setInitExp("(ArrayList<Map<String, Object>>)o");
        annotatedClass.addToImports("java.util.ArrayList");
        ifArrayForDelete.getThenPart().addToLocals(deleteArray);
        ifDelete.getThenPart().addToStatements(ifArrayForDelete);
        OJForStatement deleteForArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForDelete.addToThenPart(deleteForArray);
        deleteForArray.getBody().addToStatements("delete(map)");
        OJField deleteMap = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        deleteMap.setInitExp("(Map<String, Object>) o");
        ifArrayForDelete.setElsePart(new OJBlock());
        ifArrayForDelete.getElsePart().addToLocals(insertMap);
        ifArrayForDelete.getElsePart().addToStatements("delete(map)");

        addPostResource(concreteClassifier, annotatedClass, parentPathName);
        addPutResource(concreteClassifier, annotatedClass, parentPathName);
        addDeleteResource(concreteClassifier, annotatedClass, parentPathName);

        //get the lookup uri
        ojTryStatement.getTryPart().addToStatements("String lookupUri = getQueryValue(\"lookupUri\")");
        ojTryStatement.getTryPart().addToStatements("String qualifiedName = getQueryValue(\"qualifiedName\")");
        ojTryStatement.getTryPart().addToStatements("lookupUri = \"riap://host\" + lookupUri");
        ojTryStatement.getTryPart().addToStatements("int fakeIdIndex = lookupUri.indexOf(\"fake\")");
        OJIfStatement ifFakeId = new OJIfStatement("fakeIdIndex != -1");
        ifFakeId.addToThenPart("int indexOfForwardSlash = lookupUri.indexOf(\"/\", fakeIdIndex)");
        ifFakeId.addToThenPart("String fakeId = lookupUri.substring(fakeIdIndex, indexOfForwardSlash)");
        ifFakeId.addToThenPart("long id = " + TumlRestletGenerationUtil.TumlTmpIdManager.getLast() + ".INSTANCE.get(qualifiedName + \"::\" + fakeId)");
        ifFakeId.addToThenPart("lookupUri = lookupUri.replace(fakeId, Long.toString(id))");
        ojTryStatement.getTryPart().addToStatements(ifFakeId);
        annotatedClass.addToImports(TumlRestletGenerationUtil.TumlTmpIdManager);

        ojTryStatement.getTryPart().addToStatements(TumlRestletGenerationUtil.ClientResource.getLast() + " cr = new ClientResource(lookupUri)");
        annotatedClass.addToImports(TumlRestletGenerationUtil.ClientResource);
        ojTryStatement.getTryPart().addToStatements(TumlRestletGenerationUtil.Representation.getLast() + " result = cr.get()");
        ojTryStatement.getTryPart().addToStatements("return result");

        //Always rollback
        ojTryStatement.getFinallyPart().addToStatements(TumlRestletGenerationUtil.TumlTmpIdManager.getLast() + ".INSTANCE.remove()");
        ojTryStatement.getFinallyPart().addToStatements(TinkerGenerationUtil.graphDbAccess + ".rollback()");

        ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        ojTryStatement.getCatchPart().addToStatements("GraphDb.getDb().rollback()");
        ojTryStatement.getCatchPart().addToStatements("throw " + TumlRestletGenerationUtil.TumlExceptionUtilFactory.getLast() + ".getTumlExceptionUtil().handle(e)");
        annotatedClass.addToImports(TumlRestletGenerationUtil.TumlExceptionUtilFactory);
        post.getBody().addToStatements(ojTryStatement);

        annotatedClass.addToImports(parentPathName);


        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToOperations(post);
    }

    private void addPostResource(Classifier concreteClassifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation add = new OJAnnotatedOperation("add");
        add.setComment("This method adds a single new instance. If and id already exist it passes the existing id back as a tmpId");
        add.setVisibility(OJVisibilityKind.PRIVATE);
        add.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(add);
        add.getBody().addToStatements(TumlClassOperations.getPathName(concreteClassifier).getLast() + " childResource = new " + TumlClassOperations.getPathName(concreteClassifier).getLast() + "(true)");
        annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifier));
        add.getBody().addToStatements("childResource.fromJson(propertyMap)");
    }

    private void addPutResource(Classifier classifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation put = new OJAnnotatedOperation("put");
        put.setVisibility(OJVisibilityKind.PRIVATE);
        put.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(put);
        put.getBody().addToStatements("Long id = Long.valueOf((Integer)propertyMap.get(\"id\"))");
        put.getBody().addToStatements(
                TumlClassOperations.getPathName(classifier).getLast() + " childResource = GraphDb.getDb().instantiateClassifier(id)");
        annotatedClass.addToImports(TumlClassOperations.getPathName(classifier));
        put.getBody().addToStatements("childResource.fromJson(propertyMap)");
    }

    private void addDeleteResource(Classifier classifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {

        OJAnnotatedOperation delete = new OJAnnotatedOperation("delete");
        delete.setVisibility(OJVisibilityKind.PRIVATE);
        delete.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(delete);
        delete.getBody().addToStatements("Long id = Long.valueOf((Integer)propertyMap.get(\"id\"))");
        delete.getBody().addToStatements(
                TumlClassOperations.getPathName(classifier).getLast() + " childResource = GraphDb.getDb().instantiateClassifier(id)");
        annotatedClass.addToImports(TumlClassOperations.getPathName(classifier));
        delete.getBody().addToStatements("childResource.delete()");

    }

    private void addToRouterEnum(Class clazz, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");
        OJEnumLiteral ojLiteral = new OJEnumLiteral(TumlClassOperations.className(clazz).toUpperCase() + "S_forwardToLookup");

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + TumlClassOperations.className(clazz).toLowerCase() + "s_forwardToLookup\"");
        ojLiteral.addToAttributeValues(uri);

        OJField serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(annotatedClass.getName() + ".class");
        ojLiteral.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(annotatedClass.getPathName());
        routerEnum.addToImports(TumlRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", TumlRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
    }

}
