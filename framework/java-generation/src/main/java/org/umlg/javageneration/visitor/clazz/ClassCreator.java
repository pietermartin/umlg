package org.umlg.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgBehavioredClassifierOperations;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class ClassCreator extends BaseVisitor implements Visitor<Class> {

	public ClassCreator(Workspace workspace) {
		super(workspace);
	}

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
	public void visitBefore(org.eclipse.uml2.uml.Class clazz) {
		OJAnnotatedClass annotatedClass = new OJAnnotatedClass(UmlgClassOperations.className(clazz));
		OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()));
		annotatedClass.setMyPackage(ojPackage);
		annotatedClass.setVisibility(UmlgClassOperations.getVisibility(clazz.getVisibility()));
		annotatedClass.setAbstract(clazz.isAbstract());
        annotatedClass.getDefaultConstructor();
		
		// The super class will be set later after all classes have been created
		if (UmlgBehavioredClassifierOperations.hasBehavior(clazz)) {
			annotatedClass.setSuperclass(UmlgGenerationUtil.BASE_BEHAVIORED_CLASSIFIER);
		} else {
			annotatedClass.setSuperclass(UmlgGenerationUtil.BaseUmlgCompositionNode);
		}
		addToSource(annotatedClass);
	}

	public void visitAfter(org.eclipse.uml2.uml.Class clazz) {

	}

}
