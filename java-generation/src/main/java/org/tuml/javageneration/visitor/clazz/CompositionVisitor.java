package org.tuml.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJSimpleStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.util.TumlPropertyOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class CompositionVisitor extends BaseVisitor implements Visitor<Class> {

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		addCreateComponents(annotatedClass, clazz);
		addInit(annotatedClass, clazz);
		addInitVariables(annotatedClass, clazz);
		if (TumlClassOperations.hasCompositeOwner(clazz)) {
			addConstructorWithOwnerAsParameter(annotatedClass, clazz);
		}
	}

	@Override
	public void visitAfter(Class clazz) {
		// TODO Auto-generated method stub
	}

	private void addInit(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation init = new OJAnnotatedOperation("init");
		if (TumlClassOperations.getOtherEndToComposite(clazz) != null) {
			init.addParam("compositeOwner", TumlClassOperations.getOtherEndToCompositePathName(clazz));
		}
		annotatedClass.addToOperations(init);
		if (TumlClassOperations.hasCompositeOwner(clazz) && !TumlClassOperations.getOtherEndToComposite(clazz).isDerived()) {
			init.getBody().addToStatements("this." + TumlPropertyOperations.internalAdder(TumlClassOperations.getOtherEndToComposite(clazz)) + "(owner)");
		}
		init.getBody().addToStatements("this.hasInitBeenCalled = true");
		init.getBody().addToStatements("initVariables()");
	}

	private void addInitVariables(OJAnnotatedClass annotatedClass, Class clazz) {
		OJOperation initVariables = new OJAnnotatedOperation("initVariables");
		initVariables.setBody(annotatedClass.getDefaultConstructor().getBody());
		if (TumlClassOperations.hasSupertype(clazz)) {
			OJSimpleStatement simpleStatement = new OJSimpleStatement("super.initVariables()");
			if (initVariables.getBody().getStatements().isEmpty()) {
				initVariables.getBody().addToStatements(simpleStatement);
			} else {
				initVariables.getBody().getStatements().set(0, simpleStatement);
			}
		}
		annotatedClass.addToOperations(initVariables);
	}

	private void addCreateComponents(OJAnnotatedClass annotatedClass, Class clazz) {
		OJOperation createComponents = new OJAnnotatedOperation("createComponents");
		createComponents.setBody(new OJBlock());
		if (TumlClassOperations.hasSupertype(clazz)) {
			createComponents.getBody().addToStatements("super.createComponents()");
		}
		annotatedClass.addToOperations(createComponents);
	}

	private void addConstructorWithOwnerAsParameter(OJAnnotatedClass annotatedClass, Class clazz) {
		OJConstructor constructor = new OJConstructor();
		constructor.addParam("compositeOwner", TumlClassOperations.getOtherEndToCompositePathName(clazz));
		annotatedClass.addToConstructors(constructor);
		if (clazz.getGeneralizations().isEmpty()) {
			constructor.getBody().getStatements()
					.add(0, new OJSimpleStatement("this.vertex = " + TinkerGenerationUtil.graphDbAccess + ".addVertex(\"dribble\")"));
			constructor.getBody().getStatements().add(1, new OJSimpleStatement("createComponents()"));
			constructor.getBody().addToStatements("init(compositeOwner)");
			constructor.getBody().addToStatements("TransactionThreadEntityVar.setNewEntity(this)");
			constructor.getBody().addToStatements("defaultCreate()");
			annotatedClass.addToImports(TinkerGenerationUtil.transactionThreadEntityVar);
		} else {
			constructor.getBody().getStatements().add(0, new OJSimpleStatement("super(true)"));
			constructor.getBody().addToStatements("init(compositeOwner)");
		}
	}

}
