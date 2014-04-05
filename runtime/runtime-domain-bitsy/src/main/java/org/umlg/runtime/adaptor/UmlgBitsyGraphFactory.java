package org.umlg.runtime.adaptor;

import com.lambdazen.bitsy.BitsyGraph;
import com.lambdazen.bitsy.wrapper.BitsyAutoReloadingGraph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Neo4j db is a singleton
 */
public class UmlgBitsyGraphFactory implements UmlgGraphFactory {

    private static final Logger logger = Logger.getLogger(UmlgBitsyGraphFactory.class.getPackage().getName());
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
                BitsyGraph bitsyGraph = new BitsyGraph(dbPath);
                try {
                    this.umlgGraph = new UmlgBitsyGraph(bitsyGraph);
                    this.umlgGraph.addRoot();
                    this.umlgGraph.addDeletionNode();
                    this.umlgGraph.commit();
                    UmlgMetaNodeFactory.getUmlgMetaNodeManager().createAllMetaNodes();
                    UmlGIndexFactory.getUmlgIndexManager().createIndexes();
                    this.umlgGraph.commit();
                    this.umlgGraph.commit();
                    //Prepare groovy
                    GroovyExecutor ge = GroovyExecutor.INSTANCE;
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
                Path dbPath = Paths.get(f.getAbsolutePath());
                BitsyGraph bitsyGraph = new BitsyGraph(dbPath);
                this.umlgGraph = new UmlgBitsyGraph(bitsyGraph);
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
