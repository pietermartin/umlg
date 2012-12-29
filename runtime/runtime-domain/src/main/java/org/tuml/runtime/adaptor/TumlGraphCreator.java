package org.tuml.runtime.adaptor;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Date: 2012/12/29
 * Time: 9:23 PM
 */
public class TumlGraphCreator {

    public static TumlGraphCreator INSTANCE = new TumlGraphCreator();

    private TumlGraphCreator() {

    }

    public TumlGraph startupGraph() {
        if (GraphDb.getDb() == null) {
            Properties properties = new Properties();
            try {
                properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("tuml.env.properties"));
                String dbUrl = properties.getProperty("tinkerdb");
                @SuppressWarnings("unchecked")
                Class<TumlGraphFactory> factory = (Class<TumlGraphFactory>) Class.forName(properties.getProperty("tumlgraph.factory"));
                Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                TumlGraphFactory nakedGraphFactory = (TumlGraphFactory) m.invoke(null);
                TumlGraph tumlGraph = nakedGraphFactory.getTumlGraph(dbUrl);
                GraphDb.setDb(tumlGraph);
                return tumlGraph;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalStateException("TumlGraphCreator.INSTANCE.startupGraph() may only be called once at application startup.");
        }
    }

}
