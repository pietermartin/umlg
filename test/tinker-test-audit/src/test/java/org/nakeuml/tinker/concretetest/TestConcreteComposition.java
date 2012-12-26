package org.nakeuml.tinker.concretetest;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Test;
import org.tuml.test.tinker.BaseLocalDbTest;
import org.tinker.collectiontest.Hand;
import org.tinker.concretetest.God;
import org.tinker.inheritencetest.Biped;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class TestConcreteComposition extends BaseLocalDbTest {

	@Test
	public void testConcreteComposition() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(2, countVertices());
		assertEquals(2, countEdges());

		db.startTransaction();
		Hand hand = new Hand(god);
		hand.setName("hand1");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(5, countVertices());
		assertEquals(6, countEdges());

		db.startTransaction();
		Hand hand2 = new Hand(god);
		hand2.setName("hand2");
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(8, countVertices());
		assertEquals(10, countEdges());

		God godTest = new God(god.getVertex());
		Assert.assertEquals(3, godTest.getAudits().size());

		Assert.assertEquals(0, godTest.getAudits().get(0).getHand().size());
		Assert.assertEquals(1, godTest.getAudits().get(1).getHand().size());
		Assert.assertEquals(2, godTest.getAudits().get(2).getHand().size());

		assertEquals(8, countVertices());
		//To previous edges are created whilst traversing audits
		assertEquals(12, countEdges());

		db.startTransaction();
		God godTestDeleteHand = new God(god.getVertex());
		Hand handToDelete = godTestDeleteHand.getHand().get(1);
		handToDelete.markDeleted();
		db.stopTransaction(Conclusion.SUCCESS);
		
		assertEquals(9, countVertices());
		assertEquals(13, countEdges());

		godTest = new God(god.getVertex());
		Assert.assertEquals(4, godTest.getAudits().size());

		Assert.assertEquals(0, godTest.getAudits().get(0).getHand().size());
		Assert.assertEquals(1, godTest.getAudits().get(1).getHand().size());
		Assert.assertEquals(2, godTest.getAudits().get(2).getHand().size());
		Assert.assertEquals(1, godTest.getAudits().get(3).getHand().size());

	}

	@Test
	public void testEdgeToPreviousCreatedOnMarkDeleted() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Biped biped = new Biped(god);
		biped.setName("biped");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(4, countVertices());
		Assert.assertEquals(5, countEdges());
		db.startTransaction();
		biped.markDeleted();
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(6, countEdges());
	}
	
}
