package org.tuml.tinker.collectiontest;

import junit.framework.Assert;
import org.junit.Test;
import org.tuml.collectiontest.Bag;
import org.tuml.collectiontest.Nightmare;
import org.tuml.concretetest.God;
import org.tuml.runtime.test.BaseLocalDbTest;

public class BagTestTest extends BaseLocalDbTest {

	@Test
	public void testBag() {
		God g = new God(true);
		g.setName("GOD");
		Nightmare nightmare = new Nightmare(g);
		nightmare.setName("nightmare");
		Bag bag1 = new Bag(nightmare);
		bag1.setName("bag1");
        db.commit();
		
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(6, countEdges());
		
		nightmare.addToBag(bag1);

        db.commit();
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(1 + 6, countEdges());
		Nightmare nTest = new Nightmare(nightmare.getVertex());
		Assert.assertEquals(2, nTest.getBag().size());
		Bag b1 = nTest.getBag().asSequence().at(0);
		Bag b2 = nTest.getBag().asSequence().at(1);
		Assert.assertSame(b1, b2);
		
		Assert.assertSame(b1.getNightmare(), b2.getNightmare());
	}
	
}
