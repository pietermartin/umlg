package org.umlg.tests.batch;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/09/14
 * Time: 12:43 PM
 */
public class TestBatchMode extends BaseLocalDbTest {
    
    @Test
    public void testPropertyNull() {
        God god = new God(true);
        db.commit();
        db.batchModeOn();
        god = new God(god.getVertex());
        Assert.assertNull(god.getName3());
    }

//    @Test
//    public void testBatchModeIgnoreInverse() {
//        Assume.assumeTrue(UMLG.get().supportsBatchMode());
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        God god = new God(true);
//        god.setName("god");
//        UMLG.get().commit();
//        UMLG.get().batchModeOn();
//        for (int i = 0; i < 10_000; i++) {
//            Universe universe1 = new Universe();
//            universe1.addToGodIgnoreInverse(god);
//            universe1.setName("universe" + i);
//            SpaceTime st = new SpaceTime();
//            st.addToUniverseIgnoreInverse(universe1);
//            Space s = new Space();
//            s.addToSpaceTimeIgnoreInverse(st);
//            Time t = new Time();
//            t.addToSpaceTimeIgnoreInverse(st);
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
//
//        List<String> universeNames = new ArrayList<>();
//
//        PropertyTree godPT = PropertyTree.from("God");
//        PropertyTree universePT = PropertyTree.from(God.GodRuntimePropertyEnum.universe);
//        godPT.addChild(universePT);
//        PropertyTree spaceTimePT = PropertyTree.from(Universe.UniverseRuntimePropertyEnum.spaceTime);
//        universePT.addChild(spaceTimePT);
//        PropertyTree spacePT = PropertyTree.from(SpaceTime.SpaceTimeRuntimePropertyEnum.space);
//        spaceTimePT.addChild(spacePT);
//        PropertyTree timePT = PropertyTree.from(SpaceTime.SpaceTimeRuntimePropertyEnum.time);
//        spaceTimePT.addChild(timePT);
//        godPT.addHasContainer(new HasContainer("name", P.eq("god")));
//
//        God godAgain = UMLG.get().<God>get(godPT).get(0);
//        for (Universe universe : godAgain.getUniverse()) {
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
//            god.addToUniverse(universe1);
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
