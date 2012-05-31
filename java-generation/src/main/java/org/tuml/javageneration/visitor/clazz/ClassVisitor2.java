package org.tuml.javageneration.visitor.clazz;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJSimpleStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJAnnotationValue;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.util.TumlPropertyOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ClassVisitor2 extends BaseVisitor implements Visitor<Class> {

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		setSuperClass(annotatedClass, clazz);
		
		implementCompositionNode(annotatedClass);
		implementIsRoot(annotatedClass, TumlClassOperations.getEndToComposite(clazz) == null);
		addPersistentConstructor(annotatedClass);
		addClearCache(annotatedClass, clazz);
		addContructorWithVertex(annotatedClass, clazz);
		
		if (clazz.getGeneralizations().isEmpty()) {
			persistUid(annotatedClass);
			addGetObjectVersion(annotatedClass);
			addGetSetId(annotatedClass);
			initialiseVertexInPersistentConstructor(annotatedClass, clazz);
		} else {
			addSuperWithPersistenceToDefaultConstructor(annotatedClass);
		}

	}

	@Override
	public void visitAfter(Class clazz) {
		// TODO Auto-generated method stub

	}

	private void setSuperClass(OJAnnotatedClass annotatedClass, Class clazz) {
		List<Classifier> generals = clazz.getGenerals();
		if (generals.size() > 1) {
			throw new IllegalStateException(String.format("Multiple inheritence is not supported! Class %s has more than on genereralization.", clazz.getName()));
		}
		if (!generals.isEmpty()) {
			Classifier superClassifier = generals.get(0);
			OJAnnotatedClass superClass = findOJClass(superClassifier);
			annotatedClass.setSuperclass(superClass.getPathName());
		}
	}

	protected void persistUid(OJAnnotatedClass ojClass) {
		OJAnnotatedOperation getUid = new OJAnnotatedOperation("getUid");
		getUid.setReturnType(new OJPathName("String"));
		getUid.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
		getUid.getBody().removeAllFromStatements();
		getUid.getBody().addToStatements("String uid = (String) this.vertex.getProperty(\"uid\")");
		OJIfStatement ifStatement = new OJIfStatement("uid==null || uid.trim().length()==0");
		ifStatement.setCondition("uid==null || uid.trim().length()==0");
		ifStatement.addToThenPart("uid=UUID.randomUUID().toString()");
		ifStatement.addToThenPart("this.vertex.setProperty(\"uid\", uid)");
		getUid.getBody().addToStatements(ifStatement);
		getUid.getBody().addToStatements("return uid");
	}

	protected void addGetObjectVersion(OJAnnotatedClass ojClass) {
		OJAnnotatedOperation getObjectVersion = new OJAnnotatedOperation("getObjectVersion");
		TinkerGenerationUtil.addOverrideAnnotation(getObjectVersion);
		getObjectVersion.setReturnType(new OJPathName("int"));
		getObjectVersion.getBody().addToStatements("return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex)");
		ojClass.addToOperations(getObjectVersion);
	}

	protected void addGetSetId(OJAnnotatedClass ojClass) {
		OJAnnotatedOperation getId = new OJAnnotatedOperation("getId");
		getId.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
		getId.setReturnType(new OJPathName("java.lang.Long"));
		getId.getBody().addToStatements("return TinkerIdUtilFactory.getIdUtil().getId(this.vertex)");
		ojClass.addToOperations(getId);

		OJAnnotatedOperation setId = new OJAnnotatedOperation("setId");
		setId.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
		setId.addParam("id", new OJPathName("java.lang.Long"));
		setId.getBody().addToStatements("TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id)");
		ojClass.addToImports(TinkerGenerationUtil.tinkerIdUtilFactoryPathName);
		ojClass.addToOperations(setId);
	}

	protected void initialiseVertexInPersistentConstructor(OJAnnotatedClass ojClass, Classifier c) {
		OJConstructor constructor = ojClass.findConstructor(new OJPathName("java.lang.Boolean"));
		constructor.getBody().addToStatements("this.vertex = " + TinkerGenerationUtil.graphDbAccess + ".addVertex(\"dribble\")");
		constructor.getBody().addToStatements("TransactionThreadEntityVar.setNewEntity(this)");
		constructor.getBody().addToStatements("defaultCreate()");
		ojClass.addToImports(TinkerGenerationUtil.transactionThreadEntityVar);
		ojClass.addToImports(TinkerGenerationUtil.graphDbPathName);
	}

	private void addSuperWithPersistenceToDefaultConstructor(OJAnnotatedClass ojClass) {
		OJConstructor constructor = ojClass.findConstructor(new OJPathName("java.lang.Boolean"));
		constructor.getBody().getStatements().add(0, new OJSimpleStatement("super( " + TinkerGenerationUtil.PERSISTENT_CONSTRUCTOR_PARAM_NAME + " )"));
	}

	private void implementCompositionNode(OJAnnotatedClass ojClass) {
		ojClass.addToImplementedInterfaces(TinkerGenerationUtil.tinkerCompositionNodePathName);
	}

	protected void addPersistentConstructor(OJAnnotatedClass ojClass) {
		OJConstructor persistentConstructor = new OJConstructor();
		persistentConstructor.setName(TinkerGenerationUtil.PERSISTENT_CONSTRUCTOR_NAME);
		persistentConstructor.addParam(TinkerGenerationUtil.PERSISTENT_CONSTRUCTOR_PARAM_NAME, new OJPathName("java.lang.Boolean"));
		ojClass.addToConstructors(persistentConstructor);
	}

	public OJAnnotatedOperation addClearCache(OJAnnotatedClass ojClass, Class clazz) {
		OJAnnotatedOperation clearCache = new OJAnnotatedOperation("clearCache");
		TinkerGenerationUtil.addOverrideAnnotation(clearCache);
		if (!clazz.getGeneralizations().isEmpty()) {
			clearCache.getBody().addToStatements("super.clearCache()");
		}
		ojClass.addToOperations(clearCache);

		for (Property p :  clazz.getOwnedAttributes()) {
			if (!(p.isDerived() || p.isDerivedUnion())) {
				if (TumlPropertyOperations.isOne(p)) {
					OJSimpleStatement statement = new OJSimpleStatement("this." + TumlPropertyOperations.fieldName(p) + " = null");
					statement.setName(TumlPropertyOperations.fieldName(p));
					clearCache.getBody().addToStatements(statement);
				} else {
					OJSimpleStatement statement = new OJSimpleStatement("this." + TumlPropertyOperations.fieldName(p) + " = " + TumlPropertyOperations.getDefaultTinkerCollectionInitalisation(p).getExpression());
					statement.setName(TumlPropertyOperations.fieldName(p));
					clearCache.getBody().addToStatements(statement);
				}
			}
		}

		return clearCache;
	}

	protected void addContructorWithVertex(OJAnnotatedClass ojClass, Class clazz) {
		OJConstructor constructor = new OJConstructor();
		constructor.addParam("vertex", new OJPathName("com.tinkerpop.blueprints.pgm.Vertex"));
		if (clazz.getGeneralizations().isEmpty()) {
			constructor.getBody().addToStatements("this.vertex=vertex");
		} else {
			constructor.getBody().addToStatements("super(vertex)");
		}
		ojClass.addToConstructors(constructor);
	}

	protected void implementIsRoot(OJAnnotatedClass ojClass, boolean b) {
		OJAnnotatedOperation isRoot = new OJAnnotatedOperation("isTinkerRoot");
		isRoot.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
		isRoot.setReturnType(new OJPathName("boolean"));
		isRoot.getBody().addToStatements("return " + b);
		ojClass.addToOperations(isRoot);
	}

}
