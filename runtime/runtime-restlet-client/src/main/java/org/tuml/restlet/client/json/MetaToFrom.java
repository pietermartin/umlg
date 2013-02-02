package org.tuml.restlet.client.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2013/02/02
 * Time: 2:32 PM
 */
public class MetaToFrom {

    private String name;
    private String qualifiedName;
    private String uri;
    private List<MetaPropertyJson> properties = new ArrayList<MetaPropertyJson>();

    public MetaToFrom(Map<String, Object> toFrom) {
        this.name = (String) toFrom.get("name");
        this.qualifiedName = (String) toFrom.get("qualifiedName");
        this.uri = (String) toFrom.get("uri");
        List<Map<String, Object>> properties = (List<Map<String, Object>>) toFrom.get("properties");
        for (Map<String, Object> property : properties) {
            this.properties.add(new MetaPropertyJson(property));
        }
    }

    public String getName() {
        return name;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getUri() {
        return uri;
    }

    public List<MetaPropertyJson> getProperties() {
        return properties;
    }
}
