package org.umlg.runtime.adaptor;

import com.tinkerpop.gremlin.neo4j.structure.Neo4jEdge;
import com.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import com.tinkerpop.gremlin.neo4j.structure.Neo4jVertex;
import com.tinkerpop.gremlin.process.computer.GraphComputer;
import com.tinkerpop.gremlin.process.graph.GraphTraversal;
import com.tinkerpop.gremlin.structure.*;
import org.apache.commons.lang.time.StopWatch;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.kernel.impl.core.NodeManager;
import org.neo4j.kernel.impl.util.StringLogger;
import org.umlg.runtime.collection.memory.UmlgLazyList;
import org.umlg.runtime.domain.PersistentObject;
import org.umlg.runtime.domain.UmlgApplicationNode;
import org.umlg.runtime.util.UmlgProperties;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Date: 2013/01/09
 * Time: 8:09 PM
 */
public class UmlgNeo4jGraph implements UmlgGraph, UmlgAdminGraph {

    private UmlgTransactionEventHandlerImpl transactionEventHandler;
    private Class<UmlgApplicationNode> umlgApplicationNodeClass;
    private ExecutionEngine engine;
    private Neo4jGraph neo4jGraph;

    public UmlgNeo4jGraph(String directory) {
        this.neo4jGraph = Neo4jGraph.open(directory);
        this.transactionEventHandler = new UmlgTransactionEventHandlerImpl();
    }

    void setBypass(boolean bypasss) {
        this.transactionEventHandler.setBypass(bypasss);
    }

    public <T extends Element> void createKeyIndex(final String key, final Class<T> elementClass, final Parameter... indexParameters) {
        if (elementClass == null) {
            throw UmlgGraph.Exceptions.classForElementCannotBeNull();
        }

        Parameter<String, Class<?>> indexParameter = indexParameters[0];
        Parameter<String, Boolean> uniqueParameter = indexParameters[1];
        Parameter<String, String> labelParameter = indexParameters[2];

        if (Vertex.class.isAssignableFrom(elementClass)) {
            this.tx().readWrite();
            Schema schema = this.neo4jGraph.getRawGraph().schema();
            IndexDefinition indexDefinition = schema.indexFor(
                    DynamicLabel.label(labelParameter.getValue())).on(key).create();
        } else if (Edge.class.isAssignableFrom(elementClass)) {
            this.tx().readWrite();
        } else {
            throw UmlgGraph.Exceptions.classIsNotIndexable(elementClass);
        }
    }

    /**
     * Generic for all graphs start
     */
    @Override
    public void incrementTransactionCount() {
        this.getRoot().property("transactionCount", (Integer) this.getRoot().value("transactionCount") + 1);
    }

    @Override
    public long getTransactionCount() {
        return this.getRoot().value("transactionCount");
    }

    @Override
    public void addRoot() {
        Vertex root = addVertex(ROOT_VERTEX);
        root.property("transactionCount", 1);
        root.property("className", ROOT_CLASS_NAME);
    }

    public void commit() {
        try {
            //This null check is here for when a graph is created. It calls commit before the listener has been set.
            if (this.transactionEventHandler != null) {
                this.transactionEventHandler.beforeCommit();
            }
            this.neo4jGraph.tx().commit();
        } finally {
            TransactionThreadEntityVar.remove();
            TransactionThreadMetaNodeVar.remove();
        }
    }

    public void rollback() {
        try {
            this.neo4jGraph.tx().rollback();
        } finally {
            TransactionThreadEntityVar.remove();
            TransactionThreadMetaNodeVar.remove();
        }
    }

    @Override
    public Vertex addVertex(String className) {
        this.tx().readWrite();
        Vertex v;
        if (className != null) {
            int lastIndexOfDot = className.lastIndexOf(".");
            if (lastIndexOfDot != -1) {
                v = new Neo4jVertex(this.neo4jGraph.getRawGraph().createNode(DynamicLabel.label(className.substring(lastIndexOfDot + 1))), this.neo4jGraph);
            } else {
                v = new Neo4jVertex(this.neo4jGraph.getRawGraph().createNode(DynamicLabel.label(className)), this.neo4jGraph);
            }
            v.property("className", className);
        } else {
            v = new Neo4jVertex(this.neo4jGraph.getRawGraph().createNode(), this.neo4jGraph);
        }
        return v;
    }

    @Override
    public void addDeletionNode() {
        Vertex v = addVertex(DELETION_VERTEX);
        getRoot().addEdge(DELETED_VERTEX_EDGE, v);
    }

    private Vertex getDeletionVertex() {
        if (getRoot() != null && getRoot().outE(DELETED_VERTEX_EDGE).hasNext()) {
            return getRoot().outE(DELETED_VERTEX_EDGE).next().inV().next();
        } else {
            return null;
        }
    }

    @Override
    public <T extends PersistentObject> T getEntity(Object id) {
        try {
            Vertex v = this.v(id);
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
            String className = v.value("className");
            Class<?> c = Class.forName(className);
            return (T) c.getConstructor(Vertex.class).newInstance(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PersistentObject getFromUniqueIndex(String indexKey, Object indexValue) {
        Iterator<Vertex> iterator = this.V().has(indexKey, indexValue);
        if (iterator.hasNext()) {
            return instantiateClassifier(iterator.next());
        } else {
            return null;
        }
    }

    @Override
    public List<PersistentObject> getFromIndex(String indexKey, Object indexValue) {
        final Iterator<Vertex> iterator = this.V().has(indexKey, indexValue);
        List<PersistentObject> lazy = new UmlgLazyList<>(iterator);
        return lazy;
    }

    /* Generic for all graphs end */

    @Override
    public String executeQueryToJson(UmlgQueryEnum umlgQueryEnum, Object contextId, String query) {
        try {
            switch (umlgQueryEnum) {
                case OCL:
                    try {
                        Class<?> umlgOclExecutor = Class.forName("org.umlg.ocl.UmlgOclExecutor");
                        Method method = umlgOclExecutor.getMethod("executeOclQueryAsJson", Object.class, String.class);
                        String result = (String) method.invoke(null, contextId, query);
                        return result;
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("UmlgOclExecutor is not on the class path.");
                    } catch (Exception e) {
                        if (e instanceof RuntimeException) {
                            throw (RuntimeException) e;
                        } else if (e instanceof InvocationTargetException) {
                            Throwable target = ((InvocationTargetException) e).getTargetException();
                            if (target instanceof RuntimeException) {
                                throw (RuntimeException) target;
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
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    if (this.engine == null) {
                        this.engine = new ExecutionEngine(this.neo4jGraph.getRawGraph(), StringLogger.SYSTEM);
                    }
                    StringBuilder sb = new StringBuilder();
                    ExecutionResult executionResult = engine.execute(query);
                    sb.append(executionResult.dumpToString());
                    stopWatch.stop();
                    sb.append("Time to execute query = ");
                    sb.append(stopWatch.toString());
                    return sb.toString();
                default:
                    throw new RuntimeException("Unknown query enum");
            }
        } finally {
            this.rollback();
        }
    }

    @Override
    public <T> T executeQuery(UmlgQueryEnum umlgQueryEnum, Object contextId, String query) {
        try {
            switch (umlgQueryEnum) {
                case OCL:
                    try {
                        Class<?> umlgOclExecutor = Class.forName("org.umlg.ocl.UmlgOclExecutor");
                        Method method = umlgOclExecutor.getMethod("executeOclQuery", Object.class, String.class);
                        Object result = method.invoke(null, contextId, query);
                        return (T) result;
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("UmlgOclExecutor is not on the class path.");
                    } catch (Exception e) {
                        if (e instanceof RuntimeException) {
                            throw (RuntimeException) e;
                        } else if (e instanceof InvocationTargetException) {
                            Throwable target = ((InvocationTargetException) e).getTargetException();
                            if (target instanceof RuntimeException) {
                                throw (RuntimeException) target;
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
                    return (T) result;
                case NATIVE:
                    ExecutionEngine engine = new ExecutionEngine(this.neo4jGraph.getRawGraph(), StringLogger.SYSTEM);
                    ExecutionResult executionResult = engine.execute(query);
                    return (T) executionResult;
                default:
                    throw new RuntimeException("Unknown query enum");
            }
        } finally {
            this.rollback();
        }
    }

    @Override
    public void drop() {
        UmlgGraphManager.INSTANCE.deleteGraph();
    }

    @Override
    public Vertex getRoot() {
        return this.v(0L);
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

//    @Override
//    public void removeVertex(final Vertex vertex) {
//        this.autoStartTransaction(true);
//        Iterable<Edge> edges = vertex.edgesForDirection(Direction.BOTH);
//        for (final Edge edge : edges) {
//            edge.remove();
//        }
//        if (!vertex.getId().equals(new Long(0))) {
//            getDeletionVertex().addEdge(DELETION_VERTEX, vertex);
//            for (String key : vertex.getPropertyKeys()) {
//                vertex.removeProperty(key);
//            }
//            vertex.setProperty("deleted", true);
//        } else {
//            super.removeVertex(vertex);
//        }
//    }

    @Override
    public Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels) {

        Set<Edge> result = new HashSet<>();
        GraphTraversal<Vertex, Edge> edges = v1.bothE(labels);

        if (!v1.equals(v2)) {

            edges.forEach(
                edge -> {
                    if (edge.inV().equals(v2) || edge.outV().equals(v2)) {
                        result.add(edge);
                    }
                }
            );

        } else {

            edges.forEach(
                edge -> {
                    if (edge.inV().equals(v2) && edge.outV().equals(v2)) {
                        result.add(edge);
                    }
                }
            );

        }
        return result;

    }

    @Override
    public long countVertices() {
        int countDeletedNodes = 0;
        for (Edge edge : getDeletionVertex().outE(DELETION_VERTEX).toList()) {
            countDeletedNodes++;
        }
        return ((GraphDatabaseAPI) this.neo4jGraph.getRawGraph()).getDependencyResolver().resolveDependency(NodeManager.class).getNumberOfIdsInUse(Node.class) - 2 - countDeletedNodes;
    }

    @Override
    public long countEdges() {
        int countDeletedNodes = 0;
        for (Edge edge : getDeletionVertex().outE(DELETION_VERTEX).toList()) {
            countDeletedNodes++;
        }
        return ((GraphDatabaseAPI) this.neo4jGraph.getRawGraph()).getDependencyResolver().resolveDependency(NodeManager.class).getNumberOfIdsInUse(Relationship.class) - 1 - countDeletedNodes;
    }

    @Override
    public boolean hasEdgeBeenDeleted(Edge edge) {
        Neo4jEdge neo4jEdge = (Neo4jEdge) edge;
        try {
            neo4jEdge.getRawElement().hasProperty("asd");
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public boolean isTransactionActive() {
        return tx().isOpen();
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

    @Override
    public Vertex addVertex(Object... keyValues) {
        return this.neo4jGraph.addVertex(keyValues);
    }

    @Override
    public GraphTraversal<Vertex, Vertex> V() {
        return this.neo4jGraph.V();
    }

    @Override
    public GraphTraversal<Edge, Edge> E() {
        return this.neo4jGraph.E();
    }

    @Override
    public <C extends GraphComputer> C compute(Class<C>... graphComputerClass) {
        return this.neo4jGraph.compute(graphComputerClass);
    }

    @Override
    public Transaction tx() {
        return this.neo4jGraph.tx();
    }

    @Override
    public <V extends Variables> V variables() {
        return this.neo4jGraph.variables();
    }

    @Override
    public void close() throws Exception {
        this.neo4jGraph.close();
    }
}
