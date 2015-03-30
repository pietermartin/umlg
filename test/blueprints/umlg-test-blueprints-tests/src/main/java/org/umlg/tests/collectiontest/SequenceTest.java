package org.umlg.tests.collectiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.*;
import org.umlg.concretetest.God;
import org.umlg.qualifiertest.Many1;
import org.umlg.qualifiertest.Many2;
import org.umlg.runtime.collection.memory.UmlgMemorySequence;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequenceTest extends BaseLocalDbTest {

//    @Test
//    public void testSequence() {
//        God god = new God(true);
//        god.setName("THEGOD");
//        Hand hand = new Hand(god);
//        hand.setLeft(true);
//        hand.setName("hand1");
//        Hand hand2 = new Hand(god);
//        hand2.setLeft(true);
//        hand2.setName("hand2");
//        Hand hand3 = new Hand(god);
//        hand3.setLeft(true);
//        hand3.setName("hand3");
//        Hand hand4 = new Hand(god);
//        hand4.setLeft(true);
//        hand4.setName("hand4");
//        db.commit();
//        God godTest = new God(god.getVertex());
//        Assert.assertEquals(4, godTest.getHand().size());
//        Assert.assertEquals("hand4", godTest.getHand().get(3).getName());
//        Assert.assertEquals("hand3", godTest.getHand().get(2).getName());
//        Assert.assertEquals("hand2", godTest.getHand().get(1).getName());
//        Assert.assertEquals("hand1", godTest.getHand().get(0).getName());
//    }
//
//    @Test
//    public void testClear() {
//        God god = new God(true);
//        god.setName("THEGOD");
//        Hand hand1 = new Hand(true);
//        hand1.setLeft(true);
//        hand1.setName("hand1");
//        Hand hand2 = new Hand(true);
//        hand2.setLeft(true);
//        hand2.setName("hand2");
//        Hand hand3 = new Hand(true);
//        hand3.setLeft(true);
//        hand3.setName("hand3");
//        Hand hand4 = new Hand(true);
//        hand4.setLeft(true);
//        hand4.setName("hand4");
//        god.setHand(new UmlgMemorySequence<Hand>(Arrays.asList(new Hand[]{hand1, hand2, hand3, hand4})));
//        db.commit();
//
//        Assert.assertEquals(9, countVertices());
//        Assert.assertEquals(14, countEdges());
//
//        god.getHand().clear();
//
//        Assert.assertTrue(god.getHand().isEmpty());
//        Assert.assertNull(hand1.getGod());
//        Assert.assertNull(hand2.getGod());
//        Assert.assertNull(hand3.getGod());
//        Assert.assertNull(hand4.getGod());
//
//        Exception transactionFailed = null;
//        try {
//            db.commit();
//        } catch (Exception e) {
//            db.rollback();
//            transactionFailed = e;
//        }
//        Assert.assertNotNull(transactionFailed);
//        Assert.assertTrue("excepting UmlgConstraintViolationException", transactionFailed instanceof UmlgConstraintViolationException);
//    }
//
//    @Test
//    public void testControllingSide() {
//        God god = new God(true);
//        god.setName("THEGOD");
//        Hand hand1 = new Hand(true);
//        hand1.setLeft(true);
//        hand1.setName("hand1");
//        Hand hand2 = new Hand(true);
//        hand2.setLeft(true);
//        hand2.setName("hand2");
//        Hand hand3 = new Hand(true);
//        hand3.setLeft(true);
//        hand3.setName("hand3");
//        Hand hand4 = new Hand(true);
//        hand4.setLeft(true);
//        hand4.setName("hand4");
//        god.setHand(new UmlgMemorySequence<Hand>(Arrays.asList(new Hand[]{hand1, hand2, hand3, hand4})));
//        db.commit();
//
//        Assert.assertEquals(9, countVertices());
//        Assert.assertEquals(14, countEdges());
//
//        Hand hand5 = new Hand(true);
//        hand5.setLeft(true);
//        hand5.setName("hand5");
//        Hand hand6 = new Hand(true);
//        hand6.setLeft(true);
//        hand6.setName("hand6");
//
//        god.setHand(new UmlgMemorySequence<Hand>(Arrays.asList(new Hand[]{hand5, hand6})));
//
//        hand1.delete();
//        hand2.delete();
//        hand3.delete();
//        hand4.delete();
//
//        db.commit();
//        Assert.assertEquals(5, countVertices());
//        Assert.assertEquals(8, countEdges());
//
//        God gTest = new God(god.getVertex());
//        gTest.getHand().get(1);
//        boolean illegalStateException = false;
//        try {
//            Hand testHand = new Hand(hand2.getVertex());
//            testHand.getName();
//        } catch (Exception e) {
//            illegalStateException = true;
//        }
//        Assert.assertTrue(illegalStateException);
//        db.rollback();
//
//    }
//
//    @Test
//    public void testSequenceMaintainsOrder() {
//        God god = new God(true);
//        god.setName("THEGOD");
//        Hand hand = new Hand(god);
//        hand.setLeft(true);
//        hand.setName("hand1");
//        Hand hand2 = new Hand(god);
//        hand2.setLeft(true);
//        hand2.setName("hand2");
//        Hand hand3 = new Hand(god);
//        hand3.setLeft(true);
//        hand3.setName("hand3");
//        Hand hand4 = new Hand(god);
//        hand4.setLeft(true);
//        hand4.setName("hand4");
//        db.commit();
//        God godTest = new God(god.getVertex());
//        Assert.assertEquals(4, godTest.getHand().size());
//        godTest.getHand().get(3).getName().equals("hand4");
//        godTest.getHand().get(2).getName().equals("hand3");
//        godTest.getHand().get(1).getName().equals("hand2");
//        godTest.getHand().get(0).getName().equals("hand1");
//
//        Hand hand1_5 = new Hand(true);
//        hand1_5.setLeft(true);
//        hand1_5.setName("hand1_5");
//        god.getHand().add(1, hand1_5);
//        db.commit();
//
//        God godTest1 = new God(god.getVertex());
//        Assert.assertTrue(godTest1.getHand().get(4).getName().equals("hand4"));
//        Assert.assertTrue(godTest1.getHand().get(3).getName().equals("hand3"));
//        Assert.assertTrue(godTest1.getHand().get(2).getName().equals("hand2"));
//        Assert.assertTrue(godTest1.getHand().get(1).getName().equals("hand1_5"));
//        Assert.assertTrue(godTest1.getHand().get(0).getName().equals("hand1"));
//        Assert.assertEquals(5, godTest1.getHand().size());
//
//        God godTest2 = new God(god.getVertex());
//        int i = 0;
//        for (Hand handTest2 : godTest2.getHand()) {
//            if (i == 0) {
//                Assert.assertTrue(handTest2.getName().equals("hand1"));
//            } else if (i == 1) {
//                Assert.assertTrue(handTest2.getName().equals("hand1_5"));
//            } else if (i == 2) {
//                Assert.assertTrue(handTest2.getName().equals("hand2"));
//            } else if (i == 3) {
//                Assert.assertTrue(handTest2.getName().equals("hand3"));
//            } else if (i == 4) {
//                Assert.assertTrue(handTest2.getName().equals("hand4"));
//            }
//            i++;
//        }
//
//    }
//
//    @Test
//    public void testSequenceAddAll1() {
//        God god = new God(true);
//        god.setName("THEGOD");
//        Hand hand = new Hand(god);
//        hand.setLeft(true);
//        hand.setName("hand1");
//        Hand hand2 = new Hand(god);
//        hand2.setLeft(true);
//        hand2.setName("hand2");
//        Hand hand3 = new Hand(god);
//        hand3.setLeft(true);
//        hand3.setName("hand3");
//        Hand hand4 = new Hand(god);
//        hand4.setLeft(true);
//        hand4.setName("hand4");
//        db.commit();
//        God godTest = new God(god.getVertex());
//        Assert.assertEquals(4, godTest.getHand().size());
//
//        Hand hand5 = new Hand(true);
//        hand5.setLeft(true);
//        hand5.setName("hand5");
//        Hand hand6 = new Hand(true);
//        hand6.setLeft(true);
//        hand6.setName("hand6");
//        Hand hand7 = new Hand(true);
//        hand7.setLeft(true);
//        hand7.setName("hand7");
//        Hand hand8 = new Hand(true);
//        hand8.setLeft(true);
//        hand8.setName("hand8");
//        List<Hand> moreHands = new ArrayList<Hand>(4);
//        moreHands.add(hand5);
//        moreHands.add(hand6);
//        moreHands.add(hand7);
//        moreHands.add(hand8);
//        god.getHand().addAll(moreHands);
//        db.commit();
//
//        godTest = new God(god.getVertex());
//        Assert.assertEquals(8, godTest.getHand().size());
//        Assert.assertEquals("hand1", godTest.getHand().get(0).getName());
//        Assert.assertEquals("hand2", godTest.getHand().get(1).getName());
//        Assert.assertEquals("hand3", godTest.getHand().get(2).getName());
//        Assert.assertEquals("hand4", godTest.getHand().get(3).getName());
//        Assert.assertEquals("hand5", godTest.getHand().get(4).getName());
//        Assert.assertEquals("hand6", godTest.getHand().get(5).getName());
//        Assert.assertEquals("hand7", godTest.getHand().get(6).getName());
//        Assert.assertEquals("hand8", godTest.getHand().get(7).getName());
//    }
//
//    @Test
//    public void testSequenceAddAll2() {
//        God god = new God(true);
//        god.setName("THEGOD");
//        Hand hand = new Hand(god);
//        hand.setLeft(true);
//        hand.setName("hand1");
//        Hand hand2 = new Hand(god);
//        hand2.setLeft(true);
//        hand2.setName("hand2");
//        Hand hand3 = new Hand(god);
//        hand3.setLeft(true);
//        hand3.setName("hand3");
//        Hand hand4 = new Hand(god);
//        hand4.setLeft(true);
//        hand4.setName("hand4");
//        db.commit();
//
//        Hand hand5 = new Hand(true);
//        hand5.setLeft(true);
//        hand5.setName("hand5");
//        Hand hand6 = new Hand(true);
//        hand6.setLeft(true);
//        hand6.setName("hand6");
//        Hand hand7 = new Hand(true);
//        hand7.setLeft(true);
//        hand7.setName("hand7");
//        Hand hand8 = new Hand(true);
//        hand8.setLeft(true);
//        hand8.setName("hand8");
//        List<Hand> moreHands = new ArrayList<Hand>(4);
//        moreHands.add(hand5);
//        moreHands.add(hand6);
//        moreHands.add(hand7);
//        moreHands.add(hand8);
//        god.getHand().addAll(2, moreHands);
//        db.commit();
//
//        God godTest = new God(god.getVertex());
//        Assert.assertEquals(8, godTest.getHand().size());
//        Assert.assertEquals("hand1", godTest.getHand().get(0).getName());
//        Assert.assertEquals("hand2", godTest.getHand().get(1).getName());
//        Assert.assertEquals("hand5", godTest.getHand().get(2).getName());
//        Assert.assertEquals("hand6", godTest.getHand().get(3).getName());
//        Assert.assertEquals("hand7", godTest.getHand().get(4).getName());
//        Assert.assertEquals("hand8", godTest.getHand().get(5).getName());
//        Assert.assertEquals("hand3", godTest.getHand().get(6).getName());
//        Assert.assertEquals("hand4", godTest.getHand().get(7).getName());
//    }
//
//    @Test
//    public void testSequenceAddObjectMoreThanOnce() {
//        God god = new God(true);
//        god.setName("THEGOD");
//        Hand hand1 = new Hand(god);
//        hand1.setLeft(true);
//        hand1.setName("hand1");
//        Hand hand2 = new Hand(god);
//        hand2.setLeft(true);
//        hand2.setName("hand2");
//        db.commit();
//
//        God godTest = new God(god.getVertex());
//        godTest.getHand().add(hand1);
//        godTest.getHand().add(hand2);
//        db.commit();
//
//        God godTest2 = new God(god.getVertex());
//        Assert.assertEquals(4, godTest2.getHand().size());
//        Assert.assertEquals("hand1", godTest2.getHand().get(0).getName());
//        Assert.assertEquals("hand2", godTest2.getHand().get(1).getName());
//        Assert.assertEquals("hand1", godTest2.getHand().get(2).getName());
//        Assert.assertEquals("hand2", godTest2.getHand().get(3).getName());
//        Assert.assertEquals(7, countVertices());
//        Assert.assertEquals(14, countEdges());
//    }
//
//    @Test
//    public void testManyToManySequenceWithDuplicates() {
//        God god = new God(true);
//        god.setName("THEGOD");
//
//        Many1 many1_1 = new Many1(true);
//        many1_1.setName("many1_1");
//        many1_1.addToGod(god);
//
//        Many1 many1_2 = new Many1(true);
//        many1_2.setName("many1_1");
//        many1_2.addToGod(god);
//
//        Many1 many1_3 = new Many1(true);
//        many1_3.setName("many1_1");
//        many1_3.addToGod(god);
//
//        Many1 many1_4 = new Many1(true);
//        many1_4.setName("many1_4");
//        many1_4.addToGod(god);
//
//        Many2 many2_1 = new Many2(true);
//        many2_1.setName("many2_1");
//        many2_1.addToGod(god);
//
//        Many2 many2_2 = new Many2(true);
//        many2_2.setName("many2_2");
//        many2_2.addToGod(god);
//
//        Many2 many2_3 = new Many2(true);
//        many2_3.setName("many2_3");
//        many2_3.addToGod(god);
//
//        Many2 many2_4 = new Many2(true);
//        many2_4.setName("many2_4");
//        many2_4.addToGod(god);
//
//        many1_1.addToMany2UnqualifiedList(many2_1);
//        many1_1.addToMany2UnqualifiedList(many2_2);
//        many1_1.addToMany2UnqualifiedList(many2_3);
//        many1_1.addToMany2UnqualifiedList(many2_4);
//
//        many1_2.addToMany2UnqualifiedList(many2_1);
//        many1_2.addToMany2UnqualifiedList(many2_2);
//        many1_2.addToMany2UnqualifiedList(many2_3);
//        many1_2.addToMany2UnqualifiedList(many2_4);
//
//        many1_3.addToMany2UnqualifiedList(many2_1);
//        many1_3.addToMany2UnqualifiedList(many2_2);
//        many1_3.addToMany2UnqualifiedList(many2_3);
//        many1_3.addToMany2UnqualifiedList(many2_4);
//
//        many1_4.addToMany2UnqualifiedList(many2_1);
//        many1_4.addToMany2UnqualifiedList(many2_2);
//        many1_4.addToMany2UnqualifiedList(many2_3);
//        many1_4.addToMany2UnqualifiedList(many2_4);
//
//        db.commit();
//        Assert.assertEquals(41, countVertices());
//        Assert.assertEquals(97, countEdges());
//
//        //Some duplicates
//        many1_1.addToMany2UnqualifiedList(many2_1);
//        many1_1.addToMany2UnqualifiedList(many2_2);
//        many1_1.addToMany2UnqualifiedList(many2_3);
//        many1_1.addToMany2UnqualifiedList(many2_4);
//
//        db.commit();
//        Assert.assertEquals(49, countVertices());
//        Assert.assertEquals(117, countEdges());
//
//        //Some duplicates
//        many2_1.addToMany1UnqualifiedList(many1_1);
//
//        db.commit();
//        Assert.assertEquals(50, countVertices());
//        Assert.assertEquals(121, countEdges());
//
//        many2_1.addToMany1UnqualifiedList(many1_2);
//        db.commit();
//        Assert.assertEquals(52, countVertices());
//        Assert.assertEquals(126, countEdges());
//
//        many2_1.addToMany1UnqualifiedList(many1_3);
//        db.commit();
//        Assert.assertEquals(54, countVertices());
//        Assert.assertEquals(131, countEdges());
//
//        many2_1.addToMany1UnqualifiedList(many1_4);
//        db.commit();
//        Assert.assertEquals(56, countVertices());
//        Assert.assertEquals(136, countEdges());
//
//        many1_1 = new Many1(many1_1.getVertex());
//        Assert.assertEquals(9, many1_1.getMany2UnqualifiedList().size());
//        Assert.assertEquals("many2_1", many1_1.getMany2UnqualifiedList().get(0).getName());
//        Assert.assertEquals("many2_2", many1_1.getMany2UnqualifiedList().get(1).getName());
//        Assert.assertEquals("many2_3", many1_1.getMany2UnqualifiedList().get(2).getName());
//        Assert.assertEquals("many2_4", many1_1.getMany2UnqualifiedList().get(3).getName());
//        Assert.assertEquals("many2_1", many1_1.getMany2UnqualifiedList().get(4).getName());
//        Assert.assertEquals("many2_2", many1_1.getMany2UnqualifiedList().get(5).getName());
//        Assert.assertEquals("many2_3", many1_1.getMany2UnqualifiedList().get(6).getName());
//        Assert.assertEquals("many2_4", many1_1.getMany2UnqualifiedList().get(7).getName());
//        Assert.assertEquals("many2_1", many1_1.getMany2UnqualifiedList().get(8).getName());
//
//    }
//
//    @Test
//    public void testRemovalOfEdgeFromIndex() {
//        God god = new God(true);
//        god.setName("THEGOD");
//        Hand hand1 = new Hand(god);
//        hand1.setLeft(true);
//        hand1.setName("hand1");
//        Hand hand2 = new Hand(god);
//        hand2.setLeft(false);
//        hand2.setName("hand2");
//        db.commit();
//        Assert.assertEquals(5, countVertices());
//        Assert.assertEquals(8, countEdges());
//
//        god.addToHand(hand1);
//        db.commit();
//        Assert.assertEquals(6, countVertices());
//        Assert.assertEquals(11, countEdges());
//        God gTest = new God(god.getVertex());
//        Assert.assertEquals(3, gTest.getHand().size());
//
//        Hand h1 = gTest.getHand().get(0);
//        Hand h2 = gTest.getHand().get(1);
//        Hand h3 = gTest.getHand().get(2);
//
//        Assert.assertEquals("hand1", h1.getName());
//        Assert.assertEquals("hand2", h2.getName());
//        Assert.assertEquals("hand1", h3.getName());
//        Assert.assertEquals(h1, h3);
//    }
//
//    @Test
//    public void testSequenceWithOneElement() {
//        God god = new God(true);
//        god.setName("THEGOD");
//        Hand hand1 = new Hand(god);
//        hand1.setLeft(true);
//        hand1.setName("hand1");
//        db.commit();
//        Assert.assertEquals(3, countVertices());
//        Assert.assertEquals(5, countEdges());
//    }
//
//    @Test
//    public void testGodHand() {
//        God god = new God(true);
//        god.setName("THEGOD");
//        Hand hand1 = new Hand(god);
//        hand1.setLeft(true);
//        hand1.setName("hand1");
//        db.commit();
//
//        Assert.assertEquals(3, countVertices());
//        Assert.assertEquals(5, countEdges());
//
//        Finger finger = new Finger(hand1);
//        finger.setName("finger");
//        db.commit();
//
//        Assert.assertEquals(4, countVertices());
//        Assert.assertEquals(8, countEdges());
//    }
//
//    @Test
//    public void testAddAtIndex() {
//        God god = new God(true);
//        god.setName("THEGOD");
//        Hand hand1 = new Hand(god);
//        hand1.setLeft(true);
//        hand1.setName("hand1");
//        Finger finger1 = new Finger(hand1);
//        finger1.setName("finger1");
//        Finger finger2 = new Finger(hand1);
//        finger2.setName("finger2");
//        Finger finger3 = new Finger(hand1);
//        finger3.setName("finger3");
//        Finger finger4 = new Finger(hand1);
//        finger4.setName("finger4");
//        db.commit();
//        Assert.assertEquals(7, countVertices());
//        Assert.assertEquals(14, countEdges());
//
//        int indexToTest = 2;
//        hand1.getFinger().add(indexToTest, finger2);
//        db.commit();
//        //Check nothing happened as the element is already in the set, first remove it then add
//        Assert.assertEquals(7, countVertices());
//        Assert.assertEquals(14, countEdges());
//
//        hand1.getFinger().remove(finger2);
//        hand1.getFinger().add(indexToTest, finger2);
//        db.commit();
//        Assert.assertEquals(7, countVertices());
//        Assert.assertEquals(14, countEdges());
//
//        hand1 = new Hand(hand1.getVertex());
//
//        Assert.assertEquals("finger1", hand1.getFinger().get(0).getName());
//        Assert.assertEquals("finger3", hand1.getFinger().get(1).getName());
//        Assert.assertEquals("finger2", hand1.getFinger().get(2).getName());
//        Assert.assertEquals("finger4", hand1.getFinger().get(3).getName());
//
//        Finger fingerTest = new Finger(finger2.getVertex());
//        Assert.assertEquals("finger2", fingerTest.getName());
//        Hand handTest = new Hand(hand1.getVertex());
//        Assert.assertEquals("hand1", handTest.getName());
//        Assert.assertEquals("finger2", handTest.getFinger().get(indexToTest).getName());
//        Assert.assertEquals("finger2", handTest.getFinger().get(indexToTest).getId(), fingerTest.getId());
//    }
//
//    @Test
//    public void testSequenceMultiplicity1() {
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//        SequenceTest1 sequenceTest1 = new SequenceTest1(sequenceRoot);
//        sequenceTest1.setName("sequenceTest1");
//        db.commit();
//        Assert.assertNotNull(sequenceRoot.getSequenceTest1());
//        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
//        Assert.assertNotNull(sequenceRoot.getSequenceTest1());
//    }
//
//    @Test
//    public void testSequenceInverseIsSequence() {
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//        SequenceList2 sequenceList2 = new SequenceList2(sequenceRoot);
//        sequenceList2.setName("sequenceList2");
//        db.commit();
//        Assert.assertEquals(1, sequenceRoot.getSequenceList2().size());
//        Assert.assertNotNull(sequenceList2.getSequenceRoot());
//        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
//        sequenceList2 = new SequenceList2(sequenceList2.getVertex());
//        Assert.assertNotNull(sequenceList2.getSequenceRoot());
//        Assert.assertEquals(1, sequenceRoot.getSequenceList2().size());
//        Assert.assertEquals(3, countVertices());
//        Assert.assertEquals(5, countEdges());
//    }
//
//    //This test is primarily for coverage testing
//    @Test(expected = RuntimeException.class)
//    public void testSequenceMultiplicityOneOneThrowsExceptionForDuplicate() {
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//        SequenceTestListOne sequenceTestListOne = new SequenceTestListOne(true);
//        sequenceTestListOne.setName("sequenceTest1");
//        sequenceTestListOne.setSequenceRoot(sequenceRoot);
//        db.commit();
//        Assert.assertNotNull(sequenceRoot.getSequenceTestList());
//        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
//        Assert.assertNotNull(sequenceRoot.getSequenceTestList());
//        sequenceRoot.addToSequenceTestList(sequenceTestListOne);
//        db.commit();
//    }
//
//    @Test
//    public void testSequenceDuplicates() {
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(true);
//        sequenceTestListMany1.setName("sequenceTestListMany1");
//        sequenceRoot.addToSequenceTestListMany(sequenceTestListMany1);
//        sequenceRoot.addToSequenceTestListMany(sequenceTestListMany1);
//        db.commit();
//        Assert.assertEquals(2, sequenceRoot.getSequenceTestListMany().size());
//        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
//        Assert.assertEquals(2, sequenceRoot.getSequenceTestListMany().size());
//        Assert.assertEquals(3, countVertices());
//        Assert.assertEquals(7, countEdges());
//
//        sequenceRoot.getSequenceTestListMany().add(0, sequenceTestListMany1);
//        db.commit();
//
//        Assert.assertEquals(3, sequenceRoot.getSequenceTestListMany().size());
//        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
//        Assert.assertEquals(3, sequenceRoot.getSequenceTestListMany().size());
//
//        Assert.assertEquals(3, countVertices());
//        Assert.assertEquals(9, countEdges());
//
//        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(true);
//        sequenceTestListMany2.setName("sequenceTestListMany2");
//
//        sequenceRoot.getSequenceTestListMany().add(0, sequenceTestListMany2);
//        db.commit();
//
//        Assert.assertEquals(4, sequenceRoot.getSequenceTestListMany().size());
//        Assert.assertEquals("sequenceTestListMany2", sequenceRoot.getSequenceTestListMany().get(0).getName());
//        Assert.assertEquals("sequenceTestListMany1", sequenceRoot.getSequenceTestListMany().get(1).getName());
//        Assert.assertEquals("sequenceTestListMany1", sequenceRoot.getSequenceTestListMany().get(2).getName());
//        Assert.assertEquals("sequenceTestListMany1", sequenceRoot.getSequenceTestListMany().get(3).getName());
//
//        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
//        Assert.assertEquals(4, sequenceRoot.getSequenceTestListMany().size());
//        Assert.assertEquals(5, countVertices());
//        Assert.assertEquals(12, countEdges());
//
//        Assert.assertEquals("sequenceTestListMany2", sequenceRoot.getSequenceTestListMany().get(0).getName());
//        Assert.assertEquals("sequenceTestListMany1", sequenceRoot.getSequenceTestListMany().get(1).getName());
//        Assert.assertEquals("sequenceTestListMany1", sequenceRoot.getSequenceTestListMany().get(2).getName());
//        Assert.assertEquals("sequenceTestListMany1", sequenceRoot.getSequenceTestListMany().get(3).getName());
//
//    }
//
//    @Test
//    public void testSequenceAddingElementAtSizePlusOne() {
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(true);
//        sequenceTestListMany1.setName("sequenceTestListMany1");
//        sequenceRoot.addToSequenceTestListMany(sequenceTestListMany1);
//        db.commit();
//
//        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(true);
//        sequenceTestListMany2.setName("sequenceTestListMany2");
//        sequenceRoot.getSequenceTestListMany().add(1, sequenceTestListMany2);
//        db.commit();
//
//        Assert.assertEquals(2, sequenceRoot.getSequenceTestListMany().size());
//        Assert.assertEquals("sequenceTestListMany1", sequenceRoot.getSequenceTestListMany().get(0).getName());
//        Assert.assertEquals("sequenceTestListMany2", sequenceRoot.getSequenceTestListMany().get(1).getName());
//        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
//        Assert.assertEquals(2, sequenceRoot.getSequenceTestListMany().size());
//        Assert.assertEquals("sequenceTestListMany1", sequenceRoot.getSequenceTestListMany().get(0).getName());
//        Assert.assertEquals("sequenceTestListMany2", sequenceRoot.getSequenceTestListMany().get(1).getName());
//
//        Assert.assertEquals(5, countVertices());
//        Assert.assertEquals(8, countEdges());
//
//        sequenceRoot.getSequenceTestListMany().add(2, sequenceTestListMany2);
//        db.commit();
//
//        Assert.assertEquals(3, sequenceRoot.getSequenceTestListMany().size());
//        Assert.assertEquals("sequenceTestListMany1", sequenceRoot.getSequenceTestListMany().get(0).getName());
//        Assert.assertEquals("sequenceTestListMany2", sequenceRoot.getSequenceTestListMany().get(1).getName());
//        Assert.assertEquals("sequenceTestListMany2", sequenceRoot.getSequenceTestListMany().get(2).getName());
//        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
//        Assert.assertEquals(3, sequenceRoot.getSequenceTestListMany().size());
//        Assert.assertEquals("sequenceTestListMany1", sequenceRoot.getSequenceTestListMany().get(0).getName());
//        Assert.assertEquals("sequenceTestListMany2", sequenceRoot.getSequenceTestListMany().get(1).getName());
//        Assert.assertEquals("sequenceTestListMany2", sequenceRoot.getSequenceTestListMany().get(2).getName());
//
//        Assert.assertEquals(5, countVertices());
//        Assert.assertEquals(10, countEdges());
//    }
//
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void testSequenceAddingElementAtIncorrectSizeThrowsException() {
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(true);
//        sequenceTestListMany1.setName("sequenceTestListMany1");
//        sequenceRoot.addToSequenceTestListMany(sequenceTestListMany1);
//        db.commit();
//
//        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(true);
//        sequenceTestListMany2.setName("sequenceTestListMany2");
//        sequenceRoot.getSequenceTestListMany().add(2, sequenceTestListMany2);
//        db.commit();
//    }
//
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void testSequenceAddingElementAtIncorrectSizeThrowsException2() {
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//        db.commit();
//
//        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(true);
//        sequenceTestListMany2.setName("sequenceTestListMany2");
//        sequenceRoot.getSequenceTestListMany().add(1, sequenceTestListMany2);
//        db.commit();
//    }
//
//    @Test
//    public void testRemoval() {
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
//        sequenceTestListMany1.setName("sequenceTestListMany1");
//        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
//        sequenceTestListMany2.setName("sequenceTestListMany2");
//        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
//        sequenceTestListMany3.setName("sequenceTestListMany3");
//        db.commit();
//        Assert.assertEquals(7, countVertices());
//        Assert.assertEquals(11, countEdges());
//
//        sequenceRoot.getSequenceTestListMany().remove(0);
//        sequenceTestListMany1.delete();
//        db.commit();
//        Assert.assertEquals(5, countVertices());
//        Assert.assertEquals(8, countEdges());
//
//        sequenceRoot.getSequenceTestListMany().remove(1);
//        sequenceTestListMany3.delete();
//        db.commit();
//        Assert.assertEquals(3, countVertices());
//        Assert.assertEquals(5, countEdges());
//    }
//
//    @Test
//    public void testRemoval2() {
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
//        sequenceTestListMany1.setName("sequenceTestListMany1");
//        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
//        sequenceTestListMany2.setName("sequenceTestListMany2");
//        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
//        sequenceTestListMany3.setName("sequenceTestListMany3");
//        db.commit();
//        Assert.assertEquals(7, countVertices());
//        Assert.assertEquals(11, countEdges());
//
//        sequenceRoot.getSequenceTestListMany().remove(1);
//        sequenceTestListMany2.delete();
//        db.commit();
//        Assert.assertEquals(5, countVertices());
//        Assert.assertEquals(8, countEdges());
//
//    }
//
//    @Test
//    public void testInsertAfterDeletion() {
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//
//        SequenceTestOrderedSet sequenceTestOrderedSet1 = new SequenceTestOrderedSet(sequenceRoot);
//        sequenceTestOrderedSet1.setName("sequenceTestOrderedSet1");
//        SequenceTestOrderedSet sequenceTestOrderedSet2 = new SequenceTestOrderedSet(sequenceRoot);
//        sequenceTestOrderedSet2.setName("sequenceTestOrderedSet2");
//        SequenceTestOrderedSet sequenceTestOrderedSet3 = new SequenceTestOrderedSet(sequenceRoot);
//        sequenceTestOrderedSet3.setName("sequenceTestOrderedSet3");
//        SequenceTestOrderedSet sequenceTestOrderedSet4 = new SequenceTestOrderedSet(sequenceRoot);
//        sequenceTestOrderedSet4.setName("sequenceTestOrderedSet4");
//        SequenceTestOrderedSet sequenceTestOrderedSet5 = new SequenceTestOrderedSet(sequenceRoot);
//        sequenceTestOrderedSet5.setName("sequenceTestOrderedSet5");
//
//        db.commit();
//
//        Assert.assertEquals(6, countVertices());
//        Assert.assertEquals(12, countEdges());
//        sequenceTestOrderedSet3.delete();
//        db.commit();
//        Assert.assertEquals(5, countVertices());
//        Assert.assertEquals(10, countEdges());
//        sequenceTestOrderedSet4.delete();
//        db.commit();
//        Assert.assertEquals(4, countVertices());
//        Assert.assertEquals(8, countEdges());
//        sequenceTestOrderedSet5.delete();
//        db.commit();
//        Assert.assertEquals(3, countVertices());
//        Assert.assertEquals(6, countEdges());
//
//        sequenceRoot.reload();
//        Assert.assertEquals(2, sequenceRoot.getSequenceTestOrderedSet().size());
//        Assert.assertEquals(sequenceTestOrderedSet1, sequenceRoot.getSequenceTestOrderedSet().get(0));
//        Assert.assertEquals(sequenceTestOrderedSet2, sequenceRoot.getSequenceTestOrderedSet().get(1));
//
//        SequenceTestOrderedSet sequenceTestOrderedSet6 = new SequenceTestOrderedSet(sequenceRoot);
//        sequenceTestOrderedSet6.setName("sequenceTestOrderedSet6");
//        db.commit();
//
//        sequenceRoot.reload();
//        Assert.assertEquals(3, sequenceRoot.getSequenceTestOrderedSet().size());
//        Assert.assertEquals(sequenceTestOrderedSet1, sequenceRoot.getSequenceTestOrderedSet().get(0));
//        Assert.assertEquals(sequenceTestOrderedSet2, sequenceRoot.getSequenceTestOrderedSet().get(1));
//        Assert.assertEquals(sequenceTestOrderedSet6, sequenceRoot.getSequenceTestOrderedSet().get(2));
//    }

    @Test
    public void testDeleteFirst() {
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
        SequenceTestOrderedSet sequenceTestOrderedSet5 = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSet5.setName("sequenceTestOrderedSet5");

        db.commit();

        Assert.assertEquals(6, countVertices());
        Assert.assertEquals(12, countEdges());

        sequenceTestOrderedSet1.delete();
        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(10, countEdges());
        sequenceRoot.reload();
        Assert.assertEquals(sequenceTestOrderedSet2, sequenceRoot.getSequenceTestOrderedSet().iterator().next());

        sequenceTestOrderedSet2.delete();
        db.commit();
        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8, countEdges());
        sequenceRoot.reload();
        Assert.assertEquals(sequenceTestOrderedSet3, sequenceRoot.getSequenceTestOrderedSet().iterator().next());

        sequenceTestOrderedSet3.delete();
        db.commit();
        Assert.assertEquals(3, countVertices());
        Assert.assertEquals(6, countEdges());
        sequenceRoot.reload();
        Assert.assertEquals(sequenceTestOrderedSet4, sequenceRoot.getSequenceTestOrderedSet().iterator().next());

        sequenceTestOrderedSet4.delete();
        db.commit();
        Assert.assertEquals(2, countVertices());
        Assert.assertEquals(4, countEdges());
        sequenceRoot.reload();
        Assert.assertEquals(sequenceTestOrderedSet5, sequenceRoot.getSequenceTestOrderedSet().iterator().next());

        sequenceTestOrderedSet5.delete();
        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1, countEdges());
        sequenceRoot.reload();
        Assert.assertEquals(0, sequenceRoot.getSequenceTestOrderedSet().size());
    }

//    @Test
//    public void testDeleteLast() {
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//
//        SequenceTestOrderedSet sequenceTestOrderedSet1 = new SequenceTestOrderedSet(sequenceRoot);
//        sequenceTestOrderedSet1.setName("sequenceTestOrderedSet1");
//        SequenceTestOrderedSet sequenceTestOrderedSet2 = new SequenceTestOrderedSet(sequenceRoot);
//        sequenceTestOrderedSet2.setName("sequenceTestOrderedSet2");
//        SequenceTestOrderedSet sequenceTestOrderedSet3 = new SequenceTestOrderedSet(sequenceRoot);
//        sequenceTestOrderedSet3.setName("sequenceTestOrderedSet3");
//        SequenceTestOrderedSet sequenceTestOrderedSet4 = new SequenceTestOrderedSet(sequenceRoot);
//        sequenceTestOrderedSet4.setName("sequenceTestOrderedSet4");
//        SequenceTestOrderedSet sequenceTestOrderedSet5 = new SequenceTestOrderedSet(sequenceRoot);
//        sequenceTestOrderedSet5.setName("sequenceTestOrderedSet5");
//
//        db.commit();
//
//        Assert.assertEquals(6, countVertices());
//        Assert.assertEquals(12, countEdges());
//
//        sequenceTestOrderedSet5.delete();
//        db.commit();
//        Assert.assertEquals(5, countVertices());
//        Assert.assertEquals(10, countEdges());
//        sequenceRoot.reload();
//        Assert.assertEquals(sequenceTestOrderedSet1, sequenceRoot.getSequenceTestOrderedSet().iterator().next());
//        Assert.assertEquals(sequenceTestOrderedSet4, sequenceRoot.getSequenceTestOrderedSet().get(3));
//
//        //Check that last edge is correct
//        SequenceTestOrderedSet sequenceTestOrderedSet6 = new SequenceTestOrderedSet(sequenceRoot);
//        sequenceTestOrderedSet6.setName("sequenceTestOrderedSet6");
//        db.commit();
//        sequenceRoot.reload();
//        Assert.assertEquals(sequenceTestOrderedSet1, sequenceRoot.getSequenceTestOrderedSet().iterator().next());
//        Assert.assertEquals(sequenceTestOrderedSet6, sequenceRoot.getSequenceTestOrderedSet().get(4));
//        Assert.assertEquals(6, countVertices());
//        Assert.assertEquals(12, countEdges());
//
//        sequenceTestOrderedSet6.delete();
//        db.commit();
//        Assert.assertEquals(5, countVertices());
//        Assert.assertEquals(10, countEdges());
//        sequenceRoot.reload();
//        Assert.assertEquals(sequenceTestOrderedSet1, sequenceRoot.getSequenceTestOrderedSet().iterator().next());
//        Assert.assertEquals(sequenceTestOrderedSet4, sequenceRoot.getSequenceTestOrderedSet().get(3));
//
//        sequenceTestOrderedSet4.delete();
//        db.commit();
//        Assert.assertEquals(4, countVertices());
//        Assert.assertEquals(8, countEdges());
//        sequenceRoot.reload();
//        Assert.assertEquals(sequenceTestOrderedSet1, sequenceRoot.getSequenceTestOrderedSet().iterator().next());
//        Assert.assertEquals(sequenceTestOrderedSet3, sequenceRoot.getSequenceTestOrderedSet().get(2));
//
//        sequenceTestOrderedSet3.delete();
//        db.commit();
//        Assert.assertEquals(3, countVertices());
//        Assert.assertEquals(6, countEdges());
//        sequenceRoot.reload();
//        Assert.assertEquals(sequenceTestOrderedSet1, sequenceRoot.getSequenceTestOrderedSet().iterator().next());
//        Assert.assertEquals(sequenceTestOrderedSet2, sequenceRoot.getSequenceTestOrderedSet().get(1));
//
//        sequenceTestOrderedSet2.delete();
//        db.commit();
//        Assert.assertEquals(2, countVertices());
//        Assert.assertEquals(4, countEdges());
//        sequenceRoot.reload();
//        Assert.assertEquals(sequenceTestOrderedSet1, sequenceRoot.getSequenceTestOrderedSet().iterator().next());
//        Assert.assertEquals(sequenceTestOrderedSet1, sequenceRoot.getSequenceTestOrderedSet().get(0));
//
//        sequenceTestOrderedSet1.delete();
//        db.commit();
//        Assert.assertEquals(1, countVertices());
//        Assert.assertEquals(1, countEdges());
//        sequenceRoot.reload();
//        Assert.assertEquals(0, sequenceRoot.getSequenceTestOrderedSet().size());
//    }
//
//
//    @Test
//    public void testAdderWithIndexForOrderedSet() {
//
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//
//        SequenceTestOrderedSet sequenceTestOrderedSet1 = new SequenceTestOrderedSet(true);
//        sequenceTestOrderedSet1.setName("sequenceTestOrderedSet1");
//        SequenceTestOrderedSet sequenceTestOrderedSet2 = new SequenceTestOrderedSet(true);
//        sequenceTestOrderedSet2.setName("sequenceTestOrderedSet2");
//        SequenceTestOrderedSet sequenceTestOrderedSet3 = new SequenceTestOrderedSet(true);
//        sequenceTestOrderedSet3.setName("sequenceTestOrderedSet3");
//        SequenceTestOrderedSet sequenceTestOrderedSet4 = new SequenceTestOrderedSet(true);
//        sequenceTestOrderedSet4.setName("sequenceTestOrderedSet4");
//        SequenceTestOrderedSet sequenceTestOrderedSet5 = new SequenceTestOrderedSet(true);
//        sequenceTestOrderedSet5.setName("sequenceTestOrderedSet5");
//
//        sequenceRoot.addToSequenceTestOrderedSet(0, sequenceTestOrderedSet5);
//        sequenceRoot.addToSequenceTestOrderedSet(1, sequenceTestOrderedSet4);
//        sequenceRoot.addToSequenceTestOrderedSet(2, sequenceTestOrderedSet3);
//        sequenceRoot.addToSequenceTestOrderedSet(3, sequenceTestOrderedSet2);
//        sequenceRoot.addToSequenceTestOrderedSet(4, sequenceTestOrderedSet1);
//
//        db.commit();
//        Assert.assertEquals(6, countVertices());
//        Assert.assertEquals(12, countEdges());
//
//        sequenceRoot.reload();
//        Assert.assertEquals(sequenceTestOrderedSet5.reload(), sequenceRoot.getSequenceTestOrderedSet().get(0));
//        Assert.assertEquals(sequenceTestOrderedSet4.reload(), sequenceRoot.getSequenceTestOrderedSet().get(1));
//        Assert.assertEquals(sequenceTestOrderedSet3.reload(), sequenceRoot.getSequenceTestOrderedSet().get(2));
//        Assert.assertEquals(sequenceTestOrderedSet2.reload(), sequenceRoot.getSequenceTestOrderedSet().get(3));
//        Assert.assertEquals(sequenceTestOrderedSet1.reload(), sequenceRoot.getSequenceTestOrderedSet().get(4));
//    }
//
//    @Test
//    public void testAdderWithIndexForList() {
//
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//
//        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(true);
//        sequenceTestListMany1.setName("sequenceTestListMany1");
//        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(true);
//        sequenceTestListMany2.setName("sequenceTestListMany2");
//        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(true);
//        sequenceTestListMany3.setName("sequenceTestListMany3");
//        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(true);
//        sequenceTestListMany4.setName("sequenceTestListMany4");
//        SequenceTestListMany sequenceTestListMany5 = new SequenceTestListMany(true);
//        sequenceTestListMany5.setName("sequenceTestListMany5");
//
//        sequenceRoot.getSequenceTestListMany().add(0, sequenceTestListMany1);
//        sequenceRoot.getSequenceTestListMany().add(1, sequenceTestListMany2);
//        sequenceRoot.addToSequenceTestListMany(2, sequenceTestListMany3);
//        sequenceRoot.addToSequenceTestListMany(3, sequenceTestListMany4);
//        sequenceRoot.addToSequenceTestListMany(4, sequenceTestListMany5);
//
//        db.commit();
//
//        sequenceRoot.reload();
//        Assert.assertEquals(sequenceTestListMany5.reload(), sequenceRoot.getSequenceTestListMany().get(4));
//        Assert.assertEquals(sequenceTestListMany4.reload(), sequenceRoot.getSequenceTestListMany().get(3));
//        Assert.assertEquals(sequenceTestListMany3.reload(), sequenceRoot.getSequenceTestListMany().get(2));
//        Assert.assertEquals(sequenceTestListMany2.reload(), sequenceRoot.getSequenceTestListMany().get(1));
//        Assert.assertEquals(sequenceTestListMany1.reload(), sequenceRoot.getSequenceTestListMany().get(0));
//    }
//
//    /**
//     * This test shows how if one updates multiple row's index in the list, expecting the index to be as specified after the commit,
//     * then the update must be ordered with the index in descending order.
//     * <p/>
//     * Otherwise when subsequent updates happen the list no longer is in the order as when one specified the index.
//     */
//    @Test
//    public void testMoving2Items() {
//
//        SequenceRoot sequenceRoot = new SequenceRoot(true);
//        sequenceRoot.setName("sequenceRoot");
//
//        SequenceTestListMany sequenceTestListMany1 = new SequenceTestListMany(sequenceRoot);
//        sequenceTestListMany1.setName("sequenceTestListMany1");
//        SequenceTestListMany sequenceTestListMany2 = new SequenceTestListMany(sequenceRoot);
//        sequenceTestListMany2.setName("sequenceTestListMany2");
//        SequenceTestListMany sequenceTestListMany3 = new SequenceTestListMany(sequenceRoot);
//        sequenceTestListMany3.setName("sequenceTestListMany3");
//        SequenceTestListMany sequenceTestListMany4 = new SequenceTestListMany(sequenceRoot);
//        sequenceTestListMany4.setName("sequenceTestListMany4");
//        SequenceTestListMany sequenceTestListMany5 = new SequenceTestListMany(sequenceRoot);
//        sequenceTestListMany5.setName("sequenceTestListMany5");
//
//        db.commit();
//
//        sequenceRoot.reload();
//        Assert.assertEquals(sequenceTestListMany1.reload(), sequenceRoot.getSequenceTestListMany().get(0));
//        Assert.assertEquals(sequenceTestListMany2.reload(), sequenceRoot.getSequenceTestListMany().get(1));
//        Assert.assertEquals(sequenceTestListMany3.reload(), sequenceRoot.getSequenceTestListMany().get(2));
//        Assert.assertEquals(sequenceTestListMany4.reload(), sequenceRoot.getSequenceTestListMany().get(3));
//        Assert.assertEquals(sequenceTestListMany5.reload(), sequenceRoot.getSequenceTestListMany().get(4));
//
//        sequenceRoot.removeFromSequenceTestListMany(sequenceTestListMany2);
//        sequenceRoot.addToSequenceTestListMany(3, sequenceTestListMany2);
//        sequenceRoot.removeFromSequenceTestListMany(sequenceTestListMany1);
//        sequenceRoot.addToSequenceTestListMany(2, sequenceTestListMany1);
//
//        db.commit();
//
//        sequenceRoot.reload();
//        Assert.assertEquals(sequenceTestListMany3.reload(), sequenceRoot.getSequenceTestListMany().get(0));
//        Assert.assertEquals(sequenceTestListMany4.reload(), sequenceRoot.getSequenceTestListMany().get(1));
//        Assert.assertEquals(sequenceTestListMany1.reload(), sequenceRoot.getSequenceTestListMany().get(2));
//        Assert.assertEquals(sequenceTestListMany2.reload(), sequenceRoot.getSequenceTestListMany().get(3));
//        Assert.assertEquals(sequenceTestListMany5.reload(), sequenceRoot.getSequenceTestListMany().get(4));
//
//    }

}