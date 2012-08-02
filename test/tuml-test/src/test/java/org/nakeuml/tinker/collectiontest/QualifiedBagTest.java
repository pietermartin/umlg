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

	
	@Test
	public void testBag() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Nightmare memory1 = new Nightmare(true);
		memory1.setNameNonUnique("edno1");
		memory1.setName("memory1");
		memory1.addToGod(god);
		god.addToMemory(memory1);

		Nightmare memory2 = new Nightmare(true);
		memory2.setName("memory2");
		memory2.setNameNonUnique("edno2");
		memory2.addToGod(god);
		god.addToMemory(memory2);

		Nightmare memory3 = new Nightmare(true);
		memory3.setName("memory3");
		memory3.setNameNonUnique("edno3");
		memory3.addToGod(god);
		god.addToMemory(memory3);
		
		god.addToMemory(memory3);
		god.addToMemory(memory3);

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(4, countVertices());
		//This is failling, need to think about one to many with a bag on the many side
		Assert.assertEquals(9, countEdges());
		God godTest = new God(god.getVertex());
		
		Assert.assertEquals(3, godTest.getMemoryForMemoryQualifier1("edno3").size());

		db.startTransaction();
		Nightmare memory4 = new Nightmare(true);
		memory4.setName("memory4");
		memory4.setNameNonUnique("edno3");
		memory4.addToGod(god);
		god.addToMemory(memory4);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(11, countEdges());
		Assert.assertEquals(4, godTest.getMemoryForMemoryQualifier1("edno3").size());
		
	}	
}
