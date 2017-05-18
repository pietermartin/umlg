package org.umlg.tests.globalget;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.HasContainer;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.umlg.collectiontest.Finger;
import org.umlg.collectiontest.Hand;
import org.umlg.concretetest.God;
import org.umlg.optional.AOptional;
import org.umlg.optional.BBOptional;
import org.umlg.optional.BOptional;
import org.umlg.optional.COptional;
import org.umlg.rootallinstances.TopRoot;
import org.umlg.rootallinstances.TopRootChild;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.persistent.PropertyTree;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Date: 2016/05/28
 * Time: 8:42 PM
 */
public class TestGlobalGet extends BaseLocalDbTest {

    @Test
    public void test() {
        TopRoot topRoot1 = new TopRoot();
        topRoot1.setNameUnique("a");
        topRoot1.setIndexedName("aaa");
        TopRootChild topRootChild1 = new TopRootChild(topRoot1);
        topRootChild1.setName("topRootChild1");

        TopRoot topRoot2 = new TopRoot();
        topRoot2.setNameUnique("ab");
        topRoot2.setIndexedName("aaab");
        TopRootChild topRootChild2 = new TopRootChild(topRoot2);
        topRootChild2.setName("topRootChild2");
        UMLG.get().commit();

        //This must happen with only 2 queries to the db, 1 really but the optional 2
        PropertyTree topRootPt = PropertyTree.from("TopRoot");
        topRootPt.addChild(TopRoot.TopRootRuntimePropertyEnum.topRootChild);
        List<TopRoot> topRoots = UMLG.get().get(topRootPt);
        for (TopRoot topRoot : topRoots) {
            for (TopRootChild topRootChild : topRoot.getTopRootChild()) {
                System.out.println(topRootChild.getName());
            }
        }
        Assert.assertTrue(topRoots.containsAll(Arrays.asList(topRoot1, topRoot2)));
    }


    @Test
    public void test3Levels() {
        AOptional aOptional1 = new AOptional();
        aOptional1.setName("aOptional1");

        BOptional bOptional1 = new BOptional();
        bOptional1.setName("bOptional1");
        BOptional bOptional2 = new BOptional();
        bOptional2.setName("bOptional2");
        BOptional bOptional3 = new BOptional();
        bOptional3.setName("bOptional3");

        BBOptional bbOptional1 = new BBOptional();
        bbOptional1.setName("bbOptional1");
        BBOptional bbOptional2 = new BBOptional();
        bbOptional2.setName("bbOptional2");
        BBOptional bbOptional3 = new BBOptional();
        bbOptional3.setName("bbOptional3");

        COptional cOptional1 = new COptional();
        cOptional1.setName("cOptional1");
        COptional cOptional2 = new COptional();
        cOptional2.setName("cOptional2");
        COptional cOptional3 = new COptional();
        cOptional3.setName("cOptional3");
        COptional cOptional11 = new COptional();
        cOptional11.setName("cOptional11");
        COptional cOptional22 = new COptional();
        cOptional22.setName("cOptional22");
        COptional cOptional33 = new COptional();
        cOptional33.setName("cOptional33");

        aOptional1.addToBOptional(bOptional1);
        aOptional1.addToBOptional(bOptional2);
        aOptional1.addToBOptional(bOptional3);

        aOptional1.addToBBoptional(bbOptional1);
        aOptional1.addToBBoptional(bbOptional2);
        aOptional1.addToBBoptional(bbOptional3);

        bOptional1.addToCOptional(cOptional1);
        bOptional1.addToCOptional(cOptional2);
        bOptional1.addToCOptional(cOptional3);
        bOptional2.addToCOptional(cOptional11);
        bOptional2.addToCOptional(cOptional22);
        bOptional2.addToCOptional(cOptional33);
        UMLG.get().commit();

        PropertyTree aOptionalPT = PropertyTree.from("AOptional");
        aOptionalPT.addHasContainer(new HasContainer("name", P.eq("aOptional1")));

        PropertyTree bOptionalPT = PropertyTree.from(AOptional.AOptionalRuntimePropertyEnum.bOptional);
        bOptionalPT.addHasContainer(new HasContainer("name", P.within("bOptional1", "bOptional2", "bOptional3")));
        aOptionalPT.addChild(bOptionalPT);

        PropertyTree bbOptionalPT = PropertyTree.from(AOptional.AOptionalRuntimePropertyEnum.bBoptional);
        bbOptionalPT.addHasContainer(new HasContainer("name", P.within("bbOptional1", "bbOptional2", "bbOptional3")));
        aOptionalPT.addChild(bbOptionalPT);

        List<AOptional> aOptionals = UMLG.get().get(aOptionalPT);
        assertEquals(1, aOptionals.size());
        for (AOptional aOptional : aOptionals) {
            assertEquals(3, aOptional.getBOptional().size());
            for (BOptional bOptional : aOptional.getBOptional()) {
                if (bOptional.getName().equals("bOptional1")) {
                    assertEquals(3, bOptional.getCOptional().size());
                } else if (bOptional.getName().equals("bOptional2")) {
                    assertEquals(3, bOptional.getCOptional().size());
                } else {
                    assertEquals(0, bOptional.getCOptional().size());
                }
                for (COptional cOptional : bOptional.getCOptional()) {
                    System.out.println(cOptional.getName());
                }
            }
            assertEquals(3, aOptional.getBBoptional().size());
        }
    }

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
        int count = 10;
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

        stopWatch.start();
        PropertyTree godPT = PropertyTree.from("God");
        PropertyTree handPT = PropertyTree.from(God.GodRuntimePropertyEnum.hand);
        godPT.addChild(handPT);
        PropertyTree fingerPT = PropertyTree.from(Hand.HandRuntimePropertyEnum.finger);
        handPT.addChild(fingerPT);

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

    }

}
