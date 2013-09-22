package org.umlg.runtime.adaptor;

import com.orientechnologies.orient.core.metadata.schema.OClass;

import java.util.List;

/**
 * Date: 2013/09/20
 * Time: 8:11 PM
 */
public class VertexSchemaCreatorImpl implements VertexSchemaCreator {
    @Override
    public void create(List<String> classHierarchy) {
        UmlgOrientDbGraph g = (UmlgOrientDbGraph) GraphDb.getDb();
        //This list contains the class hierarchy starting with the super class
        //The first class is the base class
        String baseClass = classHierarchy.get(0);
        String normalizedClass = UmlgLabelConverterFactory.getUmlgLabelConverter().convert(baseClass);
        OClass cls = g.getRawGraph().getMetadata().getSchema().getClass(normalizedClass);
        if (cls == null) {
            g.createVertexType(normalizedClass);
        }

        int count = 1;
        String previous = normalizedClass;
        for (String clazz : classHierarchy) {
            if (count == 1) {
                count++;
                continue;
            }
            normalizedClass = UmlgLabelConverterFactory.getUmlgLabelConverter().convert(clazz);
            cls = g.getRawGraph().getMetadata().getSchema().getClass(normalizedClass);
            if (cls == null) {
                g.createVertexType(normalizedClass, previous);
            }
            count++;
            previous = normalizedClass;
        }
    }

}
