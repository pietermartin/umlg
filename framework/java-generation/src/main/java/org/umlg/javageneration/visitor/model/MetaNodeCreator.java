package org.umlg.javageneration.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;
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
        metaNodeCreator.setComment("This class is responsible to create the meta singleton upfront.\n * It is invoked via reflection the first time a graph is created.");
        OJPackage ojPackage = new OJPackage(UmlgGenerationUtil.UmlgRootPackage.toJavaString());
        metaNodeCreator.setMyPackage(ojPackage);
        metaNodeCreator.addToImplementedInterfaces(UmlgGenerationUtil.UmlgMetaNodeManager);
        addToSource(metaNodeCreator);

        OJAnnotatedOperation createAll = new OJAnnotatedOperation("createAllMetaNodes");
        metaNodeCreator.addToOperations(createAll);

        List<Class> concreteClasses = ModelLoader.INSTANCE.getAllConcreteClasses();
        for (Class c : concreteClasses) {
            createAll.getBody().addToStatements(UmlgClassOperations.getMetaClassName(c) + ".getInstance()");
            metaNodeCreator.addToImports(UmlgClassOperations.getMetaClassPathName(c));
        }
        OJAnnotatedOperation count = new OJAnnotatedOperation("count", "int");
        count.getBody().addToStatements("return " + concreteClasses.size());
        metaNodeCreator.addToOperations(count);
    }

    @Override
    public void visitAfter(Model element) {
    }
}
