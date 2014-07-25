package org.umlg.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.generated.OJVisibilityKindGEN;
import org.umlg.java.metamodel.java8.ForEachStatement;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;

import java.util.Set;

/**
 * Date: 2012/12/25
 * Time: 2:47 PM
 */
public class MetaClassBuilder extends ClassBuilder implements Visitor<Class> {

    public MetaClassBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        OJAnnotatedClass metaClass = new OJAnnotatedClass(UmlgClassOperations.getMetaClassName(clazz));
        OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".meta");
        metaClass.setMyPackage(ojPackage);
        metaClass.setVisibility(UmlgClassOperations.getVisibility(clazz.getVisibility()));

        if (ModelLoader.INSTANCE.isUmlGLibIncluded()) {
            metaClass.setSuperclass(UmlgGenerationUtil.BASE_CLASS_UMLG);
            addDefaultConstructor(metaClass, clazz);
            addContructorWithVertex(metaClass, clazz);
            //Ensure the meta class instance does not also try to create a edge to a meta class as it is also a normal entity
            addAddToThreadEntityVar(metaClass);
        } else {
            metaClass.setSuperclass(UmlgGenerationUtil.BASE_META_NODE);
            addDefaultConstructorStandAlone(metaClass, clazz);
            addConstructorWithVertexStandAlone(metaClass, clazz);
        }
        addToSource(metaClass);
        addGetEdgeToRootLabel(metaClass, clazz);
        addImplementsTumlMetaNode(metaClass);
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        addMetaClassGetterToRoot(clazz, metaClass);
        addDefaultCreate(metaClass);
        if (!clazz.isAbstract()) {
            addAndImplementUmlgLibNodeOnOriginalClass(annotatedClass, clazz, metaClass.getPathName());
//            addGetAllInstances(clazz, metaClass);
//            addGetAllInstancesWithFilter(clazz, metaClass);
        } else {
//            addGetAllInstancesForAbstractClass(clazz, metaClass);
//            addGetAllInstancesWithFilterForAbstractClass(clazz, metaClass);
        }
    }

    private void addAddToThreadEntityVar(OJAnnotatedClass metaClass) {
        OJAnnotatedOperation addToThreadMetaEntityVar = new OJAnnotatedOperation("addToThreadEntityVar");
        UmlgGenerationUtil.addOverrideAnnotation(addToThreadMetaEntityVar);
        addToThreadMetaEntityVar.getBody().addToStatements(UmlgGenerationUtil.transactionThreadMetaNodeVar.getLast() + ".setNewEntity(this)");
        metaClass.addToOperations(addToThreadMetaEntityVar);

    }

    private void addDefaultCreate(OJAnnotatedClass metaClass) {
        OJAnnotatedOperation defaultCreate = new OJAnnotatedOperation("defaultCreate");
        UmlgGenerationUtil.addOverrideAnnotation(defaultCreate);
        defaultCreate.getBody().addToStatements("getUid()");
        metaClass.addToOperations(defaultCreate);
    }

    private void addConstructorWithVertexStandAlone(OJAnnotatedClass metaClass, Class clazz) {
        OJConstructor constructor = new OJConstructor();
        constructor.addParam("vertex", UmlgGenerationUtil.vertexPathName);
        constructor.getBody().addToStatements("this.vertex= vertex");
        metaClass.addToConstructors(constructor);
    }

    private void addDefaultConstructorStandAlone(OJAnnotatedClass metaClass, Class clazz) {
        metaClass.getDefaultConstructor().getBody().addToStatements("this.vertex = " + UmlgGenerationUtil.UMLGAccess + ".addVertex(this.getClass().getName())");
        metaClass.getDefaultConstructor().getBody().addToStatements("this.vertex.property(\"className\", getClass().getName())");
        metaClass.getDefaultConstructor().getBody().addToStatements("defaultCreate()");
        metaClass.getDefaultConstructor().getBody().addToStatements(UmlgGenerationUtil.UMLGAccess + ".getRoot().addEdge(getEdgeToRootLabel(), this.vertex)");
    }

    private void addGetEdgeToRootLabel(OJAnnotatedClass metaClass, Class clazz) {
        OJAnnotatedOperation getEdgeToRootLabel = new OJAnnotatedOperation("getEdgeToRootLabel", new OJPathName("String"));
        getEdgeToRootLabel.getBody().addToStatements("return " + UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() + ".getUmlgLabelConverter().convert(\"" + UmlgGenerationUtil.getEdgeToRootLabelStrategyMeta(clazz) + "\")");
        metaClass.addToImports(UmlgGenerationUtil.UmlgLabelConverterFactoryPathName);
        metaClass.addToOperations(getEdgeToRootLabel);
    }

    private void addMetaClassGetterToRoot(Class clazz, OJAnnotatedClass metaClass) {

        OJAnnotatedOperation INSTANCE = new OJAnnotatedOperation("getInstance");
        INSTANCE.setComment("getInstance() does not need to be synchronized as the edge to the root vertex is created on startup.");

        INSTANCE.setStatic(true);
        INSTANCE.setReturnType(metaClass.getPathName());
        metaClass.addToOperations(INSTANCE);
        OJField result = new OJField("result", metaClass.getPathName());
        INSTANCE.getBody().addToLocals(result);

        INSTANCE.getBody().addToStatements("Iterator<Edge> iter = " + UmlgGenerationUtil.UMLGAccess + ".getRoot().outE(" + UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() + ".getUmlgLabelConverter().convert(\"" + UmlgGenerationUtil.getEdgeToRootLabelStrategyMeta(clazz) + "\"))");
        OJIfStatement ifHasNext = new OJIfStatement("iter.hasNext()");
        ifHasNext.addToThenPart("result =  new " + UmlgClassOperations.getMetaClassName(clazz) + "(iter.next().inV().next())");
        INSTANCE.getBody().addToStatements(ifHasNext);

        ifHasNext.addToElsePart("iter = " + UmlgGenerationUtil.UMLGAccess + ".getRoot().outE(" + UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() + ".getUmlgLabelConverter().convert(\"" + UmlgGenerationUtil.getEdgeToRootLabelStrategyMeta(clazz) + "\"))");

        OJIfStatement ifIter2 = new OJIfStatement("!iter.hasNext()");
        ifIter2.addToThenPart("result = new " + metaClass.getName() + "()");

        ifIter2.addToElsePart("result = new " + metaClass.getName() + "(iter.next().inV().next())");
        ifHasNext.addToElsePart(ifIter2);

        INSTANCE.getBody().addToStatements("return result");
        metaClass.addToImports("java.util.Iterator");

        metaClass.addToImports(UmlgGenerationUtil.edgePathName);
        metaClass.addToImports(UmlgGenerationUtil.UMLGPathName);

    }

    @Override
    protected void addContructorWithVertex(OJAnnotatedClass ojClass, Classifier classifier) {
        OJConstructor constructor = new OJConstructor();
        constructor.setVisibility(OJVisibilityKindGEN.PUBLIC);
        constructor.addParam("vertex", UmlgGenerationUtil.vertexPathName);
        constructor.getBody().addToStatements("super(vertex)");
        constructor.getBody().addToStatements(UmlgGenerationUtil.transactionThreadMetaNodeVar.getLast() + ".setNewEntity(this)");
        constructor.getBody().addToStatements(UmlgGenerationUtil.transactionThreadEntityVar.getLast() + ".remove(this)");
        ojClass.addToImports(UmlgGenerationUtil.transactionThreadMetaNodeVar);
        ojClass.addToConstructors(constructor);
    }

    private void addAndImplementUmlgLibNodeOnOriginalClass(OJAnnotatedClass annotatedClass, Class clazz, OJPathName metaClassPathName) {
        OJAnnotatedOperation getMetaNode = new OJAnnotatedOperation("getMetaNode");
        getMetaNode.setReturnType(UmlgGenerationUtil.UmlgMetaNode);
        annotatedClass.addToOperations(getMetaNode);
        getMetaNode.setAbstract(clazz.isAbstract());
        if (!clazz.isAbstract()) {
            annotatedClass.addToImports(metaClassPathName);
            getMetaNode.getBody().addToStatements("return " + UmlgClassOperations.getMetaClassName(clazz) + ".getInstance()");
        }
    }

    @Override
    public void visitAfter(Class element) {
    }

    private void addImplementsTumlMetaNode(OJAnnotatedClass annotatedClass) {
        annotatedClass.addToImplementedInterfaces(UmlgGenerationUtil.UmlgMetaNode);
    }

    private void addDefaultConstructor(OJAnnotatedClass annotatedClass, Class clazz) {
        annotatedClass.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.getDefaultConstructor().getBody().addToStatements("super(true)");
        annotatedClass.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.getDefaultConstructor().getBody().addToStatements(UmlgGenerationUtil.transactionThreadEntityVar.getLast() + ".remove(this)");
        annotatedClass.addToImports(UmlgGenerationUtil.transactionThreadEntityVar);
    }

}
