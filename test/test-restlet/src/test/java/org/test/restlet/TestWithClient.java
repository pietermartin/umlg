package org.test.restlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.tuml.test.Human;
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
	public void testToJson() throws JSONException, ResourceException, IOException {
		TumlRestletServerComponent component = new TumlRestletServerComponent();
		Request request = new Request(Method.GET, "http://localhost:8111/humans/2");
		Response response = new Response(request);
		component.handle(request, response);
		Assert.assertTrue(response.getStatus().isSuccess());
		System.out.println(response.getEntityAsText());
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<Map> list = mapper.readValue(response.getEntityAsText(), List.class);
		Assert.assertEquals(2, list.size());
		Assert.assertEquals("human10", list.get(0).get("name"));
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
		ClientResource service = new ClientResource("http://localhost:8111/humans/2");
		service.setNext(client);
		HumanServerResource humanServerResource = service.getChild("/humans/2", HumanServerResource.class);
		JSONArray jsonArray = new JSONArray(humanServerResource.get().getText());
		JSONObject humanObject = jsonArray.getJSONObject(0);
		System.out.println(humanObject);
		Human h = new Human(true);
		h.fromJson(humanObject.toString());
		System.out.println(h);
	}

}
