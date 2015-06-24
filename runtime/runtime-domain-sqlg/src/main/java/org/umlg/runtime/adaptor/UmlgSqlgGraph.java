package org.umlg.runtime.adaptor;

import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.process.computer.GraphComputer;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.*;
import org.umlg.runtime.collection.Filter;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.memory.UmlgMemorySet;
import org.umlg.runtime.domain.PersistentObject;
import org.umlg.runtime.domain.UmlgApplicationNode;
import org.umlg.runtime.util.UmlgProperties;
import org.umlg.sqlg.structure.RecordId;
import org.umlg.sqlg.structure.SqlgGraph;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Date: 2013/01/09
 * Time: 8:09 PM
 */
public class UmlgSqlgGraph implements UmlgGraph, UmlgAdminGraph {

    private UmlgTransactionEventHandlerImpl transactionEventHandler;
    private Class<UmlgApplicationNode> umlgApplicationNodeClass;
    protected SqlgGraph sqlG;
    //cache the root vertex
    private Vertex rootVertex;

    public UmlgSqlgGraph(Configuration config) {
        this.sqlG = SqlgGraph.open(config);
        this.transactionEventHandler = new UmlgTransactionEventHandlerImpl();
    }

    public UmlgSqlgGraph(SqlgGraph sqlgGraph) {
        this.sqlG = sqlgGraph;
        this.transactionEventHandler = new UmlgTransactionEventHandlerImpl();
    }

    void setBypass(boolean bypasss) {
        this.transactionEventHandler.setBypass(bypasss);
    }

    @Override
    public Graph getReadOnlyGraph() {
        return null;
    }

    @Override
    public Configuration configuration() {
        return this.sqlG.configuration();
    }

//    @Override
//    public Iterators iterators() {
//        return this.sqlG.iterators();
//    }

    @Override
    public void batchModeOn() {
        this.sqlG.tx().batchModeOn();
    }

    @Override
    public boolean isInBatchMode() {
        return this.sqlG.tx().isInBatchMode();
    }

    public <T extends Element> void createKeyIndex(final String key, final Class<T> elementClass, final UmlgParameter... indexUmlgParameters) {
        if (elementClass == null) {
            throw Exceptions.classForElementCannotBeNull();
        }

        UmlgParameter<String, Class<?>> indexUmlgParameter = indexUmlgParameters[0];
        UmlgParameter<String, Boolean> uniqueUmlgParameter = indexUmlgParameters[1];
        UmlgParameter<String, String> labelUmlgParameter = indexUmlgParameters[2];

        if (Vertex.class.isAssignableFrom(elementClass)) {
            this.tx().readWrite();
            if (uniqueUmlgParameter.getValue()) {
                //TODO
//                this.sqlG.createUniqueConstraint(labelParameter.getValue(), key);
                this.sqlG.createVertexLabeledIndex(labelUmlgParameter.getValue(), key, SqlgDefaultValueUtil.valueFor(indexUmlgParameter.getValue()));
            } else {
                this.sqlG.createVertexLabeledIndex(labelUmlgParameter.getValue(), key, SqlgDefaultValueUtil.valueFor(indexUmlgParameter.getValue()));

            }
        } else if (Edge.class.isAssignableFrom(elementClass)) {
            this.tx().readWrite();
            this.sqlG.createEdgeLabeledIndex(labelUmlgParameter.getValue(), key);
        } else {
            throw Exceptions.classIsNotIndexable(elementClass);
        }
    }

    /**
     * Generic for all graphs start
     */
    @Override
    public void incrementTransactionCount() {
//        this.getRoot().property("transactionCount", (Integer) this.getRoot().value("transactionCount") + 1);
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
            this.sqlG.tx().commit();
        } finally {
            TransactionThreadEntityVar.remove();
            TransactionThreadMetaNodeVar.remove();
            //This may start a new transaction
            this.transactionEventHandler.afterCommit();
            TransactionThreadNotificationVar.remove();
        }
    }

    public void rollback() {
        try {
            this.sqlG.tx().rollback();
        } finally {
            TransactionThreadEntityVar.remove();
            TransactionThreadMetaNodeVar.remove();
            TransactionThreadNotificationVar.remove();
        }
    }

    @Override
    public <TT extends PersistentObject> UmlgSet<TT> allInstances(String className) {
        UmlgMemorySet<TT> result = new UmlgMemorySet();
        String label;
        int lastIndexOfDot = className.lastIndexOf(".");
        if (lastIndexOfDot != -1) {
            label = className.substring(lastIndexOfDot + 1);
        } else {
            label = className;
        }
        this.sqlG.traversal().V().<Vertex>has(T.label, label).forEachRemaining(
                vertex -> result.add(UMLG.get().<TT>getEntity(vertex))
        );
        return result;
    }

    @Override
    public <TT extends PersistentObject> UmlgSet<TT> allInstances(String className, Filter filter) {
        UmlgMemorySet<TT> result = new UmlgMemorySet();
        String label;
        int lastIndexOfDot = className.lastIndexOf(".");
        if (lastIndexOfDot != -1) {
            label = className.substring(lastIndexOfDot + 1);
        } else {
            label = className;
        }
        this.sqlG.traversal().V().<Vertex>has(T.label, label).forEachRemaining(
                vertex -> {
                    TT entity = UMLG.get().<TT>getEntity(vertex);
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
            label = shortenClassName(className);
            return this.sqlG.addVertex(T.label, label);
        } else {
            return this.sqlG.addVertex();
        }
    }


    private String shortenClassName(String className) {
        int lastIndexOfDot = className.lastIndexOf(".");
        if (lastIndexOfDot != -1) {
            return className.substring(lastIndexOfDot + 1);
        } else {
            lastIndexOfDot = className.lastIndexOf(":");
            if (lastIndexOfDot != -1) {
                return className.substring(lastIndexOfDot + 1);
            } else {
                return className;
            }
        }
    }

    @Override
    public void addDeletionNode() {
        Vertex v = addVertex(DELETION_VERTEX);
        getRoot().addEdge(DELETED_VERTEX_EDGE, v);
    }

    private Vertex getDeletionVertex() {
        if (getRoot() != null && getRoot().edges(Direction.OUT, DELETED_VERTEX_EDGE).hasNext()) {
            return getRoot().edges(Direction.OUT, DELETED_VERTEX_EDGE).next().vertices(Direction.IN).next();
        } else {
            return null;
        }
    }

    @Override
    public <T extends PersistentObject> T getEntity(Object id) {
        RecordId recordId;
        try {
            if (!(id instanceof RecordId)) {
                recordId = RecordId.from(id);
            } else {
                recordId = (RecordId) id;
            }
            GraphTraversal<Vertex, Vertex> traversal = this.V(recordId);
            if (!traversal.hasNext()) {
                throw new RuntimeException(String.format("No vertex found for id %s", recordId.toString()));
            }
            return instantiateClassifier(traversal.next());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends PersistentObject> T getEntity(Vertex vertex) {
        return instantiateClassifier(vertex);
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
    public PersistentObject getFromUniqueIndex(String label, String indexKey, Object indexValue) {
        Iterator<Vertex> iterator = this.V().has(T.label, shortenClassName(label)).has(indexKey, indexValue);
        if (iterator.hasNext()) {
            return instantiateClassifier(iterator.next());
        } else {
            return null;
        }
    }

    @Override
    public List<PersistentObject> getFromIndex(String label, String indexKey, Object indexValue) {
        final Iterator<Vertex> iterator = this.V().has(T.label, shortenClassName(label)).has(indexKey, indexValue);
        Iterable<Vertex> iterable = () -> iterator;
        Stream<PersistentObject> targetStream = StreamSupport.stream(iterable.spliterator(), false)
                .map(v -> UMLG.get().getEntity(v));
        List<PersistentObject> result = targetStream.collect(Collectors.toList());
        return result;
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
//                    StopWatch stopWatch = new StopWatch();
//                    stopWatch.start();
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.sqlG.query(query));
//                    stopWatch.stop();
//                    sb.append("Time to execute query = ");
//                    sb.append(stopWatch.toString());
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
                return (T) "//TODO";
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
        this.sqlG.getSchemaManager().clear();
    }

    @Override
    public Vertex getRoot() {
        if (this.rootVertex == null) {
            this.rootVertex = this.V().has(T.label, UmlgGraph.ROOT_VERTEX).next();
        }
        return this.rootVertex;
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
        Iterator<Edge> edges = v1.edges(Direction.BOTH, labels);

        if (!v1.equals(v2)) {

            edges.forEachRemaining(
                    edge -> {
                        if (edge.vertices(Direction.IN).next().equals(v2) || edge.vertices(Direction.OUT).next().equals(v2)) {
                            result.add(edge);
                        }
                    }
            );

        } else {

            edges.forEachRemaining(
                    edge -> {
                        if (edge.vertices(Direction.IN).next().equals(v2) && edge.vertices(Direction.OUT).next().equals(v2)) {
                            result.add(edge);
                        }
                    }
            );

        }
        return result;

    }

    @Override
    public long countVertices() {
        return this.sqlG.countVertices() - 1;
    }

    @Override
    public long countEdges() {
        return this.sqlG.countEdges();
    }

    @Override
    public boolean hasEdgeBeenDeleted(Edge edge) {
        return false;
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
        TransactionThreadNotificationVar.remove();
        UmlgAssociationClassManager.remove();
        UMLG.remove();
    }

    @Override
    public Graph getUnderlyingGraph() {
        return this.sqlG;
    }

    @Override
    public Vertex addVertex(Object... keyValues) {
        return this.sqlG.addVertex(keyValues);
    }

    @Override
    public GraphTraversalSource traversal() {
        return this.sqlG.traversal();
    }

    //    @Override
    public GraphTraversal<Vertex, Vertex> V(final Object... vertexIds) {
        return this.sqlG.traversal().V(validateIds(vertexIds));
    }

    //    @Override
    public GraphTraversal<Edge, Edge> E(final Object... edgeIds) {
        return this.sqlG.traversal().E(validateIds(edgeIds));
    }

    @Override
    public <C extends GraphComputer> C compute(Class<C> graphComputerClass) throws IllegalArgumentException {
        return this.sqlG.compute(graphComputerClass);
    }
//    @Override
//    public void compute(Class<? extends GraphComputer> graphComputerClass) throws IllegalArgumentException {
//        this.sqlG.compute(graphComputerClass);
//    }

    @Override
    public GraphComputer compute() {
        return this.sqlG.compute();
    }

    @Override
    public Iterator<Vertex> vertices(Object... vertexIds) {
        return this.sqlG.vertices(vertexIds);
    }

    @Override
    public Iterator<Edge> edges(Object... edgeIds) {
        return this.sqlG.edges(edgeIds);
    }

//    @Override
//    public TraversalEngine engine() {
//        return this.sqlG.engine();
//    }
//
//    @Override
//    public void engine(TraversalEngine traversalEngine) {
//        this.sqlG.engine(traversalEngine);
//    }

    private RecordId[] validateIds(final Object... ids) {
        List<RecordId> longIds = new ArrayList<>();
        for (Object id : ids) {
            if (id instanceof RecordId) {
                longIds.add((RecordId) id);
            } else {
                longIds.add(RecordId.from(id));
            }
        }
        return longIds.toArray(new RecordId[]{});
    }

    @Override
    public Transaction tx() {
        return this.sqlG.tx();
    }

    @Override
    public Variables variables() {
        return this.sqlG.variables();
    }

    @Override
    public void close() throws Exception {
        this.sqlG.close();
    }

    @Override
    public boolean supportsBatchMode() {
        return this.sqlG.features().supportsBatchMode();
    }

    @Override
    public Features features() {
        return this.sqlG.features();
    }
}
