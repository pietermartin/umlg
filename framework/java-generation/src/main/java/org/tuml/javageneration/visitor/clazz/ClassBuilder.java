package org.tuml.javageneration.visitor.clazz;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJSimpleStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.opaeum.java.metamodel.annotation.OJAnnotationValue;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ClassBuilder extends BaseVisitor implements Visitor<Class> {

	public ClassBuilder(Workspace workspace) {
		super(workspace);
	}

	public static final String INIT_VARIABLES = "initVariables";
	public static final String INITIALISE_PROPERTIES = "initialiseProperties";

	@Override
	public void visitBefore(Class clazz) {
		OJAnnotatedClass annotatedClass = findOJClass(clazz);
		setSuperClass(annotatedClass, clazz);
		if (TumlClassOperations.hasCompositeOwner(clazz)) {
			implementCompositionNode(annotatedClass);
		} else {
			implementTumlNode(annotatedClass);
		}
		addDefaultSerialization(annotatedClass);
		implementIsRoot(annotatedClass, TumlClassOperations.getOtherEndToComposite(clazz) == null);
		addPersistentConstructor(annotatedClass);
		addInitialiseProperties(annotatedClass, clazz);
		addContructorWithVertex(annotatedClass, clazz);
		addInitialisePropertiesInConstructorWithVertex(annotatedClass);
		if (clazz.getGeneralizations().isEmpty()) {
			persistUid(annotatedClass);
			addGetObjectVersion(annotatedClass);
			addGetSetId(annotatedClass);
			initialiseVertexInPersistentConstructor(annotatedClass, clazz);
			addInitialisePropertiesInPersistentConstructor(annotatedClass);
			addInitVariablesInPersistentConstructor(annotatedClass);
			createComponentsInPersistentConstructor(annotatedClass);
		} else {
			addSuperWithPersistenceToDefaultConstructor(annotatedClass);
		}
		addCreateComponents(annotatedClass, clazz);
		addInitVariables(annotatedClass, clazz);
		addDelete(annotatedClass, clazz);
	}

	@Override
	public void visitAfter(Class clazz) {
	}

	private void addInitialisePropertiesInConstructorWithVertex(OJAnnotatedClass annotatedClass) {
		OJConstructor c = annotatedClass.findConstructor(TinkerGenerationUtil.vertexPathName);
		c.getBody().addToStatements(INITIALISE_PROPERTIES + "()");
	}

	// TODO turn into proper value
	private void addDefaultSerialization(OJAnnotatedClass annotatedClass) {
		OJField defaultSerialization = new OJField(annotatedClass, "serialVersionUID", new OJPathName("long"));
		defaultSerialization.setFinal(true);
		defaultSerialization.setStatic(true);
		defaultSerialization.setInitExp("1L");
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
		ojClass.addToImports("java.util.UUID");
		ojClass.addToOperations(getUid);
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

	protected void initialiseVertexInPersistentConstructor(OJAnnotatedClass ojClass, Class c) {
		OJConstructor constructor = ojClass.findConstructor(new OJPathName("java.lang.Boolean"));
		constructor.getBody().addToStatements("this.vertex = " + TinkerGenerationUtil.graphDbAccess + ".addVertex(\"dribble\")");
		constructor.getBody().addToStatements("this.vertex.setProperty(\"className\", getClass().getName())");
		if (TumlClassOperations.hasCompositeOwner(c)) {
			constructor.getBody().addToStatements("TransactionThreadEntityVar.setNewEntity(this)");
			ojClass.addToImports(TinkerGenerationUtil.transactionThreadEntityVar);
		}
		constructor.getBody().addToStatements("defaultCreate()");
		ojClass.addToImports(TinkerGenerationUtil.graphDbPathName);
	}

	private void addSuperWithPersistenceToDefaultConstructor(OJAnnotatedClass ojClass) {
		OJConstructor constructor = ojClass.findConstructor(new OJPathName("java.lang.Boolean"));
		constructor.getBody().getStatements().add(0, new OJSimpleStatement("super( " + TinkerGenerationUtil.PERSISTENT_CONSTRUCTOR_PARAM_NAME + " )"));
	}

	protected void addPersistentConstructor(OJAnnotatedClass ojClass) {
		OJConstructor persistentConstructor = new OJConstructor();
		persistentConstructor.setName(TinkerGenerationUtil.PERSISTENT_CONSTRUCTOR_NAME);
		persistentConstructor.addParam(TinkerGenerationUtil.PERSISTENT_CONSTRUCTOR_PARAM_NAME, new OJPathName("java.lang.Boolean"));
		ojClass.addToConstructors(persistentConstructor);
	}

	private void addInitialiseProperties(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation initialiseProperties = new OJAnnotatedOperation(INITIALISE_PROPERTIES);
		TinkerGenerationUtil.addOverrideAnnotation(initialiseProperties);
		if (!clazz.getGeneralizations().isEmpty()) {
			initialiseProperties.getBody().addToStatements("super." + INITIALISE_PROPERTIES + "()");
		}
		annotatedClass.addToOperations(initialiseProperties);
		for (Property p : TumlClassOperations.getAllOwnedProperties(clazz)) {
			PropertyWrapper pWrap = new PropertyWrapper(p);
			if (!(pWrap.isDerived() || pWrap.isDerivedUnion())) {
				OJSimpleStatement statement = new OJSimpleStatement("this." + pWrap.fieldname() + " = " + pWrap.javaDefaultInitialisation(clazz));
				statement.setName(pWrap.fieldname());
				initialiseProperties.getBody().addToStatements(statement);
				annotatedClass.addToImports(pWrap.javaImplTypePath());
			}
		}
	}

	protected void addContructorWithVertex(OJAnnotatedClass ojClass, Class clazz) {
		OJConstructor constructor = new OJConstructor();
		constructor.addParam("vertex", TinkerGenerationUtil.vertexPathName);
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

	private void addInitialisePropertiesInPersistentConstructor(OJAnnotatedClass annotatedClass) {
		OJConstructor constructor = annotatedClass.findConstructor(new OJPathName("java.lang.Boolean"));
		constructor.getBody().addToStatements(INITIALISE_PROPERTIES + "()");
	}

	private void addInitVariablesInPersistentConstructor(OJAnnotatedClass annotatedClass) {
		OJConstructor constructor = annotatedClass.findConstructor(new OJPathName("java.lang.Boolean"));
		constructor.getBody().addToStatements(INIT_VARIABLES + "()");
	}

	private void createComponentsInPersistentConstructor(OJAnnotatedClass annotatedClass) {
		OJConstructor constructor = annotatedClass.findConstructor(new OJPathName("java.lang.Boolean"));
		constructor.getBody().addToStatements("createComponents()");
	}

	private void addInitVariables(OJAnnotatedClass annotatedClass, Class clazz) {
		OJOperation initVariables = new OJAnnotatedOperation(INIT_VARIABLES);
		initVariables.setBody(annotatedClass.getDefaultConstructor().getBody());
		if (TumlClassOperations.hasSupertype(clazz)) {
			OJSimpleStatement simpleStatement = new OJSimpleStatement("super.initVariables()");
			if (initVariables.getBody().getStatements().isEmpty()) {
				initVariables.getBody().addToStatements(simpleStatement);
			} else {
				initVariables.getBody().getStatements().set(0, simpleStatement);
			}
		}
		annotatedClass.addToOperations(initVariables);
	}

	private void addCreateComponents(OJAnnotatedClass annotatedClass, Class clazz) {
		OJOperation createComponents = new OJAnnotatedOperation("createComponents");
		createComponents.setBody(new OJBlock());
		if (TumlClassOperations.hasSupertype(clazz)) {
			createComponents.getBody().addToStatements("super.createComponents()");
		}
		annotatedClass.addToOperations(createComponents);
	}

	private void addDelete(OJAnnotatedClass annotatedClass, Class clazz) {
		OJAnnotatedOperation delete = new OJAnnotatedOperation("delete");
		TinkerGenerationUtil.addOverrideAnnotation(delete);
		annotatedClass.addToOperations(delete);
	}

	private void implementCompositionNode(OJAnnotatedClass annotatedClass) {
		annotatedClass.addToImplementedInterfaces(TinkerGenerationUtil.tinkerCompositionNodePathName);
	}

	private void implementTumlNode(OJAnnotatedClass annotatedClass) {
		annotatedClass.addToImplementedInterfaces(TinkerGenerationUtil.TINKER_NODE);
	}

}
