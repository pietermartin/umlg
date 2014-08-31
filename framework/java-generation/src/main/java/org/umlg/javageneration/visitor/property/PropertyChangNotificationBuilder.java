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
public class PropertyChangNotificationBuilder {

    public static void buildChangeNotification(OJAnnotatedClass owner, OJAnnotatedOperation setter, PropertyWrapper propertyWrapper) {
        OJIfStatement ifChanged = new OJIfStatement();
        ifChanged.setCondition("!changed(" + propertyWrapper.getter() + "(), " + propertyWrapper.fieldname() + ")");
        ifChanged.addToThenPart(
                "Optional.ofNullable(" +
                UmlgGenerationUtil.UmlgNotificationManager.getLast() + ".INSTANCE.get(" +
                propertyWrapper.getTumlRuntimePropertyEnum() +
                "))\n    ." +
                "ifPresent(\n        " +
                "n -> n.notifyChanged(\n            " +
                "this,\n            " +
                propertyWrapper.getTumlRuntimePropertyEnum() + ",\n            " +
                propertyWrapper.getter() + "(),\n            " +
                propertyWrapper.fieldname() + ")\n)"
        );
        owner.addToImports(UmlgGenerationUtil.UmlgNotificationManager);
        owner.addToImports("java.util.Optional");
        setter.getBody().addToStatements(0, ifChanged);
    }

}
