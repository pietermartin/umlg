package org.umlg.tests.globalget;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assume;
import org.junit.Test;
import org.umlg.collectiontest.Finger;
import org.umlg.collectiontest.Hand;
import org.umlg.concretetest.God;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.persistent.PropertyTree;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.umlg.collectiontest.Hand.HandRuntimePropertyEnum.finger;
import static org.umlg.concretetest.God.GodRuntimePropertyEnum.hand;

/**
 * Date: 2016/05/28
 * Time: 8:42 PM
 */
public class TestGlobalGet extends BaseLocalDbTest {

//    @Test
//    public void test() {
//        TopRoot topRoot1 = new TopRoot();
//        topRoot1.setNameUnique("a");
//        topRoot1.setIndexedName("aaa");
//        TopRootChild topRootChild1 = new TopRootChild(topRoot1);
//        topRootChild1.setName("topRootChild1");
//
//        TopRoot topRoot2 = new TopRoot();
//        topRoot2.setNameUnique("ab");
//        topRoot2.setIndexedName("aaab");
//        TopRootChild topRootChild2 = new TopRootChild(topRoot2);
//        topRootChild2.setName("topRootChild2");
//        UMLG.get().commit();
//
//        //This must happen with only 2 queries to the db, 1 really but the optional 2
//        List<TopRoot> topRoots = UMLG.get().get(PropertyTree.from("TopRoot").addChild(TopRoot.TopRootRuntimePropertyEnum.topRootChild));
//        for (TopRoot topRoot : topRoots) {
//            for (TopRootChild topRootChild : topRoot.getTopRootChild()) {
//                System.out.println(topRootChild2.getName());
//            }
//        }
//        assertTrue(topRoots.containsAll(Arrays.asList(topRoot1, topRoot2)));
//    }
//
//    @Test
//    public void testGlobalGet2Ways() {
//        AOptional aOptional1 = new AOptional();
//        aOptional1.setName("aOptional1");
//        BOptional bOptional1 = new BOptional();
//        bOptional1.setName("bOptional1");
//        BBOptional bbOptional1 = new BBOptional();
//        bbOptional1.setName("bbOptional1");
//        aOptional1.addToBOptional(bOptional1);
//        aOptional1.addToBBoptional(bbOptional1);
//        UMLG.get().commit();
//
//        PropertyTree aOptionalPT = PropertyTree.from("AOptional");
//        aOptionalPT.addChild(AOptional.AOptionalRuntimePropertyEnum.bOptional);
//        aOptionalPT.addChild(AOptional.AOptionalRuntimePropertyEnum.bBoptional);
//        List<AOptional> aOptionals = UMLG.get().get(aOptionalPT);
//
//        for (AOptional aOptional : aOptionals) {
//            for (BOptional bOptional : aOptional.getBOptional()) {
//                System.out.println(bOptional.getName());
//            }
//            for (BBOptional bbOptional : aOptional.getBBoptional()) {
//                System.out.println(bbOptional.getName());
//            }
//        }
//    }
//
//    @Test
//    public void test3Levels() {
//        AOptional aOptional1 = new AOptional();
//        aOptional1.setName("aOptional1");
//        BOptional bOptional1 = new BOptional();
//        bOptional1.setName("bOptional1");
//        BOptional bOptional2 = new BOptional();
//        bOptional2.setName("bOptional2");
//        COptional cOptional1 = new COptional();
//        cOptional1.setName("cOptional1");
//        COptional cOptional2 = new COptional();
//        cOptional2.setName("cOptional2");
//        COptional cOptional3 = new COptional();
//        cOptional3.setName("cOptional3");
//        COptional cOptional11 = new COptional();
//        cOptional11.setName("cOptional11");
//        COptional cOptional22 = new COptional();
//        cOptional22.setName("cOptional22");
//        COptional cOptional33 = new COptional();
//        cOptional33.setName("cOptional33");
//        aOptional1.addToBOptional(bOptional1);
//        aOptional1.addToBOptional(bOptional2);
//        bOptional1.addToCOptional(cOptional1);
//        bOptional1.addToCOptional(cOptional2);
//        bOptional1.addToCOptional(cOptional3);
//        bOptional2.addToCOptional(cOptional11);
//        bOptional2.addToCOptional(cOptional22);
//        bOptional2.addToCOptional(cOptional33);
//        UMLG.get().commit();
//
//        PropertyTree aOptionalPT = PropertyTree.from("AOptional");
//        PropertyTree bOptionalPT = PropertyTree.from(AOptional.AOptionalRuntimePropertyEnum.bOptional);
//        aOptionalPT.addChild(bOptionalPT);
//        aOptionalPT.addChild(AOptional.AOptionalRuntimePropertyEnum.bBoptional);
//        bOptionalPT.addChild(BOptional.BOptionalRuntimePropertyEnum.cOptional);
//        List<AOptional> aOptionals = UMLG.get().get(aOptionalPT);
//        assertEquals(1, aOptionals.size());
//        for (AOptional aOptional : aOptionals) {
//            assertEquals(2, aOptional.getBOptional().size());
//            for (BOptional bOptional : aOptional.getBOptional()) {
//                assertEquals(3, bOptional.getCOptional().size());
//                for (COptional cOptional : bOptional.getCOptional()) {
//                    System.out.println(cOptional.getName());
//                }
//            }
//            for (BBOptional bbOptional : aOptional.getBBoptional()) {
//                System.out.println(bbOptional.getName());
//            }
//        }
//    }

    @Test
    public void testHandFingerNail() {
        Assume.assumeTrue(UMLG.get().supportsBatchMode());
        God god = new God();
        god.setName("god");
        for (int j = 0; j < 1; j++) {
            Hand hand = new Hand();
            god.addToHand(hand);
            hand.setName("hand_" + j);
            for (int k = 0; k < 1; k++) {
                Finger finger = new Finger();
                hand.addToFinger(finger);
                finger.setName("finger_" + k);
            }
        }
        UMLG.get().commit();
        god.delete();
        UMLG.get().commit();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        UMLG.get().batchModeOn();
        int count = 10_000;
        for (int i = 1; i < count + 1; i++) {
            god = new God();
            god.setName("god_" + i);
            for (int j = 0; j < 2; j++) {
                Hand hand = new Hand(god);
                hand.setName("hand_" + j);
                for (int k = 0; k < 5; k++) {
                    Finger finger = new Finger(hand);
                    finger.setName("finger_" + k);
                }
            }
        }
        UMLG.get().commit();
        stopWatch.stop();
        System.out.println("Time to insert " + stopWatch.toString());
        stopWatch.reset();

        System.out.println("start sleeping");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("done sleeping");

        stopWatch.start();
        PropertyTree godPT = PropertyTree.from("God");
        PropertyTree handPT = PropertyTree.from(hand);
        handPT.addChild(finger);
        godPT.addChild(handPT);
        List<God> gods = UMLG.get().get(godPT);
        assertEquals(count, gods.size());
        for (God godAgain : gods) {
            assertEquals(2, godAgain.getHand().size());
            for (Hand hand : godAgain.getHand()) {
                assertEquals(5, hand.getFinger().size());
            }
        }
        stopWatch.stop();
        System.out.println("Time to query " + stopWatch.toString());

//        System.out.println("start sleeping");
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("done sleeping");
    }
}
