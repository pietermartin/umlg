package org.umlg.tests.collectiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.SequenceRoot;
import org.umlg.collectiontest.SequenceTestOrderedSet;
import org.umlg.collectiontest.World;
import org.umlg.concretetest.God;
import org.umlg.embeddedtest.TestEmbedded;
import org.umlg.runtime.collection.memory.UmlgMemorySequence;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.Arrays;

public class OrderedSetTestTest extends BaseLocalDbTest {

    @Test
    public void testOrderedSetIsUnique() {
        God god = new God(true);
        god.setName("THEGOD");
        World world1 = new World(god);
        world1.setName("world1");
        god.getWorld().add(world1);
        db.commit();
        God godTest = new God(god.getVertex());
        Assert.assertEquals(1, godTest.getWorld().size());
    }

    @Test
    public void testOrderedSetIsOrdered() {
        God god = new God(true);
        god.setName("THEGOD");
        World world1 = new World(god);
        world1.setName("world1");
        World world2 = new World(god);
        world2.setName("world2");
        World world3 = new World(god);
        world3.setName("world3");
        World world4 = new World(god);
        world4.setName("world4");
        db.commit();
        God godTest = new God(god.getVertex());
        Assert.assertEquals(4, godTest.getWorld().size());
        Assert.assertEquals("world1", godTest.getWorld().get(0).getName());
        Assert.assertEquals("world2", godTest.getWorld().get(1).getName());
        Assert.assertEquals("world3", godTest.getWorld().get(2).getName());
        Assert.assertEquals("world4", godTest.getWorld().get(3).getName());
        God godTest2 = new God(god.getVertex());
        World world5 = new World(true);
        world5.setName("world5");
        godTest2.getWorld().add(2, world5);
        db.commit();
        God godTest3 = new God(god.getVertex());
        Assert.assertEquals(5, godTest3.getWorld().size());
        Assert.assertEquals("world1", godTest3.getWorld().get(0).getName());
        Assert.assertEquals("world2", godTest3.getWorld().get(1).getName());
        Assert.assertEquals("world5", godTest3.getWorld().get(2).getName());
        Assert.assertEquals("world3", godTest3.getWorld().get(3).getName());
        Assert.assertEquals("world4", godTest3.getWorld().get(4).getName());
    }

    @Test
    public void testChangeOrderOnEmbedded() {
        God g = new God(true);
        g.setName("GOD");
        TestEmbedded embedded = new TestEmbedded(g);
        embedded.setName("asd");
        embedded.addToManyOrderedRequiredInteger(9);
        embedded.addToManyOrderedRequiredInteger(8);
        embedded.addToManyOrderedRequiredInteger(7);
        db.commit();
        TestEmbedded test = new TestEmbedded(embedded.getVertex());

        //3 are created by initial value
        Assert.assertEquals(6, test.getManyOrderedRequiredInteger().size());

        test.setManyOrderedRequiredInteger(new UmlgMemorySequence<Integer>(Arrays.asList(3, 2, 1)));
        Assert.assertEquals(3, test.getManyOrderedRequiredInteger().size());

        Assert.assertEquals(Integer.valueOf(3), test.getManyOrderedRequiredInteger().get(0));
        Assert.assertEquals(Integer.valueOf(2), test.getManyOrderedRequiredInteger().get(1));
        Assert.assertEquals(Integer.valueOf(1), test.getManyOrderedRequiredInteger().get(2));

    }

    @Test
    public void testRemoval() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestOrderedSet sequenceTestOrderedSet1 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet1.setName("sequenceTestOrderedSet1");
        db.commit();

        Assert.assertEquals(2, countVertices());
        Assert.assertEquals(4 + 2, countEdges());

        sequenceRoot.getSequenceTestOrderedSet().remove(0);
        sequenceTestOrderedSet1.delete();
        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1 + 1, countEdges());
    }

    @Test
    public void testRemoval2() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestOrderedSet sequenceTestOrderedSet1 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet1.setName("sequenceTestOrderedSet1");
        SequenceTestOrderedSet sequenceTestOrderedSet2 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet2.setName("sequenceTestOrderedSet2");
        SequenceTestOrderedSet sequenceTestOrderedSet3 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet3.setName("sequenceTestOrderedSet3");
        SequenceTestOrderedSet sequenceTestOrderedSet4 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet4.setName("sequenceTestOrderedSet4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(10 + 5, countEdges());

        sequenceRoot.getSequenceTestOrderedSet().remove(0);
        sequenceTestOrderedSet1.delete();
        db.commit();
        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8 + 4, countEdges());
    }

    @Test
    public void testRemoval3() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestOrderedSet sequenceTestOrderedSet1 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet1.setName("sequenceTestOrderedSet1");
        SequenceTestOrderedSet sequenceTestOrderedSet2 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet2.setName("sequenceTestOrderedSet2");
        SequenceTestOrderedSet sequenceTestOrderedSet3 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet3.setName("sequenceTestOrderedSet3");
        SequenceTestOrderedSet sequenceTestOrderedSet4 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet4.setName("sequenceTestOrderedSet4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(10 + 5, countEdges());

        sequenceRoot.getSequenceTestOrderedSet().remove(1);
        sequenceTestOrderedSet2.delete();
        db.commit();
        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8 + 4, countEdges());
    }

    @Test
    public void testRemoval4() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestOrderedSet sequenceTestOrderedSet1 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet1.setName("sequenceTestOrderedSet1");
        SequenceTestOrderedSet sequenceTestOrderedSet2 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet2.setName("sequenceTestOrderedSet2");
        SequenceTestOrderedSet sequenceTestOrderedSet3 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet3.setName("sequenceTestOrderedSet3");
        SequenceTestOrderedSet sequenceTestOrderedSet4 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet4.setName("sequenceTestOrderedSet4");
        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(10 + 5, countEdges());

        sequenceRoot.getSequenceTestOrderedSet().remove(3);
        sequenceTestOrderedSet4.delete();
        db.commit();
        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8 + 4, countEdges());
    }

    @Test
    public void testAddAtIndex1() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestOrderedSet sequenceTestOrderedSet1 = new SequenceTestOrderedSet(true);
        sequenceTestOrderedSet1.setName("sequenceTestOrderedSet1");
        sequenceRoot.getSequenceTestOrderedSet().add(0, sequenceTestOrderedSet1);
        SequenceTestOrderedSet sequenceTestOrderedSet2 = new SequenceTestOrderedSet(true);
        sequenceTestOrderedSet2.setName("sequenceTestOrderedSet2");
        sequenceRoot.getSequenceTestOrderedSet().add(0, sequenceTestOrderedSet2);
        SequenceTestOrderedSet sequenceTestOrderedSet3 = new SequenceTestOrderedSet(true);
        sequenceTestOrderedSet3.setName("sequenceTestOrderedSet3");
        sequenceRoot.getSequenceTestOrderedSet().add(2, sequenceTestOrderedSet3);
        db.commit();
        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8 + 4, countEdges());
        Assert.assertEquals("sequenceTestOrderedSet2", sequenceRoot.getSequenceTestOrderedSet().get(0).getName());
        Assert.assertEquals("sequenceTestOrderedSet1", sequenceRoot.getSequenceTestOrderedSet().get(1).getName());
        Assert.assertEquals("sequenceTestOrderedSet3", sequenceRoot.getSequenceTestOrderedSet().get(2).getName());
        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
        Assert.assertEquals("sequenceTestOrderedSet2", sequenceRoot.getSequenceTestOrderedSet().get(0).getName());
        Assert.assertEquals("sequenceTestOrderedSet1", sequenceRoot.getSequenceTestOrderedSet().get(1).getName());
        Assert.assertEquals("sequenceTestOrderedSet3", sequenceRoot.getSequenceTestOrderedSet().get(2).getName());
        db.commit();
    }

    @Test
    public void testSetAtIndex1() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTestOrderedSet sequenceTestOrderedSet1 = new SequenceTestOrderedSet(true);
        sequenceTestOrderedSet1.setName("sequenceTestOrderedSet1");
        sequenceRoot.getSequenceTestOrderedSet().add(0, sequenceTestOrderedSet1);
        SequenceTestOrderedSet sequenceTestOrderedSet2 = new SequenceTestOrderedSet(true);
        sequenceTestOrderedSet2.setName("sequenceTestOrderedSet2");
        sequenceRoot.getSequenceTestOrderedSet().add(1, sequenceTestOrderedSet2);
        SequenceTestOrderedSet sequenceTestOrderedSet3 = new SequenceTestOrderedSet(true);
        sequenceTestOrderedSet3.setName("sequenceTestOrderedSet3");
        sequenceRoot.getSequenceTestOrderedSet().add(2, sequenceTestOrderedSet3);
        db.commit();
        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8 + 4, countEdges());
        Assert.assertEquals("sequenceTestOrderedSet1", sequenceRoot.getSequenceTestOrderedSet().get(0).getName());
        Assert.assertEquals("sequenceTestOrderedSet2", sequenceRoot.getSequenceTestOrderedSet().get(1).getName());
        Assert.assertEquals("sequenceTestOrderedSet3", sequenceRoot.getSequenceTestOrderedSet().get(2).getName());
        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
        Assert.assertEquals("sequenceTestOrderedSet1", sequenceRoot.getSequenceTestOrderedSet().get(0).getName());
        Assert.assertEquals("sequenceTestOrderedSet2", sequenceRoot.getSequenceTestOrderedSet().get(1).getName());
        Assert.assertEquals("sequenceTestOrderedSet3", sequenceRoot.getSequenceTestOrderedSet().get(2).getName());

        SequenceTestOrderedSet sequenceTestOrderedSet4 = new SequenceTestOrderedSet(true);
        sequenceTestOrderedSet4.setName("sequenceTestOrderedSet4");
        SequenceTestOrderedSet removed = sequenceRoot.getSequenceTestOrderedSet().set(1, sequenceTestOrderedSet4);
        Assert.assertEquals(removed, sequenceTestOrderedSet2);
        removed.delete();
        db.commit();

        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8 + 4, countEdges());
        Assert.assertEquals("sequenceTestOrderedSet1", sequenceRoot.getSequenceTestOrderedSet().get(0).getName());
        Assert.assertEquals("sequenceTestOrderedSet4", sequenceRoot.getSequenceTestOrderedSet().get(1).getName());
        Assert.assertEquals("sequenceTestOrderedSet3", sequenceRoot.getSequenceTestOrderedSet().get(2).getName());
        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
        Assert.assertEquals("sequenceTestOrderedSet1", sequenceRoot.getSequenceTestOrderedSet().get(0).getName());
        Assert.assertEquals("sequenceTestOrderedSet4", sequenceRoot.getSequenceTestOrderedSet().get(1).getName());
        Assert.assertEquals("sequenceTestOrderedSet3", sequenceRoot.getSequenceTestOrderedSet().get(2).getName());

    }

}