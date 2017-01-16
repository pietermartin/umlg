package org.umlg.tests.collectiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.Bag;
import org.umlg.collectiontest.Nightmare;
import org.umlg.concretetest.God;
import org.umlg.runtime.test.BaseLocalDbTest;

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
		
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
		
		nightmare.addToBag(bag1);

        db.commit();
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(3, countEdges());
		Nightmare nTest = new Nightmare(nightmare.getVertex());
		Assert.assertEquals(2, nTest.getBag().size());
		Bag b1 = nTest.getBag().asSequence().at(0);
		Bag b2 = nTest.getBag().asSequence().at(1);
		Assert.assertSame(b1, b2);
		
		Assert.assertSame(b1.getNightmare(), b2.getNightmare());
	}
	
}
