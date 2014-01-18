package org.umlg.javageneration.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJWhileStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class RootEntryPointBuilder extends BaseVisitor implements Visitor<Class> {

	public RootEntryPointBuilder(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		if (!UmlgClassOperations.hasCompositeOwner(clazz) && !clazz.isAbstract()) {
			OJAnnotatedClass root = this.workspace.findOJClass(UmlgGenerationUtil.UmlgRootPackage.toJavaString() + "." + StringUtils.capitalize(ModelLoader.INSTANCE.getModel().getName()));
			addGetterToAppRootForRootEntity(clazz, root);
		}
	}

	private void addGetterToAppRootForRootEntity(Class clazz, OJAnnotatedClass root) {
		OJAnnotatedOperation getter = new OJAnnotatedOperation("get" + UmlgClassOperations.className(clazz),
				UmlgGenerationUtil.umlgSequence.getCopy().addToGenerics(UmlgClassOperations.getPathName(clazz)));
		root.addToOperations(getter);
		OJField result = new OJField("result", UmlgGenerationUtil.umlgSequence.getCopy().addToGenerics(UmlgClassOperations.getPathName(clazz)));
		result.setInitExp("new " + UmlgGenerationUtil.umlgMemorySequence.getCopy().getLast() + "<" + UmlgClassOperations.getPathName(clazz).getLast() + ">()");
		root.addToImports(UmlgGenerationUtil.umlgMemorySequence);
		root.addToImports(new OJPathName("java.util.ArrayList"));
		getter.getBody().addToLocals(result);
		OJField iter = new OJField("iter", new OJPathName("java.util.Iterator").addToGenerics(UmlgGenerationUtil.edgePathName));
		iter.setInitExp("getRootVertex().getEdges(Direction.OUT, "+ UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() +".getUmlgLabelConverter().convert(\"root" + UmlgClassOperations.getQualifiedName(clazz) + "\")).iterator()");
		getter.getBody().addToLocals(iter);
		OJWhileStatement ojWhileStatement = new OJWhileStatement();
		ojWhileStatement.setCondition("iter.hasNext()");
		ojWhileStatement.getBody().addToStatements(UmlgGenerationUtil.edgePathName.getLast() + " edge = iter.next()");
		ojWhileStatement.getBody().addToStatements("result.add(new " + UmlgClassOperations.className(clazz) + "(edge.getVertex(Direction.IN)));");
		getter.getBody().addToStatements(ojWhileStatement);
		getter.getBody().addToStatements("return result");
		root.addToImports(UmlgGenerationUtil.tinkerDirection);
        root.addToImports(UmlgGenerationUtil.UmlgLabelConverterFactoryPathName);
	}

	@Override
	public void visitAfter(Class clazz) {
	}

}
