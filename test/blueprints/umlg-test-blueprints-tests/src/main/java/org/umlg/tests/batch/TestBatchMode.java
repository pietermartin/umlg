package org.umlg.tests.batch;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2014/09/14
 * Time: 12:43 PM
 */
public class TestBatchMode extends BaseLocalDbTest {

    @Test
    public void testBatchModeIgnoreInverse() {
        Assume.assumeTrue(UMLG.get().supportsBatchMode());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        God god = new God(true);
        god.setName("god");
        UMLG.get().commit();
        UMLG.get().batchModeOn();
        for (int i = 0; i < 10000; i++) {
            Universe universe1 = new Universe();
            universe1.addToGodIgnoreInverse(god);
            universe1.setName("universe" + i);
            SpaceTime st = new SpaceTime();
            universe1.addToSpaceTimeIgnoreInverse(st);
            Space s = new Space();
            st.addToSpaceIgnoreInverse(s);
            Time t = new Time();
            st.addToTimeIgnoreInverse(t);
        }
        System.out.println("start committing");
        UMLG.get().commit();
        god.reload();
        stopWatch.stop();
        System.out.println("time taken = " + stopWatch.toString());
        stopWatch.reset();
        stopWatch.start();
        Assert.assertEquals(1, God.allInstances().size());
        Assert.assertEquals(10000, god.getUniverse().size());
        Assert.assertEquals(10000, Universe.allInstances().size());
        Assert.assertEquals(10000, SpaceTime.allInstances().size());
        Assert.assertEquals(10000, Space.allInstances().size());
        Assert.assertEquals(10000, Time.allInstances().size());
        List<String> universeNames = new ArrayList<>();
        for (Universe universe : god.getUniverse()) {
            universeNames.add(universe.getName());
            Assert.assertNotNull(universe.getSpaceTime());
            Assert.assertNotNull(universe.getSpaceTime().getSpace());
            Assert.assertNotNull(universe.getSpaceTime().getTime());
        }
        for (int i = 0; i < 10000; i++) {
            universeNames.remove("universe" + i);
        }
        Assert.assertTrue(universeNames.isEmpty());
        stopWatch.stop();
        System.out.println("time taken = " + stopWatch.toString());
    }

//    @Test
//    public void testBatchMode() {
//        Assume.assumeTrue(UMLG.get().supportsBatchMode());
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        God god = new God(true);
//        god.setName("god");
//        UMLG.get().commit();
//        UMLG.get().batchModeOn();
//        for (int i = 0; i < 10000; i++) {
//            Universe universe1 = new Universe();
//            universe1.addToGod(god);
//            universe1.setName("universe" + i);
//            SpaceTime st = new SpaceTime();
//            universe1.addToSpaceTime(st);
//            Space s = new Space();
//            st.addToSpace(s);
//            Time t = new Time();
//            st.addToTime(t);
//        }
//        System.out.println("start committing");
//        UMLG.get().commit();
//        god.reload();
//        stopWatch.stop();
//        System.out.println("time taken = " + stopWatch.toString());
//        stopWatch.reset();
//        stopWatch.start();
//        Assert.assertEquals(1, God.allInstances().size());
//        Assert.assertEquals(10000, god.getUniverse().size());
//        Assert.assertEquals(10000, Universe.allInstances().size());
//        Assert.assertEquals(10000, SpaceTime.allInstances().size());
//        Assert.assertEquals(10000, Space.allInstances().size());
//        Assert.assertEquals(10000, Time.allInstances().size());
//        List<String> universeNames = new ArrayList<>();
//        for (Universe universe : god.getUniverse()) {
//            universeNames.add(universe.getName());
//            Assert.assertNotNull(universe.getSpaceTime());
//            Assert.assertNotNull(universe.getSpaceTime().getSpace());
//            Assert.assertNotNull(universe.getSpaceTime().getTime());
//        }
//        for (int i = 0; i < 10000; i++) {
//            universeNames.remove("universe" + i);
//        }
//        Assert.assertTrue(universeNames.isEmpty());
//        stopWatch.stop();
//        System.out.println("time taken = " + stopWatch.toString());
//    }
//
//    @SuppressWarnings("unused")
//    @Test
//    public void testAllInstances1WithFilter() {
//        Assume.assumeTrue(UMLG.get().supportsBatchMode());
//        God god = new God(true);
//        god.setName("THEGOD");
//        UMLG.get().commit();
//        UMLG.get().batchModeOn();
//        for (int i = 0; i < 10; i++) {
//            Universe universe1 = new Universe(god);
//            universe1.setName("universe");
//            SpaceTime st = new SpaceTime(universe1);
//            Space s = new Space(st);
//            Time t = new Time(st);
//        }
//
//        db.commit();
//        Assert.assertEquals(10, Universe.allInstances(new Filter<Universe>() {
//            @Override
//            public boolean filter(Universe t) {
//                return t.getName().equals("universe");
//            }
//        }).size());
//    }
//
//    @Test
//    public void testEnumeration() {
//        Assume.assumeTrue(UMLG.get().supportsBatchMode());
//        UMLG.get().batchModeOn();
//        God g = new God(true);
//        g.setName("g");
//        Object s = Arrays.asList(new Object[]{REASON.BAD, REASON.GOOD});
//        UmlgSet<REASON> reasons = new UmlgMemorySet<REASON>((Collection<REASON>) s);
//        g.addToREASON(reasons);
//        db.commit();
//        Assert.assertEquals(2, g.getREASON().size());
//        Assert.assertEquals(1, God.allInstances(new Filter<God>() {
//            @Override
//            public boolean filter(God t) {
//                return t.getREASON().contains(REASON.BAD);
//            }
//        }).size());
//    }
//
//    @Test
//    public void testQueryInBatchMode() {
//        Assume.assumeTrue(UMLG.get().supportsBatchMode());
//        God god = new God(true);
//        god.setName("THEGOD");
//        UMLG.get().commit();
//        UMLG.get().batchModeOn();
//        for (int i = 0; i < 10; i++) {
//            Universe universe1 = new Universe(god);
//            universe1.setName("universe");
//            SpaceTime st = new SpaceTime(universe1);
//            Space s = new Space(st);
//            Time t = new Time(st);
//        }
//        Assert.assertEquals(10, Universe.allInstances(new Filter<Universe>() {
//            @Override
//            public boolean filter(Universe t) {
//                return t.getName().equals("universe");
//            }
//        }).size());
//    }

}
