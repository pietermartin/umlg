package org.umlg.runtime.adaptor;

/**
 * Date: 2013/09/20
 * Time: 8:12 PM
 */
public class EdgeSchemaCreatorImpl implements EdgeSchemaCreator {
    @Override
    public void create(String label) {
        UmlgOrientDbGraph g = (UmlgOrientDbGraph) UMLG.get();
        g.createEdgeType(UmlgLabelConverterFactory.getUmlgLabelConverter().convert(label));
    }
}
