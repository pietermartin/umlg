package org.umlg.javageneration.visitor.property;

import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgGenerationUtil;

/**
 * Date: 2014/08/31
 * Time: 3:01 PM
 */
public class PropertyChangeNotificationBuilder {

    //add update remove is on property adders removers and setters.
    //delete is on class.delete
    public static enum CHANGE_TYPE {
        ADD, UPDATE, REMOVE, DELETE;
    }

    public static void buildChangeNotification(OJAnnotatedClass owner, OJAnnotatedOperation setter, PropertyWrapper propertyWrapper, CHANGE_TYPE changeType) {
        OJIfStatement ifChanged = new OJIfStatement();
        switch (changeType) {
            case ADD:
                ifChanged.setCondition("true");
                ifChanged.addToThenPart(
                        UmlgGenerationUtil.UmlgNotificationManager.getLast() + ".INSTANCE.notify(\n\t" +
                                propertyWrapper.getTumlRuntimePropertyEnum() +
                                ",\n\tChangeHolder.of(\n\t\tthis,\n\t\t" +
                                propertyWrapper.getTumlRuntimePropertyEnum() + ",\n\t\t" +
                                "null,\n\t\t" +
                                propertyWrapper.fieldname() + "\n\t)\n)"
                );
                setter.getBody().addToStatements(setter.getBody().getStatements().size(), ifChanged);
                break;
            case UPDATE:
                ifChanged.setCondition("!changed(" + propertyWrapper.getter() + "(), " + propertyWrapper.fieldname() + ")");
                ifChanged.addToThenPart(
                        UmlgGenerationUtil.UmlgNotificationManager.getLast() + ".INSTANCE.notify(\n\t" +
                                propertyWrapper.getTumlRuntimePropertyEnum() +
                                ",\n\tChangeHolder.of(\n\t\tthis,\n\t\t" +
                                propertyWrapper.getTumlRuntimePropertyEnum() + ",\n\t\t" +
                                propertyWrapper.getter() + "(),\n\t\t" +
                                propertyWrapper.fieldname() + "\n\t)\n)"
                );
                setter.getBody().addToStatements(0, ifChanged);
                break;
            case REMOVE:
                ifChanged.setCondition("true");
                ifChanged.addToThenPart(
                        UmlgGenerationUtil.UmlgNotificationManager.getLast() + ".INSTANCE.notify(\n\t" +
                                propertyWrapper.getTumlRuntimePropertyEnum() +
                                ",\n\tChangeHolder.of(\n\t\tthis,\n\t\t" +
                                propertyWrapper.getTumlRuntimePropertyEnum() + ",\n\t\t" +
                                propertyWrapper.fieldname() + ",\n\t\t" +
                                "null" + "\n\t)\n)"
                );
                setter.getBody().addToStatements(setter.getBody().getStatements().size(), ifChanged);
                break;
            case DELETE:
                ifChanged.setCondition("true");
                ifChanged.addToThenPart(
                        UmlgGenerationUtil.UmlgNotificationManager.getLast() + ".INSTANCE.notify(\n\t" +
                                propertyWrapper.getTumlRuntimePropertyEnum() +
                                ",\n\tChangeHolder.of(\n\t\tthis."+
                                PropertyWrapper.from(propertyWrapper.getOtherEnd()).getter() + "()" +
                                ",\n\t\t" +
                                propertyWrapper.getTumlRuntimePropertyEnum() + ",\n\t\t" +
                                "this,\n\t\t" +
                                "null" + "\n\t)\n)"
                );
                setter.getBody().addToStatements(setter.getBody().getStatements().size(), ifChanged);
                break;
        }

        owner.addToImports(UmlgClassOperations.getPathName(propertyWrapper.getOwningType()).append(UmlgClassOperations.propertyEnumName(propertyWrapper.getOwningType())));
        owner.addToImports(UmlgGenerationUtil.UmlgNotificationManager);
        owner.addToImports(UmlgGenerationUtil.ChangeHolder);
        owner.addToImports("java.util.Optional");
    }

}
