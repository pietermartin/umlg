package org.tuml.javageneration.visitor.clazz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.tuml.java.metamodel.OJBlock;
import org.tuml.java.metamodel.OJConstructor;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJIfStatement;
import org.tuml.java.metamodel.OJOperation;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.OJSimpleStatement;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.annotation.OJAnnotationValue;
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

    public ClassBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    public static final String INIT_VARIABLES = "initVariables";
    public static final String INITIALISE_PROPERTIES = "initialiseProperties";

    @Override
    public void visitBefore(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        setSuperClass(annotatedClass, clazz);
        implementCompositionNode(annotatedClass);
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
//			createComponentsInPersistentConstructor(annotatedClass);
        } else {
            addSuperWithPersistenceToDefaultConstructor(annotatedClass);
        }
//		addCreateComponents(annotatedClass, clazz);
        addInitVariables(annotatedClass, clazz);
        addDelete(annotatedClass, clazz);
        addGetQualifiedName(annotatedClass, clazz);
        if (!clazz.isAbstract()) {
            addEdgeToMetaNode(annotatedClass, clazz);
        }
        addAllInstances(annotatedClass, clazz);
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
            throw new IllegalStateException(
                    String.format("Multiple inheritence is not supported! Class %s has more than on genereralization.", clazz.getName()));
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
        constructor.getBody().addToStatements("this.vertex = " + TinkerGenerationUtil.graphDbAccess + ".addVertex(this.getClass().getName())");
        constructor.getBody().addToStatements("this.vertex.setProperty(\"className\", getClass().getName())");
        //Link up to the meta instance for allInstances
        constructor.getBody().addToStatements("addEdgeToMetaNode()");
        constructor.getBody().addToStatements(TinkerGenerationUtil.transactionThreadEntityVar.getLast() + ".setNewEntity(this)");
        ojClass.addToImports(TinkerGenerationUtil.transactionThreadEntityVar);
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
                if (pWrap.isOne() && pWrap.isBoolean()) {
                    OJIfStatement ifEmpty = new OJIfStatement("this." + pWrap.fieldname() + ".isEmpty()");
                    ifEmpty.setComment("Booleans are defaulted to false if the entity already exist then it will already have a value");
                    ifEmpty.addToThenPart("this." + pWrap.fieldname() + ".add(false)");
                    initialiseProperties.getBody().addToStatements(ifEmpty);
                }
                annotatedClass.addToImports(pWrap.javaImplTypePath());
            }
        }
    }

    protected void addContructorWithVertex(OJAnnotatedClass ojClass, Class clazz) {
        OJConstructor constructor = new OJConstructor();
        constructor.addParam("vertex", TinkerGenerationUtil.vertexPathName);
        if (clazz.getGeneralizations().isEmpty()) {
            constructor.getBody().addToStatements("this.vertex=vertex");
            constructor.getBody().addToStatements(TinkerGenerationUtil.transactionThreadEntityVar.getLast() + ".setNewEntity(this)");
            ojClass.addToImports(TinkerGenerationUtil.transactionThreadEntityVar);
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

    public static void addGetQualifiedName(OJAnnotatedClass annotatedClass, Classifier c) {
        OJAnnotatedOperation getQualifiedName = new OJAnnotatedOperation("getQualifiedName");
        TinkerGenerationUtil.addOverrideAnnotation(getQualifiedName);
        getQualifiedName.setReturnType("String");
        getQualifiedName.getBody().addToStatements("return \"" + c.getQualifiedName() + "\"");
        annotatedClass.addToOperations(getQualifiedName);
    }

    private void addAllInstancesWalkingTheTree(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation allInstances = new OJAnnotatedOperation("allInstances");
        allInstances.setStatic(true);
        if (TumlClassOperations.getSpecializations(clazz).isEmpty()) {
            allInstances.setReturnType(TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(TumlClassOperations.getPathName(clazz)));
        } else {
            String pathName = "? extends " + TumlClassOperations.getPathName(clazz).getLast();
            allInstances.setReturnType(TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(pathName));
        }
        annotatedClass.addToImports(TinkerGenerationUtil.Root);
        List<String> propertyList = new ArrayList<String>();
        List<String> localPropertyList = new ArrayList<String>();
        if (TumlClassOperations.hasCompositeOwner(clazz)) {
            PropertyWrapper otherEndToComposite = new PropertyWrapper(TumlClassOperations.getOtherEndToComposite(clazz));
            PropertyWrapper composite = new PropertyWrapper(otherEndToComposite.getOtherEnd());
            buildLookupFromRoot(annotatedClass, propertyList, composite, TumlClassOperations.getPathName(clazz), clazz);
        } else {
            // Check if it has specialisations that have composite owners
            Set<Classifier> specializationsWithCompositeOwner = TumlClassOperations.getSpecializationWithCompositeOwner(clazz);
            // Do not visit where you come from. Infinite loop...
            // specializationsWithCompositeOwner.remove(clazz);
            if (!specializationsWithCompositeOwner.isEmpty()) {
                // Specialisation must be unioned together
                List<String> unionedPropertyList = new ArrayList<String>();
                int count = 1;
                for (Classifier c : specializationsWithCompositeOwner) {
                    List<String> propertyListToUnion = new ArrayList<String>();
                    propertyListToUnion.addAll(propertyList);
                    PropertyWrapper otherEndToComposite = new PropertyWrapper(TumlClassOperations.getOtherEndToComposite(c));
                    PropertyWrapper composite = new PropertyWrapper(otherEndToComposite.getOtherEnd());
                    buildLookupFromRoot(annotatedClass, propertyListToUnion, composite, TumlClassOperations.getPathName(clazz), c);
                    if (specializationsWithCompositeOwner.size() != count++) {
                        propertyListToUnion.add(0, ")");
                        propertyListToUnion.add(".union(");
                    }
                    unionedPropertyList.addAll(propertyListToUnion);
                }
                propertyList.clear();
                propertyList.addAll(unionedPropertyList);
            }
            Set<Classifier> specializationsWithoutCompositeOwner = TumlClassOperations.getConcreteSpecializationsWithoutCompositeOwner(clazz);
            // specializationsWithoutCompositeOwner.remove(clazz);
            if (!specializationsWithoutCompositeOwner.isEmpty()) {
                List<String> unionedPropertyList = new ArrayList<String>();
                int count = 1;
                for (Classifier classifier : specializationsWithoutCompositeOwner) {
                    List<String> propertyListToUnion = new ArrayList<String>();

                    if (1 != count || !specializationsWithCompositeOwner.isEmpty()) {
                        propertyListToUnion.add(".union(");
                    }
                    if (specializationsWithoutCompositeOwner.size() != count++) {
                        propertyListToUnion.add(")");
                    }
                    propertyListToUnion.addAll(localPropertyList);

                    propertyListToUnion.add(">flatten()");
                    propertyListToUnion.add(TumlClassOperations.getPathName(clazz).getLast());
                    propertyListToUnion.add(".<");
                    propertyListToUnion.add(TinkerGenerationUtil.Root.getCopy().getLast() + ".INSTANCE." + "get" + TumlClassOperations.className(classifier)
                            + "()");
                    unionedPropertyList.addAll(propertyListToUnion);
                }
                localPropertyList.clear();
                if (specializationsWithCompositeOwner.isEmpty()) {
                    // Nothing to union
                    propertyList.clear();
                }
                propertyList.addAll(unionedPropertyList);
                if (!specializationsWithCompositeOwner.isEmpty()) {
                    propertyList.add(0, ")");
                }
            }
            if (specializationsWithCompositeOwner.isEmpty() && specializationsWithoutCompositeOwner.isEmpty()) {
                if (!clazz.isAbstract()) {
                    propertyList.add(TinkerGenerationUtil.Root.getCopy().getLast() + ".INSTANCE." + "get" + TumlClassOperations.className(clazz) + "()");
                } else {
                    propertyList.add("new " + TinkerGenerationUtil.tumlMemorySet.getLast() + "<" + TumlClassOperations.getPathName(clazz).getLast() + ">()");
                    annotatedClass.addToImports(TinkerGenerationUtil.tumlMemorySet);
                }
            }

        }
        Collections.reverse(propertyList);
        StringBuilder java = new StringBuilder();
        java.append("return ");
        for (String s : propertyList) {
            java.append(s);
        }
        java.append(".asSet()");
        allInstances.getBody().addToStatements(java.toString());
        annotatedClass.addToImports(TinkerGenerationUtil.Root);
        annotatedClass.addToOperations(allInstances);
    }

    private void buildLookupFromRoot(OJAnnotatedClass annotatedClass, List<String> propertyList, PropertyWrapper compositeEndPWrap, OJPathName returnPath,
                                     Classifier allInstanceToGet) {
        annotatedClass.addToImports(TinkerGenerationUtil.BodyExpressionEvaluator);
        annotatedClass.addToImports(compositeEndPWrap.javaBaseTypePath());
        annotatedClass.addToImports(TumlClassOperations.getPathName(compositeEndPWrap.getOwningType()));
        annotatedClass.addToImports(compositeEndPWrap.javaTumlTypePath());
        boolean needNarrowing = TumlClassOperations.isSpecializationOf(allInstanceToGet, compositeEndPWrap.getType())
                && !allInstanceToGet.equals(compositeEndPWrap.getType());
        if (needNarrowing) {
            // Add in a narrowing of the result to the correct type only
            propertyList.add(constructTypeNarrowCollectStatement(compositeEndPWrap, allInstanceToGet, returnPath));
            annotatedClass.addToImports(TumlClassOperations.getPathName(allInstanceToGet));
        }
        propertyList.add(constructCollectStatement(compositeEndPWrap, returnPath, needNarrowing));
        List<String> localPropertyList = new ArrayList<String>();
        localPropertyList.addAll(propertyList);
        Classifier owningType = (Classifier) compositeEndPWrap.getOwningType();
        if (!(owningType instanceof Interface) && TumlClassOperations.hasCompositeOwner(owningType)) {
            PropertyWrapper otherEndToComposite = new PropertyWrapper(TumlClassOperations.getOtherEndToComposite(owningType));
            PropertyWrapper composite = new PropertyWrapper(otherEndToComposite.getOtherEnd());
            buildLookupFromRoot(annotatedClass, propertyList, composite, composite.javaBaseTypePath(), owningType);
        } else {
            // Check if it has specialisations that have composite owners
            Set<Classifier> specializationsWithCompositeOwner;
            if (owningType instanceof Interface) {
                specializationsWithCompositeOwner = TumlClassOperations.getRealizationWithCompositeOwner((Interface) owningType);
            } else {
                specializationsWithCompositeOwner = TumlClassOperations.getSpecializationWithCompositeOwner(owningType);
            }
            // Do not visit where you come from. Infinite loop...
            specializationsWithCompositeOwner.remove(owningType);
            specializationsWithCompositeOwner.remove(compositeEndPWrap.getType());

            // These 2 groups must be unioned together
            Set<Classifier> specializationsWithoutCompositeOwner;
            if (owningType instanceof Interface) {
                specializationsWithoutCompositeOwner = TumlClassOperations.getRealizationWithoutCompositeOwner((Interface) owningType);
            } else {
                specializationsWithoutCompositeOwner = TumlClassOperations.getConcreteSpecializationsWithoutCompositeOwner(owningType);
            }
            specializationsWithoutCompositeOwner.remove(owningType);
            if (owningType.isAbstract()) {
                specializationsWithoutCompositeOwner.remove(compositeEndPWrap.getType());
            }

            if (!specializationsWithCompositeOwner.isEmpty()) {

                // Specialisation must be unioned together
                List<String> unionedPropertyList = new ArrayList<String>();
                int count = 1;
                for (Classifier c : specializationsWithCompositeOwner) {
                    List<String> propertyListToUnion = new ArrayList<String>();
                    propertyListToUnion.addAll(propertyList);
                    PropertyWrapper otherEndToComposite = new PropertyWrapper(TumlClassOperations.getOtherEndToComposite(c));
                    PropertyWrapper composite = new PropertyWrapper(otherEndToComposite.getOtherEnd());
                    buildLookupFromRoot(annotatedClass, propertyListToUnion, composite, TumlClassOperations.getPathName(owningType), c);
                    if (specializationsWithCompositeOwner.size() != count++) {
                        propertyListToUnion.add(0, ")");
                        propertyListToUnion.add(".union(");
                    }
                    unionedPropertyList.addAll(propertyListToUnion);
                }
                propertyList.clear();
                propertyList.addAll(unionedPropertyList);

            }

            if (!specializationsWithoutCompositeOwner.isEmpty()) {
                List<String> unionedPropertyList = new ArrayList<String>();
                int count = 1;
                for (Classifier classifier : specializationsWithoutCompositeOwner) {
                    List<String> propertyListToUnion = new ArrayList<String>();

                    if (1 != count || !specializationsWithCompositeOwner.isEmpty()) {
                        propertyListToUnion.add(".union(");
                    }
                    if (specializationsWithoutCompositeOwner.size() != count++) {
                        propertyListToUnion.add(")");
                    }
                    propertyListToUnion.addAll(localPropertyList);
                    propertyListToUnion.add(">flatten()");
                    propertyListToUnion.add(TumlClassOperations.getPathName(owningType).getLast());
                    propertyListToUnion.add(".<");
                    propertyListToUnion.add(TinkerGenerationUtil.Root.getCopy().getLast() + ".INSTANCE." + "get" + TumlClassOperations.className(classifier)
                            + "()");
                    unionedPropertyList.addAll(propertyListToUnion);
                }
                localPropertyList.clear();
                if (specializationsWithCompositeOwner.isEmpty()) {
                    // Nothing to union
                    propertyList.clear();
                }
                propertyList.addAll(unionedPropertyList);
                if (!specializationsWithCompositeOwner.isEmpty()) {
                    propertyList.add(0, ")");
                }
            }

            if (specializationsWithCompositeOwner.isEmpty() && specializationsWithoutCompositeOwner.isEmpty()) {
                if (!owningType.isAbstract()) {
                    propertyList.add(TinkerGenerationUtil.Root.getCopy().getLast() + ".INSTANCE." + "get" + TumlClassOperations.className(owningType) + "()");
                } else {
                    //Convert it to the same collection type being union with
                    if (compositeEndPWrap.isOrdered()) {
                        propertyList.add(".asSequence()");
                    }
                    propertyList.add("new " + TinkerGenerationUtil.tumlMemorySet.getLast() + "<" + TumlClassOperations.getPathName(owningType).getLast()
                            + ">()");
                    annotatedClass.addToImports(TinkerGenerationUtil.tumlMemorySet);
                }
            }

        }
    }

    private String constructTypeNarrowCollectStatement(PropertyWrapper pWrap, Classifier allInstanceToGet, OJPathName returnPath) {
        OJPathName returnPathForNarrow = TumlClassOperations.getPathName(allInstanceToGet);
        StringBuilder sb = new StringBuilder();
        sb.append(".<");
        sb.append(returnPath.getLast());
        sb.append(", ");
        sb.append(returnPathForNarrow.getLast());
        sb.append(">");
        sb.append("collect(new BodyExpressionEvaluator<");
        sb.append(returnPathForNarrow.getLast());
        sb.append(", ");
        sb.append(TumlClassOperations.getPathName(pWrap.getType()).getLast());
        sb.append(">() {\n");
        sb.append("		@Override\n");
        sb.append("		public ");
        sb.append(returnPathForNarrow.getLast());
        sb.append(" evaluate(");
        sb.append(TumlClassOperations.getPathName(pWrap.getType()).getLast());
        sb.append(" e) {\n");
        sb.append("			return e instanceof ");
        sb.append(returnPathForNarrow.getLast());
        sb.append(" ? (");
        sb.append(returnPathForNarrow.getLast());
        sb.append(")e : null");
        sb.append(";\n		}}\n)");
        return sb.toString();
    }

    private String constructCollectStatement(PropertyWrapper compositeEndPWrap, OJPathName returnPath, boolean needNarrowing) {
        StringBuilder sb = new StringBuilder();
        sb.append(".<");
        if (needNarrowing) {
            sb.append(compositeEndPWrap.javaBaseTypePath().getLast());
        } else {
            sb.append(returnPath.getLast());
        }
        sb.append(", ");
        if (compositeEndPWrap.isOne()) {
            sb.append(compositeEndPWrap.javaBaseTypePath().getLast());
        } else {
            sb.append(compositeEndPWrap.javaTumlTypePath().getLast());
        }
        sb.append(">");
        sb.append("collect(new BodyExpressionEvaluator<");
        if (compositeEndPWrap.isOne()) {
            sb.append(compositeEndPWrap.javaBaseTypePath().getLast());
        } else {
            sb.append(compositeEndPWrap.javaTumlTypePath().getLast());
        }
        sb.append(", ");
        sb.append(TumlClassOperations.getPathName(compositeEndPWrap.getOwningType()).getLast());
        sb.append(">() {\n");
        sb.append("		@Override\n");
        sb.append("		public ");
        if (compositeEndPWrap.isOne()) {
            sb.append(compositeEndPWrap.javaBaseTypePath().getLast());
        } else {
            sb.append(compositeEndPWrap.javaTumlTypePath().getLast());
        }
        sb.append(" evaluate(");
        sb.append(TumlClassOperations.getPathName(compositeEndPWrap.getOwningType()).getLast());
        sb.append(" e) {\n");
        sb.append("			return e.");
//		sb.append(compositeEndPWrap.getter());
        // Deal with hierarchies
        if (compositeEndPWrap.getType() instanceof Class && compositeEndPWrap.getOtherEnd().getType() instanceof Class
                && TumlClassOperations.isHierarchy((Class) compositeEndPWrap.getType())
                && TumlClassOperations.isHierarchy((Class) compositeEndPWrap.getOtherEnd().getType())) {
//			sb.append("().union((TinkerSet<? extends ");
//			sb.append(compositeEndPWrap.javaBaseTypePath().getLast());
//			sb.append(">) e.getAllChildren());\n	}}\n)");
            sb.append("getAllChildren().<" + compositeEndPWrap.javaBaseTypePath().getLast() + ">flatten();\n	}}\n)");
        } else {
            sb.append(compositeEndPWrap.getter());
            sb.append("();\n		}}\n)");
        }
        return sb.toString();
    }

    private void addEdgeToMetaNode(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation addEdgeToMetaNode = new OJAnnotatedOperation("addEdgeToMetaNode");
        TinkerGenerationUtil.addOverrideAnnotation(addEdgeToMetaNode);
        addEdgeToMetaNode.getBody().addToStatements(TinkerGenerationUtil.graphDbAccess + ".addEdge(null, this.vertex, getMetaNode().getVertex(), " + TinkerGenerationUtil.TUML_NODE.getLast() + ".ALLINSTANCES_EDGE_LABEL)");
        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
        annotatedClass.addToOperations(addEdgeToMetaNode);
    }

    private void addAllInstances(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation allInstances = new OJAnnotatedOperation("allInstances");
        allInstances.setStatic(true);
        if (TumlClassOperations.getSpecializations(clazz).isEmpty()) {
            allInstances.setReturnType(TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(TumlClassOperations.getPathName(clazz)));
        } else {
            String pathName = "? extends " + TumlClassOperations.getPathName(clazz).getLast();
            allInstances.setReturnType(TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(pathName));
        }

        OJField result = new OJField("result", TinkerGenerationUtil.tinkerSet.getCopy().addToGenerics(TumlClassOperations.getPathName(clazz)));
        result.setInitExp("new " + TinkerGenerationUtil.tumlMemorySet.getCopy().addToGenerics(TumlClassOperations.getPathName(clazz)).getLast() + "()");
        allInstances.getBody().addToLocals(result);
        Set<Classifier> specializations = TumlClassOperations.getSpecializations(clazz);
        for (Classifier c : specializations) {
            annotatedClass.addToImports(TumlClassOperations.getPathName(c));
            allInstances.getBody().addToStatements("result.addAll(" + TumlClassOperations.className(c) + ".allInstances())");
        }

        allInstances.getBody().addToStatements("return result");
        annotatedClass.addToImports(TinkerGenerationUtil.tumlMemorySet);
        annotatedClass.addToImports(TinkerGenerationUtil.Root);
        annotatedClass.addToOperations(allInstances);
    }

}
