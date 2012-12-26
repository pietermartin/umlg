package org.nakeuml.tinker.concretetest;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.test.tinker.BaseLocalDbTest;
import org.tinker.concretetest.God;
import org.tinker.concretetest.GodAudit;
import org.tinker.embeddedtest.REASON;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class TestEmbedded extends BaseLocalDbTest {

	@Test
	public void testSingleEnum() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		god.setReason(REASON.GOOD);
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(2, countVertices());
		assertEquals(2, countEdges());
		God godTest = new God(god.getVertex());
		Assert.assertEquals(1, godTest.getAudits().size());
		GodAudit auditGod = godTest.getAudits().get(0);
		Assert.assertNotNull(auditGod);
		Assert.assertEquals("THEGOD", auditGod.getName());
		Assert.assertEquals(REASON.GOOD, auditGod.getReason());
		God original = auditGod.getOriginal();
		Assert.assertNotNull(original);
		Assert.assertEquals("THEGOD", original.getName());
	}
	
	@Test
	public void testEmbeddedIntegers() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		god.addToEmbeddedInteger(0);
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(4, countVertices());
		assertEquals(4, countEdges());
		
		db.startTransaction();
		god.addToEmbeddedInteger(1);
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(7, countVertices());
		assertEquals(7, countEdges());
		
		God godTest = new God(god.getVertex());
		Assert.assertEquals(2, godTest.getAudits().size());
		Assert.assertEquals(2, godTest.getAudits().get(1).getEmbeddedInteger().size());

		//Traversing the audit calls previous which lazily creates the previous edge
		assertEquals(8, countEdges());

		db.startTransaction();
		god.addToEmbeddedInteger(2);
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(10, countVertices());
		assertEquals(11, countEdges());
		
		godTest = new God(god.getVertex());
		Assert.assertEquals(3, godTest.getAudits().size());
		Assert.assertEquals(1, godTest.getAudits().get(0).getEmbeddedInteger().size());
		Assert.assertEquals(2, godTest.getAudits().get(1).getEmbeddedInteger().size());
		Assert.assertEquals(3, godTest.getAudits().get(2).getEmbeddedInteger().size());

		assertEquals(12, countEdges());

		db.startTransaction();
		god.removeFromEmbeddedInteger(1);
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(11, countVertices());
		assertEquals(13, countEdges());

		Assert.assertEquals(4, godTest.getAudits().size());
		Assert.assertEquals(1, godTest.getAudits().get(0).getEmbeddedInteger().size());
		Assert.assertEquals(2, godTest.getAudits().get(1).getEmbeddedInteger().size());
		Assert.assertEquals(3, godTest.getAudits().get(2).getEmbeddedInteger().size());
		Assert.assertEquals(2, godTest.getAudits().get(3).getEmbeddedInteger().size());

		boolean found0 = false;
		boolean found2 = false;
		Set<Integer> integers = godTest.getAudits().get(3).getEmbeddedInteger();
		for (Integer integer : integers) {
			if (integer.equals(0)) {
				found0 = true;
			}
			if (integer.equals(2)) {
				found2 = true;
			}
		}
		Assert.assertTrue(found0);
		Assert.assertTrue(found2);
	}	
	
}
