package org.tuml.restlet.client.json;

import java.util.List;
import java.util.Map;

/**
 * Date: 2013/02/02
 * Time: 12:06 PM
 */
public class DataMetaJsonPair {

    private MetaJson metaJson;
    private DataJson dataJson;

    public DataMetaJsonPair(MetaJson metaJson, DataJson dataJson) {
        this.metaJson = metaJson;
        this.dataJson = dataJson;
    }

    public DataMetaJsonPair(Map<String, Object> dataMeta) {
        List<Map<String, Object>> data = (List<Map<String, Object>>) dataMeta.get("data");
        this.dataJson = new DataJson(data);
        Map<String, Object> meta = (Map<String, Object>) dataMeta.get("meta");
        this.metaJson = new MetaJson(meta);
    }

    public DataJson getDataJson() {
        return dataJson;
    }

    public MetaJson getMetaJson() {
        return metaJson;
    }


}
