package org.umlg.runtime.adaptor;

import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;
import com.thinkaurelius.titan.graphdb.database.StandardTitanGraph;
import com.tinkerpop.blueprints.*;
import org.apache.commons.io.FileUtils;
import org.umlg.runtime.util.UmlgProperties;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Date: 2013/08/31
 * Time: 3:31 PM
 */
public class UmlgTitanGraph extends StandardTitanGraph implements TumlGraph {

    private static final Logger logger = Logger.getLogger(UmlgTitanGraph.class.getPackage().getName());

    public UmlgTitanGraph(GraphDatabaseConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void incrementTransactionCount() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getTransactionCount() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addRoot() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Vertex getRoot() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Vertex addVertex(String className) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long countVertices() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long countEdges() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void registerListeners() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T instantiateClassifier(Long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasEdgeBeenDeleted(Edge edge) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearTxThreadVar() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearThreadVars() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addDeletionNode() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String executeQuery(TumlQueryEnum tumlQueryEnum, Long contextId, String query) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends Element> Index<T> createIndex(String s, Class<T> tClass, Parameter... parameters) {
        throw new RuntimeException();
    }

    @Override
    public <T extends Element> Index<T> getIndex(String s, Class<T> tClass) {
        throw new RuntimeException();
    }

    @Override
    public Iterable<Index<? extends Element>> getIndices() {
        throw new RuntimeException();
    }

    @Override
    public void dropIndex(String s) {
        throw new RuntimeException();
    }

    @Override
    public void drop() {
        this.shutdown();
        //Delete the files
        String dbUrl = UmlgProperties.INSTANCE.getTumlDbLocation();
        String parsedUrl = dbUrl;
        if (dbUrl.startsWith("local:")) {
            parsedUrl = dbUrl.replace("local:", "");
        }
        File dir = new File(parsedUrl);
        if (dir.exists()) {
            try {
                logger.info(String.format("Deleting dir %s", new Object[]{dir.getAbsolutePath()}));
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
