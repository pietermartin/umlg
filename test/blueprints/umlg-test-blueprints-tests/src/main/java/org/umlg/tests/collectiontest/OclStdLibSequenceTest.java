package org.umlg.tests.collectiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.SequenceRoot;
import org.umlg.collectiontest.SequenceTestListMany;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.memory.UmlgMemorySequence;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/03/03
 * Time: 10:13 AM
 */
public class OclStdLibSequenceTest extends BaseLocalDbTest {

    @Test
    public void testEquals() {
        UmlgSequence<SequenceTestListMany> testListPass = new UmlgMemorySequence<SequenceTestListMany>();
        UmlgSequence<SequenceTestListMany> testListFail = new UmlgMemorySequence<SequenceTestListMany>();
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();
        testListPass.add(sequenceTestListMany1);
        testListPass.add(sequenceTestListMany2);
        testListPass.add(sequenceTestListMany3);
        testListPass.add(sequenceTestListMany4);

        SequenceTestListMany sequenceTestListMany5 = new SequenceTestListMany(true);
        sequenceTestListMany5.setName("sequenceTestListMany5");

        testListFail.add(sequenceTestListMany1);
        testListFail.add(sequenceTestListMany2);
        testListFail.add(sequenceTestListMany5);
        testListFail.add(sequenceTestListMany4);

        Assert.assertTrue(sequenceRoot.getSequenceTestListMany().equals(testListPass));
        Assert.assertFalse(sequenceRoot.getSequenceTestListMany().equals(testListFail));
    }

    @Test
    public void testUnion() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        SequenceRoot sequenceRoot1 = new SequenceRoot(true);
        sequenceRoot1.setName("sequenceRoot1");
        SequenceTestListMany sequenceTestListMany5 = new SequenceTestListMany(sequenceRoot1);
        sequenceTestListMany5.setName("sequenceTestListMany5");
        SequenceTestListMany sequenceTestListMany6 = new SequenceTestListMany(sequenceRoot1);
        sequenceTestListMany6.setName("sequenceTestListMany6");
        SequenceTestListMany sequenceTestListMany7 = new SequenceTestListMany(sequenceRoot1);
        sequenceTestListMany7.setName("sequenceTestListMany7");
        SequenceTestListMany sequenceTestListMany8 = new SequenceTestListMany(sequenceRoot1);
        sequenceTestListMany8.setName("sequenceTestListMany8");

        db.commit();
        Assert.assertEquals(10, countVertices());
        Assert.assertEquals(10, countEdges());

        Assert.assertEquals(8, sequenceRoot.getSequenceTestListMany().union(sequenceRoot1.getSequenceTestListMany()).size());
        //This checks that the ocl is side effect free
        Assert.assertEquals(10, countVertices());
        Assert.assertEquals(10, countEdges());


    }

    @Test
    public void testAppend() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        SequenceTestListMany sequenceTestListMany5 = new SequenceTestListMany(true);
        sequenceTestListMany5.setName("sequenceTestListMany5");

        UmlgSequence<SequenceTestListMany> result = sequenceRoot.getSequenceTestListMany().append(sequenceTestListMany5);
        Assert.assertEquals(5, result.size());
        Assert.assertEquals("sequenceTestListMany5", result.get(4).getName());

    }

    @Test
    public void testPrepend() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        SequenceTestListMany sequenceTestListMany5 = new SequenceTestListMany(true);
        sequenceTestListMany5.setName("sequenceTestListMany5");

        UmlgSequence<SequenceTestListMany> result = sequenceRoot.getSequenceTestListMany().prepend(sequenceTestListMany5);
        Assert.assertEquals(5, result.size());
        Assert.assertEquals("sequenceTestListMany5", result.get(0).getName());
    }

    @Test
    public void testInsertAt() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        SequenceTestListMany sequenceTestListMany5 = new SequenceTestListMany(true);
        sequenceTestListMany5.setName("sequenceTestListMany5");

        UmlgSequence<SequenceTestListMany> result = sequenceRoot.getSequenceTestListMany().insertAt(2, sequenceTestListMany5);
        Assert.assertEquals(5, result.size());
        Assert.assertEquals("sequenceTestListMany5", result.get(2).getName());
    }

    @Test
    public void testSubSequence() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        UmlgSequence<SequenceTestListMany> result = sequenceRoot.getSequenceTestListMany().subSequence(2, 3);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("sequenceTestListMany3", result.get(0).getName());
        Assert.assertEquals("sequenceTestListMany4", result.get(1).getName());
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());
    }

    @Test
    public void testAt() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        SequenceTestListMany result = sequenceRoot.getSequenceTestListMany().at(2);
        Assert.assertNotNull(result);
        Assert.assertEquals("sequenceTestListMany3", result.getName());
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());
    }

    @Test
    public void testIndexOf() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        Assert.assertEquals(2, sequenceRoot.getSequenceTestListMany().indexOf(sequenceTestListMany3));

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());
    }

    @Test
    public void testFirst() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        Assert.assertEquals(sequenceTestListMany1, sequenceRoot.getSequenceTestListMany().first());

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());
    }

    @Test
    public void testLast() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        Assert.assertEquals(sequenceTestListMany4, sequenceRoot.getSequenceTestListMany().last());

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());
    }

    @Test
    public void testIncluding() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        SequenceTestListMany sequenceTestListMany5 = new SequenceTestListMany(true);
        sequenceTestListMany5.setName("sequenceTestListMany5");

        UmlgSequence<SequenceTestListMany> result = sequenceRoot.getSequenceTestListMany().including(sequenceTestListMany5);
        Assert.assertEquals(5, result.size());
        Assert.assertEquals("sequenceTestListMany5", result.get(4).getName());

    }

    @Test
    public void testExcluding() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        UmlgSequence<SequenceTestListMany> result = sequenceRoot.getSequenceTestListMany().excluding(sequenceTestListMany3);
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("sequenceTestListMany1", result.get(0).getName());
        Assert.assertEquals("sequenceTestListMany2", result.get(1).getName());
        Assert.assertEquals("sequenceTestListMany4", result.get(2).getName());

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());
    }

    @Test
    public void testReverse() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany1.setName("sequenceTestListMany1");
        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany2.setName("sequenceTestListMany2");
        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany3.setName("sequenceTestListMany3");
        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
        sequenceTestListMany4.setName("sequenceTestListMany4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        UmlgSequence<SequenceTestListMany> result = sequenceRoot.getSequenceTestListMany().reverse();
        Assert.assertEquals(4, result.size());
        Assert.assertEquals("sequenceTestListMany1", result.get(3).getName());
        Assert.assertEquals("sequenceTestListMany2", result.get(2).getName());
        Assert.assertEquals("sequenceTestListMany3", result.get(1).getName());
        Assert.assertEquals("sequenceTestListMany4", result.get(0).getName());

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());
    }

}
