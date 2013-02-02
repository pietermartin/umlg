package org.tuml.restlet.client.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2013/02/02
 * Time: 12:28 PM
 */
public class ObjectJson {

    private List<PropertyJson> properties = new ArrayList<PropertyJson>();

    public ObjectJson(List<PropertyJson> properties) {
        this.properties = properties;
    }

    public ObjectJson(Map<String, Object> properties) {
        for (String propertyName : properties.keySet()) {
            Object propertyValue = properties.get(propertyName);
            this.properties.add(new PropertyJson(propertyName, propertyValue));
        }
    }

    public List<PropertyJson> getProperties() {
        return properties;
    }

    public PropertyJson get(String propertyName) {
        for (PropertyJson propertyJson : properties) {
            if (propertyJson.getName().equals(propertyName)) {
                return propertyJson;
            }
        }
        return null;
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int count = 0;
        for (PropertyJson propertyJson : this.properties) {
            count++;
            if (count == this.properties.size()) {
                sb.append(propertyJson.toJson());
                sb.append(",");
            } else {
                sb.append(propertyJson.toJson());
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
