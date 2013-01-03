package org.test.restlet;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.*;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.tuml.restandjson.RestAndJsonComponent;
import org.tuml.runtime.restlet.TumlMetaQueryServerResource;
import org.tuml.runtime.restlet.TumlMetaQueryServerResourceImpl;

import java.io.IOException;

/**
 * Date: 2012/12/26
 * Time: 6:14 PM
 */
public class TestClassQueryWithClient {

    private static RestAndJsonComponent tumlRestletServerComponent2 = new RestAndJsonComponent();

    @BeforeClass
    public static void beforeClass() throws Exception {
        tumlRestletServerComponent2.start();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        tumlRestletServerComponent2.stop();
    }

    @org.junit.Test
    public void testClassQuery() throws ResourceException, JSONException, IOException {
        Client client = new Client(new Context(), Protocol.HTTP);
        ClientResource service = new ClientResource("http://localhost:8111/");
        service.setNext(client);
        TumlMetaQueryServerResource rootServerResource = service.getChild("/restAndJson/classquery/2/query", TumlMetaQueryServerResource.class);
        String s = rootServerResource.get().getText();
        System.out.println("halo == " + s);
    }
}
