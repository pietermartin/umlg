package org.tuml.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.OJWhileStatement;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class RootEntryPointBuilder extends BaseVisitor implements Visitor<Class> {

    public final static String ROOT_PATHNAME = "org.tuml.root.Root";
	public RootEntryPointBuilder(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		if (!TumlClassOperations.hasCompositeOwner(clazz) && !clazz.isAbstract()) {
			OJAnnotatedClass root = this.workspace.findOJClass(ROOT_PATHNAME);
			addGetterToAppRootForRootEntity(clazz, root);
		}
	}

	private void addGetterToAppRootForRootEntity(Class clazz, OJAnnotatedClass root) {
		OJAnnotatedOperation getter = new OJAnnotatedOperation("get" + TumlClassOperations.className(clazz),
				TinkerGenerationUtil.tinkerSequence.getCopy().addToGenerics(TumlClassOperations.getPathName(clazz)));
		root.addToOperations(getter);
		OJField result = new OJField("result", TinkerGenerationUtil.tinkerSequence.getCopy().addToGenerics(TumlClassOperations.getPathName(clazz)));
		result.setInitExp("new " + TinkerGenerationUtil.tumlMemorySequence.getCopy().getLast() + "<" + TumlClassOperations.getPathName(clazz).getLast() + ">()");
		root.addToImports(TinkerGenerationUtil.tumlMemorySequence);
		root.addToImports(new OJPathName("java.util.ArrayList"));
		getter.getBody().addToLocals(result);
		OJField iter = new OJField("iter", new OJPathName("java.util.Iterator").addToGenerics(TinkerGenerationUtil.edgePathName));
		iter.setInitExp("getRootVertex().getEdges(Direction.OUT, \"root" + TumlClassOperations.getQualifiedName(clazz) + "\").iterator()");
		getter.getBody().addToLocals(iter);
		OJWhileStatement ojWhileStatement = new OJWhileStatement();
		ojWhileStatement.setCondition("iter.hasNext()");
		ojWhileStatement.getBody().addToStatements("Edge edge = (Edge) iter.next()");
		ojWhileStatement.getBody().addToStatements("result.add(new " + TumlClassOperations.className(clazz) + "(edge.getVertex(Direction.IN)));");
		getter.getBody().addToStatements(ojWhileStatement);
		getter.getBody().addToStatements("return result");
		root.addToImports(TinkerGenerationUtil.tinkerDirection);
	}

	@Override
	public void visitAfter(Class clazz) {
	}

}
