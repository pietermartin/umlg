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
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TumlGraph;
import org.tuml.runtime.adaptor.TumlGraphFactory;
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
		db = createNakedGraph();
		GraphDb.setDb(db);
	}

    @After
    public void after() {
        db.shutdown();
        GraphDb.remove();
    }

    protected TumlGraph createNakedGraph() {
		String dbUrl = TumlProperties.INSTANCE.getTumlDbLocation();
		String parsedUrl = dbUrl;
		if (dbUrl.startsWith("local:")) {
			parsedUrl = dbUrl.replace("local:", "");
		}
		File dir = new File(parsedUrl);
		if (dir.exists()) {
			try {
				FileUtils.deleteDirectory(dir);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		try {
            TinkerImplementation tinkerImplementation = TinkerImplementation.fromName(TumlProperties.INSTANCE.getTinkerImplementation());
            @SuppressWarnings("unchecked")
			Class<TumlGraphFactory> factory = (Class<TumlGraphFactory>) Class.forName(tinkerImplementation.getTumlGraphFactory());
			Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
			TumlGraphFactory nakedGraphFactory = (TumlGraphFactory) m.invoke(null);
			return nakedGraphFactory.getTumlGraph(dbUrl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected long countVertices() {
		return db.countVertices();
	}

	protected long countEdges() {
		return db.countEdges();
	}

}
