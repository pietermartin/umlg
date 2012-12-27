package org.tuml.tinker.lookup;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.collectiontest.Nightmare;
import org.tuml.concretetest.God;
import org.tuml.interfacetest.Being;
import org.tuml.interfacetest.Creature;
import org.tuml.interfacetest.ManyA;
import org.tuml.interfacetest.ManyB;
import org.tuml.interfacetest.Spook;
import org.tuml.lookup.Devil1;
import org.tuml.lookup.Devil2;
import org.tuml.lookup.Level1;
import org.tuml.lookup.Level2;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestOneLookup extends BaseLocalDbTest {

	@Test
	public void testCompositeParent() {
		db.startTransaction();
		God g = new God(true);
		g.setName("G");
		Creature c1 = new Creature(g);
		c1.setName("c1");
		Creature c2 = new Creature(g);
		c2.setName("c2");
		Spook s1 = new Spook(g);
		s1.setName("s1");
		Spook s2 = new Spook(g);
		s2.setName("s2");
		c1.addToSpook(s1);
		c2.addToSpook(s2);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(g, new Creature(c1.getVertex()).lookupFor_creature_spook_CompositeParent());
	}
	
	@Test
	public void testCompositeParentOfManyToManies() {
		db.startTransaction();
		God g = new God(true);
		ManyA manyA1 = new ManyA(g);
		ManyA manyA2 = new ManyA(g);
		ManyA manyA3 = new ManyA(g);
		ManyA manyA4 = new ManyA(g);
		
		ManyB manyB1 = new ManyB(g);
		ManyB manyB2 = new ManyB(g);
		ManyB manyB3 = new ManyB(g);
		ManyB manyB4 = new ManyB(g);
		manyA1.addToIManyB(manyB1);
		manyA1.addToIManyB(manyB2);
		manyA1.addToIManyB(manyB3);
		manyA1.addToIManyB(manyB4);
		manyA2.addToIManyB(manyB1);
		manyA2.addToIManyB(manyB2);
		manyA2.addToIManyB(manyB3);
		manyA2.addToIManyB(manyB4);
		manyA3.addToIManyB(manyB1);
		manyA3.addToIManyB(manyB2);
		manyA3.addToIManyB(manyB3);
		manyA3.addToIManyB(manyB4);
		manyA4.addToIManyB(manyB1);
		manyA4.addToIManyB(manyB2);
		manyA4.addToIManyB(manyB3);
		manyA4.addToIManyB(manyB4);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(g, new ManyA(manyA4.getVertex()).lookupFor_iManyA_iManyB_CompositeParent());
		Assert.assertEquals(g, new ManyB(manyB4.getVertex()).lookupFor_iManyB_iManyA_CompositeParent());
	}
	
	@Test
	public void testLookupStrategy() {
		db.startTransaction();
		God g = new God(true);
		g.setName("G");
		Creature c1 = new Creature(g);
		c1.setName("c1");
		Creature c2 = new Creature(g);
		c2.setName("c2");
		db.stopTransaction(Conclusion.SUCCESS);

		TinkerSet<Creature> result = g.getBeing().<Creature, Creature> collect(new BodyExpressionEvaluator<Creature, Being>() {
			@Override
			public Creature evaluate(Being e) {
				if (e instanceof Creature) {
					return (Creature) e;
				}
				return null;
			}
		}).asSet();
		Assert.assertEquals(2, result.size());

		TinkerSet<Being> creatures = g.getBeing().select(new BooleanExpressionEvaluator<Being>() {
			@Override
			public Boolean evaluate(Being e) {
				return e instanceof Creature;
			}
		});
		Assert.assertEquals(2, creatures.size());
	}

	@Test
	public void testLookup1() {
		db.startTransaction();
		God g = new God(true);
		g.setName("G");
		Creature c1 = new Creature(g);
		c1.setName("c1");
		Creature c2 = new Creature(g);
		c2.setName("c2");
		Spook s1 = new Spook(g);
		s1.setName("s1");
		Spook s2 = new Spook(g);
		s2.setName("s2");
		c1.addToSpook(s1);
		c2.addToSpook(s2);
		Creature c3 = new Creature(g);
		Spook s3 = new Spook(g);
		db.stopTransaction(Conclusion.SUCCESS);

		Assert.assertEquals(1, c1.lookupFor_creature_spook().size());
		Assert.assertEquals(1, c2.lookupFor_creature_spook().size());
		Assert.assertEquals(1, s1.lookupFor_spook_creature().size());
		Assert.assertEquals(1, s2.lookupFor_spook_creature().size());
	}

	@Test
	public void testLookupWithNonCompositeLookupInCompositeTree() {
		db.startTransaction();
		God g = new God(true);
		g.setName("G");
		Nightmare n1 = new Nightmare(true);
		n1.setName("n1");
		g.addToNightmare(n1);
		Nightmare n2 = new Nightmare(true);
		n2.setName("n2");
		g.addToNightmare(n2);
		Nightmare n3 = new Nightmare(true);
		n3.setName("n3");
		g.addToNightmare(n3);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, g.lookupFor_godOfMemory_memory().size());
	}
	
	@Test
	public void testUniqueManyLookup() {
		db.startTransaction();
		God g = new God(true);
		Level1 l1_0 = new Level1(g);
		Level1 l1_1 = new Level1(g);
		Level1 l1_2 = new Level1(g);
		
		Level2 l2_0_0 = new Level2(l1_0);
		Level2 l2_0_1 = new Level2(l1_0);
		Level2 l2_0_2 = new Level2(l1_0);
		
		Level2 l2_1_0 = new Level2(l1_1);
		Level2 l2_1_1 = new Level2(l1_1);
		Level2 l2_1_2 = new Level2(l1_1);
		
		Level2 l2_2_0 = new Level2(l1_2);
		Level2 l2_2_1 = new Level2(l1_2);
		Level2 l2_2_2 = new Level2(l1_2);
		
		Devil1 d1_0 = new Devil1(g);
		Devil1 d1_1 = new Devil1(g);
		Devil1 d1_2 = new Devil1(g);
		
		Devil2 d2_0_0 = new Devil2(d1_0);
		Devil2 d2_0_1 = new Devil2(d1_0);
		Devil2 d2_0_2 = new Devil2(d1_0);
		
		Devil2 d2_1_0 = new Devil2(d1_1);
		Devil2 d2_1_1 = new Devil2(d1_1);
		Devil2 d2_1_2 = new Devil2(d1_1);
		
		Devil2 d2_2_0 = new Devil2(d1_2);
		Devil2 d2_2_1 = new Devil2(d1_2);
		Devil2 d2_2_2 = new Devil2(d1_2);
		
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(9, l2_0_0.lookupFor_level2_devil2().size());
		
		db.startTransaction();
		l2_0_0.addToDevil2(d2_0_0);
		db.stopTransaction(Conclusion.SUCCESS);
		
		//TODO
		l2_0_0.initialiseProperty(Level2.Level2RuntimePropertyEnum.level1, false);
		
		Assert.assertEquals(8, l2_0_0.lookupFor_level2_devil2().size());
	}

}
