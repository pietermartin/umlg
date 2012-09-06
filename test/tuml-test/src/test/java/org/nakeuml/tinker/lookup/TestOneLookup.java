package org.nakeuml.tinker.lookup;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.collectiontest.Nightmare;
import org.tuml.concretetest.God;
import org.tuml.interfacetest.Being;
import org.tuml.interfacetest.Creature;
import org.tuml.interfacetest.Spook;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestOneLookup extends BaseLocalDbTest {

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

		TinkerSet<Creature> result = g.getBeing().collect(new BodyExpressionEvaluator<Creature, Being>() {@Override	public Creature evaluate(Being e) {	if (e instanceof Creature) { return (Creature)e; } return null;	}}).asSet();
		Assert.assertEquals(2, result.size());
		
		TinkerSet<Being> creatures = g.getBeing().select(new BooleanExpressionEvaluator<Being>() {@Override	public Boolean evaluate(Being e) {	return e instanceof Creature; }	});
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
	
}
