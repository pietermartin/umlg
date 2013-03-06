package org.tuml.tinker.collectiontest;

import junit.framework.Assert;
import org.junit.Test;
import org.tuml.collectiontest.BagRoot;
import org.tuml.collectiontest.BagTest;
import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.memory.TumlMemoryBag;
import org.tuml.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/03/05
 * Time: 3:24 PM
 */
public class OclStdLibBagTest extends BaseLocalDbTest {

    @Test
    public void testEquals() {
        TinkerBag<BagTest> test = new TumlMemoryBag<BagTest>();
        BagRoot bagRoot = new BagRoot(true);
        bagRoot.setName("bagRoot");
        BagTest bagTest1 = new BagTest(bagRoot);
        bagTest1.setName("bagTest1");
        BagTest bagTest2 = new BagTest(bagRoot);
        bagTest2.setName("bagTest2");
        BagTest bagTest3 = new BagTest(bagRoot);
        bagTest3.setName("bagTest3");
        BagTest bagTest4 = new BagTest(bagRoot);
        bagTest4.setName("bagTest4");
        bagRoot.getBagTest().add(bagTest2);
        db.commit();

        test.add(bagTest4);
        test.add(bagTest3);
        test.add(bagTest2);
        test.add(bagTest2);
        test.add(bagTest1);

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(6, countEdges());
        Assert.assertTrue(bagRoot.getBagTest().equals(test));

        test.remove(bagTest2);
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(6, countEdges());
        Assert.assertFalse(bagRoot.getBagTest().equals(test));
    }

    @Test
    public void testUnion() {
        TinkerBag<BagTest> test = new TumlMemoryBag<BagTest>();
        BagRoot bagRoot = new BagRoot(true);
        bagRoot.setName("bagRoot");
        BagTest bagTest1 = new BagTest(bagRoot);
        bagTest1.setName("bagTest1");
        BagTest bagTest2 = new BagTest(bagRoot);
        bagTest2.setName("bagTest2");
        BagTest bagTest3 = new BagTest(bagRoot);
        bagTest3.setName("bagTest3");
        BagTest bagTest4 = new BagTest(bagRoot);
        bagTest4.setName("bagTest4");
        bagRoot.getBagTest().add(bagTest2);
        db.commit();

        test.add(bagTest4);
        test.add(bagTest3);
        test.add(bagTest2);
        test.add(bagTest2);
        test.add(bagTest1);

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(6, countEdges());
        Assert.assertEquals(10, bagRoot.getBagTest().union(test).size());
    }
}