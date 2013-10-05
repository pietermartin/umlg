package org.umlg.javageneration.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJWhileStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class RootEntryPointBuilder extends BaseVisitor implements Visitor<Class> {

    public final static String ROOT_PATHNAME = "org.umlg.root.Root";
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
		iter.setInitExp("getRootVertex().getEdges(Direction.OUT, "+TinkerGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() +".getUmlgLabelConverter().convert(\"root" + TumlClassOperations.getQualifiedName(clazz) + "\")).iterator()");
		getter.getBody().addToLocals(iter);
		OJWhileStatement ojWhileStatement = new OJWhileStatement();
		ojWhileStatement.setCondition("iter.hasNext()");
		ojWhileStatement.getBody().addToStatements(TinkerGenerationUtil.edgePathName.getLast() + " edge = iter.next()");
		ojWhileStatement.getBody().addToStatements("result.add(new " + TumlClassOperations.className(clazz) + "(edge.getVertex(Direction.IN)));");
		getter.getBody().addToStatements(ojWhileStatement);
		getter.getBody().addToStatements("return result");
		root.addToImports(TinkerGenerationUtil.tinkerDirection);
        root.addToImports(TinkerGenerationUtil.UmlgLabelConverterFactoryPathName);
	}

	@Override
	public void visitAfter(Class clazz) {
	}

}
