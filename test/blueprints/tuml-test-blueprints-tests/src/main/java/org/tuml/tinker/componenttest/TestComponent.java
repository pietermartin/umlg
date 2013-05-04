package org.tuml.tinker.componenttest;

import junit.framework.Assert;
import org.junit.Test;
import org.tuml.componenttest.Space;
import org.tuml.componenttest.SpaceTime;
import org.tuml.componenttest.Time;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.runtime.test.BaseLocalDbTest;

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
