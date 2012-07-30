package org.tuml.javageneration.audit.visitor.clazz;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJForStatement;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.OJParameter;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJTryStatement;
import org.opaeum.java.metamodel.OJVisibilityKind;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedField;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJAnnotationValue;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.Workspace;
import org.tuml.javageneration.naming.Namer;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class AuditClassCreator extends BaseVisitor implements Visitor<Class> {

	private OJAnnotatedClass auditClass;
	
	public AuditClassCreator(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Class clazz) {
		this.auditClass = new OJAnnotatedClass(TumlClassOperations.className(clazz) + "Audit");
		OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()));
		this.auditClass.setMyPackage(ojPackage);
		this.auditClass.setVisibility(TumlClassOperations.getVisibility(clazz.getVisibility()));
		this.auditClass.setAbstract(clazz.isAbstract());
		if (!clazz.getGenerals().isEmpty()) {
			Classifier superClassifier = clazz.getGenerals().get(0);
			OJAnnotatedClass superClass = findOJClass(superClassifier);
			OJPathName superTypePathName = superClass.getPathName();
			String className = superTypePathName.getLast();
			superTypePathName.replaceTail(className + "Audit");
			this.auditClass.setSuperclass(superClass.getPathName());
		}
		this.auditClass.addToImplementedInterfaces(TinkerGenerationUtil.tinkerAuditNodePathName);
		addToSource(this.auditClass);
	}

	@Override
	public void visitAfter(Class clazz) {
		if (clazz.getGeneralizations().isEmpty()) {
			addVertexFieldWithSetter();
			addGetPreviousNextAuditVertexInternal(true);
			addGetPreviousNextAuditVertexInternal(false);
			addCreateEdgeToPreviousNextAuditInternal(true);
			addCreateEdgeToPreviousNextAuditInternal(false);
			addIteratorToNext();
			addGetOriginal(clazz);
			implementGetTransactionNo();
			addGetUid();
			addGetOriginalUid();
			implementGetSetId();
			addGetObjectVersion();
			if (TumlClassOperations.getAttribute(clazz, "name") == null) {
				addGetName(clazz);
			}
		}
		if (!clazz.isAbstract()) {
			implementGetPreviousAuditEntry();
			implementGetNextAuditEntry();
			implementGetNextAuditEntries();
		} else {
			implementAbstractGetPreviousAuditEntry();
			implementAbstractGetNextAuditEntries();
		}
		addContructorWithVertex(clazz);
//		implementTinkerNode(this.auditClass);
		implementTinkerAuditNode();
		implementIsTinkerRoot(TumlClassOperations.hasCompositeOwner(clazz));
//		implementEmptyClearCache();
		this.auditClass.addToImports(TinkerGenerationUtil.tinkerDirection);
	}

	private void addVertexFieldWithSetter() {
		OJPathName underlyingVertexPath = TinkerGenerationUtil.vertexPathName;
		OJField vertexField = new OJAnnotatedField("vertex", underlyingVertexPath);
		vertexField.setVisibility(OJVisibilityKind.PROTECTED);
		this.auditClass.addToFields(vertexField);
		OJOperation setter = new OJAnnotatedOperation("getVertex", underlyingVertexPath);
		setter.getBody().addToStatements("return this.vertex");
		this.auditClass.addToOperations(setter);
	}

	private void addGetPreviousNextAuditVertexInternal(boolean previous) {
		OJOperation getPreviousAuditVertexInternal = new OJOperation();
		getPreviousAuditVertexInternal.setVisibility(OJVisibilityKind.PRIVATE);
		if (previous) {
			getPreviousAuditVertexInternal.setName("getPreviousAuditVertexInternal");
		} else {
			getPreviousAuditVertexInternal.setName("getNextAuditVertexInternal");
		}
		getPreviousAuditVertexInternal.setReturnType(TinkerGenerationUtil.vertexPathName);
		
		OJIfStatement ifHasOriginal = new OJIfStatement("getOriginal()!=null");
		getPreviousAuditVertexInternal.getBody().addToStatements(ifHasOriginal);
		ifHasOriginal.addToElsePart("return null");
		ifHasOriginal.addToThenPart("TreeMap<Long, Edge> auditTransactions = new TreeMap<Long, Edge>()");
		OJForStatement forEdge = new OJForStatement("edge", TinkerGenerationUtil.edgePathName, "getOriginal().getVertex().getEdges(Direction.OUT, \"audit\")");
		forEdge.getBody().addToStatements("Long transaction = (Long) edge.getProperty(\"transactionNo\")");
		forEdge.getBody().addToStatements("auditTransactions.put(transaction, edge)");
		ifHasOriginal.addToThenPart(forEdge);

		OJIfStatement ifNoAudits = new OJIfStatement("!auditTransactions.isEmpty()");
		OJForStatement forAudits;
		if (previous) {
			ifNoAudits.addToThenPart("NavigableSet<Long> descendingKeySet = auditTransactions.descendingKeySet()");
			forAudits = new OJForStatement("auditTransactionNo", new OJPathName("Long"), "descendingKeySet");
		} else {
			ifNoAudits.addToThenPart("NavigableSet<Long> ascendingKeySet = auditTransactions.navigableKeySet()");
			forAudits = new OJForStatement("auditTransactionNo", new OJPathName("Long"), "ascendingKeySet");
		}
		OJIfStatement ifTransactionSmaller = new OJIfStatement();
		if (previous) {
			ifTransactionSmaller.setCondition("auditTransactionNo < getTransactionNo()");
		} else {
			ifTransactionSmaller.setCondition("auditTransactionNo > getTransactionNo()");

		}
		ifTransactionSmaller.addToThenPart("return auditTransactions.get(auditTransactionNo).getVertex(Direction.IN)");
		forAudits.getBody().addToStatements(ifTransactionSmaller);
		ifNoAudits.addToThenPart(forAudits);
		ifNoAudits.addToThenPart("return null");
		ifNoAudits.addToElsePart("return null");
		ifHasOriginal.addToThenPart(ifNoAudits);

		this.auditClass.addToImports(new OJPathName("java.util.TreeMap"));
		this.auditClass.addToImports(new OJPathName("java.util.NavigableSet"));
		this.auditClass.addToOperations(getPreviousAuditVertexInternal);
	}

	private void addCreateEdgeToPreviousNextAuditInternal(boolean previous) {
		OJOperation createEdgeToPreviousAuditInternal = new OJOperation();
		//THis protected scope is more like friendly scope, not for inheritence
		createEdgeToPreviousAuditInternal.setVisibility(OJVisibilityKind.PROTECTED);
		if (previous) {
			createEdgeToPreviousAuditInternal.setName("createEdgeToPreviousAuditInternal");
		} else {
			createEdgeToPreviousAuditInternal.setName("createEdgeToNextAuditInternal");
		}
		createEdgeToPreviousAuditInternal.setReturnType(TinkerGenerationUtil.edgePathName);
		createEdgeToPreviousAuditInternal.getBody().addToStatements(
				"Vertex " + (previous ? "previousAuditVertex  = getPreviousAuditVertexInternal()" : "nextAuditVertex = getNextAuditVertexInternal()"));
		OJIfStatement ifPreviousAuditVertex = new OJIfStatement();

		if (previous) {
			ifPreviousAuditVertex.setCondition("previousAuditVertex != null");
		} else {
			ifPreviousAuditVertex.setCondition("nextAuditVertex != null");
		}

		OJField auditParentEdge = new OJField();
		auditParentEdge.setName("auditParentEdge");
		auditParentEdge.setType(TinkerGenerationUtil.edgePathName);
		ifPreviousAuditVertex.getThenPart().addToLocals(auditParentEdge);

		OJIfStatement isTransactionNotActive = new OJIfStatement("!GraphDb.getDb().isTransactionActive()");
		ifPreviousAuditVertex.getThenPart().addToStatements(isTransactionNotActive);
		OJTryStatement ojTryStatement = new OJTryStatement();
		ojTryStatement.setCatchPart(null);
		isTransactionNotActive.addToThenPart(ojTryStatement);
		ojTryStatement.getTryPart().addToStatements("GraphDb.getDb().startTransaction()");
		ojTryStatement.getFinallyPart().addToStatements("GraphDb.getDb().stopTransaction(Conclusion.SUCCESS)");
		this.auditClass.addToImports(TinkerGenerationUtil.tinkerConclusionPathName);
		if (previous) {
			ojTryStatement.getTryPart().addToStatements(
					"auditParentEdge = " + TinkerGenerationUtil.graphDbAccess + ".addEdge(null, this.vertex, previousAuditVertex, \"previous\")");
		} else {
			ojTryStatement.getTryPart().addToStatements("auditParentEdge = " + TinkerGenerationUtil.graphDbAccess + ".addEdge(null, nextAuditVertex, this.vertex, \"previous\")");
		}
		ojTryStatement.getTryPart().addToStatements("auditParentEdge.setProperty(\"outClass\", this.getClass().getName() + \"Audit\")");
		ojTryStatement.getTryPart().addToStatements("auditParentEdge.setProperty(\"inClass\", this.getClass().getName() + \"Audit\")");

		if (previous) {
			isTransactionNotActive.addToElsePart("auditParentEdge = " + TinkerGenerationUtil.graphDbAccess + ".addEdge(null, this.vertex, previousAuditVertex, \"previous\")");
		} else {
			isTransactionNotActive.addToElsePart("auditParentEdge = " + TinkerGenerationUtil.graphDbAccess + ".addEdge(null, nextAuditVertex, this.vertex, \"previous\")");
		}
		isTransactionNotActive.addToElsePart("auditParentEdge.setProperty(\"outClass\", this.getClass().getName() + \"Audit\")");
		isTransactionNotActive.addToElsePart("auditParentEdge.setProperty(\"inClass\", this.getClass().getName() + \"Audit\")");

		ifPreviousAuditVertex.addToThenPart("return auditParentEdge");
		ifPreviousAuditVertex.addToElsePart("return null");
		createEdgeToPreviousAuditInternal.getBody().addToStatements(ifPreviousAuditVertex);
		this.auditClass.addToImports(TinkerGenerationUtil.graphDbPathName);
		this.auditClass.addToOperations(createEdgeToPreviousAuditInternal);
	}

	private void addIteratorToNext() {
		OJOperation iterateToLatest = new OJOperation();
		iterateToLatest.setName("iterateToLatest");
		iterateToLatest.setReturnType(TinkerGenerationUtil.tinkerAuditNodePathName);
		iterateToLatest.addParam("transactionNo", new OJPathName("java.lang.Long"));
		iterateToLatest.addParam("previous", TinkerGenerationUtil.tinkerAuditNodePathName);
		iterateToLatest.getBody().addToStatements("TinkerAuditNode nextAudit = previous.getNextAuditEntry()");
		OJIfStatement ifNextAuditNotNull = new OJIfStatement("nextAudit!=null");
		OJIfStatement ifTransactionNoSmaller = new OJIfStatement("((transactionNo == -1L) || (nextAudit.getTransactionNo() < transactionNo))",
				"return iterateToLatest(transactionNo, nextAudit)");
		ifTransactionNoSmaller.addToElsePart("return previous");
		ifNextAuditNotNull.addToThenPart(ifTransactionNoSmaller);
		ifNextAuditNotNull.addToElsePart("return previous");
		iterateToLatest.getBody().addToStatements(ifNextAuditNotNull);
		this.auditClass.addToOperations(iterateToLatest);
	}

	private void addGetOriginal(Class c) {
		OJOperation getOriginal = new OJOperation();
		getOriginal.setName("getOriginal");
		getOriginal.setReturnType(TumlClassOperations.getPathName(c));
		getOriginal.getBody().addToStatements("Iterable<Edge> iter = this.vertex.getEdges(Direction.IN)");
		OJForStatement forStatement = new OJForStatement("edge", TinkerGenerationUtil.edgePathName, "iter");
		OJIfStatement ifStatement = new OJIfStatement("edge.getLabel().startsWith(\"audit\")");
		forStatement.getBody().addToStatements(ifStatement);
		getOriginal.getBody().addToStatements(forStatement);
		OJTryStatement ojTryStatement = new OJTryStatement();
		OJBlock tryBlock = new OJBlock();
		tryBlock.addToStatements("Class<?> c = Class.forName((String) edge.getProperty(\"outClass\"))");
		tryBlock.addToStatements("return (" + TumlClassOperations.getPathName(c).getLast() + ")c.getConstructor(Vertex.class).newInstance(edge.getVertex(Direction.OUT))");
		ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
		OJBlock catchBlock = new OJBlock();
		catchBlock.addToStatements("throw new RuntimeException(e)");
		ojTryStatement.setCatchPart(catchBlock);
		ojTryStatement.setTryPart(tryBlock);
		ifStatement.addToThenPart(ojTryStatement);
		getOriginal.getBody().addToStatements("return null");
		this.auditClass.addToOperations(getOriginal);
	}

	private void implementGetTransactionNo() {
		OJOperation ojOperation = new OJOperation();
		ojOperation.setName("getTransactionNo");
		ojOperation.setReturnType(new OJPathName("java.lang.Long"));
		ojOperation.getBody().addToStatements("return (Long)this.vertex.getProperty(\"transactionNo\")");
		this.auditClass.addToOperations(ojOperation);
	}

	private void addGetUid() {
		OJOperation getUid = new OJOperation();
		getUid.setName("getUid");
		getUid.setReturnType(new OJPathName("String"));
		getUid.getBody().addToStatements("String uid = (String) this.vertex.getProperty(\"uid\")");
		OJIfStatement ifStatement = new OJIfStatement("uid==null || uid.trim().length()==0");
		ifStatement.addToThenPart("uid=UUID.randomUUID().toString()");
		ifStatement.addToThenPart("this.vertex.setProperty(\"uid\", uid)");
		getUid.getBody().addToStatements(ifStatement);
		getUid.getBody().addToStatements("return uid");
		this.auditClass.addToOperations(getUid);
		this.auditClass.addToImports(new OJPathName("java.util.UUID"));
	}

	private void addGetOriginalUid() {
		OJOperation getOriginalUid = new OJOperation();
		getOriginalUid.setName("getOriginalUid");
		getOriginalUid.setReturnType(new OJPathName("String"));
		getOriginalUid.getBody().addToStatements("return (String) this.vertex.getProperty(\"" + TinkerGenerationUtil.ORIGINAL_UID + "\")");
		this.auditClass.addToOperations(getOriginalUid);
	}

	private void implementGetSetId() {
		OJAnnotatedOperation getId = new OJAnnotatedOperation("getId");
		getId.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
		getId.setReturnType(new OJPathName("java.lang.Long"));
		getId.getBody().addToStatements("return TinkerIdUtilFactory.getIdUtil().getId(this.vertex)");
		this.auditClass.addToImports(TinkerGenerationUtil.tinkerIdUtilFactoryPathName);
		this.auditClass.addToOperations(getId);

		OJAnnotatedOperation setId = new OJAnnotatedOperation("setId");
		setId.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
		setId.addParam("id", new OJPathName("java.lang.Long"));
		setId.getBody().addToStatements("TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id)");
		this.auditClass.addToOperations(setId);
	}

	private void addGetObjectVersion() {
		OJAnnotatedOperation getObjectVersion = new OJAnnotatedOperation("getObjectVersion");
		getObjectVersion.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
		getObjectVersion.setReturnType(new OJPathName("int"));
		getObjectVersion.getBody().addToStatements("return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex)");
		this.auditClass.addToImports(TinkerGenerationUtil.tinkerIdUtilFactoryPathName);
		this.auditClass.addToOperations(getObjectVersion);
	}

	private void addGetName(Class entity) {
		OJAnnotatedOperation getName = new OJAnnotatedOperation("getName");
		getName.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
		getName.setName("getName");
		getName.setReturnType(new OJPathName("String"));
		getName.setBody(new OJBlock());
		getName.getBody().addToStatements("return \"" + entity.getName() + "[\"+getId()+\"]\"");
		this.auditClass.addToOperations(getName);
	}

	private void implementGetPreviousAuditEntry() {
		this.auditClass.addToImports(TinkerGenerationUtil.edgePathName);
		OJOperation getPreviousAuditEntry = new OJOperation();
		getPreviousAuditEntry.setName("getPreviousAuditEntry");
		getPreviousAuditEntry.setReturnType(this.auditClass.getPathName());
		getPreviousAuditEntry.getBody().addToStatements("Iterable<Edge> iter = this.vertex.getEdges(Direction.OUT, \"previous\")");
		OJIfStatement ifIter = new OJIfStatement("!iter.iterator().hasNext()");
		ifIter.addToThenPart("Edge previousEdge = createEdgeToPreviousAuditInternal()");
		OJIfStatement ifHasPrevious = new OJIfStatement("previousEdge != null");
		ifHasPrevious.addToThenPart("return new " + this.auditClass.getName() + "(previousEdge.getVertex(Direction.IN))");
		ifHasPrevious.addToElsePart("return null");
		ifIter.addToThenPart(ifHasPrevious);

		ifIter.addToElsePart("return new " + this.auditClass.getName() + "(this.vertex.getEdges(Direction.OUT, \"previous\").iterator().next().getVertex(Direction.IN))");
		getPreviousAuditEntry.getBody().addToStatements(ifIter);
		this.auditClass.addToOperations(getPreviousAuditEntry);
	}

	private void implementGetNextAuditEntry() {
		this.auditClass.addToImports(TinkerGenerationUtil.edgePathName);
		OJOperation getPreviousAuditEntry = new OJOperation();
		getPreviousAuditEntry.setName("getNextAuditEntry");
		getPreviousAuditEntry.setReturnType(this.auditClass.getPathName());
		getPreviousAuditEntry.getBody().addToStatements("Iterable<Edge> iter = this.vertex.getEdges(Direction.IN, \"previous\")");
		OJIfStatement ifIter = new OJIfStatement("!iter.iterator().hasNext()");
		ifIter.addToThenPart("Edge nextEdge = createEdgeToNextAuditInternal()");
		OJIfStatement ifHasPrevious = new OJIfStatement("nextEdge != null");
		ifHasPrevious.addToThenPart("return new " + this.auditClass.getName() + "(nextEdge.getVertex(Direction.OUT))");
		ifHasPrevious.addToElsePart("return null");
		ifIter.addToThenPart(ifHasPrevious);

		ifIter.addToElsePart("return new " + this.auditClass.getName() + "(this.vertex.getEdges(Direction.IN, \"previous\").iterator().next().getVertex(Direction.OUT))");
		getPreviousAuditEntry.getBody().addToStatements(ifIter);
		this.auditClass.addToOperations(getPreviousAuditEntry);
	}

	private void implementGetNextAuditEntries() {
		this.auditClass.addToImports(TinkerGenerationUtil.edgePathName);
		OJOperation getNextAuditEntries = new OJOperation();
		getNextAuditEntries.setName("getNextAuditEntries");
		OJPathName result = new OJPathName("java.util.List");
		OJPathName resultElementType = new OJPathName(this.auditClass.getPathName().toJavaString());
		resultElementType.replaceTail("? extends " + resultElementType.getLast());
		result.addToElementTypes(resultElementType);
		getNextAuditEntries.setReturnType(result);
		getNextAuditEntries.getBody().addToStatements("List<" + this.auditClass.getPathName().getLast() + "> result = new ArrayList<" + this.auditClass.getPathName().getLast() + ">()");
		getNextAuditEntries.getBody().addToStatements("getNextAuditEntriesInternal(result)");
		getNextAuditEntries.getBody().addToStatements("return result");
		this.auditClass.addToOperations(getNextAuditEntries);
		this.auditClass.addToImports(new OJPathName("java.util.ArrayList"));
		this.auditClass.addToImports(new OJPathName("java.util.Iterator"));

		OJOperation getNextAuditEntriesInternal = new OJOperation();
		getNextAuditEntriesInternal.setName("getNextAuditEntriesInternal");
		getNextAuditEntriesInternal.setVisibility(OJVisibilityKind.PRIVATE);
		OJPathName resultInternalElementType = new OJPathName("java.util.List");
		resultInternalElementType.addToElementTypes(this.auditClass.getPathName());
		getNextAuditEntriesInternal.addParam("nextAudits", resultInternalElementType);
		getNextAuditEntriesInternal.getBody().addToStatements("Iterator<Edge> iter = this.vertex.getEdges(Direction.IN, \"previous\").iterator()");

		OJIfStatement ifStatement = new OJIfStatement("iter.hasNext()");
		ifStatement.addToThenPart(this.auditClass.getPathName().getLast() + " nextAudit = new " + this.auditClass.getPathName().getLast() + "(iter.next().getVertex(Direction.OUT))");
		ifStatement.addToThenPart("nextAudits.add(nextAudit)");
		ifStatement.addToThenPart("nextAudit.getNextAuditEntriesInternal(nextAudits)");
		getNextAuditEntriesInternal.getBody().addToStatements(ifStatement);
		this.auditClass.addToOperations(getNextAuditEntriesInternal);
	}

	private void implementAbstractGetPreviousAuditEntry() {
		this.auditClass.addToImports(TinkerGenerationUtil.edgePathName);
		OJOperation getPreviousAuditEntry = new OJOperation();
		getPreviousAuditEntry.setName("getPreviousAuditEntry");
		getPreviousAuditEntry.setReturnType(this.auditClass.getPathName());
		getPreviousAuditEntry.setAbstract(true);
		this.auditClass.addToOperations(getPreviousAuditEntry);
	}

	private void implementAbstractGetNextAuditEntries() {
		this.auditClass.addToImports(TinkerGenerationUtil.edgePathName);
		OJOperation getNextAuditEntries = new OJOperation();
		getNextAuditEntries.setName("getNextAuditEntries");
		OJPathName result = new OJPathName("java.util.List");
		OJPathName elementType = new OJPathName(this.auditClass.getPathName().toJavaString());
		elementType.replaceTail("? extends " + this.auditClass.getPathName().getLast());
		result.addToElementTypes(elementType);
		getNextAuditEntries.setReturnType(result);
		getNextAuditEntries.setAbstract(true);
		this.auditClass.addToOperations(getNextAuditEntries);
	}

	private void addContructorWithVertex(Class c) {
		OJConstructor constructor = new OJConstructor();
		constructor.addParam("vertex", TinkerGenerationUtil.vertexPathName);
		if (c.getGeneralizations().isEmpty()) {
			constructor.getBody().addToStatements("this.vertex=vertex");
		} else {
			constructor.getBody().addToStatements("super(vertex)");
		}
		this.auditClass.addToConstructors(constructor);
	}

//	private void implementTinkerAuditNode(OJAnnotatedInterface intf) {
//		intf.addToSuperInterfaces(TinkerGenerationUtil.tinkerAuditNodePathName);
//	}

	private void implementTinkerAuditNode() {
		this.auditClass.addToImplementedInterfaces(TinkerGenerationUtil.tinkerAuditNodePathName);
	}

	private void implementIsTinkerRoot(boolean b) {
		OJOperation isRoot = new OJOperation();
		isRoot.setName("isTinkerRoot");
		isRoot.setReturnType(new OJPathName("boolean"));
		isRoot.getBody().addToStatements("return " + b);
		this.auditClass.addToOperations(isRoot);
	}

//	private void implementEmptyClearCache() {
//		OJAnnotatedOperation clearCache = new OJAnnotatedOperation("clearCache");
//		TinkerGenerationUtil.addOverrideAnnotation(clearCache);
//		this.auditClass.addToOperations(clearCache);
//	}

}
