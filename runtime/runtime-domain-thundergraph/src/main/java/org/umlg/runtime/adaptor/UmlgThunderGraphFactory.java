package org.umlg.runtime.adaptor;

import java.io.File;

/**
 * Date: 2013/08/31
 * Time: 3:31 PM
 */
public class UmlgThunderGraphFactory implements UmlgGraphFactory {

    public static UmlgThunderGraphFactory INSTANCE = new UmlgThunderGraphFactory();
    private UmlgGraph umlgGraph;

    private UmlgThunderGraphFactory() {
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
                this.umlgGraph = new UmlgThunderGraph(f);
                this.umlgGraph.addRoot();
                this.umlgGraph.addDeletionNode();
                this.umlgGraph.commit();
                UmlgMetaNodeFactory.getUmlgMetaNodeManager().createAllMetaNodes();
                UmlGIndexFactory.getUmlgIndexManager().createIndexes();
                this.umlgGraph.commit();
            } else {
                this.umlgGraph = new UmlgThunderGraph(f);
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
