package org.umlg.runtime.adaptor;

/**
 * Date: 2013/01/03
 * Time: 10:58 AM
 */
public enum UmlgAdaptorImplementation {
    NEO4J(
            "org.umlg.runtime.adaptor.UmlgNeo4jGraphFactory",
            "org.umlg.runtime.adaptor.UmlgNeo4jExceptionUtilIml",
            "org.umlg.runtime.adaptor.TumlNeo4jTestUtil",
            "org.umlg.runtime.adaptor.UmlgDefaultLabelConverter",
            "org.umlg.runtime.adaptor.UmlgDefaultQualifierId",
            "org.umlg.runtime.adaptor.Neo4jAdminApp"),
    ORIENTDB(
            "org.umlg.runtime.adaptor.UmlgOrientDbGraphFactory",
            "org.umlg.runtime.adaptor.UmlgOrientDbExceptionUtilIml",
            "org.umlg.runtime.adaptor.UmlgOrientDbTestUtil",
            "org.umlg.runtime.adaptor.UmlgOrientDbLabelConverter",
            "org.umlg.runtime.adaptor.UmlgOrientDbQualifierId",
            "//TODO"),
    TITAN(
            "org.umlg.runtime.adaptor.UmlgTitanGraphFactory",
            "org.umlg.runtime.adaptor.UmlgTitanExceptionUtilIml",
            "org.umlg.runtime.adaptor.UmlgTitanTestUtil",
            "org.umlg.runtime.adaptor.UmlgDefaultLabelConverter",
            "org.umlg.runtime.adaptor.UmlgDefaultQualifierId",
            "//TODO"),
    BITSY(
            "org.umlg.runtime.adaptor.UmlgBitsyGraphFactory",
            "org.umlg.runtime.adaptor.UmlgBitsyExceptionUtilIml",
            "org.umlg.runtime.adaptor.UmlgBitsyTestUtil",
            "org.umlg.runtime.adaptor.UmlgDefaultLabelConverter",
            "org.umlg.runtime.adaptor.UmlgDefaultQualifierId",
            "//TODO");

    private String tumlGraphFactory;
    private String umlgExceptionUtil;
    private String tumlTestUtil;
    private String umlgLabelConverter;
    private String umlgQualifierId;
    private String umlgAdminApp;

    private UmlgAdaptorImplementation(String tumlGraphFactory, String umlgExceptionUtil, String tumlTestUtil, String umlgLabelConverter, String umlgQualifierId, String umlgAdminApp) {
        this.tumlGraphFactory = tumlGraphFactory;
        this.umlgExceptionUtil = umlgExceptionUtil;
        this.tumlTestUtil = tumlTestUtil;
        this.umlgLabelConverter = umlgLabelConverter;
        this.umlgQualifierId = umlgQualifierId;
        this.umlgAdminApp = umlgAdminApp;
    }

    public String getTumlGraphFactory() {
        return tumlGraphFactory;
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

    public String getUmlgQualifierId() {
        return this.umlgQualifierId;
    }

    public String getUmlgAdminApp() {
        return umlgAdminApp;
    }

    public static UmlgAdaptorImplementation fromName(String name) {
        if (name.equalsIgnoreCase(NEO4J.name())) {
            return NEO4J;
        } else if (name.equalsIgnoreCase(ORIENTDB.name())) {
            return ORIENTDB;
        } else if (name.equalsIgnoreCase(TITAN.name())) {
            return TITAN;
        } else if (name.equalsIgnoreCase(BITSY.name())) {
            return BITSY;
        } else {
            throw new RuntimeException("Unknown tinker implementation " + name);
        }
    }

}
