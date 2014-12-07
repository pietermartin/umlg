package org.umlg.runtime.notification;

import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

/**
 * Date: 2014/08/31
 * Time: 12:54 PM
 */
@FunctionalInterface
public interface NotificationListener {

    //add update remove is on property adders removers and setters.
    //delete is on class.delete
    public static enum COMMIT_TYPE {
        BEFORE_COMMIT, AFTER_COMMIT;
    }

    void notifyChanged(COMMIT_TYPE commit_type, UmlgNode umlgNode, UmlgRuntimeProperty umlgRuntimeProperty, ChangeHolder.ChangeType changeType, Object value);
}
