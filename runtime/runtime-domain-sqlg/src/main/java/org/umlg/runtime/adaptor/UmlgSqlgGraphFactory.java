package org.umlg.runtime.adaptor;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.umlg.runtime.util.UmlgProperties;
import org.umlg.sqlg.structure.SqlgGraph;
import org.umlg.sqlg.util.SqlgUtil;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * sqlg db is a singleton
 */
public class UmlgSqlgGraphFactory implements UmlgGraphFactory {

    private static final Logger logger = Logger.getLogger(UmlgSqlgGraphFactory.class.getPackage().getName());
    public static UmlgSqlgGraphFactory INSTANCE = new UmlgSqlgGraphFactory();
    private UmlgSqlgGraph umlgGraph;
    private Configuration configuration;

    private UmlgSqlgGraphFactory() {
    }

    public static UmlgGraphFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public void shutdown() {
        if (this.umlgGraph != null) {
            this.umlgGraph.rollback();
            try {
                this.umlgGraph.close();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public UmlgGraph getTumlGraph(String url) {
        if (this.umlgGraph == null) {
            try {
                this.configuration = new PropertiesConfiguration("sqlg.properties");
                logger.info("loading sqlg.properties from the classpath");
            } catch (ConfigurationException e) {
                //if sqlgraph is not on the classpath, look in umlg.env.properties for its location
                try {
                    System.out.println(new File(".").getAbsolutePath());
                    String[] propertiesFileLoacation = UmlgProperties.INSTANCE.getSqlgPropertiesLocation();
                    boolean foundPropertiesFile = false;
                    for (String location : propertiesFileLoacation) {
                        File propertiesFile = new File(location);
                        if (propertiesFile.exists()) {
                            this.configuration = new PropertiesConfiguration(propertiesFile);
                            foundPropertiesFile = true;
                            logger.info(String.format("loading sqlg.properties from the %s", new String[]{propertiesFile.getAbsolutePath()}));
                            break;
                        }
                    }
                    if (!foundPropertiesFile) {
                        throw new RuntimeException("sqlg.properties must be on the classpath or umlg.env.properties must specify its location in a property sqlg.properties.location", e);
                    }
                } catch (ConfigurationException e1) {
                    throw new RuntimeException("sqlg.properties must be on the classpath or umlg.env.properties must specify its location in a property sqlg.properties.location", e);
                }
            }
            TransactionThreadEntityVar.remove();
            TransactionThreadNotificationVar.remove();
            SqlgGraph sqlgGraph = SqlgGraph.open(configuration);
            this.umlgGraph = new UmlgSqlgGraph(sqlgGraph);
            if (!sqlgGraph.getTopology().getVertexLabel(sqlgGraph.getSqlDialect().getPublicSchema(), UmlgGraph.ROOT_VERTEX).isPresent()) {
                try {
//                    this.umlgGraph.addRoot();
                    this.umlgGraph.commit();
                    //This is to bypass the beforeCommit
                    this.umlgGraph.setBypass(true);
                    UmlGIndexFactory.getUmlgIndexManager().createIndexes();
                    if (this.configuration.getBoolean("generate.meta.nodes", false)) {
                        UmlgMetaNodeFactory.getUmlgMetaNodeManager().createAllMetaNodes();
                    }
                    this.umlgGraph.commit();
                    this.umlgGraph.setBypass(false);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Could not start sqlg db!", e);
                    if (this.umlgGraph != null) {
                        this.umlgGraph.rollback();
                    }
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            }
            //Prepare groovy
            GroovyExecutor ge = GroovyExecutor.INSTANCE;
        }
        return this.umlgGraph;
    }

    @Override
    public void drop() {
        this.umlgGraph.rollback();
        SqlgGraph sqlgGraph = (SqlgGraph)this.umlgGraph.getUnderlyingGraph();
        SqlgUtil.dropDb(sqlgGraph);
        sqlgGraph.tx().commit();
        sqlgGraph.close();
    }

    @Override
    public void clear() {
        this.umlgGraph = null;
    }

}
