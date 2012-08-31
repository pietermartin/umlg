package org.test.restlet;

import java.io.IOException;
import java.util.Map;

import junit.framework.Assert;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.tuml.runtime.test.BaseLocalDbTest;
import org.tuml.test.Human;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestToJson extends BaseLocalDbTest {

	@Test
	public void testToJson() throws JsonParseException, JsonMappingException, IOException {
		db.startTransaction();
		Human human = new Human(true);
		human.setName("human1");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(1, countVertices());
		Assert.assertEquals(1, countEdges());
		ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String,Object> json = objectMapper.readValue(human.toJson(), Map.class);
		Assert.assertEquals(2, json.get("id"));
	}

}
