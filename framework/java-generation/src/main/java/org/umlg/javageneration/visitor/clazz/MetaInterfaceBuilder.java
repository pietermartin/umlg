package org.umlg.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.generated.OJVisibilityKindGEN;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.Set;

/**
 * Date: 2012/12/25
 * Time: 2:47 PM
 */
public class MetaInterfaceBuilder extends BaseVisitor implements Visitor<Interface> {

    public MetaInterfaceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Interface clazz) {
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
        addMetaClassGetterToRoot(clazz, metaClass);
        addDefaultCreate(metaClass);
//        addGetAllInstancesForAbstractClass(clazz, metaClass);
//        addGetAllInstancesWithFilterForAbstractClass(clazz, metaClass);
    }

    @Override
    public void visitAfter(Interface element) {
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

    private void addConstructorWithVertexStandAlone(OJAnnotatedClass metaClass, Classifier classifier) {
        OJConstructor constructor = new OJConstructor();
        constructor.addParam("vertex", UmlgGenerationUtil.vertexPathName);
        constructor.getBody().addToStatements("this.vertex= vertex");
        metaClass.addToConstructors(constructor);
    }

    private void addDefaultConstructorStandAlone(OJAnnotatedClass metaClass, Classifier classifier) {
        metaClass.getDefaultConstructor().getBody().addToStatements("this.vertex = " + UmlgGenerationUtil.UMLGAccess + ".addVertex(this.getClass().getName())");
        metaClass.getDefaultConstructor().getBody().addToStatements("this.vertex.property(\"className\", getClass().getName())");
        metaClass.getDefaultConstructor().getBody().addToStatements("defaultCreate()");
//        metaClass.getDefaultConstructor().getBody().addToStatements(UmlgGenerationUtil.UMLGAccess + ".addEdge(null, " + UmlgGenerationUtil.UMLGAccess + ".getRoot(), this.vertex, getEdgeToRootLabel())");
        metaClass.getDefaultConstructor().getBody().addToStatements(UmlgGenerationUtil.UMLGAccess + ".getRoot().addEdge(getEdgeToRootLabel(), this.vertex)");
    }

    private void addEmptyAddEdgeToMetaNode(OJAnnotatedClass metaClass) {
        OJAnnotatedOperation addEdgeToMetaNode = new OJAnnotatedOperation("addEdgeToMetaNode");
        UmlgGenerationUtil.addOverrideAnnotation(addEdgeToMetaNode);
        metaClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        metaClass.addToOperations(addEdgeToMetaNode);
    }

    private void addGetAllInstancesForAbstractClass(Classifier classifier, OJAnnotatedClass metaClass) {
        OJAnnotatedOperation allInstances = new OJAnnotatedOperation("getAllInstances");
        UmlgGenerationUtil.addOverrideAnnotation(allInstances);
        OJPathName classPathName = UmlgClassOperations.getPathName(classifier);
        allInstances.setReturnType(UmlgGenerationUtil.umlgSet.getCopy().addToGenerics(classPathName));

        OJField resultField = new OJField("result", UmlgGenerationUtil.umlgMemorySet.getCopy().addToGenerics(classPathName));
        resultField.setInitExp("new " + UmlgGenerationUtil.umlgMemorySet.getCopy().addToGenerics(classPathName).getLast() + "()");
        allInstances.getBody().addToLocals(resultField);

        Set<Classifier> concreteImplementations = UmlgClassOperations.getConcreteRealization(classifier);
        for (Classifier concreteImplementation : concreteImplementations) {
            allInstances.getBody().addToStatements("result.addAll(" + UmlgClassOperations.getMetaClassName(concreteImplementation) + ".getInstance().getAllInstances())");
            metaClass.addToImports(UmlgClassOperations.getMetaClassPathName(concreteImplementation));
        }
        allInstances.getBody().addToStatements("return result");

        metaClass.addToImports(UmlgGenerationUtil.UMLG_NODE);
        metaClass.addToOperations(allInstances);
    }

    private void addGetAllInstancesWithFilterForAbstractClass(Classifier classifier, OJAnnotatedClass metaClass) {
        OJAnnotatedOperation allInstances = new OJAnnotatedOperation("getAllInstances");
        allInstances.addToParameters(new OJParameter("filter", UmlgGenerationUtil.Filter));

        UmlgGenerationUtil.addOverrideAnnotation(allInstances);
        OJPathName classPathName = UmlgClassOperations.getPathName(classifier);
        allInstances.setReturnType(UmlgGenerationUtil.umlgSet.getCopy().addToGenerics(classPathName));

        OJField resultField = new OJField("result", UmlgGenerationUtil.umlgMemorySet.getCopy().addToGenerics(classPathName));
        resultField.setInitExp("new " + UmlgGenerationUtil.umlgMemorySet.getCopy().addToGenerics(classPathName).getLast() + "()");
        allInstances.getBody().addToLocals(resultField);

        Set<Classifier> concreteImplementations = UmlgClassOperations.getConcreteRealization(classifier);
        for (Classifier concreteImplementation : concreteImplementations) {
            allInstances.getBody().addToStatements("result.addAll(" + UmlgClassOperations.getMetaClassName(concreteImplementation) + ".getInstance().getAllInstances())");
            metaClass.addToImports(UmlgClassOperations.getMetaClassPathName(concreteImplementation));
        }
        allInstances.getBody().addToStatements("return result");

        metaClass.addToImports(UmlgGenerationUtil.UMLG_NODE);
        metaClass.addToOperations(allInstances);
    }

    private void addGetEdgeToRootLabel(OJAnnotatedClass metaClass, Classifier classifier) {
        OJAnnotatedOperation getEdgeToRootLabel = new OJAnnotatedOperation("getEdgeToRootLabel", new OJPathName("String"));
        getEdgeToRootLabel.getBody().addToStatements("return " + UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() + ".getUmlgLabelConverter().convert(\"" + UmlgGenerationUtil.getEdgeToRootLabelStrategyMeta(classifier) + "\")");
        metaClass.addToImports(UmlgGenerationUtil.UmlgLabelConverterFactoryPathName);
        metaClass.addToOperations(getEdgeToRootLabel);
    }

    private void addMetaClassGetterToRoot(Classifier classifier, OJAnnotatedClass metaClass) {

        OJAnnotatedOperation INSTANCE = new OJAnnotatedOperation("getInstance");
        INSTANCE.setStatic(true);
        INSTANCE.setReturnType(metaClass.getPathName());
        metaClass.addToOperations(INSTANCE);
        OJField result = new OJField("result", metaClass.getPathName());
        INSTANCE.getBody().addToLocals(result);

        INSTANCE.getBody().addToStatements("Iterator<Edge> iter = " + UmlgGenerationUtil.UMLGAccess + ".getRoot().edges(Direction.OUT, " + UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() + ".getUmlgLabelConverter().convert(\"" + UmlgGenerationUtil.getEdgeToRootLabelStrategyMeta(classifier) + "\"))");
        OJIfStatement ifHasNext = new OJIfStatement("iter.hasNext()");
        ifHasNext.addToThenPart("result =  new " + UmlgClassOperations.getMetaClassName(classifier) + "(iter.next().inVertex())");
        INSTANCE.getBody().addToStatements(ifHasNext);

        ifHasNext.addToElsePart("iter = " + UmlgGenerationUtil.UMLGAccess + ".getRoot().edges(Direction.OUT, " + UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() + ".getUmlgLabelConverter().convert(\"" + UmlgGenerationUtil.getEdgeToRootLabelStrategyMeta(classifier) + "\"))");

        OJIfStatement ifIter2 = new OJIfStatement("!iter.hasNext()");
        ifIter2.addToThenPart("result = new " + metaClass.getName() + "()");

        ifIter2.addToElsePart("result = new " + metaClass.getName() + "(iter.next().inVertex())");
        ifHasNext.addToElsePart(ifIter2);

        INSTANCE.getBody().addToStatements("return result");
        metaClass.addToImports("java.util.Iterator");

        metaClass.addToImports(UmlgGenerationUtil.tinkerDirection);
        metaClass.addToImports(UmlgGenerationUtil.edgePathName);
        metaClass.addToImports(UmlgGenerationUtil.UMLGPathName);

    }

    private void addContructorWithVertex(OJAnnotatedClass ojClass, Classifier classifier) {
        OJConstructor constructor = new OJConstructor();
        constructor.setVisibility(OJVisibilityKindGEN.PUBLIC);
        constructor.addParam("vertex", UmlgGenerationUtil.vertexPathName);
        constructor.getBody().addToStatements("super(vertex)");
        constructor.getBody().addToStatements(UmlgGenerationUtil.transactionThreadMetaNodeVar.getLast() + ".setNewEntity(this)");
        constructor.getBody().addToStatements(UmlgGenerationUtil.transactionThreadEntityVar.getLast() + ".remove(this)");
        ojClass.addToImports(UmlgGenerationUtil.transactionThreadMetaNodeVar);
        ojClass.addToConstructors(constructor);
    }

    private void addImplementsTumlMetaNode(OJAnnotatedClass annotatedClass) {
        annotatedClass.addToImplementedInterfaces(UmlgGenerationUtil.UmlgMetaNode);
    }

    private void addDefaultConstructor(OJAnnotatedClass annotatedClass, Classifier classifier) {
        annotatedClass.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.getDefaultConstructor().getBody().addToStatements("super(true)");
        annotatedClass.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.getDefaultConstructor().getBody().addToStatements(UmlgGenerationUtil.transactionThreadEntityVar.getLast() + ".remove(this)");
        annotatedClass.addToImports(UmlgGenerationUtil.transactionThreadEntityVar);
    }

}
