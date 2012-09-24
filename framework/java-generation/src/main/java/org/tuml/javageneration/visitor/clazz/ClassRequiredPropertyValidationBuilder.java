package org.tuml.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ClassRequiredPropertyValidationBuilder extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Class>{

	public ClassRequiredPropertyValidationBuilder(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		OJAnnotatedOperation validateRequiredProperties = new OJAnnotatedOperation("validateRequiredProperties", new OJPathName("java.util.List").addToGenerics(TinkerGenerationUtil.TumlConstraintViolation));
		annotatedClass.addToOperations(validateRequiredProperties);
		OJField result = new OJField("result", new OJPathName("java.util.List").addToGenerics(TinkerGenerationUtil.TumlConstraintViolation));
		result.setInitExp("new ArrayList<" + TinkerGenerationUtil.TumlConstraintViolation.getLast() + ">()");
		annotatedClass.addToImports(new OJPathName("java.util.ArrayList"));
		validateRequiredProperties.getBody().addToLocals(result);
		for (Property p : TumlClassOperations.getAllOwnedProperties(clazz)) {
			PropertyWrapper pWrap = new PropertyWrapper(p);
			if (!pWrap.isDerived() &&  pWrap.getLower() > 0) {
				OJIfStatement ifNotNull = new OJIfStatement(pWrap.getter() + "() == null");
				ifNotNull.addToThenPart("result.add(new " + TinkerGenerationUtil.TumlConstraintViolation.getLast() + "(\"required\", \"" + pWrap.getQualifiedName() + "\", \"field is required\"))");
				validateRequiredProperties.getBody().addToStatements(ifNotNull);
			}
		}
		validateRequiredProperties.getBody().addToStatements("return result");
	}

	@Override
	public void visitAfter(Class element) {
		
	}

}
