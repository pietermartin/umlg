package org.umlg.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;

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
        if (!clazz.isAbstract()) {
            OJAnnotatedClass metaClass = new OJAnnotatedClass(TumlClassOperations.getMetaClassName(clazz));
            OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".meta");
            metaClass.setMyPackage(ojPackage);
            metaClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));

            if (ModelLoader.INSTANCE.isUmlGLibIncluded()) {
                metaClass.setSuperclass(TinkerGenerationUtil.BASE_CLASS_TUML);
                addDefaultConstructor(metaClass, clazz);
                addContructorWithVertex(metaClass, clazz);
                //Ensure the meta class instance does not also try to create a edge to a meta class as it is also a normal entity
                addEmptyAddEdgeToMetaNode(metaClass);
                addAddToThreadEntityVar(metaClass);
            } else {
                metaClass.setSuperclass(TinkerGenerationUtil.BASE_META_NODE);
                addDefaultConstructorStandAlone(metaClass, clazz);
                addConstructorWithVertexStandAlone(metaClass, clazz);
            }
            addToSource(metaClass);
            addGetEdgeToRootLabel(metaClass, clazz);
            addImplementsTumlMetaNode(metaClass);
            OJAnnotatedClass annotatedClass = findOJClass(clazz);
            addAndImplementUmlgLibNodeOnOriginalClass(annotatedClass, clazz, metaClass.getPathName());

            addMetaClassGetterToRoot(clazz, metaClass);

            addGetAllInstances(clazz, metaClass);
            addGetAllInstancesWithFilter(clazz, metaClass);

            addGetHighId(clazz, metaClass);

            addDefaultCreate(metaClass);

        }
    }

    private void addAddToThreadEntityVar(OJAnnotatedClass metaClass) {
        OJAnnotatedOperation addToThreadMetaEntityVar = new OJAnnotatedOperation("addToThreadEntityVar");
        TinkerGenerationUtil.addOverrideAnnotation(addToThreadMetaEntityVar);
        addToThreadMetaEntityVar.getBody().addToStatements(TinkerGenerationUtil.transactionThreadMetaNodeVar.getLast() + ".setNewEntity(this)");
        metaClass.addToOperations(addToThreadMetaEntityVar);

    }

    private void addDefaultCreate(OJAnnotatedClass metaClass) {
        OJAnnotatedOperation defaultCreate = new OJAnnotatedOperation("defaultCreate");
        TinkerGenerationUtil.addOverrideAnnotation(defaultCreate);
        defaultCreate.getBody().addToStatements("getUid()");
        metaClass.addToOperations(defaultCreate);
    }

//    private void addInternalSetId(Class clazz, OJAnnotatedClass metaClass) {
//        //This method does nothing as meta node's are not accessed via the id
//        OJAnnotatedOperation internalSetId = new OJAnnotatedOperation("internalSetId");
//        TinkerGenerationUtil.addOverrideAnnotation(internalSetId);
//        internalSetId.getBody().addToStatements("setId(\"" + clazz.getQualifiedName() + "Meta::1\")");
//        metaClass.addToOperations(internalSetId);
//    }

    private void addConstructorWithVertexStandAlone(OJAnnotatedClass metaClass, Class clazz) {
        OJConstructor constructor = new OJConstructor();
        constructor.addParam("vertex", TinkerGenerationUtil.vertexPathName);
        constructor.getBody().addToStatements("this.vertex= vertex");
        metaClass.addToConstructors(constructor);
    }

    private void addDefaultConstructorStandAlone(OJAnnotatedClass metaClass, Class clazz) {
        metaClass.getDefaultConstructor().getBody().addToStatements("this.vertex = " + TinkerGenerationUtil.graphDbAccess + ".addVertex(this.getClass().getName())");
        metaClass.getDefaultConstructor().getBody().addToStatements("this.vertex.setProperty(\"className\", getClass().getName())");
        metaClass.getDefaultConstructor().getBody().addToStatements("defaultCreate()");
        metaClass.getDefaultConstructor().getBody().addToStatements(TinkerGenerationUtil.graphDbAccess + ".addEdge(null, " + TinkerGenerationUtil.graphDbAccess + ".getRoot(), this.vertex, getEdgeToRootLabel())");
    }

    private void addEmptyAddEdgeToMetaNode(OJAnnotatedClass metaClass) {
        OJAnnotatedOperation addEdgeToMetaNode = new OJAnnotatedOperation("addEdgeToMetaNode");
        TinkerGenerationUtil.addOverrideAnnotation(addEdgeToMetaNode);
        metaClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        metaClass.addToOperations(addEdgeToMetaNode);
    }

    private void addGetAllInstances(Class clazz, OJAnnotatedClass metaClass) {
        OJAnnotatedOperation allInstances = new OJAnnotatedOperation("getAllInstances");
        TinkerGenerationUtil.addOverrideAnnotation(allInstances);
        OJPathName classPathName = TumlClassOperations.getPathName(clazz);
        allInstances.setReturnType(TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(classPathName));

        OJField resultField = new OJField("result", TinkerGenerationUtil.tumlMemorySet.getCopy().addToGenerics(classPathName));
        resultField.setInitExp("new " + TinkerGenerationUtil.tumlMemorySet.getCopy().addToGenerics(classPathName).getLast() + "()");
        allInstances.getBody().addToLocals(resultField);
        OJField iter = new OJField("iter", new OJPathName("java.lang.Iterable").addToGenerics(TinkerGenerationUtil.edgePathName));
        iter.setInitExp("this.vertex.getEdges(Direction.OUT, TumlNode.ALLINSTANCES_EDGE_LABEL)");
        allInstances.getBody().addToLocals(iter);

        OJForStatement forIter = new OJForStatement("edge", TinkerGenerationUtil.edgePathName, "iter");
        forIter.getBody().addToStatements("result.add(GraphDb.getDb().<" + classPathName.getLast() + ">instantiateClassifier(edge.getVertex(Direction.IN).getId()))");
        allInstances.getBody().addToStatements(forIter);
        allInstances.getBody().addToStatements("return result");

        metaClass.addToImports(TinkerGenerationUtil.TUML_NODE);
        metaClass.addToImports(TinkerGenerationUtil.tinkerIdUtilFactoryPathName);

        metaClass.addToOperations(allInstances);
    }

    private void addGetAllInstancesWithFilter(Class clazz, OJAnnotatedClass metaClass) {
        OJAnnotatedOperation allInstances = new OJAnnotatedOperation("getAllInstances");
        allInstances.addToParameters(new OJParameter("filter", TinkerGenerationUtil.Filter));

        TinkerGenerationUtil.addOverrideAnnotation(allInstances);
        OJPathName classPathName = TumlClassOperations.getPathName(clazz);
        allInstances.setReturnType(TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(classPathName));

        OJField resultField = new OJField("result", TinkerGenerationUtil.tumlMemorySet.getCopy().addToGenerics(classPathName));
        resultField.setInitExp("new " + TinkerGenerationUtil.tumlMemorySet.getCopy().addToGenerics(classPathName).getLast() + "()");
        allInstances.getBody().addToLocals(resultField);
        OJField iter = new OJField("iter", new OJPathName("java.lang.Iterable").addToGenerics(TinkerGenerationUtil.edgePathName));
        iter.setInitExp("this.vertex.getEdges(Direction.OUT, TumlNode.ALLINSTANCES_EDGE_LABEL)");
        allInstances.getBody().addToLocals(iter);

        OJForStatement forIter = new OJForStatement("edge", TinkerGenerationUtil.edgePathName, "iter");
        forIter.getBody().addToStatements(classPathName.getLast() + " instance = GraphDb.getDb().instantiateClassifier(edge.getVertex(Direction.IN).getId())");
        OJIfStatement ifFilter = new OJIfStatement("filter.filter(instance)");
        ifFilter.addToThenPart("result.add(instance)");
        forIter.getBody().addToStatements(ifFilter);

        allInstances.getBody().addToStatements(forIter);
        allInstances.getBody().addToStatements("return result");

        metaClass.addToImports(TinkerGenerationUtil.TUML_NODE);
        metaClass.addToImports(TinkerGenerationUtil.tinkerIdUtilFactoryPathName);

        metaClass.addToOperations(allInstances);
    }

    private void addGetEdgeToRootLabel(OJAnnotatedClass metaClass, Class clazz) {
        OJAnnotatedOperation getEdgeToRootLabel = new OJAnnotatedOperation("getEdgeToRootLabel", new OJPathName("String"));
        getEdgeToRootLabel.getBody().addToStatements("return " + TinkerGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() + ".getUmlgLabelConverter().convert(\"" + TinkerGenerationUtil.getEdgeToRootLabelStrategyMeta(clazz) + "\")");
        metaClass.addToImports(TinkerGenerationUtil.UmlgLabelConverterFactoryPathName);
        metaClass.addToOperations(getEdgeToRootLabel);
    }

    private void addMetaClassGetterToRoot(Class clazz, OJAnnotatedClass metaClass) {

        OJAnnotatedOperation INSTANCE = new OJAnnotatedOperation("getInstance");
        INSTANCE.setStatic(true);
        INSTANCE.setReturnType(metaClass.getPathName());
        metaClass.addToOperations(INSTANCE);
        OJField result = new OJField("result", metaClass.getPathName());
        INSTANCE.getBody().addToLocals(result);

        INSTANCE.getBody().addToStatements("Iterator<Edge> iter = " + TinkerGenerationUtil.graphDbAccess + ".getRoot().getEdges(Direction.OUT, " + TinkerGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() + ".getUmlgLabelConverter().convert(\"" + TinkerGenerationUtil.getEdgeToRootLabelStrategyMeta(clazz) + "\")).iterator()");
        OJIfStatement ifHasNext = new OJIfStatement("iter.hasNext()");
        ifHasNext.addToThenPart("result =  new " + TumlClassOperations.getMetaClassName(clazz) + "(iter.next().getVertex(Direction.IN))");
        INSTANCE.getBody().addToStatements(ifHasNext);

        ifHasNext.addToElsePart("iter = " + TinkerGenerationUtil.graphDbAccess + ".getRoot().getEdges(Direction.OUT, " + TinkerGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() + ".getUmlgLabelConverter().convert(\"" + TinkerGenerationUtil.getEdgeToRootLabelStrategyMeta(clazz) + "\")).iterator()");

        OJIfStatement ifIter2 = new OJIfStatement("!iter.hasNext()");
        ifIter2.addToThenPart("result = new " + metaClass.getName() + "()");

        ifIter2.addToElsePart("result = new " + metaClass.getName() + "(iter.next().getVertex(Direction.IN))");
        ifHasNext.addToElsePart(ifIter2);

        INSTANCE.getBody().addToStatements("return result");
        metaClass.addToImports("java.util.Iterator");

        metaClass.addToImports("com.tinkerpop.blueprints.Direction");
        metaClass.addToImports("com.tinkerpop.blueprints.Direction");
        metaClass.addToImports("com.tinkerpop.blueprints.Edge");
        metaClass.addToImports(TinkerGenerationUtil.graphDbPathName);

    }

    @Override
    protected void addContructorWithVertex(OJAnnotatedClass ojClass, Class clazz) {
        OJConstructor constructor = new OJConstructor();
        constructor.addParam("vertex", TinkerGenerationUtil.vertexPathName);
        constructor.getBody().addToStatements("super(vertex)");
        constructor.getBody().addToStatements(TinkerGenerationUtil.transactionThreadMetaNodeVar.getLast() + ".setNewEntity(this)");
        constructor.getBody().addToStatements(TinkerGenerationUtil.transactionThreadEntityVar.getLast() + ".remove(this)");
        ojClass.addToImports(TinkerGenerationUtil.transactionThreadMetaNodeVar);
        ojClass.addToConstructors(constructor);
    }

    private void addAndImplementUmlgLibNodeOnOriginalClass(OJAnnotatedClass annotatedClass, Class clazz, OJPathName metaClassPathName) {
        OJAnnotatedOperation getMetaNode = new OJAnnotatedOperation("getMetaNode");
        getMetaNode.setReturnType(TinkerGenerationUtil.TumlMetaNode);
        annotatedClass.addToOperations(getMetaNode);
        getMetaNode.setAbstract(clazz.isAbstract());
        if (!clazz.isAbstract()) {
            annotatedClass.addToImports(metaClassPathName);
            getMetaNode.getBody().addToStatements("return " + TumlClassOperations.getMetaClassName(clazz) + ".getInstance()");
        }
    }

    @Override
    public void visitAfter(Class element) {
    }

    private void addImplementsTumlMetaNode(OJAnnotatedClass annotatedClass) {
        annotatedClass.addToImplementedInterfaces(TinkerGenerationUtil.TumlMetaNode);
    }

    private void addDefaultConstructor(OJAnnotatedClass annotatedClass, Class clazz) {
        annotatedClass.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.getDefaultConstructor().getBody().addToStatements("super(true)");
        annotatedClass.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.getDefaultConstructor().getBody().addToStatements(TinkerGenerationUtil.transactionThreadEntityVar.getLast() + ".remove(this)");
        annotatedClass.addToImports(TinkerGenerationUtil.transactionThreadEntityVar);
    }

    private void addGetHighId(Class clazz, OJAnnotatedClass metaClass) {
        OJAnnotatedOperation getIdHigh = new OJAnnotatedOperation("getIdHigh", new OJPathName("java.lang.Long"));
        TinkerGenerationUtil.addOverrideAnnotation(getIdHigh);
        getIdHigh.getBody().addToStatements("return this.vertex.getProperty(\"highId\")");
        metaClass.addToOperations(getIdHigh);
    }

}
