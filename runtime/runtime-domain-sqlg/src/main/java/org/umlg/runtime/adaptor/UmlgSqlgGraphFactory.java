package org.umlg.runtime.adaptor;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.umlg.runtime.util.UmlgProperties;
import org.umlg.sqlg.sql.dialect.SqlDialect;
import org.umlg.sqlg.structure.SchemaManager;
import org.umlg.sqlg.structure.SqlgGraph;

import java.io.File;
import java.sql.*;
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
            if (!sqlgGraph.getSchemaManager().tableExist(sqlgGraph.getSqlDialect().getPublicSchema(), SchemaManager.VERTEX_PREFIX + UmlgGraph.ROOT_VERTEX)) {
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
//        sqlgGraph.getSchemaManager().close();
        SqlDialect sqlDialect = sqlgGraph.getSqlDialect();
        try (Connection conn = sqlgGraph.getSqlgDataSource().getDatasource().getConnection()) {
            DatabaseMetaData metadata = conn.getMetaData();
            String catalog = null;
            String schemaPattern = null;
            String tableNamePattern = "%";
            String[] types = {"TABLE"};
            ResultSet result = metadata.getTables(catalog, schemaPattern, tableNamePattern, types);
            while (result.next()) {
                StringBuilder sql = new StringBuilder("DROP TABLE ");
                String schema = result.getString(2);
                String table = result.getString(3);
                if (sqlDialect.getGisSchemas().contains(schema) || sqlDialect.getSpacialRefTable().contains(table)) {
                    continue;
                }
                sql.append(sqlDialect.maybeWrapInQoutes(schema));
                sql.append(".");
                sql.append(sqlDialect.maybeWrapInQoutes(table));
                sql.append(" CASCADE");
                if (sqlDialect.needsSemicolon()) {
                    sql.append(";");
                }
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {
                    preparedStatement.executeUpdate();
                }
            }
            catalog = null;
            schemaPattern = null;
            result = metadata.getSchemas(catalog, schemaPattern);
            while (result.next()) {
                String schema = result.getString(1);
                if (sqlDialect.getGisSchemas().contains(schema)) {
                    continue;
                }
                if (!sqlDialect.getDefaultSchemas().contains(schema)) {
                    StringBuilder sql = new StringBuilder("DROP SCHEMA ");
                    sql.append(sqlDialect.maybeWrapInQoutes(schema));
                    sql.append(" CASCADE");
                    if (sqlDialect.needsSemicolon()) {
                        sql.append(";");
                    }
                    try (PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {
                        preparedStatement.executeUpdate();
                    }
                }
            }
            sqlgGraph.getSqlgDataSource().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            sqlgGraph.close();
            UMLG.remove();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        this.umlgGraph = null;
    }

}
