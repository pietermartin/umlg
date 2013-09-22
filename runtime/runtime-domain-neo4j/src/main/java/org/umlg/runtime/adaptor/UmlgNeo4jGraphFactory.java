package org.umlg.runtime.adaptor;

import java.io.File;

/**
 * Neo4j db is a singleton
 */
public class UmlgNeo4jGraphFactory implements UmlgGraphFactory {

    public static UmlgNeo4jGraphFactory INSTANCE = new UmlgNeo4jGraphFactory();
    private UmlgGraph umlgGraph;

    private UmlgNeo4jGraphFactory() {
    }

    public static UmlgGraphFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public UmlgGraph getTumlGraph(String url) {
        if (this.umlgGraph == null) {
            File f = new File(url);
            TransactionThreadEntityVar.remove();
            if (!f.exists()) {
                this.umlgGraph = new UmlgNeo4jGraph(f.getAbsolutePath());
                this.umlgGraph.addRoot();
                this.umlgGraph.addDeletionNode();
                this.umlgGraph.commit();
                UmlgMetaNodeFactory.getUmlgMetaNodeManager().createAllMetaNodes();
                UmlGIndexFactory.getUmlgIndexManager().createIndexes();
                this.umlgGraph.commit();
            } else {
                this.umlgGraph = new UmlgNeo4jGraph(f.getAbsolutePath());
            }
        }
        return this.umlgGraph;
    }


    @Override
    public void shutdown() {
        if (this.umlgGraph != null) {
            this.umlgGraph.shutdown();
        }
    }

    @Override
    public void drop() {
        if (this.umlgGraph != null) {
            this.umlgGraph.drop();
            this.umlgGraph = null;
        }
    }

}
