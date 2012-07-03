package org.nakeuml.tinker.collectiontest;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.inheritencetest.Biped;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestInitCalled extends BaseLocalDbTest {

	@Test
	public void testInitCalled() {
		db.startTransaction();
		God god = new God(true);
		god.setName("God1");
		Biped biped = new Biped(true);
		god.getAbstractSpecies().add(biped);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countEdges());
		Assert.assertEquals("thisisdodge", biped.getName());
	}
	
}
