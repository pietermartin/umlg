package org.umlg.runtime.test;

import java.net.URL;
import java.util.logging.LogManager;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.umlg.runtime.adaptor.*;

public class BaseLocalDbTest {

	protected UmlgGraph db;

	@BeforeClass
	public static void beforeClass() {
		try {
			URL url = BaseLocalDbTest.class.getResource("/logging.properties");
			LogManager.getLogManager().readConfiguration(url.openStream());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Before
	public void before() {
        UmlgGraphManager.INSTANCE.deleteGraph();
		this.db = GraphDb.getDb();
	}

    @After
    public void after() {
        this.db.rollback();
        UmlgGraphManager.INSTANCE.deleteGraph();
        GraphDb.remove();
        TransactionThreadVar.remove();
        TransactionThreadEntityVar.remove();
        TransactionThreadMetaNodeVar.remove();
    }

	protected long countVertices() {
		return this.db.countVertices() - UmlgMetaNodeFactory.getUmlgMetaNodeManager().count();
	}

	protected long countEdges() {
		return this.db.countEdges() - UmlgMetaNodeFactory.getUmlgMetaNodeManager().count();
	}

}
