package org.tuml.runtime.test;

import java.net.URL;
import java.util.logging.LogManager;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.tuml.runtime.adaptor.*;

public class BaseLocalDbTest {

	protected TumlGraph db;

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
        TumlGraphManager.INSTANCE.deleteGraph();
		this.db = GraphDb.getDb();
	}

    @After
    public void after() {
        TumlGraphManager.INSTANCE.shutdown();
        TumlGraphManager.INSTANCE.deleteGraph();
        GraphDb.remove();
        TransactionThreadVar.remove();
        TransactionThreadEntityVar.remove();
        TransactionThreadMetaNodeVar.remove();
    }

	protected long countVertices() {
		return this.db.countVertices() - TumlMetaNodeFactory.getTumlMetaNodeManager().count();
	}

	protected long countEdges() {
		return this.db.countEdges() - TumlMetaNodeFactory.getTumlMetaNodeManager().count();
	}

    protected boolean isTransactionFailedException(Exception e) {
        return TumlTestUtilFactory.getTestUtil().isTransactionFailedException(e);
    }

}
