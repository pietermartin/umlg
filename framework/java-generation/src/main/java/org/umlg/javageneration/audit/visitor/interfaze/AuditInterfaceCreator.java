package org.umlg.javageneration.audit.visitor.interfaze;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class AuditInterfaceCreator extends BaseVisitor implements Visitor<Interface> {

	private OJAnnotatedInterface auditClass;
	
	public AuditInterfaceCreator(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Interface clazz) {
		this.auditClass = new OJAnnotatedInterface(UmlgClassOperations.className(clazz) + "Audit");
		OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()));
		this.auditClass.setMyPackage(ojPackage);
		this.auditClass.setVisibility(UmlgClassOperations.getVisibility(clazz.getVisibility()));
		this.auditClass.setAbstract(clazz.isAbstract());
		if (!clazz.getGenerals().isEmpty()) {
			Classifier superClassifier = clazz.getGenerals().get(0);
			OJAnnotatedClass superClass = findOJClass(superClassifier);
			OJPathName superTypePathName = superClass.getPathName();
			String className = superTypePathName.getLast();
			superTypePathName.replaceTail(className + "Audit");
			this.auditClass.addToSuperInterfaces(superTypePathName);
		}
		this.auditClass.addToSuperInterfaces(UmlgGenerationUtil.tinkerAuditNodePathName);
		addToSource(this.auditClass);
	}

	@Override
	public void visitAfter(Interface clazz) {
	}

}
