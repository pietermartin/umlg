package org.test.restlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.*;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.tuml.restandjson.RestAndJsonComponent;
import org.tuml.restlet.RootServerResource;
import org.tuml.restlet.test.BaseRestletTest;
import org.tuml.test.restlet.Hand_hand_finger_Finger_ServerResource;
import org.tuml.test.restlet.HumanServerResource;
import org.tuml.test.restlet.Human_human_hand_Hand_ServerResource;

public class TestWithClient extends BaseRestletTest {

    @Test
    public void testRoot() throws ResourceException, JSONException, IOException {
        Client client = new Client(new Context(), Protocol.HTTP);
        ClientResource service = new ClientResource("http://localhost:8111/");
        service.setNext(client);
        RootServerResource rootServerResource = service.getChild("/restAndJson", RootServerResource.class);
        String text = rootServerResource.get().getText();
        JSONArray jsonArray = new JSONArray(text);
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        Assert.assertNotNull(jsonObject.get("data"));
        Assert.assertEquals(JSONArray.class, jsonObject.get("data").getClass());
        Assert.assertNotNull(jsonObject.get("meta"));
        Assert.assertEquals(JSONObject.class, jsonObject.get("meta").getClass());
        JSONObject meta = (JSONObject) jsonObject.get("meta");
        Assert.assertEquals(JSONObject.class, meta.get("to").getClass());
    }

    private JSONObject getJsonObject(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        JSONObject jsonData = (JSONObject) jsonArray.get(0);
        jsonArray = (JSONArray) jsonData.get("data");
        return (JSONObject) jsonArray.get(0);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPostFingerToHand() throws ResourceException, JSONException, IOException {
        Client client = new Client(new Context(), Protocol.HTTP);
        ClientResource service = new ClientResource("http://localhost:8111/");
        service.setNext(client);
        HumanServerResource humanServerResource = service.getChild("/restAndJson/humans/2", HumanServerResource.class);
        String humanJson = humanServerResource.get().getText();
        JSONObject humanJsonObject = getJsonObject(humanJson);
        Human_human_hand_Hand_ServerResource human_human_hand_hand_serverResource = service.getChild("/restAndJson/humans/" + humanJsonObject.get("id") + "/hand", Human_human_hand_Hand_ServerResource.class);
        JSONObject handJsonObject = getJsonObject(human_human_hand_hand_serverResource.get().getText());
        Hand_hand_finger_Finger_ServerResource hand_finger_ServerResource = service.getChild("/restAndJson/hands/" + handJsonObject.get("id") + "/finger", Hand_hand_finger_Finger_ServerResource.class);
        JSONObject fingerJsonObject = getJsonObject(hand_finger_ServerResource.get().getText());

        Assert.assertNotNull(fingerJsonObject.get("id"));
        Assert.assertNotSame("", fingerJsonObject.get("id"));
        String name = (String) fingerJsonObject.get("name");
        Assert.assertTrue(name.startsWith("finger"));

        fingerJsonObject.put("name", "testFingerName");
        Representation r = hand_finger_ServerResource.post(new JsonRepresentation(fingerJsonObject.toString()));

        JSONArray fingerArray = new JSONArray(r.getText());
        boolean found = false;
        JSONObject finger = (JSONObject) fingerArray.get(0);
        JSONArray data = (JSONArray) finger.get("data");
        for (int i = 0; i < data.length(); i++) {
            finger = (JSONObject) data.get(i);
            if (finger.get("name").equals("testFingerName")) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void postNewFingersToHand() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
        Client client = new Client(new Context(), Protocol.HTTP);
        ClientResource service = new ClientResource("http://localhost:8111/");
        service.setNext(client);
        HumanServerResource humanServerResource = service.getChild("/restAndJson/humans/2", HumanServerResource.class);
        String humanJson = humanServerResource.get().getText();
        JSONObject humanJsonObject = getJsonObject(humanJson);
        Human_human_hand_Hand_ServerResource human_human_hand_hand_serverResource = service.getChild("/restAndJson/humans/" + humanJsonObject.get("id") + "/hand", Human_human_hand_Hand_ServerResource.class);
        JSONObject handJsonObject = getJsonObject(human_human_hand_hand_serverResource.get().getText());
        Hand_hand_finger_Finger_ServerResource hand_finger_ServerResource = service.getChild("/restAndJson/hands/" + handJsonObject.get("id") + "/finger", Hand_hand_finger_Finger_ServerResource.class);

        List<Integer> manyRequiredIntegers = new ArrayList<Integer>();
        manyRequiredIntegers.add(new Integer(1));
        ObjectMapper fingerMapper = new ObjectMapper();
        List<Map<String, Object>> fingers = new ArrayList<Map<String, Object>>();
        Map<String, Object> f1Data = new HashMap<String, Object>();
        f1Data.put("name", "f1");
        f1Data.put("manyRequiredInteger", manyRequiredIntegers);
        fingers.add(f1Data);
        Map<String, Object> f2Data = new HashMap<String, Object>();
        f2Data.put("name", "f2");
        f2Data.put("manyRequiredInteger", manyRequiredIntegers);
        fingers.add(f2Data);
        Map<String, Object> f3Data = new HashMap<String, Object>();
        f3Data.put("name", "f3");
        f3Data.put("manyRequiredInteger", manyRequiredIntegers);
        fingers.add(f3Data);
        Map<String, Object> f4Data = new HashMap<String, Object>();
        f4Data.put("name", "f4");
        f4Data.put("manyRequiredInteger", manyRequiredIntegers);
        fingers.add(f4Data);
        Map<String, Object> f5Data = new HashMap<String, Object>();
        f5Data.put("name", "f5");
        f5Data.put("manyRequiredInteger", manyRequiredIntegers);
        fingers.add(f5Data);

        Representation r = hand_finger_ServerResource.post(new JsonRepresentation(fingerMapper.writeValueAsString(fingers)));
        JSONArray fingerArray = new JSONArray(r.getText());
        JSONObject finger = (JSONObject) fingerArray.get(0);
        JSONArray data = (JSONArray) finger.get("data");
        Assert.assertTrue(data.length() > 5);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void updateFingersOfHand() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
        Client client = new Client(new Context(), Protocol.HTTP);
        ClientResource service = new ClientResource("http://localhost:8111/");
        service.setNext(client);
        HumanServerResource humanServerResource = service.getChild("/restAndJson/humans/2", HumanServerResource.class);
        String humanJson = humanServerResource.get().getText();
        JSONObject humanJsonObject = getJsonObject(humanJson);
        Human_human_hand_Hand_ServerResource human_human_hand_hand_serverResource = service.getChild("/restAndJson/humans/" + humanJsonObject.get("id") + "/hand", Human_human_hand_Hand_ServerResource.class);
        JSONObject handJsonObject = getJsonObject(human_human_hand_hand_serverResource.get().getText());
        Hand_hand_finger_Finger_ServerResource hand_finger_ServerResource = service.getChild("/restAndJson/hands/" + handJsonObject.get("id") + "/finger", Hand_hand_finger_Finger_ServerResource.class);

        JSONArray fingersResult = new JSONArray(hand_finger_ServerResource.get().getText());
        JSONObject fingers = (JSONObject) fingersResult.get(0);
        JSONArray data = (JSONArray) fingers.get("data");
        for (int i = 0; i < data.length(); i++) {
            JSONObject finger = (JSONObject) data.get(i);
            finger.put("name", "n" + i);
        }

        Representation r = hand_finger_ServerResource.put(new JsonRepresentation(data.toString()));
        JSONArray fingerArray = new JSONArray(r.getText());
        JSONObject finger = (JSONObject) fingerArray.get(0);
        data = (JSONArray) finger.get("data");
        Assert.assertEquals(5, data.length());
        boolean foundN1 = false;
        boolean foundN2 = false;
        boolean foundN3 = false;
        boolean foundN4 = false;
        boolean foundN5 = false;
        for (int i = 0; i < data.length(); i++) {
            finger = (JSONObject) data.get(i);
            if (finger.get("name").equals("n0")) {
                foundN1 = true;
            }
            if (finger.get("name").equals("n1")) {
                foundN2 = true;
            }
            if (finger.get("name").equals("n2")) {
                foundN3 = true;
            }
            if (finger.get("name").equals("n3")) {
                foundN4 = true;
            }
            if (finger.get("name").equals("n4")) {
                foundN5 = true;
            }
        }
        Assert.assertTrue(foundN1 && foundN2 && foundN3 && foundN4 && foundN5);
    }

    @Override
    protected Component instantiateComponent() {
        return new RestAndJsonComponent();
    }
}
