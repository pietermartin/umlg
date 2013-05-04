package org.tuml.tinker.embeddedtest;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.embeddedtest.REASON;
import org.tuml.embeddedtest.TestEmbedded;
import org.tuml.inheritencetest.Mamal;
import org.tuml.runtime.collection.memory.TumlMemoryBag;
import org.tuml.runtime.collection.memory.TumlMemoryOrderedSet;
import org.tuml.runtime.test.BaseLocalDbTest;

import java.util.Arrays;

public class TestEmbeddedTest extends BaseLocalDbTest {

    @Test
    public void testOneToManyEnum() {
        God god = new God(true);
        god.setName("THEGOD");
        god.addToREASON(REASON.GOOD);
        db.commit();
        Assert.assertEquals(2, countVertices());
        Assert.assertEquals(2 + 1, countEdges());
        God g = new God(god.getVertex());
        Assert.assertEquals(1, g.getREASON().size());
        Assert.assertEquals(REASON.GOOD, g.getREASON().iterator().next());
    }

    @Test
    public void testOneToManyEmbeddedString() {
        God god = new God(true);
        god.setName("THEGOD");
        god.addToEmbeddedString("testthis");
        db.commit();
        Assert.assertEquals(2, countVertices());
        Assert.assertEquals(2 + 1, countEdges());
        God g = new God(god.getVertex());
        Assert.assertEquals(1, g.getEmbeddedString().size());
        Assert.assertEquals("testthis", g.getEmbeddedString().iterator().next());
    }

    @Test
    public void testOneToManyEmbeddedInteger() {
        God god = new God(true);
        god.setName("THEGOD");
        god.addToEmbeddedInteger(1);
        db.commit();
        Assert.assertEquals(2, countVertices());
        Assert.assertEquals(2 + 1, countEdges());
        God g = new God(god.getVertex());
        Assert.assertEquals(1, g.getEmbeddedInteger().size());
        Assert.assertEquals(new Integer(1), g.getEmbeddedInteger().iterator().next());
    }

    @Test
    public void testOneEmbeddedEnum() {
        God god = new God(true);
        god.setName("THEGOD");
        god.setReason(REASON.GOOD);
        db.commit();
        Assert.assertEquals(2, countVertices());
        Assert.assertEquals(2 + 1, countEdges());
        God g = new God(god.getVertex());
        Assert.assertEquals(REASON.GOOD, g.getReason());
    }

    @Test
    public void testOneEmbeddedEntity() {
        God god = new God(true);
        god.setName("THEGOD");
        Mamal mamal = new Mamal(god);
        mamal.setName("PET");
        god.setPet(mamal);
        db.commit();
        Assert.assertEquals(2, countVertices());
        Assert.assertEquals(3 + 2, countEdges());
        God g = new God(god.getVertex());
        Assert.assertEquals("PET", g.getPet().getName());
    }

    @Test
    public void testManyEmbeddedEntity() {
        God god = new God(true);
        god.setName("THEGOD");
        Mamal mamal1 = new Mamal(god);
        mamal1.setName("PET1");
        Mamal mamal2 = new Mamal(god);
        mamal2.setName("PET2");
        Mamal mamal3 = new Mamal(god);
        mamal3.setName("PET3");
        Mamal mamal4 = new Mamal(god);
        mamal4.setName("PET4");
        god.addToAnimalFarm(mamal1);
        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(6 + 5, countEdges());
        god.addToAnimalFarm(mamal2);
        god.addToAnimalFarm(mamal3);
        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(8 + 5, countEdges());
        god.addToAnimalFarm(mamal2);
        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(8 + 5, countEdges());
    }

    @Test
    public void testRemoveManyEnum() {
        God god = new God(true);
        god.setName("THEGOD");
        god.addToREASON(REASON.GOOD);
        god.addToREASON(REASON.BAD);
        db.commit();
        Assert.assertEquals(3, countVertices());
        Assert.assertEquals(3 + 1, countEdges());
        God g = new God(god.getVertex());
        Assert.assertEquals(2, g.getREASON().size());
        g.removeFromREASON(REASON.GOOD);
        db.commit();
        Assert.assertEquals(2, countVertices());
        Assert.assertEquals(2 + 1, countEdges());
    }

    @Test
    public void testRemoveEmbeddedString() {
        God god = new God(true);
        god.setName("THEGOD");
        god.addToEmbeddedString("testthis");
        db.commit();
        Assert.assertEquals(2 , countVertices());
        Assert.assertEquals(2 + 1, countEdges());
        God g = new God(god.getVertex());
        Assert.assertEquals(1, g.getEmbeddedString().size());
        Assert.assertEquals("testthis", g.getEmbeddedString().iterator().next());
        g.removeFromEmbeddedString("testthis");
        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1 + 1, countEdges());
        God g2 = new God(god.getVertex());
        Assert.assertEquals(0, g2.getEmbeddedString().size());
    }

    @Test
    public void testRequiredEmbeddedManyInteger() {
        God g = new God(true);
        g.setName("ANOTHERGOD");
        org.tuml.embeddedtest.TestEmbedded testEmbedded = new org.tuml.embeddedtest.TestEmbedded(g);
        testEmbedded.setName("asd");
        db.commit();
        try {
            org.tuml.embeddedtest.TestEmbedded gt = new org.tuml.embeddedtest.TestEmbedded(testEmbedded.getVertex());
            Assert.assertEquals(new Integer(1), gt.getManyOrderedRequiredInteger().iterator().next());
            gt.clearManyOrderedRequiredInteger();
            db.commit();
        } catch (Exception e) {
            junit.framework.Assert.assertTrue(isTransactionFailedException(e));
            return;
        }
        junit.framework.Assert.fail("Expected transaction failed exception");
    }

    @Test
    public void testRequiredEmbeddedManyIntegerOrder() {
        God g = new God(true);
        g.setName("ANOTHERGOD");
        org.tuml.embeddedtest.TestEmbedded testEmbedded = new org.tuml.embeddedtest.TestEmbedded(g);
        testEmbedded.setName("asd");
        db.commit();

        org.tuml.embeddedtest.TestEmbedded gt = new org.tuml.embeddedtest.TestEmbedded(testEmbedded.getVertex());
        Assert.assertEquals(3, gt.getManyOrderedRequiredInteger().size());
        Assert.assertEquals(Integer.valueOf(1), gt.getManyOrderedRequiredInteger().get(0));
        Assert.assertEquals(Integer.valueOf(2), gt.getManyOrderedRequiredInteger().get(1));
        Assert.assertEquals(Integer.valueOf(3), gt.getManyOrderedRequiredInteger().get(2));
        db.commit();
        gt.getManyOrderedRequiredInteger().add(2, 4);
        db.commit();
        Assert.assertEquals(4, gt.getManyOrderedRequiredInteger().size());
        Assert.assertEquals(Integer.valueOf(1), gt.getManyOrderedRequiredInteger().get(0));
        Assert.assertEquals(Integer.valueOf(2), gt.getManyOrderedRequiredInteger().get(1));
        Assert.assertEquals(Integer.valueOf(4), gt.getManyOrderedRequiredInteger().get(2));
        Assert.assertEquals(Integer.valueOf(3), gt.getManyOrderedRequiredInteger().get(3));
    }

    @Test
    public void testRequiredOrederedEmbeddedManyString() {
        God g = new God(true);
        g.setName("ANOTHERGOD");
        org.tuml.embeddedtest.TestEmbedded testEmbedded = new org.tuml.embeddedtest.TestEmbedded(g);
        testEmbedded.setName("asd");
        db.commit();
        org.tuml.embeddedtest.TestEmbedded gt = new org.tuml.embeddedtest.TestEmbedded(testEmbedded.getVertex());
        Assert.assertEquals("a", gt.getManyRequiredOrderedUniqueString().get(0));
        Assert.assertEquals("b", gt.getManyRequiredOrderedUniqueString().get(1));
        Assert.assertEquals("c", gt.getManyRequiredOrderedUniqueString().get(2));
        db.commit();
        gt.getManyRequiredOrderedUniqueString().add(2, "d");
        db.commit();
        Assert.assertEquals("a", gt.getManyRequiredOrderedUniqueString().get(0));
        Assert.assertEquals("b", gt.getManyRequiredOrderedUniqueString().get(1));
        Assert.assertEquals("d", gt.getManyRequiredOrderedUniqueString().get(2));
        Assert.assertEquals("c", gt.getManyRequiredOrderedUniqueString().get(3));

        gt.addToManyRequiredOrderedUniqueString("a");
        db.commit();
        Assert.assertEquals(4, gt.getManyRequiredOrderedUniqueString().size());
        Assert.assertEquals("a", gt.getManyRequiredOrderedUniqueString().get(0));
        Assert.assertEquals("b", gt.getManyRequiredOrderedUniqueString().get(1));
        Assert.assertEquals("d", gt.getManyRequiredOrderedUniqueString().get(2));
        Assert.assertEquals("c", gt.getManyRequiredOrderedUniqueString().get(3));
    }

    @Test
    public void testManyBoolean() {
        God g = new God(true);
        g.setName("ANOTHERGOD");
        TestEmbedded testEmbedded = new TestEmbedded(g);
        testEmbedded.setName("asd");
        db.commit();
        testEmbedded.addToManyBoolean(true);
        testEmbedded.addToManyBoolean(false);
        db.commit();
        TestEmbedded testEmbeddedX = new TestEmbedded(testEmbedded.getVertex());
        Assert.assertEquals(2, testEmbeddedX.getManyBoolean().size());
        testEmbeddedX.setManyBoolean(new TumlMemoryBag<Boolean>(Arrays.asList(new Boolean[]{true, true})));
        db.commit();
        TestEmbedded testEmbeddedY = new TestEmbedded(testEmbeddedX.getVertex());
        Assert.assertEquals(2, testEmbeddedY.getManyBoolean().size());
    }

    @Test
    public void testManyBooleanBagJol() {
        God g = new God(true);
        g.setName("ANOTHERGOD");
        TestEmbedded testEmbedded = new TestEmbedded(g);
        testEmbedded.setName("asd");
        db.commit();
        testEmbedded.addToManyBoolean(true);
        testEmbedded.addToManyBoolean(true);
        db.commit();
        TestEmbedded testEmbeddedX = new TestEmbedded(testEmbedded.getVertex());
        Assert.assertEquals(2, testEmbeddedX.getManyBoolean().size());
        testEmbeddedX.setManyBoolean(new TumlMemoryBag<Boolean>(Arrays.asList(new Boolean[]{false, false})));
        db.commit();
        TestEmbedded testEmbeddedY = new TestEmbedded(testEmbeddedX.getVertex());
        Assert.assertEquals(2, testEmbeddedY.getManyBoolean().size());
        boolean b1 = testEmbeddedY.getManyBoolean().asSequence().at(0);
        boolean b2 = testEmbeddedY.getManyBoolean().asSequence().at(0);
        Assert.assertSame(false, b1);
        Assert.assertSame(false, b2);
    }

    @Test
    public void testEmbeddedVertexAlreadyRemoved() {
        God god = new God(true);
        god.setName("name");
        TestEmbedded testEmbedded = new TestEmbedded(god);
        testEmbedded.setName("name");
        testEmbedded.addToManyOrderedString("a");
        testEmbedded.addToManyOrderedString("b");
        testEmbedded.addToManyOrderedString("c");
        testEmbedded.clearManyOrderedString();
        testEmbedded.setManyRequiredOrderedUniqueString(new TumlMemoryOrderedSet<String>(Arrays.asList(new String[]{"a", "b"})));
        db.commit();

    }

}
