package org.umlg.tests.embeddedtest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.embeddedtest.REASON;
import org.umlg.embeddedtest.TestEmbedded;
import org.umlg.inheritencetest.Mamal;
import org.umlg.runtime.collection.memory.UmlgMemoryBag;
import org.umlg.runtime.collection.memory.UmlgMemoryOrderedSet;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

import java.util.Arrays;
import java.util.Iterator;

public class TestEmbeddedTest extends BaseLocalDbTest {

    @Test
    public void testOneToManyEnum() {
        God god = new God(true);
        god.setName("THEGOD");
        god.addToREASON(REASON.GOOD);
        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1, countEdges());
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
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1, countEdges());
        God g = new God(god.getVertex());
        Assert.assertEquals(1, g.getEmbeddedString().size());
        Assert.assertEquals("testthis", g.getEmbeddedString().iterator().next());
    }

    @Test
    public void testOneToManyEmbeddedStringWithMany() {
        God god = new God(true);
        god.setName("THEGOD");
        god.addToEmbeddedString("testthis1");
        god.addToEmbeddedString("testthis2");
        god.addToEmbeddedString("testthis3");
        db.commit();
        god.reload();
        Assert.assertEquals(3, god.getEmbeddedString().size());
    }

    @Test
    public void testOneToManyEmbeddedInteger() {
        God god = new God(true);
        god.setName("THEGOD");
        god.addToEmbeddedInteger(1);
        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1, countEdges());
        God g = new God(god.getVertex());
        Assert.assertEquals(1, g.getEmbeddedInteger().size());
        Assert.assertEquals(new Integer(1), g.getEmbeddedInteger().iterator().next());
    }

    @Test
    public void testOneToManyEmbeddedInteger_Again() {
        God god = new God(true);
        god.setName("THEGOD");
        god.addToEmbeddedInteger(1);
        god.addToEmbeddedInteger(2);
        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1, countEdges());
        God g = new God(god.getVertex());
        Assert.assertEquals(2, g.getEmbeddedInteger().size());
        Iterator<Integer> iterator = g.getEmbeddedInteger().iterator();
        Assert.assertEquals(new Integer(1), iterator.next());
        Assert.assertEquals(new Integer(2), iterator.next());
    }

    @Test
    public void testOneEmbeddedEnum() {
        God god = new God(true);
        god.setName("THEGOD");
        god.setReason(REASON.GOOD);
        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1, countEdges());
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
        Assert.assertEquals(3, countEdges());
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
        Assert.assertEquals(6, countEdges());
        god.addToAnimalFarm(mamal2);
        god.addToAnimalFarm(mamal3);
        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(8, countEdges());
        god.addToAnimalFarm(mamal2);
        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(8, countEdges());
    }

    @Test
    public void testRemoveManyEnum() {
        God god = new God(true);
        god.setName("THEGOD");
        god.addToREASON(REASON.GOOD);
        god.addToREASON(REASON.BAD);
        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1, countEdges());
        God g = new God(god.getVertex());
        Assert.assertEquals(2, g.getREASON().size());
        g.removeFromREASON(REASON.GOOD);
        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1, countEdges());
    }

    @Test
    public void testRemoveEmbeddedString() {
        God god = new God(true);
        god.setName("THEGOD");
        god.addToEmbeddedString("testthis");
        db.commit();
        Assert.assertEquals(1 , countVertices());
        Assert.assertEquals(1, countEdges());
        God g = new God(god.getVertex());
        Assert.assertEquals(1, g.getEmbeddedString().size());
        Assert.assertEquals("testthis", g.getEmbeddedString().iterator().next());
        g.removeFromEmbeddedString("testthis");
        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1, countEdges());
        God g2 = new God(god.getVertex());
        Assert.assertEquals(0, g2.getEmbeddedString().size());
    }

    @Test
    public void testRequiredEmbeddedManyInteger() {
        God g = new God(true);
        g.setName("ANOTHERGOD");
        TestEmbedded testEmbedded = new TestEmbedded(g);
        testEmbedded.setName("asd");
        db.commit();
        try {
            TestEmbedded gt = new TestEmbedded(testEmbedded.getVertex());
            Assert.assertEquals(new Integer(1), gt.getManyOrderedRequiredInteger().iterator().next());
            gt.clearManyOrderedRequiredInteger();
            db.commit();
        } catch (Exception e) {
            Assert.assertTrue("expecting UmlgConstraintViolationException", e instanceof UmlgConstraintViolationException);
            return;
        }
        Assert.fail("Expected transaction failed exception");
    }

    @Test
    public void testRequiredEmbeddedManyIntegerOrder() {
        God g = new God(true);
        g.setName("ANOTHERGOD");
        TestEmbedded testEmbedded = new TestEmbedded(g);
        testEmbedded.setName("asd");
        db.commit();

        TestEmbedded gt = new TestEmbedded(testEmbedded.getVertex());
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

        //Start testing duplicates
        gt.getManyOrderedRequiredInteger().add(2);
        db.commit();
        gt.reload();
        Assert.assertEquals(5, gt.getManyOrderedRequiredInteger().size());

        //Now remove both numbers 4
        gt.reload();
        gt.removeFromManyOrderedRequiredInteger(2);
        gt.removeFromManyOrderedRequiredInteger(2);
        db.commit();
        gt.reload();
        Assert.assertEquals(3, gt.getManyOrderedRequiredInteger().size());
        gt.reload();
        gt.clearManyOrderedRequiredInteger();
        gt.addToManyOrderedRequiredInteger(1);
        gt.addToManyOrderedRequiredInteger(2);
        gt.addToManyOrderedRequiredInteger(3);
        db.commit();
        gt.reload();
        Assert.assertEquals(3, gt.getManyOrderedRequiredInteger().size());
    }

    @Test
    public void testRequiredOrderedEmbeddedManyString() {
        God g = new God(true);
        g.setName("ANOTHERGOD");
        TestEmbedded testEmbedded = new TestEmbedded(g);
        testEmbedded.setName("asd");
        db.commit();
        TestEmbedded gt = new TestEmbedded(testEmbedded.getVertex());
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
        testEmbeddedX.setManyBoolean(new UmlgMemoryBag<>(Arrays.asList(true, true)));
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
        testEmbeddedX.setManyBoolean(new UmlgMemoryBag<>(Arrays.asList(false, false)));
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
        testEmbedded.setManyRequiredOrderedUniqueString(new UmlgMemoryOrderedSet<>(Arrays.asList("a", "b")));
        db.commit();
    }

}
