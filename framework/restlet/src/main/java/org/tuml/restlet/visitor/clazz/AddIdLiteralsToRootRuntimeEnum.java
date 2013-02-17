package org.tuml.restlet.visitor.clazz;

import java.util.Collections;

import org.eclipse.uml2.uml.Model;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.annotation.OJEnum;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.validation.Validation;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.javageneration.visitor.clazz.RuntimePropertyImplementor;

public class AddIdLiteralsToRootRuntimeEnum extends BaseVisitor implements Visitor<Model> {

	public AddIdLiteralsToRootRuntimeEnum(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model model) {
		OJAnnotatedClass annotatedClass = this.workspace.findOJClass("org.tuml.root.Root");
		OJEnum ojEnum = annotatedClass.findEnum("RootRuntimePropertyEnum");
		addField(annotatedClass, ojEnum, "id");
	}

	@Override
	public void visitAfter(Model element) {
	}

	private void addField(OJAnnotatedClass annotatedClass, OJEnum ojEnum, String fieldName) {
		OJAnnotatedOperation fromLabel = ojEnum.findOperation("fromLabel", new OJPathName("String"));
		OJAnnotatedOperation fromQualifiedName = ojEnum.findOperation("fromQualifiedName", new OJPathName("String"));
		OJAnnotatedOperation fromInverseQualifiedName = ojEnum.findOperation("fromInverseQualifiedName", new OJPathName("String"));
		RuntimePropertyImplementor
				.addEnumLiteral(ojEnum, fromLabel, fromQualifiedName, fromInverseQualifiedName, fieldName, "not_applicable", "inverseO::not_applicable", true, true, null,
						Collections.<Validation> emptyList(), false, false, false, false, false, false, false, false, false, 1, 1, false, false, false, false, false,
						true, "");
	}

}
