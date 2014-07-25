package org.umlg.runtime.test;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgAdminGraph;
import org.umlg.runtime.adaptor.UmlgGraph;
import org.umlg.runtime.adaptor.UmlgMetaNodeFactory;

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
			ex.printStackTrace();
		}
	}

	@Before
	public void before() {
		this.db = UMLG.get();
    }

    @After
    public void after() {
        ((UmlgAdminGraph)this.db).drop();
    }

	protected long countVertices() {
//		return ((UmlgAdminGraph)this.db).countVertices() - UmlgMetaNodeFactory.getUmlgMetaNodeManager().count();
        return ((UmlgAdminGraph)this.db).countVertices();
	}

	protected long countEdges() {
//		return ((UmlgAdminGraph)this.db).countEdges() - UmlgMetaNodeFactory.getUmlgMetaNodeManager().count();
        return ((UmlgAdminGraph)this.db).countEdges();
	}

}
