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

    private static ThreadLocal<Map<String, Set<Object>>> associationClassMap = new ThreadLocal<Map<String, Set<Object>>>() {
        @Override
        protected Map<String, Set<Object>> initialValue() {
            return new HashMap<String, Set<Object>>();
        }
    };

    public synchronized void put(String associationClassName, Object associationToObjectId) {
        if (UmlgAssociationClassManager.associationClassMap.get().get(associationClassName) == null) {
            UmlgAssociationClassManager.associationClassMap.get().put(associationClassName, new HashSet<Object>());
        }
        UmlgAssociationClassManager.associationClassMap.get().get(associationClassName).add(associationToObjectId);
    }

    public boolean has(String associationClassName, Object associationToObjectId) {
        return UmlgAssociationClassManager.associationClassMap.get().get(associationClassName) != null &&
                UmlgAssociationClassManager.associationClassMap.get().get(associationClassName).contains(associationToObjectId);
    }

    public static void remove() {
        UmlgAssociationClassManager.associationClassMap.remove();
    }

}
