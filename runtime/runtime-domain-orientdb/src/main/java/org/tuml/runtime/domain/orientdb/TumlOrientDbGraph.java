package org.tuml.runtime.domain.orientdb;

import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.tuml.runtime.adaptor.BaseTumlGraph;
import org.tuml.runtime.adaptor.TumlGraph;
import org.tuml.runtime.adaptor.TumlTinkerIndex;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 2013/01/06
 * Time: 12:49 PM
 */
public class TumlOrientDbGraph extends BaseTumlGraph implements TumlGraph {

    private OrientGraph orientGraph;
    private static final String VERTEX_ID_COUNT = "vertexIdCount";

    public TumlOrientDbGraph(OrientGraph orientGraph) {
        super(orientGraph);
        this.orientGraph = orientGraph;
    }

    public Long getNextVertexId() {
        ODocument vertexIdCountDoc = this.orientGraph.getRawGraph().getRoot(VERTEX_ID_COUNT);
        Long count = (Long) vertexIdCountDoc.field("count");
        vertexIdCountDoc.field("count", count + 1);
        return count;
    }

    @Override
    public Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels) {
        OrientVertex ov1 = (OrientVertex) v1;
        OrientVertex ov2 = (OrientVertex) v2;
        Set<OIdentifiable> rawResult = this.orientGraph.getRawGraph().getEdgesBetweenVertexes(ov1.getRawVertex(), ov2.getRawVertex(), labels);
        Set<Edge> result = new HashSet<Edge>(rawResult.size());
        for (OIdentifiable oIdentifiable : rawResult) {
            rawResult.add(new OrientEdge(this.orientGraph, (ODocument) oIdentifiable));
        }
        return result;
    }

    @Override
    public void addRoot() {
        ODocument root = this.orientGraph.getRawGraph().createVertex();
        root.field("transactionCount", 1);
        this.orientGraph.getRawGraph().setRoot("root", root);

        ODocument vertexIdCountDoc = this.orientGraph.getRawGraph().createVertex();
        vertexIdCountDoc.field("count", 1L);
        this.orientGraph.getRawGraph().setRoot(VERTEX_ID_COUNT, vertexIdCountDoc);

    }

    @Override
    public Vertex getRoot() {
        return new OrientVertex(this.orientGraph, this.orientGraph.getRawGraph().getRoot("root"));
    }

    @Override
    public long countVertices() {
        return this.orientGraph.getRawGraph().countVertexes() - 2;
    }

    @Override
    public long countEdges() {
        return this.orientGraph.getRawGraph().countEdges();
    }

    @Override
    public void registerListeners() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TransactionManager getTransactionManager() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resume(Transaction t) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Transaction suspend() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Transaction getTransaction() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends Element> TumlTinkerIndex<T> createIndex(String indexName, Class<T> indexClass) {
        return new TumlOrientDbIndex<T>(this.orientGraph.createIndex(indexName, indexClass));
    }

    @Override
    public <T extends Element> Index<T> createIndex(String indexName, Class<T> indexClass, Parameter... indexParameters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends Element> TumlTinkerIndex<T> getIndex(String indexName, Class<T> indexClass) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasEdgeBeenDeleted(Edge edge) {
        //This method is only a problem for neo4j indexes
        return false;
    }
}