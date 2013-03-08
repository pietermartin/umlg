package org.tuml.tinker.collectiontest;

import junit.framework.Assert;
import org.junit.Test;
import org.tuml.collectiontest.Finger;
import org.tuml.collectiontest.Hand;
import org.tuml.concretetest.God;
import org.tuml.runtime.test.BaseLocalDbTest;

/**
 * Date: 2012/12/28
 * Time: 5:13 PM
 */
public class TestOrderedListKeepsIndex extends BaseLocalDbTest {

    @Test
    public void testOrderedListKeepIndex() {
        God g = new God(true);
        Hand hand = new Hand(g);
        hand.setName("left");
        Finger finger1 = new Finger(hand);
        finger1.setName("finger1");
        Finger finger2 = new Finger(hand);
        finger2.setName("finger2");
        Finger finger3 = new Finger(hand);
        finger3.setName("finger3");
        db.commit();

        Assert.assertEquals(6 + 3, countVertices());
        Assert.assertEquals(12 + 3 + 5, countEdges());

        Hand handTest = new Hand(db.getVertex(hand.getId()));
        Finger fingerTest = new Finger(db.getVertex(finger1.getId()));
        handTest.getFinger().add(fingerTest);
        db.commit();

        Assert.assertEquals(0, handTest.getFinger().indexOf(fingerTest));
    }

    //TODO think about the semantics of addToX, addToFinger removes the hand from the finger and then adds it to the hand
    //addToX does not have the remove logic and readd logic
    @Test
    public void testOrderedListMoveIndex() {
        God g = new God(true);
        Hand hand = new Hand(g);
        hand.setName("left");
        Finger finger1 = new Finger(hand);
        finger1.setName("finger1");
        Finger finger2 = new Finger(hand);
        finger2.setName("finger2");
        Finger finger3 = new Finger(hand);
        finger3.setName("finger3");
        db.commit();

        Assert.assertEquals(6 + 3, countVertices());
        Assert.assertEquals(12 + 3 + 5, countEdges());

        Hand handTest = new Hand(db.getVertex(hand.getId()));
        Finger fingerTest = new Finger(db.getVertex(finger1.getId()));
        handTest.addToFinger(fingerTest);
        db.commit();

        Assert.assertEquals(2, handTest.getFinger().indexOf(fingerTest));
    }
}
