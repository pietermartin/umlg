package org.umlg.runtime.adaptor;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date: 2013/08/31
 * Time: 3:31 PM
 */
public class UmlgThunderGraphFactory implements UmlgGraphFactory {

    private static final Logger logger = Logger.getLogger(UmlgThunderGraphFactory.class.getPackage().getName());
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
                try {
                    this.umlgGraph = new UmlgThunderGraph(f);
                    ((UmlgAdminGraph)this.umlgGraph).addRoot();
                    ((UmlgAdminGraph)this.umlgGraph).addDeletionNode();
                    this.umlgGraph.commit();
                    UmlgMetaNodeFactory.getUmlgMetaNodeManager().createAllMetaNodes();
                    UmlGIndexFactory.getUmlgIndexManager().createIndexes();
                    this.umlgGraph.commit();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Could not start titan db!", e);
                    if (this.umlgGraph != null) {
                        this.umlgGraph.rollback();
                    }
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                this.umlgGraph = new UmlgThunderGraph(f);
            }
            //Prepare groovy
            GroovyExecutor ge = GroovyExecutor.INSTANCE;
        }
        return this.umlgGraph;
    }


    @Override
    public void shutdown() {
        if (this.umlgGraph != null) {
            this.umlgGraph.rollback();
            this.umlgGraph.shutdown();
        }
    }

    @Override
    public void clear() {
        this.umlgGraph = null;
    }

}
