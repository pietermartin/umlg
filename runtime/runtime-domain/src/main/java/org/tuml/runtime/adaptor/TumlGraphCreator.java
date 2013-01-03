package org.tuml.runtime.adaptor;

import org.apache.commons.io.FileUtils;
import org.tuml.runtime.util.TumlProperties;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            try {
                String dbUrl = TumlProperties.INSTANCE.getTumlDb();

                if (TumlProperties.INSTANCE.isClearDbOnStartUp()) {
                    String parsedUrl = dbUrl;
                    if (dbUrl.startsWith("local:")) {
                        parsedUrl = dbUrl.replace("local:", "");
                    }
                    File dir = new File(parsedUrl);
                    if (dir.exists()) {
                        try {
                            FileUtils.moveDirectory(dir, new File(dir, "bak-" + new SimpleDateFormat("ddmmyyyy").format(new Date())));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                @SuppressWarnings("unchecked")
                Class<TumlGraphFactory> factory = (Class<TumlGraphFactory>) Class.forName(TumlProperties.INSTANCE.getTumlGraphFactory());
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
