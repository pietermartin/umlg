package org.umlg.tests.collectiontest;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.collectiontest.BagRoot;
import org.umlg.collectiontest.BagTest;
import org.umlg.runtime.collection.UmlgBag;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.memory.UmlgMemoryBag;
import org.umlg.runtime.collection.memory.UmlgMemorySet;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/03/05
 * Time: 3:24 PM
 */
public class OclStdLibBagTest extends BaseLocalDbTest {

    @Test
    public void testEquals() {
        UmlgBag<BagTest> test = new UmlgMemoryBag<BagTest>();
        BagRoot bagRoot = new BagRoot(true);
        bagRoot.setName("bagRoot");
        BagTest bagTest1 = new BagTest(bagRoot);

        if (bagRoot.getBagTest().size()==2) {
            System.out.println("aaaaaaaaaaaaaaaa");
        }

        bagTest1.setName("bagTest1");

        if (bagRoot.getBagTest().size()==2) {
            System.out.println("bbbbbbbbbbbbbb");
        }

        BagTest bagTest2 = new BagTest(bagRoot);
        bagTest2.setName("bagTest2");
        BagTest bagTest3 = new BagTest(bagRoot);
        bagTest3.setName("bagTest3");
        BagTest bagTest4 = new BagTest(bagRoot);
        bagTest4.setName("bagTest4");
        bagRoot.getBagTest().add(bagTest2);
        db.commit();

        if (bagRoot.getBagTest().size() > 5) {
            System.out.println("ssssssssssssssssss");
        }

        test.add(bagTest4);
        test.add(bagTest3);
        test.add(bagTest2);
        test.add(bagTest2);
        test.add(bagTest1);

        Assert.assertEquals(5, countVertices());
        //5 for allInstances
        Assert.assertEquals(6 + 5, countEdges());
        Assert.assertTrue(bagRoot.getBagTest().equals(test));

        test.remove(bagTest2);
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(6 + 5, countEdges());
        Assert.assertFalse(bagRoot.getBagTest().equals(test));
    }

    @Test
    public void testUnionBag() {
        UmlgBag<BagTest> test = new UmlgMemoryBag<BagTest>();
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
        Assert.assertEquals(6 + 5, countEdges());
        Assert.assertEquals(10, bagRoot.getBagTest().union(test).size());
        Assert.assertEquals(10, bagRoot.getBagTest().size());
        Assert.assertEquals(10, test.size());
    }

    @Test
    public void testUnionSet() {
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

        UmlgSet<BagTest> test = new UmlgMemorySet<BagTest>();
        test.add(bagTest1);
        test.add(bagTest2);
        test.add(bagTest3);
        test.add(bagTest4);

        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(6 + 5, countEdges());
        Assert.assertEquals(9, bagRoot.getBagTest().union(test).size());
    }

    @Test
    public void testIntersectionBag() {
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

        UmlgBag<BagTest> test = new UmlgMemoryBag<BagTest>();
        test.add(bagTest1);
        test.add(bagTest2);

        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(6 + 5, countEdges());
        Assert.assertEquals(2, bagRoot.getBagTest().intersection(test).size());
    }

    @Test
    public void testIncluding() {
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


        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5 + 5, countEdges());
        Assert.assertEquals(5, bagRoot.getBagTest().including(bagTest1).size());
        Assert.assertEquals(4, bagRoot.getBagTest().size());
    }

    @Test
    public void testExcluding() {
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


        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5 + 5, countEdges());
        Assert.assertEquals(3, bagRoot.getBagTest().excluding(bagTest1).size());
        Assert.assertEquals(4, bagRoot.getBagTest().size());
    }

}