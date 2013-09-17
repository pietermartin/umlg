package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.Vertex;

/**
 * Date: 2013/09/15
 * Time: 10:17 AM
 */
public class UmlgNeo4jLabelConverter implements UmlgLabelConverter {

    private static UmlgNeo4jLabelConverter INSTANCE = new UmlgNeo4jLabelConverter();

    private UmlgNeo4jLabelConverter() {
        super();
    }

    public static UmlgNeo4jLabelConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public String convert(String label) {
        //No need to convert the labels in Neo4j
        return label;
    }
}
