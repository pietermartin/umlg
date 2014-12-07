package org.umlg.javageneration.visitor.property;

import org.umlg.java.metamodel.OJForStatement;
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
public class UmlgNodeDeleteNotificationBuilder {


    public static void buildDeleteNotification(OJAnnotatedClass owner, OJAnnotatedOperation delete, PropertyWrapper propertyWrapper) {
        OJIfStatement ifChanged = new OJIfStatement();
        ifChanged.setCondition("true");
        PropertyWrapper otherEnd = PropertyWrapper.from(propertyWrapper.getOtherEnd());
        if (otherEnd.isMany()) {
            OJForStatement forStatement = new OJForStatement(otherEnd.getName(), otherEnd.javaBaseTypePath(), "this." + otherEnd.getter() + "()");
            forStatement.getBody().addToStatements(constructNotifierForOtherEndMany(propertyWrapper, otherEnd));
            delete.getBody().addToStatements(0, forStatement);
        } else {
            ifChanged.addToThenPart(
                    constructNotifier(propertyWrapper)
            );
            delete.getBody().addToStatements(0, ifChanged);
        }
        owner.addToImports(UmlgClassOperations.getPathName(propertyWrapper.getOwningType()).append(UmlgClassOperations.propertyEnumName(propertyWrapper.getOwningType())));
        owner.addToImports(UmlgGenerationUtil.UmlgNotificationManager);
        owner.addToImports(UmlgGenerationUtil.ChangeHolder);
        owner.addToImports(UmlgGenerationUtil.ChangeHolder);
        owner.addToImports("java.util.Optional");
    }

    private static String constructNotifier(PropertyWrapper propertyWrapper) {
        return UmlgGenerationUtil.UmlgNotificationManager.getLast() + ".INSTANCE.notify(\n\t" +
                propertyWrapper.getTumlRuntimePropertyEnum() +
                ",\n\tChangeHolder.of(\n\t\tthis." +
                PropertyWrapper.from(propertyWrapper.getOtherEnd()).getter() + "()" +
                ",\n\t\t" +
                propertyWrapper.getTumlRuntimePropertyEnum() + ",\n\t\t" +
                "ChangeHolder.ChangeType.DELETE,\n\t\t" +
                "this.toJson()" + "\n\t)\n)";
    }

    private static String constructNotifierForOtherEndMany(PropertyWrapper propertyWrapper, PropertyWrapper otherEnd) {
        return UmlgGenerationUtil.UmlgNotificationManager.getLast() + ".INSTANCE.notify(\n\t" +
                propertyWrapper.getTumlRuntimePropertyEnum() +
                ",\n\tChangeHolder.of(\n\t\t" +
                otherEnd.fieldname() +
                ",\n\t\t" +
                propertyWrapper.getTumlRuntimePropertyEnum() + ",\n\t\t" +
                "ChangeHolder.ChangeType.DELETE,\n\t\t" +
                "this.toJson()" + "\n\t)\n)";
    }

}
