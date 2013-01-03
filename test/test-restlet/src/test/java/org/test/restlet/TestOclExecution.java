package org.test.restlet;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import junit.framework.Assert;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.tuml.restandjson.RestAndJsonComponent;
import org.tuml.root.QueryExecuteServerResource;
import org.tuml.root.Root;
import org.tuml.test.Hand;
import org.tuml.test.Human;

public class TestOclExecution {

    private static final RestAndJsonComponent tumlRestletServerComponent2 = new RestAndJsonComponent();

    public  TestOclExecution() throws Exception {
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        tumlRestletServerComponent2.start();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        tumlRestletServerComponent2.stop();
    }

    @Test
    public void testOclExecution() throws ResourceException, JSONException, IOException, org.json.JSONException {
        Client client = new Client(new Context(), Protocol.HTTP);
        ClientResource service = new ClientResource("http://localhost:8111/");
        service.setNext(client);
        Human human = Root.INSTANCE.getHuman().get(0);
        Set<Hand> hands = human.getHand();
        Hand theHand = null;
        for (Hand hand : hands) {
            if (hand.getName().equals("hand1")) {
                theHand = hand;
                break;
            }
        }
        QueryExecuteServerResource oclExecutionServerResource = service.getChild("/restAndJson/" + human.getId()
                + "/oclExecuteQuery?ocl=self.hand->select(name='hand1')", QueryExecuteServerResource.class);
        String text = oclExecutionServerResource.get().getText();
        JSONArray jsonArray = new JSONArray(text);
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        jsonArray = (JSONArray) jsonObject.get("data");
        jsonObject = (JSONObject) jsonArray.get(0);
        Assert.assertEquals(theHand.getId().intValue(), jsonObject.get("id"));
    }

}
