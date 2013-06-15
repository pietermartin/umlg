package org.umlg.javageneration.visitor.clazz;

import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlBehavioredClassifierOperations;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class ClassCreator extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Class> {

	public ClassCreator(Workspace workspace) {
		super(workspace);
	}

	public void visitBefore(org.eclipse.uml2.uml.Class clazz) {
		OJAnnotatedClass annotatedClass = new OJAnnotatedClass(TumlClassOperations.className(clazz));
		OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()));
		annotatedClass.setMyPackage(ojPackage);
		annotatedClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));
		annotatedClass.setAbstract(clazz.isAbstract());
        annotatedClass.getDefaultConstructor();
		
		// The super class will be set later after all classes have been created
		if (TumlBehavioredClassifierOperations.hasBehavior(clazz)) {
			annotatedClass.setSuperclass(TinkerGenerationUtil.BASE_BEHAVIORED_CLASSIFIER);
		} else {
			annotatedClass.setSuperclass(TinkerGenerationUtil.BASE_TUML_COMPOSITION_NODE);
		}
		addToSource(annotatedClass);
	}

	public void visitAfter(org.eclipse.uml2.uml.Class clazz) {

	}

}
