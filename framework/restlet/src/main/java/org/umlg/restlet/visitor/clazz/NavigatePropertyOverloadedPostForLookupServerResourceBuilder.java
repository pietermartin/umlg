package org.umlg.restlet.visitor.clazz;

import org.eclipse.uml2.uml.Property;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.*;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;

public class NavigatePropertyOverloadedPostForLookupServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Property> {

    public NavigatePropertyOverloadedPostForLookupServerResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper pWrap = new PropertyWrapper(p);
        if (!pWrap.isDataType() && !pWrap.isEnumeration() && pWrap.isNavigable() && !pWrap.isRefined()) {

            OJAnnotatedClass owner = findOJClass(pWrap.getType());
            OJPackage ojPackage = owner.getMyPackage();

            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(getClassName(pWrap, "LookupServerResourceImpl"));
//            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_"
//                    + pWrap.getOtherEnd().getName() + "_" + pWrap.getName() + "_LookupServerResourceImpl");

            annotatedClass.setSuperclass(UmlgRestletGenerationUtil.ServerResource);
            annotatedClass.setMyPackage(ojPackage);
            addToSource(annotatedClass);
            addDefaultConstructor(annotatedClass);

            addCompositeParentIdField(pWrap, annotatedClass);
            if (!pWrap.isDerived()) {
                addPostObjectRepresentation(pWrap/*, annotatedInf*/, annotatedClass);
            }
            addServerResourceToRouterEnum(pWrap, annotatedClass);

        }
    }

    @Override
    public void visitAfter(Property p) {
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

    private void addPostObjectRepresentation(PropertyWrapper pWrap/*, OJAnnotatedInterface annotatedInf*/, OJAnnotatedClass annotatedClass) {
//        OJAnnotatedOperation postInf = new OJAnnotatedOperation("post", UmlgRestletGenerationUtil.Representation);
//        postInf.addToParameters(new OJParameter("entity", UmlgRestletGenerationUtil.Representation));
//        annotatedInf.addToOperations(postInf);
//        postInf.addAnnotationIfNew(new OJAnnotationValue(UmlgRestletGenerationUtil.Post, "json"));

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

        ojTryStatement.getTryPart().addToStatements(pathName.getLast() + " overloaded = mapper.readValue(" + entityText.getName() + ", Map.class)");
        ojTryStatement.getTryPart().addToStatements("Object o = overloaded.get(\"insert\")");

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

        //get the lookup uri
        ojTryStatement.getTryPart().addToStatements("String lookupUri = " + UmlgRestletGenerationUtil.UmlgURLEncoder.getLast() + ".decode(getReference().getQueryAsForm(false).getFirstValue(\"lookupUri\"))");
        ojTryStatement.getTryPart().addToStatements("lookupUri = \"riap://host\" + lookupUri");
        ojTryStatement.getTryPart().addToStatements("int fakeIdIndex = lookupUri.indexOf(\"fake\")");
        OJIfStatement ifFakeId = new OJIfStatement("fakeIdIndex != -1");
        ifFakeId.addToThenPart("int indexOfForwardSlash = lookupUri.indexOf(\"/\", fakeIdIndex)");
        ifFakeId.addToThenPart("String fakeId = lookupUri.substring(fakeIdIndex, indexOfForwardSlash)");
        ifFakeId.addToThenPart("Object id = " + UmlgGenerationUtil.UmlgTmpIdManager.getLast() + ".INSTANCE.get(fakeId)");
        ifFakeId.addToThenPart("lookupUri = lookupUri.replace(fakeId, " + UmlgRestletGenerationUtil.UmlgURLEncoder.getLast() + ".encode(id.toString()))");
        ojTryStatement.getTryPart().addToStatements(ifFakeId);
        annotatedClass.addToImports(UmlgGenerationUtil.UmlgTmpIdManager);

        ojTryStatement.getTryPart().addToStatements(UmlgRestletGenerationUtil.ClientResource.getLast() + " cr = new ClientResource(lookupUri)");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLEncoder);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ClientResource);
        ojTryStatement.getTryPart().addToStatements(UmlgRestletGenerationUtil.Representation.getLast() + " result = cr.get()");
        ojTryStatement.getTryPart().addToStatements("return result");

        //Always rollback
        ojTryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.UmlgTmpIdManager.getLast() + ".INSTANCE.remove()");
        ojTryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");

        ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));

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
        put.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(put);

        OJBlock firstBlock = new OJBlock();
        firstBlock.addToStatements("Object id = propertyMap.get(\"id\")");
        firstBlock.addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(id)");
        annotatedClass.addToImports(pWrap.javaBaseTypePath());
        firstBlock.addToStatements("childResource.fromJson(propertyMap)");
        put.getBody().addToStatements(firstBlock);

        OJBlock secondBlock = new OJBlock();

        OJField baseTumlClass = new OJField("baseTumlClass", new OJPathName("Class").addToGenerics("? extends " + pWrap.javaBaseTypePath().getLast()));
        baseTumlClass.setInitExp("childResource.getClass()");
        secondBlock.addToLocals(baseTumlClass);

        put.getBody().addToStatements(secondBlock);
    }

    private void addPostResource(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation add = new OJAnnotatedOperation("add");
        add.setVisibility(OJVisibilityKind.PRIVATE);

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

        OJTryStatement tryInstantiate = new OJTryStatement();
        add.getBody().addToStatements(tryInstantiate);

        if (pWrap.isComposite()) {
            PropertyWrapper otherEndPWrap = new PropertyWrapper(pWrap.getOtherEnd());
            if (!pWrap.isMemberOfAssociationClass()) {
                OJField constructor = new OJField("constructor", new OJPathName("java.lang.reflect.Constructor").addToGenerics(pWrap.javaBaseTypePath()));
                constructor.setInitExp("baseTumlClass.getConstructor(" + otherEndPWrap.javaBaseTypePath().getLast() + ".class)");
                tryInstantiate.getTryPart().addToLocals(constructor);
                tryInstantiate.getTryPart().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = constructor.newInstance(parentResource)");
            } else {
                PropertyWrapper otherEnd = new PropertyWrapper(pWrap.getOtherEnd());
                OJField constructor = new OJField("constructor", new OJPathName("java.lang.reflect.Constructor").addToGenerics(pWrap.javaBaseTypePath()));
                constructor.setInitExp("baseTumlClass.getConstructor(" + otherEndPWrap.javaBaseTypePath().getLast() + ".class, " + otherEnd.getAssociationClassPathName().getLast() + ".class)");
                tryInstantiate.getTryPart().addToLocals(constructor);
                tryInstantiate.getTryPart().addToStatements(otherEnd.getAssociationClassPathName().getLast() + " " + otherEnd.getAssociationClassFakePropertyName() +
                        " = new " + otherEnd.getAssociationClassPathName().getLast() + "(true)");
                tryInstantiate.getTryPart().addToStatements(otherEnd.getAssociationClassFakePropertyName() + ".fromJson((Map<String, Object>) propertyMap.get(\"" + otherEnd.getAssociationClassFakePropertyName() + "\"))");
                tryInstantiate.getTryPart().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = constructor.newInstance(parentResource, " + otherEnd.getAssociationClassFakePropertyName() + ")");
                annotatedClass.addToImports(otherEnd.getAssociationClassPathName());
            }
        } else {
            tryInstantiate.getTryPart().addToStatements("Object id = propertyMap.get(\"id\")");
            tryInstantiate.getTryPart().addToStatements(pWrap.javaBaseTypePath().getLast() + " childResource = " + UmlgGenerationUtil.UMLGAccess + ".instantiateClassifier(id)");
            if (!pWrap.isMemberOfAssociationClass()) {
                tryInstantiate.getTryPart().addToStatements("parentResource." + pWrap.adder() + "(childResource)");
            } else {
                PropertyWrapper otherEnd = new PropertyWrapper(pWrap.getOtherEnd());
                tryInstantiate.getTryPart().addToStatements(otherEnd.getAssociationClassPathName().getLast() + " " + otherEnd.getAssociationClassFakePropertyName() +
                        " = new " + otherEnd.getAssociationClassPathName().getLast() + "(true)");
                tryInstantiate.getTryPart().addToStatements(otherEnd.getAssociationClassFakePropertyName() + ".fromJson((Map<String, Object>) propertyMap.get(\"" + otherEnd.getAssociationClassFakePropertyName() + "\"))");
                tryInstantiate.getTryPart().addToStatements("parentResource." + pWrap.adder() + "(childResource, " + otherEnd.getAssociationClassFakePropertyName() + ")");
                annotatedClass.addToImports(otherEnd.getAssociationClassPathName());
            }
        }
        annotatedClass.addToImports(pWrap.javaBaseTypePath());
        tryInstantiate.getTryPart().addToStatements("childResource.fromJson(propertyMap)");
        if (pWrap.isOrdered()) {
            //TODO
        } else {
            //TODO
        }
        tryInstantiate.setCatchParam(new OJParameter("e", "Exception"));
        tryInstantiate.getCatchPart().addToStatements("throw new RuntimeException(e)");
    }

    private void addServerResourceToRouterEnum(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());

        OJEnumLiteral ojLiteral = new OJEnumLiteral(UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_" + pWrap.fieldname() + "_forwardToLookup");

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "s/{"
                + UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "Id}/" + pWrap.fieldname() + "_forwardToLookup\"");
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

//    private void addCompositeParentIdField(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
//        OJField compositeParentFieldId = new OJField(UmlgClassOperations.getPathName(pWrap.getOtherEnd().getType()).getLast().toLowerCase() + "Id",
//                new OJPathName("Object"));
//        compositeParentFieldId.setVisibility(OJVisibilityKind.PRIVATE);
//        annotatedClass.addToFields(compositeParentFieldId);
//    }

}
