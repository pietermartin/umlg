package org.nakeuml.tinker.concretetest;

import org.junit.Assert;

import org.junit.Test;
import org.umlg.componenttest.SpaceTime;
import org.umlg.concretetest.Demon;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestNonCompositeOneToManyAudit extends BaseLocalDbTest {

	@Test
	public void testNonCompositeOneToMany() {
		db.startTransaction();
		God god = new God(true);
		god.setName("GODDER");
		Demon demon1 = new Demon(god);
		demon1.setName("demon1");
		Demon demon2 = new Demon(god);
		demon2.setName("demon2");
		Demon demon3 = new Demon(god);
		demon3.setName("demon3");
		Universe universe = new Universe(god);
		universe.setName("universe1");
		Universe universe2 = new Universe(god);
		universe2.setName("universe2");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(24, countVertices());
		Assert.assertEquals(35, countEdges());

		db.startTransaction();
		SpaceTime st = universe.getSpaceTime();
		universe.setSpaceTime(universe2.getSpaceTime());
		universe2.setSpaceTime(st);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(28, countVertices());
		Assert.assertEquals(43, countEdges());
	}
	
}
