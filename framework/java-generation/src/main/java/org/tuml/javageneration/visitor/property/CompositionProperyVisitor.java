package org.tuml.javageneration.visitor.property;

import java.util.Arrays;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.util.TumlPropertyOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class CompositionProperyVisitor extends BaseVisitor implements Visitor<Property> {

	@Override
	public void visitBefore(Property p) {
		if (!p.isDerived() && p.getType() instanceof org.eclipse.uml2.uml.Class && TumlPropertyOperations.isOne(p) && p.isComposite() && p.getLower() == 1) {
			PropertyWrapper pWrap = new PropertyWrapper(p);
			OJAnnotatedClass owner = findOJClass(p);
			OJAnnotatedOperation createComponents = owner.findOperation("createComponents");
			OJAnnotatedOperation init = (OJAnnotatedOperation) owner.findOperation("init", Arrays.asList(TumlClassOperations.getPathName(p.getOtherEnd().getType())));
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
