package org.nakeuml.tinker.collectiontest;

import junit.framework.Assert;

import org.junit.Test;
import org.tinker.collectiontest.Nightmare;
import org.tinker.concretetest.God;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class QualifiedBagTest extends BaseLocalDbTest {

	@Test(expected=IllegalStateException.class)
	public void testQualifierEnforcesUniqueness() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Nightmare nightmare1 = new Nightmare(true);
		nightmare1.setName("nightmare1");
		nightmare1.init(god);
		nightmare1.addToOwningObject();
		god.getNightmare().add(nightmare1, god.getQualifierForNightmare(nightmare1));
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
		memory1.init(god);
		memory1.addToOwningObject();
		god.addToMemory(memory1);

		Nightmare memory2 = new Nightmare(true);
		memory2.setName("memory2");
		memory2.setNameNonUnique("edno2");
		memory2.init(god);
		memory2.addToOwningObject();
		god.addToMemory(memory2);

		Nightmare memory3 = new Nightmare(true);
		memory3.setName("memory3");
		memory3.setNameNonUnique("edno3");
		memory3.init(god);
		memory3.addToOwningObject();
		god.addToMemory(memory3);
		
		god.addToMemory(memory3);
		god.addToMemory(memory3);

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(4, countVertices());
		Assert.assertEquals(9, countEdges());
		God godTest = new God(god.getVertex());
		//Qualified relationships return a Set
		Assert.assertEquals(1, godTest.getMemoryForQualifier1("edno3").size());

		db.startTransaction();
		Nightmare memory4 = new Nightmare(true);
		memory4.setName("memory4");
		memory4.setNameNonUnique("edno3");
		memory4.init(god);
		memory4.addToOwningObject();
		god.addToMemory(memory4);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(11, countEdges());
		Assert.assertEquals(2, godTest.getMemoryForQualifier1("edno3").size());
		
	}	
}
