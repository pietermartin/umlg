package org.umlg.runtime.adaptor;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Neo4j db is a singleton
 */
public class UmlgNeo4jGraphFactory implements UmlgGraphFactory {

    private static final Logger logger = Logger.getLogger(UmlgNeo4jGraphFactory.class.getPackage().getName());
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
                try {
                    this.umlgGraph = new UmlgNeo4jGraph(f.getAbsolutePath());
                    this.umlgGraph.addRoot();
                    this.umlgGraph.addDeletionNode();
                    this.umlgGraph.commit();
                    UmlgMetaNodeFactory.getUmlgMetaNodeManager().createAllMetaNodes();
                    UmlGIndexFactory.getUmlgIndexManager().createIndexes();
                    this.umlgGraph.commit();
                    //Prepare groovy
                    GroovyExecutor ge = GroovyExecutor.INSTANCE;
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Could not start titan db!", e);
                    if (this.umlgGraph != null) {
                        this.umlgGraph.rollback();
                    }
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException)e;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                this.umlgGraph = new UmlgNeo4jGraph(f.getAbsolutePath());
            }
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
