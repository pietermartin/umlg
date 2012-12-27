package org.tuml.javageneration.visitor.clazz;

import com.sun.xml.internal.ws.util.StringUtils;
import org.eclipse.uml2.uml.Class;
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
        if (!clazz.isAbstract()) {
            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.getMetaClassName(clazz));
            OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()) + ".meta");
            annotatedClass.setMyPackage(ojPackage);
            annotatedClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));

            annotatedClass.setSuperclass(TinkerGenerationUtil.BASE_CLASS_TUML);
            addToSource(annotatedClass);
            addDefaultConstructor(annotatedClass, clazz);
            annotatedClass.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
            addContructorWithVertex(annotatedClass, clazz);
            addINSTANCE(annotatedClass, clazz);
            addEdgeToRoot(annotatedClass, clazz);
            addImplementsTumlMetaNode(annotatedClass);

            addAndImplementTumlLibNodeOnOriginalClass(annotatedClass, clazz);
        }
    }

    @Override
    protected void addContructorWithVertex(OJAnnotatedClass ojClass, Class clazz) {
        OJConstructor constructor = new OJConstructor();
        constructor.addParam("vertex", TinkerGenerationUtil.vertexPathName);
        constructor.getBody().addToStatements("super(vertex)");
        ojClass.addToConstructors(constructor);
    }


    private void addAndImplementTumlLibNodeOnOriginalClass(OJAnnotatedClass metaClass, Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        annotatedClass.addToImports(metaClass.getPathName());
        annotatedClass.addToImplementedInterfaces(TinkerGenerationUtil.TumlLibNode);
        OJAnnotatedOperation getMetaNode = new OJAnnotatedOperation("getMetaNode");
        TinkerGenerationUtil.addOverrideAnnotation(getMetaNode);
        getMetaNode.setReturnType(TinkerGenerationUtil.TumlMetaNode);
        getMetaNode.getBody().addToStatements("return " + TumlClassOperations.getMetaClassName(clazz) + ".getInstance()");
        annotatedClass.addToOperations(getMetaNode);
    }

    @Override
    public void visitAfter(Class element) {
    }

    private void addImplementsTumlMetaNode(OJAnnotatedClass annotatedClass) {
        annotatedClass.addToImplementedInterfaces(TinkerGenerationUtil.TumlMetaNode);
    }

    private void addEdgeToRoot(OJAnnotatedClass annotatedClass, Class clazz) {
        OJConstructor constructor = annotatedClass.getDefaultConstructor();
        constructor.getBody().addToStatements(
                TinkerGenerationUtil.edgePathName.getLast() + " edge = " + TinkerGenerationUtil.graphDbAccess + ".addEdge(null, " + TinkerGenerationUtil.graphDbAccess
                        + ".getRoot(), this.vertex, \"root" + TumlClassOperations.getMetaClassName(clazz) + "\")");
        constructor.getBody().addToStatements("edge.setProperty(\"inClass\", this.getClass().getName())");
        annotatedClass.addToImports(TinkerGenerationUtil.edgePathName.getCopy());
        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName.getCopy());
    }

    private void addDefaultConstructor(OJAnnotatedClass annotatedClass, Class clazz) {
        annotatedClass.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.getDefaultConstructor().getBody().addToStatements("super(true)");
    }

    private void addINSTANCE(OJAnnotatedClass ojClass, Class clazz) {
        OJField field = new OJField(StringUtils.decapitalize(TumlClassOperations.getMetaClassName(clazz)), ojClass.getPathName());
        field.setStatic(true);
        field.setInitExp("null");
        field.setVisibility(OJVisibilityKindGEN.PRIVATE);
        ojClass.addToFields(field);

        OJAnnotatedOperation INSTANCE = new OJAnnotatedOperation("getInstance");
        INSTANCE.setStatic(true);
        INSTANCE.setReturnType(ojClass.getPathName());
        OJIfStatement ifNull = new OJIfStatement(StringUtils.decapitalize(TumlClassOperations.getMetaClassName(clazz)) + " == null");
        ojClass.addToOperations(INSTANCE);


        ifNull.addToThenPart("ExecutorService es = Executors.newFixedThreadPool(1)");
        ifNull.addToThenPart("es.submit(new Runnable() {\n    @Override\n    public void run() {\n        " +
                TumlClassOperations.getMetaClassName(clazz) + "." + StringUtils.decapitalize(TumlClassOperations.getMetaClassName(clazz)) + " = new " + TumlClassOperations.getMetaClassName(clazz) + "()");

        ifNull.addToThenPart("        " + TinkerGenerationUtil.graphDbAccess + ".stopTransaction(TransactionalGraph.Conclusion.SUCCESS)");
        ojClass.addToImports(TinkerGenerationUtil.tinkerTransactionalGraphPathName);
        ifNull.addToThenPart("    }");
        ifNull.addToThenPart("})");
        ifNull.addToThenPart("es.shutdown()");

        OJTryStatement ojTryStatement = new OJTryStatement();

        OJIfStatement ifSuccess = new OJIfStatement("!es.awaitTermination(3, TimeUnit.SECONDS)");
        ifSuccess.addToThenPart("throw new RuntimeException(\"Dang dog, 3 seconds to insert one miserable vertex is not enuf!!!!\")");
        ojTryStatement.getTryPart().addToStatements(ifSuccess);

        ojClass.addToImports(new OJPathName("java.util.concurrent.TimeUnit"));
        ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.InterruptedException")));
        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");
        ifNull.addToThenPart(ojTryStatement);

        ojClass.addToImports("java.util.concurrent.ExecutorService");
        ojClass.addToImports("java.util.concurrent.Executors");
        ojClass.addToImports("java.lang.Override");

        INSTANCE.getBody().addToStatements(ifNull);
        INSTANCE.getBody().addToStatements("return " + TumlClassOperations.getMetaClassName(clazz) + "." + StringUtils.decapitalize(TumlClassOperations.getMetaClassName(clazz)));

    }
}
