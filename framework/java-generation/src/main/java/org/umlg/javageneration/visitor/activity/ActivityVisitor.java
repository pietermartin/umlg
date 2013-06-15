package org.umlg.javageneration.visitor.activity;

import org.eclipse.uml2.uml.Activity;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.visitor.BaseVisitor;

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
