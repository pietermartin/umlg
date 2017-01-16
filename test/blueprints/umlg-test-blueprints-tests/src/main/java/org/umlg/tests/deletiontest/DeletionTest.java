package org.umlg.tests.deletiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.Dream;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.interfacetest.ManyA;
import org.umlg.interfacetest.ManyB;
import org.umlg.onetoone.OneOne;
import org.umlg.onetoone.OneTwo;
import org.umlg.runtime.adaptor.TransactionThreadEntityVar;
import org.umlg.runtime.test.BaseLocalDbTest;

public class DeletionTest extends BaseLocalDbTest {

	@SuppressWarnings("unused")
	@Test
	public void testDeletion() {
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		SpaceTime st1 = new SpaceTime(universe1);
		Space s1 = new Space(st1);
		Time t1 = new Time(st1);

        db.commit();
		Assert.assertEquals(4, countVertices());
		Assert.assertEquals(4, countEdges());
		God godTest = new God(god.getVertex());
		Universe testDeletion = godTest.getUniverse().iterator().next();
		testDeletion.delete();
        db.commit();
		Assert.assertEquals(0, countVertices());
		Assert.assertEquals(0, countEdges());
	}

	@SuppressWarnings("unused")
	@Test
	public void testDeletionManyToMany() {
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		SpaceTime st1 = new SpaceTime(universe1);
		Space s1 = new Space(st1);
		Time t1 = new Time(st1);


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

        db.commit();
		Assert.assertEquals(12, countVertices());
		Assert.assertEquals(28, countEdges());
		ManyB manyBTest = new ManyB(manyB1.getVertex());
		Assert.assertEquals(4, manyBTest.getIManyA().size());
		ManyA testDeletion = new ManyA(manyA1.getVertex());
		testDeletion.delete();
        db.commit();
		Assert.assertEquals(11, countVertices());
		Assert.assertEquals(23, countEdges());
		manyBTest = new ManyB(manyB1.getVertex());
		Assert.assertEquals(3, manyBTest.getIManyA().size());

		testDeletion = new ManyA(manyA2.getVertex());
		testDeletion.delete();
        db.commit();
		Assert.assertEquals(10, countVertices());
		Assert.assertEquals(18, countEdges());
		manyBTest = new ManyB(manyB1.getVertex());
		Assert.assertEquals(2, manyBTest.getIManyA().size());

		testDeletion = new ManyA(manyA3.getVertex());
		testDeletion.delete();
        db.commit();
		Assert.assertEquals(9, countVertices());
		Assert.assertEquals(13, countEdges());
		manyBTest = new ManyB(manyB1.getVertex());
		Assert.assertEquals(1, manyBTest.getIManyA().size());

		testDeletion = new ManyA(manyA4.getVertex());
		testDeletion.delete();
        db.commit();
		Assert.assertEquals(8, countVertices());
		Assert.assertEquals(8, countEdges());
		God testGod = new God(god.getVertex());
		Assert.assertEquals(4, testGod.getIMany().size());

		testGod.getIMany().clear();
		ManyB manyB = new ManyB(manyB1.getVertex());
		manyB.delete();
		manyB = new ManyB(manyB2.getVertex());
		manyB.delete();
		manyB = new ManyB(manyB3.getVertex());
		manyB.delete();
		manyB = new ManyB(manyB4.getVertex());
		manyB.delete();

        db.commit();
		Assert.assertEquals(4, countVertices());
		Assert.assertEquals(4, countEdges());

	}

	@Test
	public void deleteOneToOneInverse() {
		God god = new God(true);
		god.setName("GODDER");
		OneOne oneOne1 = new OneOne(god);
		oneOne1.setName("oneone1");
		OneTwo oneTwo1 = new OneTwo(god);
		oneTwo1.setName("onetwo1");
		oneOne1.setOneTwo(oneTwo1);
        db.commit();
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(3, countEdges());
		oneOne1.delete();
        db.commit();
		Assert.assertEquals(1, countVertices());
		Assert.assertEquals(1, countEdges());
		Assert.assertNull(oneTwo1.getOneOne());
	}

	@Test
	public void deleteOneToOneNonInverse() {
		God god = new God(true);
		god.setName("GODDER");
		OneOne oneOne1 = new OneOne(god);
		oneOne1.setName("oneone1");
		OneTwo oneTwo1 = new OneTwo(god);
		oneTwo1.setName("onetwo1");
		oneOne1.setOneTwo(oneTwo1);
        db.commit();
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(3, countEdges());
		oneTwo1.delete();
        db.commit();
		Assert.assertEquals(1, countVertices());
		Assert.assertEquals(1, countEdges());
		OneOne testOneOne1 = new OneOne(oneOne1.getVertex());
		Assert.assertNull(testOneOne1.getOneTwo());
	}

    @Test
    public void testTransactionThreadVarRemove() {
        God god = new God(true);
        god.setName("GODDER");
        Dream dream = new Dream(god);
        dream.setName("dream1");
        Assert.assertEquals(2, TransactionThreadEntityVar.get().size());
        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1, countEdges());
        Assert.assertEquals(0, TransactionThreadEntityVar.get().size());

        god.removeFromDream(dream);

        Assert.assertTrue(TransactionThreadEntityVar.get().contains(dream));

        Assert.assertEquals(2, TransactionThreadEntityVar.get().size());
        dream.delete();
        Assert.assertFalse(TransactionThreadEntityVar.get().contains(dream));
        db.commit();
        Assert.assertEquals(0, countVertices());
        Assert.assertEquals(0, countEdges());
        Assert.assertEquals(0, TransactionThreadEntityVar.get().size());
    }

}
