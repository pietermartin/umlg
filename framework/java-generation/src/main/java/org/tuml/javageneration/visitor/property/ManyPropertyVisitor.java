package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.tuml.java.metamodel.OJForStatement;
import org.tuml.java.metamodel.OJIfStatement;
import org.tuml.java.metamodel.OJVisibilityKind;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedInterface;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ManyPropertyVisitor extends BaseVisitor implements Visitor<Property> {

	public ManyPropertyVisitor(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		if (propertyWrapper.isMany() && !propertyWrapper.isDerived() && !propertyWrapper.isQualifier()) {
			OJAnnotatedClass owner = findOJClass(p);
			buildGetter(owner, propertyWrapper);
			buildManyAdder(owner, propertyWrapper);
			buildSetter(owner, propertyWrapper);
		}
	}

	@Override
	public void visitAfter(Property element) {

	}

	public static void buildGetter(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation getter = new OJAnnotatedOperation(propertyWrapper.getter(), propertyWrapper.javaTumlTypePath());
		getter.getBody().addToStatements("return this." + propertyWrapper.fieldname());
		owner.addToOperations(getter);
	}

	public static void buildManyAdder(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation adder = new OJAnnotatedOperation(propertyWrapper.adder());
		adder.addParam(propertyWrapper.fieldname(), propertyWrapper.javaTypePath());

		if (!(owner instanceof OJAnnotatedInterface)) {
			if (!propertyWrapper.hasQualifiers()) {
				OJIfStatement ifNotNull = new OJIfStatement("!" + propertyWrapper.fieldname() + ".isEmpty()");
				ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".addAll(" + propertyWrapper.fieldname() + ")");
				adder.getBody().addToStatements(ifNotNull);
			} else {
				String elementName = propertyWrapper.fieldname().substring(0, 1);
				OJForStatement forAll = new OJForStatement(elementName, propertyWrapper.javaBaseTypePath(), propertyWrapper.fieldname());
				forAll.getBody().addToStatements("this." + propertyWrapper.adder() + "(" + elementName + ")");
				adder.getBody().addToStatements(forAll);
			}
		}
		owner.addToOperations(adder);

		OJAnnotatedOperation singleAdder = new OJAnnotatedOperation(propertyWrapper.adder());
		singleAdder.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
		if (!(owner instanceof OJAnnotatedInterface)) {

			PropertyWrapper otherEnd = new PropertyWrapper(propertyWrapper.getOtherEnd());
			if (/*For bags the one side can have many edges to the same element*/propertyWrapper.isUnique() && propertyWrapper.hasOtherEnd() && !propertyWrapper.isEnumeration() && otherEnd.isOne()) {
				OJIfStatement ifNotNull2 = new OJIfStatement(propertyWrapper.fieldname() + " != null"); 
				ifNotNull2.addToThenPart(propertyWrapper.fieldname() + "." + otherEnd.clearer() + "()");
				ifNotNull2.addToThenPart(propertyWrapper.fieldname() + ".initialiseProperty(" + TumlClassOperations.propertyEnumName(otherEnd.getOwningType()) + "."
						+ otherEnd.fieldname() + ", false)");
				ifNotNull2.addToThenPart(propertyWrapper.remover() + "(" + propertyWrapper.fieldname() + ")");
				owner.addToImports(TumlClassOperations.getPathName(otherEnd.getOwningType()).append(TumlClassOperations.propertyEnumName(otherEnd.getOwningType())));
				singleAdder.getBody().addToStatements(ifNotNull2);
			}
			
			OJIfStatement ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");
			ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ")");
			singleAdder.getBody().addToStatements(ifNotNull);
		}
		owner.addToOperations(singleAdder);

	}

	public static void buildSetter(OJAnnotatedClass owner, PropertyWrapper pWrap) {
		OJAnnotatedOperation setter = new OJAnnotatedOperation(pWrap.setter());
		if (pWrap.isReadOnly()) {
			setter.setVisibility(OJVisibilityKind.PROTECTED);
		}
		setter.addParam(pWrap.fieldname(), pWrap.javaTypePath());
		setter.getBody().addToStatements(pWrap.clearer() + "()");
        OJIfStatement ifNotNull = new OJIfStatement(pWrap.fieldname() + " != null");
        ifNotNull.addToThenPart(pWrap.adder() + "(" + pWrap.fieldname() + ")");
		setter.getBody().addToStatements(ifNotNull);
		owner.addToOperations(setter);
	}

}
