package org.umlg.tinker.componenttest;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.runtime.test.BaseLocalDbTest;

public class TestComponent extends BaseLocalDbTest {

	@SuppressWarnings("unused")
	@Test
	public void testComponent() {
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		SpaceTime st1 = new SpaceTime(universe1);
		Space s1 = new Space(st1);
		Time t1 = new Time(st1);

        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(5 + 5, countEdges());
	}
	
}
