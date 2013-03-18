package org.tuml.tinker.uniqueindextest;

import junit.framework.Assert;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import org.tuml.collectiontest.Finger;
import org.tuml.collectiontest.Hand;
import org.tuml.concretetest.God;
import org.tuml.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/02/15
 * Time: 7:45 PM
 */
public class UniqueIndexTest extends BaseLocalDbTest {

    @Test
    public void testUniqueIndex() {
        God god = new God(true);
        god.setName("god");
        Hand hand1 = new Hand(god);
        hand1.setName("hand1");
        Hand hand2 = new Hand(god);
        hand2.setName("hand2");
        db.commit();
        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(13, countEdges());
        Hand hand2Test = db.instantiateClassifier(hand2.getId());
        Assert.assertEquals(hand2Test, hand2);
        String id = hand2Test.getId();
        hand2Test.delete();
        db.commit();
        Hand hand2Test2 = db.instantiateClassifier(id);
        Assert.assertNull(hand2Test2);
    }
}
