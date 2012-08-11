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

}
