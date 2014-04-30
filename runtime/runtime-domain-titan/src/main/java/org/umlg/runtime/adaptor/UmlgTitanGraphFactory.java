package org.umlg.runtime.adaptor;

import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date: 2013/08/31
 * Time: 3:31 PM
 */
public class UmlgTitanGraphFactory implements UmlgGraphFactory {

    private static final Logger logger = Logger.getLogger(UmlgTitanGraphFactory.class.getPackage().getName());
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
                try {
                    this.umlgGraph = new UmlgTitanGraph(new GraphDatabaseConfiguration(this.propertiesConfiguration));
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
                        throw (RuntimeException)e;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                this.umlgGraph = new UmlgTitanGraph(new GraphDatabaseConfiguration(this.propertiesConfiguration));
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
