package org.umlg.runtime.util;

/**
 * Date: 2013/01/03
 * Time: 10:58 AM
 */
public enum TinkerImplementation {
    NEO4J("org.umlg.runtime.adaptor.TumlNeo4jGraphFactory", "org.umlg.runtime.adaptor.TumlNeo4jIdUtilImpl", "org.umlg.runtime.adaptor.TumlNeo4jExceptionUtilIml", "org.umlg.runtime.adaptor.TumlNeo4jTestUtil"),
    ORIENTDB("org.umlg.runtime.adaptor.TumlOrientDbGraphFactory", "org.umlg.runtime.adaptor.TumlOrientDbIdUtilImpl", "TODO", "org.umlg.runtime.adaptor.TumlOrientDbTestUtil");
    private String tumlGraphFactory;
    private String tumlIdUtil;
    private String tumlExceptionUtil;
    private String tumlTestUtil;

    private TinkerImplementation(String tumlGraphFactory, String tumlIdUtil, String tumlExceptionUtil, String tumlTestUtil) {
        this.tumlGraphFactory = tumlGraphFactory;
        this.tumlIdUtil = tumlIdUtil;
        this.tumlExceptionUtil = tumlExceptionUtil;
        this.tumlTestUtil = tumlTestUtil;
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

    public String getTumlExceptionUtil() {
        return tumlExceptionUtil;
    }

    public static TinkerImplementation fromName(String name) {
        if (name.equalsIgnoreCase(NEO4J.name())) {
            return NEO4J;
        } else if (name.equalsIgnoreCase(ORIENTDB.name())) {
            return ORIENTDB;
        } else {
            throw new RuntimeException("Unknown tinker implementation " + name);
        }
    }

}
