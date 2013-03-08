package org.tuml.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.tuml.java.metamodel.OJConstructor;
import org.tuml.java.metamodel.OJForStatement;
import org.tuml.java.metamodel.OJIfStatement;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class CompositionVisitor extends BaseVisitor implements Visitor<Class> {

	public CompositionVisitor(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		if (TumlClassOperations.hasCompositeOwner(clazz)) {
			addConstructorWithOwnerAsParameter(annotatedClass, clazz);
		} else {
			if (TumlClassOperations.getSpecializations(clazz).isEmpty()) {
                addImplementRootNodeInterface(annotatedClass);
				addEdgeToRoot(annotatedClass, clazz);
			}
            if (!clazz.isAbstract()) {
                implementRootNode(clazz, annotatedClass);
            }
		}
		addGetOwningObject(annotatedClass, clazz);
		addCompositeChildrenToDelete(annotatedClass, clazz);
	}

    @Override
    public void visitAfter(Class clazz) {
    }

    private void implementRootNode(Class clazz, OJAnnotatedClass annotatedClass) {
        OJAnnotatedOperation getEdgeToRootLabel = new OJAnnotatedOperation("getEdgeToRootLabel", new OJPathName("String"));
        getEdgeToRootLabel.getBody().addToStatements("return \"" + TinkerGenerationUtil.getEdgeToRootLabelStrategy(clazz) + "\"");
        annotatedClass.addToOperations(getEdgeToRootLabel);
    }

    private void addImplementRootNodeInterface(OJAnnotatedClass annotatedClass) {
        annotatedClass.addToImplementedInterfaces(TinkerGenerationUtil.TUML_ROOT_NODE);
        annotatedClass.addToImports(TinkerGenerationUtil.TUML_ROOT_NODE);
    }

	private void addConstructorWithOwnerAsParameter(OJAnnotatedClass annotatedClass, Class clazz) {
		OJConstructor constructor = new OJConstructor();
		constructor.addParam("compositeOwner", TumlClassOperations.getOtherEndToCompositePathName(clazz));
		annotatedClass.addToConstructors(constructor);
		if (clazz.getGeneralizations().isEmpty()) {
			constructor.getBody().addToStatements("this.vertex = " + TinkerGenerationUtil.graphDbAccess + ".addVertex(this.getClass().getName())");

            constructor.getBody().addToStatements("this.vertex.setProperty(\"className\", getClass().getName())");

            //Link up to the meta instance for allInstances
            constructor.getBody().addToStatements("addEdgeToMetaNode()");

            constructor.getBody().addToStatements("initialiseProperties()");
			constructor.getBody().addToStatements(ClassBuilder.INIT_VARIABLES + "()");

			PropertyWrapper pWrap = new PropertyWrapper(TumlClassOperations.getOtherEndToComposite(clazz));
			constructor.getBody().addToStatements(pWrap.adder() + "(compositeOwner)");
			constructor.getBody().addToStatements(TinkerGenerationUtil.transactionThreadEntityVar.getLast() + ".setNewEntity(this)");
			constructor.getBody().addToStatements("defaultCreate()");
			annotatedClass.addToImports(TinkerGenerationUtil.transactionThreadEntityVar);
		} else {
			constructor.getBody().addToStatements("super(true)");
			PropertyWrapper pWrap = new PropertyWrapper(TumlClassOperations.getOtherEndToComposite(clazz));
			constructor.getBody().addToStatements(pWrap.adder() + "(compositeOwner)");
		}
	}

	private void addGetOwningObject(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation getOwningObject = new OJAnnotatedOperation("getOwningObject", TinkerGenerationUtil.TUML_NODE.getCopy());
		TinkerGenerationUtil.addOverrideAnnotation(getOwningObject);
		if (TumlClassOperations.hasCompositeOwner(clazz)) {
			getOwningObject.getBody().addToStatements("return " + new PropertyWrapper(TumlClassOperations.getOtherEndToComposite(clazz)).getter() + "()");
		} else {
			getOwningObject.getBody().addToStatements("return null");
		}
		annotatedClass.addToOperations(getOwningObject);
	}

	private void addEdgeToRoot(OJAnnotatedClass annotatedClass, Class clazz) {
		OJConstructor constructor = annotatedClass.findConstructor(new OJPathName("java.lang.Boolean"));
		constructor.getBody().addToStatements(
				TinkerGenerationUtil.edgePathName.getLast() + " edge = " + TinkerGenerationUtil.graphDbAccess + ".addEdge(null, " + TinkerGenerationUtil.graphDbAccess
						+ ".getRoot(), this.vertex, getEdgeToRootLabel())");
		constructor.getBody().addToStatements("edge.setProperty(\"inClass\", this.getClass().getName())");
		annotatedClass.addToImports(TinkerGenerationUtil.edgePathName.getCopy());
		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName.getCopy());
	}

	private void addCompositeChildrenToDelete(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation delete = annotatedClass.findOperation("delete");
		for (Property p : TumlClassOperations.getChildPropertiesToDelete(clazz)) {
			PropertyWrapper pWrap = new PropertyWrapper(p);
			if (pWrap.isMany()) {
				OJForStatement forChildToDelete = new OJForStatement("child", pWrap.javaBaseTypePath(), pWrap.getter() + "()");
				forChildToDelete.getBody().addToStatements("child.delete()");
				delete.getBody().addToStatements(forChildToDelete);
			} else if (!pWrap.isDataType()) {
				OJIfStatement ifChildToDeleteNotNull = new OJIfStatement(pWrap.getter() + "() != null");
				ifChildToDeleteNotNull.addToThenPart(pWrap.getter() + "().delete()");
				delete.getBody().addToStatements(ifChildToDeleteNotNull);
			}
		}

		for (Property p : TumlClassOperations.getPropertiesToClearOnDeletion(clazz)) {
			PropertyWrapper pWrap = new PropertyWrapper(p);
			delete.getBody().addToStatements("this." + pWrap.fieldname() + ".clear()");
		}
		if (clazz.getGenerals().isEmpty()) {
            delete.getBody().addToStatements(TinkerGenerationUtil.transactionThreadEntityVar.getLast() + ".remove(this.vertex.getId().toString())");
			delete.getBody().addToStatements(TinkerGenerationUtil.graphDbAccess + ".removeVertex(this.vertex)");
			annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName.getCopy());
		} else {
			delete.getBody().addToStatements("super.delete()");
		}
	}

}
