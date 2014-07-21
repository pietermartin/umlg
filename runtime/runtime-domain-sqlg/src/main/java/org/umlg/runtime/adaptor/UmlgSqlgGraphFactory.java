package org.umlg.runtime.adaptor;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.umlg.sqlgraph.sql.dialect.SqlGraphDialect;
import org.umlg.sqlgraph.structure.SchemaManager;
import org.umlg.sqlgraph.structure.SqlGraphDataSource;
import org.umlg.sqlgraph.test.JDBC;

import java.beans.PropertyVetoException;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Neo4j db is a singleton
 */
public class UmlgSqlgGraphFactory implements UmlgGraphFactory {

    private static final Logger logger = Logger.getLogger(UmlgSqlgGraphFactory.class.getPackage().getName());
    public static UmlgSqlgGraphFactory INSTANCE = new UmlgSqlgGraphFactory();
    private UmlgSqlgGraph umlgGraph;
    private Configuration config;

    private UmlgSqlgGraphFactory() {
    }

    public static UmlgGraphFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public UmlgGraph getTumlGraph(String url) {
        URL sqlProperties = Thread.currentThread().getContextClassLoader().getResource("sqlgraph.properties");
        try {
            this.config = new PropertiesConfiguration(sqlProperties);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
        try {
            Class.forName(SqlGraphDialect.POSTGRES.getJdbcDriver());
            SqlGraphDialect sqlGraphDialect = SqlGraphDialect.POSTGRES;
            SqlGraphDataSource.INSTANCE.setupDataSource(
                    sqlGraphDialect.getJdbcDriver(),
                    this.config.getString("jdbc.url"),
                    this.config.getString("jdbc.username"),
                    this.config.getString("jdbc.password"));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (this.umlgGraph == null) {
            TransactionThreadEntityVar.remove();
            try (Connection conn = SqlGraphDataSource.INSTANCE.get(config.getString("jdbc.url")).getConnection()) {
                if (!tableExist(conn, SchemaManager.VERTICES)) {
                    try {
                        this.umlgGraph = new UmlgSqlgGraph(config);
                        this.umlgGraph.addRoot();
                        this.umlgGraph.addDeletionNode();
                        this.umlgGraph.commit();
                        UmlgMetaNodeFactory.getUmlgMetaNodeManager().createAllMetaNodes();
                        this.umlgGraph.commit();
                        //This is to bypass the beforeCommit
                        this.umlgGraph.setBypass(true);
                        UmlGIndexFactory.getUmlgIndexManager().createIndexes();
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
                } else {
                    this.umlgGraph = new UmlgSqlgGraph(config);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
    public void drop() {
        this.umlgGraph.rollback();
        try (Connection conn = SqlGraphDataSource.INSTANCE.get(this.config.getString("jdbc.url")).getConnection()) {
            DatabaseMetaData metadata = conn.getMetaData();
            String catalog = "sqlgraphdb";
            String schemaPattern = null;
            String tableNamePattern = "%";
            String[] types = {"TABLE"};
            ResultSet result = metadata.getTables(catalog, schemaPattern, tableNamePattern, types);
            while (result.next()) {
                StringBuilder sql = new StringBuilder("DROP TABLE ");
                sql.append(SqlGraphDialect.POSTGRES.getSqlDialect().maybeWrapInQoutes(result.getString(3)));
                sql.append(" CASCADE");
                if (SqlGraphDialect.POSTGRES.getSqlDialect().needsSemicolon()) {
                    sql.append(";");
                }
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        this.umlgGraph = null;
    }

    private boolean tableExist(Connection conn, String table) {
        DatabaseMetaData metadata;
        try {
            metadata = conn.getMetaData();
            String catalog = null;
            String schemaPattern = null;
            String tableNamePattern = table;
            String[] types = null;
            ResultSet result = metadata.getTables(catalog, schemaPattern, tableNamePattern, types);
            while (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
