package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Edge;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.kernel.impl.core.NodeManager;
import org.neo4j.kernel.impl.util.StringLogger;
import org.umlg.runtime.collection.memory.UmlgLazyList;
import org.umlg.runtime.domain.PersistentObject;
import org.umlg.runtime.domain.UmlgApplicationNode;
import org.umlg.runtime.domain.UmlgNode;
import org.umlg.runtime.util.UmlgProperties;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

/**
 * Date: 2013/01/09
 * Time: 8:09 PM
 */
public class UmlgNeo4jGraph extends Neo4j2Graph implements UmlgGraph, UmlgAdminGraph {

    private UmlgTransactionEventHandler transactionEventHandler;
    private static final Logger logger = Logger.getLogger(UmlgNeo4jGraph.class.getPackage().getName());
    private Class<UmlgApplicationNode> umlgApplicationNodeClass;

    public UmlgNeo4jGraph(String directory) {
        super(directory);
        setCheckElementsInTransaction(true);
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
    public <T extends PersistentObject> T getEntity(Object id) {
        try {
            Vertex v = this.getVertex(id);
            if (v == null) {
                throw new RuntimeException(String.format("No vertex found for id %d", new Object[]{id}));
            }
            return instantiateClassifier(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T instantiateClassifier(Vertex v) {
        try {
            // TODO reimplement schemaHelper
            String className = v.getProperty("className");
            Class<?> c = Class.forName(className);
            return (T) c.getConstructor(Vertex.class).newInstance(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PersistentObject getFromUniqueIndex(String indexKey, Object indexValue) {
        Iterator<Vertex> iterator = query().has(indexKey, indexValue).vertices().iterator();
        if ( iterator.hasNext() ) {
            return instantiateClassifier(iterator.next());
        } else {
            return null;
        }
    }

    @Override
    public List<PersistentObject> getFromIndex(String indexKey, Object indexValue) {
        final Iterator<Vertex> iterator = query().has(indexKey, indexValue).vertices().iterator();
        List<PersistentObject> lazy = new UmlgLazyList(iterator);
        return lazy;
    }

    /* Generic for all graphs end */

    @Override
    public String executeQueryToString(UmlgQueryEnum umlgQueryEnum, Object contextId, String query) {

        switch (umlgQueryEnum) {
            case OCL:
                try {
                    Class<?> umlgOclExecutor = Class.forName("org.umlg.ocl.UmlgOclExecutor");
                    Method method = umlgOclExecutor.getMethod("executeOclQueryAsJson", Object.class, String.class);
                    String result = (String)method.invoke(null, contextId, query);
                    return result;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("UmlgOclExecutor is not on the class path.");
                } catch (Exception e) {
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException)e;
                    } else if (e instanceof InvocationTargetException) {
                        Throwable target = ((InvocationTargetException) e).getTargetException();
                        if (target instanceof RuntimeException) {
                            throw (RuntimeException)target;
                        } else {
                            throw new RuntimeException(target);
                        }
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            case GROOVY:
                String result;
                if (contextId != null) {
                    result = GroovyExecutor.INSTANCE.executeGroovyAsString(contextId, query);
                } else {
                    result = GroovyExecutor.INSTANCE.executeGroovyAsString(null, query);
                }
                return result;
            case NATIVE:
                ExecutionEngine engine = new ExecutionEngine(getRawGraph(), StringLogger.SYSTEM);
                ExecutionResult executionResult = engine.execute(query);
                result = executionResult.dumpToString();
                return result;
            default:
                throw new RuntimeException("Unknown query enum");
        }
    }

    @Override
    public <T> T executeQuery(UmlgQueryEnum umlgQueryEnum, Object contextId, String query) {

        switch (umlgQueryEnum) {
            case OCL:
                try {
                    Class<?> umlgOclExecutor = Class.forName("org.umlg.ocl.UmlgOclExecutor");
                    Method method = umlgOclExecutor.getMethod("executeOclQuery", Object.class, String.class);
                    Object result = method.invoke(null, contextId, query);
                    return (T)result;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("UmlgOclExecutor is not on the class path.");
                } catch (Exception e) {
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException)e;
                    } else if (e instanceof InvocationTargetException) {
                        Throwable target = ((InvocationTargetException) e).getTargetException();
                        if (target instanceof RuntimeException) {
                            throw (RuntimeException)target;
                        } else {
                            throw new RuntimeException(target);
                        }
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            case GROOVY:
                Object result;
                if (contextId != null) {
                    result = GroovyExecutor.INSTANCE.executeGroovy(contextId, query);
                } else {
                    result = GroovyExecutor.INSTANCE.executeGroovy(null, query);
                }
                return (T)result;
            case NATIVE:
                ExecutionEngine engine = new ExecutionEngine(getRawGraph(), StringLogger.SYSTEM);
                ExecutionResult executionResult = engine.execute(query);
                return (T)executionResult;
            default:
                throw new RuntimeException("Unknown query enum");
        }
    }

    @Override
    public void drop() {
        UmlgGraphManager.INSTANCE.deleteGraph();
    }

    @Override
    public Vertex getRoot() {
        return this.getVertex(0L);
    }

    @Override
    public UmlgApplicationNode getUmlgApplicationNode() {
        try {
            if (this.umlgApplicationNodeClass == null) {
                this.umlgApplicationNodeClass = (Class<UmlgApplicationNode>) Thread.currentThread().getContextClassLoader().loadClass(UmlgProperties.INSTANCE.getModelJavaName());
            }
            return (UmlgApplicationNode) this.umlgApplicationNodeClass.getField("INSTANCE").get(null);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeVertex(final Vertex vertex) {
        this.autoStartTransaction(true);
        Iterable<Edge> edges = vertex.getEdges(Direction.BOTH);
        for (final Edge edge : edges) {
            edge.remove();
        }
        if (!vertex.getId().equals(new Long(0))) {
            getDeletionVertex().addEdge(DELETION_VERTEX, vertex);
            for (String key : vertex.getPropertyKeys()) {
                vertex.removeProperty(key);
            }
            vertex.setProperty("deleted", true);
        } else {
            super.removeVertex(vertex);
        }
    }

    @Override
    public Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels) {

        Set<Edge> result = new HashSet<Edge>();
        Iterable<Edge> edges = v1.getEdges(Direction.BOTH, labels);
        if (!v1.equals(v2)) {
            for (Edge edge : edges) {
                if (edge.getVertex(Direction.IN).equals(v2) || edge.getVertex(Direction.OUT).equals(v2)) {
                    result.add(edge);
                }
            }
        } else {
            for (Edge edge : edges) {
                if (edge.getVertex(Direction.IN).equals(v2) && edge.getVertex(Direction.OUT).equals(v2)) {
                    result.add(edge);
                }
            }
        }
        return result;


//        Node n1 = ((Neo4j2Vertex) v1).getRawVertex();
//        Node n2 = ((Neo4j2Vertex) v2).getRawVertex();
//        List<DynamicRelationshipType> dynaRel = new ArrayList<DynamicRelationshipType>(labels.length);
//        for (String label : labels) {
//            dynaRel.add(DynamicRelationshipType.withName(label));
//        }
//        Set<Edge> result = new HashSet<Edge>(dynaRel.size());
//        Iterable<Relationship> relationships = n1.getRelationships(dynaRel.toArray(new DynamicRelationshipType[]{}));
//        for (Relationship relationship : relationships) {
//            if (relationship.getEndNode().equals(n2) || relationship.getStartNode().equals(n2)) {
//                result.add(this.getEdge(relationship.getId()));
//            }
//        }
//        return result;
    }

    @Override
    public long countVertices() {
        int countDeletedNodes = 0;
        for (Edge edge : getDeletionVertex().getEdges(com.tinkerpop.blueprints.Direction.OUT, DELETION_VERTEX)) {
            countDeletedNodes++;
        }
        return ((GraphDatabaseAPI) this.getRawGraph()).getDependencyResolver().resolveDependency(NodeManager.class).getNumberOfIdsInUse(Node.class) - 2 - countDeletedNodes;
    }

    @Override
    public long countEdges() {
        int countDeletedNodes = 0;
        for (Edge edge : getDeletionVertex().getEdges(com.tinkerpop.blueprints.Direction.OUT, DELETION_VERTEX)) {
            countDeletedNodes++;
        }
        return ((GraphDatabaseAPI) this.getRawGraph()).getDependencyResolver().resolveDependency(NodeManager.class).getNumberOfIdsInUse(Relationship.class) - 1 - countDeletedNodes;
    }

    @Override
    public boolean hasEdgeBeenDeleted(Edge edge) {
        Neo4j2Edge neo4jEdge = (Neo4j2Edge) edge;
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
        UMLG.remove();
    }

}
