package org.nakeuml.tinker.collectiontest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.inheritencetest.AbstractSpecies;
import org.tuml.inheritencetest.Biped;
import org.tuml.runtime.collection.ocl.IterateExpressionAccumulator;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestInitCalled extends BaseLocalDbTest {

	@Test
	public void testInitCalled() {
		db.startTransaction();
		God god = new God(true);
		god.setName("God1");
		Biped biped = new Biped(true);
		biped.setName("thisisdodge");
		god.getAbstractSpecies().add(biped);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countEdges());
		God g = new God(god.getVertex());
		Assert.assertEquals("thisisdodge",g.getAbstractSpecies().iterate(new IterateExpressionAccumulator<List<Biped>, AbstractSpecies>() {
			@Override
			public List<Biped> accumulate(List<Biped> acc, AbstractSpecies e) {
				acc.add((Biped) e);
				return acc;
			}

			@Override
			public List<Biped> initAccumulator() {
				return new ArrayList<Biped>();
			}
		}).get(0).getName());
	}
	
}
