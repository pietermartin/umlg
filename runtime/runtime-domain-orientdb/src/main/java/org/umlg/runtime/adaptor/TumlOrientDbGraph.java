package org.umlg.runtime.adaptor;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.db.record.ODatabaseRecord;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.umlg.runtime.domain.PersistentObject;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Date: 2013/01/09
 * Time: 7:25 PM
 */
public class TumlOrientDbGraph extends OrientGraph implements TumlGraph {

    private static final String VERTEX_ID_COUNT = "vertexIdCount";
    private UmlgTransactionEventHandler transactionEventHandler;
    private static final Logger logger = Logger.getLogger(TumlOrientDbGraph.class.getPackage().getName());

    public TumlOrientDbGraph(OGraphDatabase iDatabase) {
        super(iDatabase);
        this.transactionEventHandler = new UmlgTransactionEventHandlerImpl();
    }

    public TumlOrientDbGraph(String url) {
        super(url);
        this.transactionEventHandler = new UmlgTransactionEventHandlerImpl();
    }

    public TumlOrientDbGraph(String url, String username, String password) {
        super(url, username, password);
        this.transactionEventHandler = new UmlgTransactionEventHandlerImpl();
    }

    @Override
    public void commit() {
        try {
            this.transactionEventHandler.beforeCommit();
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

    public OrientEdge addEdge(final Object id, final Vertex outVertex,
                              final Vertex inVertex, final String label) {
        return super.addEdge(id, outVertex, inVertex, label);
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
    public Vertex addVertex(String className) {
        Vertex v = super.addVertex(null);
        if (className != null) {
            v.setProperty("className", className);
        }
        return v;
    }

    @Override
    public void removeVertex(final Vertex vertex) {
        this.autoStartTransaction();
        Iterable<Edge> edges = vertex.getEdges(Direction.BOTH);
        for (final Edge edge : edges) {
            edge.remove();
        }
        if (!vertex.getId().equals(new Long(0))) {
            getRoot().addEdge(DELETED_NODES, vertex);
            vertex.setProperty("deleted", true);
        } else {
            vertex.remove();
        }
    }

    @Override
    public Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels) {
        OrientVertex ov1 = (OrientVertex) v1;
        OrientVertex ov2 = (OrientVertex) v2;

        OGraphDatabase oGraphDatabase = (OGraphDatabase)this.getRawGraph();
        Set<OIdentifiable> rawResult = oGraphDatabase.getEdgesBetweenVertexes(ov1.getIdentity(), ov2.getIdentity(), labels);
        Set<Edge> result = new HashSet<Edge>(rawResult.size());
        for (OIdentifiable oIdentifiable : rawResult) {
            result.add(new OrientEdge(this, oIdentifiable));
        }
        return result;
    }

    @Override
    public Vertex getRoot() {
        return query().has("UmlGRoot", "UmlGRoot").vertices().iterator().next();
    }

    @Override
    public void addRoot() {
        Vertex root = addVertex("root");
        root.setProperty("UmlGRoot", "UmlGRoot");
        root.setProperty("transactionCount", 1);
    }

    @Override
    public long countVertices() {
        int countDeletedNodes = 0;
        for (Edge edge : getRoot().getEdges(com.tinkerpop.blueprints.Direction.OUT, DELETED_NODES)) {
            countDeletedNodes++;
        }
        return super.countVertices() - 1 - countDeletedNodes;
    }

    @Override
    public long countEdges() {
        int countDeletedNodes = 0;
        for (Edge edge : getRoot().getEdges(com.tinkerpop.blueprints.Direction.OUT, DELETED_NODES)) {
            countDeletedNodes++;
        }
        return super.countEdges() - countDeletedNodes;
    }

    @Override
    public void addDeletionNode() {
        Vertex v = addVertex(null);
        addEdge(null, getRoot(), v, DELETED_NODES);
    }

    @Override
    public void shutdown() {
        try {
            commit();
        } catch (Exception e) {
            rollback();
        }
        super.shutdown();
    }

    @Override
    public void drop() {
        this.getRawGraph().drop();
    }

    @Override
    public boolean hasEdgeBeenDeleted(Edge edge) {
        //This method is only a problem for neo4j indexes
        return false;
    }

    @Override
    public void clearTxThreadVar() {
        final ODatabaseRecord tlDb = ODatabaseRecordThreadLocal.INSTANCE
                .getIfDefined();
        if (tlDb != null) {
            logger.warning("Transaction threadvar was not empty!!!!! Bug somewhere in clearing the transaction!!!");
//            rollback();
//            throw new IllegalStateException("Transaction thread var is not empty!!!");
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
    public String executeQuery(TumlQueryEnum tumlQueryEnum, Long contextId, String query) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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

}
