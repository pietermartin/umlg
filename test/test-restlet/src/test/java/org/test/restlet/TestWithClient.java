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
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.tuml.restlet.RootServerResource;
import org.tuml.test.restlet.Hand_hand_finger_Finger_ServerResource;
import org.tuml.test.restlet.HumanServerResource;

public class TestWithClient {

	@BeforeClass
	public static void beforeClass() throws Exception {
		new TumlRestletServerComponent2().start();
	}

	@AfterClass
	public static void afterClass() throws Exception {
		new TumlRestletServerComponent2().stop();
	}

	@Test
	public void testRoot() throws ResourceException, JSONException, IOException {
		Client client = new Client(new Context(), Protocol.HTTP);
		ClientResource service = new ClientResource("http://localhost:8111/");
		service.setNext(client);
		RootServerResource rootServerResource = service.getChild("/restAndJson", RootServerResource.class);
		JSONObject jsonObject = new JSONObject(rootServerResource.get().getText());
		Assert.assertNotNull(jsonObject.get("data"));
		Assert.assertEquals(JSONArray.class, jsonObject.get("data").getClass());
		Assert.assertNotNull(jsonObject.get("meta"));
		Assert.assertEquals(JSONObject.class, jsonObject.get("meta").getClass());
		JSONObject meta = (JSONObject) jsonObject.get("meta");
		Assert.assertEquals(JSONArray.class, meta.get("properties").getClass());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLoadAndPutPropertyHumanFromJson() throws ResourceException, JSONException, IOException {
		Client client = new Client(new Context(), Protocol.HTTP);
		ClientResource service = new ClientResource("http://localhost:8111/");
		service.setNext(client);
		HumanServerResource humanServerResource = service.getChild("/restAndJson/humans/2", HumanServerResource.class);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> humanMap = objectMapper.readValue(humanServerResource.get().getText(), Map.class);
		Assert.assertEquals(2, humanMap.size());
		Assert.assertNotNull(humanMap.get("data"));
		Assert.assertNotNull(humanMap.get("meta"));

		Map<String, Object> humanDataMap = (Map<String, Object>) humanMap.get("data");
		Assert.assertEquals("human10", humanDataMap.get("name"));
		humanDataMap.put("name", "johnny");
		humanMap.put("data", humanDataMap);

		// Write the new name to the server
		humanServerResource.put(new JsonRepresentation(objectMapper.writeValueAsString(humanDataMap)));

		// Fetch the data again
		humanMap = objectMapper.readValue(humanServerResource.get().getText(), Map.class);
		Assert.assertEquals(2, humanMap.size());
		Assert.assertNotNull(humanMap.get("data"));
		Assert.assertNotNull(humanMap.get("meta"));

		humanDataMap = (Map<String, Object>) humanMap.get("data");
		Assert.assertEquals("johnny", humanDataMap.get("name"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPostFingerToHand() throws ResourceException, JSONException, IOException {
		Client client = new Client(new Context(), Protocol.HTTP);
		ClientResource service = new ClientResource("http://localhost:8111/");
		service.setNext(client);
		Hand_hand_finger_Finger_ServerResource hand_finger_ServerResource = service.getChild("/restAndJson/hands/3/finger", Hand_hand_finger_Finger_ServerResource.class);

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, List<Map<String, Object>>> handFingerMap = objectMapper.readValue(hand_finger_ServerResource.get().getText(), Map.class);

		Assert.assertEquals(5, handFingerMap.get("data").size());

		Map<String, Object> fingerPropertyMap = handFingerMap.get("data").get(0);
		Assert.assertNotNull(fingerPropertyMap.get("id"));
		Assert.assertNotSame("", fingerPropertyMap.get("id"));
		Assert.assertEquals("finger0", fingerPropertyMap.get("name"));

		fingerPropertyMap.put("name", "testFingerName");

		Representation r = hand_finger_ServerResource.post(new JsonRepresentation(objectMapper.writeValueAsString(fingerPropertyMap)));
		handFingerMap = objectMapper.readValue(r.getText(), Map.class);
		Assert.assertEquals(5, handFingerMap.get("data").size());
		boolean found = false;
		for (int i = 0; i < 5; i++) {
			fingerPropertyMap = handFingerMap.get("data").get(i);
			if (fingerPropertyMap.get("name").equals("testFingerName")) {
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void postNewFingersToHand() throws JsonGenerationException, JsonMappingException, IOException {
		Client client = new Client(new Context(), Protocol.HTTP);
		ClientResource service = new ClientResource("http://localhost:8111/");
		service.setNext(client);
		Hand_hand_finger_Finger_ServerResource hand_finger_ServerResource = service.getChild("/restAndJson/hands/3/finger", Hand_hand_finger_Finger_ServerResource.class);

		ObjectMapper finggerMapper = new ObjectMapper();
		List<Map<String,Object>> fingers = new ArrayList<Map<String,Object>>();
		Map<String,Object> f1Data = new HashMap<String,Object>();
		f1Data.put("name", "f1");
		fingers.add(f1Data);
		Map<String,Object> f2Data = new HashMap<String,Object>();
		f2Data.put("name", "f2");
		fingers.add(f2Data);
		Map<String,Object> f3Data = new HashMap<String,Object>();
		f3Data.put("name", "f3");
		fingers.add(f3Data);
		Map<String,Object> f4Data = new HashMap<String,Object>();
		f4Data.put("name", "f4");
		fingers.add(f4Data);
		Map<String,Object> f5Data = new HashMap<String,Object>();
		f5Data.put("name", "f5");
		fingers.add(f5Data);

		Representation r = hand_finger_ServerResource.post(new JsonRepresentation(finggerMapper.writeValueAsString(fingers)));
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, List<Map<String, Object>>> handFingerMap = objectMapper.readValue(r.getText(), Map.class);
		Assert.assertEquals(10, handFingerMap.get("data").size());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateFingersOfHand() throws JsonGenerationException, JsonMappingException, IOException {
		Client client = new Client(new Context(), Protocol.HTTP);
		ClientResource service = new ClientResource("http://localhost:8111/");
		service.setNext(client);
		Hand_hand_finger_Finger_ServerResource hand_finger_ServerResource = service.getChild("/restAndJson/hands/3/finger", Hand_hand_finger_Finger_ServerResource.class);

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, List<Map<String, Object>>> handFingerMap = objectMapper.readValue(hand_finger_ServerResource.get().getText(), Map.class);
		Assert.assertEquals(5, handFingerMap.get("data").size());
		List<Map<String, Object>> list = handFingerMap.get("data");
		int count = 1;
		for (Map<String, Object> map : list) {
			map.put("name", "n" + count++);
		}
		Representation r = hand_finger_ServerResource.post(new JsonRepresentation(objectMapper.writeValueAsString(list)));
		handFingerMap = objectMapper.readValue(r.getText(), Map.class);
		Assert.assertEquals(5, handFingerMap.get("data").size());
		list = handFingerMap.get("data");
		boolean foundn1 = false;
		boolean foundn2 = false;
		boolean foundn3 = false;
		boolean foundn4 = false;
		boolean foundn5 = false;
		for (Map<String, Object> map : list) {
			if (map.get("name").equals("n1")) {
				foundn1 = true;
			}
			if (map.get("name").equals("n2")) {
				foundn2 = true;
			}
			if (map.get("name").equals("n3")) {
				foundn3 = true;
			}
			if (map.get("name").equals("n4")) {
				foundn4 = true;
			}
			if (map.get("name").equals("n5")) {
				foundn5 = true;
			}
		}
		Assert.assertTrue(foundn1 && foundn2 && foundn3 && foundn4 && foundn5 );
		
		hand_finger_ServerResource = service.getChild("/restAndJson/hands/3/finger", Hand_hand_finger_Finger_ServerResource.class);
		handFingerMap = objectMapper.readValue(hand_finger_ServerResource.get().getText(), Map.class);
		Assert.assertEquals(5, handFingerMap.get("data").size());
		
	}

}
