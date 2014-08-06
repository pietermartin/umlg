package org.umlg.runtime.adaptor;

import com.tinkerpop.gremlin.neo4j.structure.Neo4jEdge;
import com.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import com.tinkerpop.gremlin.process.computer.GraphComputer;
import com.tinkerpop.gremlin.process.graph.GraphTraversal;
import com.tinkerpop.gremlin.structure.*;
import com.tinkerpop.gremlin.structure.strategy.ReadOnlyGraphStrategy;
import com.tinkerpop.gremlin.structure.strategy.StrategyWrappedEdge;
import com.tinkerpop.gremlin.structure.strategy.StrategyWrappedGraph;
import com.tinkerpop.gremlin.structure.util.GraphFactory;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.lang.time.StopWatch;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.ConstraintViolationException;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.AutoIndexer;
import org.neo4j.graphdb.index.RelationshipAutoIndexer;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.kernel.impl.core.NodeManager;
import org.neo4j.kernel.impl.util.StringLogger;
import org.umlg.runtime.collection.Filter;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.memory.UmlgLazyList;
import org.umlg.runtime.collection.memory.UmlgMemorySet;
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
    private StrategyWrappedGraph neo4jGraph;

    public UmlgNeo4jGraph(String directory) {
        BaseConfiguration conf = new BaseConfiguration();
        conf.setProperty("gremlin.graph", "com.tinkerpop.gremlin.neo4j.structure.Neo4jGraph");
        conf.setProperty(Neo4jGraph.CONFIG_DIRECTORY, directory);
        conf.setProperty("gremlin.neo4j.conf.node_auto_indexing", "true");
        conf.setProperty("gremlin.neo4j.conf.relationship_auto_indexing", "true");
        this.neo4jGraph = (StrategyWrappedGraph) GraphFactory.open(conf, new UmlgNeo4jGraphStrategy());
        this.transactionEventHandler = new UmlgTransactionEventHandlerImpl();
    }

    @Override
    public Graph getReadOnlyGraph() {
        Neo4jGraph rawNeo4jGraph = (Neo4jGraph) this.neo4jGraph.getBaseGraph();
        final StrategyWrappedGraph swg = new StrategyWrappedGraph(rawNeo4jGraph);
        swg.strategy().setGraphStrategy(new ReadOnlyGraphStrategy());
        return swg;
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
            if (uniqueParameter.getValue()) {
                createUniqueConstraint(labelParameter.getValue(), key);
            } else {
                createLabeledIndex(labelParameter.getValue(), key);

            }
        } else if (Edge.class.isAssignableFrom(elementClass)) {
            this.tx().readWrite();
            createLegacyIndex(elementClass, key);
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
    public <T extends PersistentObject> UmlgSet<T> allInstances(String className) {
        UmlgMemorySet<T> result = new UmlgMemorySet();
        int lastIndexOfDot = className.lastIndexOf(".");
        String label;
        if (lastIndexOfDot != -1) {
            label = className.substring(lastIndexOfDot + 1);
        } else {
            label = className;
        }
        this.neo4jGraph.V().<Vertex>has(Element.LABEL, label).forEach (
                vertex -> result.add(UMLG.get().<T>getEntity(vertex.id()))
        );
        return result;
    }

    @Override
    public <T extends PersistentObject> UmlgSet<T> allInstances(String className, Filter filter) {
        UmlgMemorySet<T> result = new UmlgMemorySet();
        int lastIndexOfDot = className.lastIndexOf(".");
        String label;
        if (lastIndexOfDot != -1) {
            label = className.substring(lastIndexOfDot + 1);
        } else {
            label = className;
        }
        this.neo4jGraph.V().<Vertex>has(Element.LABEL, label).forEach (
                vertex -> {
                    T entity = UMLG.get().<T>getEntity(vertex.id());
                    if (filter.filter(entity)) {
                        result.add(entity);
                    }
                }
        );
        return result;
    }

    @Override
    public Vertex addVertex(String className) {
        String label;
        if (className != null) {
            int lastIndexOfDot = className.lastIndexOf(".");
            if (lastIndexOfDot != -1) {
                label = className.substring(lastIndexOfDot + 1);
            } else {
                label = className;
            }
            return this.neo4jGraph.addVertex(Element.LABEL, label);
        } else {
            return this.neo4jGraph.addVertex();
        }
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
        return new UmlgLazyList<PersistentObject>(iterator);
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
                        this.engine = new ExecutionEngine(((Neo4jGraph) this.neo4jGraph.getBaseGraph()).getBaseGraph(), StringLogger.SYSTEM);
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
                ExecutionEngine engine = new ExecutionEngine(((Neo4jGraph) this.neo4jGraph.getBaseGraph()).getBaseGraph(), StringLogger.SYSTEM);
                ExecutionResult executionResult = engine.execute(query);
                return (T) executionResult;
            default:
                throw new RuntimeException("Unknown query enum");
        }
    }

    @Override
    public void drop() {
        UmlgGraphManager.INSTANCE.deleteGraph();
    }

    @Override
    public void clear() {
        this.V().forEach(Vertex::remove);
        this.E().forEach(Edge::remove);
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

    @Override
    public Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels) {

        Set<Edge> result = new HashSet<>();
        GraphTraversal<Vertex, Edge> edges = v1.bothE(labels);

        if (!v1.equals(v2)) {

            edges.forEach(
                    edge -> {
                        if (edge.inV().next().equals(v2) || edge.outV().next().equals(v2)) {
                            result.add(edge);
                        }
                    }
            );

        } else {

            edges.forEach(
                    edge -> {
                        if (edge.inV().next().equals(v2) && edge.outV().next().equals(v2)) {
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
        return ((GraphDatabaseAPI) ((Neo4jGraph) this.neo4jGraph.getBaseGraph()).getBaseGraph()).getDependencyResolver().resolveDependency(NodeManager.class).getNumberOfIdsInUse(Node.class) - 2 - countDeletedNodes;
    }

    @Override
    public long countEdges() {
        int countDeletedNodes = 0;
        for (Edge edge : getDeletionVertex().outE(DELETION_VERTEX).toList()) {
            countDeletedNodes++;
        }
        return ((GraphDatabaseAPI) ((Neo4jGraph) this.neo4jGraph.getBaseGraph()).getBaseGraph()).getDependencyResolver().resolveDependency(NodeManager.class).getNumberOfIdsInUse(Relationship.class) - 1 - countDeletedNodes;
    }

    @Override
    public boolean hasEdgeBeenDeleted(Edge edge) {
        Neo4jEdge neo4jEdge = (Neo4jEdge) ((StrategyWrappedEdge) edge).getBaseEdge();
        try {
            neo4jEdge.getBaseElement().hasProperty("asd");
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
    public Vertex v(final Object id) {
        return this.neo4jGraph.v(id);
    }

    @Override
    public Edge e(final Object id) {
        return this.neo4jGraph.e(id);
    }

    @Override
    public GraphTraversal<Edge, Edge> E() {
        return this.neo4jGraph.E();
    }

    @Override
    public <S> GraphTraversal<S, S> of() {
        return this.neo4jGraph.of();
    }

    @Override
    public <C extends GraphComputer> C compute(Class<C>... graphComputerClass) {
        return (this.neo4jGraph.getBaseGraph()).compute(graphComputerClass);
    }

    @Override
    public Transaction tx() {
        return this.neo4jGraph.tx();
    }

    @Override
    public Variables variables() {
        return ((Neo4jGraph) this.neo4jGraph.getBaseGraph()).variables();
    }

    @Override
    public void close() throws Exception {
        this.neo4jGraph.close();
    }

    @Override
    public Features getFeatures() {
        return this.neo4jGraph.getFeatures();
    }

    private IndexDefinition createLabeledIndex(String label, String propertyKey) throws ConstraintViolationException {
        this.tx().readWrite();
        Schema schema = ((Neo4jGraph)this.neo4jGraph.getBaseGraph()).getBaseGraph().schema();
        return schema.indexFor(DynamicLabel.label(label)).on(propertyKey).create();
    }

    private void createLegacyIndex(Class<? extends Element> elementClass, String key) {
        this.tx().readWrite();
        if (Vertex.class.isAssignableFrom(elementClass)) {
            AutoIndexer<Node> nodeAutoIndexer = ((Neo4jGraph)this.neo4jGraph.getBaseGraph()).getBaseGraph().index().getNodeAutoIndexer();
            if (!nodeAutoIndexer.isEnabled()) {
                throw new IllegalStateException("Automatic indexing must be enabled at startup for legacy indexing to work on vertices!");
            }
            nodeAutoIndexer.startAutoIndexingProperty(key);
        } else if (Edge.class.isAssignableFrom(elementClass)) {
            RelationshipAutoIndexer relationshipAutoIndexer = ((Neo4jGraph)this.neo4jGraph.getBaseGraph()).getBaseGraph().index().getRelationshipAutoIndexer();
            if (!relationshipAutoIndexer.isEnabled()) {
                throw new IllegalStateException("Automatic indexing must be enabled at startup for legacy indexing to work on edges!");
            }
            relationshipAutoIndexer.startAutoIndexingProperty(key);
        } else {
            throw new IllegalArgumentException("Class is not indexable: " + elementClass);
        }
    }

    private ConstraintDefinition createUniqueConstraint(String label, String propertyKey) {
        this.tx().readWrite();
        Schema schema = ((Neo4jGraph)this.neo4jGraph.getBaseGraph()).getBaseGraph().schema();
        return schema.constraintFor(DynamicLabel.label(label)).assertPropertyIsUnique(propertyKey).create();
    }

}
