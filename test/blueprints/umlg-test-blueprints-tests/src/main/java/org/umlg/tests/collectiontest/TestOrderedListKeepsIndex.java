package org.umlg.tests.collectiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.Finger;
import org.umlg.collectiontest.Hand;
import org.umlg.concretetest.God;
import org.umlg.runtime.test.BaseLocalDbTest;

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

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        Hand handTest = db.getEntity(hand.getId());
        Finger fingerTest = db.getEntity(finger1.getId());
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

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        Hand handTest = db.getEntity(hand.getId());
        Finger fingerTest = db.getEntity(finger1.getId());
        handTest.addToFinger(fingerTest);
        db.commit();

        Assert.assertEquals(2, handTest.getFinger().indexOf(fingerTest));
    }
}
