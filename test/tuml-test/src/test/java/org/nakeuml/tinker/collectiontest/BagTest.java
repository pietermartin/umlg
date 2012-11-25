package org.nakeuml.tinker.collectiontest;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.collectiontest.Bag;
import org.tuml.collectiontest.Nightmare;
import org.tuml.concretetest.God;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class BagTest extends BaseLocalDbTest {

	@Test
	public void testBag() {
		God g = new God(true);
		g.setName("GOD");
		Nightmare nightmare = new Nightmare(g);
		nightmare.setName("nightmare");
		Bag bag1 = new Bag(nightmare);
		bag1.setName("bag1");
		db.stopTransaction(Conclusion.SUCCESS);
		
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(3, countEdges());
		
		nightmare.addToBag(bag1);
		
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(4, countEdges());
		Nightmare nTest = new Nightmare(nightmare.getVertex());
		Assert.assertEquals(2, nTest.getBag().size());
		Bag b1 = nTest.getBag().asSequence().at(0);
		Bag b2 = nTest.getBag().asSequence().at(1);
		Assert.assertSame(b1, b2);
		
		Assert.assertSame(b1.getNightmare(), b2.getNightmare());
	}
	
}
