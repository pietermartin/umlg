package org.umlg.runtime.adaptor;

import java.io.File;

public class UmlgOrientDbGraphFactory implements UmlgGraphFactory {

    public static UmlgOrientDbGraphFactory INSTANCE = new UmlgOrientDbGraphFactory();
    private TumlGraph tumlGraph;

    private UmlgOrientDbGraphFactory() {
    }

    public static UmlgGraphFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public TumlGraph getTumlGraph(String url) {
        File f = new File(url);
        TransactionThreadEntityVar.remove();
        if (!f.exists()) {
            this.tumlGraph = new TumlOrientDbGraph("local:" + f.getAbsolutePath());
            //New graph
            this.tumlGraph.addRoot();
            this.tumlGraph.commit();
            this.tumlGraph.addDeletionNode();
            TumlMetaNodeFactory.getTumlMetaNodeManager().createAllMetaNodes();
            TumlIndexFactory.getTumlIndexManager().createIndexes();
            this.tumlGraph.commit();
        } else {
            this.tumlGraph = new TumlOrientDbGraph("local:" + f.getAbsolutePath());
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
