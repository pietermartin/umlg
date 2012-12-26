package org.tuml.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.tuml.java.metamodel.*;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.javageneration.util.Namer;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 2012/12/25
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
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

    private void addAndImplementTumlLibNodeOnOriginalClass(OJAnnotatedClass metaClass, Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        annotatedClass.addToImports(metaClass.getPathName());
        annotatedClass.addToImplementedInterfaces(TinkerGenerationUtil.TumlLibNode);
        OJAnnotatedOperation getMetaNode = new OJAnnotatedOperation("getMetaNode");
        TinkerGenerationUtil.addOverrideAnnotation(getMetaNode);
        getMetaNode.setReturnType(TinkerGenerationUtil.TumlMetaNode);
        getMetaNode.getBody().addToStatements("return " + TumlClassOperations.getMetaClassName(clazz) + ".INSTANCE");
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
                        + ".getRoot(), this.vertex, \"root" + TumlClassOperations.className(clazz) + "\")");
        constructor.getBody().addToStatements("edge.setProperty(\"inClass\", this.getClass().getName())");
        annotatedClass.addToImports(TinkerGenerationUtil.edgePathName.getCopy());
        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName.getCopy());
    }

    private void addDefaultConstructor(OJAnnotatedClass annotatedClass, Class clazz) {
        annotatedClass.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.getDefaultConstructor().getBody().addToStatements("super(true)");
    }

    private void addINSTANCE(OJAnnotatedClass ojClass, Class clazz) {
        OJField INSTANCE = new OJField("INSTANCE", ojClass.getPathName());
        INSTANCE.setStatic(true);
        INSTANCE.setInitExp("new " + TumlClassOperations.getMetaClassName(clazz) + "()");
        ojClass.addToFields(INSTANCE);
    }
}
