package org.umlg.runtime.notification;

import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

/**
 * Date: 2014/10/14
 * Time: 8:28 AM
 */
public class ChangeHolder {

    public static enum ChangeType {
        ADD, REMOVE, DELETE
    }

    private UmlgNode umlgNode;
    private UmlgRuntimeProperty umlgRuntimeProperty;
    private Object value;
    private ChangeType changeType;

    private ChangeHolder(UmlgNode umlgNode, UmlgRuntimeProperty umlgRuntimeProperty, ChangeType changeType, Object value) {
        if (changeType == ChangeType.DELETE && !(value instanceof String)) {
            throw new IllegalStateException("ChangeType.DELETE must pass in a json representation of the deleted node!");
        }
        this.umlgNode = umlgNode;
        this.umlgRuntimeProperty = umlgRuntimeProperty;
        this.changeType = changeType;
        this.value = value;
    }

    public static ChangeHolder of(UmlgNode umlgNode, UmlgRuntimeProperty umlgRuntimeProperty, ChangeType changeType, Object value) {
        return new ChangeHolder(umlgNode, umlgRuntimeProperty, changeType, value);
    }

    public UmlgNode getUmlgNode() {
        return umlgNode;
    }

    public UmlgRuntimeProperty getUmlgRuntimeProperty() {
        return umlgRuntimeProperty;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public Object getValue() {
        return value;
    }
}
