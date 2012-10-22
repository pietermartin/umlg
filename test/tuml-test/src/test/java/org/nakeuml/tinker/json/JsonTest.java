package org.nakeuml.tinker.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe.UniverseRuntimePropertyEnum;
import org.tuml.embeddedtest.REASON;
import org.tuml.inheritencetest.Biped;
import org.tuml.inheritencetest.Mamal;
import org.tuml.inheritencetest.Quadped;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class JsonTest extends BaseLocalDbTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testEmbeddedManiesToJson() throws JsonParseException, JsonMappingException, IOException {
		db.startTransaction();
		God god = new God(true);
		god.addToEmbeddedString("embeddedString1");
		god.addToEmbeddedString("embeddedString2");
		god.addToEmbeddedString("embeddedString3");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(4, countVertices());

		ObjectMapper mapper = new ObjectMapper();
		System.out.println(god.toJson());
		Map<String, Object> jsonObject = mapper.readValue(god.toJson(), Map.class);
		Assert.assertFalse(jsonObject.isEmpty());
		Assert.assertSame(ArrayList.class, jsonObject.get("embeddedString").getClass());
		boolean foundembeddedString1 = false;
		boolean foundembeddedString2 = false;
		boolean foundembeddedString3 = false;
		List<String> embeddeds = (List<String>) jsonObject.get("embeddedString");
		for (String s : embeddeds) {
			if (s.equals("embeddedString1")) {
				foundembeddedString1 = true;
			}
			if (s.equals("embeddedString2")) {
				foundembeddedString2 = true;
			}
			if (s.equals("embeddedString3")) {
				foundembeddedString3 = true;
			}
		}
		Assert.assertTrue(foundembeddedString1 && foundembeddedString2 && foundembeddedString3);
	}

	@Test
	public void testEmbeddedManiesFromJson() throws JsonParseException, JsonMappingException, IOException {
		db.startTransaction();
		God god = new God(true);
		god.addToEmbeddedString("embeddedString1");
		god.addToEmbeddedString("embeddedString2");
		god.addToEmbeddedString("embeddedString3");
		god.addToEmbeddedInteger(1);
		god.addToEmbeddedInteger(2);
		god.addToEmbeddedInteger(3);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(7, countVertices());

		String json = god.toJson();
		God godtest = new God(true);
		godtest.fromJson(json);
		Assert.assertEquals(god.getEmbeddedString().size(), godtest.getEmbeddedString().size());
		Assert.assertEquals(god.getEmbeddedInteger().size(), godtest.getEmbeddedInteger().size());
		boolean foundembeddedString1 = false;
		boolean foundembeddedString2 = false;
		boolean foundembeddedString3 = false;
		for (String s : godtest.getEmbeddedString()) {
			if (s.equals("embeddedString1")) {
				foundembeddedString1 = true;
			}
			if (s.equals("embeddedString2")) {
				foundembeddedString2 = true;
			}
			if (s.equals("embeddedString3")) {
				foundembeddedString3 = true;
			}
		}
		Assert.assertTrue(foundembeddedString1 && foundembeddedString2 && foundembeddedString3);
		boolean foundembeddedInteger1 = false;
		boolean foundembeddedInteger2 = false;
		boolean foundembeddedInteger3 = false;
		for (Integer i : godtest.getEmbeddedInteger()) {
			if (i.equals(1)) {
				foundembeddedInteger1 = true;
			}
			if (i.equals(2)) {
				foundembeddedInteger2 = true;
			}
			if (i.equals(3)) {
				foundembeddedInteger3 = true;
			}
		}
		Assert.assertTrue(foundembeddedInteger1 && foundembeddedInteger2 && foundembeddedInteger3);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testJsonToFromWithNulls() throws JsonParseException, JsonMappingException, IOException {
		db.startTransaction();
		God g1 = new God(true);
		g1.setName("g1");
		g1.setReason(REASON.BAD);
		God g2 = new God(true);
		g2.setName("g2");
		g2.setReason(null);
		db.stopTransaction(Conclusion.SUCCESS);

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> jsonMap = objectMapper.readValue(g1.toJson(), Map.class);
		Assert.assertEquals("BAD", jsonMap.get("reason"));

		jsonMap = objectMapper.readValue(g2.toJson(), Map.class);
		Assert.assertEquals("null", jsonMap.get("reason"));

		db.startTransaction();
		God god1FromJson = new God(true);
		god1FromJson.fromJson(g1.toJson());
		God god2FromJson = new God(true);
		god2FromJson.fromJson(g2.toJson());
		db.stopTransaction(Conclusion.SUCCESS);

		Assert.assertEquals(REASON.BAD, god1FromJson.getReason());
		Assert.assertEquals("g1", god1FromJson.getName());
		Assert.assertNull(god2FromJson.getReason());
		Assert.assertEquals("g2", god2FromJson.getName());
		Assert.assertNull(god1FromJson.getBeginning());
		Assert.assertNull(god2FromJson.getBeginning());
		Assert.assertNull(god1FromJson.getPet());
		Assert.assertNull(god2FromJson.getPet());
	}

	@Test
	public void testDates() throws JsonParseException, JsonMappingException, IOException {
		db.startTransaction();
		God g1 = new God(true);
		g1.setName("g1");
		DateTime beginning = new DateTime();
		g1.setBeginning(beginning);
		db.stopTransaction(Conclusion.SUCCESS);

		God testG = new God(g1.getVertex());
		Assert.assertEquals(beginning, testG.getBeginning());

		ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, Object> jsonMap = objectMapper.readValue(testG.toJson(), Map.class);

		Assert.assertEquals(beginning.toString(), jsonMap.get("beginning"));
	}

	@Test
	public void testValidation() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = UniverseRuntimePropertyEnum.asJson();
		@SuppressWarnings("unchecked")
		Map<String, ArrayList<Map<String, Object>>> jsonMap = objectMapper.readValue(json, Map.class);
		ArrayList<Map<String, Object>> o = jsonMap.get("properties");
		boolean foundValidations = false;
		for (Map<String, Object> map : o) {
			foundValidations = map.containsKey("validations");
		}
		Assert.assertTrue(foundValidations);
	}
	
	@Test
	public void testWithInheritence() throws JsonParseException, JsonMappingException, IOException {
		db.startTransaction();
		God g1 = new God(true);
		g1.setName("g1");
		DateTime beginning = new DateTime();
		g1.setBeginning(beginning);
		Mamal mamal1 = new Mamal(g1);
		mamal1.setName("mamal1");
		Biped biped1 = new Biped(g1);
		biped1.setName("biped1");
		Quadped quadped1 = new Quadped(g1);
		quadped1.setName("quadped1");
		db.stopTransaction(Conclusion.SUCCESS);
		
		ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, Object> jsonMap = objectMapper.readValue(quadped1.toJson(), Map.class);
		Assert.assertEquals(4, jsonMap.size());
		Assert.assertEquals(jsonMap.get("name"), "quadped1");
	}
	
}
