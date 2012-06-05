package org.nakeuml.tinker.interfacetest;

import org.junit.Assert;
import org.junit.Test;
import org.tinker.concretetest.God;
import org.tinker.interfacetest.Creature;
import org.tinker.interfacetest.Phantom;
import org.tinker.interfacetest.Spook;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class TestOneToManyInterface extends BaseLocalDbTest {

	@Test
	public void testCompositeCreation() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Spook spook = new Spook(god);
		spook.setName("spook1");
		Creature creature = new Creature(god);
		creature.setName("creature1");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, countVertices());
		God g = new God(god.getVertex());
		Assert.assertEquals(1, g.getSpirit().size());
		db.startTransaction();
		Phantom phantom = new Phantom(god);
		phantom.setName("phanton1");
		db.stopTransaction(Conclusion.SUCCESS);
		God g1 = new God(god.getVertex());
		Assert.assertEquals(2, g1.getSpirit().size());
		db.startTransaction();
		spook.setCreature(creature);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertNotNull(spook.getCreature());
		Spook spook2 = new Spook(spook.getVertex());
		Assert.assertNotNull(spook2.getCreature());
	}
	
}
