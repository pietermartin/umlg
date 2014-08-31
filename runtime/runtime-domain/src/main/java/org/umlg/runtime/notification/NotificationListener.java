package org.umlg.runtime.notification;

import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

/**
 * Date: 2014/08/31
 * Time: 12:54 PM
 */
@FunctionalInterface
public interface NotificationListener {
    void notifyChanged(UmlgNode umlgNode, UmlgRuntimeProperty umlgRuntimeProperty, Object oldValue, Object newValue);
}
