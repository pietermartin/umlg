package org.tuml.javageneration.audit.visitor.clazz;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJVisibilityKind;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.Workspace;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ClassAuditTransformation extends BaseVisitor implements Visitor<Class> {

	public ClassAuditTransformation(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		setSuperClass(annotatedClass, clazz);
		if (clazz.getGeneralizations().isEmpty()) {
			addCreateAuditVertex(annotatedClass, clazz);
			addCreateAuditVertexWithAuditEdge(clazz, annotatedClass);
		}
	}

	@Override
	public void visitAfter(Class element) {
		// TODO Auto-generated method stub
	}

	private void setSuperClass(OJAnnotatedClass annotatedClass, Class clazz) {
		List<Classifier> generals = clazz.getGenerals();
		if (generals.size() > 1) {
			throw new IllegalStateException(String.format("Multiple inheritence is not supported! Class %s has more than on genereralization.", clazz.getName()));
		}
		if (generals.isEmpty()) {
			annotatedClass.setSuperclass(TinkerGenerationUtil.BASE_TUML_AUDIT.getCopy());
		}
	}

	private void addCreateAuditVertex(OJAnnotatedClass ojClass, Class clazz) {
		OJAnnotatedOperation createAuditVertex = new OJAnnotatedOperation("createAuditVertex");
		createAuditVertex.setVisibility(OJVisibilityKind.PUBLIC);
		createAuditVertex.addParam("createParentVertex", new OJPathName("boolean"));
		createAuditVertex.getBody().addToStatements("createAuditVertexWithAuditEdge()");
		// if (entity.getEndToComposite() != null || entity.getIsAbstract()) {
		// OJIfStatement ifCreateParentVertex = new
		// OJIfStatement("createParentVertex", "addEdgeToCompositeOwner()");
		// createAuditVertex.getBody().addToStatements(ifCreateParentVertex);
		// }
		ojClass.addToOperations(createAuditVertex);
	}

	private void addCreateAuditVertexWithAuditEdge(Class c, OJAnnotatedClass ojClass) {
		OJAnnotatedOperation createAuditVertexWithAuditEdge = new OJAnnotatedOperation("createAuditVertexWithAuditEdge");
		createAuditVertexWithAuditEdge.setVisibility(OJVisibilityKind.PRIVATE);
		ojClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		createAuditVertexWithAuditEdge.getBody().addToStatements("this.auditVertex = " + TinkerGenerationUtil.graphDbAccess + ".addVertex(\"dribble\")");
		createAuditVertexWithAuditEdge.getBody().addToStatements("TransactionThreadVar.putAuditVertexFalse(getClass().getName() + getUid(), this.auditVertex)");
		createAuditVertexWithAuditEdge.getBody().addToStatements(
				"this.auditVertex.setProperty(\"transactionNo\", " + TinkerGenerationUtil.graphDbAccess + ".getTransactionCount())");
		createAuditVertexWithAuditEdge.getBody().addToStatements(
				"Edge auditEdgeToOriginal = " + TinkerGenerationUtil.graphDbAccess + ".addEdge(null, this.vertex, this.auditVertex, \"audit\")");
		createAuditVertexWithAuditEdge.getBody().addToStatements(
				"auditEdgeToOriginal.setProperty(\"transactionNo\", " + TinkerGenerationUtil.graphDbAccess + ".getTransactionCount())");
		createAuditVertexWithAuditEdge.getBody().addToStatements("auditEdgeToOriginal.setProperty(\"outClass\", " + TinkerGenerationUtil.TINKER_GET_CLASSNAME + ")");
		createAuditVertexWithAuditEdge.getBody().addToStatements("auditEdgeToOriginal.setProperty(\"inClass\", " + TinkerGenerationUtil.TINKER_GET_CLASSNAME + " + \"Audit\")");
		createAuditVertexWithAuditEdge.getBody().addToStatements("copyAuditShallowState(this, this)");
		ojClass.addToOperations(createAuditVertexWithAuditEdge);
	}

}
