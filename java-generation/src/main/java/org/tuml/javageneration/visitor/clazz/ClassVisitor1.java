package org.tuml.javageneration.visitor.clazz;

import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.naming.Namer;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlBehavioredClassifierOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ClassVisitor1 extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Class> {

	public void visitBefore(org.eclipse.uml2.uml.Class clazz) {
		OJAnnotatedClass annotatedClass = new OJAnnotatedClass(Namer.name(clazz));
		OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()));
		annotatedClass.setMyPackage(ojPackage);
		// The super class will be set later after all classes have been created
		if (TumlBehavioredClassifierOperations.hasBehavior(clazz)) {
			annotatedClass.setSuperclass(TinkerGenerationUtil.BASE_BEHAVIORED_CLASSIFIER);
		} else {
			annotatedClass.setSuperclass(TinkerGenerationUtil.BASE_TINKER);
		}
		addToSource(annotatedClass);
	}

	public void visitAfter(org.eclipse.uml2.uml.Class clazz) {

	}

}
