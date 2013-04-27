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

    private static ThreadLocal<Map<String, Long>> tmpIdVar = new ThreadLocal<Map<String, Long>>() {
        @Override
        protected Map<String, Long> initialValue() {
            return new HashMap<String, Long>();
        }
    };

    public long get(String fakeId) {
        return tmpIdVar.get().get(fakeId);
    }

    public void put(String qualifiedNameAndFakeId, long id) {
        tmpIdVar.get().put(qualifiedNameAndFakeId, id);
    }

    public static void remove() {
        tmpIdVar.remove();
    }
}
