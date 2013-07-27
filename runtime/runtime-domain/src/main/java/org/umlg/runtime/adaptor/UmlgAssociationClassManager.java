package org.umlg.runtime.adaptor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Date: 2013/07/26
 * Time: 8:07 AM
 */
public class UmlgAssociationClassManager {

    public static UmlgAssociationClassManager INSTANCE = new UmlgAssociationClassManager();

    private UmlgAssociationClassManager() {
    }

    private static ThreadLocal<Map<String, Set<Long>>> associationClassMap = new ThreadLocal<Map<String, Set<Long>>>() {
        @Override
        protected Map<String, Set<Long>> initialValue() {
            return new HashMap<String, Set<Long>>();
        }
    };

    public synchronized void put(String associationClassName, Long associationToObjectId) {
        if (UmlgAssociationClassManager.associationClassMap.get().get(associationClassName) == null) {
            UmlgAssociationClassManager.associationClassMap.get().put(associationClassName, new HashSet<Long>());
        }
        UmlgAssociationClassManager.associationClassMap.get().get(associationClassName).add(associationToObjectId);
    }

    public boolean has(String associationClassName, Long associationToObjectId) {
        return UmlgAssociationClassManager.associationClassMap.get().get(associationClassName) != null &&
                UmlgAssociationClassManager.associationClassMap.get().get(associationClassName).contains(associationToObjectId);
    }

    public static void remove() {
        UmlgAssociationClassManager.associationClassMap.remove();
    }

}
