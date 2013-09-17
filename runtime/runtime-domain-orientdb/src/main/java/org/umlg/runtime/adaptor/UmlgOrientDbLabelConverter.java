package org.umlg.runtime.adaptor;

/**
 * Date: 2013/09/15
 * Time: 10:18 AM
 */
public class UmlgOrientDbLabelConverter implements UmlgLabelConverter {

    private static UmlgOrientDbLabelConverter INSTANCE = new UmlgOrientDbLabelConverter();

    private UmlgOrientDbLabelConverter() {
        super();
    }

    public static UmlgOrientDbLabelConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public String convert(String label) {
        return label.replace("::", "__").replace("<", "__").replace(">", "__");
    }
}
