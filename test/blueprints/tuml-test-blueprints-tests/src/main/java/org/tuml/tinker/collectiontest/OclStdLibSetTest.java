package org.tuml.tinker.collectiontest;

import junit.framework.Assert;
import org.junit.Test;
import org.tuml.collectiontest.SetRoot;
import org.tuml.collectiontest.SetTest;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.memory.TumlMemorySet;
import org.tuml.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/03/03
 * Time: 8:36 PM
 */
public class OclStdLibSetTest extends BaseLocalDbTest {

    @Test
    public void testUnionSet() {
        SetRoot setRoot1 = new SetRoot(true);
        setRoot1.setName("setRoot");
        SetTest setTest1 = new SetTest(setRoot1);
        setTest1.setName("setTest1");
        SetTest setTest2 = new SetTest(setRoot1);
        setTest2.setName("setTest2");
        SetTest setTest3 = new SetTest(setRoot1);
        setTest3.setName("setTest3");
        SetTest setTest4 = new SetTest(setRoot1);
        setTest4.setName("setTest4");

        SetRoot setRoot2 = new SetRoot(true);
        setRoot2.setName("setRoot");
        SetTest setTest21 = new SetTest(setRoot2);
        setTest21.setName("setTest21");
        SetTest setTest22 = new SetTest(setRoot2);
        setTest22.setName("setTest22");
        SetTest setTest23 = new SetTest(setRoot2);
        setTest23.setName("setTest23");
        SetTest setTest24 = new SetTest(setRoot2);
        setTest24.setName("setTest24");

        db.commit();

        Assert.assertEquals(10, countVertices());
        Assert.assertEquals(10 + 10, countEdges());

        Assert.assertEquals(8, setRoot1.getSetTest().union(setRoot2.getSetTest()).union(setRoot1.getSetTest()).size());

        db.commit();

        Assert.assertEquals(10, countVertices());
        Assert.assertEquals(10 + 10, countEdges());
    }

    @Test
    public void testUnionBag() {
        SetRoot setRoot1 = new SetRoot(true);
        setRoot1.setName("setRoot");
        SetTest setTest1 = new SetTest(setRoot1);
        setTest1.setName("setTest1");
        SetTest setTest2 = new SetTest(setRoot1);
        setTest2.setName("setTest2");
        SetTest setTest3 = new SetTest(setRoot1);
        setTest3.setName("setTest3");
        SetTest setTest4 = new SetTest(setRoot1);
        setTest4.setName("setTest4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5 + 5, countEdges());

        Assert.assertEquals(8, setRoot1.getSetTest().union(setRoot1.getSetTest().asBag()).size());

        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5 + 5, countEdges());
    }

    @Test
    public void testEquals() {
        SetRoot setRoot1 = new SetRoot(true);
        setRoot1.setName("setRoot");
        SetTest setTest1 = new SetTest(setRoot1);
        setTest1.setName("setTest1");
        SetTest setTest2 = new SetTest(setRoot1);
        setTest2.setName("setTest2");
        SetTest setTest3 = new SetTest(setRoot1);
        setTest3.setName("setTest3");
        SetTest setTest4 = new SetTest(setRoot1);
        setTest4.setName("setTest4");
        db.commit();

        TinkerSet<SetTest> other = new TumlMemorySet<SetTest>();
        other.add(setTest1);
        other.add(setTest2);
        other.add(setTest3);
        other.add(setTest4);

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5 + 5, countEdges());

        Assert.assertTrue(setRoot1.getSetTest().equals(other));

        db.commit();

        other.remove(setTest4);

        db.commit();
        Assert.assertFalse(setRoot1.getSetTest().equals(other));

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5 + 5, countEdges());
    }

    @Test
    public void testIntersection() {
        SetRoot setRoot1 = new SetRoot(true);
        setRoot1.setName("setRoot");
        SetTest setTest1 = new SetTest(setRoot1);
        setTest1.setName("setTest1");
        SetTest setTest2 = new SetTest(setRoot1);
        setTest2.setName("setTest2");
        SetTest setTest3 = new SetTest(setRoot1);
        setTest3.setName("setTest3");
        SetTest setTest4 = new SetTest(setRoot1);
        setTest4.setName("setTest4");

        db.commit();

        TinkerSet<SetTest> other = new TumlMemorySet<SetTest>();
        other.add(setTest1);
        other.add(setTest2);

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5 + 5, countEdges());

        Assert.assertEquals(2, setRoot1.getSetTest().intersection(other).size());

        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5 + 5, countEdges());
    }

    @Test
    public void testSubtract() {
        SetRoot setRoot1 = new SetRoot(true);
        setRoot1.setName("setRoot");
        SetTest setTest1 = new SetTest(setRoot1);
        setTest1.setName("setTest1");
        SetTest setTest2 = new SetTest(setRoot1);
        setTest2.setName("setTest2");
        SetTest setTest3 = new SetTest(setRoot1);
        setTest3.setName("setTest3");
        SetTest setTest4 = new SetTest(setRoot1);
        setTest4.setName("setTest4");

        SetRoot setRoot2 = new SetRoot(true);
        setRoot2.setName("setRoot");
        SetTest setTest21 = new SetTest(setRoot2);
        setTest21.setName("setTest21");
        SetTest setTest22 = new SetTest(setRoot2);
        setTest22.setName("setTest22");
        SetTest setTest23 = new SetTest(setRoot2);
        setTest23.setName("setTest23");
        SetTest setTest24 = new SetTest(setRoot2);
        setTest24.setName("setTest24");

        db.commit();

        TinkerSet<SetTest> other = new TumlMemorySet<SetTest>();
        other.add(setTest3);
        other.add(setTest4);
        other.add(setTest21);
        other.add(setTest22);

        Assert.assertEquals(10, countVertices());
        Assert.assertEquals(10 + 10, countEdges());

        TinkerSet<SetTest> subtract = setRoot1.getSetTest().union(setRoot2.getSetTest()).subtract(other);
        Assert.assertEquals(4, subtract.size());

        db.commit();

        Assert.assertEquals(10, countVertices());
        Assert.assertEquals(10 + 10, countEdges());
    }

    @Test
    public void testIncluding() {
        SetRoot setRoot1 = new SetRoot(true);
        setRoot1.setName("setRoot");
        SetTest setTest1 = new SetTest(setRoot1);
        setTest1.setName("setTest1");
        SetTest setTest2 = new SetTest(setRoot1);
        setTest2.setName("setTest2");
        SetTest setTest3 = new SetTest(setRoot1);
        setTest3.setName("setTest3");
        SetTest setTest4 = new SetTest(setRoot1);
        setTest4.setName("setTest4");

        SetRoot setRoot2 = new SetRoot(true);
        setRoot2.setName("setRoot");
        SetTest setTest21 = new SetTest(setRoot2);
        setTest21.setName("setTest21");
        SetTest setTest22 = new SetTest(setRoot2);
        setTest22.setName("setTest22");
        SetTest setTest23 = new SetTest(setRoot2);
        setTest23.setName("setTest23");
        SetTest setTest24 = new SetTest(setRoot2);
        setTest24.setName("setTest24");

        db.commit();

        TinkerSet<SetTest> other = new TumlMemorySet<SetTest>();
        other.add(setTest3);
        other.add(setTest4);
        other.add(setTest21);
        other.add(setTest22);

        Assert.assertEquals(10, countVertices());
        Assert.assertEquals(10 + 10, countEdges());

        TinkerSet<SetTest> including = setRoot1.getSetTest().including(setTest21);
        Assert.assertEquals(5, including.size());

        db.commit();

        Assert.assertEquals(10, countVertices());
        Assert.assertEquals(10 + 10, countEdges());
    }

    @Test
    public void testExcluding() {
        SetRoot setRoot1 = new SetRoot(true);
        setRoot1.setName("setRoot");
        SetTest setTest1 = new SetTest(setRoot1);
        setTest1.setName("setTest1");
        SetTest setTest2 = new SetTest(setRoot1);
        setTest2.setName("setTest2");
        SetTest setTest3 = new SetTest(setRoot1);
        setTest3.setName("setTest3");
        SetTest setTest4 = new SetTest(setRoot1);
        setTest4.setName("setTest4");

        SetRoot setRoot2 = new SetRoot(true);
        setRoot2.setName("setRoot");
        SetTest setTest21 = new SetTest(setRoot2);
        setTest21.setName("setTest21");
        SetTest setTest22 = new SetTest(setRoot2);
        setTest22.setName("setTest22");
        SetTest setTest23 = new SetTest(setRoot2);
        setTest23.setName("setTest23");
        SetTest setTest24 = new SetTest(setRoot2);
        setTest24.setName("setTest24");

        db.commit();

        TinkerSet<SetTest> other = new TumlMemorySet<SetTest>();
        other.add(setTest3);
        other.add(setTest4);
        other.add(setTest21);
        other.add(setTest22);

        Assert.assertEquals(10, countVertices());
        Assert.assertEquals(10 + 10, countEdges());

        TinkerSet<SetTest> excluding = setRoot1.getSetTest().excluding(setTest2);
        Assert.assertEquals(3, excluding.size());

        db.commit();

        Assert.assertEquals(10, countVertices());
        Assert.assertEquals(10 + 10, countEdges());
    }

    @Test
    public void testSymmetricDifference() {
        SetRoot setRoot1 = new SetRoot(true);
        setRoot1.setName("setRoot");
        SetTest setTest1 = new SetTest(setRoot1);
        setTest1.setName("setTest1");
        SetTest setTest2 = new SetTest(setRoot1);
        setTest2.setName("setTest2");
        SetTest setTest3 = new SetTest(setRoot1);
        setTest3.setName("setTest3");
        SetTest setTest4 = new SetTest(setRoot1);
        setTest4.setName("setTest4");

        SetRoot setRoot2 = new SetRoot(true);
        setRoot2.setName("setRoot");
        SetTest setTest21 = new SetTest(setRoot2);
        setTest21.setName("setTest21");
        SetTest setTest22 = new SetTest(setRoot2);
        setTest22.setName("setTest22");
        SetTest setTest23 = new SetTest(setRoot2);
        setTest23.setName("setTest23");
        SetTest setTest24 = new SetTest(setRoot2);
        setTest24.setName("setTest24");

        db.commit();

        TinkerSet<SetTest> other = new TumlMemorySet<SetTest>();
        other.add(setTest3);
        other.add(setTest4);
        other.add(setTest21);
        other.add(setTest22);

        Assert.assertEquals(10, countVertices());
        Assert.assertEquals(10 + 10, countEdges());

        TinkerSet<SetTest> symmetricDifference = setRoot1.getSetTest().symmetricDifference(other);
        Assert.assertEquals(4, symmetricDifference.size());

        boolean found1 = false;
        boolean found2 = false;
        boolean found3 = false;
        boolean found4 = false;
        for (SetTest setTest : symmetricDifference) {
            if (setTest.getName().equals("setTest1")) {
                found1 = true;
            }
            if (setTest.getName().equals("setTest2")) {
                found2 = true;
            }
            if (setTest.getName().equals("setTest21")) {
                found3 = true;
            }
            if (setTest.getName().equals("setTest22")) {
                found4 = true;
            }
        }

        Assert.assertTrue(found1 && found2 && found3 && found4);

        db.commit();

        Assert.assertEquals(10, countVertices());
        Assert.assertEquals(10 + 10, countEdges());
    }

}
