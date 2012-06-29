package org.tuml.javageneration.visitor.property;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJForStatement;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ManyPropertyVisitor extends BaseVisitor implements Visitor<Property> {

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
		owner.addToOperations(adder);

		OJAnnotatedOperation singleAdder = new OJAnnotatedOperation(propertyWrapper.adder());
		singleAdder.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
		OJIfStatement ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");
		if (!propertyWrapper.hasQualifiers()) {
			ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ")");
		} else {
			ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ", " + propertyWrapper.getQualifiedGetterName() + "("
					+ propertyWrapper.fieldname() + "))");
		}
		singleAdder.getBody().addToStatements(ifNotNull);
		owner.addToOperations(singleAdder);

	}

	public static void buildSetter(OJAnnotatedClass owner, PropertyWrapper pWrap) {
		OJAnnotatedOperation setter = new OJAnnotatedOperation(pWrap.setter());
		setter.addParam(pWrap.fieldname(), pWrap.javaTypePath());
		setter.getBody().addToStatements(pWrap.clearer() + "()");
		setter.getBody().addToStatements(pWrap.adder() + "(" + pWrap.fieldname() + ")");
		owner.addToOperations(setter);
	}

}
