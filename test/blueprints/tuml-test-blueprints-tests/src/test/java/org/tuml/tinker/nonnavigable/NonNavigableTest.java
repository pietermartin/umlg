package org.tuml.tinker.nonnavigable;

import junit.framework.Assert;
import org.junit.Test;
import org.tuml.componenttest.Space;
import org.tuml.componenttest.SpaceTime;
import org.tuml.componenttest.Time;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.navigability.NonNavigableMany;
import org.tuml.navigability.NonNavigableOne;
import org.tuml.runtime.test.BaseLocalDbTest;

public class NonNavigableTest extends BaseLocalDbTest {

	@SuppressWarnings("unused")
	@Test
	public void testNonNavigableOne() {
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		SpaceTime st1 = new SpaceTime(universe1);
		Space s1 = new Space(st1);
		Time t1 = new Time(st1);

		NonNavigableOne nonNavigableOne = new NonNavigableOne(god);
		nonNavigableOne.setName("nonNovigableOne");
		universe1.setNonNavigableOne(nonNavigableOne);
        db.commit();
		Assert.assertEquals(6, countVertices());
		Assert.assertEquals(7, countEdges());
		Universe testUniverse = new Universe(universe1.getVertex());
		Assert.assertNotNull(testUniverse.getNonNavigableOne());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testRemoveNonNavigableOne() {
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		SpaceTime st1 = new SpaceTime(universe1);
		Space s1 = new Space(st1);
		Time t1 = new Time(st1);

		NonNavigableOne nonNavigableOne = new NonNavigableOne(god);
		nonNavigableOne.setName("nonNovigableOne");
		universe1.setNonNavigableOne(nonNavigableOne);
        db.commit();
		Assert.assertEquals(6, countVertices());
		Assert.assertEquals(7, countEdges());
		Universe testUniverse = new Universe(universe1.getVertex());
		Assert.assertNotNull(testUniverse.getNonNavigableOne());
		
		universe1.setNonNavigableOne(null);
        db.commit();
		Assert.assertEquals(6, countVertices());
		Assert.assertEquals(6, countEdges());
		testUniverse = new Universe(universe1.getVertex());
		Assert.assertNull(testUniverse.getNonNavigableOne());
	}

	@SuppressWarnings("unused")
	@Test
	public void testNonNavigableMany() {
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		SpaceTime st1 = new SpaceTime(universe1);
		Space s1 = new Space(st1);
		Time t1 = new Time(st1);

		NonNavigableMany nonNavigableMany = new NonNavigableMany(god);
		nonNavigableMany.setName("nonNavigableMany");
		universe1.addToNonNavigableMany(nonNavigableMany);
		NonNavigableMany nonNavigableMany2 = new NonNavigableMany(god);
		nonNavigableMany2.setName("nonNavigableMany2");
		universe1.addToNonNavigableMany(nonNavigableMany2);
        db.commit();
		Assert.assertEquals(7, countVertices());
		Assert.assertEquals(9, countEdges());
		Universe testUniverse = new Universe(universe1.getVertex());
		Assert.assertEquals(2, testUniverse.getNonNavigableMany().size());
	}

}
