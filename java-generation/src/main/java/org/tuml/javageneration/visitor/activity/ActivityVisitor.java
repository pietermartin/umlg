package org.tuml.javageneration.visitor.activity;

import org.eclipse.uml2.uml.Activity;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.naming.Namer;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ActivityVisitor extends BaseVisitor implements Visitor<Activity> {

	public void visitBefore(Activity activity) {
		OJAnnotatedClass annotatedClass = new OJAnnotatedClass(Namer.name(activity));
		OJPackage ojPackage = new OJPackage(Namer.name(activity.getNearestPackage()));
		annotatedClass.setMyPackage(ojPackage);
		addToSource(annotatedClass);
	}

	public void visitAfter(Activity activity) {
		
	}

}
