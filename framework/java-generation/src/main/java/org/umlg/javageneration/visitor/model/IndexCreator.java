package org.umlg.javageneration.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.List;

/**
 * Date: 2013/03/24
 * Time: 9:21 AM
 */
public class IndexCreator extends BaseVisitor implements Visitor<Model> {

    public IndexCreator(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Model element) {
        OJAnnotatedClass indexCreator = new OJAnnotatedClass("IndexCreator");
        indexCreator.setComment("This class is responsible to create all keyed indexes.\nIt is invoked the first time a graph is created.");
        OJPackage ojPackage = new OJPackage(TinkerGenerationUtil.UmlgRootPackage.toJavaString());
        indexCreator.setMyPackage(ojPackage);
        indexCreator.addToImplementedInterfaces(TinkerGenerationUtil.UmlgIndexManager);
        addToSource(indexCreator);

        OJAnnotatedOperation createIndexes = new OJAnnotatedOperation("createIndexes");
        indexCreator.addToOperations(createIndexes);

        List<Property> qualifiers = ModelLoader.INSTANCE.getAllQualifiers();
        for (Property q : qualifiers) {
            PropertyWrapper qualifierWrap = new PropertyWrapper(q);
            createIndexes.getBody().addToStatements(TinkerGenerationUtil.graphDbAccess + ".createKeyIndex(" + TinkerGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() + ".getUmlgLabelConverter().convert(\"" + qualifierWrap.getQualifiedName() + "\"), " + TinkerGenerationUtil.edgePathName.getLast() + ".class)");
            indexCreator.addToImports(TinkerGenerationUtil.edgePathName);
        }
        indexCreator.addToImports(TinkerGenerationUtil.UmlgLabelConverterFactoryPathName);
        //Create index for the application root
        createIndexes.getBody().addToStatements(TinkerGenerationUtil.graphDbAccess + ".createKeyIndex(" + TinkerGenerationUtil.UmlgGraph.getLast() + ".ROOT_VERTEX, " + TinkerGenerationUtil.vertexPathName.getLast() + ".class)");

        indexCreator.addToImports(TinkerGenerationUtil.UmlgGraph);
        indexCreator.addToImports(TinkerGenerationUtil.vertexPathName);
        indexCreator.addToImports(TinkerGenerationUtil.graphDbPathName);
    }

    @Override
    public void visitAfter(Model element) {
    }
}
