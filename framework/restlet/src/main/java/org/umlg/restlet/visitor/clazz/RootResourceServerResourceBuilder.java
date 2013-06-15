package org.umlg.restlet.visitor.clazz;

import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.umlg.java.metamodel.OJBlock;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJForStatement;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.OJParameter;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJTryStatement;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedField;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJAnnotationValue;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.restlet.util.TumlRestletGenerationUtil;

@Deprecated
public class RootResourceServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Class> {

    public RootResourceServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Class clazz) {
        if (!clazz.isAbstract() && !TumlClassOperations.hasCompositeOwner(clazz)) {

            OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(TumlClassOperations.className(clazz) + "sServerResource");
            OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".restlet");
            annotatedInf.setMyPackage(ojPackage);
            addToSource(annotatedInf);

            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.className(clazz) + "sServerResourceImpl");
            annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
            annotatedClass.setMyPackage(ojPackage);
            annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
            annotatedClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));
            addToSource(annotatedClass);

            addDefaultConstructor(annotatedClass);
            addGetRootObjectRepresentation(clazz, annotatedInf, annotatedClass);
            addPostObjectRepresentation(clazz, annotatedInf, annotatedClass, REST.POST);
            addPostObjectRepresentation(clazz, annotatedInf, annotatedClass, REST.PUT);
            addPostObjectRepresentation(clazz, annotatedInf, annotatedClass, REST.DELETE);
            addToRouterEnum(clazz, annotatedClass);

        }
    }

    @Override
    public void visitAfter(Class clazz) {
    }

    private void addPostObjectRepresentation(Classifier concreteClassifier, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass, REST action) {
        OJAnnotatedOperation postInf;
        if (action == REST.POST) {
            postInf = new OJAnnotatedOperation("post", TumlRestletGenerationUtil.Representation);
        } else if (action == REST.PUT) {
            postInf = new OJAnnotatedOperation("put", TumlRestletGenerationUtil.Representation);
        } else {
            postInf = new OJAnnotatedOperation("delete", TumlRestletGenerationUtil.Representation);
        }

        postInf.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
        annotatedInf.addToOperations(postInf);
        postInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Post, "json"));

        OJAnnotatedOperation post;
        if (action == REST.POST) {
            post = new OJAnnotatedOperation("post", TumlRestletGenerationUtil.Representation);
        } else if (action == REST.PUT) {
            post = new OJAnnotatedOperation("put", TumlRestletGenerationUtil.Representation);
        } else {
            post = new OJAnnotatedOperation("delete", TumlRestletGenerationUtil.Representation);
        }

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

        OJField jsonData = new OJField("json", "java.lang.StringBuilder");
        jsonData.setInitExp("new StringBuilder()");
        ojTryStatement.getTryPart().addToLocals(jsonData);
        ojTryStatement.getTryPart().addToStatements("json.append(\"[\")");
        ojTryStatement.getTryPart().addToStatements("json.append(\"{\\\"data\\\": [\")");

        OJIfStatement ifTextNull = new OJIfStatement("entityText != null");
        ojTryStatement.getTryPart().addToStatements(ifTextNull);

        OJAnnotatedField objectO = new OJAnnotatedField("o", new OJPathName("Object"));
        objectO.setInitExp("mapper.readValue(" + entityText.getName() + ", Object.class)");
        ifTextNull.getThenPart().addToLocals(objectO);
        OJIfStatement ifArray = new OJIfStatement("o instanceof ArrayList");
        OJPathName genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJField array = new OJField("array", new OJPathName("java.util.List").addToGenerics(genericsForArray));
        array.setInitExp("(ArrayList<Map<String, Object>>)o");
        annotatedClass.addToImports("java.util.ArrayList");
        ifArray.getThenPart().addToLocals(array);

        OJField count = new OJField("count", "int");
        count.setInitExp("0");
        ifArray.getThenPart().addToLocals(count);

        ifTextNull.getThenPart().addToStatements(ifArray);
        OJForStatement forArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArray.addToThenPart(forArray);
        forArray.getBody().addToStatements("count++");
        if (action == REST.POST || action == REST.PUT) {
            forArray.getBody().addToStatements("json.append(" + action.getMethodName() + "(map))");
        } else {
            forArray.getBody().addToStatements(action.getMethodName() + "(map)");

        }
        OJIfStatement ifToAddCommaToJson = new OJIfStatement("count < array.size()");
        ifToAddCommaToJson.addToThenPart("json.append(\",\")");
        forArray.getBody().addToStatements(ifToAddCommaToJson);

        OJField map = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        map.setInitExp("(Map<String, Object>) o");
        ifArray.setElsePart(new OJBlock());
        ifArray.getElsePart().addToLocals(map);
        if (action == REST.POST || action == REST.PUT) {
            ifArray.getElsePart().addToStatements("json.append(" + action.getMethodName() + "(map))");
        } else {
            ifArray.getElsePart().addToStatements(action.getMethodName() + "(map)");
        }

        ifTextNull.addToElsePart("json.append(add())");

        if (action == REST.POST) {
            addPostResource(concreteClassifier, annotatedClass, parentPathName);
        } else if (action == REST.PUT) {
            addPutResource(concreteClassifier, annotatedClass, parentPathName);
        } else {
            addDeleteResource(concreteClassifier, annotatedClass, parentPathName);
        }

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
        annotatedClass.addToImports(TumlRestletGenerationUtil.TumlExceptionUtilFactory);
        post.getBody().addToStatements(ojTryStatement);

        annotatedClass.addToImports(parentPathName);


        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(post);
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
        annotatedClass.addToImports("org.umlg.root.Root");

        tryStatement.getTryPart().addToStatements("json.append(\"[\")");
        Set<Classifier> concreteImplementations = TumlClassOperations.getConcreteImplementations(clazz);
        int count = 1;
        for (Classifier classifier : concreteImplementations) {

            tryStatement.getTryPart().addToStatements("json.append(\"{\\\"data\\\": [\")");

            if (concreteImplementations.size() > 1) {
                tryStatement.getTryPart().addToStatements(
                        "json.append(ToJsonUtil.toJson(resource.select(new "
                                + TinkerGenerationUtil.BooleanExpressionEvaluator.getCopy().addToGenerics(TumlClassOperations.getPathName(clazz)).getLast()
                                + "() {\n			@Override\n			public Boolean evaluate(" + TumlClassOperations.getPathName(clazz).getLast()
                                + " e) {\n				return e instanceof " + TumlClassOperations.getPathName(classifier).getLast() + ";\n			}\n		})))");
                annotatedClass.addToImports(TinkerGenerationUtil.BooleanExpressionEvaluator);
                annotatedClass.addToImports(TumlClassOperations.getPathName(clazz));
                annotatedClass.addToImports(TumlClassOperations.getPathName(classifier));
            } else {
                tryStatement.getTryPart().addToStatements("json.append(" + TinkerGenerationUtil.ToJsonUtil.getLast() + ".toJson(resource))");
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

        //Add the url for post/put to the resource
        ojLiteral = new OJEnumLiteral(TumlClassOperations.className(clazz).toUpperCase() + "S_" + clazz.getName());

        uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + TumlClassOperations.className(clazz).toLowerCase() + "s" + "_" + clazz.getName() + "\"");
        ojLiteral.addToAttributeValues(uri);

        serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(annotatedClass.getName() + ".class");
        ojLiteral.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(annotatedClass.getPathName());
        routerEnum.addToImports(TumlRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");


    }

    protected void addPostResource(Classifier concreteClassifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation add = new OJAnnotatedOperation("add", "String");
        add.setComment("This method adds a single new instance. If and id already exist it passes the existing id back as a tmpId");
        add.setVisibility(OJVisibilityKind.PRIVATE);
        add.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(add);
        add.getBody().addToStatements(TumlClassOperations.getPathName(concreteClassifier).getLast() + " childResource = new " + TumlClassOperations.getPathName(concreteClassifier).getLast() + "(true)");
        annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifier));
        add.getBody().addToStatements("childResource.fromJson(propertyMap)");
        add.getBody().addToStatements("String jsonResult = childResource.toJson()");
        OJIfStatement ifContainsId = new OJIfStatement("propertyMap.containsKey(\"id\")");
        ifContainsId.addToThenPart("Long tmpId = Long.valueOf((Integer) propertyMap.get(\"id\"))");
        ifContainsId.addToThenPart("jsonResult = jsonResult.substring(1);");
        ifContainsId.addToThenPart("jsonResult = \"{\\\"tmpId\\\": \" + tmpId + \", \" + jsonResult;");
        add.getBody().addToStatements(ifContainsId);
        add.getBody().addToStatements("return jsonResult");

        OJAnnotatedOperation addWithoutData = new OJAnnotatedOperation("add", "String");
        addWithoutData.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToOperations(addWithoutData);
        addWithoutData.getBody().addToStatements(TumlClassOperations.getPathName(concreteClassifier).getLast() + " childResource = new " + TumlClassOperations.getPathName(concreteClassifier).getLast() + "(true)");
        annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifier));
        addWithoutData.getBody().addToStatements("return childResource.toJson()");
    }

    protected void addPutResource(Classifier classifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation put = new OJAnnotatedOperation("put", "String");
        put.setVisibility(OJVisibilityKind.PRIVATE);
        put.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(put);
        put.getBody().addToStatements("Long id = Long.valueOf((Integer)propertyMap.get(\"id\"))");
        put.getBody().addToStatements(
                TumlClassOperations.getPathName(classifier).getLast() + " childResource = GraphDb.getDb().instantiateClassifier(id)");
        annotatedClass.addToImports(TumlClassOperations.getPathName(classifier));
        put.getBody().addToStatements("childResource.fromJson(propertyMap)");
        put.getBody().addToStatements("return childResource.toJson()");
    }

    protected void addDeleteResource(Classifier classifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
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
