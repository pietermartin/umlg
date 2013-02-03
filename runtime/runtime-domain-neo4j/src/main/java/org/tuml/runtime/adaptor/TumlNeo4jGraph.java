package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jEdge;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jVertex;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.tuml.runtime.domain.PersistentObject;

import javax.transaction.*;
import javax.transaction.Transaction;
import java.util.*;

/**
 * Date: 2013/01/09
 * Time: 8:09 PM
 */
public class TumlNeo4jGraph extends Neo4jGraph implements TumlGraph {

    private TransactionEventHandler<PersistentObject> transactionEventHandler;
    private Map<TransactionIdentifier, Transaction> transactionIdentifierTransactionMap;
    private Map<Transaction, TransactionIdentifier> transactionTransactionIdentifierMap;

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
        return (Integer) getRoot().getProperty("transactionCount");
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
            if ((relationship.getStartNode().equals(n1) && relationship.getEndNode().equals(n2))
                    || (relationship.getStartNode().equals(n2) && relationship.getEndNode().equals(n1))) {

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
        return ((EmbeddedGraphDatabase) this.getRawGraph()).getNodeManager().getNumberOfIdsInUse(Node.class) - 1;
    }

    @Override
    public long countEdges() {
        return ((EmbeddedGraphDatabase) this.getRawGraph()).getNodeManager().getNumberOfIdsInUse(Relationship.class);
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
            // TODO reimplement schemaHelper
            String className = (String) v.getProperty("className");
            Class<?> c = Class.forName(className);
            // Class<?> c = schemaHelper.getClassNames().get((String)
            // v.getProperty("className"));
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
    public void resume(TransactionIdentifier transactionIdentifier) {
        GraphDatabaseAPI graphDatabaseAPI = (GraphDatabaseAPI) getRawGraph();
        TransactionManager transactionManager = graphDatabaseAPI.getTxManager();
        try {
            transactionManager.resume(this.transactionIdentifierTransactionMap.get(transactionIdentifier));
        } catch (InvalidTransactionException e) {
            throw new RuntimeException(e);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TransactionIdentifier suspend() {
        this.autoStartTransaction();
        GraphDatabaseAPI graphDatabaseAPI = (GraphDatabaseAPI) getRawGraph();
        TransactionManager transactionManager = graphDatabaseAPI.getTxManager();
        try {
            Transaction t = transactionManager.suspend();
            TransactionIdentifier transactionIdentifier = this.transactionTransactionIdentifierMap.get(t);
            return transactionIdentifier;
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setRollbackOnly() {
        GraphDatabaseAPI graphDatabaseAPI = (GraphDatabaseAPI) getRawGraph();
        TransactionManager transactionManager = graphDatabaseAPI.getTxManager();
        try {
            transactionManager.setRollbackOnly();
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
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
            tx.get().finish();
            tx.remove();
            TransactionIdentifier transactionIdentifier = this.transactionTransactionIdentifierMap.remove(t);
            this.transactionIdentifierTransactionMap.remove(transactionIdentifier);

        }
    }

    @Override
    public void commit() {
        if (null == tx.get()) {
            return;
        }

        try {
            tx.get().success();
        } finally {
            tx.get().finish();
            tx.remove();
            GraphDatabaseAPI graphDatabaseAPI = (GraphDatabaseAPI) getRawGraph();
            TransactionManager transactionManager = graphDatabaseAPI.getTxManager();
            try {
                javax.transaction.Transaction t = transactionManager.getTransaction();
                TransactionIdentifier transactionIdentifier = this.transactionTransactionIdentifierMap.remove(t);
                this.transactionIdentifierTransactionMap.remove(transactionIdentifier);
            } catch (SystemException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T extends Element> TumlTinkerIndex<T> createIndex(String indexName, Class<T> indexClass) {
        return new TumlNeo4jIndex(super.createIndex(indexName, indexClass));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T extends Element> TumlTinkerIndex<T> getIndex(String indexName, Class<T> indexClass) {
        Index<T> index = super.getIndex(indexName, indexClass);
        if (index != null) {
            return new TumlNeo4jIndex(index);
        } else {
            return null;
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

}
