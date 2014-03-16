package org.umlg.runtime.util;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Date: 2013/01/02
 * Time: 3:08 PM
 */
public class UmlgProperties {

    public static UmlgProperties INSTANCE = new UmlgProperties();
    private CompositeConfiguration properties;

    private UmlgProperties() {
        try {
            this.properties = new CompositeConfiguration();
            PropertiesConfiguration pc = new PropertiesConfiguration("umlg.env.properties");
            this.properties.addConfiguration(pc);
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
        return this.properties.getString("umlg.db.location");
    }

    public boolean isStartAdminApplication() {
        return this.properties.getBoolean("start.admin.application", false);
    }

    public boolean isClearDbOnStartUp() {
        return this.properties.getBoolean("start.clear.db", false);
    }

    public boolean isCreateDefaultData() {
        return this.properties.getBoolean("start.default.data", false);
    }

    public String getDefaultDataLoaderClass() {
        return this.properties.getString("default.data.class");
    }

    public boolean isTransactionsMutliThreaded() {
        return this.properties.getBoolean("transaction.multithreaded", false);
    }

    public boolean isLoadUiResourcesFromFile() {
        return this.properties.getBoolean("umlg.ui.from.file", false);
    }

    public String getWebserverIp() {
        return this.properties.getString("webserver.ip", "127.0.0.1");
    }

}
