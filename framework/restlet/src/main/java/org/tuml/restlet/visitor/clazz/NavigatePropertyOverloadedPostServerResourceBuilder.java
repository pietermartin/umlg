package org.tuml.restlet.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.java.metamodel.*;
import org.tuml.java.metamodel.annotation.*;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.restlet.generation.RestletVisitors;
import org.tuml.restlet.util.TumlRestletGenerationUtil;
import org.tuml.restlet.visitor.model.QueryExecuteResourceBuilder;

import java.util.Set;

public class NavigatePropertyOverloadedPostServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Property> {

    private static final String OVERLOADED_POST = "OverloadedPost";

    public NavigatePropertyOverloadedPostServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper pWrap = new PropertyWrapper(p);
        if (!pWrap.isDataType() && !pWrap.isEnumeration() && pWrap.isNavigable()) {

            Set<Classifier> concreteImplementations = TumlClassOperations.getConcreteImplementations((Classifier) pWrap.getType());
            for (Classifier classifier : concreteImplementations) {
                OJAnnotatedClass owner = findOJClass(classifier);

                OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_"
                        + pWrap.getOtherEnd().getName() + "_" + pWrap.getName() + "_" + StringUtils.capitalize(classifier.getName()) + "_" + OVERLOADED_POST + "_ServerResource");
                OJPackage ojPackage = new OJPackage(owner.getMyPackage().toString() + ".restlet");
                annotatedInf.setMyPackage(ojPackage);
                addToSource(annotatedInf);

                OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_"
                        + pWrap.getOtherEnd().getName() + "_" + pWrap.getName() + "_" + StringUtils.capitalize(classifier.getName()) + "_" + OVERLOADED_POST + "_ServerResourceImpl");
                annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
                annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
                annotatedClass.setMyPackage(ojPackage);
                addToSource(annotatedClass);
                addDefaultConstructor(annotatedClass);

                addCompositeParentIdField(pWrap, annotatedClass);
                addGetObjectRepresentation(pWrap, annotatedInf, annotatedClass);

                // Put must be Idempotence, i.e. calling it many times must make
                // no
                // difference to server state
                // non unique sequence or a bag can not put as adding the same
                // value
                // more than once changes the state
                addPostObjectRepresentation(classifier, pWrap, annotatedInf, annotatedClass);
//                addPutDeleteObjectRepresentation(pWrap, annotatedInf, annotatedClass, true);
//                addPutDeleteObjectRepresentation(pWrap, annotatedInf, annotatedClass, false);
                addServerResourceToRouterEnum(classifier, pWrap, annotatedClass);
            }

        }
    }

    @Override
    public void visitAfter(Property p) {
    }

    private void addGetObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass) {

        OJAnnotatedOperation get = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
        get.addToThrows(TumlRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);

        OJTryStatement tryStatement = new OJTryStatement();

        OJPathName parentPathName = TumlClassOperations.getPathName(pWrap.getOtherEnd().getType());
        tryStatement.getTryPart().addToStatements(
                "this." + parentPathName.getLast().toLowerCase() + "Id = (String)getRequestAttributes().get(\""
                        + parentPathName.getLast().toLowerCase() + "Id\")");
        tryStatement.getTryPart().addToStatements(
                parentPathName.getLast() + " parentResource = GraphDb.getDb().instantiateClassifier(" + parentPathName.getLast().toLowerCase() + "Id" + ")");
        annotatedClass.addToImports(parentPathName);
        buildToJson(pWrap, annotatedClass, tryStatement.getTryPart());
        get.getBody().addToStatements(tryStatement);
        tryStatement.getFinallyPart().addToStatements(TinkerGenerationUtil.graphDbAccess + ".rollback()");
        tryStatement.setCatchPart(null);
        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }

    private void addDeleteResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation delete = new OJAnnotatedOperation("delete");
        delete.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToOperations(delete);
        annotatedClass.addToImports(pWrap.javaBaseTypePath());
        if (pWrap.isComposite()) {
            delete.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
            delete.getBody().addToStatements("String id = (String)propertyMap.get(\"id\")");
            delete.getBody().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = GraphDb.getDb().instantiateClassifier(id)");
            delete.getBody().addToStatements("childResource.delete()");
        } else {
            delete.addToParameters(new OJParameter("parentResource", parentPathName));
            delete.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
            delete.getBody().addToStatements("String id = (String)propertyMap.get(\"id\")");
            delete.getBody().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = GraphDb.getDb().instantiateClassifier(id)");
            delete.getBody().addToStatements("parentResource." + pWrap.remover() + "(childResource)");
        }

    }

    private void addPostObjectRepresentation(Classifier concreteClassifier, PropertyWrapper pWrap, OJAnnotatedInterface annotatedInf,
                                             OJAnnotatedClass annotatedClass) {
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

        checkIfTransactionSuspended(post);

        PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());

        OJPathName parentPathName = otherEndPWrap.javaBaseTypePath();
        post.getBody().addToStatements(
                "this." + parentPathName.getLast().toLowerCase() + "Id = (String)getRequestAttributes().get(\""
                        + parentPathName.getLast().toLowerCase() + "Id\")");
        post.getBody().addToStatements(
                parentPathName.getLast() + " parentResource = GraphDb.getDb().instantiateClassifier(" + parentPathName.getLast().toLowerCase() + "Id" + ")");

        OJTryStatement ojTryStatement = new OJTryStatement();
        OJField mapper = new OJField("mapper", TinkerGenerationUtil.ObjectMapper);
        mapper.setInitExp("new ObjectMapper()");
        ojTryStatement.getTryPart().addToLocals(mapper);
        OJPathName pathName = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJAnnotatedField entityText = new OJAnnotatedField("entityText", "String");
        entityText.setInitExp("entity.getText()");
        ojTryStatement.getTryPart().addToLocals(entityText);
        OJAnnotatedField objectO = new OJAnnotatedField("overloaded", pathName);
        objectO.setInitExp("mapper.readValue(" + entityText.getName() + ", Map.class)");
        ojTryStatement.getTryPart().addToLocals(objectO);

        OJField overloaded = new OJField("o", new OJPathName("Object"));
        overloaded.setInitExp("overloaded.get(\"insert\")");
        ojTryStatement.getTryPart().addToLocals(overloaded);

        //Insert
        OJIfStatement ifInsert = new OJIfStatement("o != null");
        OJIfStatement ifArrayForInsert = new OJIfStatement("o instanceof ArrayList");
        OJPathName genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJField array = new OJField("array", new OJPathName("java.util.ArrayList").addToGenerics(genericsForArray));
        array.setInitExp("(ArrayList<Map<String, Object>>)o");
        ifArrayForInsert.getThenPart().addToLocals(array);
        ifInsert.addToThenPart(ifArrayForInsert);
        ojTryStatement.getTryPart().addToStatements(ifInsert);
        OJForStatement forArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForInsert.addToThenPart(forArray);
        forArray.getBody().addToStatements("add(parentResource, map)");

        OJField map = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        map.setInitExp("(Map<String, Object>) o");
        ifArrayForInsert.setElsePart(new OJBlock());
        ifArrayForInsert.getElsePart().addToLocals(map);
        ifArrayForInsert.getElsePart().addToStatements("add(parentResource, map)");

        addPostResource(concreteClassifier, pWrap, annotatedClass, parentPathName);

        //Delete
        ojTryStatement.getTryPart().addToStatements("o = overloaded.get(\"delete\")");
        OJIfStatement ifDelete = new OJIfStatement("o != null");
        OJIfStatement ifArrayForDelete = new OJIfStatement("o instanceof ArrayList");
        genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        array = new OJField("array", new OJPathName("java.util.ArrayList").addToGenerics(genericsForArray));
        array.setInitExp("(ArrayList<Map<String, Object>>)o");
        ifArrayForDelete.getThenPart().addToLocals(array);
        ifDelete.addToThenPart(ifArrayForDelete);
        ojTryStatement.getTryPart().addToStatements(ifDelete);
        forArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForDelete.addToThenPart(forArray);
        if (pWrap.isComposite()) {
            forArray.getBody().addToStatements("delete(map)");
        } else {
            forArray.getBody().addToStatements("delete(parentResource, map)");
        }

        map = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        map.setInitExp("(Map<String, Object>) o");
        ifArrayForDelete.setElsePart(new OJBlock());
        ifArrayForDelete.getElsePart().addToLocals(map);
        if (pWrap.isComposite()) {
            ifArrayForDelete.getElsePart().addToStatements("delete(map)");
        } else {
            ifArrayForDelete.getElsePart().addToStatements("delete(parentResource, map)");
        }
        addDeleteResource(pWrap, annotatedClass, parentPathName);

        //Update
        ojTryStatement.getTryPart().addToStatements("o = overloaded.get(\"update\")");
        OJIfStatement ifUpdate = new OJIfStatement("o != null");
        OJIfStatement ifArrayForUpdate = new OJIfStatement("o instanceof ArrayList");
        genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        array = new OJField("array", new OJPathName("java.util.ArrayList").addToGenerics(genericsForArray));
        array.setInitExp("(ArrayList<Map<String, Object>>)o");
        ifArrayForUpdate.getThenPart().addToLocals(array);
        ifUpdate.addToThenPart(ifArrayForUpdate);
        ojTryStatement.getTryPart().addToStatements(ifUpdate);
        forArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForUpdate.addToThenPart(forArray);
        forArray.getBody().addToStatements("put(map)");

        map = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        map.setInitExp("(Map<String, Object>) o");
        ifArrayForUpdate.setElsePart(new OJBlock());
        ifArrayForUpdate.getElsePart().addToLocals(map);
        ifArrayForUpdate.getElsePart().addToStatements("put(map)");

        addPutResource(pWrap, annotatedClass, parentPathName);

//        ojTryStatement.getTryPart().addToStatements("GraphDb.getDb().commit()");
        commitIfNotFromResume(ojTryStatement.getTryPart());

        ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        ojTryStatement.getCatchPart().addToStatements("GraphDb.getDb().rollback()");

        ojTryStatement.getCatchPart().addToStatements(TumlRestletGenerationUtil.TumlExceptionUtilFactory.getLast() + ".getTumlExceptionUtil().handle(e)");
        annotatedClass.addToImports(TumlRestletGenerationUtil.TumlExceptionUtilFactory);

        post.getBody().addToStatements(ojTryStatement);

        annotatedClass.addToImports(parentPathName);

        post.getBody().addToStatements("return get()");

        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(post);
    }

    private void addPutResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation put = new OJAnnotatedOperation("put");
        put.setVisibility(OJVisibilityKind.PRIVATE);
        put.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(put);
        put.getBody().addToStatements("String id = (String)propertyMap.get(\"id\")");
        put.getBody().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = GraphDb.getDb().instantiateClassifier(id)");
        annotatedClass.addToImports(pWrap.javaBaseTypePath());
        put.getBody().addToStatements("childResource.fromJson(propertyMap)");
    }

    private void addPostResource(Classifier concreteClassifier, PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation add = new OJAnnotatedOperation("add");
        add.setVisibility(OJVisibilityKind.PRIVATE);
        add.addToParameters(new OJParameter("parentResource", parentPathName));
        add.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(add);
        if (pWrap.isComposite()) {
            add.getBody().addToStatements(
                    pWrap.javaBaseTypePath().getLast() + " childResource = new " + TumlClassOperations.getPathName(concreteClassifier).getLast() + "(true)");
        } else {
            add.getBody().addToStatements("String id = (String)propertyMap.get(\"id\")");
            add.getBody().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = GraphDb.getDb().instantiateClassifier(id)");
        }
        annotatedClass.addToImports(pWrap.javaBaseTypePath());
        annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifier));
        add.getBody().addToStatements("childResource.fromJson(propertyMap)");
        if (pWrap.isOrdered()) {
            annotatedClass.addToImports(TumlRestletGenerationUtil.Parameter);
            add.getBody().addToStatements("Parameter indexParameter = getQuery().getFirst(\"index\")");
            OJIfStatement ifIndexNull = new OJIfStatement("indexParameter != null");
            ifIndexNull.addToThenPart("int index = Integer.valueOf(indexParameter.getValue())");
            ifIndexNull.addToThenPart("parentResource." + pWrap.getter() + "().add(index, childResource)");
            ifIndexNull.addToElsePart("parentResource." + pWrap.adder() + "(childResource)");
            add.getBody().addToStatements(ifIndexNull);
        } else {
            add.getBody().addToStatements("parentResource." + pWrap.adder() + "(childResource)");
        }
    }

    private void buildToJson(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJBlock block) {

        Set<Classifier> concreteImplementations = TumlClassOperations.getConcreteImplementations((Classifier) pWrap.getType());
        Set<Classifier> concreteImplementationsFrom = TumlClassOperations.getConcreteImplementations((Classifier) pWrap.getOwningType());
        if (!concreteImplementationsFrom.isEmpty()) {
            annotatedClass.addToImports(TinkerGenerationUtil.ToJsonUtil);
            block.addToStatements("StringBuilder json = new StringBuilder()");
            block.addToStatements("json.append(\"[\")");

            int count = 1;
            // For meta data, put where one is navigating to first, then where on is
            // navigating from
            // This is consistent with navigating to a entity with a vertex where
            // there is no navigating from.
            // i.e. the first meta data in the array is the entity navigating to.
            for (Classifier concreteClassifierTo : concreteImplementations) {
                annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifierTo));
                block.addToStatements("json.append(\"{\\\"data\\\": [\")");
                if (pWrap.isOne()) {
                    OJIfStatement ifOneInstanceOf = new OJIfStatement("parentResource." + pWrap.getter() + "() instanceof "
                            + TumlClassOperations.getPathName(concreteClassifierTo).getLast());
                    ifOneInstanceOf.addToThenPart("json.append(ToJsonUtil.toJsonWithoutCompositeParent(parentResource." + pWrap.getter() + "()))");
                    block.addToStatements(ifOneInstanceOf);
                } else {
                    block.addToStatements("json.append(ToJsonUtil.toJsonWithoutCompositeParent(parentResource." + pWrap.getter() + "().select(new "
                            + TinkerGenerationUtil.BooleanExpressionEvaluator.getCopy().addToGenerics(TumlClassOperations.getPathName(pWrap.getType())).getLast()
                            + "() {\n			@Override\n			public Boolean evaluate(" + TumlClassOperations.getPathName(pWrap.getType()).getLast()
                            + " e) {\n				return e instanceof " + TumlClassOperations.getPathName(concreteClassifierTo).getLast() + ";\n			}\n		})))");
                    annotatedClass.addToImports(TinkerGenerationUtil.BooleanExpressionEvaluator);
                }
                annotatedClass.addToImports(TumlClassOperations.getPathName(pWrap.getType()));
                block.addToStatements("json.append(\"],\")");

                block.addToStatements("json.append(\" \\\"meta\\\" : {\")");
                // The execute ocl query resource is only required if the below
                // visitor is availeble
                if (RestletVisitors.containsVisitorForClass(QueryExecuteResourceBuilder.class)
                        && (pWrap.getType().getQualifiedName().equals(TumlRestletGenerationUtil.instanceQueryQualifiedName) || pWrap.getType().getQualifiedName().equals(TumlRestletGenerationUtil.classQueryQualifiedName))) {
                    block.addToStatements("json.append(\"\\\"oclExecuteUri\\\": \\\"/" + pWrap.getModel().getName() + "/{contextId}/oclExecuteQuery\\\", \")");
                }
                block.addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "\\\"\")");
                block.addToStatements("json.append(\", \\\"to\\\": \")");
                int countFrom = 1;
                OJIfStatement ifStatementFrom = new OJIfStatement();
                for (Classifier concreteClassifierFrom : concreteImplementationsFrom) {
                    OJBlock conditionBlockFrom = new OJBlock();
                    annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifierFrom));
                    String condition = "parentResource instanceof " + TumlClassOperations.getPathName(concreteClassifierFrom).getLast();
                    if (countFrom == 1) {
                        ifStatementFrom.setCondition(condition);
                        ifStatementFrom.setThenPart(conditionBlockFrom);
                    } else if (countFrom == concreteImplementationsFrom.size()) {
                        ifStatementFrom.setElsePart(conditionBlockFrom);
                    } else {
                        conditionBlockFrom = ifStatementFrom.addToElseIfCondition(condition, "");
                    }
                    conditionBlockFrom.addToStatements("json.append(" + TumlClassOperations.propertyEnumName(concreteClassifierTo) + ".asJson())");
                    conditionBlockFrom.addToStatements("json.append(\", \\\"from\\\": \")");
                    conditionBlockFrom.addToStatements("json.append(" + TumlClassOperations.propertyEnumName(concreteClassifierFrom) + ".asJson())");
                    annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifierFrom).append(
                            TumlClassOperations.propertyEnumName(concreteClassifierFrom)));
                    countFrom++;
                }
                block.addToStatements(ifStatementFrom);

                annotatedClass.addToImports(TumlClassOperations.getPathName(pWrap.getOwningType()).append(
                        TumlClassOperations.propertyEnumName(pWrap.getOwningType())));
                annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifierTo)
                        .append(TumlClassOperations.propertyEnumName(concreteClassifierTo)));
                block.addToStatements("json.append(\"}\")");
                if (concreteImplementations.size() != 1 && count != concreteImplementations.size()) {
                    block.addToStatements("json.append(\"}, \")");
                }
                count++;
            }
            block.addToStatements("json.append(\"}]\")");
            block.addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
        } else {
            //TODO not thought through
            block.addToStatements("return null");
        }
    }

    private void addServerResourceToRouterEnum(Classifier concreteClassifier, PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");

        OJEnumLiteral ojLiteral = new OJEnumLiteral((TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_" + OVERLOADED_POST + "_" + pWrap.fieldname()
                + "_" + concreteClassifier.getName()).toUpperCase());

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/overloadedpost/" + TumlClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "s/{"
                + TumlClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "Id}/" + pWrap.fieldname() + "\"");
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
        ojLiteral = new OJEnumLiteral((TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_" + OVERLOADED_POST + "_" + pWrap.fieldname()
                + "_" + pWrap.fieldname() + "_" + concreteClassifier.getName()).toUpperCase());

        uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/overloadedpost/" + TumlClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "s/{"
                + TumlClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "Id}/" + pWrap.fieldname() + "_" + concreteClassifier.getName() + "\"");
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

    private void addCompositeParentIdField(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJField compositeParentFieldId = new OJField(TumlClassOperations.getPathName(pWrap.getOtherEnd().getType()).getLast().toLowerCase() + "Id",
                new OJPathName("String"));
        compositeParentFieldId.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToFields(compositeParentFieldId);
    }

}
