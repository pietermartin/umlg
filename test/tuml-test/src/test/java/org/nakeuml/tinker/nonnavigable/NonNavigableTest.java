package org.nakeuml.tinker.nonnavigable;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.navigability.NonNavigableMany;
import org.tuml.navigability.NonNavigableOne;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class NonNavigableTest extends BaseLocalDbTest {

	@Test
	public void testNonNavigableOne() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		NonNavigableOne nonNavigableOne = new NonNavigableOne(god);
		nonNavigableOne.setName("nonNovigableOne");
		universe1.setNonNavigableOne(nonNavigableOne);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(6, countVertices());
		Assert.assertEquals(7, countEdges());
		Universe testUniverse = new Universe(universe1.getVertex());
		Assert.assertNotNull(testUniverse.getNonNavigableOne());
	}
	
	@Test
	public void testRemoveNonNavigableOne() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		NonNavigableOne nonNavigableOne = new NonNavigableOne(god);
		nonNavigableOne.setName("nonNovigableOne");
		universe1.setNonNavigableOne(nonNavigableOne);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(6, countVertices());
		Assert.assertEquals(7, countEdges());
		Universe testUniverse = new Universe(universe1.getVertex());
		Assert.assertNotNull(testUniverse.getNonNavigableOne());
		
		db.startTransaction();
		universe1.setNonNavigableOne(null);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(6, countVertices());
		Assert.assertEquals(6, countEdges());
		testUniverse = new Universe(universe1.getVertex());
		Assert.assertNull(testUniverse.getNonNavigableOne());
	}

	@Test
	public void testNonNavigableMany() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		NonNavigableMany nonNavigableMany = new NonNavigableMany(god);
		nonNavigableMany.setName("nonNavigableMany");
		universe1.addToNonNavigableMany(nonNavigableMany);
		NonNavigableMany nonNavigableMany2 = new NonNavigableMany(god);
		nonNavigableMany2.setName("nonNavigableMany2");
		universe1.addToNonNavigableMany(nonNavigableMany2);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(7, countVertices());
		Assert.assertEquals(9, countEdges());
		Universe testUniverse = new Universe(universe1.getVertex());
		Assert.assertEquals(2, testUniverse.getNonNavigableMany().size());
	}

}
