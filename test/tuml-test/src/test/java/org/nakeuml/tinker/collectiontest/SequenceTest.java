package org.nakeuml.tinker.collectiontest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.collectiontest.Hand;
import org.tuml.concretetest.God;
import org.tuml.qualifiertest.Many1;
import org.tuml.qualifiertest.Many2;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class SequenceTest extends BaseLocalDbTest {

	@Test
	public void testSequence() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Hand hand = new Hand(god);
		hand.setLeft(true);
		hand.setName("hand1");
		Hand hand2 = new Hand(god);
		hand2.setLeft(true);
		hand2.setName("hand2");
		Hand hand3 = new Hand(god);
		hand3.setLeft(true);
		hand3.setName("hand3");
		Hand hand4 = new Hand(god);
		hand4.setLeft(true);
		hand4.setName("hand4");
		db.stopTransaction(Conclusion.SUCCESS);
		God godTest = new God(god.getVertex());
		Assert.assertEquals(4, godTest.getHand().size());
		godTest.getHand().get(3).getName().equals("hand4");
		godTest.getHand().get(2).getName().equals("hand3");
		godTest.getHand().get(1).getName().equals("hand2");
		godTest.getHand().get(0).getName().equals("hand1");
	}
	
	@Test
	public void testSequenceMaintainsOrder() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Hand hand = new Hand(god);
		hand.setLeft(true);
		hand.setName("hand1");
		Hand hand2 = new Hand(god);
		hand2.setLeft(true);
		hand2.setName("hand2");
		Hand hand3 = new Hand(god);
		hand3.setLeft(true);
		hand3.setName("hand3");
		Hand hand4 = new Hand(god);
		hand4.setLeft(true);
		hand4.setName("hand4");
		db.stopTransaction(Conclusion.SUCCESS);
		God godTest = new God(god.getVertex());
		Assert.assertEquals(4, godTest.getHand().size());
		godTest.getHand().get(3).getName().equals("hand4");
		godTest.getHand().get(2).getName().equals("hand3");
		godTest.getHand().get(1).getName().equals("hand2");
		godTest.getHand().get(0).getName().equals("hand1");
		
		db.startTransaction();
		
		
		Hand hand1_5 = new Hand(true);
		hand1_5.setLeft(true);
		hand1_5.setName("hand1_5");
		hand1_5.init(god);
		god.getHand().add(1, hand1_5);
		db.stopTransaction(Conclusion.SUCCESS);
		
		God godTest1 = new God(god.getVertex());
		Assert.assertTrue(godTest1.getHand().get(4).getName().equals("hand4"));
		Assert.assertTrue(godTest1.getHand().get(3).getName().equals("hand3"));
		Assert.assertTrue(godTest1.getHand().get(2).getName().equals("hand2"));
		Assert.assertTrue(godTest1.getHand().get(1).getName().equals("hand1_5"));
		Assert.assertTrue(godTest1.getHand().get(0).getName().equals("hand1"));
		
		God godTest2 = new God(god.getVertex());
		int i = 0;
		for (Hand handTest2 : godTest2.getHand()) {
			if (i==0) {
				Assert.assertTrue(handTest2.getName().equals("hand1"));
			} else if (i==1) {
				Assert.assertTrue(handTest2.getName().equals("hand1_5"));
			} else if (i==2) {
				Assert.assertTrue(handTest2.getName().equals("hand2"));
			} else if (i==3) {
				Assert.assertTrue(handTest2.getName().equals("hand3"));
			} else if (i==4) {
				Assert.assertTrue(handTest2.getName().equals("hand4"));
			}
			i++;
		}
		
	}

	@Test
	public void testSequenceAddAll1() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Hand hand = new Hand(god);
		hand.setLeft(true);
		hand.setName("hand1");
		Hand hand2 = new Hand(god);
		hand2.setLeft(true);
		hand2.setName("hand2");
		Hand hand3 = new Hand(god);
		hand3.setLeft(true);
		hand3.setName("hand3");
		Hand hand4 = new Hand(god);
		hand4.setLeft(true);
		hand4.setName("hand4");
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		Hand hand5 = new Hand(true);
		hand5.setLeft(true);
		hand5.setName("hand5");
		Hand hand6 = new Hand(true);
		hand6.setLeft(true);
		hand6.setName("hand6");
		Hand hand7 = new Hand(true);
		hand7.setLeft(true);
		hand7.setName("hand7");
		Hand hand8 = new Hand(true);
		hand8.setLeft(true);
		hand8.setName("hand8");
		List<Hand> moreHands = new ArrayList<Hand>(4);
		moreHands.add(hand5);
		moreHands.add(hand6);
		moreHands.add(hand7);
		moreHands.add(hand8);
		god.getHand().addAll(moreHands);
		db.stopTransaction(Conclusion.SUCCESS);

		God godTest = new God(god.getVertex());
		Assert.assertEquals(8, godTest.getHand().size());
		Assert.assertEquals("hand1", godTest.getHand().get(0).getName());
		Assert.assertEquals("hand2", godTest.getHand().get(1).getName());
		Assert.assertEquals("hand3", godTest.getHand().get(2).getName());
		Assert.assertEquals("hand4", godTest.getHand().get(3).getName());
		Assert.assertEquals("hand5", godTest.getHand().get(4).getName());
		Assert.assertEquals("hand6", godTest.getHand().get(5).getName());
		Assert.assertEquals("hand7", godTest.getHand().get(6).getName());
		Assert.assertEquals("hand8", godTest.getHand().get(7).getName());
	}
	
	@Test
	public void testSequenceAddAll2() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Hand hand = new Hand(god);
		hand.setLeft(true);
		hand.setName("hand1");
		Hand hand2 = new Hand(god);
		hand2.setLeft(true);
		hand2.setName("hand2");
		Hand hand3 = new Hand(god);
		hand3.setLeft(true);
		hand3.setName("hand3");
		Hand hand4 = new Hand(god);
		hand4.setLeft(true);
		hand4.setName("hand4");
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		Hand hand5 = new Hand(true);
		hand5.setLeft(true);
		hand5.setName("hand5");
		Hand hand6 = new Hand(true);
		hand6.setLeft(true);
		hand6.setName("hand6");
		Hand hand7 = new Hand(true);
		hand7.setLeft(true);
		hand7.setName("hand7");
		Hand hand8 = new Hand(true);
		hand8.setLeft(true);
		hand8.setName("hand8");
		List<Hand> moreHands = new ArrayList<Hand>(4);
		moreHands.add(hand5);
		moreHands.add(hand6);
		moreHands.add(hand7);
		moreHands.add(hand8);
		god.getHand().addAll(2, moreHands);
		db.stopTransaction(Conclusion.SUCCESS);

		God godTest = new God(god.getVertex());
		Assert.assertEquals(8, godTest.getHand().size());
		Assert.assertEquals("hand1", godTest.getHand().get(0).getName());
		Assert.assertEquals("hand2", godTest.getHand().get(1).getName());
		Assert.assertEquals("hand5", godTest.getHand().get(2).getName());
		Assert.assertEquals("hand6", godTest.getHand().get(3).getName());
		Assert.assertEquals("hand7", godTest.getHand().get(4).getName());
		Assert.assertEquals("hand8", godTest.getHand().get(5).getName());
		Assert.assertEquals("hand3", godTest.getHand().get(6).getName());
		Assert.assertEquals("hand4", godTest.getHand().get(7).getName());
	}

	@Test
	public void testSequenceAddObjectMoreThanOnce() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Hand hand = new Hand(god);
		hand.setLeft(true);
		hand.setName("hand1");
		Hand hand2 = new Hand(god);
		hand2.setLeft(true);
		hand2.setName("hand2");
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		God godTest = new God(god.getVertex());
		godTest.getHand().add(hand);
		godTest.getHand().add(hand2);
		db.stopTransaction(Conclusion.SUCCESS);
		
		God godTest2 = new God(god.getVertex());
		Assert.assertEquals(4, godTest2.getHand().size());
		Assert.assertEquals("hand1", godTest2.getHand().get(0).getName());
		Assert.assertEquals("hand2", godTest2.getHand().get(1).getName());
		Assert.assertEquals("hand1", godTest2.getHand().get(2).getName());
		Assert.assertEquals("hand2", godTest2.getHand().get(3).getName());
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(5, countEdges());
	}
	
	@Test
	public void testManyToManySequenceWithDuplicates() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		
		Many1 many1_1 = new Many1(true);
		many1_1.setName("many1_1");
		many1_1.init(god);
		many1_1.addToOwningObject();
		
		Many1 many1_2 = new Many1(true);
		many1_2.setName("many1_1");
		many1_2.init(god);
		many1_2.addToOwningObject();

		Many1 many1_3 = new Many1(true);
		many1_3.setName("many1_1");
		many1_3.init(god);
		many1_3.addToOwningObject();

		Many1 many1_4 = new Many1(true);
		many1_4.setName("many1_4");
		many1_4.init(god);
		many1_4.addToOwningObject();

		Many2 many2_1 = new Many2(true);
		many2_1.setName("many2_1");
		many2_1.init(god);
		many2_1.addToOwningObject();
		
		Many2 many2_2 = new Many2(true);
		many2_2.setName("many2_2");
		many2_2.init(god);
		many2_2.addToOwningObject();

		Many2 many2_3 = new Many2(true);
		many2_3.setName("many2_3");
		many2_3.init(god);
		many2_3.addToOwningObject();

		Many2 many2_4 = new Many2(true);
		many2_4.setName("many2_4");
		many2_4.init(god);
		many2_4.addToOwningObject();

		many1_1.addToMany2UnqualifiedList(many2_1);
		many1_1.addToMany2UnqualifiedList(many2_2);
		many1_1.addToMany2UnqualifiedList(many2_3);
		many1_1.addToMany2UnqualifiedList(many2_4);

		many1_2.addToMany2UnqualifiedList(many2_1);
		many1_2.addToMany2UnqualifiedList(many2_2);
		many1_2.addToMany2UnqualifiedList(many2_3);
		many1_2.addToMany2UnqualifiedList(many2_4);

		many1_3.addToMany2UnqualifiedList(many2_1);
		many1_3.addToMany2UnqualifiedList(many2_2);
		many1_3.addToMany2UnqualifiedList(many2_3);
		many1_3.addToMany2UnqualifiedList(many2_4);

		many1_4.addToMany2UnqualifiedList(many2_1);
		many1_4.addToMany2UnqualifiedList(many2_2);
		many1_4.addToMany2UnqualifiedList(many2_3);
		many1_4.addToMany2UnqualifiedList(many2_4);
		
		//Some duplicates
		many1_1.addToMany2UnqualifiedList(many2_1);
		many1_1.addToMany2UnqualifiedList(many2_2);
		many1_1.addToMany2UnqualifiedList(many2_3);
		many1_1.addToMany2UnqualifiedList(many2_4);

		//Some duplicates
		many2_1.addToMany1UnqualifiedList(many1_1);
		many2_1.addToMany1UnqualifiedList(many1_2);
		many2_1.addToMany1UnqualifiedList(many1_3);
		many2_1.addToMany1UnqualifiedList(many1_4);

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(9, countVertices());
		Assert.assertEquals(33, countEdges());
	}

	
}