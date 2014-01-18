package org.umlg.javageneration.audit.visitor.clazz;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.umlg.java.metamodel.OJBlock;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJForStatement;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJParameter;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJTryStatement;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.generated.OJVisibilityKindGEN;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

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
			addCreateAuditVertexWithAuditEdge(annotatedClass, clazz);
			addGetAudits(annotatedClass, clazz);
			addCopyShallowStateToAuditVertex(annotatedClass, clazz);
		}
		addAddPreviousOnDeletion(clazz, annotatedClass);
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
			annotatedClass.setSuperclass(UmlgGenerationUtil.BASE_TUML_AUDIT.getCopy());
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

	private void addCreateAuditVertexWithAuditEdge(OJAnnotatedClass ojClass, Class c) {
		OJAnnotatedOperation createAuditVertexWithAuditEdge = new OJAnnotatedOperation("createAuditVertexWithAuditEdge");
		createAuditVertexWithAuditEdge.setVisibility(OJVisibilityKind.PRIVATE);
		ojClass.addToImports(UmlgGenerationUtil.graphDbPathName);
		createAuditVertexWithAuditEdge.getBody().addToStatements("this.auditVertex = " + UmlgGenerationUtil.graphDbAccess + ".addVertex(\"dribble\")");
		createAuditVertexWithAuditEdge.getBody().addToStatements(
				UmlgGenerationUtil.transactionThreadVar.getLast() + ".putAuditVertexFalse(getClass().getName() + getUid(), this.auditVertex)");
		createAuditVertexWithAuditEdge.getBody().addToStatements(
				"this.auditVertex.setProperty(\"transactionNo\", " + UmlgGenerationUtil.graphDbAccess + ".getTransactionCount())");
		createAuditVertexWithAuditEdge.getBody().addToStatements(
				"Edge auditEdgeToOriginal = " + UmlgGenerationUtil.graphDbAccess + ".addEdge(null, this.vertex, this.auditVertex, \"audit\")");
		createAuditVertexWithAuditEdge.getBody().addToStatements(
				"auditEdgeToOriginal.setProperty(\"transactionNo\", " + UmlgGenerationUtil.graphDbAccess + ".getTransactionCount())");
		createAuditVertexWithAuditEdge.getBody().addToStatements("auditEdgeToOriginal.setProperty(\"outClass\", this.getClass().getName())");
		createAuditVertexWithAuditEdge.getBody().addToStatements("auditEdgeToOriginal.setProperty(\"inClass\", this.getClass().getName() + \"Audit\")");
		createAuditVertexWithAuditEdge.getBody().addToStatements("copyShallowStateToAudit(this, this)");
		ojClass.addToImports(UmlgGenerationUtil.transactionThreadVar.getCopy());
		ojClass.addToOperations(createAuditVertexWithAuditEdge);
	}

	private void addGetAudits(OJAnnotatedClass originalClass, Class c) {
		OJAnnotatedOperation getAudits = new OJAnnotatedOperation("getAudits");
		OJField result = new OJField();
		result.setName("result");
		OJPathName resultPathName = new OJPathName("java.util.LinkedList");
		OJPathName auditPath = UmlgClassOperations.getAuditPathName(c);
		resultPathName.addToElementTypes(auditPath);
		getAudits.setReturnType(resultPathName);
		result.setType(resultPathName);
		result.setInitExp("new LinkedList<" + auditPath.getLast() + ">()");
		getAudits.getBody().addToLocals(result);

		OJForStatement forStatement = new OJForStatement("edge", UmlgGenerationUtil.edgePathName, "this.vertex.getEdges(" + UmlgGenerationUtil.tinkerDirection.getLast()
				+ ".OUT, \"audit\")");
		OJTryStatement ojTryStatement = new OJTryStatement();
		ojTryStatement.getTryPart().addToStatements("Class<?> c = Class.forName((String) edge.getProperty(\"inClass\"))");
		ojTryStatement.getTryPart().addToStatements(
				"result.add((" + auditPath.getLast() + ") c.getConstructor(Vertex.class).newInstance(edge.getVertex(" + UmlgGenerationUtil.tinkerDirection.getLast() + ".IN)))");
		ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("Exception")));
		ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");

		forStatement.getBody().addToStatements(ojTryStatement);
		getAudits.getBody().addToStatements(forStatement);

		StringBuilder sb = new StringBuilder();
		sb.append("Collections.sort(result,new Comparator<TinkerAuditNode>() {\n");
		sb.append("    @Override \n");
		sb.append("    public int compare(TinkerAuditNode o1, TinkerAuditNode o2) {\n");
		sb.append("        return (o1.getTransactionNo() < o2.getTransactionNo() ? -1 : (o1.getTransactionNo() == o2.getTransactionNo() ? 0 : 1));\n");
		sb.append("    };\n");
		sb.append("})");
		getAudits.getBody().addToStatements(sb.toString());
		originalClass.addToImports(new OJPathName("java.util.Collections"));
		originalClass.addToImports(new OJPathName("java.util.Comparator"));
		originalClass.addToImports(UmlgGenerationUtil.tinkerAuditNodePathName);
		originalClass.addToImports(UmlgGenerationUtil.tinkerDirection);

		getAudits.getBody().addToStatements("return result");
		originalClass.addToOperations(getAudits);
	}

	private void addCopyShallowStateToAuditVertex(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation oper = new OJAnnotatedOperation("copyShallowStateToAudit");
		oper.setVisibility(OJVisibilityKindGEN.PUBLIC);
		oper.addParam("from", annotatedClass.getPathName());
		oper.addParam("to", annotatedClass.getPathName());
		addCopyStatements(clazz, annotatedClass, oper.getBody(), false, true);
		addGetOriginalUid(oper);
		oper.getBody().addToStatements("to.auditVertex.setProperty(\"change\", change.toArray(new String[]{}))");
		annotatedClass.addToOperations(oper);
	}

	private void addCopyStatements(Class clazz, OJAnnotatedClass annotatedClass, OJBlock body, boolean b, boolean c) {
		OJField change = new OJField("change", new OJPathName("java.util.List").addToGenerics("String"));
		annotatedClass.addToImports(new OJPathName("java.lang.ArrayList"));
		change.setInitExp("new ArrayList<String>()");
		annotatedClass.addToImports("java.util.ArrayList");
		body.addToLocals(change);
		for (Property p : UmlgClassOperations.getOnePrimitiveOrEnumProperties(clazz)) {
			PropertyWrapper pWrap = new PropertyWrapper(p);
			OJIfStatement ifNotNull = new OJIfStatement("from." + pWrap.getter() + "() != null");
			ifNotNull.addToThenPart("to." + pWrap.setter() + "(from." + pWrap.getter() + "())");
			ifNotNull.addToThenPart("change.add(\"" + pWrap.getName() + "\")");
			body.addToStatements(ifNotNull);
		}
	}

	private void addGetOriginalUid(OJAnnotatedOperation oper) {
		oper.getBody().addToStatements("to.auditVertex.setProperty(\"" + UmlgGenerationUtil.ORIGINAL_UID + "\" , getUid())");
	}

	private void addAddPreviousOnDeletion(Class clazz, OJAnnotatedClass annotatedClass) {
		if (clazz.getGenerals().isEmpty()) {
			OJAnnotatedOperation delete = annotatedClass.findOperation("delete");
			delete.getBody().addToStatements(delete.getBody().getStatements().size() - 1, "getAudits().getLast().createEdgeToPreviousAuditInternal()");
		}
	}

}
