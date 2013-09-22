package org.umlg.runtime.adaptor;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2013/04/27
 * Time: 10:29 AM
 *
 */
public class UmlgTmpIdManager {

    public static UmlgTmpIdManager INSTANCE = new UmlgTmpIdManager();

    private UmlgTmpIdManager() {
    }

    private static ThreadLocal<Map<String, Object>> fakeIdToIdMap = new ThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };

    private static ThreadLocal<Map<Object, String>> idToFakeIdMap = new ThreadLocal<Map<Object, String>>() {
        @Override
        protected Map<Object, String> initialValue() {
            return new HashMap<Object, String>();
        }
    };

    public String get(Object id) {
        return idToFakeIdMap.get().get(id);
    }

    public Object get(String fakeId) {
        return fakeIdToIdMap.get().get(fakeId);
    }

    public void put(String fakeId, Object id) {
        fakeIdToIdMap.get().put(fakeId, id);
        idToFakeIdMap.get().put(id, fakeId);
    }

    public static void remove() {
        fakeIdToIdMap.remove();
        idToFakeIdMap.remove();
    }
}
