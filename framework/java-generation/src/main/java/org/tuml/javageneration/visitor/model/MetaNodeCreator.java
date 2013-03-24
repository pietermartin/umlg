package org.tuml.javageneration.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.tuml.framework.ModelLoader;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.java.metamodel.OJPackage;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.eclipse.uml2.uml.Class;

import java.util.List;

/**
 * Date: 2013/03/24
 * Time: 9:21 AM
 */
public class MetaNodeCreator extends BaseVisitor implements Visitor<Model> {

    public MetaNodeCreator(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Model element) {
        OJAnnotatedClass metaNodeCreator = new OJAnnotatedClass("MetaNodeCreator");
        OJPackage ojPackage = new OJPackage(TinkerGenerationUtil.TumlRootPackage.toJavaString());
        metaNodeCreator.setMyPackage(ojPackage);
        metaNodeCreator.addToImplementedInterfaces(TinkerGenerationUtil.TumlMetaNodeManager);
        addToSource(metaNodeCreator);

        OJAnnotatedOperation createAll = new OJAnnotatedOperation("createAllMetaNodes");
        metaNodeCreator.addToOperations(createAll);

        List<Class> concreteClasses = ModelLoader.INSTANCE.getAllConcreteClasses();
        for (Class c : concreteClasses) {
            createAll.getBody().addToStatements(TumlClassOperations.getMetaClassName(c) + ".getInstance()");
            metaNodeCreator.addToImports(TumlClassOperations.getMetaClassPathName(c));
        }

    }

    @Override
    public void visitAfter(Model element) {
    }
}
