package org.umlg.runtime.adaptor;

import java.io.File;

/**
 * Neo4j db is a singleton
 */
public class UmlgNeo4jGraphFactory implements UmlgGraphFactory {

    public static UmlgNeo4jGraphFactory INSTANCE = new UmlgNeo4jGraphFactory();
    private TumlGraph tumlGraph;

    private UmlgNeo4jGraphFactory() {
    }

    public static UmlgGraphFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public TumlGraph getTumlGraph(String url) {
        if (this.tumlGraph == null) {
            File f = new File(url);
            TransactionThreadEntityVar.remove();
            if (!f.exists()) {
                this.tumlGraph = new TumlNeo4jGraph(f.getAbsolutePath());
                this.tumlGraph.addRoot();
                this.tumlGraph.addDeletionNode();
                this.tumlGraph.commit();
                TumlMetaNodeFactory.getTumlMetaNodeManager().createAllMetaNodes();
                TumlIndexFactory.getTumlIndexManager().createIndexes();
                this.tumlGraph.commit();
            } else {
                this.tumlGraph = new TumlNeo4jGraph(f.getAbsolutePath());
            }
        }
        return this.tumlGraph;
    }


    @Override
    public void shutdown() {
        if (this.tumlGraph != null) {
            this.tumlGraph.shutdown();
        }
    }

    @Override
    public void drop() {
        if (this.tumlGraph != null) {
            this.tumlGraph.drop();
            this.tumlGraph = null;
        }
    }

}
