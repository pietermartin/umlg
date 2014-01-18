package org.umlg.tests.ringtest;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.collectiontest.Finger;
import org.umlg.collectiontest.Hand;
import org.umlg.collectiontest.Ring;
import org.umlg.concretetest.God;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/10/18
 * Time: 8:29 PM
 */
public class TestFingerRing extends BaseLocalDbTest {

    @Test
    public void testFingerRing() {
        God g = new God(true);
        g.setName("name");
        Hand hand = new Hand(g);
        hand.setName("hand1");
        Finger finger1 = new Finger(hand);
        finger1.setName("finger1");
        Finger finger2 = new Finger(hand);
        finger2.setName("finger2");
        Ring ring1 = new Ring(true);
        ring1.setName("ring1");
        Ring ring2 = new Ring(true);
        ring2.setName("ring2");
        Ring ring3 = new Ring(true);
        ring3.setName("ring3");
        Ring ring4 = new Ring(true);
        ring4.setName("ring4");

        finger1.addToRing(ring1);
        finger1.addToRing(ring2);
        finger1.addToRing(ring3);
        finger1.addToRing(ring4);
        db.commit();

        finger1.reload();
        Assert.assertEquals(4, finger1.getRing().size());

        ring1.reload();
        ring1.clearFinger();

        db.commit();

        finger1.reload();
        Assert.assertEquals(3, finger1.getRing().size());
        ring1.reload();
        Assert.assertNull(ring1.getFinger());

    }

    @Test(expected = IllegalStateException.class)
    public void testFingerAddedToAnotherHand() {
        God g = new God(true);
        g.setName("god");
        Hand hand1 = new Hand(g);
        hand1.setName("hand1");
        Hand hand2 = new Hand(g);
        hand2.setName("hand2");
        Finger finger1 = new Finger(hand1);
        finger1.setName("finger1");
        Finger finger2 = new Finger(hand2);
        finger2.setName("finger2");
        db.commit();

        hand2.getFinger().add(finger1);
        db.commit();

        hand2.reload();
        Assert.assertEquals(2, hand2.getFinger().size());
        Assert.assertEquals(0, hand1.getFinger().size());
    }

    @Test
    public void testFingerAddedToAnotherHandViaAdder() {
        God g = new God(true);
        g.setName("god");
        Hand hand1 = new Hand(g);
        hand1.setName("hand1");
        Hand hand2 = new Hand(g);
        hand2.setName("hand2");
        Finger finger1 = new Finger(hand1);
        finger1.setName("finger1");
        Finger finger2 = new Finger(hand2);
        finger2.setName("finger2");
        db.commit();

        hand2.addToFinger(finger1);
        db.commit();

        hand2.reload();
        Assert.assertEquals(2, hand2.getFinger().size());
        Assert.assertEquals(0, hand1.getFinger().size());
    }

}
