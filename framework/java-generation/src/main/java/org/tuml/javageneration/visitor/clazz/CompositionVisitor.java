package org.tuml.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.javageneration.visitor.property.PropertyWrapper;

public class CompositionVisitor extends BaseVisitor implements Visitor<Class> {

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		if (TumlClassOperations.hasCompositeOwner(clazz)) {
			addInit(annotatedClass, clazz);
			addGetOwningObject(annotatedClass, clazz);
			addConstructorWithOwnerAsParameter(annotatedClass, clazz);
		} else {
			addEdgeToRoot(annotatedClass, clazz);
		}
	}

	@Override
	public void visitAfter(Class clazz) {
		// TODO Auto-generated method stub
	}

	private void addInit(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation init = new OJAnnotatedOperation("init");
		init.setComment("This gets called on creation with the compositional owner. The composition owner does not itself need to be a composite node");
		TinkerGenerationUtil.addOverrideAnnotation(init);
		init.addParam("compositeOwner", TinkerGenerationUtil.TINKER_NODE.getCopy());
		Property otherEndToComposite = TumlClassOperations.getOtherEndToComposite(clazz);
		if (TumlClassOperations.hasCompositeOwner(clazz) && !otherEndToComposite.isDerived()) {
			PropertyWrapper propertyWrapper = new PropertyWrapper(otherEndToComposite);
			init.getBody().addToStatements("this." + propertyWrapper.adder() + "(("+propertyWrapper.javaBaseTypePath().getLast()+")compositeOwner)");
		}
		init.getBody().addToStatements("this.hasInitBeenCalled = true");
		init.getBody().addToStatements("initVariables()");
		annotatedClass.addToOperations(init);
	}


	private void addConstructorWithOwnerAsParameter(OJAnnotatedClass annotatedClass, Class clazz) {
		OJConstructor constructor = new OJConstructor();
		constructor.addParam("compositeOwner", TumlClassOperations.getOtherEndToCompositePathName(clazz));
		annotatedClass.addToConstructors(constructor);
		if (clazz.getGeneralizations().isEmpty()) {
			constructor.getBody().addToStatements("this.vertex = " + TinkerGenerationUtil.graphDbAccess + ".addVertex(\"dribble\")");
			constructor.getBody().addToStatements("initialiseProperties()");
			constructor.getBody().addToStatements("createComponents()");
			constructor.getBody().addToStatements("init(compositeOwner)");
			constructor.getBody().addToStatements("TransactionThreadEntityVar.setNewEntity(this)");
			constructor.getBody().addToStatements("defaultCreate()");
			annotatedClass.addToImports(TinkerGenerationUtil.transactionThreadEntityVar);
		} else {
			constructor.getBody().addToStatements("super(true)");
			constructor.getBody().addToStatements("init(compositeOwner)");
		}
	}

	private void addGetOwningObject(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation getOwningObject = new OJAnnotatedOperation("getOwningObject", TinkerGenerationUtil.TINKER_NODE.getCopy());
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
		constructor.getBody().addToStatements(TinkerGenerationUtil.edgePathName.getLast() + " edge = " + TinkerGenerationUtil.graphDbAccess + ".addEdge(null, " + TinkerGenerationUtil.graphDbAccess + ".getRoot(), this.vertex, \"root\")");
		constructor.getBody().addToStatements("edge.setProperty(\"inClass\", this.getClass().getName())");
		annotatedClass.addToImports(TinkerGenerationUtil.edgePathName.getCopy());
		annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName.getCopy());
	}

}
