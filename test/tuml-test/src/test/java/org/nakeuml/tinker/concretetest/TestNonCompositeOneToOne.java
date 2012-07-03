package org.nakeuml.tinker.concretetest;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.concretetest.Angel;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.onetoone.OneOne;
import org.tuml.onetoone.OneTwo;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestNonCompositeOneToOne extends BaseLocalDbTest {

	@Test
	public void testNonCompositeOneToOneCreation() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		Angel angel = new Angel(god);
		angel.setName("angel1");
		universe1.setAngel(angel);
		db.stopTransaction(Conclusion.SUCCESS);
		Universe universeTest = new Universe(universe1.getVertex());
		Assert.assertNotNull(universeTest.getAngel());
		Assert.assertEquals(7, countEdges());
	}

	@Test
	public void testNonCompositeOneToOneRemoval() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		Angel angel = new Angel(god);
		angel.setName("angel1");
		universe1.setAngel(angel);
		db.stopTransaction(Conclusion.SUCCESS);
		Universe universeTest = new Universe(universe1.getVertex());
		Assert.assertNotNull(universeTest.getAngel());
		Assert.assertEquals(7, countEdges());
		db.startTransaction();
		universeTest.setAngel(null);
		db.stopTransaction(Conclusion.SUCCESS);
		Universe universeTest2 = new Universe(universe1.getVertex());
		Assert.assertNull(universeTest2.getAngel());
		Assert.assertEquals(6, countEdges());
	}
	
	@Test
	public void testOneToOneOne() {
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
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(7, countEdges());
		
		db.startTransaction();
		oneOne1.setOneTwo(oneTwo2);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(6, countEdges());
		
		oneOne2 = new OneOne(oneOne2.getVertex());
		Assert.assertNull(oneOne2.getOneTwo());
		oneTwo1 = new OneTwo(oneTwo1.getVertex());
		Assert.assertNull(oneTwo1.getOneOne());
		oneOne1 = new OneOne(oneOne1.getVertex());
		Assert.assertNotNull(oneOne1.getOneTwo());
		
		db.startTransaction();
		oneOne2.setOneTwo(oneTwo1);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(7, countEdges());
		
	}

	@Test
	public void testOneToOneTwo() {
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
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(7, countEdges());
		
		db.startTransaction();
		oneOne1.setOneTwo(oneTwo2);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(6, countEdges());
		
		OneOne testOneOne2 = new OneOne(oneOne2.getVertex());
		Assert.assertNull(testOneOne2.getOneTwo());
		OneTwo testOneTwo1 = new OneTwo(oneTwo1.getVertex());
		Assert.assertNull(testOneTwo1.getOneOne());
		Assert.assertNotNull(oneOne1.getOneTwo());
		
		db.startTransaction();
		oneOne2.setOneTwo(oneTwo1);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(7, countEdges());
		
	}

	@Test
	public void testOneToOneSetNull() {
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
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(7, countEdges());
		
		db.startTransaction();
		oneOne1.setOneTwo(null);	
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(6, countEdges());
		Assert.assertNull(oneOne1.getOneTwo());
		Assert.assertNull(oneTwo1.getOneOne());
		
		db.startTransaction();
		oneOne1.setOneTwo(oneTwo2);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(6, countEdges());
		OneOne testOneOne2 = new OneOne(oneOne2.getVertex());
		Assert.assertNull(testOneOne2.getOneTwo());
		
	}	

}
