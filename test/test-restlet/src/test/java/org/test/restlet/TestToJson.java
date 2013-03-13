package org.test.restlet;

import junit.framework.Assert;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.tuml.runtime.test.BaseLocalDbTest;
import org.tuml.test.Alien;

import java.io.IOException;
import java.util.Map;

public class TestToJson extends BaseLocalDbTest {

	@Test
	public void testToJson() throws IOException {
		Alien human = new Alien(true);
		human.setName("human1");
		db.commit();
		Assert.assertEquals(1 + 1, countVertices());
		Assert.assertEquals(1 + 1 + 1, countEdges());
		ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String,Object> json = objectMapper.readValue(human.toJson(), Map.class);
		Assert.assertEquals(2, json.get("id"));
	}

}
