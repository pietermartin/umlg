package org.umlg.restlet.client.json;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2013/02/02
 * Time: 11:31 AM
 */
public class RootJson {

    private List<DataMetaJsonPair> dataAndMetas = new ArrayList<DataMetaJsonPair>();

    public RootJson(List<DataMetaJsonPair> dataAndMetas) {
        this.dataAndMetas = dataAndMetas;
    }

    public RootJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Map<String, Object>> dataMetas = objectMapper.readValue(json, List.class);
            for (Map<String, Object> dataMeta : dataMetas) {
                dataAndMetas.add(new DataMetaJsonPair(dataMeta));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DataMetaJsonPair> getDataAndMetas() {
        return dataAndMetas;
    }

    public String toJson() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
