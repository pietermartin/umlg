package org.tuml.runtime.util;

/**
 * Date: 2013/01/03
 * Time: 10:58 AM
 */
public enum TinkerImplementation {
    NEO4J("org.tuml.runtime.adaptor.TumlNeo4jGraphFactory", "org.tuml.runtime.adaptor.TumlNeo4jIdUtilImpl", "org.tuml.runtime.adaptor.TumlNeo4jTestUtil"),
    ORIENTDB("org.tuml.runtime.adaptor.TumlOrientDbGraphFactory", "org.tuml.runtime.adaptor.TumlOrientDbIdUtilImpl", "org.tuml.runtime.adaptor.TumlOrientDbTestUtil");
    private String tumlGraphFactory;
    private String tumlIdUtil;
    private String tumlTestUtil;
    private TinkerImplementation(String tumlGraphFactory, String tumlIdUtil, String tumlTestUtil) {
        this.tumlGraphFactory = tumlGraphFactory;
        this.tumlIdUtil = tumlIdUtil;
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
