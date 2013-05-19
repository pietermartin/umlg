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

import java.util.Set;

public class RootOverLoadedPostResourceServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Class> {

    public RootOverLoadedPostResourceServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Class clazz) {
        if (!clazz.isAbstract() && !TumlClassOperations.hasCompositeOwner(clazz)) {

            OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(TumlClassOperations.className(clazz) + "s_ServerResource");
            OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".restlet");
            annotatedInf.setMyPackage(ojPackage);
            addToSource(annotatedInf);

            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.className(clazz) + "s_ServerResourceImpl");
            annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
            annotatedClass.setMyPackage(ojPackage);
            annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
            annotatedClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));
            addToSource(annotatedClass);

            addDefaultConstructor(annotatedClass);
            addGetRootObjectRepresentation(clazz, annotatedInf, annotatedClass);
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

        ojTryStatement.getTryPart().addToStatements("StringBuilder json = new StringBuilder()");
        ojTryStatement.getTryPart().addToStatements("json.append(\"[\")");
        ojTryStatement.getTryPart().addToStatements("json.append(\"{\\\"data\\\": [\")");

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
        ifArrayForInsert.getThenPart().addToStatements("int count = 1");
        OJForStatement insertForArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForInsert.addToThenPart(insertForArray);
        insertForArray.getBody().addToStatements("json.append(add(map))");
        OJIfStatement insertIfCount = new OJIfStatement("count++ != array.size()");
        insertIfCount.addToThenPart("json.append(\", \")");
        insertForArray.getBody().addToStatements(insertIfCount);
        OJField insertMap = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        insertMap.setInitExp("(Map<String, Object>) o");
        ifArrayForInsert.setElsePart(new OJBlock());
        ifArrayForInsert.getElsePart().addToLocals(insertMap);
        ifArrayForInsert.getElsePart().addToStatements("json.append(add(map))");

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
        ifArrayForUpdate.getThenPart().addToStatements("int count = 1");
        OJForStatement updateForArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForUpdate.addToThenPart(updateForArray);
        updateForArray.getBody().addToStatements("json.append(put(map))");
        OJIfStatement updateIfCount = new OJIfStatement("count++ != array.size()");
        updateIfCount.addToThenPart("json.append(\", \")");
        updateForArray.getBody().addToStatements(updateIfCount);
        OJField updateMap = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        updateMap.setInitExp("(Map<String, Object>) o");
        ifArrayForUpdate.setElsePart(new OJBlock());
        ifArrayForUpdate.getElsePart().addToLocals(insertMap);
        ifArrayForUpdate.getElsePart().addToStatements("json.append(put(map))");

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

        //Check if transaction needs commiting
        commitOrRollback(ojTryStatement.getTryPart());

        ojTryStatement.getTryPart().addToStatements("meta", "json.append(\"], \\\"meta\\\": {\")");

        ojTryStatement.getTryPart().addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + concreteClassifier.getQualifiedName() + "\\\"\")");
        ojTryStatement.getTryPart().addToStatements("json.append(\", \\\"to\\\": \")");

        // Meta data remains for the root object as viewing a many list does
        // not
        // change the context
        ojTryStatement.getTryPart().addToStatements("json.append(" + TumlClassOperations.propertyEnumName(concreteClassifier) + ".asJson())");
        ojTryStatement.getTryPart().addToStatements("json.append(\", \\\"from\\\": \")");
        ojTryStatement.getTryPart().addToStatements("json.append(" + TinkerGenerationUtil.RootRuntimePropertyEnum.getLast() + ".asJson())");
        ojTryStatement.getTryPart().addToStatements("json.append(\"}}]\")");
        ojTryStatement.getTryPart().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
        ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        ojTryStatement.getCatchPart().addToStatements("GraphDb.getDb().rollback()");
        ojTryStatement.getCatchPart().addToStatements("throw " + TumlRestletGenerationUtil.TumlExceptionUtilFactory.getLast() + ".getTumlExceptionUtil().handle(e)");
        ojTryStatement.getFinallyPart().addToStatements(TinkerGenerationUtil.TumlTmpIdManager.getLast() + ".INSTANCE.remove()");
        annotatedClass.addToImports(TinkerGenerationUtil.TumlTmpIdManager);
        annotatedClass.addToImports(TumlRestletGenerationUtil.TumlExceptionUtilFactory);
        post.getBody().addToStatements(ojTryStatement);

        annotatedClass.addToImports(parentPathName);


        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(post);
    }

    private void addPostResource(Classifier concreteClassifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation add = new OJAnnotatedOperation("add", "String");
        add.setComment("This method adds a single new instance. If and id already exist it passes the existing id back as a tmpId");
        add.setVisibility(OJVisibilityKind.PRIVATE);
        add.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(add);
        add.getBody().addToStatements(TumlClassOperations.getPathName(concreteClassifier).getLast() + " childResource = new " + TumlClassOperations.getPathName(concreteClassifier).getLast() + "(true)");
        annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifier));
        add.getBody().addToStatements("childResource.fromJson(propertyMap)");
        add.getBody().addToStatements("String jsonResult = childResource.toJsonWithoutCompositeParent(true)");
        add.getBody().addToStatements("return jsonResult");
    }

    private void addPutResource(Classifier classifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation put = new OJAnnotatedOperation("put", "String");
        put.setVisibility(OJVisibilityKind.PRIVATE);
        put.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(put);
        put.getBody().addToStatements("Long id = Long.valueOf((Integer)propertyMap.get(\"id\"))");
        put.getBody().addToStatements(
                TumlClassOperations.getPathName(classifier).getLast() + " childResource = GraphDb.getDb().instantiateClassifier(id)");
        annotatedClass.addToImports(TumlClassOperations.getPathName(classifier));
        put.getBody().addToStatements("childResource.fromJson(propertyMap)");
        put.getBody().addToStatements("return childResource.toJsonWithoutCompositeParent()");
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

    private void addGetRootObjectRepresentation(Class clazz, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
        annotatedInf.addToOperations(getInf);
        getInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Get, "json"));

        OJAnnotatedOperation get = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
        get.addToThrows(TumlRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
        TinkerGenerationUtil.addOverrideAnnotation(get);

        OJTryStatement tryStatement = new OJTryStatement();

        OJField json = new OJField("json", new OJPathName("java.lang.StringBuilder"));
        json.setInitExp("new StringBuilder()");
        tryStatement.getTryPart().addToLocals(json);
        OJField resource = new OJField("resource", new OJPathName("java.util.List").addToGenerics(TumlClassOperations.getPathName(clazz)));
        resource.setInitExp("Root.INSTANCE.get" + TumlClassOperations.className(clazz) + "()");
        tryStatement.getTryPart().addToLocals(resource);
        annotatedClass.addToImports("org.tuml.root.Root");

        tryStatement.getTryPart().addToStatements("json.append(\"[\")");
        Set<Classifier> concreteImplementations = TumlClassOperations.getConcreteImplementations(clazz);
        int count = 1;
        for (Classifier classifier : concreteImplementations) {

            tryStatement.getTryPart().addToStatements("json.append(\"{\\\"data\\\": [\")");

            if (concreteImplementations.size() > 1) {
                tryStatement.getTryPart().addToStatements(
                        "json.append(ToJsonUtil.toJsonWithoutCompositeParent(resource.select(new "
                                + TinkerGenerationUtil.BooleanExpressionEvaluator.getCopy().addToGenerics(TumlClassOperations.getPathName(clazz)).getLast()
                                + "() {\n			@Override\n			public Boolean evaluate(" + TumlClassOperations.getPathName(clazz).getLast()
                                + " e) {\n				return e instanceof " + TumlClassOperations.getPathName(classifier).getLast() + ";\n			}\n		})))");
                annotatedClass.addToImports(TinkerGenerationUtil.BooleanExpressionEvaluator);
                annotatedClass.addToImports(TumlClassOperations.getPathName(clazz));
                annotatedClass.addToImports(TumlClassOperations.getPathName(classifier));
            } else {
                tryStatement.getTryPart().addToStatements("json.append(ToJsonUtil.toJsonWithoutCompositeParent(resource))");
            }

            annotatedClass.addToImports(TinkerGenerationUtil.ToJsonUtil);

            tryStatement.getTryPart().addToStatements("meta", "json.append(\"], \\\"meta\\\": {\")");

            tryStatement.getTryPart().addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + clazz.getQualifiedName() + "\\\"\")");
            tryStatement.getTryPart().addToStatements("json.append(\", \\\"to\\\": \")");

            // Meta data remains for the root object as viewing a many list does
            // not
            // change the context
            tryStatement.getTryPart().addToStatements("json.append(" + TumlClassOperations.propertyEnumName(clazz) + ".asJson())");
            tryStatement.getTryPart().addToStatements("json.append(\", \\\"from\\\": \")");
            tryStatement.getTryPart().addToStatements("json.append(" + TinkerGenerationUtil.RootRuntimePropertyEnum.getLast() + ".asJson())");
            annotatedClass.addToImports(TinkerGenerationUtil.RootRuntimePropertyEnum);
            annotatedClass.addToImports(TumlClassOperations.getPathName(clazz).append(TumlClassOperations.propertyEnumName(clazz)));
            if (concreteImplementations.size() != 1 && count++ != concreteImplementations.size()) {
                tryStatement.getTryPart().addToStatements("json.append(\",\")");
            }
        }
        tryStatement.getTryPart().addToStatements("json.append(\"}}]\")");
        tryStatement.getTryPart().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

        get.getBody().addToStatements(tryStatement);
        tryStatement.setCatchPart(null);
        tryStatement.getFinallyPart().addToStatements(TinkerGenerationUtil.graphDbAccess + ".rollback()");

        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }

    private void addToRouterEnum(Class clazz, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");
        OJEnumLiteral ojLiteral = new OJEnumLiteral(TumlClassOperations.className(clazz).toUpperCase() + "S");

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + TumlClassOperations.className(clazz).toLowerCase() + "s\"");
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

    private void addPrivateGetter(Class clazz, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {
        OJField jsonResult = new OJField("jsonEntityData", "java.lang.StringBuilder");
        jsonResult.setInitExp("new StringBuilder()");
        annotatedClass.addToFields(jsonResult);
        OJAnnotatedOperation get = new OJAnnotatedOperation("internalGetJson", "String");
        get.addToThrows(TumlRestletGenerationUtil.ResourceException);
    }

    private enum REST {
        PUT("put"), POST("add"), DELETE("delete");
        private String methodName;

        private REST(String methodName) {
            this.methodName = methodName;
        }

        private String getMethodName() {
            return this.methodName;
        }
    }

}
