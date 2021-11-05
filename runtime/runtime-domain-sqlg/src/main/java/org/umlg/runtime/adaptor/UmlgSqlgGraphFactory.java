package org.umlg.runtime.adaptor;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.umlg.runtime.util.UmlgProperties;
import org.umlg.sqlg.structure.SqlgGraph;
import org.umlg.sqlg.util.SqlgUtil;

import java.io.File;
import java.net.URL;
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
                this.umlgGraph.sqlG.close();
                this.clear();
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
            Configurations configs = new Configurations();
            URL URL = Thread.currentThread().getContextClassLoader().getResource("sqlg.properties");
            try {
                if (URL != null) {
                    this.configuration = configs.properties(URL);
                    logger.info("loading sqlg.properties from the classpath");
                } else {
                    System.out.println(new File(".").getAbsolutePath());
                    String[] propertiesFileLoacation = UmlgProperties.INSTANCE.getSqlgPropertiesLocation();
                    boolean foundPropertiesFile = false;
                    for (String location : propertiesFileLoacation) {
                        File propertiesFile = new File(location);
                        if (propertiesFile.exists()) {
                            this.configuration = configs.properties(propertiesFile);
                            foundPropertiesFile = true;
                            logger.info(String.format("loading sqlg.properties from the %s", propertiesFile.getAbsolutePath()));
                            break;
                        }
                    }
                    if (!foundPropertiesFile) {
                        throw new RuntimeException("sqlg.properties must be on the classpath or umlg.env.properties must specify its location in a property sqlg.properties.location");
                    }

                }
            } catch (ConfigurationException e) {
                throw new RuntimeException("sqlg.properties must be on the classpath or umlg.env.properties must specify its location in a property sqlg.properties.location", e);
            }
            TransactionThreadEntityVar.remove();
            TransactionThreadBypassValidationVar.remove();
            TransactionThreadNotificationVar.remove();
            SqlgGraph sqlgGraph = SqlgGraph.open(configuration);
            this.umlgGraph = new UmlgSqlgGraph(sqlgGraph);
            if (!sqlgGraph.getTopology().getVertexLabel(sqlgGraph.getSqlDialect().getPublicSchema(), UmlgGraph.ROOT_VERTEX).isPresent()) {
                try {
//                    this.umlgGraph.addRoot();
                    this.umlgGraph.commit();
                    //This is to bypass the beforeCommit
                    this.umlgGraph.bypassValidation();
                    UmlGIndexFactory.getUmlgIndexManager().createIndexes();
                    if (this.configuration.getBoolean("generate.meta.nodes", false)) {
                        UmlgMetaNodeFactory.getUmlgMetaNodeManager().createAllMetaNodes();
                    }
                    this.umlgGraph.commit();
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
//            GroovyExecutor ge = GroovyExecutor.INSTANCE;
        }
        return this.umlgGraph;
    }

    @Override
    public void drop() {
        this.umlgGraph.rollback();
        SqlgGraph sqlgGraph = (SqlgGraph) this.umlgGraph.getUnderlyingGraph();
        SqlgUtil.dropDb(sqlgGraph);
        sqlgGraph.tx().commit();
        sqlgGraph.close();
    }

    @Override
    public void clear() {
        this.umlgGraph = null;
    }

}
