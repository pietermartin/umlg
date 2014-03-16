package org.umlg.runtime.adaptor;

import com.google.common.base.Preconditions;
import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;
import com.thinkaurelius.titan.graphdb.database.StandardTitanGraph;
import com.tinkerpop.blueprints.*;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.list.LazyList;
import org.apache.commons.io.FileUtils;
import org.umlg.runtime.domain.PersistentObject;
import org.umlg.runtime.domain.UmlgNode;
import org.umlg.runtime.util.UmlgProperties;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

/**
 * Date: 2013/08/31
 * Time: 3:31 PM
 */
public class UmlgTitanGraph extends StandardTitanGraph implements UmlgGraph {

    private static final Logger logger = Logger.getLogger(UmlgTitanGraph.class.getPackage().getName());
    private UmlgTransactionEventHandler transactionEventHandler;

    public UmlgTitanGraph(GraphDatabaseConfiguration configuration) {
        super(configuration);
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
        List<PersistentObject> lazy = LazyList.lazyList(new ArrayList<PersistentObject>(), new Factory<PersistentObject>() {
            @Override
            public PersistentObject create() {
                return instantiateClassifier(iterator.next());
            }
        });
        return lazy;
    }

    @Override
    public <T extends Element> void createKeyIndex(final String key, final Class<T> elementClass, final Parameter... indexParameters) {
        Preconditions.checkState(indexParameters.length == 2, "UmlgGraph.createKeyIndex must have indexParameters of length 2, One for the type and one for uniqueness.");
        Parameter<String, Class<?>> indexParameter = indexParameters[0];
        Parameter<String, Boolean> uniqueParameter = indexParameters[1];
        if (indexParameter.getValue() == String.class) {
            if (uniqueParameter.getValue()) {
                this.makeKey(key).dataType(String.class).indexed(elementClass).unique().make();
            } else {
                this.makeKey(key).dataType(String.class).indexed(elementClass).make();
            }
        } else if (indexParameter.getValue() == Integer.class) {
            if (uniqueParameter.getValue()) {
                this.makeKey(key).dataType(Integer.class).indexed(elementClass).unique().make();
            } else {
                this.makeKey(key).dataType(Integer.class).indexed(elementClass).make();
            }
        } else if (indexParameter.getValue() == Double.class) {
            if (uniqueParameter.getValue()) {
                this.makeKey(key).dataType(Double.class).indexed(elementClass).unique().make();
            } else {
                this.makeKey(key).dataType(Double.class).indexed(elementClass).make();
            }
        } else if (indexParameter.getValue() == Boolean.class) {
            if (uniqueParameter.getValue()) {
                this.makeKey(key).dataType(Boolean.class).indexed(elementClass).unique().make();
            } else {
                this.makeKey(key).dataType(Boolean.class).indexed(elementClass).make();
            }
        } else {
            throw new RuntimeException(String.format("Unsupport type for indexing!", new String[]{elementClass.getName()}));
        }
    }

    /** Generic for all graphs end */

    @Override
    public String executeQuery(UmlgQueryEnum umlgQueryEnum, Object contextId, String query) {

        switch (umlgQueryEnum) {
            case OCL:
                try {
                    Class<?> umlgOclExecutor = Class.forName("org.umlg.ocl.UmlgOclExecutor");
                    Method method = umlgOclExecutor.getMethod("executeOclQueryToJson", UmlgNode.class, String.class);
                    UmlgNode context = (UmlgNode) GraphDb.getDb().instantiateClassifier(contextId);
                    String json = (String) method.invoke(null, context, query);
                    return json;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("UmlgOclExecutor is not on the class path.");
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
                throw new IllegalStateException("Titan does not have a native query language!");
        }

        throw new RuntimeException("Unknown query enum");

    }

    @Override
    public void drop() {
        UmlgGraphManager.INSTANCE.deleteGraph();
    }

    @Override
    public Vertex getRoot() {
        return this.getVertex(4L);
    }

    @Override
    public void removeVertex(final Vertex vertex) {
        this.getCurrentThreadTx();
        Iterable<Edge> edges = vertex.getEdges(Direction.BOTH);
        for (final Edge edge : edges) {
            edge.remove();
        }
        if (!vertex.getId().equals(new Long(4))) {
            getDeletionVertex().addEdge(DELETION_VERTEX, vertex);
            vertex.setProperty("deleted", true);
        } else {
            super.removeVertex(vertex);
        }
    }

    @Override
    public Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels) {
        Set<Edge> result = new HashSet<Edge>();
        Iterable<Edge> edges = v1.getEdges(Direction.BOTH, labels);
        for (Edge edge : edges) {
            if (edge.getVertex(Direction.IN).equals(v2) || edge.getVertex(Direction.OUT).equals(v2)) {
                result.add(edge);
            }
        }
        return result;
    }

    @Override
    public long countVertices() {
        int countDeletedNodes = 0;
        if (getDeletionVertex() != null) {
            for (Edge edge : getDeletionVertex().getEdges(com.tinkerpop.blueprints.Direction.OUT, DELETION_VERTEX)) {
                countDeletedNodes++;
            }
        }
        int count = 0;
        for (Vertex v : getVertices()) {
            count++;
        }
        return count - 2 - countDeletedNodes;
    }

    @Override
    public long countEdges() {
        int countDeletedNodes = 0;
        for (Edge edge : getDeletionVertex().getEdges(com.tinkerpop.blueprints.Direction.OUT, DELETION_VERTEX)) {
            countDeletedNodes++;
        }
        int count = 0;
        for (Edge v : getEdges()) {
            count++;
        }
        return count - 1 - countDeletedNodes;
    }

    @Override
    public boolean hasEdgeBeenDeleted(Edge edge) {
        //This method is only a problem for neo4j indexes
        return false;
    }

    @Override
    public boolean isTransactionActive() {
        //TODO
        return false;
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
