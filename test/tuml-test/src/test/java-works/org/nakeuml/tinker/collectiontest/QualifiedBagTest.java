package org.nakeuml.tinker.collectiontest;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.collectiontest.Nightmare;
import org.tuml.concretetest.God;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class QualifiedBagTest extends BaseLocalDbTest {

	@Test(expected=IllegalStateException.class)
	public void testQualifierEnforcesUniqueness() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Nightmare nightmare1 = new Nightmare(true);
		nightmare1.setName("nightmare1");
		nightmare1.addToGod(god);
		god.getNightmare().add(nightmare1);
		db.stopTransaction(Conclusion.SUCCESS);
		God godTest = new God(god.getVertex());
		Assert.assertEquals(2, godTest.getNightmare().size());
	}

	
	@Test(expected=IllegalStateException.class)
	public void testQualifierEnforcesUniqueness2() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Nightmare nightmare1 = new Nightmare(true);
		nightmare1.setName("nightmare1");
		nightmare1.addToGod(god);
		db.stopTransaction(Conclusion.SUCCESS);
		God godTest1 = new God(god.getVertex());
		Assert.assertEquals(1, godTest1.getNightmare().size());

		db.startTransaction();
		God refreshedGod = new God(god.getVertex());
		refreshedGod.getNightmare().add(nightmare1);
		db.stopTransaction(Conclusion.SUCCESS);

		God godTest = new God(god.getVertex());
		Assert.assertEquals(2, godTest.getNightmare().size());
	}

}
