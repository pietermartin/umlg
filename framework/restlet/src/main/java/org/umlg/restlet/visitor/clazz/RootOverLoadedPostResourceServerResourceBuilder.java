package org.umlg.restlet.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.*;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.restlet.generation.RestletVisitors;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;
import org.umlg.restlet.visitor.model.QueryExecuteResourceBuilder;

import java.util.SortedSet;

public class RootOverLoadedPostResourceServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Class> {

    public RootOverLoadedPostResourceServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        if (!clazz.isAbstract() && !UmlgClassOperations.hasCompositeOwner(clazz) && !(clazz instanceof AssociationClass)) {
            OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()));

            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(UmlgClassOperations.className(clazz) + "s_ServerResourceImpl");
            annotatedClass.setSuperclass(UmlgRestletGenerationUtil.ServerResource);
            annotatedClass.setMyPackage(ojPackage);
            annotatedClass.setVisibility(UmlgClassOperations.getVisibility(clazz.getVisibility()));
            addToSource(annotatedClass);

            addDefaultConstructor(annotatedClass);
            addGetRootObjectRepresentation(clazz, annotatedClass);
            addOptionsRootObjectRepresentation(clazz, annotatedClass);
            addPostObjectRepresentation(clazz, annotatedClass);
            addToRouterEnum(clazz, annotatedClass);
        }
    }

    @Override
    public void visitAfter(Class clazz) {
    }

    private void addPostObjectRepresentation(Classifier concreteClassifier, OJAnnotatedClass annotatedClass) {

        OJAnnotatedOperation post = new OJAnnotatedOperation("post", UmlgRestletGenerationUtil.Representation);

        post.addToParameters(new OJParameter("entity", UmlgRestletGenerationUtil.Representation));
        post.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(post);
        UmlgGenerationUtil.addSuppressWarning(post);

        OJPathName parentPathName = UmlgClassOperations.getPathName(concreteClassifier);

        OJTryStatement ojTryStatement = new OJTryStatement();
        OJField mapper = new OJField("mapper", UmlgGenerationUtil.ObjectMapper);
        mapper.setInitExp(UmlgGenerationUtil.ObjectMapperFactory.getLast() + ".INSTANCE.getObjectMapper()");
        annotatedClass.addToImports(UmlgGenerationUtil.ObjectMapperFactory);
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
//        ojTryStatement.getTryPart().addToStatements("boolean insertedSomething = false");

        OJField objectList = new OJField("objectList", new OJPathName("java.util.List").addToGenerics(UmlgGenerationUtil.UMLG_NODE));
        objectList.setInitExp("new ArrayList<" + UmlgGenerationUtil.UMLG_NODE.getLast() + ">()");
        ojTryStatement.getTryPart().addToLocals(objectList);

        OJIfStatement ifInsert = new OJIfStatement("o != null");
        ojTryStatement.getTryPart().addToStatements(ifInsert);
        OJIfStatement ifArrayForInsert = new OJIfStatement("o instanceof ArrayList");
        OJPathName genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJField insertArray = new OJField("array", new OJPathName("java.util.List").addToGenerics(genericsForArray));
        insertArray.setInitExp("(ArrayList<Map<String, Object>>)o");
        annotatedClass.addToImports("java.util.ArrayList");
        ifArrayForInsert.getThenPart().addToLocals(insertArray);
//        ifArrayForInsert.getThenPart().addToStatements("insertedSomething = !array.isEmpty()");
        ifInsert.getThenPart().addToStatements(ifArrayForInsert);
//        ifArrayForInsert.getThenPart().addToStatements("int count = 1");
        OJForStatement insertForArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForInsert.addToThenPart(insertForArray);
//        insertForArray.getBody().addToStatements("json.append(add(map))");
        insertForArray.getBody().addToStatements("objectList.add(add(map))");
//        OJIfStatement insertIfCount = new OJIfStatement("count++ != array.size()");
//        insertIfCount.addToThenPart("json.append(\", \")");
//        insertForArray.getBody().addToStatements(insertIfCount);
        OJField insertMap = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        insertMap.setInitExp("(Map<String, Object>) o");
        ifArrayForInsert.setElsePart(new OJBlock());
        ifArrayForInsert.getElsePart().addToLocals(insertMap);
//        ifArrayForInsert.getElsePart().addToStatements("insertedSomething = true");
//        ifArrayForInsert.getElsePart().addToStatements("json.append(add(map))");
        ifArrayForInsert.getElsePart().addToStatements("objectList.add(add(map))");

        //update
        ojTryStatement.getTryPart().addToStatements("o = overloaded.get(\"update\")");
//        ojTryStatement.getTryPart().addToStatements("boolean updatedSomething = false");
        OJIfStatement ifUpdate = new OJIfStatement("o != null");
        ojTryStatement.getTryPart().addToStatements(ifUpdate);
        OJIfStatement ifArrayForUpdate = new OJIfStatement("o instanceof ArrayList");
        genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJField updateArray = new OJField("array", new OJPathName("java.util.List").addToGenerics(genericsForArray));
        updateArray.setInitExp("(ArrayList<Map<String, Object>>)o");
        annotatedClass.addToImports("java.util.ArrayList");
        ifArrayForUpdate.getThenPart().addToLocals(updateArray);
//        ifArrayForUpdate.getThenPart().addToStatements("updatedSomething = !array.isEmpty()");
//        OJIfStatement ifInsertedSomething = new OJIfStatement("insertedSomething && updatedSomething", "json.append(\", \")");
//        ifArrayForUpdate.getThenPart().addToStatements(ifInsertedSomething);
        ifUpdate.getThenPart().addToStatements(ifArrayForUpdate);
//        ifArrayForUpdate.getThenPart().addToStatements("int count = 1");
        OJForStatement updateForArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForUpdate.addToThenPart(updateForArray);
//        updateForArray.getBody().addToStatements("json.append(put(map))");
        updateForArray.getBody().addToStatements("objectList.add(put(map))");
//        OJIfStatement updateIfCount = new OJIfStatement("count++ != array.size()");
//        updateIfCount.addToThenPart("json.append(\", \")");
//        updateForArray.getBody().addToStatements(updateIfCount);
        OJField updateMap = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        updateMap.setInitExp("(Map<String, Object>) o");
        ifArrayForUpdate.setElsePart(new OJBlock());
        ifArrayForUpdate.getElsePart().addToLocals(insertMap);
//        ifArrayForUpdate.getElsePart().addToStatements("updatedSomething = true");
//        ifArrayForUpdate.getElsePart().addToStatements(ifInsertedSomething);
//        ifArrayForUpdate.getElsePart().addToStatements("json.append(put(map))");
        ifArrayForUpdate.getElsePart().addToStatements("objectList.add(put(map))");

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

//        OJIfStatement iInsertedOrUpdated = new OJIfStatement("(insertedSomething || updatedSomething) && !array.isEmpty()");
//        iInsertedOrUpdated.addToThenPart("json.append(\", \")");
//        ifArrayForDelete.addToThenPart(iInsertedOrUpdated);

        ifDelete.getThenPart().addToStatements(ifArrayForDelete);
        OJForStatement deleteForArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForDelete.addToThenPart(deleteForArray);
        deleteForArray.getBody().addToStatements("delete(map)");
        OJField deleteMap = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        deleteMap.setInitExp("(Map<String, Object>) o");
        ifArrayForDelete.setElsePart(new OJBlock());
        ifArrayForDelete.getElsePart().addToLocals(insertMap);

//        iInsertedOrUpdated = new OJIfStatement("insertedSomething || updatedSomething");
//        iInsertedOrUpdated.addToThenPart("json.append(\", \")");
//        ifArrayForDelete.addToElsePart(iInsertedOrUpdated);
        ifArrayForDelete.getElsePart().addToStatements("delete(map)");

        addPostResource(concreteClassifier, annotatedClass);
        addPutResource(concreteClassifier, annotatedClass);
        addDeleteResource(concreteClassifier, annotatedClass);

        //Check if transaction needs commiting
        commitOrRollback(ojTryStatement);

        OJField count = new OJField("count", "int");
        count.setInitExp("0");
        ojTryStatement.getTryPart().addToLocals(count);
        ojTryStatement.getTryPart().addToStatements("StringBuilder json = new StringBuilder()");
        ojTryStatement.getTryPart().addToStatements("json.append(\"[\")");
        ojTryStatement.getTryPart().addToStatements("json.append(\"{\\\"data\\\": [\")");

        OJForStatement forObjectList = new OJForStatement("umlgNode", UmlgGenerationUtil.UMLG_NODE, "objectList");
        forObjectList.getBody().addToStatements("json.append(umlgNode.toJsonWithoutCompositeParent(true))");
        OJIfStatement ifCountSize = new OJIfStatement("++count < objectList.size()", "json.append(\",\")");
        forObjectList.getBody().addToStatements(ifCountSize);
        ojTryStatement.getTryPart().addToStatements(forObjectList);

        ojTryStatement.getTryPart().addToStatements("meta", "json.append(\"], \\\"meta\\\": {\")");

        ojTryStatement.getTryPart().addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + concreteClassifier.getQualifiedName() + "\\\"\")");
        ojTryStatement.getTryPart().addToStatements("json.append(\", \\\"to\\\": \")");

        // Meta data remains for the root object as viewing a many list does
        // not
        // change the context
        ojTryStatement.getTryPart().addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(concreteClassifier) + ".asJson())");
        ojTryStatement.getTryPart().addToStatements("json.append(\", \\\"from\\\": \")");

//        ojTryStatement.getTryPart().addToStatements("json.append(" + StringUtils.capitalize(ModelLoader.INSTANCE.getModel().getName()) + ".asJson())");
        ojTryStatement.getTryPart().addToStatements("json.append(" + StringUtils.capitalize(ModelLoader.INSTANCE.getModel().getName()) + "RuntimePropertyEnum.asJson())");

        ojTryStatement.getTryPart().addToStatements("json.append(\"}}]\")");
        ojTryStatement.getTryPart().addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
        ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        ojTryStatement.getCatchPart().addToStatements("GraphDb.getDb().rollback()");
        ojTryStatement.getCatchPart().addToStatements("throw " + UmlgRestletGenerationUtil.UmlgExceptionUtilFactory.getLast() + ".getTumlExceptionUtil().handle(e)");
        ojTryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.UmlgTmpIdManager.getLast() + ".INSTANCE.remove()");
        annotatedClass.addToImports(UmlgGenerationUtil.UmlgTmpIdManager);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgExceptionUtilFactory);
        post.getBody().addToStatements(ojTryStatement);

        annotatedClass.addToImports(parentPathName);


        annotatedClass.addToImports(UmlgGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(post);
    }

    private void addPostResource(Classifier concreteClassifier, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation add = new OJAnnotatedOperation("add", UmlgGenerationUtil.UMLG_NODE);
        add.setComment("This method adds a single new instance. If and id already exist it passes the existing id back as a tmpId");
        add.setVisibility(OJVisibilityKind.PRIVATE);
        add.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(add);
        add.getBody().addToStatements(UmlgClassOperations.getPathName(concreteClassifier).getLast() + " childResource = new " + UmlgClassOperations.getPathName(concreteClassifier).getLast() + "(true)");
        annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifier));
        add.getBody().addToStatements("childResource.fromJson(propertyMap)");
        add.getBody().addToStatements("return childResource");
//        add.getBody().addToStatements("String jsonResult = childResource.toJsonWithoutCompositeParent(true)");
//        add.getBody().addToStatements("return jsonResult");
    }

    private void addPutResource(Classifier classifier, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation put = new OJAnnotatedOperation("put", UmlgGenerationUtil.UMLG_NODE);
        put.setVisibility(OJVisibilityKind.PRIVATE);
        put.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(put);
        put.getBody().addToStatements("Object id = propertyMap.get(\"id\")");
        put.getBody().addToStatements(
                UmlgClassOperations.getPathName(classifier).getLast() + " childResource = GraphDb.getDb().instantiateClassifier(id)");
        annotatedClass.addToImports(UmlgClassOperations.getPathName(classifier));
        put.getBody().addToStatements("childResource.fromJson(propertyMap)");
//        put.getBody().addToStatements("return childResource.toJsonWithoutCompositeParent()");
        put.getBody().addToStatements("return childResource");
    }

    private void addDeleteResource(Classifier classifier, OJAnnotatedClass annotatedClass) {

        OJAnnotatedOperation delete = new OJAnnotatedOperation("delete");
        delete.setVisibility(OJVisibilityKind.PRIVATE);
        delete.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(delete);
        delete.getBody().addToStatements("Object id = propertyMap.get(\"id\")");
        delete.getBody().addToStatements(
                UmlgClassOperations.getPathName(classifier).getLast() + " childResource = GraphDb.getDb().instantiateClassifier(id)");
        annotatedClass.addToImports(UmlgClassOperations.getPathName(classifier));
        delete.getBody().addToStatements("childResource.delete()");

    }

    private void addGetRootObjectRepresentation(Class clazz, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation get = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
        get.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(get);

        OJTryStatement tryStatement = new OJTryStatement();

        OJField json = new OJField("json", new OJPathName("java.lang.StringBuilder"));
        json.setInitExp("new StringBuilder()");
        tryStatement.getTryPart().addToLocals(json);

//        OJField resource = new OJField("resource", UmlgGenerationUtil.umlgSequence.getCopy().addToGenerics(UmlgClassOperations.getPathName(clazz)));
//        resource.setInitExp(StringUtils.capitalize(ModelLoader.INSTANCE.getModel().getName()) + ".INSTANCE.get" + UmlgClassOperations.className(clazz) + "()");

        OJField resource = new OJField("resource", UmlgGenerationUtil.umlgSet.getCopy().addToGenerics(UmlgClassOperations.getPathName(clazz)));
        resource.setInitExp("(" + UmlgGenerationUtil.umlgSet.getLast() + "<" + UmlgClassOperations.getPathName(clazz).getLast() + ">)" + UmlgClassOperations.getPathName(clazz).getLast() + ".allInstances()");
        tryStatement.getTryPart().addToLocals(resource);
        annotatedClass.addToImports(UmlgGenerationUtil.UmlgRootPackage.toJavaString() + "." + StringUtils.capitalize(ModelLoader.INSTANCE.getModel().getName()));

        tryStatement.getTryPart().addToStatements("json.append(\"[\")");
        SortedSet<Classifier> sortedConcreteImplementations = UmlgClassOperations.getConcreteImplementations(clazz);
        int count = 1;
        for (Classifier classifier : sortedConcreteImplementations) {

            tryStatement.getTryPart().addToStatements("json.append(\"{\\\"data\\\": [\")");

            if (sortedConcreteImplementations.size() > 1) {
                tryStatement.getTryPart().addToStatements(
                        "json.append(ToJsonUtil.toJsonWithoutCompositeParent(resource.select(new "
                                + UmlgGenerationUtil.BooleanExpressionEvaluator.getCopy().addToGenerics(UmlgClassOperations.getPathName(clazz)).getLast()
                                + "() {\n			@Override\n			public Boolean evaluate(" + UmlgClassOperations.getPathName(clazz).getLast()
                                + " e) {\n				return e.getClass() == " + UmlgClassOperations.getPathName(classifier).getLast() + ".class;\n			}\n		})))");
                annotatedClass.addToImports(UmlgGenerationUtil.BooleanExpressionEvaluator);
                annotatedClass.addToImports(UmlgClassOperations.getPathName(clazz));
                annotatedClass.addToImports(UmlgClassOperations.getPathName(classifier));
            } else {
                tryStatement.getTryPart().addToStatements("json.append(ToJsonUtil.toJsonWithoutCompositeParent(resource))");
            }

            annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);

            tryStatement.getTryPart().addToStatements("json.append(\"], \\\"meta\\\": {\")");

            tryStatement.getTryPart().addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + clazz.getQualifiedName() + "\\\"\")");
            tryStatement.getTryPart().addToStatements("json.append(\",\\\"qualifiedNameTo\\\": \\\"" + classifier.getQualifiedName() + "\\\"\")");

            // The execute ocl query resource is only required if the below
            // visitor is available
            if (RestletVisitors.containsVisitorForClass(QueryExecuteResourceBuilder.class)
                    && (classifier.getQualifiedName().equals(UmlgRestletGenerationUtil.rootQueryQualifiedName))) {
                tryStatement.getTryPart().addToStatements("json.append(\", \\\"oclExecuteUri\\\": \\\"/" + classifier.getModel().getName() + "/oclExecuteQuery\\\"\")");
            }


            annotatedClass.addToImports(UmlgClassOperations.getPathName(clazz).append(UmlgClassOperations.propertyEnumName(clazz)));
            tryStatement.getTryPart().addToStatements("json.append(\"}\")");
            if (sortedConcreteImplementations.size() != 1 && count++ != sortedConcreteImplementations.size()) {
                tryStatement.getTryPart().addToStatements("json.append(\"},\")");
            }

        }
        tryStatement.getTryPart().addToStatements("json.append(\"}]\")");
        tryStatement.getTryPart().addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

        get.getBody().addToStatements(tryStatement);
        tryStatement.setCatchPart(null);
        tryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.graphDbAccess + ".rollback()");

        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }

    private void addOptionsRootObjectRepresentation(Class clazz, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation options = new OJAnnotatedOperation("options", UmlgRestletGenerationUtil.Representation);
        options.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);

        OJTryStatement tryStatement = new OJTryStatement();

        OJField json = new OJField("json", new OJPathName("java.lang.StringBuilder"));
        json.setInitExp("new StringBuilder()");
        tryStatement.getTryPart().addToLocals(json);
        annotatedClass.addToImports(UmlgGenerationUtil.UmlgRootPackage.toJavaString() + "." + StringUtils.capitalize(ModelLoader.INSTANCE.getModel().getName()));

        tryStatement.getTryPart().addToStatements("json.append(\"[\")");
        SortedSet<Classifier> sortedConcreteImplementations = UmlgClassOperations.getConcreteImplementations(clazz);
        int count = 1;
        for (Classifier classifier : sortedConcreteImplementations) {
            annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
            tryStatement.getTryPart().addToStatements("json.append(\"{\\\"meta\\\" : {\")");
            tryStatement.getTryPart().addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + clazz.getQualifiedName() + "\\\"\")");
            // The execute ocl query resource is only required if the below
            // visitor is available
            if (RestletVisitors.containsVisitorForClass(QueryExecuteResourceBuilder.class)
                    && (classifier.getQualifiedName().equals(UmlgRestletGenerationUtil.rootQueryQualifiedName))) {
                tryStatement.getTryPart().addToStatements("json.append(\", \\\"oclExecuteUri\\\": \\\"/" + classifier.getModel().getName() + "/oclExecuteQuery\\\"\")");
            }
            tryStatement.getTryPart().addToStatements("json.append(\", \\\"to\\\": \")");

            // Meta data remains for the root object as viewing a many list does
            // not
            // change the context
            tryStatement.getTryPart().addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(classifier) + ".asJson())");
            tryStatement.getTryPart().addToStatements("json.append(\", \\\"from\\\": \")");
            tryStatement.getTryPart().addToStatements("json.append(" + StringUtils.capitalize(ModelLoader.INSTANCE.getModel().getName()) + "RuntimePropertyEnum.asJson())");
            annotatedClass.addToImports(UmlgGenerationUtil.UmlgRootPackage.toJavaString() + "." + StringUtils.capitalize(ModelLoader.INSTANCE.getModel().getName()) + "." + StringUtils.capitalize(ModelLoader.INSTANCE.getModel().getName()) + "RuntimePropertyEnum");
            annotatedClass.addToImports(UmlgClassOperations.getPathName(classifier).append(UmlgClassOperations.propertyEnumName(classifier)));

            tryStatement.getTryPart().addToStatements("json.append(\"}}\")");
            if (sortedConcreteImplementations.size() != 1 && count++ != sortedConcreteImplementations.size()) {
                tryStatement.getTryPart().addToStatements("json.append(\",\")");
            }
        }
        tryStatement.getTryPart().addToStatements("json.append(\"]\")");
        tryStatement.getTryPart().addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");

        options.getBody().addToStatements(tryStatement);
        tryStatement.setCatchPart(null);
        tryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.graphDbAccess + ".rollback()");

        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(options);
    }

    private void addToRouterEnum(Class clazz, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());
        OJEnumLiteral ojLiteral = new OJEnumLiteral(UmlgClassOperations.className(clazz).toUpperCase() + "S");

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + UmlgClassOperations.className(clazz).toLowerCase() + "s\"");
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
