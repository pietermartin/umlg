package org.umlg.runtime.adaptor;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UmlgOrientDbGraphFactory implements UmlgGraphFactory {

    private static final Logger logger = Logger.getLogger(UmlgOrientDbGraphFactory.class.getPackage().getName());
    public static UmlgOrientDbGraphFactory INSTANCE = new UmlgOrientDbGraphFactory();
    private UmlgGraph umlgGraph;
    private PropertiesConfiguration propertiesConfiguration;

    private UmlgOrientDbGraphFactory() {
    }

    public static UmlgGraphFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public UmlgGraph getTumlGraph(String url) {
        File f = new File(url);
        TransactionThreadEntityVar.remove();
        if (!f.exists()) {
            try {
                this.propertiesConfiguration = new PropertiesConfiguration("orientdb.properties");
            } catch (ConfigurationException e) {
                throw new RuntimeException(e);
            }
            this.propertiesConfiguration.addProperty("blueprints.orientdb.url", "local:" + f.getAbsolutePath());
            try {
                this.umlgGraph = new UmlgOrientDbGraph(this.propertiesConfiguration);
//            UmlgSchemaCreatorFactory.getUmlgSchemaCreator().createVertexSchemas(new VertexSchemaCreatorImpl());
                UmlgSchemaCreatorFactory.getUmlgSchemaCreator().createEdgeSchemas(new EdgeSchemaCreatorImpl());
                //New graph
                this.umlgGraph.addRoot();
                //Can not do a regular commit here as the this.transactionEventHandler.beforeCommit() calls getRoot()
                UmlgOrientDbGraph rawGraph = (UmlgOrientDbGraph) this.umlgGraph;
                rawGraph.getRawGraph().commit();
                UmlGIndexFactory.getUmlgIndexManager().createIndexes();
                this.umlgGraph.addDeletionNode();
                UmlgMetaNodeFactory.getUmlgMetaNodeManager().createAllMetaNodes();
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
            this.umlgGraph = new UmlgOrientDbGraph(this.propertiesConfiguration);
        }
        //Prepare groovy
        GroovyExecutor ge = GroovyExecutor.INSTANCE;
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
