package org.tuml.javageneration;

import java.util.ArrayList;
import java.util.List;

import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.audit.visitor.clazz.AuditClassCreator;
import org.tuml.javageneration.audit.visitor.clazz.AuditToJsonCreator;
import org.tuml.javageneration.audit.visitor.clazz.ClassAuditTransformation;
import org.tuml.javageneration.audit.visitor.interfaze.AuditInterfaceCreator;
import org.tuml.javageneration.audit.visitor.property.AuditPropertyVisitor;

public class DefaultAuditVisitors {

	public static List<Visitor<?>> getDefaultJavaVisitors() {
		List<Visitor<?>> result = new ArrayList<Visitor<?>>();
		result.addAll(DefaultVisitors.getDefaultJavaVisitors());
		result.add(new ClassAuditTransformation(Workspace.INSTANCE));
		result.add(new AuditClassCreator(Workspace.INSTANCE));
		result.add(new AuditInterfaceCreator(Workspace.INSTANCE));
		result.add(new AuditPropertyVisitor(Workspace.INSTANCE));
		result.add(new AuditToJsonCreator(Workspace.INSTANCE));
		return result;
		
//		if (this.audit) {
//			ModelVisitor.visitModel(this.model, new ClassAuditTransformation(this));
//			ModelVisitor.visitModel(this.model, new AuditClassCreator(this));
//			ModelVisitor.visitModel(this.model, new AuditInterfaceCreator(this));
//			ModelVisitor.visitModel(this.model, new AuditPropertyVisitor(this));
//		}
//		ModelVisitor.visitModel(this.model, new SingleServerResourceBuilder(this));
	}
	
}
