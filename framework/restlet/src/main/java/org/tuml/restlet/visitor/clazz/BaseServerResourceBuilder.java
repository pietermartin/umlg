package org.tuml.restlet.visitor.clazz;

import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.visitor.BaseVisitor;

public abstract class BaseServerResourceBuilder extends BaseVisitor {

	public BaseServerResourceBuilder(Workspace workspace) {
		super(workspace);
	}

	protected void addDefaultConstructor(OJAnnotatedClass annotatedClass) {
		annotatedClass.getDefaultConstructor().getBody().addToStatements("setNegotiated(false)");
	}

}
