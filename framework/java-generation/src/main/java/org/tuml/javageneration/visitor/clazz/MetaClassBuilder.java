package org.tuml.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.tuml.framework.ModelLoader;
import org.tuml.java.metamodel.*;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.generated.OJVisibilityKindGEN;
import org.tuml.javageneration.util.Namer;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;

/**
 * Date: 2012/12/25
 * Time: 2:47 PM
 */
public class MetaClassBuilder extends ClassBuilder implements Visitor<org.eclipse.uml2.uml.Class> {

    public MetaClassBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Class clazz) {
        //Validate that tumllib is available
        if (!ModelLoader.INSTANCE.isTumlLibIncluded()) {
            throw new IllegalStateException("tumllib is not imported in the model. It is required for " + MetaClassBuilder.class.getName());
        }
        if (!clazz.isAbstract()) {
            OJAnnotatedClass metaClass = new OJAnnotatedClass(TumlClassOperations.getMetaClassName(clazz));
            OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".meta");
            metaClass.setMyPackage(ojPackage);
            metaClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));

            metaClass.setSuperclass(TinkerGenerationUtil.BASE_CLASS_TUML);
            addToSource(metaClass);
            addDefaultConstructor(metaClass, clazz);

            addContructorWithVertex(metaClass, clazz);
            addGetEdgeToRootLabel(metaClass, clazz);
            addImplementsTumlMetaNode(metaClass);
            OJAnnotatedClass annotatedClass = findOJClass(clazz);
            addAndImplementTumlLibNodeOnOriginalClass(annotatedClass, clazz, metaClass.getPathName());

            addMetaClassGetterToRoot(clazz, metaClass);

            //Ensure the meta class instance does not also try to create a edge to a meta class as it is also a normal entity
            addEmptyAddEdgeToMetaNode(metaClass);

            addGetAllInstances(clazz, metaClass);

//        } else {
//            OJAnnotatedClass annotatedClass = findOJClass(clazz);
//            addAndImplementTumlLibNodeOnOriginalClass(annotatedClass, clazz, null);
        }
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
        forIter.getBody().addToStatements("result.add(GraphDb.getDb().<" + classPathName.getLast() + ">instantiateClassifier(TinkerIdUtilFactory.getIdUtil().getId(edge.getVertex(Direction.IN))))");
        allInstances.getBody().addToStatements(forIter);
        allInstances.getBody().addToStatements("return result");

        metaClass.addToImports(TinkerGenerationUtil.TUML_NODE);
        metaClass.addToImports(TinkerGenerationUtil.tinkerIdUtilFactoryPathName);

        metaClass.addToOperations(allInstances);
    }

    private void addGetEdgeToRootLabel(OJAnnotatedClass metaClass, Class clazz) {
        OJAnnotatedOperation getEdgeToRootLabel = new OJAnnotatedOperation("getEdgeToRootLabel", new OJPathName("String"));
        getEdgeToRootLabel.getBody().addToStatements("return \"root" + TumlClassOperations.getMetaClassName(clazz) + "\"");
        metaClass.addToOperations(getEdgeToRootLabel);
    }

    private void addMetaClassGetterToRoot(Class clazz, OJAnnotatedClass metaClass) {

        OJAnnotatedOperation INSTANCE = new OJAnnotatedOperation("getInstance");
        INSTANCE.setStatic(true);
        INSTANCE.setReturnType(metaClass.getPathName());
        metaClass.addToOperations(INSTANCE);
        OJField result = new OJField("result", metaClass.getPathName());
        INSTANCE.getBody().addToLocals(result);

        INSTANCE.getBody().addToStatements("Iterator<Edge> iter = " + TinkerGenerationUtil.graphDbAccess + ".getRoot().getEdges(Direction.OUT, \"" + TinkerGenerationUtil.getEdgeToRootLabelStrategyMeta(clazz) + "\").iterator()");
        OJIfStatement ifHasNext = new OJIfStatement("iter.hasNext()");
        ifHasNext.addToThenPart("result =  new " + TumlClassOperations.getMetaClassName(clazz) + "(iter.next().getVertex(Direction.IN))");
        INSTANCE.getBody().addToStatements(ifHasNext);

        OJIfStatement ifLock = new OJIfStatement(TinkerGenerationUtil.graphDbAccess + ".lockOnTransaction(" + metaClass.getName() + ".class)");
        ifLock.addToThenPart("result = new " + metaClass.getName() + "()");
        ifLock.addToElsePart("iter = " + TinkerGenerationUtil.graphDbAccess + ".getRoot().getEdges(Direction.OUT, \"" + TinkerGenerationUtil.getEdgeToRootLabelStrategyMeta(clazz) + "\").iterator()");
        ifLock.addToElsePart("result = new " + metaClass.getName() + "(iter.next().getVertex(Direction.IN))");
        ifHasNext.addToElsePart(ifLock);

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
        ojClass.addToConstructors(constructor);
    }


    private void addAndImplementTumlLibNodeOnOriginalClass(OJAnnotatedClass annotatedClass, Class clazz, OJPathName metaClassPathName) {
        OJAnnotatedOperation getMetaNode = new OJAnnotatedOperation("getMetaNode");
//        getMetaNode.setStatic(true);
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
        annotatedClass.getDefaultConstructor().getBody().addToStatements(TinkerGenerationUtil.transactionThreadEntityVar.getLast() + ".remove(getVertex().getId().toString())");
        annotatedClass.addToImports(TinkerGenerationUtil.transactionThreadEntityVar);
    }

}
