package org.tuml.runtime.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Properties;
import java.util.logging.LogManager;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.tuml.runtime.adaptor.*;
import org.tuml.runtime.util.TinkerImplementation;
import org.tuml.runtime.util.TumlProperties;

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
        this.db = TumlGraphCreator.INSTANCE.startupGraph();
		GraphDb.setDb(this.db);
	}

    @After
    public void after() {
        this.db.shutdown();
        GraphDb.remove();
    }

	protected long countVertices() {
		return db.countVertices();
	}

	protected long countEdges() {
		return db.countEdges();
	}

}
