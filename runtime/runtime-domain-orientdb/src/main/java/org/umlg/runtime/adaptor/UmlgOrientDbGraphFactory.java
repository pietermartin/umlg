package org.umlg.runtime.adaptor;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;

public class UmlgOrientDbGraphFactory implements UmlgGraphFactory {

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
            this.umlgGraph = new UmlgOrientDbGraph(this.propertiesConfiguration);
//            UmlgSchemaCreatorFactory.getUmlgSchemaCreator().createVertexSchemas(new VertexSchemaCreatorImpl());
            UmlgSchemaCreatorFactory.getUmlgSchemaCreator().createEdgeSchemas(new EdgeSchemaCreatorImpl());
            //New graph
            this.umlgGraph.addRoot();
            //Can not do a regular commit here as the this.transactionEventHandler.beforeCommit() calls getRoot()
            UmlgOrientDbGraph rawGraph = (UmlgOrientDbGraph)this.umlgGraph;
            rawGraph.getRawGraph().commit();
            UmlGIndexFactory.getUmlgIndexManager().createIndexes();
            this.umlgGraph.addDeletionNode();
            UmlgMetaNodeFactory.getUmlgMetaNodeManager().createAllMetaNodes();
            this.umlgGraph.commit();
        } else {
            this.umlgGraph = new UmlgOrientDbGraph(this.propertiesConfiguration);
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
