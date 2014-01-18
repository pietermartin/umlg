package org.umlg.javageneration.visitor.property;

import java.util.Arrays;

import org.eclipse.uml2.uml.Property;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class ComponentProperyVisitor extends BaseVisitor implements Visitor<Property> {

	public ComponentProperyVisitor(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper pWrap = new PropertyWrapper(p);
		if (pWrap.isComponent()) {
			OJAnnotatedClass owner = findOJClass(p);
			OJAnnotatedOperation createComponents = owner.findOperation("createComponents");
			OJAnnotatedOperation init = (OJAnnotatedOperation) owner.findOperation("init", Arrays.asList(UmlgClassOperations.getPathName(p.getOtherEnd().getType())));
			OJIfStatement ifNull = new OJIfStatement(pWrap.getter() + "() == null", pWrap.setter() + "(new " + pWrap.javaBaseTypePath().getLast() + "(true))");
			owner.addToImports(pWrap.javaBaseTypePath());
			createComponents.getBody().addToStatements(ifNull);
			if (p.getType() instanceof org.eclipse.uml2.uml.Class && init != null) {
				init.getBody().addToStatements(pWrap.getter() + "().init(this)");
			}
		}

	}

	@Override
	public void visitAfter(Property p) {

	}

}

