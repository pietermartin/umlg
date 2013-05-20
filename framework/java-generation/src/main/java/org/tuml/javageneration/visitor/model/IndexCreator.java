package org.tuml.javageneration.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.tuml.framework.ModelLoader;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJIfStatement;
import org.tuml.java.metamodel.OJPackage;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.visitor.BaseVisitor;

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
        OJPackage ojPackage = new OJPackage(TinkerGenerationUtil.TumlRootPackage.toJavaString());
        indexCreator.setMyPackage(ojPackage);
        indexCreator.addToImplementedInterfaces(TinkerGenerationUtil.TumlIndexManager);
        addToSource(indexCreator);

        OJAnnotatedOperation createIndexes = new OJAnnotatedOperation("createIndexes");
        indexCreator.addToOperations(createIndexes);

        List<Property> qualifiers = ModelLoader.INSTANCE.getAllQualifiers();
        for (Property q : qualifiers) {
            PropertyWrapper ownerWrap = new PropertyWrapper((Property) q.getOwner());

            OJField index = new OJField("index", TinkerGenerationUtil.tinkerIndexPathName);
            createIndexes.getBody().addToLocals(index);
            createIndexes.getBody().addToStatements("index  = " + TinkerGenerationUtil.graphDbAccess + ".getIndex(\"" + ownerWrap.getQualifiedName() + "\", " + TinkerGenerationUtil.edgePathName.getLast() + ".class)");
            OJIfStatement ifIndexNull = new OJIfStatement("index == null");
            ifIndexNull.addToThenPart(TinkerGenerationUtil.graphDbAccess + ".createIndex(\"" + ownerWrap.getQualifiedName() + "\", " + TinkerGenerationUtil.edgePathName.getLast() + ".class)");
            createIndexes.getBody().addToStatements(ifIndexNull);

            indexCreator.addToImports(TinkerGenerationUtil.tinkerIndexPathName);
            indexCreator.addToImports(TinkerGenerationUtil.edgePathName);
            indexCreator.addToImports(TinkerGenerationUtil.graphDbPathName);
        }

    }

    @Override
    public void visitAfter(Model element) {
    }
}
