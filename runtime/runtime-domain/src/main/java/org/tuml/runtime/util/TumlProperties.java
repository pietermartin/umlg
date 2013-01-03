package org.tuml.runtime.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Date: 2013/01/02
 * Time: 3:08 PM
 */
public class TumlProperties {

    public static TumlProperties INSTANCE = new TumlProperties();
    private Properties properties;

    private TumlProperties() {
        this.properties = new Properties();
        try {
            this.properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("tuml.env.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTumlDb() {
        return this.properties.getProperty("tumldb");
    }

    public String getTumlGraphFactory() {
        return this.properties.getProperty("tumlgraph.factory");
    }

    public String getTumlIdUtil() {
        return this.properties.getProperty("tuml.tinkeridutil");
    }

    public boolean isStartAdminApplication() {
        return Boolean.parseBoolean(this.properties.getProperty("start.admin.application"));
    }

    public boolean isClearDbOnStartUp() {
        return Boolean.parseBoolean(this.properties.getProperty("start.clear.db"));
    }

    public boolean isCreateDefaultData() {
        return Boolean.parseBoolean(this.properties.getProperty("start.default.data"));
    }

    public String getDefaultDataLoaderClass() {
        return this.properties.getProperty("default.data.class");
    }
}
