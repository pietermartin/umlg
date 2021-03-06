package org.umlg.tests.nonnavigable;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.navigability.NonNavigableMany;
import org.umlg.navigability.NonNavigableOne;
import org.umlg.runtime.test.BaseLocalDbTest;

public class NonNavigableTest extends BaseLocalDbTest {


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
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(6, countEdges());
        Universe testUniverse = new Universe(universe1.getVertex());
        Assert.assertNotNull(testUniverse.getNonNavigableOne());

        universe1.setNonNavigableOne(null);
        testUniverse = new Universe(universe1.getVertex());
        Assert.assertNull(testUniverse.getNonNavigableOne());
        db.rollback();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(6, countEdges());
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
		Assert.assertEquals(6, countVertices());
		Assert.assertEquals(8, countEdges());
		Universe testUniverse = new Universe(universe1.getVertex());
		Assert.assertEquals(2, testUniverse.getNonNavigableMany().size());
	}

}
