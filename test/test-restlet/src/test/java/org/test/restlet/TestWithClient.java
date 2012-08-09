package org.test.restlet;

import java.io.IOException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.resource.ResourceException;
import org.tuml.runtime.test.BaseLocalDbTest;

public class TestWithClient extends BaseLocalDbTest {

	@Test
	public void testToJson() throws JSONException, ResourceException, IOException {
		TumlRestletServerComponent component = new TumlRestletServerComponent();
		Request request = new Request(Method.GET, "http://localhost:8111/humans"); 
		Response response = new Response(request); 
		component.handle(request, response); 
		Assert.assertTrue(response.getStatus().isSuccess());
		JSONArray jsonArray = new JSONArray(response.getEntityAsText());
//		Assert.assertEquals("human1", jsonObject.get("name"));

	}
}
