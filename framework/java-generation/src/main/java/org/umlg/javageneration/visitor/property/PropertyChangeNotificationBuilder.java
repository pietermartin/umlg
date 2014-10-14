package org.umlg.javageneration.visitor.property;

import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;

/**
 * Date: 2014/08/31
 * Time: 3:01 PM
 */
public class PropertyChangeNotificationBuilder {

    public static void buildChangeNotification(OJAnnotatedClass owner, OJAnnotatedOperation setter, PropertyWrapper propertyWrapper, boolean adder) {
        OJIfStatement ifChanged = new OJIfStatement();
        if (adder) {
            ifChanged.setCondition("true");
        } else {
            ifChanged.setCondition("!changed(" + propertyWrapper.getter() + "(), " + propertyWrapper.fieldname() + ")");
        }
        ifChanged.addToThenPart(
                UmlgGenerationUtil.UmlgNotificationManager.getLast() + ".INSTANCE.notify(\n\t" +
                        propertyWrapper.getTumlRuntimePropertyEnum() +
                        ",\n\tChangeHolder.of(\n\t\tthis,\n\t\t" +
                        propertyWrapper.getTumlRuntimePropertyEnum() + ",\n\t\t" +
                        (adder ? ("null,\n\t\t") : (propertyWrapper.getter() + "(),\n\t\t")) +
                        propertyWrapper.fieldname() + "\n\t)\n)"
        );
        owner.addToImports(UmlgGenerationUtil.UmlgNotificationManager);
        owner.addToImports(UmlgGenerationUtil.ChangeHolder);
        owner.addToImports("java.util.Optional");
        setter.getBody().addToStatements(0, ifChanged);
    }

}
