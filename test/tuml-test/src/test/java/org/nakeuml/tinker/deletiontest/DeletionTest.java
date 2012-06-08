package org.nakeuml.tinker.deletiontest;

import org.junit.Assert;
import org.junit.Test;
import org.tinker.concretetest.God;
import org.tinker.concretetest.Universe;
import org.tinker.interfacetest.ManyA;
import org.tinker.interfacetest.ManyB;
import org.tinker.onetoone.OneOne;
import org.tinker.onetoone.OneTwo;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class DeletionTest extends BaseLocalDbTest {

	@Test
	public void testDeletion() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(5, countEdges());
		db.startTransaction();
		God godTest = new God(god.getVertex());
		Universe testDeletion = godTest.getUniverse().iterator().next();
		testDeletion.delete();
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(1, countVertices());
		Assert.assertEquals(1, countEdges());
	}

	@Test
	public void testDeletionManyToMany() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		
		ManyA manyA1 = new ManyA(god);
		manyA1.setName("many1");
		ManyA manyA2 = new ManyA(god);
		manyA2.setName("manyA2");
		ManyA manyA3 = new ManyA(god);
		manyA3.setName("manyA3");
		ManyA manyA4 = new ManyA(god);
		manyA4.setName("manyA4");

		ManyB manyB1 = new ManyB(god);
		manyB1.setName("manyB1");
		ManyB manyB2 = new ManyB(god);
		manyB2.setName("manyB2");
		ManyB manyB3 = new ManyB(god);
		manyB3.setName("manyB3");
		ManyB manyB4 = new ManyB(god);
		manyB4.setName("manyB4");

		manyA1.addToIManyB(manyB1);
		manyA1.addToIManyB(manyB2);
		manyA1.addToIManyB(manyB3);
		manyA1.addToIManyB(manyB4);

		manyA2.addToIManyB(manyB1);
		manyA2.addToIManyB(manyB2);
		manyA2.addToIManyB(manyB3);
		manyA2.addToIManyB(manyB4);

		manyA3.addToIManyB(manyB1);
		manyA3.addToIManyB(manyB2);
		manyA3.addToIManyB(manyB3);
		manyA3.addToIManyB(manyB4);

		manyA4.addToIManyB(manyB1);
		manyA4.addToIManyB(manyB2);
		manyA4.addToIManyB(manyB3);
		manyA4.addToIManyB(manyB4);

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(13, countVertices());
		Assert.assertEquals(29, countEdges());
		ManyB manyBTest = new ManyB(manyB1.getVertex());
		Assert.assertEquals(4, manyBTest.getIManyA().size());
		db.startTransaction();
		ManyA testDeletion = new ManyA(manyA1.getVertex());
		testDeletion.delete();
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(12, countVertices());
		Assert.assertEquals(24, countEdges());
		manyBTest = new ManyB(manyB1.getVertex());
		Assert.assertEquals(3, manyBTest.getIManyA().size());

		db.startTransaction();
		testDeletion = new ManyA(manyA2.getVertex());
		testDeletion.delete();
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(11, countVertices());
		Assert.assertEquals(19, countEdges());
		manyBTest = new ManyB(manyB1.getVertex());
		Assert.assertEquals(2, manyBTest.getIManyA().size());

		db.startTransaction();
		testDeletion = new ManyA(manyA3.getVertex());
		testDeletion.delete();
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(10, countVertices());
		Assert.assertEquals(14, countEdges());
		manyBTest = new ManyB(manyB1.getVertex());
		Assert.assertEquals(1, manyBTest.getIManyA().size());

		db.startTransaction();
		testDeletion = new ManyA(manyA4.getVertex());
		testDeletion.delete();
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(9, countVertices());
		Assert.assertEquals(9, countEdges());
		God testGod = new God(god.getVertex());
		Assert.assertEquals(4, testGod.getIMany().size());

		db.startTransaction();
		testGod.getIMany().clear();
		ManyB manyB = new ManyB(manyB1.getVertex());
		manyB.delete();
		manyB = new ManyB(manyB2.getVertex());
		manyB.delete();
		manyB = new ManyB(manyB3.getVertex());
		manyB.delete();
		manyB = new ManyB(manyB4.getVertex());
		manyB.delete();
		
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(5, countEdges());

	}
	
	@Test
	public void deleteOneToOneInverse() {
		db.startTransaction();
		God god = new God(true);
		god.setName("GODDER");
		OneOne oneOne1 = new OneOne(god);
		oneOne1.setName("oneone1");
		OneTwo oneTwo1 = new OneTwo(god);
		oneTwo1.setName("onetwo1");
		oneOne1.setOneTwo(oneTwo1);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(4, countEdges());
		db.startTransaction();
		oneOne1.delete();
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
		Assert.assertNull(oneTwo1.getOneOne());
	}

	@Test
	public void deleteOneToOneNonInverse() {
		db.startTransaction();
		God god = new God(true);
		god.setName("GODDER");
		OneOne oneOne1 = new OneOne(god);
		oneOne1.setName("oneone1");
		OneTwo oneTwo1 = new OneTwo(god);
		oneTwo1.setName("onetwo1");
		oneOne1.setOneTwo(oneTwo1);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(4, countEdges());
		db.startTransaction();
		oneTwo1.delete();
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
		OneOne testOneOne1 = new OneOne(oneOne1.getVertex());
		Assert.assertNull(testOneOne1.getOneTwo());
	}

}
