package org.umlg.tinker.collectiontest;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.inheritencetest.AbstractSpecies;
import org.umlg.inheritencetest.Biped;
import org.umlg.runtime.collection.ocl.IterateExpressionAccumulator;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.ArrayList;
import java.util.List;

public class TestInitCalled extends BaseLocalDbTest {

	@Test
	public void testInitCalled() {
		God god = new God(true);
		god.setName("God1");
		Biped biped = new Biped(true);
		biped.setName("thisisdodge");
		god.getAbstractSpecies().add(biped);
        db.commit();
		Assert.assertEquals(2 + 2, countEdges());
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
