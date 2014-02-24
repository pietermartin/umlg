package org.umlg.runtime.util;

import java.util.Properties;

/**
 * Date: 2013/01/02
 * Time: 3:08 PM
 */
public class UmlgProperties {

    public static UmlgProperties INSTANCE = new UmlgProperties();
    private Properties properties;
    private String webserverIp;

    private UmlgProperties() {
        this.properties = new Properties();
        try {
            this.properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("umlg.env.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Expecting \"umlg.env.properties\" file on the classpath with ");
        }
        validateRequiredProperties();
    }

    private void validateRequiredProperties() {
        if (!this.properties.containsKey("umlg.db.location")) {
            throw new IllegalStateException("umlg.env.properties must have a property \"umlg.db.location\"");
        }
    }

    public String getTumlDbLocation() {
        return this.properties.getProperty("umlg.db.location");
    }

//    public String getTinkerImplementation() {
//        return this.properties.getProperty("tests.implementation");
//    }

    public boolean isStartAdminApplication() {
        return Boolean.parseBoolean(this.properties.getProperty("start.admin.application", "false"));
    }

    public boolean isClearDbOnStartUp() {
        return Boolean.parseBoolean(this.properties.getProperty("start.clear.db", "false"));
    }

    public boolean isCreateDefaultData() {
        return Boolean.parseBoolean(this.properties.getProperty("start.default.data", "false"));
    }

    public String getDefaultDataLoaderClass() {
        return this.properties.getProperty("default.data.class");
    }

    public boolean isTransactionsMutliThreaded() {
        return Boolean.parseBoolean(this.properties.getProperty("transaction.multithreaded", "false"));
    }

    public boolean isLoadUiResourcesFromFile() {
        return Boolean.parseBoolean(this.properties.getProperty("umlg.ui.from.file", "false"));
    }

    public String getWebserverIp() {
        return this.properties.getProperty("webserver.ip", "127.0.0.1");
    }
}
