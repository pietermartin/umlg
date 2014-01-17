package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jEdge;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jVertex;
import org.apache.commons.io.FileUtils;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.impl.util.StringLogger;
import org.umlg.runtime.domain.UmlgNode;
import org.umlg.runtime.util.UmlgProperties;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

/**
 * Date: 2013/01/09
 * Time: 8:09 PM
 */
public class UmlgNeo4jGraph extends Neo4jGraph implements UmlgGraph {

    private UmlgTransactionEventHandler transactionEventHandler;
    private static final Logger logger = Logger.getLogger(UmlgNeo4jGraph.class.getPackage().getName());

    public UmlgNeo4jGraph(String directory) {
        super(directory);
        this.transactionEventHandler = new UmlgTransactionEventHandlerImpl();
    }

    /** Generic for all graphs start */
    @Override
    public void incrementTransactionCount() {
        this.getRoot().setProperty("transactionCount", (Integer) this.getRoot().getProperty("transactionCount") + 1);
    }

    @Override
    public long getTransactionCount() {
        return this.getRoot().getProperty("transactionCount");
    }

    @Override
    public void addRoot() {
        Vertex root = addVertex(ROOT_VERTEX);
        root.setProperty("transactionCount", 1);
        root.setProperty("className", ROOT_CLASS_NAME);
    }

    @Override
    public void commit() {
        try {
            //This null check is here for when a graph is created. It calls commit before the listener has been set.
            if (this.transactionEventHandler != null) {
                this.transactionEventHandler.beforeCommit();
            }
            super.commit();
        } finally {
            TransactionThreadEntityVar.remove();
            TransactionThreadMetaNodeVar.remove();
        }
    }

    @Override
    public void rollback() {
        try {
            super.rollback();
        } finally {
            TransactionThreadEntityVar.remove();
            TransactionThreadMetaNodeVar.remove();
        }
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
    public void addDeletionNode() {
        Vertex v = addVertex(DELETION_VERTEX);
        addEdge(null, getRoot(), v, DELETED_VERTEX_EDGE);
    }

    private Vertex getDeletionVertex() {
        if (getRoot() != null && getRoot().getEdges(Direction.OUT, DELETED_VERTEX_EDGE).iterator().hasNext()) {
            return getRoot().getEdges(Direction.OUT, DELETED_VERTEX_EDGE).iterator().next().getVertex(Direction.IN);
        } else {
            return null;
        }
    }

    @Override
    public <T> T instantiateClassifier(Object id) {
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

    /** Generic for all graphs end */

    @Override
    public String executeQuery(UmlgQueryEnum umlgQueryEnum, Object contextId, String query) {

        switch (umlgQueryEnum) {
            case OCL:
                try {
                    Class<?> tumlOclExecutor = Class.forName("org.umlg.ocl.TumlOclExecutor");
                    Method method = tumlOclExecutor.getMethod("executeOclQueryToJson", String.class, UmlgNode.class, String.class);
                    UmlgNode context = (UmlgNode) GraphDb.getDb().instantiateClassifier(contextId);
                    String json = (String) method.invoke(null, context.getQualifiedName(), context, query);
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
                ExecutionEngine engine = new ExecutionEngine(getRawGraph(), StringLogger.SYSTEM);
                ExecutionResult executionResult = engine.execute(query);
                result = executionResult.dumpToString();
                return result;
        }

        throw new RuntimeException("Unknown query enum");

    }

    @Override
    public void drop() {
        UmlgGraphManager.INSTANCE.deleteGraph();
    }

    @Override
    public Vertex getRoot() {
        return this.getVertex(1L);
    }

    @Override
    public void removeVertex(final Vertex vertex) {
        this.autoStartTransaction();
        Iterable<Edge> edges = vertex.getEdges(Direction.BOTH);
        for (final Edge edge : edges) {
            edge.remove();
        }
        if (!vertex.getId().equals(new Long(0))) {
            getDeletionVertex().addEdge(DELETION_VERTEX, vertex);
            vertex.setProperty("deleted", true);
        } else {
            super.removeVertex(vertex);
        }
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
            if (relationship.getEndNode().equals(n2) || relationship.getStartNode().equals(n2)) {
                result.add(this.getEdge(relationship.getId()));
            }
        }
        return result;
    }

    @Override
    public long countVertices() {
        int countDeletedNodes = 0;
        for (Edge edge : getDeletionVertex().getEdges(com.tinkerpop.blueprints.Direction.OUT, DELETION_VERTEX)) {
            countDeletedNodes++;
        }
        return ((EmbeddedGraphDatabase) this.getRawGraph()).getNodeManager().getNumberOfIdsInUse(Node.class) - 2 - countDeletedNodes;
    }

    @Override
    public long countEdges() {
        int countDeletedNodes = 0;
        for (Edge edge : getDeletionVertex().getEdges(com.tinkerpop.blueprints.Direction.OUT, DELETION_VERTEX)) {
            countDeletedNodes++;
        }
        return ((EmbeddedGraphDatabase) this.getRawGraph()).getNodeManager().getNumberOfIdsInUse(Relationship.class) - 1 - countDeletedNodes;
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
    }

    @Override
    public boolean isTransactionActive() {
        return  (tx.get() != null);
    }

    @Override
    public void afterThreadContext() {
//        if (TransactionThreadEntityVar.get()!=null && TransactionThreadEntityVar.get().size()>0) {
//            throw new RuntimeException("wtf");
//        }
//        if (TransactionThreadMetaNodeVar.get()!=null && TransactionThreadMetaNodeVar.get().size()>0) {
//            throw new RuntimeException("wtf");
//        }
        TransactionThreadEntityVar.remove();
        TransactionThreadMetaNodeVar.remove();
        UmlgAssociationClassManager.remove();
        GraphDb.remove();
    }

}
