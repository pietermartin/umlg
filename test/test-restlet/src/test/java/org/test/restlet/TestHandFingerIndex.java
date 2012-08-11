package org.test.restlet;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.runtime.test.BaseLocalDbTest;
import org.tuml.test.Finger;
import org.tuml.test.Hand;
import org.tuml.test.Human;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestHandFingerIndex extends BaseLocalDbTest {

	@Test
	public void test() {
		db.startTransaction();
		Human human = new Human(true);
		Hand hand = new Hand(human);
		hand.setName("left");
		Finger finger1 = new Finger(hand);
		finger1.setName("finger1");
		Finger finger2 = new Finger(hand);
		finger2.setName("finger2");
		Finger finger3 = new Finger(hand);
		finger3.setName("finger3");
		db.stopTransaction(Conclusion.SUCCESS);
		
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(5, countEdges());
		
		db.startTransaction();
		Hand handTest = new Hand(db.getVertex(hand.getId()));
		Finger fingerTest = new Finger(db.getVertex(finger1.getId()));
		handTest.addToFinger(fingerTest);
		db.stopTransaction(Conclusion.SUCCESS);
		
	}
	
}
