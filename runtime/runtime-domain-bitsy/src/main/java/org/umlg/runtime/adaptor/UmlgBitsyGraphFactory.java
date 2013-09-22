package org.umlg.runtime.adaptor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Neo4j db is a singleton
 */
public class UmlgBitsyGraphFactory implements UmlgGraphFactory {

    public static UmlgBitsyGraphFactory INSTANCE = new UmlgBitsyGraphFactory();
    private UmlgGraph umlgGraph;

    private UmlgBitsyGraphFactory() {
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
                f.mkdir();
                Path dbPath = Paths.get(f.getAbsolutePath());
                this.umlgGraph = new UmlgBitsyGraph(dbPath);
                this.umlgGraph.addRoot();
                this.umlgGraph.addDeletionNode();
                this.umlgGraph.commit();
                UmlgMetaNodeFactory.getUmlgMetaNodeManager().createAllMetaNodes();
                UmlGIndexFactory.getUmlgIndexManager().createIndexes();
                this.umlgGraph.commit();
            } else {
                Path dbPath = Paths.get(f.getAbsolutePath());
                this.umlgGraph = new UmlgBitsyGraph(dbPath);
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
