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
            metaClass.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
            addContructorWithVertex(metaClass, clazz);
            addGetEdgeToRootLabel(metaClass, clazz);
            addImplementsTumlMetaNode(metaClass);
            OJAnnotatedClass annotatedClass = findOJClass(clazz);
            addAndImplementTumlLibNodeOnOriginalClass(annotatedClass, clazz, metaClass.getPathName());

            addMetaClassGetterToRoot(clazz, metaClass);
        } else {
            OJAnnotatedClass annotatedClass = findOJClass(clazz);
            addAndImplementTumlLibNodeOnOriginalClass(annotatedClass, clazz, null);
        }
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

        ifHasNext.addToElsePart("synchronized (" + TumlClassOperations.getMetaClassName(clazz) + ".class) {");
        ifHasNext.addToElsePart("    ExecutorService es = Executors.newFixedThreadPool(1, " +
                "new ThreadFactory() {\n        @Override\n        public Thread newThread(Runnable r) {\n            return new Thread(r, \"meta-class-creator-thread\");\n        }\n    })"
        );
        ifHasNext.addToElsePart("\n    Future<" + TumlClassOperations.getMetaClassName(clazz) + "> f = es.submit(" +
                "new Callable() {\n        @Override\n        public " + TumlClassOperations.getMetaClassName(clazz) + " call() {\n            " +
                TumlClassOperations.getMetaClassName(clazz) + " result = new " + TumlClassOperations.getMetaClassName(clazz) + "();\n            " + TinkerGenerationUtil.graphDbAccess + ".commit();\n            return result;\n        }\n    })");
        ifHasNext.addToElsePart("    es.shutdown()");
        ifHasNext.addToElsePart("    try {\n        result = f.get(3, TimeUnit.SECONDS);\n    } catch (Exception e) {\n        throw new RuntimeException(e);\n    }");
        ifHasNext.addToElsePart("}");
        INSTANCE.getBody().addToStatements("return result");
        metaClass.addToImports("java.util.concurrent.ExecutorService");
        metaClass.addToImports("java.util.concurrent.Future");
        metaClass.addToImports("java.util.concurrent.Executors");
        metaClass.addToImports("java.util.concurrent.ThreadFactory");
        metaClass.addToImports("java.util.concurrent.TimeUnit");
        metaClass.addToImports("java.util.concurrent.Callable");
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
        annotatedClass.addToImplementedInterfaces(TinkerGenerationUtil.TumlLibNode);
        OJAnnotatedOperation getMetaNode = new OJAnnotatedOperation("getMetaNode");
        TinkerGenerationUtil.addOverrideAnnotation(getMetaNode);
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
    }

}
