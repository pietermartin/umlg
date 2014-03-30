package org.umlg.restlet.visitor.clazz;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.ModelLoader;
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

import java.util.*;

public class NavigatePropertyOverloadedPostServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Property> {

    public NavigatePropertyOverloadedPostServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper pWrap = new PropertyWrapper(p);
        if (!pWrap.isDataType() && !pWrap.isEnumeration() && pWrap.isNavigable()) {

            OJAnnotatedClass owner = findOJClass(pWrap.getType());
            OJPackage ojPackage = owner.getMyPackage();

            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(getClassName(pWrap, "ServerResourceImpl"));

            annotatedClass.setSuperclass(UmlgRestletGenerationUtil.ServerResource);
            annotatedClass.setMyPackage(ojPackage);
            addToSource(annotatedClass);
            addDefaultConstructor(annotatedClass);

            addCompositeParentIdField(pWrap, annotatedClass);
            addGetObjectRepresentation(pWrap, annotatedClass);
            addOptionsObjectRepresentation(pWrap, annotatedClass);

            if (!pWrap.isDerived()) {
                addPostObjectRepresentation(pWrap, annotatedClass);
            }

            addServerResourceToRouterEnum(pWrap, annotatedClass);

        }
    }

    @Override
    public void visitAfter(Property p) {
    }

    private void addGetObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {

        OJAnnotatedOperation get = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
        UmlgGenerationUtil.addOverrideAnnotation(get);
        get.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(get);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);

        OJTryStatement tryStatement = new OJTryStatement();

        OJPathName parentPathName;
        if (pWrap.getOtherEnd() != null) {
            parentPathName = UmlgClassOperations.getPathName(pWrap.getOtherEnd().getType());
        } else {
            parentPathName = UmlgClassOperations.getPathName(pWrap.getOwningType());
        }
        tryStatement.getTryPart().addToStatements(
                "this." + parentPathName.getLast().toLowerCase() + "Id = " + UmlgRestletGenerationUtil.UmlgURLDecoder.getLast() + ".decode((String)getRequestAttributes().get(\""
                        + parentPathName.getLast().toLowerCase() + "Id\"))");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLDecoder);

        tryStatement.getTryPart().addToStatements(
                parentPathName.getLast() + " parentResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(" + parentPathName.getLast().toLowerCase() + "Id" + ")");
        annotatedClass.addToImports(parentPathName);
        buildToJson(pWrap, annotatedClass, tryStatement.getTryPart());
        get.getBody().addToStatements(tryStatement);
        tryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");
        tryStatement.setCatchPart(null);
        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }

    private void addOptionsObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation option = new OJAnnotatedOperation("options", UmlgRestletGenerationUtil.Representation);
        UmlgGenerationUtil.addOverrideAnnotation(option);
        option.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToOperations(option);
        OJTryStatement tryStatement = new OJTryStatement();
        OJPathName parentPathName;
        if (pWrap.getOtherEnd() != null) {
            parentPathName = UmlgClassOperations.getPathName(pWrap.getOtherEnd().getType());
        } else {
            parentPathName = UmlgClassOperations.getPathName(pWrap.getOwningType());
        }
        tryStatement.getTryPart().addToStatements(
                "this." + parentPathName.getLast().toLowerCase() + "Id = " + UmlgRestletGenerationUtil.UmlgURLDecoder.getLast() + ".decode((String)getRequestAttributes().get(\""
                        + parentPathName.getLast().toLowerCase() + "Id\"))");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLDecoder);

        tryStatement.getTryPart().addToStatements(
                parentPathName.getLast() + " parentResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(" + parentPathName.getLast().toLowerCase() + "Id" + ")");
        annotatedClass.addToImports(parentPathName);

        //do the stuff here man
        buildToJsonForOption(pWrap, annotatedClass, tryStatement.getTryPart());

        option.getBody().addToStatements(tryStatement);
        tryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");
        tryStatement.setCatchPart(null);
        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
    }

    private void addDeleteResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation delete = new OJAnnotatedOperation("delete");
        delete.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToOperations(delete);
        annotatedClass.addToImports(pWrap.javaBaseTypePath());
        if (pWrap.isComposite()) {
            delete.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
            delete.getBody().addToStatements("Object id = propertyMap.get(\"id\")");
            delete.getBody().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(id)");
            delete.getBody().addToStatements("childResource.delete()");
        } else {
            delete.addToParameters(new OJParameter("parentResource", parentPathName));
            delete.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
            delete.getBody().addToStatements("Object id = propertyMap.get(\"id\")");
            delete.getBody().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(id)");
            delete.getBody().addToStatements("parentResource." + pWrap.remover() + "(childResource)");
        }

    }

    private void addPostObjectRepresentation(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {

        OJAnnotatedOperation post = new OJAnnotatedOperation("post", UmlgRestletGenerationUtil.Representation);
        post.addToParameters(new OJParameter("entity", UmlgRestletGenerationUtil.Representation));
        post.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(post);
        UmlgGenerationUtil.addSuppressWarning(post);

        PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());

        OJPathName parentPathName = otherEndPWrap.javaBaseTypePath();
        post.getBody().addToStatements(
                "this." + parentPathName.getLast().toLowerCase() + "Id = " + UmlgRestletGenerationUtil.UmlgURLDecoder.getLast() + ".decode((String)getRequestAttributes().get(\""
                        + parentPathName.getLast().toLowerCase() + "Id\"))");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLDecoder);

        post.getBody().addToStatements(
                parentPathName.getLast() + " parentResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(" + parentPathName.getLast().toLowerCase() + "Id" + ")");

        OJTryStatement ojTryStatement = new OJTryStatement();
        OJField mapper = new OJField("mapper", UmlgGenerationUtil.ObjectMapper);
        mapper.setInitExp(UmlgGenerationUtil.ObjectMapperFactory.getLast() + ".INSTANCE.getObjectMapper()");
        annotatedClass.addToImports(UmlgGenerationUtil.ObjectMapperFactory);
        ojTryStatement.getTryPart().addToLocals(mapper);

        OJPathName pathName = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJAnnotatedField entityText = new OJAnnotatedField("entityText", "String");
        entityText.setInitExp("entity.getText()");
        ojTryStatement.getTryPart().addToLocals(entityText);

        OJField resultMap = new OJField("resultMap", new OJPathName("java.util.Map").addToGenerics(
                new OJPathName("Class<? extends " + pWrap.javaBaseTypePath().getLast() + ">")).addToGenerics("List<" + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + ">"));
        resultMap.setInitExp("new HashMap<Class<? extends " + pWrap.javaBaseTypePath().getLast() + ">, List<" + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + ">>()");
        annotatedClass.addToImports("java.util.HashMap");
        annotatedClass.addToImports("java.util.List");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgNodeJsonHolder);
        ojTryStatement.getTryPart().addToLocals(resultMap);

        ojTryStatement.getTryPart().addToStatements(pathName.getLast() + " overloaded = mapper.readValue(" + entityText.getName() + ", Map.class)");
        ojTryStatement.getTryPart().addToStatements("Object o = overloaded.get(\"insert\")");

        //Insert
        OJIfStatement ifInsert = new OJIfStatement("o != null");
        OJIfStatement ifArrayForInsert = new OJIfStatement("o instanceof ArrayList");
        OJPathName genericsForArray = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJField array = new OJField("array", new OJPathName("java.util.List").addToGenerics(genericsForArray));
        array.setInitExp("(ArrayList<Map<String, Object>>)o");
        ifArrayForInsert.getThenPart().addToLocals(array);
        ifInsert.addToThenPart(ifArrayForInsert);

        ojTryStatement.getTryPart().addToStatements(ifInsert);
        OJForStatement forArray = new OJForStatement("overloadedJsonMap", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForInsert.addToThenPart(forArray);
        forArray.getBody().addToStatements("add(resultMap, parentResource, overloadedJsonMap)");

        OJField map = new OJField("overloadedJsonMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        map.setInitExp("(Map<String, Object>) o");
        ifArrayForInsert.setElsePart(new OJBlock());
        ifArrayForInsert.getElsePart().addToLocals(map);
        ifArrayForInsert.getElsePart().addToStatements("add(resultMap, parentResource, overloadedJsonMap)");

        addPostResource(pWrap, annotatedClass, parentPathName);

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

        forArray = new OJForStatement("overloadedJsonMap", new OJPathName("java.util.Map").addToGenerics(new OJPathName("String")).addToGenerics(
                new OJPathName("Object")), "array");
        ifArrayForUpdate.addToThenPart(forArray);
        if (pWrap.isOrdered()) {
            forArray.getBody().addToStatements("put(resultMap, parentResource, overloadedJsonMap)");
        } else {
            forArray.getBody().addToStatements("put(resultMap, overloadedJsonMap)");
        }
        map = new OJField("overloadedJsonMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        map.setInitExp("(Map<String, Object>) o");
        ifArrayForUpdate.setElsePart(new OJBlock());
        ifArrayForUpdate.getElsePart().addToLocals(map);

        if (pWrap.isOrdered()) {
            //Include the parent as it will be needed to add the child at a particular index
            ifArrayForUpdate.getElsePart().addToStatements("put(resultMap, parentResource, overloadedJsonMap)");
        } else {
            ifArrayForUpdate.getElsePart().addToStatements("put(resultMap, overloadedJsonMap)");
        }

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

        OJForStatement forConcreteClassifiers = new OJForStatement("baseClass", new OJPathName("Class").addToGenerics("? extends " + pWrap.javaBaseTypePath().getLast()), "resultMap.keySet()");
        jsonResultBlock.addToStatements(forConcreteClassifiers);
        if (pWrap.isOne()) {
            forConcreteClassifiers.getBody().addToStatements("result.append(\"{\\\"data\\\": \")");
        } else {
            forConcreteClassifiers.getBody().addToStatements("result.append(\"{\\\"data\\\": [\")");
        }
        OJField objectList = new OJField("objectList", new OJPathName("java.util.List").addToGenerics(UmlgRestletGenerationUtil.UmlgNodeJsonHolder));
        objectList.setInitExp("resultMap.get(baseClass)");
        forConcreteClassifiers.getBody().addToLocals(objectList);

        OJField objectListCount = new OJField("objectListCount", new OJPathName("int"));
        objectListCount.setInitExp("1");
        forConcreteClassifiers.getBody().addToLocals(objectListCount);

        OJForStatement forObjectList = new OJForStatement("object", UmlgRestletGenerationUtil.UmlgNodeJsonHolder, "objectList");
        forObjectList.getBody().addToStatements("result.append(object.toJson())");
        OJIfStatement ifObjectListCountSmallerThanSize = new OJIfStatement("objectListCount++ < objectList.size()", "result.append(\",\")");
        forObjectList.getBody().addToStatements(ifObjectListCountSmallerThanSize);
        forConcreteClassifiers.getBody().addToStatements(forObjectList);

        if (pWrap.isOne()) {
            forConcreteClassifiers.getBody().addToStatements("result.append(\",\")");
        } else {
            forConcreteClassifiers.getBody().addToStatements("result.append(\"],\")");
        }
        forConcreteClassifiers.getBody().addToStatements("result.append(\" \\\"meta\\\" : {\")");
        forConcreteClassifiers.getBody().addToStatements("result.append(\"\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "\\\"\")");
        forConcreteClassifiers.getBody().addToStatements("result.append(\", \\\"to\\\": \")");

        OJIfStatement ifClassInstanceOf = null;
        Set<Classifier> concreteImplementations = UmlgClassOperations.getConcreteImplementations((Classifier) pWrap.getType());
        if (!concreteImplementations.isEmpty()) {
            ifClassInstanceOf = new OJIfStatement();
            forConcreteClassifiers.getBody().addToStatements(ifClassInstanceOf);
        }
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
            annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteImplementation).append(UmlgClassOperations.propertyEnumName(concreteImplementation)));

            ojIfBlock.addToStatements("result.append(\", \\\"from\\\": \")");
            Classifier owningType = (Classifier) pWrap.getOwningType();
            ojIfBlock.addToStatements("result.append(" + UmlgClassOperations.propertyEnumName(owningType) + ".asJson())");
            annotatedClass.addToImports(UmlgClassOperations.getPathName(owningType).append(UmlgClassOperations.propertyEnumName(owningType)));
            ojIfBlock.addToStatements("result.append(\"}\")");

        }


        OJIfStatement ifLast = new OJIfStatement("count++ == resultMap.size()");
        ifLast.addToThenPart("result.append(\"}\")");
        ifLast.addToElsePart("result.append(\"},\")");
        forConcreteClassifiers.getBody().addToStatements(ifLast);

        if (!concreteImplementations.isEmpty()) {
            ifClassInstanceOf.addToElsePart("throw new IllegalStateException(\"Unknown type \" + baseClass.getName())");
        }
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
        put.addToParameters(new OJParameter("resultMap", new OJPathName("java.util.Map").addToGenerics(new OJPathName("Class").addToGenerics("? extends " + pWrap.javaBaseTypePath().getLast())).addToGenerics("List<" + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + ">")));
        if (pWrap.isOrdered()) {
            put.addToParameters(new OJParameter("parentResource", parentPathName));
        }
        put.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(put);

        OJBlock firstBlock = new OJBlock();
        firstBlock.addToStatements("Object id = propertyMap.get(\"id\")");
        firstBlock.addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(id)");
        annotatedClass.addToImports(pWrap.javaBaseTypePath());
        firstBlock.addToStatements("childResource.fromJson(propertyMap)");

        if (pWrap.isOrdered()) {
            //Place the child at the correct index
            firstBlock.addToStatements("Integer index = (Integer)propertyMap.get(\"" + UmlgRestletGenerationUtil._INDEX + "\")");
            OJIfStatement ifIndexNotNull = new OJIfStatement("index != null");
            if (!pWrap.isMemberOfAssociationClass()) {
                ifIndexNotNull.addToThenPart("parentResource." + pWrap.remover() + "(childResource)");
                ifIndexNotNull.addToThenPart("parentResource." + pWrap.adder() + "(index, childResource)");
            } else {
                ifIndexNotNull.addToThenPart("parentResource." + pWrap.associationClassMoverForProperty() + "(index, childResource)");
            }

            firstBlock.addToStatements(ifIndexNotNull);
        }

        put.getBody().addToStatements(firstBlock);

        OJBlock secondBlock = new OJBlock();

        OJField baseTumlClass = new OJField("baseTumlClass", new OJPathName("Class").addToGenerics("? extends " + pWrap.javaBaseTypePath().getLast()));
        baseTumlClass.setInitExp("childResource.getClass()");
        secondBlock.addToLocals(baseTumlClass);

        OJField sb = new OJField("objectList", "List<" + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + ">");
        secondBlock.addToLocals(sb);

        OJIfStatement ifSbExist = new OJIfStatement("!resultMap.containsKey(baseTumlClass)");
        ifSbExist.addToThenPart("objectList = new ArrayList<" + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + ">()");
        ifSbExist.addToThenPart("resultMap.put(baseTumlClass, objectList)");
        ifSbExist.addToElsePart("objectList = resultMap.get(baseTumlClass)");
        secondBlock.addToStatements(ifSbExist);

        secondBlock.addToStatements("objectList.add(new " + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + "(childResource))");
        put.getBody().addToStatements(secondBlock);

    }

    private void addPostResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation add = new OJAnnotatedOperation("add");
        add.setVisibility(OJVisibilityKind.PRIVATE);

        add.addToParameters(new OJParameter("resultMap", new OJPathName("java.util.Map").addToGenerics(new OJPathName("Class").addToGenerics("? extends " + pWrap.javaBaseTypePath().getLast())).addToGenerics("List<" + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + ">")));
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

        OJField sb = new OJField("objectList", "List<" + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + ">");
        add.getBody().addToLocals(sb);

        OJIfStatement ifSbExist = new OJIfStatement("!resultMap.containsKey(baseTumlClass)");
        ifSbExist.addToThenPart("objectList = new ArrayList<" + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + ">()");
        ifSbExist.addToThenPart("resultMap.put(baseTumlClass, objectList)");
        ifSbExist.addToElsePart("objectList = resultMap.get(baseTumlClass)");
        add.getBody().addToStatements(ifSbExist);

        OJTryStatement tryInstantiate = new OJTryStatement();
        add.getBody().addToStatements(tryInstantiate);

        if (pWrap.isComposite()) {
            PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());
            if (!pWrap.isMemberOfAssociationClass()) {
                OJField constructor = new OJField("constructor", new OJPathName("java.lang.reflect.Constructor").addToGenerics(pWrap.javaBaseTypePath()));
                constructor.setInitExp("baseTumlClass.getConstructor(" + otherEndPWrap.javaBaseTypePath().getLast() + ".class)");
                tryInstantiate.getTryPart().addToLocals(constructor);
                tryInstantiate.getTryPart().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = constructor.newInstance(parentResource)");
                tryInstantiate.getTryPart().addToStatements("childResource.fromJson(propertyMap)");
                tryInstantiate.getTryPart().addToStatements("objectList.add(new " + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + "(childResource))");
            } else {
                PropertyWrapper otherEnd = new PropertyWrapper(pWrap.getOtherEnd());
                OJField constructor = new OJField("constructor", new OJPathName("java.lang.reflect.Constructor").addToGenerics(pWrap.javaBaseTypePath()));
                constructor.setInitExp("baseTumlClass.getConstructor(" + otherEndPWrap.javaBaseTypePath().getLast() + ".class, " + otherEnd.getAssociationClassPathName().getLast() + ".class)");
                tryInstantiate.getTryPart().addToLocals(constructor);

//TODO check this out, it creates a duplicate as the fromJson creates the association class
                tryInstantiate.getTryPart().addToStatements(otherEnd.getAssociationClassPathName().getLast() + " " + otherEnd.getAssociationClassFakePropertyName() +
                        " = new " + otherEnd.getAssociationClassPathName().getLast() + "(true)");
                tryInstantiate.getTryPart().addToStatements(otherEnd.getAssociationClassFakePropertyName() + ".fromJson((Map<String, Object>) propertyMap.get(\"" + otherEnd.getAssociationClassFakePropertyName() + "\"))");
                tryInstantiate.getTryPart().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = constructor.newInstance(parentResource, " + otherEnd.getAssociationClassFakePropertyName() + ")");
                tryInstantiate.getTryPart().addToStatements("childResource.fromJson(propertyMap)");
                tryInstantiate.getTryPart().addToStatements("objectList.add(new " + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + "(childResource, \"" + otherEnd.getAssociationClassFakePropertyName() + "\", " + otherEnd.getAssociationClassFakePropertyName() + "))");
                annotatedClass.addToImports(otherEnd.getAssociationClassPathName());
            }
        } else {
            tryInstantiate.getTryPart().addToStatements("Object id = propertyMap.get(\"id\")");
            //Need to remove the one from the map, otherwise the from will also add the one and create another association class
            PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());
            tryInstantiate.getTryPart().addToStatements("propertyMap.remove(\"" + otherEndPWrap.fieldname() + "\")");
            tryInstantiate.getTryPart().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(id)");
            if (!pWrap.isMemberOfAssociationClass()) {
                tryInstantiate.getTryPart().addToStatements("childResource.fromJson(propertyMap)");
                tryInstantiate.getTryPart().addToStatements("parentResource." + pWrap.adder() + "(childResource)");
                tryInstantiate.getTryPart().addToStatements("objectList.add(new " + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + "(childResource))");
            } else {
                tryInstantiate.getTryPart().addToStatements("childResource.fromJson(propertyMap)");
                PropertyWrapper otherEnd = new PropertyWrapper(pWrap.getOtherEnd());

//TODO check this out, it creates a duplicate as the fromJson creates the association class
                tryInstantiate.getTryPart().addToStatements(otherEnd.getAssociationClassPathName().getLast() + " " + otherEnd.getAssociationClassFakePropertyName() +
                        " = new " + otherEnd.getAssociationClassPathName().getLast() + "(true)");
                tryInstantiate.getTryPart().addToStatements(otherEnd.getAssociationClassFakePropertyName() + ".fromJson((Map<String, Object>) propertyMap.get(\"" + otherEnd.getAssociationClassFakePropertyName() + "\"))");
                tryInstantiate.getTryPart().addToStatements("parentResource." + pWrap.adder() + "(childResource, " + otherEnd.getAssociationClassFakePropertyName() + ")");
                tryInstantiate.getTryPart().addToStatements("objectList.add(new " + UmlgRestletGenerationUtil.UmlgNodeJsonHolder.getLast() + "(childResource, \"" + otherEnd.getAssociationClassFakePropertyName() + "\", " + otherEnd.getAssociationClassFakePropertyName() + "))");
                annotatedClass.addToImports(otherEnd.getAssociationClassPathName());
            }
        }

        annotatedClass.addToImports(pWrap.javaBaseTypePath());
        if (pWrap.isOrdered()) {
            //TODO
        } else {
            //TODO
        }
        tryInstantiate.setCatchParam(new OJParameter("e", "Exception"));
        tryInstantiate.getCatchPart().addToStatements("throw new RuntimeException(e)");
    }

    private void buildToJson(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJBlock block) {

        //This is very important to be a sorted set. The get and options method need to return the meta data in the same order.
        //This allows the client to merge them easily

        SortedSet<Classifier> sortedConcreteImplementations = new TreeSet<Classifier>(new Comparator<Classifier>() {
            @Override
            public int compare(Classifier o1, Classifier o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        //For derived unions only show tabs for each property that makes up the union.
        if (pWrap.isDerivedUnion()) {
            List<Property> subsettingProperties = ModelLoader.INSTANCE.findSubsettingProperties(pWrap.getProperty());
            for (Property subsettingProperty : subsettingProperties) {
                sortedConcreteImplementations.addAll(UmlgClassOperations.getConcreteImplementations((Classifier) subsettingProperty.getType()));
            }
        } else {
            //For non derived union show all concrete implementations
            sortedConcreteImplementations = UmlgClassOperations.getConcreteImplementations((Classifier) pWrap.getType());
        }

        Set<Classifier> concreteImplementationsFrom = UmlgClassOperations.getConcreteImplementations((Classifier) pWrap.getOwningType());
        if (!concreteImplementationsFrom.isEmpty()) {
            annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
            //block get reassigned for the meta data if statement
            OJBlock returnBlock = block;
            block.addToStatements("StringBuilder json = new StringBuilder()");
            block.addToStatements("json.append(\"[\")");

            int count = 1;
            // For meta data, put where one is navigating to first, then where on is
            // navigating from
            // This is consistent with navigating to a entity with a vertex where
            // there is no navigating from.
            // i.e. the first meta data in the array is the entity navigating to.
            for (Classifier concreteClassifierTo : sortedConcreteImplementations) {
                annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierTo));
                if (pWrap.isOne()) {
                    block.addToStatements("json.append(\"{\\\"data\\\": \")");
                } else {
                    block.addToStatements("json.append(\"{\\\"data\\\": [\")");
                }
                if (pWrap.isOne()) {
                    OJIfStatement ifOneInstanceOf = new OJIfStatement("parentResource." + pWrap.getter() + "() != null && parentResource." + pWrap.getter() + "().getClass() == "
                            + UmlgClassOperations.getPathName(concreteClassifierTo).getLast() + ".class");
                    ifOneInstanceOf.addToThenPart("json.append(" + UmlgGenerationUtil.ToJsonUtil.getLast() + ".toJsonWithoutCompositeParent(parentResource." + pWrap.getter() + "()))");
                    ifOneInstanceOf.addToElsePart("json.append(\"null\")");
                    block.addToStatements(ifOneInstanceOf);
                } else {
                    block.addToStatements("json.append(" + UmlgGenerationUtil.ToJsonUtil.getLast() + ".toJsonWithoutCompositeParent(parentResource." + pWrap.getter() + "().select(new "
                            + UmlgGenerationUtil.BooleanExpressionEvaluator.getCopy().addToGenerics(UmlgClassOperations.getPathName(pWrap.getType())).getLast()
                            + "() {\n			@Override\n			public Boolean evaluate(" + UmlgClassOperations.getPathName(pWrap.getType()).getLast()
                            + " e) {\n				return e.getClass() == " + UmlgClassOperations.getPathName(concreteClassifierTo).getLast() + ".class;\n			}\n		})))");
                    annotatedClass.addToImports(UmlgGenerationUtil.BooleanExpressionEvaluator);
                }
                annotatedClass.addToImports(UmlgClassOperations.getPathName(pWrap.getType()));
                if (pWrap.isOne()) {
                    block.addToStatements("json.append(\",\")");
                } else {
                    block.addToStatements("json.append(\"],\")");
                }

                block.addToStatements("json.append(\" \\\"meta\\\" : {\")");
                block.addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "\\\"\")");

                block.addToStatements("json.append(\",\\\"qualifiedNameFrom\\\": \\\"\" + parentResource.getQualifiedName() + \"\\\"\")");
                block.addToStatements("json.append(\",\\\"qualifiedNameTo\\\": \\\"" + concreteClassifierTo.getQualifiedName() + "\\\"\")");

                block.addToStatements("json.append(\"}\")");
                if (sortedConcreteImplementations.size() != 1 && count != sortedConcreteImplementations.size()) {
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

    private void buildToJsonForOption(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJBlock block) {

        SortedSet<Classifier> concreteImplementations = new TreeSet<Classifier>(new Comparator<Classifier>() {
            @Override
            public int compare(Classifier o1, Classifier o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        //For derived unions only show tabs for each property that makes up the union.
        if (pWrap.isDerivedUnion()) {
            List<Property> subsettingProperties = ModelLoader.INSTANCE.findSubsettingProperties(pWrap.getProperty());
            for (Property subsettingProperty : subsettingProperties) {
                concreteImplementations.addAll(UmlgClassOperations.getConcreteImplementations((Classifier) subsettingProperty.getType()));
            }
        } else {
            //For non derived union show all concrete implementations
            concreteImplementations = UmlgClassOperations.getConcreteImplementations((Classifier) pWrap.getType());
        }

        Set<Classifier> concreteImplementationsFrom = UmlgClassOperations.getConcreteImplementations((Classifier) pWrap.getOwningType());
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

                for (Classifier concreteClassifierFrom : concreteImplementationsFrom) {

                    block.addToStatements("json.append(\"{\")");

                    block.addToStatements("json.append(\" \\\"meta\\\" : {\")");
                    block.addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "\\\"\")");

                    // The execute ocl query resource is only required if the below
                    // visitor is available
                    if (RestletVisitors.containsVisitorForClass(QueryExecuteResourceBuilder.class)
                            && (pWrap.getType().getQualifiedName().equals(UmlgRestletGenerationUtil.instanceQueryQualifiedName) || pWrap.getType().getQualifiedName().equals(UmlgRestletGenerationUtil.classQueryQualifiedName))) {
                        block.addToStatements("json.append(\", \\\"oclExecuteUri\\\": \\\"/" + pWrap.getModel().getName() + "/{contextId}/oclExecuteQuery\\\"\")");
                    }
                    block.addToStatements("json.append(\", \\\"to\\\": \")");
                    OJBlock conditionBlockFrom = new OJBlock();
                    annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierFrom));

                    conditionBlockFrom.addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(concreteClassifierTo) + ".asJson())");
                    conditionBlockFrom.addToStatements("json.append(\", \\\"from\\\": \")");
                    conditionBlockFrom.addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(concreteClassifierFrom) + ".asJson())");
                    annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierFrom).append(
                            UmlgClassOperations.propertyEnumName(concreteClassifierFrom)));

                    block.addToStatements(conditionBlockFrom);

                    annotatedClass.addToImports(UmlgClassOperations.getPathName(pWrap.getOwningType()).append(
                            UmlgClassOperations.propertyEnumName(pWrap.getOwningType())));
                    annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierTo)
                            .append(UmlgClassOperations.propertyEnumName(concreteClassifierTo)));

                    if (count++ < (concreteImplementations.size() * concreteImplementationsFrom.size())) {
                        block.addToStatements("json.append(\"}},\")");
                    } else {
                        block.addToStatements("json.append(\"}}\")");
                    }

                }
                block = returnBlock;
            }
            returnBlock.addToStatements("json.append(\"]\")");
            returnBlock.addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
        } else {
            //TODO not thought through
            block.addToStatements("return null");
        }
    }

    private void addServerResourceToRouterEnum(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());

        OJEnumLiteral ojLiteral = new OJEnumLiteral(UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_" + pWrap.fieldname());

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "s/{"
                + UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "Id}/" + pWrap.fieldname() + "\"");
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

//        //Add the url for post/put to the resource
//        ojLiteral = new OJEnumLiteral(UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_" + pWrap.fieldname());
//
//        uri = new OJField();
//        uri.setType(new OJPathName("String"));
//        uri.setInitExp("\"" + UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "s/{"
//                + UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "Id}/" + pWrap.fieldname() + "\"");
//        ojLiteral.addToAttributeValues(uri);
//
//        serverResourceClassField = new OJField();
//        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
//        serverResourceClassField.setInitExp(annotatedClass.getName() + ".class");
//        ojLiteral.addToAttributeValues(serverResourceClassField);
//        routerEnum.addToImports(annotatedClass.getPathName());
//        routerEnum.addToImports(UmlgRestletGenerationUtil.ServerResource);
//
//        routerEnum.addToLiterals(ojLiteral);
//
//        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");

    }

//    private void addCompositeParentIdField(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
//        OJField compositeParentFieldId;
//        if (pWrap.getOtherEnd() != null) {
//            compositeParentFieldId = new OJField(UmlgClassOperations.getPathName(pWrap.getOtherEnd().getType()).getLast().toLowerCase() + "Id",
//                new OJPathName("Object"));
//        } else {
//            compositeParentFieldId = new OJField(UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "Id",
//                    new OJPathName("Object"));
//        }
//        compositeParentFieldId.setVisibility(OJVisibilityKind.PRIVATE);
//        annotatedClass.addToFields(compositeParentFieldId);
//    }

}
