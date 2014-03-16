package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.Property;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJAnnotationValue;

public class UmlgGenerationUtil {

    public static String BaseClassUmlgQualifiedName = "org::umlg::meta::BaseClassUmlg";
    public static OJPathName Parameter = new OJPathName("com.tinkerpop.blueprints.Parameter");
    public static OJPathName BASE_CLASS_UMLG = new OJPathName("org.umlg.meta.BaseClassUmlg");

    public static String umlgizeUmlName(String umlName) {
        return umlName.replace("::", "__");
    }

    public static final String ALLINSTANCES_EDGE_LABEL = "ALLINSTANCES_EDGE_LABEL";

    public static final OJPathName UmlgProperties = new OJPathName("org.umlg.runtime.util.UmlgProperties");

    public static final String rollback = "rollback";

    public final static OJPathName Pair = new OJPathName("org.umlg.runtime.util.Pair");

    public final static OJPathName UmlgAssociationClassManager = new OJPathName("org.umlg.runtime.adaptor.UmlgAssociationClassManager");
    public final static OJPathName UmlgTmpIdManager = new OJPathName("org.umlg.runtime.adaptor.UmlgTmpIdManager");
    public static final OJPathName Filter = new OJPathName("org.umlg.runtime.collection.Filter");
    public static final OJPathName UmlgIndexManager = new OJPathName("org.umlg.runtime.adaptor.UmlgIndexManager");
    public static final OJPathName UmlgMetaNodeManager = new OJPathName("org.umlg.runtime.adaptor.UmlgMetaNodeManager");
    public static final String QualifiedNameClassMapName = "qualifiedNameClassMap";

    public static final OJPathName UmlgGraph = new OJPathName("org.umlg.runtime.adaptor.UmlgGraph");
    public static final OJPathName UmlgGraphManager = new OJPathName("org.umlg.runtime.adaptor.UmlgGraphManager");
    public static final OJPathName ModelLoader = new OJPathName("org.umlg.framework.ModelLoader");
    public static final OJPathName UmlgOcl2Parser = new OJPathName("org.umlg.ocl.UmlgOcl2Parser");
    public static final OJPathName UmlgMetaNode = new OJPathName("org.umlg.runtime.domain.UmlgMetaNode");
    public static final OJPathName QualifiedNameClassMap = new OJPathName("org.umlg.model.QualifiedNameClassMap");
    public static final OJPathName UmlgAdaptorPackage = new OJPathName("org.umlg.runtime.adaptor");
    public static final OJPathName UmlgRootPackage = new OJPathName("org.umlg.model");
    public static final OJPathName UmlgSchemaMap = new OJPathName("org.umlg.runtime.adaptor.UmlgSchemaMap");
    public static final OJPathName UmlgSchemaFactory = new OJPathName("org.umlg.runtime.adaptor.UmlgSchemaFactory");
    public static final OJPathName UmlgSchemaMapImpl = new OJPathName("org.umlg.runtime.adaptor.UmlgSchemaMapImpl");
    public static final OJPathName UmlgSchemaCreatorImpl = new OJPathName("org.umlg.runtime.adaptor.UmlgSchemaCreatorImpl");
    public static final OJPathName UmlgSchemaCreator = new OJPathName("org.umlg.runtime.adaptor.UmlgSchemaCreator");
    public static final OJPathName VertexSchemaCreator = new OJPathName("org.umlg.runtime.adaptor.VertexSchemaCreator");
    public static final OJPathName EdgeSchemaCreator = new OJPathName("org.umlg.runtime.adaptor.EdgeSchemaCreator");

    public static final String QualifiedNameVertexSchemaSet = "qualifiedNameVertexSchemaSet";
    public static final String QualifiedNameEdgeSchemaSet = "qualifiedNameEdgeSchemaSet";
    public static final OJPathName UmlgApplicationNode = new OJPathName("org.umlg.runtime.domain.UmlgApplicationNode");
    public static final OJPathName UmlgEnum = new OJPathName("org.umlg.runtime.domain.UmlgEnum");
    public static final OJPathName UmlgConstraintViolationException = new OJPathName("org.umlg.runtime.validation.UmlgConstraintViolationException");
    public static final OJPathName UmlgValidator = new OJPathName("org.umlg.runtime.validation.UmlgValidator");
    public static final OJPathName UmlgConstraintViolation = new OJPathName("org.umlg.runtime.validation.UmlgConstraintViolation");
    public static final OJPathName UmlgValidation = new OJPathName("org.umlg.runtime.validation.UmlgValidation");
    public static final OJPathName DataTypeEnum = new OJPathName("org.umlg.runtime.domain.DataTypeEnum");
    public static final OJPathName BodyExpressionEvaluator = new OJPathName("org.umlg.runtime.collection.ocl.BodyExpressionEvaluator");
    public static final OJPathName BooleanExpressionEvaluator = new OJPathName("org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator");

    public final static OJPathName ObjectMapper = new OJPathName("com.fasterxml.jackson.databind.ObjectMapper");
    public final static OJPathName ObjectMapperFactory = new OJPathName("org.umlg.runtime.util.ObjectMapperFactory");
    public static final OJPathName ToJsonUtil = new OJPathName("org.umlg.runtime.domain.json.ToJsonUtil");
    public static final OJPathName umlgMemoryCollection = new OJPathName("org.umlg.runtime.collection.memory.UmlgMemoryCollection");
    public static final OJPathName umlgMemoryBag = new OJPathName("org.umlg.runtime.collection.memory.UmlgMemoryBag");
    public static final OJPathName umlgMemorySet = new OJPathName("org.umlg.runtime.collection.memory.UmlgMemorySet");
    public static final OJPathName umlgMemoryOrderedSet = new OJPathName("org.umlg.runtime.collection.memory.UmlgMemoryOrderedSet");
    public static final OJPathName umlgMemorySequence = new OJPathName("org.umlg.runtime.collection.memory.UmlgMemorySequence");

    public static final OJPathName umlgOclIsInvalidException = new OJPathName("org.umlg.runtime.domain.ocl.OclIsInvalidException");
    public static final OJPathName umlgUmlgCollections = new OJPathName("org.umlg.runtime.util.UmlgCollections");
    public static final OJPathName umlgOclStdCollectionLib = new OJPathName("org.umlg.runtime.collection.ocl.*");
    public static final OJPathName umlgMemoryCollectionLib = new OJPathName("org.umlg.runtime.collection.memory.*");
    public static final OJPathName umlgRuntimeCollectionLib = new OJPathName("org.umlg.runtime.collection.*");

    public static final OJPathName umlgUmlgSequenceClosableIterableImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgSequenceClosableIterableImpl");
    public static final OJPathName umlgUmlgSetClosableIterableImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgSetClosableIterableImpl");

    public static final OJPathName umlgRuntimePropertyPathName = new OJPathName("org.umlg.runtime.collection.UmlgRuntimeProperty");
    public static final OJPathName umlgTransactionalGraphPathName = new OJPathName("com.tinkerpop.blueprints.TransactionalGraph");
//    public static final OJPathName tinkerIndexPathName = new OJPathName("com.tinkerpop.blueprints.Index");
    public static final OJPathName umlgCloseableIterablePathName = new OJPathName("com.tinkerpop.blueprints.CloseableIterable");

    public static final OJPathName umlgBagCloseableIterablePathName = new OJPathName("org.umlg.runtime.collection.persistent.UmlgBagClosableIterableImpl");
    public static final OJPathName umlgSequenceCloseableIterablePathName = new OJPathName("org.umlg.runtime.collection.persistent.UmlgSequenceClosableIterableImpl");
    public static final OJPathName umlgSetCloseableIterablePathName = new OJPathName("org.umlg.runtime.collection.persistent.UmlgSetClosableIterableImpl");
    public static final OJPathName umlgOrderedSetCloseableIterablePathName = new OJPathName("org.umlg.runtime.collection.persistent.UmlgOrderedSetClosableIterableImpl");


    public static final OJPathName tinkerDirection = new OJPathName("com.tinkerpop.blueprints.Direction");
    public static final OJPathName AssociationClassNode = new OJPathName("org.umlg.runtime.domain.AssociationClassNode");
    public static final OJPathName umlgCompositionNodePathName = new OJPathName("org.umlg.runtime.domain.CompositionNode");
    public static final OJPathName compositionNodePathName = new OJPathName("org.umlg.runtime.domain.CompositionNode");
    public static final OJPathName BASE_TUML_AUDIT = new OJPathName("org.umlg.runtime.domain.BaseTumlAudit");
    public static final OJPathName BASE_BEHAVIORED_CLASSIFIER = new OJPathName("org.umlg.runtime.domain.BaseTinkerBehavioredClassifier");
    public static final String BASE_BEHAVIORED_CLASSIFIER_QUALIFIEDNAME = "org::umlg::runtime::domain::BaseTinkerBehavioredClassifier";
    public static final OJPathName BaseUmlgCompositionNode = new OJPathName("org.umlg.runtime.domain.BaseUmlgCompositionNode");
    public static final String BaseUmlgCompositionNodeQualifiedName = "org::umlg::runtime::domain::BaseUmlgCompositionNode";
    public static final OJPathName BASE_META_NODE = new OJPathName("org.umlg.runtime.domain.BaseMetaNode");
    public static final String PERSISTENT_CONSTRUCTOR_NAME = "persistentConstructor";
    public static final String PERSISTENT_CONSTRUCTOR_PARAM_NAME = "persistent";

    public static final String ORIGINAL_UID = "originalUid";
    public static OJPathName vertexPathName = new OJPathName("com.tinkerpop.blueprints.Vertex");
    public static OJPathName umlgFormatter = new OJPathName("org.umlg.runtime.util.UmlgFormatter");
    public static OJPathName transactionThreadVar = new OJPathName("org.umlg.runtime.adaptor.TransactionThreadVar");
    public static OJPathName transactionThreadEntityVar = new OJPathName("org.umlg.runtime.adaptor.TransactionThreadEntityVar");
    public static OJPathName transactionThreadMetaNodeVar = new OJPathName("org.umlg.runtime.adaptor.TransactionThreadMetaNodeVar");
    public static OJPathName UMLGPathName = new OJPathName("org.umlg.runtime.adaptor.UMLG");
    public static OJPathName tinkerAuditNodePathName = new OJPathName("org.umlg.runtime.domain.TinkerAuditNode");

    public static OJPathName umlgCollection = new OJPathName("org.umlg.runtime.collection.UmlgCollection");
    public static OJPathName umlgSet = new OJPathName("org.umlg.runtime.collection.UmlgSet");
    public static OJPathName umlgSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgSetImpl");
    public static OJPathName umlgPropertyAssociationClassSet = new OJPathName("org.umlg.runtime.collection.UmlgPropertyAssociationClassSet");
    public static OJPathName umlgPropertyAssociationClassSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgPropertyAssociationClassSetImpl");
    public static OJPathName umlgAssociationClassSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgAssociationClassSetImpl");
    public static OJPathName umlgQualifiedSet = new OJPathName("org.umlg.runtime.collection.UmlgQualifiedSet");
    public static OJPathName umlgQualifiedSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgQualifiedSetImpl");

    public static OJPathName umlgSequence = new OJPathName("org.umlg.runtime.collection.UmlgSequence");
    public static OJPathName umlgSequenceImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgSequenceImpl");
    public static OJPathName umlgPropertyAssociationClassSequence = new OJPathName("org.umlg.runtime.collection.UmlgPropertyAssociationClassSequence");
    public static OJPathName umlgPropertyAssociationClassSequenceImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgPropertyAssociationClassSequenceImpl");
    public static OJPathName umlgAssociationClassSequenceImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgAssociationClassSequenceImpl");


    public static OJPathName umlgQualifiedSequence = new OJPathName("org.umlg.runtime.collection.UmlgQualifiedSequence");
    public static OJPathName umlgQualifiedSequenceImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgQualifiedSequenceImpl");
    public static OJPathName umlgOrderedSet = new OJPathName("org.umlg.runtime.collection.UmlgOrderedSet");
    public static OJPathName umlgOrderedSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgOrderedSetImpl");
    public static OJPathName umlgQualifiedOrderedSet = new OJPathName("org.umlg.runtime.collection.UmlgQualifiedOrderedSet");
    public static OJPathName umlgQualifiedOrderedSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgQualifiedOrderedSetImpl");
    public static OJPathName umlgPropertyAssociationClassOrderedSet = new OJPathName("org.umlg.runtime.collection.UmlgPropertyAssociationClassOrderedSet");
    public static OJPathName umlgPropertyAssociationClassOrderedSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgPropertyAssociationClassOrderedSetImpl");
    public static OJPathName umlgAssociationClassOrderedSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgAssociationClassOrderedSetImpl");

    public static OJPathName umlgBag = new OJPathName("org.umlg.runtime.collection.UmlgBag");
    public static OJPathName umlgBagImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgBagImpl");
    public static OJPathName umlgPropertyAssociationClassBag = new OJPathName("org.umlg.runtime.collection.UmlgPropertyAssociationClassBag");
    public static OJPathName umlgPropertyAssociationClassBagImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgPropertyAssociationClassBagImpl");
    public static OJPathName umlgAssociationClassBagImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgAssociationClassBagImpl");

    public static OJPathName umlgQualifiedBag = new OJPathName("org.umlg.runtime.collection.UmlgQualifiedBag");
    public static OJPathName umlgQualifiedBagImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgQualifiedBagImpl");

    public static OJPathName edgePathName = new OJPathName("com.tinkerpop.blueprints.Edge");
    public static String UMLGAccess = "UMLG.getDb()";
    public static OJPathName UMLG_NODE = new OJPathName("org.umlg.runtime.domain.UmlgNode");
    public static OJPathName UMLG_ROOT_NODE = new OJPathName("org.umlg.runtime.domain.UmlgRootNode");

    public static OJPathName UmlgQualifierPathName = new OJPathName("org.umlg.runtime.collection.Qualifier");
    public static OJPathName umlgMultiplicityPathName = new OJPathName("org.umlg.runtime.collection.Multiplicity");

//    public static OJPathName UmlgIdUtilFactoryPathName = new OJPathName("org.umlg.runtime.adaptor.UmlgIdUtilFactory");
    public static OJPathName UmlgLabelConverterFactoryPathName = new OJPathName("org.umlg.runtime.adaptor.UmlgLabelConverterFactory");
    public static OJPathName UmlgQualifierIdFactory = new OJPathName("org.umlg.runtime.adaptor.UmlgQualifierIdFactory");

    public static String getEdgeToRootLabelStrategyMeta(org.eclipse.uml2.uml.Class clazz) {
        return "root" + UmlgClassOperations.getQualifiedName(clazz) + "Meta";
    }

    public static String getEdgeToRootLabelStrategy(org.eclipse.uml2.uml.Class clazz) {
        return "root" + UmlgClassOperations.getQualifiedName(clazz);
    }

    public static String calculateMultiplcity(MultiplicityElement multiplicityKind) {
        if (multiplicityKind.getLower() == 1 && ((multiplicityKind.getUpper() == Integer.MAX_VALUE) || (multiplicityKind.getUpper() == -1))) {
            return "Multiplicity.ONE_TO_MANY";
        } else if (multiplicityKind.getLower() == 0 && ((multiplicityKind.getUpper() == Integer.MAX_VALUE) || (multiplicityKind.getUpper() == -1))) {
            return "Multiplicity.ZERO_TO_MANY";
        } else if (multiplicityKind.getLower() == 0 && multiplicityKind.getUpper() == 1) {
            return "Multiplicity.ZERO_TO_ONE";
        } else if (multiplicityKind.getLower() == 1 && multiplicityKind.getUpper() == 1) {
            return "Multiplicity.ONE_TO_ONE";
        } else {
            throw new IllegalStateException("wtf");
        }
    }

    public static String getEdgeName(Property p) {
        boolean isControllingSide = UmlgPropertyOperations.isControllingSide(p);
        if (p.getAssociation() != null) {
            return p.getAssociation().getName();
        } else {
            // Note that the properties swap around between inverse and
            // !inverse.
            // This is to ensure that the edge on both sides has the same name.
            if (!isControllingSide) {
                return umlgizeUmlName(p.getOtherEnd().getQualifiedName() + "__" + p.getQualifiedName());
            } else {
                if (p.getOtherEnd() != null) {
                    return umlgizeUmlName(p.getQualifiedName() + "__" + p.getOtherEnd().getQualifiedName());
                } else {
                    return umlgizeUmlName(p.getName());
                }
            }
        }
    }

    public static void addOverrideAnnotation(OJAnnotatedOperation oper) {
        oper.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.Override")));
    }

    public static void addSuppressWarning(OJAnnotatedOperation put) {
        put.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.SuppressWarnings"), "unchecked"));
    }

    public static String getCollectionInterface(MultiplicityElement multiplicityElement) {
        if (multiplicityElement.isUnique() && multiplicityElement.isOrdered()) {
            return "OrderedSet";
        } else if (multiplicityElement.isUnique() && !multiplicityElement.isOrdered()) {
            return "Set";
        } else if (!multiplicityElement.isUnique() && !multiplicityElement.isOrdered()) {
            return "Bag";
        } else if (!multiplicityElement.isUnique() && multiplicityElement.isOrdered()) {
            return "Sequence";
        } else {
            throw new IllegalStateException("Not supported");
        }
    }
}
