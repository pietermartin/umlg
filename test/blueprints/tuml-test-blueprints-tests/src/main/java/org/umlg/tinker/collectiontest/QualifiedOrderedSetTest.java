package org.umlg.tinker.collectiontest;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.collectiontest.FWomen;
import org.umlg.collectiontest.Fantasy;
import org.umlg.concretetest.God;
import org.umlg.runtime.test.BaseLocalDbTest;

public class QualifiedOrderedSetTest extends BaseLocalDbTest {

	@Test
	public void testOrderedSetIsUnique() {
		God god = new God(true);
		god.setName("THEGOD");
		Fantasy fantasy1 = new Fantasy(true);
		fantasy1.setName("fantasy1");
		fantasy1.addToGod(god);
		god.getFantasy().add(fantasy1);

        FWomen fWomen1 = new FWomen(fantasy1);
        FWomen fWomen2 = new FWomen(fantasy1);

        db.commit();
		God godTest = new God(god.getVertex());
		Assert.assertEquals(1, godTest.getFantasy().size());
	}
	
	@Test
	public void testOrderedSetIsOrdered() {
		God god = new God(true);
		god.setName("THEGOD");
		Fantasy fantasy1 = new Fantasy(true);
		fantasy1.setName("fantasy1");
		god.getFantasy().add(fantasy1);
        FWomen fWomen1 = new FWomen(fantasy1);
        FWomen fWomen2 = new FWomen(fantasy1);

		Fantasy fantasy2 = new Fantasy(true);
		fantasy2.setName("fantasy2");
		god.getFantasy().add(fantasy2);
        FWomen fWomen1_2 = new FWomen(fantasy2);
        FWomen fWomen2_2 = new FWomen(fantasy2);

		Fantasy fantasy3 = new Fantasy(true);
		fantasy3.setName("fantasy3");
		god.getFantasy().add(fantasy3);
        FWomen fWomen1_3 = new FWomen(fantasy3);
        FWomen fWomen2_3 = new FWomen(fantasy3);

		Fantasy fantasy4 = new Fantasy(true);
		fantasy4.setName("fantasy4");
		god.getFantasy().add(fantasy4);
        FWomen fWomen1_4 = new FWomen(fantasy4);
        FWomen fWomen2_4 = new FWomen(fantasy4);


        db.commit();
		God godTest = new God(god.getVertex());
		Assert.assertEquals(4, godTest.getFantasy().size());
		Assert.assertEquals("fantasy1", godTest.getFantasy().get(0).getName());
		Assert.assertEquals("fantasy2", godTest.getFantasy().get(1).getName());
		Assert.assertEquals("fantasy3", godTest.getFantasy().get(2).getName());
		Assert.assertEquals("fantasy4", godTest.getFantasy().get(3).getName());
		God godTest2 = new God(god.getVertex());
		Fantasy fantasy5 = new Fantasy(true);
		fantasy5.setName("fantasy5");
		godTest2.getFantasy().add(2, fantasy5);
        FWomen fWomen1_5 = new FWomen(fantasy5);
        FWomen fWomen2_5 = new FWomen(fantasy5);

        db.commit();
		God godTest3 = new God(god.getVertex());
		Assert.assertEquals(5, godTest3.getFantasy().size());
		Assert.assertEquals("fantasy1", godTest3.getFantasy().get(0).getName());
		Assert.assertEquals("fantasy2", godTest3.getFantasy().get(1).getName());
		Assert.assertEquals("fantasy5", godTest3.getFantasy().get(2).getName());
		Assert.assertEquals("fantasy3", godTest3.getFantasy().get(3).getName());
		Assert.assertEquals("fantasy4", godTest3.getFantasy().get(4).getName());
	}

}