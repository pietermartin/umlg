package org.umlg.runtime.util;

/**
 * Date: 2013/01/03
 * Time: 10:58 AM
 */
public enum UmlgAdaptorImplementation {
    NEO4J(
            "org.umlg.runtime.adaptor.UmlgNeo4jGraphFactory",
            "org.umlg.runtime.adaptor.TumlNeo4jIdUtilImpl",
            "org.umlg.runtime.adaptor.UmlgNeo4jExceptionUtilIml",
            "org.umlg.runtime.adaptor.TumlNeo4jTestUtil",
            "org.umlg.runtime.adaptor.UmlgNeo4jLabelConverter",
            "org.umlg.runtime.adaptor.Neo4jAdminApp"),
    ORIENTDB(
            "org.umlg.runtime.adaptor.UmlgOrientDbGraphFactory",
            "org.umlg.runtime.adaptor.TumlOrientDbIdUtilImpl",
            "org.umlg.runtime.adaptor.UmlgOrientDbExceptionUtilIml",
            "org.umlg.runtime.adaptor.TumlOrientDbTestUtil",
            "org.umlg.runtime.adaptor.UmlgOrientDbLabelConverter",
            "//TODO");
    private String tumlGraphFactory;
    private String tumlIdUtil;
    private String umlgExceptionUtil;
    private String tumlTestUtil;
    private String umlgLabelConverter;
    private String umlgAdminApp;

    private UmlgAdaptorImplementation(String tumlGraphFactory, String tumlIdUtil, String umlgExceptionUtil, String tumlTestUtil, String umlgLabelConverter, String umlgAdminApp) {
        this.tumlGraphFactory = tumlGraphFactory;
        this.tumlIdUtil = tumlIdUtil;
        this.umlgExceptionUtil = umlgExceptionUtil;
        this.tumlTestUtil = tumlTestUtil;
        this.umlgLabelConverter = umlgLabelConverter;
        this.umlgAdminApp = umlgAdminApp;
    }

    public String getTumlGraphFactory() {
        return tumlGraphFactory;
    }

    public String getTumlIdUtil() {
        return tumlIdUtil;
    }

    public String getTumlTestUtil() {
        return tumlTestUtil;
    }

    public String getUmlgExceptionUtil() {
        return umlgExceptionUtil;
    }

    public String getUmlgLabelConverter() {
        return this.umlgLabelConverter;
    }

    public String getUmlgAdminApp() {
        return umlgAdminApp;
    }

    public static UmlgAdaptorImplementation fromName(String name) {
        if (name.equalsIgnoreCase(NEO4J.name())) {
            return NEO4J;
        } else if (name.equalsIgnoreCase(ORIENTDB.name())) {
            return ORIENTDB;
        } else {
            throw new RuntimeException("Unknown tinker implementation " + name);
        }
    }

}
