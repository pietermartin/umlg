package org.tuml.javageneration.audit.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.Workspace;
import org.tuml.javageneration.visitor.BaseVisitor;

public class AuditPropertyVisitor extends BaseVisitor implements Visitor<Property> {

	public AuditPropertyVisitor(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Property element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitAfter(Property element) {
		// TODO Auto-generated method stub
		
	}

}
