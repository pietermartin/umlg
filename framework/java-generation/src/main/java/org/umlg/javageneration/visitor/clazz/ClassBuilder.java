package org.umlg.javageneration.visitor.clazz;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedField;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJAnnotationValue;
import org.umlg.javageneration.ocl.UmlgOcl2Java;
import org.umlg.javageneration.util.*;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.ocl.UmlgOcl2Parser;

import java.util.List;
import java.util.Set;

public class ClassBuilder extends BaseVisitor implements Visitor<Class> {

    public static final String INIT_VARIABLES = "initVariables";
    public static final String INIT_DATE_TYPE_VARIABLES_WITH_DEFAULT_VALUES = "initDataTypeVariablesWithDefaultValues";
    public static final String INITIALISE_PROPERTIES = "initialiseProperties";
    public static final String BOOLEAN_PROPERTIES = "z_internalBooleanProperties";
    public static final String DATE_TYPE_PROPERTIES_WITH_DEFAULT_VALUES = "z_internalDataTypePropertiesWithDefaultValues";
    public static final String DATE_TYPE_PROPERTIES = "z_internalDataTypeProperties";
    public static final String GET_COLLECTION_FOR = "z_internalGetCollectionFor";

    public static final String INTERNAL_ADD_TO_COLLECTION = "z_internalAddToCollection";
    public static final String INTERNAL_ADD_PERSISTENT_VALUE_TO_COLLECTION = "z_internalAddPersistentValueToCollection";
    public static final String INTERNAL_MARK_TO_COLLECTION_LOADED = "z_internalMarkCollectionLoaded";

    public ClassBuilder(Workspace workspace) {
        super(workspace);
    }

    public ClassBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        setSuperClass(annotatedClass, clazz);
        implementCompositionNode(annotatedClass);
        addDefaultSerialization(annotatedClass);
        implementIsRoot(annotatedClass, UmlgClassOperations.getOtherEndToComposite(clazz).isEmpty());
        addPersistentConstructor(annotatedClass);
        callPersistentConstructorFromDefault(annotatedClass);
        addInitialiseProperties(annotatedClass, clazz);
        addGetBooleanProperties(annotatedClass, clazz);
        addGetDataTypeWithDefaultValues(annotatedClass, clazz);
        addGetDataTypeProperties(annotatedClass, clazz);
        addGetCollectionForRuntimeProperty(annotatedClass, clazz);
        addContructorWithVertexAndConstructorWithId(annotatedClass, clazz);
        if (clazz.getGeneralizations().isEmpty()) {
            persistUid(annotatedClass);
        }
        addInitVariables(annotatedClass, clazz);
        addPrimitiveInitVariables(annotatedClass, clazz);
        addDelete(annotatedClass, clazz);
        addGetQualifiedName(annotatedClass, clazz);

        addAllInstances(annotatedClass, clazz);
        addAllInstancesWithFilter(annotatedClass, clazz);
        addConstraints(annotatedClass, clazz);
        if (UmlgClassOperations.isAssociationClass(clazz)) {
            annotatedClass.addToImplementedInterfaces(UmlgGenerationUtil.AssociationClassNode);
            createCopyOnePrimitivePropertiesToEdge(annotatedClass, clazz);
        }
    }


    @Override
    public void visitAfter(Class clazz) {
    }

    private void createCopyOnePrimitivePropertiesToEdge(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation copyOnePrimitivePropertiesToEdge = new OJAnnotatedOperation("z_internalCopyOnePrimitivePropertiesToEdge");
        UmlgGenerationUtil.addOverrideAnnotation(copyOnePrimitivePropertiesToEdge);
        copyOnePrimitivePropertiesToEdge.addToParameters(new OJParameter("edge", UmlgGenerationUtil.edgePathName));
        annotatedClass.addToOperations(copyOnePrimitivePropertiesToEdge);
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
                    String.format("Multiple inheritance is not supported! Class %s has more than on genereralization.", clazz.getName()));
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

        new OJField(getUid.getBody(), "uid", "String");
        OJIfStatement ifUidNotPresent = new OJIfStatement("!this.vertex.property(\"uid\").isPresent() && this.vertex.property(\"uid\").value() != null");
        ifUidNotPresent.addToThenPart("uid=UUID.randomUUID().toString()");
        ifUidNotPresent.addToThenPart("this.vertex.property(\"uid\", uid)");
        ifUidNotPresent.addToElsePart("uid=this.vertex.value(\"uid\")");
        getUid.getBody().addToStatements(ifUidNotPresent);
        getUid.getBody().addToStatements("return uid");

        ojClass.addToImports("java.util.UUID");
        ojClass.addToOperations(getUid);
    }

    protected void addPersistentConstructor(OJAnnotatedClass ojClass) {
        OJConstructor persistentConstructor = new OJConstructor();
        persistentConstructor.setName(UmlgGenerationUtil.PERSISTENT_CONSTRUCTOR_NAME);
        persistentConstructor.addParam(UmlgGenerationUtil.PERSISTENT_CONSTRUCTOR_PARAM_NAME, new OJPathName("java.lang.Boolean"));
        persistentConstructor.getBody().addToStatements("super(" + UmlgGenerationUtil.PERSISTENT_CONSTRUCTOR_PARAM_NAME + ")");
        ojClass.addToConstructors(persistentConstructor);
    }

    private void callPersistentConstructorFromDefault(OJAnnotatedClass annotatedClass) {
        annotatedClass.getDefaultConstructor().getBody().addToStatements("this(true)");
    }

    public static void addGetBooleanProperties(OJAnnotatedClass annotatedClass, Classifier classifier) {
        OJAnnotatedOperation booleanProperties = new OJAnnotatedOperation(BOOLEAN_PROPERTIES);
        booleanProperties.setReturnType(new OJPathName("java.util.Set").addToGenerics(UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy()));
        annotatedClass.addToImports("java.util.HashSet");
        UmlgGenerationUtil.addOverrideAnnotation(booleanProperties);
        annotatedClass.addToOperations(booleanProperties);
        OJAnnotatedField result = new OJAnnotatedField("result", booleanProperties.getReturnType());
        booleanProperties.getBody().addToLocals(result);
        if (!classifier.getGeneralizations().isEmpty()) {
            result.setInitExp("super." + BOOLEAN_PROPERTIES + "()");
        } else {
            result.setInitExp("new HashSet<" + UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy() + ">()");
        }
        for (Property p : UmlgClassOperations.getAllOwnedProperties(classifier)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!(pWrap.isDerived() || pWrap.isDerivedUnion()) && !(pWrap.isRefined()) &&
                    (!(classifier instanceof Enumeration && pWrap.getType() instanceof DataType))
                    && pWrap.isOne() && pWrap.isBoolean()) {

                String propertyRuntimeEnumName = UmlgClassOperations.propertyEnumName(classifier) + "." + pWrap.fieldname();
                OJSimpleStatement addBooleanStatement = new OJSimpleStatement("result.add(" + propertyRuntimeEnumName + ")");
                booleanProperties.getBody().addToStatements(addBooleanStatement);
            }
        }
        booleanProperties.getBody().addToStatements("return result");
    }

    public static void addGetDataTypeWithDefaultValues(OJAnnotatedClass annotatedClass, Classifier classifier) {
        OJAnnotatedOperation primitiveProperties = new OJAnnotatedOperation(DATE_TYPE_PROPERTIES_WITH_DEFAULT_VALUES);
        primitiveProperties.setReturnType(new OJPathName("java.util.Map").addToGenerics(UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy()).addToGenerics("Object"));
        annotatedClass.addToImports("java.util.HashMap");
        UmlgGenerationUtil.addOverrideAnnotation(primitiveProperties);
        annotatedClass.addToOperations(primitiveProperties);
        OJAnnotatedField result = new OJAnnotatedField("result", primitiveProperties.getReturnType());
        primitiveProperties.getBody().addToLocals(result);
        if (!classifier.getGeneralizations().isEmpty()) {
            result.setInitExp("super." + DATE_TYPE_PROPERTIES_WITH_DEFAULT_VALUES + "()");
        } else {
            result.setInitExp("new HashMap<" + UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy() + ", Object>()");
        }
        for (Property p : UmlgClassOperations.getAllOwnedProperties(classifier)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!(pWrap.isDerived() || pWrap.isDerivedUnion()) && !(pWrap.isRefined()) &&
                    pWrap.getType() instanceof DataType &&
//                    pWrap.isOne() &&
                    (pWrap.getDefaultValue() != null && !pWrap.hasOclDefaultValue())) {

                String propertyRuntimeEnumName = UmlgClassOperations.propertyEnumName(classifier) + "." + pWrap.fieldname();
                OJSimpleStatement addPrimitiveDefaultValueStatement;
                addPrimitiveDefaultValueStatement = new OJSimpleStatement("result.put(" + propertyRuntimeEnumName + ", " + pWrap.getDefaultValueAsJava() + ")");
                primitiveProperties.getBody().addToStatements(addPrimitiveDefaultValueStatement);
            }
        }
        primitiveProperties.getBody().addToStatements("return result");
    }

    public static void addGetDataTypeProperties(OJAnnotatedClass annotatedClass, Classifier classifier) {
        OJAnnotatedOperation primitiveProperties = new OJAnnotatedOperation(DATE_TYPE_PROPERTIES);
        primitiveProperties.setReturnType(new OJPathName("java.util.Set").addToGenerics(UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy()));
        annotatedClass.addToImports("java.util.HashSet");
        UmlgGenerationUtil.addOverrideAnnotation(primitiveProperties);
        annotatedClass.addToOperations(primitiveProperties);
        OJAnnotatedField result = new OJAnnotatedField("result", primitiveProperties.getReturnType());
        primitiveProperties.getBody().addToLocals(result);
        if (!classifier.getGeneralizations().isEmpty()) {
            result.setInitExp("super." + DATE_TYPE_PROPERTIES + "()");
        } else {
            result.setInitExp("new HashSet<" + UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy() + ">()");
        }
        for (Property p : UmlgClassOperations.getAllOwnedProperties(classifier)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!(pWrap.isDerived() || pWrap.isDerivedUnion()) && !(pWrap.isRefined()) &&
                    pWrap.getType() instanceof DataType) {

                String propertyRuntimeEnumName = UmlgClassOperations.propertyEnumName(classifier) + "." + pWrap.fieldname();
                OJSimpleStatement addPrimitiveDefaultValueStatement;
                addPrimitiveDefaultValueStatement = new OJSimpleStatement("result.add(" + propertyRuntimeEnumName + ")");
                primitiveProperties.getBody().addToStatements(addPrimitiveDefaultValueStatement);
            }
        }
        primitiveProperties.getBody().addToStatements("return result");
    }

    private void addGetCollectionForRuntimeProperty(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation getCollectionFor = new OJAnnotatedOperation(GET_COLLECTION_FOR);
        getCollectionFor.addParam("tumlRuntimeProperty", UmlgGenerationUtil.umlgRuntimePropertyPathName.getCopy());
        getCollectionFor.addParam("inverse", "boolean");
        getCollectionFor.setReturnType(new OJPathName("UmlgCollection<? extends Object>"));
        annotatedClass.addToImports(UmlgGenerationUtil.umlgCollection);
        UmlgGenerationUtil.addOverrideAnnotation(getCollectionFor);
        annotatedClass.addToOperations(getCollectionFor);
        OJField result = new OJField("result", new OJPathName("UmlgCollection<? extends Object>"));
        result.setInitExp("null");
        getCollectionFor.getBody().addToLocals(result);
        if (!clazz.getGeneralizations().isEmpty()) {
            getCollectionFor.getBody().addToStatements("result = super." + GET_COLLECTION_FOR + "(tumlRuntimeProperty, inverse)");
        }

        OJField runtimeProperty = new OJField("runtimeProperty", new OJPathName(UmlgClassOperations.propertyEnumName(clazz)));

        OJIfStatement isInverse = new OJIfStatement("!inverse");
        isInverse.setThenPart(new OJBlock());
        isInverse.setElsePart(new OJBlock());
        isInverse.getThenPart().addToStatements("runtimeProperty = (" + UmlgClassOperations.propertyEnumName(clazz) + ".fromQualifiedName(tumlRuntimeProperty.getQualifiedName()))");
        isInverse.getElsePart().addToStatements("runtimeProperty = (" + UmlgClassOperations.propertyEnumName(clazz) + ".fromQualifiedName(tumlRuntimeProperty.getInverseQualifiedName()))");

        OJIfStatement ifResultNull = new OJIfStatement("result == null");
        ifResultNull.setThenPart(new OJBlock());
        getCollectionFor.getBody().addToStatements(ifResultNull);
        ifResultNull.getThenPart().addToLocals(runtimeProperty);
        ifResultNull.getThenPart().addToStatements(isInverse);

        OJIfStatement ifRuntimeNull = new OJIfStatement("runtimeProperty != null");
        ifResultNull.getThenPart().addToStatements(ifRuntimeNull);

        OJSwitchStatement ojSwitchStatement = new OJSwitchStatement();
        ojSwitchStatement.setCondition("runtimeProperty");
        ifRuntimeNull.getThenPart().addToStatements(ojSwitchStatement);
        for (Property p : UmlgClassOperations.getAllOwnedProperties(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            //Boolean are defaulted in the constructor
            if (!(pWrap.isDerived() || pWrap.isDerivedUnion()) && !pWrap.isRefined()) {
                OJSwitchCase ojSwitchCase = new OJSwitchCase();
                ojSwitchCase.setLabel(pWrap.fieldname());
                OJSimpleStatement statement = new OJSimpleStatement("result = this." + pWrap.fieldname());
                statement.setName(pWrap.fieldname());
                ojSwitchCase.getBody().addToStatements(statement);
                ojSwitchStatement.addToCases(ojSwitchCase);
                annotatedClass.addToImports(pWrap.javaImplTypePath());
            }
        }
        getCollectionFor.getBody().addToStatements("return result");
    }


    public static void addInitialiseProperties(OJAnnotatedClass annotatedClass, Classifier classifier) {
        OJAnnotatedOperation initialiseProperties = new OJAnnotatedOperation(INITIALISE_PROPERTIES);
        annotatedClass.addToImports(UmlgGenerationUtil.PropertyTree);
        initialiseProperties.setComment("boolean properties' default values are initialized in the constructor via z_internalBooleanProperties");
        initialiseProperties.addParam("loaded", new OJPathName("boolean"));
        UmlgGenerationUtil.addOverrideAnnotation(initialiseProperties);
        if (!classifier.getGeneralizations().isEmpty()) {
            initialiseProperties.getBody().addToStatements("super." + INITIALISE_PROPERTIES + "(loaded)");
        }
        annotatedClass.addToOperations(initialiseProperties);
        for (Property p : UmlgClassOperations.getAllOwnedProperties(classifier)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!(pWrap.isDerived() || pWrap.isDerivedUnion()) && !(pWrap.isRefined()) &&
                    !(classifier instanceof Enumeration && pWrap.getType() instanceof DataType)) {

                OJSimpleStatement statement = new OJSimpleStatement("this." + pWrap.fieldname() + " = " +
                        pWrap.javaDefaultInitialisation(classifier));

                annotatedClass.addToImports(UmlgGenerationUtil.PropertyTree);
                statement.setName(pWrap.fieldname());
                initialiseProperties.getBody().addToStatements(statement);
                annotatedClass.addToImports(pWrap.javaImplTypePath());

                if (pWrap.isMemberOfAssociationClass()) {
                    //Initialize the collection to the association class
                    statement = new OJSimpleStatement(
                            "this." + pWrap.getAssociationClassFakePropertyName() + " = " +
                                    pWrap.javaDefaultInitialisationForAssociationClass(classifier, true));
                    statement.setName(pWrap.getAssociationClassFakePropertyName());
                    initialiseProperties.getBody().addToStatements(statement);
                    annotatedClass.addToImports(UmlgPropertyOperations.getDefaultTinkerCollectionForAssociationClass(pWrap.getProperty()));
                }
            }
        }

        if (classifier instanceof AssociationClass) {
            //Do the property for the association class
            AssociationClass associationClass = (AssociationClass) classifier;
            List<Property> memberEnds = associationClass.getMemberEnds();
            for (Property memberEnd : memberEnds) {
                PropertyWrapper pWrap = new PropertyWrapper(memberEnd);
                OJSimpleStatement statement = new OJSimpleStatement("this." + pWrap.fieldname() + " = " + pWrap.javaDefaultInitialisation(classifier, true));
                statement.setName(pWrap.fieldname());
                initialiseProperties.getBody().addToStatements(statement);
                annotatedClass.addToImports(UmlgPropertyOperations.getDefaultTinkerCollection(memberEnd, true));
                annotatedClass.addToImports(UmlgGenerationUtil.PropertyTree);
            }
        }
    }

    protected void addContructorWithVertexAndConstructorWithId(OJAnnotatedClass ojClass, Classifier classifier) {
        OJConstructor constructor = new OJConstructor();
        constructor.addParam("id", "Object");
        constructor.getBody().addToStatements("super(id)");
        ojClass.addToConstructors(constructor);

        OJConstructor vertexConstructor = new OJConstructor();
        vertexConstructor.addParam("vertex", UmlgGenerationUtil.vertexPathName);
        vertexConstructor.getBody().addToStatements("super(vertex)");
        ojClass.addToConstructors(vertexConstructor);

    }

    protected void implementIsRoot(OJAnnotatedClass ojClass, boolean b) {
        OJAnnotatedOperation isRoot = new OJAnnotatedOperation("isTinkerRoot");
        isRoot.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
        isRoot.setReturnType(new OJPathName("boolean"));
        isRoot.getBody().addToStatements("return " + b);
        ojClass.addToOperations(isRoot);
    }

    private void addInitVariables(OJAnnotatedClass annotatedClass, Class clazz) {
        OJOperation initVariables = new OJAnnotatedOperation(INIT_VARIABLES);
        if (UmlgClassOperations.hasSupertype(clazz)) {
            OJSimpleStatement simpleStatement = new OJSimpleStatement("super.initVariables()");
            if (initVariables.getBody().getStatements().isEmpty()) {
                initVariables.getBody().addToStatements(simpleStatement);
            } else {
                initVariables.getBody().getStatements().set(0, simpleStatement);
            }
        }
        annotatedClass.addToOperations(initVariables);
    }

    private void addPrimitiveInitVariables(OJAnnotatedClass annotatedClass, Class clazz) {
        OJOperation initVariables = new OJAnnotatedOperation(INIT_DATE_TYPE_VARIABLES_WITH_DEFAULT_VALUES);
        if (UmlgClassOperations.hasSupertype(clazz)) {
            OJSimpleStatement simpleStatement = new OJSimpleStatement("super." + INIT_DATE_TYPE_VARIABLES_WITH_DEFAULT_VALUES + "()");
            if (initVariables.getBody().getStatements().isEmpty()) {
                initVariables.getBody().addToStatements(simpleStatement);
            } else {
                initVariables.getBody().getStatements().set(0, simpleStatement);
            }
        }
        annotatedClass.addToOperations(initVariables);
    }

    private void addDelete(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation delete = new OJAnnotatedOperation("delete");
        UmlgGenerationUtil.addOverrideAnnotation(delete);
        annotatedClass.addToOperations(delete);
    }

    private void implementCompositionNode(OJAnnotatedClass annotatedClass) {
        annotatedClass.addToImplementedInterfaces(UmlgGenerationUtil.umlgCompositionNodePathName);
    }

    public static void addGetQualifiedName(OJAnnotatedClass annotatedClass, Classifier c) {
        OJAnnotatedOperation getQualifiedName = new OJAnnotatedOperation("getQualifiedName");
        UmlgGenerationUtil.addOverrideAnnotation(getQualifiedName);
        getQualifiedName.setReturnType("String");
        getQualifiedName.getBody().addToStatements("return \"" + c.getQualifiedName() + "\"");
        annotatedClass.addToOperations(getQualifiedName);
    }

    private void addEdgeToMetaNode(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation addEdgeToMetaNode = new OJAnnotatedOperation("addEdgeToMetaNode");
        UmlgGenerationUtil.addOverrideAnnotation(addEdgeToMetaNode);
        addEdgeToMetaNode.getBody().addToStatements("getMetaNode().getVertex().addEdge(" + UmlgGenerationUtil.UMLG_NODE.getLast() + ".ALLINSTANCES_EDGE_LABEL, this.vertex)");
        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
        annotatedClass.addToImports(UmlgGenerationUtil.UMLG_NODE);
        annotatedClass.addToOperations(addEdgeToMetaNode);
    }

    private void addAllInstances(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation allInstances = new OJAnnotatedOperation("allInstances");
        allInstances.setStatic(true);
        if (UmlgClassOperations.getConcreteImplementations(clazz).isEmpty()) {
            allInstances.setReturnType(UmlgGenerationUtil.umlgSet.getCopy().addToGenerics(UmlgClassOperations.getPathName(clazz)));
        } else {
            String pathName = "? extends " + UmlgClassOperations.getPathName(clazz).getLast();
            allInstances.setReturnType(UmlgGenerationUtil.umlgSet.getCopy().addToGenerics(pathName));
        }

        OJField result = new OJField("result", UmlgGenerationUtil.umlgSet.getCopy().addToGenerics(UmlgClassOperations.getPathName(clazz)));
        result.setInitExp("new " + UmlgGenerationUtil.umlgMemorySet.getCopy().addToGenerics(UmlgClassOperations.getPathName(clazz)).getLast() + "()");
        allInstances.getBody().addToLocals(result);
        Set<Classifier> specializations = UmlgClassOperations.getConcreteImplementations(clazz);
        if (!clazz.isAbstract()) {
            specializations.add(clazz);
        }
        for (Classifier c : specializations) {
            annotatedClass.addToImports(UmlgClassOperations.getPathName(c));
            allInstances.getBody().addToStatements("result.addAll(" + UmlgGenerationUtil.UMLGPathName.getLast() + ".get().allInstances(" + UmlgClassOperations.getPathName(c).getLast() + ".class.getName()))");
            annotatedClass.addToImports(UmlgClassOperations.getPathName(c));
        }
        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);

        allInstances.getBody().addToStatements("return result");
        annotatedClass.addToImports(UmlgGenerationUtil.umlgMemorySet);
        annotatedClass.addToOperations(allInstances);
    }

    private void addAllInstancesWithFilter(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation allInstances = new OJAnnotatedOperation("allInstances");
        allInstances.addToParameters(new OJParameter("filter", UmlgGenerationUtil.Filter));
        allInstances.setStatic(true);
        if (UmlgClassOperations.getConcreteImplementations(clazz).isEmpty()) {
            allInstances.setReturnType(UmlgGenerationUtil.umlgSet.getCopy().addToGenerics(UmlgClassOperations.getPathName(clazz)));
        } else {
            String pathName = "? extends " + UmlgClassOperations.getPathName(clazz).getLast();
            allInstances.setReturnType(UmlgGenerationUtil.umlgSet.getCopy().addToGenerics(pathName));
        }

        OJField result = new OJField("result", UmlgGenerationUtil.umlgSet.getCopy().addToGenerics(UmlgClassOperations.getPathName(clazz)));
        result.setInitExp("new " + UmlgGenerationUtil.umlgMemorySet.getCopy().addToGenerics(UmlgClassOperations.getPathName(clazz)).getLast() + "()");
        allInstances.getBody().addToLocals(result);
        Set<Classifier> specializations = UmlgClassOperations.getConcreteImplementations(clazz);
        if (!clazz.isAbstract()) {
            specializations.add(clazz);
        }
        for (Classifier c : specializations) {
            annotatedClass.addToImports(UmlgClassOperations.getPathName(c));
            allInstances.getBody().addToStatements("result.addAll(UMLG.get().allInstances(" + UmlgClassOperations.getPathName(c).getLast() + ".class.getName(), filter))");
            annotatedClass.addToImports(UmlgClassOperations.getPathName(c));
        }

        allInstances.getBody().addToStatements("return result");
        annotatedClass.addToImports(UmlgGenerationUtil.umlgMemorySet);
//        annotatedClass.addToImports(UmlgGenerationUtil.Root);
        annotatedClass.addToOperations(allInstances);
    }

    private void addConstraints(OJAnnotatedClass annotatedClass, Class clazz) {
        List<Constraint> constraints = ModelLoader.INSTANCE.getConstraints(clazz);
        for (Constraint constraint : constraints) {
            ConstraintWrapper constraintWrapper = new ConstraintWrapper(constraint);
            OJAnnotatedOperation checkClassConstraint = new OJAnnotatedOperation(UmlgClassOperations.checkClassConstraintName(constraint));

            checkClassConstraint.setReturnType(new OJPathName("java.util.List").addToGenerics(UmlgGenerationUtil.UmlgConstraintViolation));
            OJField result = new OJField("result", new OJPathName("java.util.List").addToGenerics(UmlgGenerationUtil.UmlgConstraintViolation));
            result.setInitExp("new ArrayList<" + UmlgGenerationUtil.UmlgConstraintViolation.getLast() + ">()");

            OJIfStatement ifConstraintFails = new OJIfStatement();
            String ocl = constraintWrapper.getConstraintOclAsString();
            checkClassConstraint.setComment(String.format("Implements the ocl statement for constraint '%s'\n<pre>\n%s\n</pre>", constraintWrapper.getName(), ocl));
            OCLExpression<Classifier> oclExp = UmlgOcl2Parser.INSTANCE.parseOcl(ocl);

            ifConstraintFails.setCondition("(" + UmlgOcl2Java.oclToJava(clazz, annotatedClass, oclExp) + ") == false");
            ifConstraintFails.addToThenPart("result.add(new " + UmlgGenerationUtil.UmlgConstraintViolation.getLast() + "(\"" + constraintWrapper.getName() + "\", \""
                    + clazz.getQualifiedName() + "\", \"ocl\\n" + ocl.replace("\n", "\\n") + "\\nfails!\"))");

            checkClassConstraint.getBody().addToStatements(ifConstraintFails);
            checkClassConstraint.getBody().addToLocals(result);
            checkClassConstraint.getBody().addToStatements("return result");

            annotatedClass.addToOperations(checkClassConstraint);
        }
    }

}
