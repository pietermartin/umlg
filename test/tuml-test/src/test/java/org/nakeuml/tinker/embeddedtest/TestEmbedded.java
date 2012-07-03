package org.nakeuml.tinker.embeddedtest;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.embeddedtest.REASON;
import org.tuml.inheritencetest.Mamal;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestEmbedded extends BaseLocalDbTest {

	@Test
	public void testOneToManyEnum() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		god.addToREASON(REASON.GOOD);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
		God g = new God(god.getVertex());
		Assert.assertEquals(1, g.getREASON().size());
		Assert.assertEquals(REASON.GOOD, g.getREASON().iterator().next());
	}
	
	@Test
	public void testOneToManyEmbeddedString() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		god.addToEmbeddedString("testthis");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
		God g = new God(god.getVertex());
		Assert.assertEquals(1, g.getEmbeddedString().size());
		Assert.assertEquals("testthis", g.getEmbeddedString().iterator().next());
	}	
	
	@Test
	public void testOneToManyEmbeddedInteger() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		god.addToEmbeddedInteger(1);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
		God g = new God(god.getVertex());
		Assert.assertEquals(1, g.getEmbeddedInteger().size());
		Assert.assertEquals(new Integer(1), g.getEmbeddedInteger().iterator().next());
	}
	
	@Test
	public void testOneEmbeddedEnum() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		god.setReason(REASON.GOOD);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
		God g = new God(god.getVertex());
		Assert.assertEquals(REASON.GOOD, g.getReason());
	}
	
	@Test
	public void testOneEmbeddedEntity() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Mamal mamal = new Mamal(god);
		mamal.setName("PET");
		god.setPet(mamal);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(3, countEdges());
		God g = new God(god.getVertex());
		Assert.assertEquals("PET", g.getPet().getName());
	}	
	
	@Test
	public void testManyEmbeddedEntity() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Mamal mamal1 = new Mamal(god);
		mamal1.setName("PET1");
		Mamal mamal2 = new Mamal(god);
		mamal2.setName("PET2");
		Mamal mamal3 = new Mamal(god);
		mamal3.setName("PET3");
		Mamal mamal4 = new Mamal(god);
		mamal4.setName("PET4");
		god.addToAnimalFarm(mamal1);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(6, countEdges());
		db.startTransaction();
		god.addToAnimalFarm(mamal2);
		god.addToAnimalFarm(mamal3);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(8, countEdges());
		db.startTransaction();
		god.addToAnimalFarm(mamal2);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(8, countEdges());
	}
	
	@Test
	public void testRemoveManyEnum() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		god.addToREASON(REASON.GOOD);
		god.addToREASON(REASON.BAD);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(3, countEdges());
		God g = new God(god.getVertex());
		Assert.assertEquals(2, g.getREASON().size());
		db.startTransaction();
		g.removeFromREASON(REASON.GOOD);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
	}
	
	@Test
	public void testRemoveEmbeddedString() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		god.addToEmbeddedString("testthis");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
		God g = new God(god.getVertex());
		Assert.assertEquals(1, g.getEmbeddedString().size());
		Assert.assertEquals("testthis", g.getEmbeddedString().iterator().next());
		db.startTransaction();
		g.removeFromEmbeddedString("testthis");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(1, countVertices());
		Assert.assertEquals(1, countEdges());
		God g2 = new God(god.getVertex());
		Assert.assertEquals(0, g2.getEmbeddedString().size());
	}
	
}
