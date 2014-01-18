package org.umlg.tests.collectiontest;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.collectiontest.Nightmare;
import org.umlg.concretetest.God;
import org.umlg.runtime.test.BaseLocalDbTest;

public class QualifiedBagTest extends BaseLocalDbTest {

	@Test(expected=IllegalStateException.class)
	public void testQualifierEnforcesUniqueness() {
		God god = new God(true);
		god.setName("THEGOD");
		Nightmare nightmare1 = new Nightmare(true);
		nightmare1.setName("nightmare1");
		nightmare1.addToGod(god);
		god.getNightmare().add(nightmare1);
        db.commit();
		God godTest = new God(god.getVertex());
		Assert.assertEquals(2, godTest.getNightmare().size());
	}

	
	@Test(expected=IllegalStateException.class)
	public void testQualifierEnforcesUniqueness2() {
		God god = new God(true);
		god.setName("THEGOD");
		Nightmare nightmare1 = new Nightmare(true);
		nightmare1.setName("nightmare1");
		nightmare1.addToGod(god);
        db.commit();
		God godTest1 = new God(god.getVertex());
		Assert.assertEquals(1, godTest1.getNightmare().size());

		God refreshedGod = new God(god.getVertex());
		refreshedGod.getNightmare().add(nightmare1);
        db.commit();

		God godTest = new God(god.getVertex());
		Assert.assertEquals(2, godTest.getNightmare().size());
	}

}
