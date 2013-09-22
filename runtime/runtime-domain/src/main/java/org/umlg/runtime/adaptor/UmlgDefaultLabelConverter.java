package org.umlg.runtime.adaptor;

/**
 * Date: 2013/09/15
 * Time: 10:17 AM
 */
public class UmlgDefaultLabelConverter implements UmlgLabelConverter {

    private static UmlgDefaultLabelConverter INSTANCE = new UmlgDefaultLabelConverter();

    private UmlgDefaultLabelConverter() {
        super();
    }

    public static UmlgDefaultLabelConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public String convert(String label) {
        //No need to convert the labels in Neo4j
        return label;
    }
}
