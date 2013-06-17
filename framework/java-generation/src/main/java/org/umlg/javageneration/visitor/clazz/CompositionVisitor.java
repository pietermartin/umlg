package org.umlg.javageneration.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.VisitSubclasses;
import org.umlg.java.metamodel.OJConstructor;
import org.umlg.java.metamodel.OJForStatement;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class CompositionVisitor extends BaseVisitor implements Visitor<Class> {

	public CompositionVisitor(Workspace workspace) {
		super(workspace);
	}

	@Override
    @VisitSubclasses({Class.class, AssociationClass.class})
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
        PropertyWrapper pWrap = new PropertyWrapper(TumlClassOperations.getOtherEndToComposite(clazz));
        if (pWrap.isAssociationClass()) {
            constructor.addParam(StringUtils.uncapitalize(pWrap.getAssociationClass().getName()), pWrap.getAssociationClassPathName());
        }
		annotatedClass.addToConstructors(constructor);
        constructor.getBody().addToStatements("super(true)");

        if (!pWrap.isAssociationClass()) {
            constructor.getBody().addToStatements(pWrap.adder() + "(compositeOwner)");
        } else {
            constructor.getBody().addToStatements(pWrap.adder() + "(compositeOwner, " + StringUtils.uncapitalize(pWrap.getAssociationClass().getName()) + ")");
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
            delete.getBody().addToStatements(TinkerGenerationUtil.transactionThreadEntityVar.getLast() + ".remove(this)");
			delete.getBody().addToStatements(TinkerGenerationUtil.graphDbAccess + ".removeVertex(this.vertex)");
            annotatedClass.addToImports(TinkerGenerationUtil.transactionThreadEntityVar);
			annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName.getCopy());
		} else {
			delete.getBody().addToStatements("super.delete()");
		}
	}

}
