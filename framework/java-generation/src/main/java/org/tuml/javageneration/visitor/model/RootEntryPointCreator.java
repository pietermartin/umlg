package org.tuml.javageneration.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.OJVisibilityKind;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.visitor.BaseVisitor;

public class RootEntryPointCreator extends BaseVisitor implements Visitor<Model> {

	public RootEntryPointCreator(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Model model) {
		OJAnnotatedClass root = new OJAnnotatedClass("Root");
		OJPackage ojPackage = new OJPackage("org.tuml.root");
		root.setMyPackage(ojPackage);
		addToSource(root);
		
		root.getDefaultConstructor().setVisibility(OJVisibilityKind.PRIVATE);
		root.getDefaultConstructor().getBody().addToStatements("v = GraphDb.getDb().getRoot()");
		root.addToImports(TinkerGenerationUtil.graphDbPathName);
		root.addToImports(TinkerGenerationUtil.vertexPathName);
		
		OJField INSTANCE = new OJField("INSTANCE", root.getPathName());
		INSTANCE.setStatic(true);
		INSTANCE.setInitExp("new Root()");
		root.addToFields(INSTANCE);
		
		OJField vertex = new OJField("v", TinkerGenerationUtil.vertexPathName);
		root.addToFields(vertex);
	}

	@Override
	public void visitAfter(Model model) {
	}

}
