package org.umlg.tests.concretetest;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.concretetest.Angel;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.onetoone.OneOne;
import org.umlg.onetoone.OneTwo;
import org.umlg.runtime.test.BaseLocalDbTest;

public class TestNonCompositeOneToOne extends BaseLocalDbTest {

	@SuppressWarnings("unused")
	@Test
	public void testNonCompositeOneToOneCreation() {
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		SpaceTime st1 = new SpaceTime(universe1);
		Space s1 = new Space(st1);
		Time t1 = new Time(st1);

		Angel angel = new Angel(god);
		angel.setName("angel1");
		universe1.setAngel(angel);
        db.commit();
		Universe universeTest = new Universe(universe1.getVertex());
		Assert.assertNotNull(universeTest.getAngel());
		Angel angelTest = new Angel(angel.getVertex());
		Assert.assertNotNull(angelTest.getUniverse());
		Assert.assertEquals(7 + 6, countEdges());
	}

	@SuppressWarnings("unused")
	@Test
	public void testNonCompositeOneToOneCreationOtherWayAround() {
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		SpaceTime st1 = new SpaceTime(universe1);
		Space s1 = new Space(st1);
		Time t1 = new Time(st1);

		Angel angel = new Angel(god);
		angel.setName("angel1");
		angel.setUniverse(universe1);
        db.commit();
		Universe universeTest = new Universe(universe1.getVertex());
		Assert.assertNotNull(universeTest.getAngel());
		Angel angelTest = new Angel(angel.getVertex());
		Assert.assertNotNull(angelTest.getUniverse());
		Assert.assertEquals(7 + 6, countEdges());
	}

	@SuppressWarnings("unused")
	@Test
	public void testNonCompositeOneToOneRemoval() {
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		SpaceTime st1 = new SpaceTime(universe1);
		Space s1 = new Space(st1);
		Time t1 = new Time(st1);

		Angel angel = new Angel(god);
		angel.setName("angel1");
		universe1.setAngel(angel);
        db.commit();
		Universe universeTest = new Universe(universe1.getVertex());
		Assert.assertNotNull(universeTest.getAngel());
		Assert.assertEquals(7 + 6, countEdges());
		universeTest.setAngel(null);
        db.commit();
		Universe universeTest2 = new Universe(universe1.getVertex());
		Assert.assertNull(universeTest2.getAngel());
		Assert.assertEquals(6 + 6, countEdges());
	}

	@Test
	public void testOneToOneOne() {
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

        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(7 + 5, countEdges());

		oneOne1.setOneTwo(oneTwo2);
        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(6 + 5, countEdges());

		oneOne2 = new OneOne(oneOne2.getVertex());
		Assert.assertNull(oneOne2.getOneTwo());
		oneTwo1 = new OneTwo(oneTwo1.getVertex());
		Assert.assertNull(oneTwo1.getOneOne());
		oneOne1 = new OneOne(oneOne1.getVertex());
		Assert.assertNotNull(oneOne1.getOneTwo());

		oneOne2.setOneTwo(oneTwo1);
        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(7 + 5, countEdges());

	}

	@Test
	public void testOneToOneTwo() {
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

        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(7 + 5, countEdges());

		oneOne1.setOneTwo(oneTwo2);
        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(6 + 5, countEdges());

		OneOne testOneOne2 = new OneOne(oneOne2.getVertex());
		Assert.assertNull(testOneOne2.getOneTwo());
		OneTwo testOneTwo1 = new OneTwo(oneTwo1.getVertex());
		Assert.assertNull(testOneTwo1.getOneOne());
		Assert.assertNotNull(oneOne1.getOneTwo());

		oneOne2.setOneTwo(oneTwo1);
        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(7 + 5, countEdges());

	}

	@Test
	public void testOneToOneSetNull() {
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

        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(7 + 5, countEdges());

		oneOne1.setOneTwo(null);
        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(6 + 5, countEdges());
		Assert.assertNull(oneOne1.getOneTwo());
		Assert.assertNull(oneTwo1.getOneOne());

		oneOne1.setOneTwo(oneTwo2);
        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(6 + 5, countEdges());
		OneOne testOneOne2 = new OneOne(oneOne2.getVertex());
		Assert.assertNull(testOneOne2.getOneTwo());

	}

}
