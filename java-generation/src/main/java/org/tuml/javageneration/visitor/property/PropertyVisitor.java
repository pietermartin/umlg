package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.util.TumlPropertyOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class PropertyVisitor extends BaseVisitor implements Visitor<Property> {

	@Override
	public void visitBefore(Property p) {
		if (TumlPropertyOperations.isQualifier(p)) {
			OJAnnotatedClass owner = findOJClass(p);
			if (owner == null) {
				System.out.println();
			}
			if (p.isNavigable() && !p.isDerived() && p.isMultivalued()) {
				// visitManyProperty()
			}
		}
	}

	@Override
	public void visitAfter(Property element) {

	}

	// private void visitManyProperty(OJAnnotatedClass ojClass,
	// NakedStructuralFeatureMap map) {
	// for (OJConstructor constructor : ojClass.getConstructors()) {
	// if (constructor.getParameters().isEmpty()) {
	// // Skip default constructor
	// continue;
	// }
	// OJPathName collectionPathName =
	// TinkerGenerationUtil.getDefaultTinkerCollection(map);
	//
	// ojClass.addToImports(collectionPathName);
	//
	// OJSimpleStatement ojSimpleStatement = new
	// OJSimpleStatement(map.fieldname() + " = ");
	// ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() +
	// TinkerGenerationUtil.getDefaultTinkerCollectionInitalisation(map,
	// collectionPathName).getExpression());
	// constructor.getBody().addToStatements(ojSimpleStatement);
	// }
	// }

}
