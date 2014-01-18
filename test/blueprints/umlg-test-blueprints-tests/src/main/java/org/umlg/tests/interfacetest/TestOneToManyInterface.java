package org.umlg.tests.interfacetest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.interfacetest.Creature;
import org.umlg.interfacetest.Phantom;
import org.umlg.interfacetest.Spook;
import org.umlg.runtime.test.BaseLocalDbTest;

public class TestOneToManyInterface extends BaseLocalDbTest {

	@Test
	public void testCompositeCreation() {
		God god = new God(true);
		god.setName("THEGOD");
		Spook spook = new Spook(god);
		spook.setName("spook1");
		Creature creature = new Creature(god);
		creature.setName("creature1");
        db.commit();
		Assert.assertEquals(3, countVertices());
		God g = new God(god.getVertex());
		Assert.assertEquals(1, g.getSpirit().size());
		Phantom phantom = new Phantom(god);
		phantom.setName("phanton1");
        db.commit();
		God g1 = new God(god.getVertex());
		Assert.assertEquals(2, g1.getSpirit().size());
		spook.setCreature(creature);
        db.commit();
		Assert.assertNotNull(spook.getCreature());
		Spook spook2 = new Spook(spook.getVertex());
		Assert.assertNotNull(spook2.getCreature());
	}
	
}
