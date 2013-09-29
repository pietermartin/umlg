package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJAnnotationValue;

public class TinkerGenerationUtil {

    public static OJPathName BASE_CLASS_TUML = new OJPathName("org.umlg.meta.BaseClassTuml");

    public static String tinkeriseUmlName(String umlName) {
        return umlName.replace("::", "__");
    }

    public static final String UmlgRootVertex = "UmlgRootVertex";

    public static final String INIT_VERTEX = "initVertex";

    public static final String TINKER_DB_NULL = "__NULL__";

    public static final String ALLINSTANCES_EDGE_LABEL = "ALLINSTANCES_EDGE_LABEL";

    public static final OJPathName UmlgProperties = new OJPathName("org.umlg.runtime.util.UmlgProperties");

    public static final String rollback = "rollback";
    public static final String transactionIdentifier = "transactionIdentifier";

    public final static OJPathName Pair = new OJPathName("org.umlg.runtime.util.Pair");

    public final static OJPathName UmlgAssociationClassManager = new OJPathName("org.umlg.runtime.adaptor.UmlgAssociationClassManager");
    public final static OJPathName UmlgTmpIdManager = new OJPathName("org.umlg.runtime.adaptor.UmlgTmpIdManager");
    public static final OJPathName Filter = new OJPathName("org.umlg.runtime.collection.Filter");
    public static final OJPathName UmlgIndexManager = new OJPathName("org.umlg.runtime.adaptor.UmlgIndexManager");
    public static final OJPathName UmlgMetaNodeManager = new OJPathName("org.umlg.runtime.adaptor.UmlgMetaNodeManager");
    public static final String QualifiedNameClassMapName = "qualifiedNameClassMap";
    public static final OJPathName UmlgGraphManager = new OJPathName("org.umlg.runtime.adaptor.UmlgGraphManager");
    public static final OJPathName ModelLoader = new OJPathName("org.umlg.framework.ModelLoader");
    public static final OJPathName UmlgOcl2Parser = new OJPathName("org.umlg.ocl.UmlgOcl2Parser");
    public static final OJPathName TumlMetaNode = new OJPathName("org.umlg.runtime.domain.TumlMetaNode");
    public static final OJPathName QualifiedNameClassMap = new OJPathName("org.umlg.root.QualifiedNameClassMap");
    public static final OJPathName UmlgAdaptorPackage = new OJPathName("org.umlg.runtime.adaptor");
    public static final OJPathName UmlgRootPackage = new OJPathName("org.umlg.root");
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
    public static final OJPathName TumlConstraintViolationException = new OJPathName("org.umlg.runtime.validation.TumlConstraintViolationException");
    public static final OJPathName TumlValidator = new OJPathName("org.umlg.runtime.validation.TumlValidator");
    public static final OJPathName TumlConstraintViolation = new OJPathName("org.umlg.runtime.validation.TumlConstraintViolation");
    public static final OJPathName TumlValidation = new OJPathName("org.umlg.runtime.validation.TumlValidation");
    public static final OJPathName DataTypeEnum = new OJPathName("org.umlg.runtime.domain.DataTypeEnum");
    public static final OJPathName BodyExpressionEvaluator = new OJPathName("org.umlg.runtime.collection.ocl.BodyExpressionEvaluator");
    public static final OJPathName BooleanExpressionEvaluator = new OJPathName("org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator");
    public static final OJPathName Root = new OJPathName("org.umlg.root.Root");
    public static final OJPathName RootRuntimePropertyEnum = new OJPathName("org.umlg.root.Root.RootRuntimePropertyEnum");

    public final static OJPathName ObjectMapper = new OJPathName("com.fasterxml.jackson.databind.ObjectMapper");
    public final static OJPathName ObjectMapperFactory = new OJPathName("org.umlg.runtime.util.ObjectMapperFactory");
    public static final OJPathName ToJsonUtil = new OJPathName("org.umlg.runtime.domain.json.ToJsonUtil");
    public static final OJPathName tumlMemoryCollection = new OJPathName("org.umlg.runtime.collection.memory.TumlMemoryCollection");
    public static final OJPathName tumlMemoryBag = new OJPathName("org.umlg.runtime.collection.memory.TumlMemoryBag");
    public static final OJPathName tumlMemorySet = new OJPathName("org.umlg.runtime.collection.memory.TumlMemorySet");
    public static final OJPathName tumlMemoryOrderedSet = new OJPathName("org.umlg.runtime.collection.memory.TumlMemoryOrderedSet");
    public static final OJPathName tumlMemorySequence = new OJPathName("org.umlg.runtime.collection.memory.TumlMemorySequence");


    public static final OJPathName tumlOclIsInvalidException = new OJPathName("org.umlg.runtime.domain.ocl.OclIsInvalidException");
    public static final OJPathName tumlTumlCollections = new OJPathName("org.umlg.runtime.util.TumlCollections");
    public static final OJPathName tumlOclStdCollectionLib = new OJPathName("org.umlg.runtime.collection.ocl.*");
    public static final OJPathName tumlMemoryCollectionLib = new OJPathName("org.umlg.runtime.collection.memory.*");
    public static final OJPathName tumlRuntimeCollectionLib = new OJPathName("org.umlg.runtime.collection.*");

    public static final OJPathName tumlTinkerSequenceClosableIterableImpl = new OJPathName("org.umlg.runtime.collection.persistent.TinkerSequenceClosableIterableImpl");
    public static final OJPathName tumlTinkerSetClosableIterableImpl = new OJPathName("org.umlg.runtime.collection.persistent.TinkerSetClosableIterableImpl");

    public static final OJPathName tumlRuntimePropertyPathName = new OJPathName("org.umlg.runtime.collection.TumlRuntimeProperty");
    public static final OJPathName tinkerTransactionalGraphPathName = new OJPathName("com.tinkerpop.blueprints.TransactionalGraph");
//    public static final OJPathName tinkerIndexPathName = new OJPathName("com.tinkerpop.blueprints.Index");
    public static final OJPathName tinkerCloseableIterablePathName = new OJPathName("com.tinkerpop.blueprints.CloseableIterable");

    public static final OJPathName tumlBagCloseableIterablePathName = new OJPathName("org.umlg.runtime.collection.persistent.TinkerBagClosableIterableImpl");
    public static final OJPathName tumlSequenceCloseableIterablePathName = new OJPathName("org.umlg.runtime.collection.persistent.TinkerSequenceClosableIterableImpl");
    public static final OJPathName tumlSetCloseableIterablePathName = new OJPathName("org.umlg.runtime.collection.persistent.TinkerSetClosableIterableImpl");
    public static final OJPathName tumlOrderedSetCloseableIterablePathName = new OJPathName("org.umlg.runtime.collection.persistent.TinkerOrderedSetClosableIterableImpl");


    public static final OJPathName tinkerDirection = new OJPathName("com.tinkerpop.blueprints.Direction");
    public static final OJPathName AssociationClassNode = new OJPathName("org.umlg.runtime.domain.AssociationClassNode");
    public static final OJPathName tinkerCompositionNodePathName = new OJPathName("org.umlg.runtime.domain.CompositionNode");
    public static final OJPathName compositionNodePathName = new OJPathName("org.umlg.runtime.domain.CompositionNode");
    public static final OJPathName tinkerIdUtilPathName = new OJPathName("org.umlg.runtime.adaptor.TinkerIdUtil");
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
    public static OJPathName tumlFormatter = new OJPathName("org.umlg.runtime.util.TumlFormatter");
    public static OJPathName transactionThreadVar = new OJPathName("org.umlg.runtime.adaptor.TransactionThreadVar");
    public static OJPathName transactionThreadEntityVar = new OJPathName("org.umlg.runtime.adaptor.TransactionThreadEntityVar");
    public static OJPathName transactionThreadMetaNodeVar = new OJPathName("org.umlg.runtime.adaptor.TransactionThreadMetaNodeVar");
    public static OJPathName graphDbPathName = new OJPathName("org.umlg.runtime.adaptor.GraphDb");
    public static OJPathName tinkerAuditNodePathName = new OJPathName("org.umlg.runtime.domain.TinkerAuditNode");

    public static OJPathName tinkerCollection = new OJPathName("org.umlg.runtime.collection.TinkerCollection");
    public static OJPathName tinkerSet = new OJPathName("org.umlg.runtime.collection.TinkerSet");
    public static OJPathName tinkerSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.TinkerSetImpl");
    public static OJPathName umlgPropertyAssociationClassSet = new OJPathName("org.umlg.runtime.collection.UmlgPropertyAssociationClassSet");
    public static OJPathName umlgPropertyAssociationClassSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgPropertyAssociationClassSetImpl");
    public static OJPathName umlgAssociationClassSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgAssociationClassSetImpl");
    public static OJPathName tinkerQualifiedSet = new OJPathName("org.umlg.runtime.collection.TinkerQualifiedSet");
    public static OJPathName tinkerQualifiedSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.TinkerQualifiedSetImpl");

    public static OJPathName tinkerSequence = new OJPathName("org.umlg.runtime.collection.TinkerSequence");
    public static OJPathName tinkerSequenceImpl = new OJPathName("org.umlg.runtime.collection.persistent.TinkerSequenceImpl");
    public static OJPathName umlgPropertyAssociationClassSequence = new OJPathName("org.umlg.runtime.collection.UmlgPropertyAssociationClassSequence");
    public static OJPathName umlgPropertyAssociationClassSequenceImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgPropertyAssociationClassSequenceImpl");
    public static OJPathName umlgAssociationClassSequenceImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgAssociationClassSequenceImpl");


    public static OJPathName tinkerQualifiedSequence = new OJPathName("org.umlg.runtime.collection.TinkerQualifiedSequence");
    public static OJPathName tinkerQualifiedSequenceImpl = new OJPathName("org.umlg.runtime.collection.persistent.TinkerQualifiedSequenceImpl");
    public static OJPathName tinkerOrderedSet = new OJPathName("org.umlg.runtime.collection.TinkerOrderedSet");
    public static OJPathName tinkerOrderedSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.TinkerOrderedSetImpl");
    public static OJPathName tinkerQualifiedOrderedSet = new OJPathName("org.umlg.runtime.collection.TinkerQualifiedOrderedSet");
    public static OJPathName tinkerQualifiedOrderedSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.TinkerQualifiedOrderedSetImpl");
    public static OJPathName umlgPropertyAssociationClassOrderedSet = new OJPathName("org.umlg.runtime.collection.UmlgPropertyAssociationClassOrderedSet");
    public static OJPathName umlgPropertyAssociationClassOrderedSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgPropertyAssociationClassOrderedSetImpl");
    public static OJPathName umlgAssociationClassOrderedSetImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgAssociationClassOrderedSetImpl");

    public static OJPathName tinkerBag = new OJPathName("org.umlg.runtime.collection.TinkerBag");
    public static OJPathName tinkerBagImpl = new OJPathName("org.umlg.runtime.collection.persistent.TinkerBagImpl");
    public static OJPathName umlgPropertyAssociationClassBag = new OJPathName("org.umlg.runtime.collection.UmlgPropertyAssociationClassBag");
    public static OJPathName umlgPropertyAssociationClassBagImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgPropertyAssociationClassBagImpl");
    public static OJPathName umlgAssociationClassBagImpl = new OJPathName("org.umlg.runtime.collection.persistent.UmlgAssociationClassBagImpl");

    public static OJPathName tinkerQualifiedBag = new OJPathName("org.umlg.runtime.collection.TinkerQualifiedBag");
    public static OJPathName tinkerQualifiedBagImpl = new OJPathName("org.umlg.runtime.collection.persistent.TinkerQualifiedBagImpl");

    public static OJPathName edgePathName = new OJPathName("com.tinkerpop.blueprints.Edge");
    public static String graphDbAccess = "GraphDb.getDb()";
    public static OJPathName UMLG_NODE = new OJPathName("org.umlg.runtime.domain.UmlgNode");
    public static OJPathName UMLG_ROOT_NODE = new OJPathName("org.umlg.runtime.domain.UmlgRootNode");

    public static OJPathName UmlgQualifierPathName = new OJPathName("org.umlg.runtime.collection.Qualifier");
    public static OJPathName umlgMultiplicityPathName = new OJPathName("org.umlg.runtime.collection.Multiplicity");

//    public static OJPathName UmlgIdUtilFactoryPathName = new OJPathName("org.umlg.runtime.adaptor.UmlgIdUtilFactory");
    public static OJPathName UmlgLabelConverterFactoryPathName = new OJPathName("org.umlg.runtime.adaptor.UmlgLabelConverterFactory");
    public static OJPathName UmlgQualifierIdFactory = new OJPathName("org.umlg.runtime.adaptor.UmlgQualifierIdFactory");

    public static String getEdgeToRootLabelStrategyMeta(org.eclipse.uml2.uml.Class clazz) {
        return "root" + TumlClassOperations.getQualifiedName(clazz) + "Meta";
    }

    public static String getEdgeToRootLabelStrategy(org.eclipse.uml2.uml.Class clazz) {
        return "root" + TumlClassOperations.getQualifiedName(clazz);
    }

    public static String umlPrimitiveTypeToJava(Type type) {
        if (type instanceof PrimitiveType) {
            PrimitiveType primitiveType = (PrimitiveType) type;
            if (primitiveType.getName().equals("String")) {
                return "String";
            } else if (primitiveType.getName().equals("Integer")) {
                return "Integer";
            } else if (primitiveType.getName().equals("Boolean")) {
                return "Boolean";
            } else if (primitiveType.getName().equals("UnlimitedNatural")) {
                return "Long";
            } else {
                throw new IllegalStateException("unknown primitive " + primitiveType.getName());
            }
        } else {
            return type.getName();
        }
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
        boolean isControllingSide = TumlPropertyOperations.isControllingSide(p);
        if (p.getAssociation() != null) {
            return p.getAssociation().getName();
        } else {
            // Note that the properties swap around between inverse and
            // !inverse.
            // This is to ensure that the edge on both sides has the same name.
            if (!isControllingSide) {
                return tinkeriseUmlName(p.getOtherEnd().getQualifiedName() + "__" + p.getQualifiedName());
            } else {
                if (p.getOtherEnd() != null) {
                    return tinkeriseUmlName(p.getQualifiedName() + "__" + p.getOtherEnd().getQualifiedName());
                } else {
//                    return tinkeriseUmlName(p.getQualifiedName());
                    return tinkeriseUmlName(p.getName());
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
