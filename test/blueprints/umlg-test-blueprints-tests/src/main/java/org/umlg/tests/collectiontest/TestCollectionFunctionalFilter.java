package org.umlg.tests.collectiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.Hand;
import org.umlg.concretetest.God;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/08/19
 * Time: 11:55 AM
 */
public class TestCollectionFunctionalFilter extends BaseLocalDbTest {

    @Test
    public void testFunctionalFilter() {
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
        db.commit();
        God godTest = new God(god.getVertex());
        Assert.assertEquals(4, godTest.getHand().size());
        Assert.assertEquals(1, Hand.allInstances((h)->((Hand)h).getName().equals("hand1")).size());
    }

}
