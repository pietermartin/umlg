package org.umlg.runtime.notification;

import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

/**
 * Date: 2014/10/14
 * Time: 8:28 AM
 */
public class ChangeHolder {

    private UmlgNode umlgNode;
    private UmlgRuntimeProperty umlgRuntimeProperty;
    private Object oldValue;
    private Object newValue;

    private ChangeHolder(UmlgNode umlgNode, UmlgRuntimeProperty umlgRuntimeProperty, Object oldValue, Object newValue) {
        this.umlgNode = umlgNode;
        this.umlgRuntimeProperty = umlgRuntimeProperty;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public static ChangeHolder of(UmlgNode umlgNode, UmlgRuntimeProperty umlgRuntimeProperty, Object oldValue, Object newValue) {
        return new ChangeHolder(umlgNode, umlgRuntimeProperty, oldValue, newValue);
    }

    public UmlgNode getUmlgNode() {
        return umlgNode;
    }

    public UmlgRuntimeProperty getUmlgRuntimeProperty() {
        return umlgRuntimeProperty;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}
