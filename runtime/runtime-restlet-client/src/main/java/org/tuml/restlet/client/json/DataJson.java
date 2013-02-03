package org.tuml.restlet.client.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2013/02/02
 * Time: 11:46 AM
 */
public class DataJson {

    private List<ObjectJson> objects = new ArrayList<ObjectJson>();

    public static DataJson createDataJsonFromObjectJsons(List<ObjectJson> objects) {
        DataJson result = new DataJson();
        result.objects = objects;
        return result;
    }

    public DataJson() {
    }

    public DataJson(List<Map<String, Object>> data) {
        for (Map<String, Object> object : data) {
             this.objects.add(new ObjectJson(object));
        }
    }

    public List<ObjectJson> getObjects() {
        return objects;
    }

    public int count() {
        return this.objects.size();
    }

}
