package org.tuml.tinker.collectiontest;

import junit.framework.Assert;
import org.junit.Test;
import org.tuml.collectiontest.OrderedSetRoot;
import org.tuml.collectiontest.OrderedSetTest;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/03/06
 * Time: 9:05 PM
 */
public class OclStdLibOrderedSetTest extends BaseLocalDbTest {

    @Test
    public void testAppend() {
        OrderedSetRoot orderedSetRoot = new OrderedSetRoot(true);
        orderedSetRoot.setName("orderedSetRoot");
        OrderedSetTest orderedSetTest1 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest1.setName("orderedSetTest1");
        OrderedSetTest orderedSetTest2 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest2.setName("orderedSetTest2");
        OrderedSetTest orderedSetTest3 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest3.setName("orderedSetTest3");
        OrderedSetTest orderedSetTest4 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest4.setName("orderedSetTest4");

        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(10 + 5, countEdges());

        OrderedSetRoot orderedSetRoot2 = new OrderedSetRoot(true);
        orderedSetRoot2.setName("orderedSetRoot2");
        OrderedSetTest orderedSetTest5 = new OrderedSetTest(orderedSetRoot2);
        orderedSetTest5.setName("orderedSetTest5");

        Assert.assertEquals(5, orderedSetRoot.getOrderedSetTest().append(orderedSetTest5).size());
        Assert.assertEquals(orderedSetTest5, orderedSetRoot.getOrderedSetTest().append(orderedSetTest5).get(4));
    }

    @Test
    public void testPrepend() {
        OrderedSetRoot orderedSetRoot = new OrderedSetRoot(true);
        orderedSetRoot.setName("orderedSetRoot");
        OrderedSetTest orderedSetTest1 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest1.setName("orderedSetTest1");
        OrderedSetTest orderedSetTest2 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest2.setName("orderedSetTest2");
        OrderedSetTest orderedSetTest3 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest3.setName("orderedSetTest3");
        OrderedSetTest orderedSetTest4 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest4.setName("orderedSetTest4");

        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(10 + 5, countEdges());

        OrderedSetRoot orderedSetRoot2 = new OrderedSetRoot(true);
        orderedSetRoot2.setName("orderedSetRoot2");
        OrderedSetTest orderedSetTest5 = new OrderedSetTest(orderedSetRoot2);
        orderedSetTest5.setName("orderedSetTest5");

        Assert.assertEquals(5, orderedSetRoot.getOrderedSetTest().prepend(orderedSetTest5).size());
        Assert.assertEquals(orderedSetTest5, orderedSetRoot.getOrderedSetTest().prepend(orderedSetTest5).get(0));
    }

    @Test
    public void testInsertAt2() {
        OrderedSetRoot orderedSetRoot = new OrderedSetRoot(true);
        orderedSetRoot.setName("orderedSetRoot");
        OrderedSetTest orderedSetTest1 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest1.setName("orderedSetTest1");
        OrderedSetTest orderedSetTest2 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest2.setName("orderedSetTest2");
        OrderedSetTest orderedSetTest3 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest3.setName("orderedSetTest3");
        OrderedSetTest orderedSetTest4 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest4.setName("orderedSetTest4");

        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(10 + 5, countEdges());

        OrderedSetRoot orderedSetRoot2 = new OrderedSetRoot(true);
        orderedSetRoot2.setName("orderedSetRoot2");
        OrderedSetTest orderedSetTest5 = new OrderedSetTest(orderedSetRoot2);
        orderedSetTest5.setName("orderedSetTest5");

        TinkerOrderedSet<OrderedSetTest> orderedSetTests = orderedSetRoot.getOrderedSetTest().insertAt(3, orderedSetTest5);
        Assert.assertEquals(5, orderedSetTests.size());
        Assert.assertEquals(orderedSetTest5, orderedSetTests.get(3));
    }

    @Test
    public void testInsertSubOrderedSet() {
        OrderedSetRoot orderedSetRoot = new OrderedSetRoot(true);
        orderedSetRoot.setName("orderedSetRoot");
        OrderedSetTest orderedSetTest1 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest1.setName("orderedSetTest1");
        OrderedSetTest orderedSetTest2 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest2.setName("orderedSetTest2");
        OrderedSetTest orderedSetTest3 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest3.setName("orderedSetTest3");
        OrderedSetTest orderedSetTest4 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest4.setName("orderedSetTest4");

        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(10 + 5, countEdges());

        OrderedSetRoot orderedSetRoot2 = new OrderedSetRoot(true);
        orderedSetRoot2.setName("orderedSetRoot2");
        OrderedSetTest orderedSetTest5 = new OrderedSetTest(orderedSetRoot2);
        orderedSetTest5.setName("orderedSetTest5");

        TinkerOrderedSet<OrderedSetTest> orderedSetTests = orderedSetRoot.getOrderedSetTest().subOrderedSet(2, 3);
        Assert.assertEquals(2, orderedSetTests.size());
    }

    @Test
    public void testInsertAt() {
        OrderedSetRoot orderedSetRoot = new OrderedSetRoot(true);
        orderedSetRoot.setName("orderedSetRoot");
        OrderedSetTest orderedSetTest1 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest1.setName("orderedSetTest1");
        OrderedSetTest orderedSetTest2 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest2.setName("orderedSetTest2");
        OrderedSetTest orderedSetTest3 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest3.setName("orderedSetTest3");
        OrderedSetTest orderedSetTest4 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest4.setName("orderedSetTest4");

        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(10 + 5, countEdges());

        Assert.assertEquals(orderedSetTest4, orderedSetRoot.getOrderedSetTest().at(3));
    }

    @Test
    public void testFirst() {
        OrderedSetRoot orderedSetRoot = new OrderedSetRoot(true);
        orderedSetRoot.setName("orderedSetRoot");
        OrderedSetTest orderedSetTest1 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest1.setName("orderedSetTest1");
        OrderedSetTest orderedSetTest2 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest2.setName("orderedSetTest2");
        OrderedSetTest orderedSetTest3 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest3.setName("orderedSetTest3");
        OrderedSetTest orderedSetTest4 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest4.setName("orderedSetTest4");

        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(10 + 5, countEdges());

        Assert.assertEquals(orderedSetTest1, orderedSetRoot.getOrderedSetTest().first());
    }

    @Test
    public void testLast() {
        OrderedSetRoot orderedSetRoot = new OrderedSetRoot(true);
        orderedSetRoot.setName("orderedSetRoot");
        OrderedSetTest orderedSetTest1 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest1.setName("orderedSetTest1");
        OrderedSetTest orderedSetTest2 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest2.setName("orderedSetTest2");
        OrderedSetTest orderedSetTest3 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest3.setName("orderedSetTest3");
        OrderedSetTest orderedSetTest4 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest4.setName("orderedSetTest4");

        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(10 + 5, countEdges());

        Assert.assertEquals(orderedSetTest4, orderedSetRoot.getOrderedSetTest().last());
    }

    @Test
    public void testReverse() {
        OrderedSetRoot orderedSetRoot = new OrderedSetRoot(true);
        orderedSetRoot.setName("orderedSetRoot");
        OrderedSetTest orderedSetTest1 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest1.setName("orderedSetTest1");
        OrderedSetTest orderedSetTest2 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest2.setName("orderedSetTest2");
        OrderedSetTest orderedSetTest3 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest3.setName("orderedSetTest3");
        OrderedSetTest orderedSetTest4 = new OrderedSetTest(orderedSetRoot);
        orderedSetTest4.setName("orderedSetTest4");

        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(10 + 5, countEdges());

        TinkerOrderedSet<OrderedSetTest> reverse = orderedSetRoot.getOrderedSetTest().reverse();
        Assert.assertEquals(orderedSetTest1, reverse.get(3));
    }
}
