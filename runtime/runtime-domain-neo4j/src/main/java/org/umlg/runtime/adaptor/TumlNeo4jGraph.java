package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jEdge;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jVertex;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.kernel.impl.util.StringLogger;
import org.umlg.runtime.domain.PersistentObject;
import org.umlg.runtime.domain.TumlMetaNode;
import org.umlg.runtime.domain.TumlNode;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

/**
 * Date: 2013/01/09
 * Time: 8:09 PM
 */
public class TumlNeo4jGraph extends Neo4jGraph implements TumlGraph {

    private TransactionEventHandler<PersistentObject> transactionEventHandler;
    private Map<TransactionIdentifier, Transaction> transactionIdentifierTransactionMap;
    private Map<Transaction, TransactionIdentifier> transactionTransactionIdentifierMap;
    private static final Logger logger = Logger.getLogger(TumlNeo4jGraph.class.getPackage().getName());
    private final String DELETED_NODES = "deletednodes";

    public TumlNeo4jGraph(String directory) {
        super(directory);
        this.transactionIdentifierTransactionMap = new HashMap<TransactionIdentifier, Transaction>();
        this.transactionTransactionIdentifierMap = new HashMap<Transaction, TransactionIdentifier>();
    }

    public TumlNeo4jGraph(GraphDatabaseService rawGraph) {
        super(rawGraph);
        this.transactionIdentifierTransactionMap = new HashMap<TransactionIdentifier, Transaction>();
        this.transactionTransactionIdentifierMap = new HashMap<Transaction, TransactionIdentifier>();
    }

    public TumlNeo4jGraph(GraphDatabaseService rawGraph, boolean fresh) {
        super(rawGraph, fresh);
        this.transactionIdentifierTransactionMap = new HashMap<TransactionIdentifier, Transaction>();
        this.transactionTransactionIdentifierMap = new HashMap<Transaction, TransactionIdentifier>();
    }

    public TumlNeo4jGraph(String directory, Map<String, String> configuration) {
        super(directory, configuration);
        this.transactionIdentifierTransactionMap = new HashMap<TransactionIdentifier, Transaction>();
        this.transactionTransactionIdentifierMap = new HashMap<Transaction, TransactionIdentifier>();
    }

    @Override
    public void incrementTransactionCount() {
        getRoot().setProperty("transactionCount", (Integer) getRoot().getProperty("transactionCount") + 1);
    }

    @Override
    public long getTransactionCount() {
        return getRoot().getProperty("transactionCount");
    }

    @Override
    public Vertex getRoot() {
        return this.getVertex(1L);
    }

    @Override
    public Vertex addVertex(String className) {
        Vertex v = super.addVertex(null);
        if (className != null) {
            v.setProperty("className", className);
        }
        return v;
    }

    @Override
    public Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels) {
        Node n1 = ((Neo4jVertex) v1).getRawVertex();
        Node n2 = ((Neo4jVertex) v2).getRawVertex();
        List<DynamicRelationshipType> dynaRel = new ArrayList<DynamicRelationshipType>(labels.length);
        for (String label : labels) {
            dynaRel.add(DynamicRelationshipType.withName(label));
        }
        Set<Edge> result = new HashSet<Edge>(dynaRel.size());
        Iterable<Relationship> relationships = n1.getRelationships(dynaRel.toArray(new DynamicRelationshipType[]{}));
        for (Relationship relationship : relationships) {
            if ((/*relationship.getStartNode().equals(n1) && */relationship.getEndNode().equals(n2))
                    || (relationship.getStartNode().equals(n2) /*&& relationship.getEndNode().equals(n1)*/)) {

                result.add(this.getEdge(relationship.getId()));
            }
        }
        return result;
    }

    @Override
    public void addRoot() {
        try {
            this.getRawGraph().getNodeById(1);
        } catch (NotFoundException e) {
            try {
                ((EmbeddedGraphDatabase) this.getRawGraph()).getTxManager().begin();
                ((EmbeddedGraphDatabase) this.getRawGraph()).getNodeManager().setReferenceNodeId(this.getRawGraph().createNode().getId());
                ((EmbeddedGraphDatabase) this.getRawGraph()).getTxManager().commit();
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
            Vertex root = getRoot();
            root.setProperty("transactionCount", 1);
            root.setProperty("className", ROOT_CLASS_NAME);
        }
    }

    @Override
    public long countVertices() {
        int countDeletedNodes = 0;
        for (Edge edge : getRoot().getEdges(com.tinkerpop.blueprints.Direction.OUT, DELETED_NODES)) {
            countDeletedNodes++;
        }
        return ((EmbeddedGraphDatabase) this.getRawGraph()).getNodeManager().getNumberOfIdsInUse(Node.class) - 1 - countDeletedNodes;
    }

    @Override
    public long countEdges() {
        int countDeletedNodes = 0;
        for (Edge edge : getRoot().getEdges(com.tinkerpop.blueprints.Direction.OUT, DELETED_NODES)) {
            countDeletedNodes++;
        }
        return ((EmbeddedGraphDatabase) this.getRawGraph()).getNodeManager().getNumberOfIdsInUse(Relationship.class) - countDeletedNodes;
    }

    @Override
    public void registerListeners() {
        if (this.transactionEventHandler == null) {
            this.transactionEventHandler = new TumlTransactionEventHandler<PersistentObject>();
            this.getRawGraph().registerTransactionEventHandler(this.transactionEventHandler);
        }
    }

    @Override
    public <T> T instantiateClassifier(Long id) {
        try {
            Vertex v = this.getVertex(id);
            if (v == null) {
                throw new RuntimeException(String.format("No vertex found for id %d", new Object[]{id}));
            }
            // TODO reimplement schemaHelper
            String className = v.getProperty("className");
            Class<?> c = Class.forName(className);
            return (T) c.getConstructor(Vertex.class).newInstance(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void autoStartTransaction() {
        if (tx.get() == null) {
            tx.set(this.getRawGraph().beginTx());
            GraphDatabaseAPI graphDatabaseAPI = (GraphDatabaseAPI) getRawGraph();
            TransactionManager transactionManager = graphDatabaseAPI.getTxManager();
            try {
                Transaction t = transactionManager.getTransaction();
                TransactionIdentifier transactionIdentifier = new TransactionIdentifier();
                if (this.transactionIdentifierTransactionMap == null) {
                    this.transactionIdentifierTransactionMap = new HashMap<TransactionIdentifier, Transaction>();
                }
                if (this.transactionTransactionIdentifierMap == null) {
                    this.transactionTransactionIdentifierMap = new HashMap<Transaction, TransactionIdentifier>();
                }
                this.transactionIdentifierTransactionMap.put(transactionIdentifier, t);
                this.transactionTransactionIdentifierMap.put(t, transactionIdentifier);
            } catch (SystemException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void rollback() {
        try {
            if (null == tx.get()) {
                return;
            }
            GraphDatabaseAPI graphDatabaseAPI = (GraphDatabaseAPI) getRawGraph();
            TransactionManager transactionManager = graphDatabaseAPI.getTxManager();
            javax.transaction.Transaction t = null;
            try {
                t = transactionManager.getTransaction();
                if (t == null || t.getStatus() == Status.STATUS_ROLLEDBACK) {
                    return;
                }
                tx.get().failure();
            } catch (SystemException e) {
                throw new RuntimeException(e);
            } finally {
                TransactionIdentifier transactionIdentifier = this.transactionTransactionIdentifierMap.remove(t);
                this.transactionIdentifierTransactionMap.remove(transactionIdentifier);
                tx.get().finish();
                tx.remove();
            }
            //Persist the highId of the MetaNode
            for (TumlMetaNode tumlMetaNode : TransactionThreadMetaNodeVar.get()) {
                TumlIdManager.INSTANCE.persistHighId(tumlMetaNode);
            }
            GraphDb.getDb().commit();
        } finally {
            TransactionThreadEntityVar.remove();
            TransactionThreadMetaNodeVar.remove();
        }
    }

    @Override
    public void commit() {
        try {
            if (null == tx.get()) {
                return;
            }
            try {
                //Persist the highId of the MetaNode
                for (TumlMetaNode tumlMetaNode : TransactionThreadMetaNodeVar.get()) {
                    TumlIdManager.INSTANCE.persistHighId(tumlMetaNode);
                }
                tx.get().success();
            } finally {
                GraphDatabaseAPI graphDatabaseAPI = (GraphDatabaseAPI) getRawGraph();
                TransactionManager transactionManager = graphDatabaseAPI.getTxManager();
                try {
                    javax.transaction.Transaction t = transactionManager.getTransaction();
                    TransactionIdentifier transactionIdentifier = this.transactionTransactionIdentifierMap.remove(t);
                    this.transactionIdentifierTransactionMap.remove(transactionIdentifier);
                } catch (SystemException e) {
                    throw new RuntimeException(e);
                }
                tx.get().finish();
                tx.remove();
            }
        } finally {
            TransactionThreadEntityVar.remove();
            TransactionThreadMetaNodeVar.remove();
        }
    }

    @Override
    public boolean hasEdgeBeenDeleted(Edge edge) {
        Neo4jEdge neo4jEdge = (Neo4jEdge) edge;
        try {
            neo4jEdge.getRawEdge().hasProperty("asd");
            return false;
        } catch (Exception e) {
            return true;
        }
        // The way below requires a transaction to have been started.
        // Neo4jEdge neo4jEdge = (Neo4jEdge) edge;
        // EmbeddedGraphDatabase g =
        // (EmbeddedGraphDatabase)this.neo4jGraph.getRawGraph();
        // for (Relationship r :
        // g.getNodeManager().getTransactionData().deletedRelationships()) {
        // if (neo4jEdge.getRawEdge().equals(r)) {
        // return true;
        // }
        // }
        // return false;
    }

    @Override
    public void clearTxThreadVar() {
        if (tx.get() != null) {
            logger.warning("Transaction threadvar was not empty!!!!! Bug somewhere in clearing the transaction!!!");
            rollback();
            throw new IllegalStateException("Transaction thread var is not empty!!!");
        }
    }

    @Override
    public void clearThreadVars() {
//        if (TransactionThreadEntityVar.get()!=null && TransactionThreadEntityVar.get().size()>0) {
//            throw new RuntimeException("wtf");
//        }
//        if (TransactionThreadMetaNodeVar.get()!=null && TransactionThreadMetaNodeVar.get().size()>0) {
//            throw new RuntimeException("wtf");
//        }
        TransactionThreadEntityVar.remove();
        TransactionThreadMetaNodeVar.remove();
        UmlgAssociationClassManager.remove();
    }

    @Override
    public void addDeletionNode() {
        Vertex v = addVertex(null);
        addEdge(null, getRoot(), v, DELETED_NODES);
    }

    @Override
    public String executeQuery(TumlQueryEnum tumlQueryEnum, Long contextId, String query) {

        switch (tumlQueryEnum) {
            case OCL:
                try {
                    Class<?> tumlOclExecutor = Class.forName("org.umlg.ocl.TumlOclExecutor");
                    Method method = tumlOclExecutor.getMethod("executeOclQueryToJson", String.class, TumlNode.class, String.class);
                    TumlNode context = (TumlNode)GraphDb.getDb().instantiateClassifier(contextId);
                    String json = (String)method.invoke(null, context.getQualifiedName(), context, query);
                    return json;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("TumlOclExecutor is not on the class path.");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            case GREMLIN:
                String result;
                if (contextId != null) {
                    result = GremlinExecutor.executeGremlinViaGroovy(contextId, query);
                } else {
                    result = GremlinExecutor.executeGremlinViaGroovy(null, query);
                }
                return result;
            case NATIVE:
                ExecutionEngine engine = new ExecutionEngine( getRawGraph(), StringLogger.SYSTEM );
                ExecutionResult executionResult = engine.execute(query);
                result = executionResult.dumpToString();
                return result;
        }

        throw new RuntimeException("Unknown query enum");

    }

    @Override
    public void removeVertex(final Vertex vertex) {
        this.autoStartTransaction();
        final Node node = ((Neo4jVertex) vertex).getRawVertex();
        for (final Relationship relationship : node.getRelationships(org.neo4j.graphdb.Direction.BOTH)) {
            relationship.delete();
        }
        if (!vertex.getId().equals(new Long(0))) {
            getRoot().addEdge(DELETED_NODES, vertex);
            vertex.setProperty("deleted", true);
        } else {
            node.delete();
        }
    }

}
