package org.tuml.tinker.collectiontest;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.collectiontest.World;
import org.tuml.concretetest.God;
import org.tuml.embeddedtest.TestEmbedded;
import org.tuml.runtime.collection.memory.TumlMemorySequence;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class OrderedSetTest extends BaseLocalDbTest {

	@Test
	public void testOrderedSetIsUnique() {
		God god = new God(true);
		god.setName("THEGOD");
		World world1 = new World(god);
		world1.setName("world1");
		god.getWorld().add(world1);
        db.commit();
		God godTest = new God(god.getVertex());
		Assert.assertEquals(1, godTest.getWorld().size());
	}
	
	@Test
	public void testOrderedSetIsOrdered() {
		God god = new God(true);
		god.setName("THEGOD");
		World world1 = new World(god);
		world1.setName("world1");
		World world2 = new World(god);
		world2.setName("world2");
		World world3 = new World(god);
		world3.setName("world3");
		World world4 = new World(god);
		world4.setName("world4");
        db.commit();
		God godTest = new God(god.getVertex());
		Assert.assertEquals(4, godTest.getWorld().size());
		Assert.assertEquals("world1", godTest.getWorld().get(0).getName());
		Assert.assertEquals("world2", godTest.getWorld().get(1).getName());
		Assert.assertEquals("world3", godTest.getWorld().get(2).getName());
		Assert.assertEquals("world4", godTest.getWorld().get(3).getName());
		God godTest2 = new God(god.getVertex());
		World world5 = new World(true);
		world5.setName("world5");
		godTest2.getWorld().add(2, world5);
        db.commit();
		God godTest3 = new God(god.getVertex());
		Assert.assertEquals(5, godTest3.getWorld().size());
		Assert.assertEquals("world1", godTest3.getWorld().get(0).getName());
		Assert.assertEquals("world2", godTest3.getWorld().get(1).getName());
		Assert.assertEquals("world5", godTest3.getWorld().get(2).getName());
		Assert.assertEquals("world3", godTest3.getWorld().get(3).getName());
		Assert.assertEquals("world4", godTest3.getWorld().get(4).getName());
	}
	
	@Test
	public void testChangeOrderOnEmbedded() {
		God g = new God(true);
		g.setName("GOD");
		TestEmbedded embedded = new TestEmbedded(g);
		embedded.setName("asd");
		embedded.addToManyOrderedRequiredInteger(9);
		embedded.addToManyOrderedRequiredInteger(8);
		embedded.addToManyOrderedRequiredInteger(7);
        db.commit();
		TestEmbedded test = new TestEmbedded(embedded.getVertex());
		
		//3 are created by initial value
		Assert.assertEquals(6, test.getManyOrderedRequiredInteger().size());
		
		test.setManyOrderedRequiredInteger(new TumlMemorySequence<Integer>(Arrays.asList(3,2,1)));
		Assert.assertEquals(3, test.getManyOrderedRequiredInteger().size());

		Assert.assertEquals(Integer.valueOf(3), test.getManyOrderedRequiredInteger().get(0));
		Assert.assertEquals(Integer.valueOf(2), test.getManyOrderedRequiredInteger().get(1));
		Assert.assertEquals(Integer.valueOf(1), test.getManyOrderedRequiredInteger().get(2));

	}
	
	
}