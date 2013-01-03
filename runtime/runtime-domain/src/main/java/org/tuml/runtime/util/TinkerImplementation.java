package org.tuml.runtime.util;

/**
 * Date: 2013/01/03
 * Time: 10:58 AM
 */
public enum TinkerImplementation {
    NEO4J("org.tuml.runtime.domain.neo4j.TumlNeo4jGraphFactory", "org.tuml.runtime.domain.neo4j.TumlNeo4jIdUtilImpl"), ORIENTDB("", "");
    private String tumlGraphFactory;
    private String tumlIdUtil;
    private TinkerImplementation(String tumlGraphFactory, String tumlIdUtil) {
        this.tumlGraphFactory = tumlGraphFactory;
        this.tumlIdUtil = tumlIdUtil;
    }

    public String getTumlGraphFactory() {
        return tumlGraphFactory;
    }

    public String getTumlIdUtil() {
        return tumlIdUtil;
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
