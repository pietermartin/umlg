package org.nakeuml.tinker.lookup;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.collectiontest.Nightmare;
import org.tuml.concretetest.God;
import org.tuml.interfacetest.Being;
import org.tuml.interfacetest.Creature;
import org.tuml.interfacetest.IManyB;
import org.tuml.interfacetest.ManyA;
import org.tuml.interfacetest.ManyB;
import org.tuml.interfacetest.Spook;
import org.tuml.lookup.Devil1;
import org.tuml.lookup.Devil2;
import org.tuml.lookup.Devil3;
import org.tuml.lookup.Level1;
import org.tuml.lookup.Level2;
import org.tuml.lookup.Level3;
import org.tuml.lookup.Nevil1;
import org.tuml.lookup.Nevil2;
import org.tuml.lookup.Nevil3;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestLookup extends BaseLocalDbTest {

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

		TinkerSet<Creature> result = g.getBeing().<Creature, Creature>collect(new BodyExpressionEvaluator<Creature, Being>() {
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
		db.stopTransaction(Conclusion.SUCCESS);

		Assert.assertEquals(2, c1.lookupSpook().size());
		Assert.assertEquals(2, c2.lookupSpook().size());
		Assert.assertEquals(2, s1.lookupCreature().size());
		Assert.assertEquals(2, s2.lookupCreature().size());
	}

	@Test
	public void testLookupWithNonCompositeLoopupInCompositeTree() {
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
		Assert.assertEquals(3, g.lookupMemory().size());
	}

	@Test
	public void testManyLookupWith() {
		db.startTransaction();
		God g = new God(true);
		g.setName("G");
		ManyA a1 = new ManyA(g);
		a1.setName("a1");
		ManyA a2 = new ManyA(g);
		a2.setName("a2");
		ManyA a3 = new ManyA(g);
		a3.setName("a3");
		ManyA a4 = new ManyA(g);
		a4.setName("a4");

		ManyB b1 = new ManyB(g);
		b1.setName("b1");
		ManyB b2 = new ManyB(g);
		b2.setName("b2");
		ManyB b3 = new ManyB(g);
		b3.setName("b3");
		ManyB b4 = new ManyB(g);
		b4.setName("b4");

		a1.addToIManyB(b1);
		a1.addToIManyB(b2);
		a1.addToIManyB(b3);
		a1.addToIManyB(b4);

		a2.addToIManyB(b1);
		a2.addToIManyB(b2);
		a2.addToIManyB(b3);
		a2.addToIManyB(b4);

		a3.addToIManyB(b1);
		a3.addToIManyB(b2);
		a3.addToIManyB(b3);
		a3.addToIManyB(b4);

		a4.addToIManyB(b1);
		a4.addToIManyB(b2);
		a4.addToIManyB(b3);
		a4.addToIManyB(b4);

		db.stopTransaction(Conclusion.SUCCESS);
		a1 = new ManyA(a1.getVertex());

		TinkerSet<IManyB> lookup = a1.lookupIManyB();
		for (IManyB iManyB : lookup) {
			System.out.println(iManyB);
		}
		Assert.assertEquals(4, lookup.size());
		a2 = new ManyA(a2.getVertex());
		Assert.assertEquals(4, a2.lookupIManyB().size());
		a3 = new ManyA(a1.getVertex());
		Assert.assertEquals(4, a3.lookupIManyB().size());
		a4 = new ManyA(a1.getVertex());
		Assert.assertEquals(4, a3.lookupIManyB().size());
	}
	
	@Test
	public void testLookupDeeperHierarchy1() {
		db.startTransaction();
		God g = new God(true);
		g.setName("g1");

		Nevil1 n1_1 = new Nevil1(g);

		Nevil2 n2_1 = new Nevil2(n1_1);
		Nevil2 n2_2 = new Nevil2(n1_1);

		Nevil3 n3_1 = new Nevil3(n2_1);
		Nevil3 n3_2 = new Nevil3(n2_1);
		Nevil3 n3_3 = new Nevil3(n2_1);
		Nevil3 n3_4 = new Nevil3(n2_1);

		Nevil3 n3_2_1 = new Nevil3(n2_2);
		Nevil3 n3_2_2 = new Nevil3(n2_2);
		Nevil3 n3_2_3 = new Nevil3(n2_2);
		Nevil3 n3_2_4 = new Nevil3(n2_2);
		db.stopTransaction(Conclusion.SUCCESS);
		
		Assert.assertEquals(1, n3_2_1.lookupNevil1One().size());
		Assert.assertEquals(1, n3_2_2.lookupNevil1One().size());
		Assert.assertEquals(1, n3_2_3.lookupNevil1One().size());
		Assert.assertEquals(1, n3_2_4.lookupNevil1One().size());
		Assert.assertEquals(8, n1_1.lookupNevil3One().size());

	}

	@Test
	public void testLookupDeeperHierarchy2() {
		db.startTransaction();
		God g = new God(true);
		g.setName("g1");
		Level1 l1_1 = new Level1(g);

		Level2 l2_1 = new Level2(l1_1);
		Level2 l2_2 = new Level2(l1_1);

		Level3 l3_1 = new Level3(l2_1);
		Level3 l3_2 = new Level3(l2_1);
		Level3 l3_3 = new Level3(l2_1);
		Level3 l3_4 = new Level3(l2_1);

		Level3 l3_2_1 = new Level3(l2_2);
		Level3 l3_2_2 = new Level3(l2_2);
		Level3 l3_2_3 = new Level3(l2_2);
		Level3 l3_2_4 = new Level3(l2_2);

		Devil1 d1_1 = new Devil1(g);

		Devil2 d2_1 = new Devil2(d1_1);
		Devil2 d2_2 = new Devil2(d1_1);

		Devil3 d3_1 = new Devil3(d2_1);
		Devil3 d3_2 = new Devil3(d2_1);
		Devil3 d3_3 = new Devil3(d2_1);
		Devil3 d3_4 = new Devil3(d2_1);

		Devil3 d3_2_1 = new Devil3(d2_2);
		Devil3 d3_2_2 = new Devil3(d2_2);
		Devil3 d3_2_3 = new Devil3(d2_2);
		Devil3 d3_2_4 = new Devil3(d2_2);

		db.stopTransaction(Conclusion.SUCCESS);
		
		Assert.assertEquals(8, l3_2_1.lookupDevil3().size());
		Assert.assertEquals(8, l3_2_2.lookupDevil3().size());
		Assert.assertEquals(8, l3_2_3.lookupDevil3().size());
		Assert.assertEquals(8, l3_2_4.lookupDevil3().size());

		Assert.assertEquals(8, d3_2_1.lookupLevel3().size());
		Assert.assertEquals(8, d3_2_2.lookupLevel3().size());
		Assert.assertEquals(8, d3_2_3.lookupLevel3().size());
		Assert.assertEquals(8, d3_2_4.lookupLevel3().size());

	}

}
