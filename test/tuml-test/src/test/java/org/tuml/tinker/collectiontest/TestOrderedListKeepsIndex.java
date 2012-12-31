package org.tuml.tinker.collectiontest;

import com.tinkerpop.blueprints.TransactionalGraph;
import junit.framework.Assert;
import org.junit.Test;
import org.tuml.collectiontest.Bag;
import org.tuml.collectiontest.Finger;
import org.tuml.collectiontest.Hand;
import org.tuml.collectiontest.Nightmare;
import org.tuml.concretetest.God;
import org.tuml.runtime.test.BaseLocalDbTest;

/**
 * Date: 2012/12/28
 * Time: 5:13 PM
 */
public class TestOrderedListKeepsIndex extends BaseLocalDbTest {

    @Test
    public void testOrderedListKeepIndex() {
        db.startTransaction();
        God g = new God(true);
        Hand hand = new Hand(g);
        hand.setName("left");
        Finger finger1 = new Finger(hand);
        finger1.setName("finger1");
        Finger finger2 = new Finger(hand);
        finger2.setName("finger2");
        Finger finger3 = new Finger(hand);
        finger3.setName("finger3");
        db.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        db.startTransaction();
        Hand handTest = new Hand(db.getVertex(hand.getId()));
        Finger fingerTest = new Finger(db.getVertex(finger1.getId()));
        handTest.getFinger().add(fingerTest);
        db.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);

        Assert.assertEquals(0, handTest.getFinger().indexOf(fingerTest));
    }

    //TODO think about the semantics of addToX
    @Test
    public void testOrderedListMoveIndex() {
        db.startTransaction();
        God g = new God(true);
        Hand hand = new Hand(g);
        hand.setName("left");
        Finger finger1 = new Finger(hand);
        finger1.setName("finger1");
        Finger finger2 = new Finger(hand);
        finger2.setName("finger2");
        Finger finger3 = new Finger(hand);
        finger3.setName("finger3");
        db.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        db.startTransaction();
        Hand handTest = new Hand(db.getVertex(hand.getId()));
        Finger fingerTest = new Finger(db.getVertex(finger1.getId()));
        handTest.addToFinger(fingerTest);
        db.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);

        Assert.assertEquals(2, handTest.getFinger().indexOf(fingerTest));
    }
}
