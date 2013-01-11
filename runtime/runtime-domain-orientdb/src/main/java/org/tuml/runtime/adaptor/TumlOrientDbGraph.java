package org.tuml.runtime.adaptor;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.HashSet;
import java.util.Set;

/**
 * Date: 2013/01/09
 * Time: 7:25 PM
 */
public class TumlOrientDbGraph extends OrientGraph implements TumlGraph {

    private static final String VERTEX_ID_COUNT = "vertexIdCount";

    public TumlOrientDbGraph(OGraphDatabase iDatabase) {
        super(iDatabase);
    }

    public TumlOrientDbGraph(String url) {
        super(url);
    }

    public TumlOrientDbGraph(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Integer suspend() {
        return OrientDbBlueprintTransactionManager.INSTANCE.suspend();
    }

    @Override
    public TransactionalGraph resume(Integer transactionNumber) {
        return OrientDbBlueprintTransactionManager.INSTANCE.resume(transactionNumber);
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
        return new OrientVertex(this, this.getRawGraph().getRoot("root"));
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
        OrientVertex ov1 = (OrientVertex) v1;
        OrientVertex ov2 = (OrientVertex) v2;
        Set<OIdentifiable> rawResult = this.getRawGraph().getEdgesBetweenVertexes(ov1.getRawVertex(), ov2.getRawVertex(), labels);
        Set<Edge> result = new HashSet<Edge>(rawResult.size());
        for (OIdentifiable oIdentifiable : rawResult) {
            rawResult.add(new OrientEdge(this, (ODocument) oIdentifiable));
        }
        return result;
    }

    @Override
    public void addRoot() {
        ODocument root = this.getRawGraph().createVertex();
        root.field("transactionCount", 1);
        this.getRawGraph().setRoot("root", root);

        ODocument vertexIdCountDoc = this.getRawGraph().createVertex();
        vertexIdCountDoc.field("count", 1L);
        this.getRawGraph().setRoot(VERTEX_ID_COUNT, vertexIdCountDoc);
    }

    @Override
    public long countVertices() {
        return this.getRawGraph().countVertexes() - 2;
    }

    @Override
    public long countEdges() {
        return this.getRawGraph().countEdges();
    }

    @Override
    public void registerListeners() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T instantiateClassifier(Long id) {
        try {
            Vertex v = this.getVertex(id);
            // TODO reimplement schemaHelper
            Class<?> c = Class.forName((String) v.getProperty("className"));
            // Class<?> c = schemaHelper.getClassNames().get((String)
            // v.getProperty("className"));
            return (T) c.getConstructor(Vertex.class).newInstance(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends Element> TumlTinkerIndex<T> createIndex(String indexName, Class<T> indexClass) {
        return new TumlOrientDbIndex(this, this.createIndex(indexName, indexClass));
    }

    @Override
    public <T extends Element> TumlTinkerIndex<T> getIndex(String indexName, Class<T> indexClass) {
        Index<T> index = super.getIndex(indexName, indexClass);
        if (index != null) {
            return new TumlOrientDbIndex(this, index);
        } else {
            return null;
        }
    }

    @Override
    public boolean hasEdgeBeenDeleted(Edge edge) {
        //This method is only a problem for neo4j indexes
        return false;
    }

}
