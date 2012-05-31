package org.tuml.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class CompositionVisitor extends BaseVisitor implements Visitor<Class> {

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		addInit(annotatedClass, clazz);
	}

	@Override
	public void visitAfter(Class clazz) {
		// TODO Auto-generated method stub
	}
	
	private void addInit(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation init = new OJAnnotatedOperation("init");
		init.addParam("compositeOwner", TumlClassOperations.getEndToComposite(clazz).getType())
		annotatedClass.addToOperations(init);
	}
	
}
