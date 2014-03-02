package org.umlg.runtime.adaptor;

import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;

/**
 * Date: 2013/08/31
 * Time: 3:31 PM
 */
public class UmlgTitanGraphFactory implements UmlgGraphFactory {

    public static UmlgTitanGraphFactory INSTANCE = new UmlgTitanGraphFactory();
    private UmlgGraph umlgGraph;
    private PropertiesConfiguration propertiesConfiguration;

    private UmlgTitanGraphFactory() {
    }

    public static UmlgGraphFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public UmlgGraph getTumlGraph(String url) {
        if (this.umlgGraph == null) {
            File f = new File(url);
            TransactionThreadEntityVar.remove();
            try {
                this.propertiesConfiguration = new PropertiesConfiguration("umlg.titan.properties");
            } catch (ConfigurationException e) {
                throw new RuntimeException(e);
            }
            this.propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
            if (!f.exists()) {
                this.umlgGraph = new UmlgTitanGraph(new GraphDatabaseConfiguration(this.propertiesConfiguration));
                this.umlgGraph.addRoot();
                this.umlgGraph.addDeletionNode();
                this.umlgGraph.commit();
                UmlgMetaNodeFactory.getUmlgMetaNodeManager().createAllMetaNodes();
                UmlGIndexFactory.getUmlgIndexManager().createIndexes();
                this.umlgGraph.commit();
            } else {
                this.umlgGraph = new UmlgTitanGraph(new GraphDatabaseConfiguration(this.propertiesConfiguration));
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
