package org.umlg.restlet.client.json;

import java.util.Map;

/**
 * Date: 2013/02/02
 * Time: 11:47 AM
 */
public class MetaJson {

    private String qualifiedName;
    private MetaToFrom to;
    private MetaToFrom from;

    public MetaJson(Map<String, Object> meta) {
        this.qualifiedName = (String) meta.get("qualifiedName");
        Map<String, Object> to = (Map<String, Object>) meta.get("to");
        Map<String, Object> from = (Map<String, Object>) meta.get("from");
        if (to != null) {
            this.to = new MetaToFrom(to);
        }
        if (from != null) {
            this.from = new MetaToFrom(from);
        }
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public MetaToFrom getTo() {
        return to;
    }

    public MetaToFrom getFrom() {
        return from;
    }
}
