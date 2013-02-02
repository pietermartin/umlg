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

public class TransactionalNavigatePropertyServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Property> {

    private static final String TRANSACTION = "Transaction";

    public TransactionalNavigatePropertyServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper pWrap = new PropertyWrapper(p);
        if (!pWrap.isDataType() && !pWrap.isEnumeration() && pWrap.isNavigable()) {

            Set<Classifier> concreteImplementations = TumlClassOperations.getConcreteImplementations((Classifier) pWrap.getType());
            for (Classifier classifier : concreteImplementations) {
                OJAnnotatedClass owner = findOJClass(classifier);

                OJAnnotatedInterface annotatedInf = new OJAnnotatedInterface(TRANSACTION + "_" + TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_"
                        + pWrap.getOtherEnd().getName() + "_" + pWrap.getName() + "_" + StringUtils.capitalize(classifier.getName()) + "_ServerResource");
                OJPackage ojPackage = new OJPackage(owner.getMyPackage().toString() + ".restlet");
                annotatedInf.setMyPackage(ojPackage);
                addToSource(annotatedInf);

                OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TRANSACTION + "_" + TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_"
                        + pWrap.getOtherEnd().getName() + "_" + pWrap.getName() + "_" + StringUtils.capitalize(classifier.getName()) + "_ServerResourceImpl");
                annotatedClass.setSuperclass(TumlRestletGenerationUtil.ServerResource);
                annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
                annotatedClass.setMyPackage(ojPackage);
                addToSource(annotatedClass);
                addDefaultConstructor(annotatedClass);

                addCompositeParentIdAndTransactionUidField(pWrap, annotatedClass);
                addGetObjectRepresentation(pWrap, annotatedInf, annotatedClass);

                // Put must be Idempotence, i.e. calling it many times must make
                // no
                // difference to server state
                // non unique sequence or a bag can not put as adding the same
                // value
                // more than once changes the state
                addPostObjectRepresentation(classifier, pWrap, annotatedInf, annotatedClass);
                addPutDeleteObjectRepresentation(pWrap, annotatedInf, annotatedClass, true);
                addPutDeleteObjectRepresentation(pWrap, annotatedInf, annotatedClass, false);
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

        OJPathName parentPathName = TumlClassOperations.getPathName(pWrap.getOtherEnd().getType());
        get.getBody().addToStatements(
                "this." + parentPathName.getLast().toLowerCase() + "Id = Integer.parseInt((String)getRequestAttributes().get(\""
                        + parentPathName.getLast().toLowerCase() + "Id\"))");
        get.getBody().addToStatements(
                parentPathName.getLast() + " parentResource = GraphDb.getDb().instantiateClassifier(" + parentPathName.getLast().toLowerCase() + "Id" + ")");
        annotatedClass.addToImports(parentPathName);
        buildToJson(pWrap, annotatedClass, get.getBody());
        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }

    private void addPutDeleteObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedInterface annotatedInf, OJAnnotatedClass annotatedClass, boolean put) {
        OJAnnotatedOperation putOrDeleteInf = new OJAnnotatedOperation(put ? "put" : "delete", TumlRestletGenerationUtil.Representation);
        putOrDeleteInf.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
        annotatedInf.addToOperations(putOrDeleteInf);
        putOrDeleteInf.addAnnotationIfNew(new OJAnnotationValue(put ? TumlRestletGenerationUtil.Put : TumlRestletGenerationUtil.Delete, "validation"));

        OJAnnotatedOperation putOrDelete = new OJAnnotatedOperation(put ? "put" : "delete", TumlRestletGenerationUtil.Representation);
        putOrDelete.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
        putOrDelete.addToThrows(TumlRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
        TinkerGenerationUtil.addOverrideAnnotation(putOrDelete);
        TinkerGenerationUtil.addSuppressWarning(putOrDelete);

        PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());

        //get the transact uid
        putOrDelete.getBody().addToStatements("this.transactionUid = (String)getRequestAttributes().get(\"transactionUid\")");
        //get the object id

        //resume the transaction
        putOrDelete.getBody().addToStatements(
                TinkerGenerationUtil.TransactionIdentifier.getLast() + " transactionIdentifier = " + TinkerGenerationUtil.TransactionCache.getLast() + ".INSTANCE.get(this.transactionUid)");
        putOrDelete.getBody().addToStatements(TinkerGenerationUtil.graphDbAccess + ".resume(transactionIdentifier)");
        annotatedClass.addToImports(TinkerGenerationUtil.TransactionIdentifier);
        annotatedClass.addToImports(TinkerGenerationUtil.TransactionCache);


        OJTryStatement ojTryStatement = new OJTryStatement();

        OJPathName parentPathName = otherEndPWrap.javaBaseTypePath();
        if (!pWrap.isComposite() && !put) {
            ojTryStatement.getTryPart().addToStatements(
                    "this." + parentPathName.getLast().toLowerCase() + "Id = Integer.parseInt((String)getRequestAttributes().get(\""
                            + parentPathName.getLast().toLowerCase() + "Id\"))");
            ojTryStatement.getTryPart()
                    .addToStatements(
                            parentPathName.getLast() + " parentResource = GraphDb.getDb().instantiateClassifier(" + parentPathName.getLast().toLowerCase()
                                    + "Id" + ")");
        }

        OJField mapper = new OJField("mapper", TinkerGenerationUtil.ObjectMapper);
        mapper.setInitExp("new ObjectMapper()");
        ojTryStatement.getTryPart().addToLocals(mapper);
        OJAnnotatedField objectO = new OJAnnotatedField("o", new OJPathName("Object"));
        objectO.setInitExp("mapper.readValue(entity.getText(), Object.class)");
        ojTryStatement.getTryPart().addToLocals(objectO);
        OJIfStatement ifArray = new OJIfStatement("o instanceof ArrayList");
        OJPathName genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJField array = new OJField("array", new OJPathName("java.util.ArrayList").addToGenerics(genericsForArray));
        array.setInitExp("(ArrayList<Map<String, Object>>)o");
        ifArray.getThenPart().addToLocals(array);
        ojTryStatement.getTryPart().addToStatements(ifArray);
        OJForStatement forArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArray.addToThenPart(forArray);
        if (put) {
            forArray.getBody().addToStatements("put(map)");
        } else if (pWrap.isComposite()) {
            forArray.getBody().addToStatements("delete(map)");
        } else {
            forArray.getBody().addToStatements("delete(parentResource, map)");
        }

        OJField map = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        map.setInitExp("(Map<String, Object>) o");
        ifArray.setElsePart(new OJBlock());
        ifArray.getElsePart().addToLocals(map);
        if (put) {
            ifArray.getElsePart().addToStatements("put(map)");
        } else if (pWrap.isComposite()) {
            ifArray.getElsePart().addToStatements("delete(map)");
        } else {
            ifArray.getElsePart().addToStatements("delete(parentResource, map)");
        }

        if (put) {
            addPutResource(pWrap, annotatedClass, parentPathName);
        } else {
            addDeleteResource(pWrap, annotatedClass, parentPathName);
        }

        ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        ojTryStatement.getCatchPart().addToStatements(TinkerGenerationUtil.graphDbAccess + ".setRollbackOnly()");

        OJIfStatement ifRuntime = new OJIfStatement("e instanceof RuntimeException");
        ifRuntime.addToThenPart("throw (RuntimeException)e");
        ojTryStatement.getCatchPart().addToStatements(ifRuntime);
        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");

        ojTryStatement.getFinallyPart().addToStatements(TinkerGenerationUtil.graphDbAccess + ".suspend()");
        ojTryStatement.getFinallyPart().addToStatements(TinkerGenerationUtil.TransactionCache+ ".INSTANCE.put(transactionIdentifier)");

        putOrDelete.getBody().addToStatements(ojTryStatement);

        annotatedClass.addToImports(parentPathName);

        putOrDelete.getBody().addToStatements("return get()");

//        putOrDelete.getBody().addToStatements(
//                "this." + parentPathName.getLast().toLowerCase() + "Id = Integer.parseInt((String)getRequestAttributes().get(\""
//                        + parentPathName.getLast().toLowerCase() + "Id\"))");
//        putOrDelete.getBody().addToStatements(
//                parentPathName.getLast() + " parentResource = GraphDb.getDb().instantiateClassifier( " + parentPathName.getLast().toLowerCase() + "Id" + ")");
//
//        buildToJson(pWrap, annotatedClass, putOrDelete.getBody());

        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(putOrDelete);
    }

    private void addDeleteResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation delete = new OJAnnotatedOperation("delete");
        delete.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToOperations(delete);
        annotatedClass.addToImports(pWrap.javaBaseTypePath());
        if (pWrap.isComposite()) {
            delete.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
            delete.getBody().addToStatements("Integer id = (Integer)propertyMap.get(\"id\")");
            delete.getBody().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = GraphDb.getDb().instantiateClassifier(Long.valueOf(id))");
            delete.getBody().addToStatements("childResource.delete()");
        } else {
            delete.addToParameters(new OJParameter("parentResource", parentPathName));
            delete.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
            delete.getBody().addToStatements("Integer id = (Integer)propertyMap.get(\"id\")");
            delete.getBody().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = GraphDb.getDb().instantiateClassifier(Long.valueOf(id))");
            delete.getBody().addToStatements("parentResource." + pWrap.remover() + "(childResource)");
        }

    }

    private void addPostObjectRepresentation(Classifier concreteClassifier, PropertyWrapper pWrap, OJAnnotatedInterface annotatedInf,
                                             OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation postInf = new OJAnnotatedOperation("post", TumlRestletGenerationUtil.Representation);
        postInf.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
        annotatedInf.addToOperations(postInf);
        postInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Post, "validation"));

        OJAnnotatedOperation post = new OJAnnotatedOperation("post", TumlRestletGenerationUtil.Representation);
        post.addToParameters(new OJParameter("entity", TumlRestletGenerationUtil.Representation));
        post.addToThrows(TumlRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(TumlRestletGenerationUtil.ResourceException);
        TinkerGenerationUtil.addOverrideAnnotation(post);
        TinkerGenerationUtil.addSuppressWarning(post);

        PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());

        OJPathName parentPathName = otherEndPWrap.javaBaseTypePath();

        //get the transact uid
        post.getBody().addToStatements("this.transactionUid = (String)getRequestAttributes().get(\"transactionUid\")");
        //get the object id
        post.getBody().addToStatements(
                "this." + parentPathName.getLast().toLowerCase() + "Id = Integer.parseInt((String)getRequestAttributes().get(\""
                        + parentPathName.getLast().toLowerCase() + "Id\"))");

        //resume the transaction
        post.getBody().addToStatements(
                TinkerGenerationUtil.TransactionIdentifier.getLast() + " transactionIdentifier = " + TinkerGenerationUtil.TransactionCache.getLast() + ".INSTANCE.get(this.transactionUid)");
        post.getBody().addToStatements(TinkerGenerationUtil.graphDbAccess + ".resume(transactionIdentifier)");
        annotatedClass.addToImports(TinkerGenerationUtil.TransactionIdentifier);
        annotatedClass.addToImports(TinkerGenerationUtil.TransactionCache);

        post.getBody().addToStatements(
                parentPathName.getLast() + " parentResource = GraphDb.getDb().instantiateClassifier(" + parentPathName.getLast().toLowerCase() + "Id" + ")");

        OJTryStatement ojTryStatement = new OJTryStatement();
        OJField mapper = new OJField("mapper", TinkerGenerationUtil.ObjectMapper);
        mapper.setInitExp("new ObjectMapper()");
        ojTryStatement.getTryPart().addToLocals(mapper);
        OJAnnotatedField objectO = new OJAnnotatedField("o", new OJPathName("Object"));
        objectO.setInitExp("mapper.readValue(entity.getText(), Object.class)");
        ojTryStatement.getTryPart().addToLocals(objectO);
        OJIfStatement ifArray = new OJIfStatement("o instanceof ArrayList");
        OJPathName genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJField array = new OJField("array", new OJPathName("java.util.ArrayList").addToGenerics(genericsForArray));
        array.setInitExp("(ArrayList<Map<String, Object>>)o");
        ifArray.getThenPart().addToLocals(array);
        ojTryStatement.getTryPart().addToStatements(ifArray);
        OJForStatement forArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArray.addToThenPart(forArray);
        forArray.getBody().addToStatements("add(parentResource, map)");

        OJField map = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        map.setInitExp("(Map<String, Object>) o");
        ifArray.setElsePart(new OJBlock());
        ifArray.getElsePart().addToLocals(map);
        ifArray.getElsePart().addToStatements("add(parentResource, map)");

        addPostResource(concreteClassifier, pWrap, annotatedClass, parentPathName);

        annotatedClass.addToImports(TinkerGenerationUtil.tinkerConclusionPathName);

        ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        ojTryStatement.getCatchPart().addToStatements(TinkerGenerationUtil.graphDbAccess + ".setRollbackOnly()");

        OJIfStatement ifRuntime = new OJIfStatement("e instanceof RuntimeException");
        ifRuntime.addToThenPart("throw (RuntimeException)e");
        ojTryStatement.getCatchPart().addToStatements(ifRuntime);
        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");

        ojTryStatement.getFinallyPart().addToStatements(TinkerGenerationUtil.graphDbAccess + ".suspend()");
        ojTryStatement.getFinallyPart().addToStatements(TinkerGenerationUtil.TransactionCache+ ".INSTANCE.put(transactionIdentifier)");

        post.getBody().addToStatements(ojTryStatement);

        annotatedClass.addToImports(parentPathName);

        post.getBody().addToStatements("return get()");

//        buildToJson(pWrap, annotatedClass, post.getBody());

        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(post);
    }

    private void addPutResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation put = new OJAnnotatedOperation("put");
        put.setVisibility(OJVisibilityKind.PRIVATE);
        put.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(put);
        put.getBody().addToStatements("Integer id = (Integer)propertyMap.get(\"id\")");
        put.getBody().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = GraphDb.getDb().instantiateClassifier(Long.valueOf(id))");
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
            add.getBody().addToStatements("Integer id = (Integer)propertyMap.get(\"id\")");
            add.getBody().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = GraphDb.getDb().instantiateClassifier(Long.valueOf(id))");
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
            block.addToStatements("StringBuilder validation = new StringBuilder()");
            block.addToStatements("validation.append(\"[\")");

            int count = 1;
            // For meta data, put where one is navigating to first, then where on is
            // navigating from
            // This is consistent with navigating to a entity with a vertex where
            // there is no navigating from.
            // i.e. the first meta data in the array is the entity navigating to.
            for (Classifier concreteClassifierTo : concreteImplementations) {
                annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifierTo));
                block.addToStatements("validation.append(\"{\\\"data\\\": [\")");
                if (pWrap.isOne()) {
                    OJIfStatement ifOneInstanceOf = new OJIfStatement("parentResource." + pWrap.getter() + "() instanceof "
                            + TumlClassOperations.getPathName(concreteClassifierTo).getLast());
                    ifOneInstanceOf.addToThenPart("validation.append(ToJsonUtil.toJsonWithoutCompositeParent(parentResource." + pWrap.getter() + "()))");
                    block.addToStatements(ifOneInstanceOf);
                } else {
                    block.addToStatements("validation.append(ToJsonUtil.toJsonWithoutCompositeParent(parentResource." + pWrap.getter() + "().select(new "
                            + TinkerGenerationUtil.BooleanExpressionEvaluator.getCopy().addToGenerics(TumlClassOperations.getPathName(pWrap.getType())).getLast()
                            + "() {\n			@Override\n			public Boolean evaluate(" + TumlClassOperations.getPathName(pWrap.getType()).getLast()
                            + " e) {\n				return e instanceof " + TumlClassOperations.getPathName(concreteClassifierTo).getLast() + ";\n			}\n		})))");
                    annotatedClass.addToImports(TinkerGenerationUtil.BooleanExpressionEvaluator);
                }
                annotatedClass.addToImports(TumlClassOperations.getPathName(pWrap.getType()));
                block.addToStatements("validation.append(\"],\")");

                block.addToStatements("validation.append(\" \\\"meta\\\" : {\")");
                // The execute ocl query resource is only required if the below
                // visitor is availeble
                if (RestletVisitors.containsVisitorForClass(QueryExecuteResourceBuilder.class)
                        && (pWrap.getType().getQualifiedName().equals(TumlRestletGenerationUtil.instanceQueryQualifiedName) || pWrap.getType().getQualifiedName().equals(TumlRestletGenerationUtil.classQueryQualifiedName))) {
                    block.addToStatements("validation.append(\"\\\"oclExecuteUri\\\": \\\"/" + pWrap.getModel().getName() + "/{contextId}/oclExecuteQuery\\\", \")");
                }
                block.addToStatements("validation.append(\"\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "\\\"\")");
                block.addToStatements("validation.append(\", \\\"to\\\": \")");
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
                    conditionBlockFrom.addToStatements("validation.append(" + TumlClassOperations.propertyEnumName(concreteClassifierTo) + ".asJson())");
                    conditionBlockFrom.addToStatements("validation.append(\", \\\"from\\\": \")");
                    conditionBlockFrom.addToStatements("validation.append(" + TumlClassOperations.propertyEnumName(concreteClassifierFrom) + ".asJson())");
                    annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifierFrom).append(
                            TumlClassOperations.propertyEnumName(concreteClassifierFrom)));
                    countFrom++;
                }
                block.addToStatements(ifStatementFrom);

                annotatedClass.addToImports(TumlClassOperations.getPathName(pWrap.getOwningType()).append(
                        TumlClassOperations.propertyEnumName(pWrap.getOwningType())));
                annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifierTo)
                        .append(TumlClassOperations.propertyEnumName(concreteClassifierTo)));
                block.addToStatements("validation.append(\"}\")");
                if (concreteImplementations.size() != 1 && count != concreteImplementations.size()) {
                    block.addToStatements("validation.append(\"}, \")");
                }
                count++;
            }
            block.addToStatements("validation.append(\"}]\")");
            block.addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(validation.toString())");
        } else {
            //TODO not thought through
            block.addToStatements("return null");
        }
    }


    private void addServerResourceToRouterEnum(Classifier concreteClassifier, PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");

        OJEnumLiteral ojLiteral = new OJEnumLiteral((TRANSACTION + "_" + TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_" + pWrap.fieldname()
                + "_" + concreteClassifier.getName()).toUpperCase());

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/transactional/{transactionIdentifier}/" + TumlClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "s/{"
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
        ojLiteral = new OJEnumLiteral((TRANSACTION + "_" + TumlClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_" + pWrap.fieldname()
                + "_" + pWrap.fieldname() + "_" + concreteClassifier.getName()).toUpperCase());

        uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/transactional/{transactionIdentifier}/" + TumlClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "s/{"
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

    private void addCompositeParentIdAndTransactionUidField(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJField compositeParentFieldId = new OJField(TumlClassOperations.getPathName(pWrap.getOtherEnd().getType()).getLast().toLowerCase() + "Id",
                new OJPathName("long"));
        compositeParentFieldId.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToFields(compositeParentFieldId);

        OJField transactionUid = new OJField("transactionUid", new OJPathName("String"));
        transactionUid.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToFields(transactionUid);

    }

}
