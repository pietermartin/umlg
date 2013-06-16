package org.umlg.restlet.visitor.clazz;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.VisitSubclasses;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.restlet.util.TumlRestletGenerationUtil;

public class TumlRestletNodeBuilder extends BaseServerResourceBuilder implements Visitor<Class> {

	public TumlRestletNodeBuilder(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
    @VisitSubclasses({Class.class, AssociationClass.class})
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		if (clazz.getGenerals().isEmpty()) {
			addImplementsTumlRestletNode(annotatedClass);
		}
		if (!clazz.isAbstract()) {
			addGetUmlName(annotatedClass, clazz);
		}
	}

	private void addGetUmlName(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation getUmlName = new OJAnnotatedOperation("getUmlName");
		TinkerGenerationUtil.addOverrideAnnotation(getUmlName);
		getUmlName.setReturnType(new OJPathName("String"));
		getUmlName.setVisibility(OJVisibilityKind.PUBLIC);
		getUmlName.getBody().addToStatements("return \"" + clazz.getName() + "\"");
		annotatedClass.addToOperations(getUmlName);
	}

	@Override
	public void visitAfter(Class clazz) {
	}

	private void addImplementsTumlRestletNode(OJAnnotatedClass annotatedClass) {
		annotatedClass.addToImports(TumlRestletGenerationUtil.TumlRestletNode);
		annotatedClass.addToImplementedInterfaces(TumlRestletGenerationUtil.TumlRestletNode);
	}

}
