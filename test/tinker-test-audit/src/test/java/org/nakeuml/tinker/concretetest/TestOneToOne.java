package org.nakeuml.tinker.concretetest;

import org.junit.Assert;

import org.junit.Test;
import org.umlg.test.tinker.BaseLocalDbTest;
import org.tinker.concretetest.God;
import org.tinker.onetoone.OneOne;
import org.tinker.onetoone.OneTwo;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class TestOneToOne extends BaseLocalDbTest {

	@Test
	public void testOneToOneSetToNull() {
		db.startTransaction();
		God god = new God(true);
		god.setName("GODDER");
		OneOne oneOne = new OneOne(god);
		oneOne.setName("oneone1");
		OneTwo oneTwo = new OneTwo(god);
		oneTwo.setName("onetwo1");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(6, countVertices());
		Assert.assertEquals(8, countEdges());
		db.startTransaction();
		oneOne.setOneTwo(oneTwo);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(8, countVertices());
		Assert.assertEquals(12, countEdges());
		db.startTransaction();
		oneOne.setOneTwo(null);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(10, countVertices());
		Assert.assertEquals(14, countEdges());
		Assert.assertEquals(3, oneOne.getAudits().size());
		Assert.assertNull(oneOne.getAudits().get(2).getOneTwo());
	}

	@Test
	public void testOneToOne() {
		db.startTransaction();
		God god = new God(true);
		god.setName("GODDER");
		OneOne oneOne1 = new OneOne(god);
		oneOne1.setName("oneone1");
		OneOne oneOne2 = new OneOne(god);
		oneOne2.setName("oneone2");
		OneTwo oneTwo1 = new OneTwo(god);
		oneTwo1.setName("onetwo1");
		OneTwo oneTwo2 = new OneTwo(god);
		oneTwo2.setName("onetwo2");
		
		oneOne1.setOneTwo(oneTwo1);
		oneOne2.setOneTwo(oneTwo2);
		
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(10, countVertices());
		Assert.assertEquals(18, countEdges());
		
		db.startTransaction();
		oneOne1.setOneTwo(oneTwo2);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(14, countVertices());
		Assert.assertEquals(24, countEdges());
		
	}

	@Test
	public void testRemovalOfOneToOne() {
		db.startTransaction();
		God god = new God(true);
		god.setName("GODDER");
		OneOne oneOne1 = new OneOne(god);
		oneOne1.setName("oneone1");
		OneOne oneOne2 = new OneOne(god);
		oneOne2.setName("oneone2");
		OneTwo oneTwo1 = new OneTwo(god);
		oneTwo1.setName("onetwo1");
		OneTwo oneTwo2 = new OneTwo(god);
		oneTwo2.setName("onetwo2");
		
		oneOne1.setOneTwo(oneTwo1);
		oneOne2.setOneTwo(oneTwo2);
		
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(10, countVertices());
		Assert.assertEquals(18, countEdges());

		db.startTransaction();
		oneOne1.getOneTwo().markDeleted();
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(12, countVertices());
		//Remember the edge to the original gets deleted
		Assert.assertEquals(20, countEdges());
		
	}
	
}
