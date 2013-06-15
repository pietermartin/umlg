package org.umlg.runtime.restlet;

import org.umlg.runtime.adaptor.TumlTmpIdManager;
import org.umlg.runtime.domain.PersistentObject;

import java.util.Collection;

/**
 * Date: 2013/04/28
 * Time: 6:57 PM
 */
public class TumlRestletToJsonUtil {

    public static String toJson(Collection<? extends PersistentObject> entities) {
        return toJson(entities, false);
    }

    public static String toJson(Collection<? extends PersistentObject> entities, boolean deep) {
        StringBuilder json = new StringBuilder();
        if (entities != null) {
            int count = 0;
            for (PersistentObject p : entities) {
                count++;
                String fakeId = TumlTmpIdManager.INSTANCE.get(p.getId());
                if (fakeId != null) {
                    json.append(p.toJsonWithoutCompositeParent(deep));
                    json.insert(json.length() - 1, ", \"tmpId\": \"" + fakeId + "\"");
                } else {
                    json.append(p.toJsonWithoutCompositeParent(deep));
                }
                if (count != entities.size()) {
                    json.append(",");
                }
            }
        }
        return json.toString();
    }

}
