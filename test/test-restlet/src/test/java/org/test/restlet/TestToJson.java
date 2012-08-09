package org.test.restlet;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.tuml.runtime.test.BaseLocalDbTest;
import org.tuml.test.Human;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestToJson extends BaseLocalDbTest {

	@Test
	public void testToJson() throws JSONException {
		db.startTransaction();
		Human human = new Human(true);
		human.setName("human1");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(1, countVertices());
		Assert.assertEquals(1, countEdges());
		System.out.println(human.toJson());
		JSONObject jsonObject = new JSONObject(human.toJson());
		Assert.assertEquals("human1", jsonObject.get("name"));
	}

	@Test
	public void testMany() throws JSONException {
		db.startTransaction();
		Human human1 = new Human(true);
		human1.setName("human1");
		Human human2 = new Human(true);
		human2.setName("human2");
		db.stopTransaction(Conclusion.SUCCESS);

		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(human1.toJson());
		json.append(",");
		json.append(human2.toJson());
		json.append("]");

		JSONArray jSONArray = new JSONArray(json.toString());
		Assert.assertEquals(JSONObject.class, jSONArray.get(0).getClass());
		JSONObject jsonObject = (JSONObject) jSONArray.get(0);
		Assert.assertEquals("human1", jsonObject.get("name"));
	}

}
