package org.umlg.runtime.test;

import org.apache.tinkerpop.gremlin.groovy.engine.GremlinExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.umlg.runtime.adaptor.GroovyExecutor;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgAdminGraph;
import org.umlg.runtime.adaptor.UmlgGraph;

import javax.management.relation.RoleUnresolved;
import java.net.URL;
import java.util.logging.LogManager;

public class BaseLocalDbTest {

    protected UmlgGraph db;

    @BeforeClass
    public static void beforeClass() {
        try {
            URL url = BaseLocalDbTest.class.getResource("/logging.properties");
            LogManager.getLogManager().readConfiguration(url.openStream());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Before
    public void before() throws Exception {
        ((UmlgAdminGraph) UMLG.get()).drop();
        this.db = UMLG.get();
    }

    @After
    public void after() {
    }

    protected long countVertices() {
        return ((UmlgAdminGraph) this.db).countVertices();
    }

    protected long countEdges() {
        return ((UmlgAdminGraph) this.db).countEdges();
    }

}
