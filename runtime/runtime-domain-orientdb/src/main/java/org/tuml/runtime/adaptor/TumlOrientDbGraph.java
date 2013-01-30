package org.tuml.runtime.adaptor;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.tuml.runtime.domain.PersistentObject;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

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
            result.add(new OrientEdge(this, (ODocument) oIdentifiable));
        }
        return result;
    }

    @Override
    public void addRoot() {
        if (this.getRawGraph().getRoot("root") == null) {
            ODocument root = this.getRawGraph().createVertex();
            root.field("transactionCount", 1);
            this.getRawGraph().setRoot("root", root);

//            ODocument vertexIdCountDoc = this.getRawGraph().createVertex();
//            vertexIdCountDoc.field("count", 1L);
//            this.getRawGraph().setRoot(VERTEX_ID_COUNT, vertexIdCountDoc);
        }
    }

    @Override
    public long countVertices() {
        return this.getRawGraph().countVertexes() - 1;
    }

    @Override
    public long countEdges() {
        return this.getRawGraph().countEdges();
    }

    @Override
    public void registerListeners() {
        getRawGraph().registerListener(new TumlOrientDbTransactionEventHandler<PersistentObject>());
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
    public <T extends Element> TumlTinkerIndex<T> createIndex(final String indexName, final Class<T> indexClass) {
        //This needs to happen in a separate thread otherwise orientdb will commit the current transaction
        ExecutorService es = Executors.newFixedThreadPool(1);
        Future<Index<T>> f = es.submit(new Callable<Index<T>>() {
            @Override
            public Index<T> call() throws Exception {
                TumlOrientDbGraph graph = (TumlOrientDbGraph) GraphDb.getDb();
                //OrientDb does not like ':'
                String transformedIndexName = indexName.replace(":", "_");
                Index<T> index = graph.createIndexInternal(transformedIndexName, indexClass);
                graph.shutdown();
                return index;
            }
        });
        es.shutdown();
        try {
            Index<T> index = f.get();
            TumlTinkerIndex<T> index2 = getIndex(indexName, indexClass);
            return index2;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends Element> Index<T> createIndexInternal(final String indexName, final Class<T> indexClass) {
        return super.createIndex(indexName, indexClass);
    }

    @Override
    public <T extends Element> TumlTinkerIndex<T> getIndex(String indexName, Class<T> indexClass) {
        //OrientDb does not like ':'
        String transformedIndexName = indexName.replace(":", "_");
        Index<T> index = super.getIndex(transformedIndexName, indexClass);
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

    @Override
    public void shutdown() {
        try {
            commit();
        } catch (Exception e) {
            rollback();
        }
        super.shutdown();
    }

}
