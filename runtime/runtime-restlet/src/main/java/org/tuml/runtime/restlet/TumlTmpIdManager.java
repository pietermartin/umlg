package org.tuml.runtime.restlet;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2013/04/27
 * Time: 10:29 AM
 *
 */
public class TumlTmpIdManager {

    public static TumlTmpIdManager INSTANCE = new TumlTmpIdManager();

    private TumlTmpIdManager() {
    }

    private static ThreadLocal<Map<String, Long>> fakeIdToIdMap = new ThreadLocal<Map<String, Long>>() {
        @Override
        protected Map<String, Long> initialValue() {
            return new HashMap<String, Long>();
        }
    };

    private static ThreadLocal<Map<Long, String>> idToFakeIdMap = new ThreadLocal<Map<Long, String>>() {
        @Override
        protected Map<Long, String> initialValue() {
            return new HashMap<Long, String>();
        }
    };

    public String get(Long id) {
        return idToFakeIdMap.get().get(id);
    }

    public Long get(String fakeId) {
        return fakeIdToIdMap.get().get(fakeId);
    }

    public void put(String fakeId, long id) {
        fakeIdToIdMap.get().put(fakeId, id);
        idToFakeIdMap.get().put(id, fakeId);
    }

    public static void remove() {
        fakeIdToIdMap.remove();
        idToFakeIdMap.remove();
    }
}
