package org.umlg.restlet.client.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2013/02/02
 * Time: 11:46 AM
 */
public class DataJson {

    private List<ObjectJson> objects = new ArrayList<ObjectJson>();
    private List<ObjectJson> insert = new ArrayList<ObjectJson>();
    private List<ObjectJson> update = new ArrayList<ObjectJson>();
    private List<ObjectJson> delete = new ArrayList<ObjectJson>();

    public static DataJson createDataJsonFromObjectJsons(List<ObjectJson> objects) {
        DataJson result = new DataJson();
        result.objects = new ArrayList<ObjectJson>(objects);
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

    public void setObjects(List<ObjectJson> objects) {
        this.objects = new ArrayList<ObjectJson>(objects);
    }

    public int count() {
        return this.objects.size();
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        if (!this.insert.isEmpty() && !this.update.isEmpty() && !this.delete.isEmpty()) {
            sb.append("[");
            int count = 0;
            boolean first = false;
            for (ObjectJson objectJson : this.objects) {
                count++;
                if (!first && count < this.objects.size()) {
                    sb.append(",");
                }
                first = true;
                sb.append(objectJson.toJson());
            }
            sb.append("]");
        } else {
            int count = 0;
            boolean first = false;
            if (!this.insert.isEmpty()) {
                sb.append("{\"insert\": ");
            }
            for (ObjectJson objectJson : this.insert) {
                count++;
                if (!first && count < this.objects.size()) {
                    sb.append(",");
                }
                first = true;
                sb.append(objectJson.toJson());
            }
            if (!this.update.isEmpty()) {
                sb.append(", \"update\": ");
            }
            for (ObjectJson objectJson : this.update) {
                count++;
                if (!first && count < this.objects.size()) {
                    sb.append(",");
                }
                first = true;
                sb.append(objectJson.toJson());
            }
            if (!this.delete.isEmpty()) {
                sb.append(", \"delete\": ");
            }
            for (ObjectJson objectJson : this.delete) {
                count++;
                if (!first && count < this.objects.size()) {
                    sb.append(",");
                }
                first = true;
                sb.append(objectJson.toJson());
            }
            sb.append("}");
        }
        return sb.toString();
    }

    public void moveToInsert() {
        this.insert = new ArrayList<ObjectJson>(this.objects);
        this.objects.clear();
    }

    public void moveToDelete() {
        this.delete = new ArrayList<ObjectJson>(this.objects);
        this.objects.clear();
    }

    public void moveToUpdate() {
        this.update = new ArrayList<ObjectJson>(this.objects);
        this.objects.clear();
    }
}
