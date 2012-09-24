package org.tuml.javageneration.audit.visitor.property;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJClass;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJForStatement;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJParameter;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJSimpleStatement;
import org.opaeum.java.metamodel.OJTryStatement;
import org.opaeum.java.metamodel.OJVisibilityKind;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class AuditPropertyVisitor extends BaseVisitor implements Visitor<Property> {

	public static final String POLYMORPHIC_GETTER_FOR_TO_ONE_IF = "buildPolymorphicGetterForToOneIf";
	public static final String POLYMORPHIC_GETTER_FOR_TO_ONE_TRY = "buildPolymorphicGetterForToOneTry";
	public static final String POLYMORPHIC_GETTER_FOR_TO_MANY_FOR = "buildPolymorphicGetterForToManyFor";
	public static final String POLYMORPHIC_GETTER_FOR_TO_MANY_TRY = "buildPolymorphicGetterForToManyTry";

	public static final String IF_OLD_VALUE_NULL = "ifParamNull";
	public static final String IF_PARAM_NOT_NULL = "ifParamNotNull";
	public final static String ATRTIBUTE_STRATEGY_TINKER = "TINKER";

	public AuditPropertyVisitor(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Property element) {
	}

	@Override
	public void visitAfter(Property p) {
		PropertyWrapper pWrap = new PropertyWrapper(p);
		if (!pWrap.isDerived() && !pWrap.isQualifier() && !TumlClassOperations.isOnInterface(pWrap) && !(pWrap.getOwningType() instanceof Enumeration)) {

			OJAnnotatedClass auditClass = findAuditOJClass(p);
			OJAnnotatedOperation getter = new OJAnnotatedOperation(pWrap.getter());
			auditClass.addToOperations(getter);

			if (pWrap.isMany()) {
				getter.setReturnType(pWrap.javaAuditTypePath());
				buildGetAllAuditsForMany(pWrap, auditClass);
				buildGetAuditsForThisMany(pWrap, auditClass);
				buildPolymorphicGetterForMany(pWrap, getter);
			} else {
				if (!pWrap.isPrimitive() /* && !pWrap.isEnumeration() */) {
					buildGetAuditForOne(pWrap, auditClass);
					buildGetAuditForThisOne(pWrap, auditClass);
					getter.setReturnType(pWrap.javaAuditBaseTypePath());
					buildPolymorphicGetterForToOne(pWrap, getter);
				} else {
					buildGetterForToOnePrimitiveAndEnumeration(pWrap, getter);
					getter.setReturnType(pWrap.javaAuditBaseTypePath());
				}
			}
		}
	}

	private void buildGetterForToOnePrimitiveAndEnumeration(PropertyWrapper pWrap, OJAnnotatedOperation getter) {
		getter.getBody().addToStatements("return (" + pWrap.javaBaseTypePath() + ") this.vertex.getProperty(" + pWrap.getTumlRuntimePropertyEnum() + ".getLabel())");
		getter.getOwner().addToImports(TumlClassOperations.getPathName(pWrap.getOwningType()).append(TumlClassOperations.propertyEnumName(pWrap.getOwningType())));
	}

	private void buildGetAllAuditsForMany(PropertyWrapper pWrap, OJAnnotatedClass owner) {
		buildGetAllAuditsForMany(pWrap, owner, false);
	}

	private void buildGetAllAuditsForMany(PropertyWrapper pWrap, OJAnnotatedClass owner, boolean embedded) {
		OJOperation getAllAuditsForMany = new OJOperation();
		getAllAuditsForMany.setVisibility(OJVisibilityKind.PRIVATE);
		getAllAuditsForMany.setName("getAllAuditsFor" + StringUtils.capitalize(pWrap.getName()));
		getAllAuditsForMany.addParam("previous", owner.getPathName());

		OJPathName param = new OJPathName("java.util.Map");
		param.addToElementTypes(new OJPathName("String"));
		param.addToElementTypes(pWrap.javaAuditBaseTypePath());

		getAllAuditsForMany.addParam("audits", param);
		getAllAuditsForMany.addParam("removedAudits", param);

		getAllAuditsForMany.addParam("transactionNo", new OJPathName("java.lang.Long"));
		OJIfStatement ifStatement = new OJIfStatement("previous != null");
		ifStatement.addToThenPart("audits.putAll(previous.getAuditsFor" + StringUtils.capitalize(pWrap.getName()) + "(audits, removedAudits, transactionNo))");
		ifStatement.addToThenPart("getAllAuditsFor" + StringUtils.capitalize(pWrap.getName()) + "(previous.getPreviousAuditEntry(), audits, removedAudits, transactionNo)");
		getAllAuditsForMany.getBody().addToStatements(ifStatement);
		owner.addToOperations(getAllAuditsForMany);
	}

	private void buildGetAuditsForThisMany(PropertyWrapper pWrap, OJClass owner) {
		buildGetAuditsForThisMany(pWrap, owner, pWrap.isPrimitive() || pWrap.isEnumeration());
	}

	private void buildGetAuditsForThisMany(PropertyWrapper pWrap, OJClass owner, boolean embedded) {
		owner.addToImports(TinkerGenerationUtil.tumlFormatter);
		owner.addToImports(new OJPathName("java.util.Date"));
		OJOperation getAuditsForThisMany = new OJOperation();
		getAuditsForThisMany.setVisibility(OJVisibilityKind.PRIVATE);
		owner.addToOperations(getAuditsForThisMany);
		getAuditsForThisMany.setName("getAuditsFor" + StringUtils.capitalize(pWrap.getName()));
		OJPathName param = new OJPathName("java.util.Map");
		param.addToElementTypes(new OJPathName("String"));
		param.addToElementTypes(pWrap.javaAuditBaseTypePath());
		getAuditsForThisMany.addParam("audits", param);
		getAuditsForThisMany.addParam("removedAudits", param);
		getAuditsForThisMany.addParam("transactionNo", new OJPathName("java.lang.Long"));
		getAuditsForThisMany.setReturnType(param);
		OJField result = new OJField();
		result.setType(param);
		result.setName("result");
		result.setInitExp("new HashMap<String, " + pWrap.javaAuditBaseTypePath().getLast() + ">()");
		OJPathName defaultValue = pWrap.javaImplTypePath();
		owner.addToImports(defaultValue);
		getAuditsForThisMany.getBody().addToLocals(result);

		String asociationName = TinkerGenerationUtil.getEdgeName(pWrap);
		if (pWrap.isControllingSide()) {
			getAuditsForThisMany.getBody().addToStatements("Iterable<Edge> iter = this.vertex.getEdges(Direction.OUT, \"" + asociationName + "\")");
		} else {
			getAuditsForThisMany.getBody().addToStatements("Iterable<Edge> iter = this.vertex.getEdges(Direction.IN, \"" + asociationName + "\")");
		}
		OJForStatement forStatement = new OJForStatement("edge", TinkerGenerationUtil.edgePathName, "iter");
		forStatement.setName(POLYMORPHIC_GETTER_FOR_TO_MANY_FOR);
		OJTryStatement ojTryStatement = new OJTryStatement();
		OJIfStatement ifNotDeleted = new OJIfStatement("edge.getProperty(\"deletedOn\") == null");
		forStatement.getBody().addToStatements(ojTryStatement);
		ojTryStatement.setName(POLYMORPHIC_GETTER_FOR_TO_MANY_TRY);
		ojTryStatement.getTryPart().addToStatements(ifNotDeleted);
		OJSimpleStatement classForName = new OJSimpleStatement();
		OJSimpleStatement constructMany = new OJSimpleStatement();
		if (pWrap.isControllingSide()) {
			if (!embedded) {
				classForName.setExpression("Class<?> c = Class.forName((String) edge.getProperty(\"inClass\"))");
				ifNotDeleted.addToThenPart(classForName);
				constructMany.setExpression(pWrap.javaAuditBaseTypePath().getLast() + " instance = (" + pWrap.javaAuditBaseTypePath().getLast()
						+ ")c.getConstructor(Vertex.class).newInstance(edge.getVertex(Direction.IN))");
			} else {
				constructMany.setExpression(pWrap.javaAuditBaseTypePath().getLast() + " instance = (" + pWrap.javaAuditBaseTypePath().getLast()
						+ ")edge.getVertex(Direction.IN).getProperty(\"value\")");
			}
			ifNotDeleted.addToThenPart(constructMany);
		} else {
			if (!embedded) {
				classForName.setExpression("Class<?> c = Class.forName((String) edge.getProperty(\"outClass\"))");
				ifNotDeleted.addToThenPart(classForName);
				constructMany.setExpression(pWrap.javaAuditBaseTypePath().getLast() + " instance = (" + pWrap.javaAuditBaseTypePath().getLast()
						+ ")c.getConstructor(Vertex.class).newInstance(edge.getVertex(Direction.OUT))");
			} else {
				constructMany.setExpression(pWrap.javaAuditBaseTypePath().getLast() + " instance = (" + pWrap.javaAuditBaseTypePath().getLast()
						+ ")edge.getVertex(Direction.OUT).getProperty(\"value\")");
			}
			ifNotDeleted.addToThenPart(constructMany);
		}
		OJIfStatement ifStatement;
		if (!embedded) {
			ifStatement = new OJIfStatement("!removedAudits.containsKey(instance.getOriginalUid()) && !audits.containsKey(instance.getOriginalUid())");
			ifStatement.addToThenPart(pWrap.javaAuditBaseTypePath().getLast() + " previous = (" + pWrap.javaAuditBaseTypePath().getLast()
					+ ")iterateToLatest(transactionNo, instance)");
		} else {
			ifStatement = new OJIfStatement("!removedAudits.containsKey(instance.toString()) && !audits.containsKey(instance.toString())");
		}
		ifNotDeleted.addToThenPart(ifStatement);
		if (!embedded) {
			ifStatement.addToThenPart("result.put(previous.getOriginalUid(), previous)");
		} else {
			ifStatement.addToThenPart("result.put(instance.toString(), instance)");
		}
		ifNotDeleted.addToElsePart(classForName);
		ifNotDeleted.addToElsePart(constructMany);
		if (!embedded) {
			ifNotDeleted.addToElsePart("removedAudits.put(instance.getOriginalUid(), instance)");
		} else {
			ifNotDeleted.addToElsePart("removedAudits.put(instance.toString(), instance)");
		}

		ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
		ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");
		getAuditsForThisMany.getBody().addToStatements(forStatement);
		getAuditsForThisMany.getBody().addToStatements("return result");
	}

	private void buildPolymorphicGetterForMany(PropertyWrapper pWrap, OJOperation getter) {
		getter.getOwner().addToImports(new OJPathName("java.util.Map"));
		getter.getOwner().addToImports(new OJPathName("java.util.HashMap"));
		OJField audits = new OJField();
		OJPathName var = new OJPathName("java.util.Map");
		var.addToElementTypes(new OJPathName("String"));
		var.addToElementTypes(pWrap.javaAuditBaseTypePath());
		audits.setInitExp("new HashMap<String, " + pWrap.javaAuditBaseTypePath() + ">()");
		audits.setName("allAudits");
		audits.setType(var);
		getter.getBody().addToLocals(audits);
		getter.getBody().addToStatements(
				"getAllAuditsFor" + StringUtils.capitalize(pWrap.getName()) + "(this, allAudits, new HashMap<String, " + pWrap.javaAuditBaseTypePath().getLast()
						+ ">(), (getNextAuditEntry()!=null?getNextAuditEntry().getTransactionNo():-1))");
		OJPathName javaTumlMemoryTypePath = pWrap.javaTumlMemoryTypePath().getCopy();
		if (!pWrap.isPrimitive() && !pWrap.isEnumeration()) {
			javaTumlMemoryTypePath.appendToGeneric("Audit");
		}
		getter.getBody().addToStatements("return new " + javaTumlMemoryTypePath.getLast() + "(allAudits.values())");
		getter.getOwner().addToImports(javaTumlMemoryTypePath);
	}

	private void buildGetAuditForOne(PropertyWrapper pWrap, OJAnnotatedClass owner) {
		OJOperation getAllAuditsForOne = new OJOperation();
		getAllAuditsForOne.setVisibility(OJVisibilityKind.PRIVATE);
		getAllAuditsForOne.setName("getAuditFor" + StringUtils.capitalize(pWrap.getName()));
		getAllAuditsForOne.addParam("previous", owner.getPathName());

		OJPathName param = new OJPathName("java.util.Map");
		param.addToElementTypes(new OJPathName("String"));
		param.addToElementTypes(pWrap.javaAuditBaseTypePath());
		getAllAuditsForOne.addParam("removedAudits", param);

		getAllAuditsForOne.addParam("transactionNo", new OJPathName("java.lang.Long"));
		getAllAuditsForOne.setReturnType(pWrap.javaAuditBaseTypePath());
		OJIfStatement ifStatement = new OJIfStatement("previous != null");
		ifStatement.addToThenPart(pWrap.javaAuditBaseTypePath().getLast() + " result = previous.getAuditFor" + StringUtils.capitalize(pWrap.getName())
				+ "(transactionNo, removedAudits)");
		OJIfStatement ifStatement2 = new OJIfStatement("result != null", "return result");
		ifStatement2.addToElsePart("return getAuditFor" + StringUtils.capitalize(pWrap.getName()) + "(previous.getPreviousAuditEntry(), removedAudits, transactionNo)");
		ifStatement.addToThenPart(ifStatement2);
		getAllAuditsForOne.getBody().addToStatements(ifStatement);
		getAllAuditsForOne.getBody().addToStatements("return null");
		owner.addToOperations(getAllAuditsForOne);
	}

	private void buildGetAuditForThisOne(PropertyWrapper pWrap, OJAnnotatedClass owner) {

		owner.addToImports(TinkerGenerationUtil.tumlFormatter);
		owner.addToImports(new OJPathName("java.util.Date"));
		owner.addToImports(new OJPathName("java.util.HashSet"));
		OJOperation getAuditsForThisOne = new OJOperation();
		getAuditsForThisOne.setVisibility(OJVisibilityKind.PRIVATE);
		owner.addToOperations(getAuditsForThisOne);
		getAuditsForThisOne.setName("getAuditFor" + StringUtils.capitalize(pWrap.getName()));
		getAuditsForThisOne.addParam("transactionNo", new OJPathName("java.lang.Long"));

		OJPathName param = new OJPathName("java.util.Map");
		param.addToElementTypes(new OJPathName("String"));
		param.addToElementTypes(pWrap.javaAuditBaseTypePath());
		getAuditsForThisOne.addParam("removedAudits", param);

		getAuditsForThisOne.setReturnType(pWrap.javaAuditBaseTypePath());
		OJPathName defaultValue = pWrap.javaImplTypePath();
		owner.addToImports(defaultValue);

		boolean isComposite = pWrap.isControllingSide();
		String associationName = TinkerGenerationUtil.getEdgeName(pWrap);
		if (isComposite) {
			getAuditsForThisOne.getBody().addToStatements("Iterable<Edge> iter = this.vertex.getEdges(Direction.OUT, \"" + associationName + "\")");
		} else {
			getAuditsForThisOne.getBody().addToStatements("Iterable<Edge> iter = this.vertex.getEdges(Direction.IN, \"" + associationName + "\")");
		}
		OJForStatement forEdges = new OJForStatement("edge", TinkerGenerationUtil.edgePathName, "iter");
		OJIfStatement ifNotDeleted = new OJIfStatement("edge.getProperty(\"deletedOn\")==null");

		OJTryStatement ojTryStatement = new OJTryStatement();
		ojTryStatement.setName(POLYMORPHIC_GETTER_FOR_TO_ONE_TRY);

		forEdges.getBody().addToStatements(ojTryStatement);
		ojTryStatement.getTryPart().addToStatements(ifNotDeleted);

		OJSimpleStatement forClass = new OJSimpleStatement();
		OJSimpleStatement constructClass = new OJSimpleStatement();
		if (isComposite) {
			if (!pWrap.isEnumeration()) {
				forClass.setExpression("Class<?> c = Class.forName((String) edge.getProperty(\"inClass\"))");
				constructClass.setExpression(pWrap.javaAuditBaseTypePath().getLast() + " instance = (" + pWrap.javaAuditBaseTypePath().getLast()
						+ ")c.getConstructor(Vertex.class).newInstance(edge.getVertex(Direction.IN))");
			} else {
				forClass.setExpression("Object value = edge.getVertex(Direction.IN).getProperty(\"value\")");
				constructClass.setExpression("Class<?> c = Class.forName((String) edge.getProperty(\"inClass\"))");
			}
		} else {
			if (!pWrap.isEnumeration()) {
				forClass.setExpression("Class<?> c = Class.forName((String) edge.getProperty(\"outClass\"))");
				constructClass.setExpression(pWrap.javaAuditBaseTypePath().getLast() + " instance = (" + pWrap.javaAuditBaseTypePath().getLast()
						+ ")c.getConstructor(Vertex.class).newInstance(edge.getVertex(Direction.OUT))");
			} else {
				forClass.setExpression("Object value = edge.getVertex(Direction.OUT).getProperty(\"value\")");
				constructClass.setExpression("Class<?> c = Class.forName((String) edge.getProperty(\"outClass\"))");
			}
		}
		ifNotDeleted.addToThenPart(forClass);
		ifNotDeleted.addToThenPart(constructClass);

		if (!pWrap.isEnumeration()) {
			OJIfStatement ifRemovedAuditContains = new OJIfStatement("!removedAudits.containsKey(instance.getOriginalUid())");
			ifRemovedAuditContains.addToThenPart("return (" + pWrap.javaAuditBaseTypePath().getLast() + ")iterateToLatest(transactionNo, instance)");
			ifNotDeleted.addToThenPart(ifRemovedAuditContains);
		} else {
			ifNotDeleted.addToThenPart(pWrap.javaAuditBaseTypePath().getLast() + " instance = (" + pWrap.javaAuditBaseTypePath().getLast()
					+ ")Enum.valueOf((Class<? extends Enum>) c, (String) value)");
			OJIfStatement ifRemovedAuditContains = new OJIfStatement("!removedAudits.containsKey(instance.name())");
			ifRemovedAuditContains.addToThenPart("return instance");
			ifNotDeleted.addToThenPart(ifRemovedAuditContains);
		}
		ifNotDeleted.addToElsePart(forClass);
		ifNotDeleted.addToElsePart(constructClass);
		if (!pWrap.isEnumeration()) {
			ifNotDeleted.addToElsePart("removedAudits.put(instance.getOriginalUid(), instance)");
		} else {
			ifNotDeleted.addToElsePart(pWrap.javaAuditBaseTypePath().getLast() + " instance = (" + pWrap.javaAuditBaseTypePath().getLast()
					+ ")Enum.valueOf((Class<? extends Enum>) c, (String) value)");
			ifNotDeleted.addToElsePart("removedAudits.put(instance.name(), instance)");
		}

		ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
		ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");

		getAuditsForThisOne.getBody().addToStatements(forEdges);
		getAuditsForThisOne.getBody().addToStatements("return null");

	}

	private void buildPolymorphicGetterForToOne(PropertyWrapper pWrap, OJOperation getter) {
		getter.getBody().addToStatements(
				"return getAuditFor" + StringUtils.capitalize(pWrap.getName()) + "(this, new HashMap<String, " + pWrap.javaAuditBaseTypePath().getLast()
						+ ">(), (getNextAuditEntry()!=null?getNextAuditEntry().getTransactionNo():-1))");
		getter.getOwner().addToImports(new OJPathName("java.util.HashMap"));
	}

}
