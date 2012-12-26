package org.tuml.javageneration.visitor.activity;

import org.eclipse.uml2.uml.Activity;
import org.tuml.java.metamodel.OJPackage;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.Namer;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ActivityVisitor extends BaseVisitor implements Visitor<Activity> {

	public ActivityVisitor(Workspace workspace) {
		super(workspace);
	}

	public void visitBefore(Activity activity) {
		OJAnnotatedClass annotatedClass = new OJAnnotatedClass(Namer.name(activity));
		OJPackage ojPackage = new OJPackage(Namer.name(activity.getNearestPackage()));
		annotatedClass.setMyPackage(ojPackage);
		addToSource(annotatedClass);
	}

	public void visitAfter(Activity activity) {
		
	}

}
