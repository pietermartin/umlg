package org.nakeuml.tinker.concretetest;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Test;
import org.umlg.test.tinker.BaseLocalDbTest;
import org.tinker.concretetest.God;
import org.tinker.concretetest.GodAudit;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class TestSimpleTypes extends BaseLocalDbTest {

	@Test
	public void testSimpleType() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(2, countVertices());
		assertEquals(2, countEdges());
		God godTest = new God(god.getVertex());
		Assert.assertEquals(1, godTest.getAudits().size());
		GodAudit auditGod = godTest.getAudits().get(0);
		Assert.assertNotNull(auditGod);
		Assert.assertEquals("THEGOD", auditGod.getName());
		God original = auditGod.getOriginal();
		Assert.assertNotNull(original);
		Assert.assertEquals("THEGOD", original.getName());
	}
	
	@Test
	public void testSimpleTypeMultipleTransactions() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(2, countVertices());
		assertEquals(2, countEdges());
		God godTest = new God(god.getVertex());
		Assert.assertEquals(1, godTest.getAudits().size());
		GodAudit auditGod = godTest.getAudits().get(0);
		Assert.assertNotNull(auditGod);
		Assert.assertEquals("THEGOD", auditGod.getName());
		God original = auditGod.getOriginal();
		Assert.assertNotNull(original);
		Assert.assertEquals("THEGOD", original.getName());
		db.startTransaction();
		God godTest2 = new God(god.getVertex());
		godTest2.setName("THEGODDER");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, godTest2.getAudits().size());
		Assert.assertEquals("THEGODDER", godTest2.getAudits().get(1).getName());
		Assert.assertEquals("THEGOD", godTest2.getAudits().get(1).getPreviousAuditEntry().getName());
	}	

}
