package org.umlg.restlet.visitor.clazz;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.*;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.restlet.generation.RestletVisitors;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;
import org.umlg.restlet.visitor.model.QueryExecuteResourceBuilder;

import java.util.List;
import java.util.Set;

public class AssociationClassOverloadedPostServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<AssociationClass> {

    public AssociationClassOverloadedPostServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(AssociationClass ac) {

        //This is to navigate from the property to the association class itself
        List<Property> memberEnds = ac.getMemberEnds();
        for (Property memberEnd : memberEnds) {

            PropertyWrapper pWrap = new PropertyWrapper(memberEnd);

            OJAnnotatedClass owner = findOJClass(pWrap.getType());

            OJPackage ojPackage = owner.getMyPackage();

            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_"
                    + pWrap.getOtherEnd().getName() + "_" + ac.getName() + "_ServerResourceImpl");
            annotatedClass.setSuperclass(UmlgRestletGenerationUtil.ServerResource);
            annotatedClass.setMyPackage(ojPackage);
            addToSource(annotatedClass);
            addDefaultConstructor(annotatedClass);

            addCompositeParentIdField(pWrap, annotatedClass);
            addGetObjectRepresentation(pWrap, annotatedClass);
            addOptionsObjectRepresentation(pWrap, annotatedClass);

            addPostObjectRepresentation(pWrap, annotatedClass);
            addServerResourceToRouterEnum(pWrap, annotatedClass);
        }

        //And for navigating from the association class to its member ends
        for (Property memberEnd : memberEnds) {
            PropertyWrapper pWrap = new PropertyWrapper(memberEnd);
            PropertyWrapper otherEndWrap = new PropertyWrapper(pWrap.getOtherEnd());

            OJAnnotatedClass owner = findOJClass(pWrap.getType());

            OJPackage ojPackage = owner.getMyPackage();

            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(pWrap.getAssociationClassPathName().getLast()
                    + "_" + pWrap.getName() + "_ServerResourceImpl");
            annotatedClass.setSuperclass(UmlgRestletGenerationUtil.ServerResource);
            annotatedClass.setMyPackage(ojPackage);
            addToSource(annotatedClass);
            addDefaultConstructor(annotatedClass);

            addCompositeParentIdField(pWrap, annotatedClass, true);
            addGetObjectRepresentation(pWrap, annotatedClass, true);
            addOptionsObjectRepresentation(pWrap, annotatedClass, true);

            addPostObjectRepresentation(otherEndWrap, annotatedClass, true);
            addAssociationClassServerResourceToRouterEnum(pWrap, annotatedClass);
        }
    }

    @Override
    public void visitAfter(AssociationClass ac) {
    }

    private void addOptionsObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        addOptionsObjectRepresentation(pWrap, annotatedClass, false);
    }

    private void addGetObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        addGetObjectRepresentation(pWrap, annotatedClass, false);
    }

    private void addGetObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, boolean asAssociationClass) {

        OJAnnotatedOperation get = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
        get.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);

        OJTryStatement tryStatement = new OJTryStatement();
        OJPathName parentPathName;
        if (!asAssociationClass) {
            parentPathName = UmlgClassOperations.getPathName(pWrap.getOtherEnd().getType());
        } else {
            parentPathName = pWrap.getAssociationClassPathName();
        }
        tryStatement.getTryPart().addToStatements(
                "this." + parentPathName.getLast().toLowerCase() + "Id = " + UmlgRestletGenerationUtil.UmlgURLDecoder.getLast() + ".decode((String)getRequestAttributes().get(\""
                        + parentPathName.getLast().toLowerCase() + "Id\"))");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLDecoder);
        tryStatement.getTryPart().addToStatements(
                parentPathName.getLast() + " parentResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(" + parentPathName.getLast().toLowerCase() + "Id" + ")");
        annotatedClass.addToImports(parentPathName);
        buildToJson(pWrap, annotatedClass, tryStatement.getTryPart(), asAssociationClass);
        get.getBody().addToStatements(tryStatement);
        tryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");
        tryStatement.setCatchPart(null);
        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }

    private void addOptionsObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, boolean asAssociationClass) {

        OJAnnotatedOperation get = new OJAnnotatedOperation("options", UmlgRestletGenerationUtil.Representation);
        get.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);

        OJTryStatement tryStatement = new OJTryStatement();
        OJPathName parentPathName;
        if (!asAssociationClass) {
            parentPathName = UmlgClassOperations.getPathName(pWrap.getOtherEnd().getType());
        } else {
            parentPathName = pWrap.getAssociationClassPathName();
        }
        tryStatement.getTryPart().addToStatements(
                "this." + parentPathName.getLast().toLowerCase() + "Id = " + UmlgRestletGenerationUtil.UmlgURLDecoder.getLast() + ".decode((String)getRequestAttributes().get(\""
                        + parentPathName.getLast().toLowerCase() + "Id\"))");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLDecoder);
        tryStatement.getTryPart().addToStatements(
                parentPathName.getLast() + " parentResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(" + parentPathName.getLast().toLowerCase() + "Id" + ")");
        annotatedClass.addToImports(parentPathName);
        buildToJsonForOptions(pWrap, annotatedClass, tryStatement.getTryPart(), asAssociationClass);
        get.getBody().addToStatements(tryStatement);
        tryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");
        tryStatement.setCatchPart(null);
        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }


    private void addPostObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        addPostObjectRepresentation(pWrap, annotatedClass, false);
    }

    private void addPostObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, boolean asAssociationClass) {

        OJAnnotatedOperation post = new OJAnnotatedOperation("post", UmlgRestletGenerationUtil.Representation);
        post.addToParameters(new OJParameter("entity", UmlgRestletGenerationUtil.Representation));
        post.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(post);
        UmlgGenerationUtil.addSuppressWarning(post);

        PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());

        OJPathName parentPathName;
        if (!asAssociationClass) {
            parentPathName = otherEndPWrap.javaBaseTypePath();
        } else {
            parentPathName = pWrap.getAssociationClassPathName();
        }

        OJTryStatement ojTryStatement = new OJTryStatement();
        OJField mapper = new OJField("mapper", UmlgGenerationUtil.ObjectMapper);
        mapper.setInitExp(UmlgGenerationUtil.ObjectMapperFactory.getLast() + ".INSTANCE.getObjectMapper()");
        annotatedClass.addToImports(UmlgGenerationUtil.ObjectMapperFactory);
        ojTryStatement.getTryPart().addToLocals(mapper);

        OJPathName pathName = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJAnnotatedField entityText = new OJAnnotatedField("entityText", "String");
        entityText.setInitExp("entity.getText()");
        ojTryStatement.getTryPart().addToLocals(entityText);

        OJField resultMap = new OJField("resultMap", new OJPathName("java.util.Map").addToGenerics(new OJPathName("Class<? extends " + pWrap.getAssociationClassPathName().getLast() + ">")).addToGenerics("java.lang.StringBuilder"));
        resultMap.setInitExp("new HashMap<Class<? extends " + pWrap.getAssociationClassPathName().getLast() + ">, StringBuilder>()");
        annotatedClass.addToImports("java.util.HashMap");
        ojTryStatement.getTryPart().addToLocals(resultMap);

        ojTryStatement.getTryPart().addToStatements(pathName.getLast() + " overloaded = mapper.readValue(" + entityText.getName() + ", Map.class)");

        //Insert
        ojTryStatement.getTryPart().addToStatements("Object o = overloaded.get(\"insert\")");
        OJIfStatement ifInsert = new OJIfStatement("o != null");
        OJIfStatement ifArrayForInsert = new OJIfStatement("o instanceof ArrayList");
        OJIfStatement ifEmpty = new OJIfStatement("!((List)o).isEmpty()");
        ifArrayForInsert.addToThenPart(ifEmpty);
        annotatedClass.addToImports("java.util.List");
        ifEmpty.addToThenPart("throw new IllegalStateException(\"Adding an association class happens from the association member end!\")");
        ifArrayForInsert.addToElsePart("throw new IllegalStateException(\"Adding an association class happens from the association member end!\")");
        ifInsert.addToThenPart(ifArrayForInsert);
        ojTryStatement.getTryPart().addToStatements(ifInsert);

        //Delete
        ojTryStatement.getTryPart().addToStatements("o = overloaded.get(\"delete\")");
        OJIfStatement ifDelete = new OJIfStatement("o != null");
        OJIfStatement ifArrayForDelete = new OJIfStatement("o instanceof ArrayList");
        ifEmpty = new OJIfStatement("!((List)o).isEmpty()");
        ifArrayForDelete.addToThenPart(ifEmpty);
        ifEmpty.addToThenPart("throw new IllegalStateException(\"Deleting an association class happens from the association member end!\")");
        ifArrayForDelete.addToElsePart("throw new IllegalStateException(\"Deleting an association class happens from the association member end!\")");
        ifDelete.addToThenPart(ifArrayForDelete);
        ojTryStatement.getTryPart().addToStatements(ifDelete);

        //Update
        ojTryStatement.getTryPart().addToStatements("o = overloaded.get(\"update\")");
        OJIfStatement ifUpdate = new OJIfStatement("o != null");
        OJIfStatement ifArrayForUpdate = new OJIfStatement("o instanceof ArrayList");
        OJPathName genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJField array = new OJField("array", new OJPathName("java.util.ArrayList").addToGenerics(genericsForArray));
        array.setInitExp("(ArrayList<Map<String, Object>>)o");
        ifArrayForUpdate.getThenPart().addToLocals(array);
        ifUpdate.addToThenPart(ifArrayForUpdate);
        ojTryStatement.getTryPart().addToStatements(ifUpdate);

        OJForStatement forArray = new OJForStatement("map", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForUpdate.addToThenPart(forArray);
        forArray.getBody().addToStatements("put(resultMap, map)");
        OJField map = new OJField("map", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        map.setInitExp("(Map<String, Object>) o");
        ifArrayForUpdate.setElsePart(new OJBlock());
        ifArrayForUpdate.getElsePart().addToLocals(map);
        ifArrayForUpdate.getElsePart().addToStatements("put(resultMap, map)");

        addPutResource(pWrap, annotatedClass, parentPathName);

        //Check if transaction needs commiting
        commitOrRollback(ojTryStatement);

        OJBlock jsonResultBlock = new OJBlock();
        ojTryStatement.getTryPart().addToStatements(jsonResultBlock);

        OJField result = new OJField("result", "java.lang.StringBuilder");
        result.setInitExp("new StringBuilder(\"[\")");
        jsonResultBlock.addToLocals(result);
        OJField count = new OJField("count", "int");
        count.setInitExp("1");
        jsonResultBlock.addToLocals(count);

        OJForStatement forConcreteClassifiers = new OJForStatement("baseClass", new OJPathName("Class").addToGenerics("? extends " + pWrap.getAssociationClassPathName().getLast()), "resultMap.keySet()");
        jsonResultBlock.addToStatements(forConcreteClassifiers);
        if (pWrap.isOne()) {
            forConcreteClassifiers.getBody().addToStatements("result.append(\"{\\\"data\\\": \")");
        } else {
            forConcreteClassifiers.getBody().addToStatements("result.append(\"{\\\"data\\\": [\")");
        }
        forConcreteClassifiers.getBody().addToStatements("result.append(resultMap.get(baseClass))");
        if (pWrap.isOne()) {
            forConcreteClassifiers.getBody().addToStatements("result.append(\",\")");
        } else {
            forConcreteClassifiers.getBody().addToStatements("result.append(\"],\")");
        }
        forConcreteClassifiers.getBody().addToStatements("result.append(\" \\\"meta\\\" : {\")");
        forConcreteClassifiers.getBody().addToStatements("result.append(\"\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "AC\\\"\")");
        forConcreteClassifiers.getBody().addToStatements("result.append(\", \\\"to\\\": \")");

        OJIfStatement ifClassInstanceOf = new OJIfStatement();
        forConcreteClassifiers.getBody().addToStatements(ifClassInstanceOf);
        Set<Classifier> concreteImplementations = UmlgClassOperations.getConcreteImplementations(pWrap.getAssociationClass());
        boolean first = true;
        for (Classifier concreteImplementation : concreteImplementations) {
            OJBlock ojIfBlock;
            if (first) {
                first = false;
                ifClassInstanceOf.setCondition("baseClass.equals(" + UmlgClassOperations.getPathName(concreteImplementation).getLast() + ".class)");
                ojIfBlock = ifClassInstanceOf.getThenPart();
            } else {
                ojIfBlock = ifClassInstanceOf.addToElseIfCondition("baseClass.equals(" + UmlgClassOperations.getPathName(concreteImplementation).getLast() + ".class)", "");
            }
            ojIfBlock.addToStatements("result.append(" + UmlgClassOperations.propertyEnumName(concreteImplementation) + ".asJson())");
            ojIfBlock.addToStatements("result.append(\", \\\"from\\\": \")");
            Classifier owningType = (Classifier) pWrap.getOwningType();
            ojIfBlock.addToStatements("result.append(" + UmlgClassOperations.propertyEnumName(owningType) + ".asJson())");
            ojIfBlock.addToStatements("result.append(\"}\")");

        }
        OJIfStatement ifLast = new OJIfStatement("count++ == resultMap.size()");
        ifLast.addToThenPart("result.append(\"}\")");
        ifLast.addToElsePart("result.append(\"},\")");
        forConcreteClassifiers.getBody().addToStatements(ifLast);

        ifClassInstanceOf.addToElsePart("throw new IllegalStateException(\"Unknown type \" + baseClass.getName())");
        jsonResultBlock.addToStatements("result.append(\"]\")");
        jsonResultBlock.addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(result.toString())");

        ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        ojTryStatement.getCatchPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");

        ojTryStatement.getCatchPart().addToStatements("throw " + UmlgRestletGenerationUtil.UmlgExceptionUtilFactory.getLast() + ".getTumlExceptionUtil().handle(e)");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgExceptionUtilFactory);

        post.getBody().addToStatements(ojTryStatement);

        annotatedClass.addToImports(parentPathName);

        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(post);
    }

    private void addPutResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation put = new OJAnnotatedOperation("put");
        put.setVisibility(OJVisibilityKind.PRIVATE);
        put.addToParameters(new OJParameter("resultMap", new OJPathName("java.util.Map").addToGenerics(new OJPathName("Class").addToGenerics("? extends " + pWrap.getAssociationClassPathName().getLast())).addToGenerics("java.lang.StringBuilder")));
        put.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(put);

        OJBlock firstBlock = new OJBlock();
        firstBlock.addToStatements("Object id = propertyMap.get(\"id\")");
        firstBlock.addToStatements(pWrap.getAssociationClassPathName().getLast() + " childResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(id)");
        annotatedClass.addToImports(pWrap.javaBaseTypePath());
        firstBlock.addToStatements("childResource.fromJson(propertyMap)");
        put.getBody().addToStatements(firstBlock);

        OJBlock secondBlock = new OJBlock();

        OJField baseTumlClass = new OJField("baseTumlClass", new OJPathName("Class").addToGenerics("? extends " + pWrap.getAssociationClassPathName().getLast()));
        baseTumlClass.setInitExp("childResource.getClass()");
        secondBlock.addToLocals(baseTumlClass);

        OJField sb = new OJField("sb", "StringBuilder");
        secondBlock.addToLocals(sb);

        OJIfStatement ifSbExist = new OJIfStatement("!resultMap.containsKey(baseTumlClass)");
        ifSbExist.addToThenPart("sb = new StringBuilder()");
        ifSbExist.addToThenPart("resultMap.put(baseTumlClass, sb)");
        ifSbExist.addToElsePart("sb = resultMap.get(baseTumlClass)");
        ifSbExist.addToElsePart("sb.append(\",\")");
        secondBlock.addToStatements(ifSbExist);

        secondBlock.addToStatements("sb.append(childResource.toJsonWithoutCompositeParent())");
        put.getBody().addToStatements(secondBlock);

    }

    private void addPostResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        addPostResource(pWrap, annotatedClass, parentPathName, false);
    }

    private void addPostResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName, boolean asAssociationClass) {
        OJAnnotatedOperation add = new OJAnnotatedOperation("add");
        add.setVisibility(OJVisibilityKind.PRIVATE);

        add.addToParameters(new OJParameter("resultMap", new OJPathName("java.util.Map").addToGenerics(new OJPathName("Class").addToGenerics("? extends " + pWrap.javaBaseTypePath().getLast())).addToGenerics("java.lang.StringBuilder")));
        add.addToParameters(new OJParameter("parentResource", parentPathName));
        add.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(add);

        OJField qualifiedName = new OJField("qualifiedName", "String");
        qualifiedName.setInitExp("(String)propertyMap.get(\"qualifiedName\")");
        add.getBody().addToLocals(qualifiedName);

        OJField baseTumlClass = new OJField("baseTumlClass", new OJPathName("Class").addToGenerics(pWrap.javaBaseTypePath()));
        baseTumlClass.setInitExp(UmlgGenerationUtil.UmlgSchemaFactory.getLast() + ".getUmlgSchemaMap().get(qualifiedName)");
        annotatedClass.addToImports(UmlgGenerationUtil.UmlgSchemaFactory);
        add.getBody().addToLocals(baseTumlClass);

        OJField sb = new OJField("sb", "StringBuilder");
        add.getBody().addToLocals(sb);

        OJIfStatement ifSbExist = new OJIfStatement("!resultMap.containsKey(baseTumlClass)");
        ifSbExist.addToThenPart("sb = new StringBuilder()");
        ifSbExist.addToThenPart("resultMap.put(baseTumlClass, sb)");
        ifSbExist.addToElsePart("sb = resultMap.get(baseTumlClass)");
        ifSbExist.addToElsePart("sb.append(\",\")");
        add.getBody().addToStatements(ifSbExist);

        OJTryStatement tryInstantiate = new OJTryStatement();
        add.getBody().addToStatements(tryInstantiate);

        if (pWrap.isComposite()) {
            PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());
            OJField constructor = new OJField("constructor", new OJPathName("java.lang.reflect.Constructor").addToGenerics(pWrap.javaBaseTypePath()));
            constructor.setInitExp("baseTumlClass.getConstructor(" + otherEndPWrap.javaBaseTypePath().getLast() + ".class)");
            tryInstantiate.getTryPart().addToLocals(constructor);
            tryInstantiate.getTryPart().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = constructor.newInstance(parentResource)");
            tryInstantiate.getTryPart().addToStatements("childResource.fromJson(propertyMap)");
        } else {
            tryInstantiate.getTryPart().addToStatements("Object id = propertyMap.get(\"id\")");
            tryInstantiate.getTryPart().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(id)");
            if (!pWrap.isMemberOfAssociationClass() || asAssociationClass) {
                tryInstantiate.getTryPart().addToStatements("parentResource." + pWrap.adder() + "(childResource)");
            } else {
                tryInstantiate.getTryPart().addToStatements("parentResource." + pWrap.adder() + "(childResource, null)");
            }
        }
        tryInstantiate.getTryPart().addToStatements("String jsonResult = childResource.toJsonWithoutCompositeParent(true)");
        annotatedClass.addToImports(pWrap.javaBaseTypePath());
        if (pWrap.isOrdered()) {
            //TODO
        } else {
            //TODO
        }
        tryInstantiate.getTryPart().addToStatements("sb.append(jsonResult)");
        tryInstantiate.setCatchParam(new OJParameter("e", "Exception"));
        tryInstantiate.getCatchPart().addToStatements("throw new RuntimeException(e)");
    }

    private void buildToJson(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJBlock block, boolean asAssociationClass) {
        Set<Classifier> concreteImplementations;
        Set<Classifier> concreteImplementationsFrom;
        if (!asAssociationClass) {
            concreteImplementations = UmlgClassOperations.getConcreteImplementations(pWrap.getAssociationClass());
            concreteImplementationsFrom = UmlgClassOperations.getConcreteImplementations((Classifier) pWrap.getOwningType());
        } else {
            concreteImplementations = UmlgClassOperations.getConcreteImplementations((Classifier) pWrap.getType());
            concreteImplementationsFrom = UmlgClassOperations.getConcreteImplementations(pWrap.getAssociationClass());
        }
        if (!concreteImplementationsFrom.isEmpty()) {
            annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
            block.addToStatements("StringBuilder json = new StringBuilder()");
            block.addToStatements("json.append(\"[\")");

            int count = 1;
            // For meta data, put where one is navigating to first, then where on is
            // navigating from
            // This is consistent with navigating to a entity with a vertex where
            // there is no navigating from.
            // i.e. the first meta data in the array is the entity navigating to.
            for (Classifier concreteClassifierTo : concreteImplementations) {
                annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierTo));
                if (pWrap.isOne() || asAssociationClass) {
                    block.addToStatements("json.append(\"{\\\"data\\\": \")");
                } else {
                    block.addToStatements("json.append(\"{\\\"data\\\": [\")");
                }
                if (pWrap.isOne() || asAssociationClass) {

                    if (asAssociationClass) {
                        OJIfStatement ifOneInstanceOf = new OJIfStatement("parentResource." + pWrap.getter() + "() != null && parentResource." + pWrap.getter() + "().getClass() == "
                                + UmlgClassOperations.getPathName(concreteClassifierTo).getLast() + ".class");
                        ifOneInstanceOf.addToThenPart("json.append(" + UmlgGenerationUtil.ToJsonUtil.getLast() + ".toJsonWithoutCompositeParent(parentResource." + pWrap.getter() + "()))");
                        ifOneInstanceOf.addToElsePart("json.append(\"null\")");
                        block.addToStatements(ifOneInstanceOf);
                    } else {
                        OJIfStatement ifOneInstanceOf = new OJIfStatement("parentResource." + pWrap.getter() + "() != null && parentResource." + pWrap.associationClassGetter() + "().getClass() == "
                                + UmlgClassOperations.getPathName(concreteClassifierTo).getLast() + ".class");
                        ifOneInstanceOf.addToThenPart("json.append(" + UmlgGenerationUtil.ToJsonUtil.getLast() + ".toJsonWithoutCompositeParent(parentResource." + pWrap.associationClassGetter() + "()))");
                        ifOneInstanceOf.addToElsePart("json.append(\"null\")");
                        block.addToStatements(ifOneInstanceOf);
                    }
                } else {
                    block.addToStatements("json.append(" + UmlgGenerationUtil.ToJsonUtil.getLast() + ".toJsonWithoutCompositeParent(parentResource." + pWrap.associationClassGetter() + "().select(new "
                            + UmlgGenerationUtil.BooleanExpressionEvaluator.getCopy().addToGenerics(pWrap.getAssociationClassPathName()).getLast()
                            + "() {\n			@Override\n			public Boolean evaluate(" + pWrap.getAssociationClassPathName().getLast()
                            + " e) {\n				return e.getClass() == " + UmlgClassOperations.getPathName(concreteClassifierTo).getLast() + ".class;\n			}\n		})))");
                    annotatedClass.addToImports(UmlgGenerationUtil.BooleanExpressionEvaluator);
                }
                annotatedClass.addToImports(UmlgClassOperations.getPathName(pWrap.getType()));
                if (pWrap.isOne() || asAssociationClass) {
                    block.addToStatements("json.append(\",\")");
                } else {
                    block.addToStatements("json.append(\"],\")");
                }

                block.addToStatements("json.append(\" \\\"meta\\\" : {\")");
                // The execute ocl query resource is only required if the below
                // visitor is available
                if (RestletVisitors.containsVisitorForClass(QueryExecuteResourceBuilder.class)
                        && (pWrap.getType().getQualifiedName().equals(UmlgRestletGenerationUtil.instanceQueryQualifiedName) || pWrap.getType().getQualifiedName().equals(UmlgRestletGenerationUtil.classQueryQualifiedName))) {
                    block.addToStatements("json.append(\"\\\"oclExecuteUri\\\": \\\"/" + pWrap.getModel().getName() + "/{contextId}/oclExecuteQuery\\\", \")");
                }
                if (!asAssociationClass) {
                    block.addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "AC\\\"\")");
                } else {
                    block.addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "\\\"\")");
                }
                block.addToStatements("json.append(\", \\\"to\\\": \")");
                int countFrom = 1;
                OJIfStatement ifStatementFrom = new OJIfStatement();
                for (Classifier concreteClassifierFrom : concreteImplementationsFrom) {
                    OJBlock conditionBlockFrom = new OJBlock();
                    annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierFrom));
                    String condition = "parentResource.getClass() == " + UmlgClassOperations.getPathName(concreteClassifierFrom).getLast() + ".class";
                    if (countFrom == 1) {
                        ifStatementFrom.setCondition(condition);
                        ifStatementFrom.setThenPart(conditionBlockFrom);
                    } else if (countFrom == concreteImplementationsFrom.size()) {
                        ifStatementFrom.setElsePart(conditionBlockFrom);
                    } else {
                        conditionBlockFrom = ifStatementFrom.addToElseIfCondition(condition, "");
                    }
                    conditionBlockFrom.addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(concreteClassifierTo) + ".asJson())");
                    conditionBlockFrom.addToStatements("json.append(\", \\\"from\\\": \")");
                    conditionBlockFrom.addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(concreteClassifierFrom) + ".asJson())");
                    annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierFrom).append(
                            UmlgClassOperations.propertyEnumName(concreteClassifierFrom)));
                    countFrom++;
                }
                block.addToStatements(ifStatementFrom);

                annotatedClass.addToImports(UmlgClassOperations.getPathName(pWrap.getOwningType()).append(
                        UmlgClassOperations.propertyEnumName(pWrap.getOwningType())));
                annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierTo)
                        .append(UmlgClassOperations.propertyEnumName(concreteClassifierTo)));
                block.addToStatements("json.append(\"}\")");
                if (concreteImplementations.size() != 1 && count != concreteImplementations.size()) {
                    block.addToStatements("json.append(\"}, \")");
                }
                count++;
            }
            block.addToStatements("json.append(\"}]\")");
            block.addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
        } else {
            //TODO not thought through
            block.addToStatements("return null");
        }
    }

    private void buildToJsonForOptions(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJBlock block, boolean asAssociationClass) {

        Set<Classifier> concreteImplementations;
        Set<Classifier> concreteImplementationsFrom;
        if (!asAssociationClass) {
            concreteImplementations = UmlgClassOperations.getConcreteImplementations(pWrap.getAssociationClass());
            concreteImplementationsFrom = UmlgClassOperations.getConcreteImplementations((Classifier) pWrap.getOwningType());
        } else {
            concreteImplementations = UmlgClassOperations.getConcreteImplementations((Classifier) pWrap.getType());
            concreteImplementationsFrom = UmlgClassOperations.getConcreteImplementations(pWrap.getAssociationClass());
        }
        if (!concreteImplementationsFrom.isEmpty()) {
            annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
            //blok get reassigned for the meta data if statement
            OJBlock returnBlock = block;
            block.addToStatements("StringBuilder json = new StringBuilder()");
            block.addToStatements("json.append(\"[\")");

            int count = 1;
            // For meta data, put where one is navigating to first, then where on is
            // navigating from
            // This is consistent with navigating to a entity with a vertex where
            // there is no navigating from.
            // i.e. the first meta data in the array is the entity navigating to.
            for (Classifier concreteClassifierTo : concreteImplementations) {
                annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierTo));

                block.addToStatements("json.append(\"{\")");
                block.addToStatements("json.append(\" \\\"meta\\\" : {\")");


                if (!asAssociationClass) {
                    block.addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "AC\\\"\")");
                } else {
                    block.addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "\\\"\")");
                }

                // The execute ocl query resource is only required if the below
                // visitor is available
                if (RestletVisitors.containsVisitorForClass(QueryExecuteResourceBuilder.class)
                        && (pWrap.getType().getQualifiedName().equals(UmlgRestletGenerationUtil.instanceQueryQualifiedName) || pWrap.getType().getQualifiedName().equals(UmlgRestletGenerationUtil.classQueryQualifiedName))) {
                    block.addToStatements("json.append(\", \\\"oclExecuteUri\\\": \\\"/" + pWrap.getModel().getName() + "/{contextId}/oclExecuteQuery\\\"\")");
                }
                block.addToStatements("json.append(\", \\\"to\\\": \")");
                int countFrom = 1;
                OJIfStatement ifStatementFrom = new OJIfStatement();
                for (Classifier concreteClassifierFrom : concreteImplementationsFrom) {
                    OJBlock conditionBlockFrom = new OJBlock();
                    annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierFrom));
                    String condition = "parentResource.getClass() == " + UmlgClassOperations.getPathName(concreteClassifierFrom).getLast() + ".class";
                    if (countFrom == 1) {
                        ifStatementFrom.setCondition(condition);
                        ifStatementFrom.setThenPart(conditionBlockFrom);
                    } else if (countFrom == concreteImplementationsFrom.size()) {
                        ifStatementFrom.setElsePart(conditionBlockFrom);
                    } else {
                        conditionBlockFrom = ifStatementFrom.addToElseIfCondition(condition, "");
                    }
                    conditionBlockFrom.addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(concreteClassifierTo) + ".asJson())");
                    conditionBlockFrom.addToStatements("json.append(\", \\\"from\\\": \")");
                    conditionBlockFrom.addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(concreteClassifierFrom) + ".asJson())");
                    annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierFrom).append(
                            UmlgClassOperations.propertyEnumName(concreteClassifierFrom)));
                    countFrom++;
                }
                block.addToStatements(ifStatementFrom);

                annotatedClass.addToImports(UmlgClassOperations.getPathName(pWrap.getOwningType()).append(
                        UmlgClassOperations.propertyEnumName(pWrap.getOwningType())));
                annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierTo)
                        .append(UmlgClassOperations.propertyEnumName(concreteClassifierTo)));
                block.addToStatements("json.append(\"}\")");
                if (concreteImplementations.size() != 1 && count != concreteImplementations.size()) {
                    block.addToStatements("json.append(\"}, \")");
                }
                block = returnBlock;
                count++;
            }
            returnBlock.addToStatements("json.append(\"}]\")");
            returnBlock.addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
        } else {
            //TODO not thought through
            block.addToStatements("return null");
        }
    }


    private void addServerResourceToRouterEnum(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());

        OJEnumLiteral ojLiteral = new OJEnumLiteral(UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_" + pWrap.getAssociationClassFakePropertyName());

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "s/{"
                + UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "Id}/" + pWrap.getAssociationClassFakePropertyName() + "\"");
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

    private void addAssociationClassServerResourceToRouterEnum(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());

        OJEnumLiteral ojLiteral = new OJEnumLiteral(pWrap.getAssociationClassPathName().getLast() + "_" + pWrap.fieldname());

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\""+UmlgRestletGenerationUtil.restletContext+"/" + pWrap.getAssociationClassPathName().getLast().toLowerCase() + "s/{"
                + pWrap.getAssociationClassPathName().getLast().toLowerCase() + "Id}/" + pWrap.fieldname() + "\"");
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

    @Override
    protected void addCompositeParentIdField(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        addCompositeParentIdField(pWrap, annotatedClass, false);
    }

    private void addCompositeParentIdField(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, boolean asAssociationClass) {
        OJField compositeParentFieldId;
        if (!asAssociationClass) {
            compositeParentFieldId = new OJField(UmlgClassOperations.getPathName(pWrap.getOtherEnd().getType()).getLast().toLowerCase() + "Id",
                    new OJPathName("Object"));
        } else {
            compositeParentFieldId = new OJField(pWrap.getAssociationClassPathName().getLast().toLowerCase() + "Id",
                    new OJPathName("Object"));

        }
        compositeParentFieldId.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToFields(compositeParentFieldId);
    }

}
