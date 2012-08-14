package org.test.restlet;

import java.io.IOException;

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
import org.tuml.test.restlet.Hand_finger_ServerResource;
import org.tuml.test.restlet.HumanServerResource;
import org.tuml.test.restlet.Human_ring_ServerResource;

import restlet.RootServerResource;

public class TestWithClient {

	@BeforeClass
	public static void beforeClass() throws Exception {
		new TumlRestletServerComponent().start();
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		new TumlRestletServerComponent().stop();
	}
	
	@Test
	public void testRoot() throws ResourceException, JSONException, IOException {
		Client client = new Client(new Context(), Protocol.HTTP);
		ClientResource service = new ClientResource("http://localhost:8111/");
		service.setNext(client);
		RootServerResource rootServerResource = service.getChild("/", RootServerResource.class);
		
		JSONArray jsonArray = new JSONArray(rootServerResource.get().getText());
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		JSONArray humanJson = jsonObject.getJSONArray("Human");
		System.out.println(humanJson);
	}
	
	@Test
	public void testLoadHumanFromJson() throws ResourceException, JSONException, IOException {
		Client client = new Client(new Context(), Protocol.HTTP);
		ClientResource service = new ClientResource("http://localhost:8111/");
		service.setNext(client);
		HumanServerResource humanServerResource = service.getChild("/humans/2", HumanServerResource.class);
		JSONArray jsonArray = new JSONArray(humanServerResource.get().getText());
		JSONObject humanObject = jsonArray.getJSONObject(0);
		humanObject.put("name", "johnny");
		humanServerResource.put(new JsonRepresentation(humanObject.toString()));
		System.out.println(humanObject);
	}
	
	@Test
	public void testPutFingerOnHand() throws ResourceException, JSONException, IOException {
		Client client = new Client(new Context(), Protocol.HTTP);
		ClientResource service = new ClientResource("http://localhost:8111/");
		service.setNext(client);
		Hand_finger_ServerResource  hand_finger_ServerResource = service.getChild("/hands/3/finger", Hand_finger_ServerResource.class);
		JSONArray jsonArray = new JSONArray(hand_finger_ServerResource.get().getText());
		Assert.assertEquals(7, jsonArray.length());
		
		JSONObject fingerObject = (JSONObject) jsonArray.get(0);
		Assert.assertNotNull(fingerObject.get("id"));
		Assert.assertNotSame("", fingerObject.get("id"));
		fingerObject.put("name", "testFingerName");
		Representation handServerResource = hand_finger_ServerResource.put(new JsonRepresentation(fingerObject.toString()));
		JSONArray handServerResourceArray = new JSONArray(handServerResource.getText());
		Assert.assertEquals(7, handServerResourceArray.length());
		boolean found = false;
		for (int i = 0; i < 7; i++) {
			JSONObject o = handServerResourceArray.getJSONObject(i);
			if (o.getString("name").equals("testFingerName")) {
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
		
	}
	
	@Test
	public void testPutFingerOnHandAtIndex() throws ResourceException, JSONException, IOException {
		Client client = new Client(new Context(), Protocol.HTTP);
		ClientResource service = new ClientResource("http://localhost:8111/");
		service.setNext(client);
		Hand_finger_ServerResource  hand_finger_ServerResource = service.getChild("/hands/3/finger?index=2", Hand_finger_ServerResource.class);
		JSONArray jsonArray = new JSONArray(hand_finger_ServerResource.get().getText());
		Assert.assertEquals(7, jsonArray.length());
		JSONObject fingerObject = (JSONObject) jsonArray.get(0);
		Assert.assertNotNull(fingerObject.get("id"));
		Assert.assertNotSame("", fingerObject.get("id"));
		fingerObject.put("name", "testFingerName");
		Representation handServerResource = hand_finger_ServerResource.put(new JsonRepresentation(fingerObject.toString()));
		JSONArray handServerResourceArray = new JSONArray(handServerResource.getText());
		Assert.assertEquals(7, handServerResourceArray.length());
		JSONObject o = handServerResourceArray.getJSONObject(2);
		Assert.assertEquals("testFingerName", o.getString("name"));

		
		hand_finger_ServerResource = service.getChild("/hands/3/finger", Hand_finger_ServerResource.class);
		jsonArray = new JSONArray(hand_finger_ServerResource.get().getText());
		Assert.assertEquals(7, jsonArray.length());
		fingerObject = (JSONObject) jsonArray.get(0);
		Assert.assertNotNull(fingerObject.get("id"));
		Assert.assertNotSame("", fingerObject.get("id"));
		fingerObject.put("name", "testFingerName");
		hand_finger_ServerResource = service.getChild("/hands/3/finger?index=4", Hand_finger_ServerResource.class);
		handServerResource = hand_finger_ServerResource.put(new JsonRepresentation(fingerObject.toString()));
		hand_finger_ServerResource = service.getChild("/hands/3/finger", Hand_finger_ServerResource.class);
		handServerResourceArray = new JSONArray(handServerResource.getText());
		Assert.assertEquals(7, handServerResourceArray.length());
		o = handServerResourceArray.getJSONObject(4);
		Assert.assertEquals("testFingerName", o.getString("name"));
	}
	
	@Test
	public void testPostRingOnHuman() throws ResourceException, JSONException, IOException {
		Client client = new Client(new Context(), Protocol.HTTP);
		ClientResource service = new ClientResource("http://localhost:8111/");
		service.setNext(client);
		Human_ring_ServerResource  human_ring_ServerResource = service.getChild("/humans/2/ring", Human_ring_ServerResource.class);
		JSONArray jsonArray = new JSONArray(human_ring_ServerResource.get().getText());
		int numberOfRings = jsonArray.length();
		
		JSONObject ringObject = (JSONObject) jsonArray.get(0);
		Assert.assertNotNull(ringObject.get("id"));
		Assert.assertNotSame("", ringObject.get("id"));
		ringObject.put("name", "testRingName");
		Representation humanRingResource = human_ring_ServerResource.post(new JsonRepresentation(ringObject.toString()));
		JSONArray humanServerResourceArray = new JSONArray(humanRingResource.getText());
		//Currently you can not add the same element twice in a one to many with a bag on the many side
		Assert.assertEquals(numberOfRings, humanServerResourceArray.length());
	}	

}
