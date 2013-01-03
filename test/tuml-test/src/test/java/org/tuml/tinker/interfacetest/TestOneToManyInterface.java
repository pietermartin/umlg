package org.tuml.tinker.interfacetest;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.interfacetest.Creature;
import org.tuml.interfacetest.Phantom;
import org.tuml.interfacetest.Spook;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

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
