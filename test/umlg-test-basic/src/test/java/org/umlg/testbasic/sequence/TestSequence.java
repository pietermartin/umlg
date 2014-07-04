package org.umlg.testbasic.sequence;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.sequence.SequenceNotUniqueRoot;
import org.umlg.sequence.SequenceNotUniqueTest;
import org.umlg.sequence.SequenceRoot;
import org.umlg.sequence.SequenceTest;

public class TestSequence extends BaseLocalDbTest {

    @Test
    public void testSequenceInverseLogic() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTest sequenceTest1 = new SequenceTest(sequenceRoot);
        sequenceTest1.setName("sequenceTest1");
        SequenceTest sequenceTest2 = new SequenceTest(sequenceRoot);
        sequenceTest2.setName("sequenceTest2");
        SequenceTest sequenceTest3 = new SequenceTest(sequenceRoot);
        sequenceTest3.setName("sequenceTest3");
        db.commit();
        SequenceRoot sequenceRootTest = new SequenceRoot(sequenceRoot.getVertex());
        Assert.assertEquals("sequenceTest3", sequenceRootTest.getSequenceTest().get(2).getName());
        SequenceTest sequenceTestTest = new SequenceTest(sequenceTest2.getVertex());
        Assert.assertEquals("sequenceRoot", sequenceTestTest.getSequenceRoot().getName());
        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8 + 4, countEdges());
    }

    @Test
    public void testSequenceNormalLogic() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTest sequenceTest1 = new SequenceTest(true);
        sequenceTest1.setName("sequenceTest1");
        sequenceRoot.getSequenceTest().add(sequenceTest1);
        SequenceTest sequenceTest2 = new SequenceTest(true);
        sequenceTest2.setName("sequenceTest2");
        sequenceRoot.getSequenceTest().add(sequenceTest2);
        SequenceTest sequenceTest3 = new SequenceTest(true);
        sequenceTest3.setName("sequenceTest3");
        sequenceRoot.getSequenceTest().add(sequenceTest3);
        db.commit();
        SequenceRoot sequenceRootTest = new SequenceRoot(sequenceRoot.getVertex());
        Assert.assertEquals("sequenceTest3", sequenceRootTest.getSequenceTest().get(2).getName());
        SequenceTest sequenceTestTest = new SequenceTest(sequenceTest2.getVertex());
        Assert.assertEquals("sequenceRoot", sequenceTestTest.getSequenceRoot().getName());
        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8 + 4, countEdges());
    }

    @Test
    public void testSequenceUsingIndexedPosition() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTest sequenceTest1 = new SequenceTest(true);
        sequenceTest1.setName("sequenceTest1");
        sequenceRoot.getSequenceTest().add(0, sequenceTest1);
        SequenceTest sequenceTest2 = new SequenceTest(true);
        sequenceTest2.setName("sequenceTest2");
        sequenceRoot.getSequenceTest().add(1, sequenceTest2);
        SequenceTest sequenceTest3 = new SequenceTest(true);
        sequenceTest3.setName("sequenceTest3");
        sequenceRoot.getSequenceTest().add(2, sequenceTest3);

        SequenceTest sequenceTest2_1 = new SequenceTest(true);
        sequenceTest2_1.setName("sequenceTest2_1");
        sequenceRoot.getSequenceTest().add(2, sequenceTest2_1);

        SequenceTest sequenceTest1_0 = new SequenceTest(true);
        sequenceTest1_0.setName("sequenceTest1_0");
        sequenceRoot.getSequenceTest().add(0, sequenceTest1_0);

        SequenceTest sequenceTest3_1 = new SequenceTest(true);
        sequenceTest3_1.setName("sequenceTest3_1");
        sequenceRoot.getSequenceTest().add(sequenceTest3_1);

        db.commit();
        SequenceRoot sequenceRootTest = new SequenceRoot(sequenceRoot.getVertex());
        Assert.assertEquals("sequenceTest3_1", sequenceRootTest.getSequenceTest().get(5).getName());
        Assert.assertEquals("sequenceTest3", sequenceRootTest.getSequenceTest().get(4).getName());
        Assert.assertEquals("sequenceTest2_1", sequenceRootTest.getSequenceTest().get(3).getName());
        Assert.assertEquals("sequenceTest2", sequenceRootTest.getSequenceTest().get(2).getName());
        Assert.assertEquals("sequenceTest1", sequenceRootTest.getSequenceTest().get(1).getName());
        Assert.assertEquals("sequenceTest1_0", sequenceRootTest.getSequenceTest().get(0).getName());
        SequenceTest sequenceTestTest = new SequenceTest(sequenceTest2.getVertex());
        Assert.assertEquals("sequenceRoot", sequenceTestTest.getSequenceRoot().getName());

        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(14 + 7, countEdges());
    }

    @Test
    public void testSequenceRemovalInverse() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTest sequenceTest1 = new SequenceTest(true);
        sequenceTest1.setName("sequenceTest1");
        sequenceRoot.getSequenceTest().add(0, sequenceTest1);
        SequenceTest sequenceTest2 = new SequenceTest(true);
        sequenceTest2.setName("sequenceTest2");
        sequenceRoot.getSequenceTest().add(1, sequenceTest2);
        SequenceTest sequenceTest3 = new SequenceTest(true);
        sequenceTest3.setName("sequenceTest3");
        sequenceRoot.getSequenceTest().add(2, sequenceTest3);
        db.commit();

        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8 + 4, countEdges());

        sequenceTest1 = new SequenceTest(sequenceTest1.getVertex());
        sequenceTest1.removeFromSequenceRoot(sequenceRoot);
        sequenceTest1.delete();

        db.commit();

        Assert.assertEquals(3, countVertices());
        Assert.assertEquals(6 + 3, countEdges());

        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
        Assert.assertEquals(2, sequenceRoot.getSequenceTest().size());
        Assert.assertEquals("sequenceTest2", sequenceRoot.getSequenceTest().get(0).getName());
        Assert.assertEquals("sequenceTest3", sequenceRoot.getSequenceTest().get(1).getName());


    }

    @Test
    public void testSequenceRemoval() {
        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        SequenceTest sequenceTest1 = new SequenceTest(true);
        sequenceTest1.setName("sequenceTest1");
        sequenceRoot.getSequenceTest().add(0, sequenceTest1);
        SequenceTest sequenceTest2 = new SequenceTest(true);
        sequenceTest2.setName("sequenceTest2");
        sequenceRoot.getSequenceTest().add(1, sequenceTest2);
        SequenceTest sequenceTest3 = new SequenceTest(true);
        sequenceTest3.setName("sequenceTest3");
        sequenceRoot.getSequenceTest().add(2, sequenceTest3);
        db.commit();

        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8 + 4, countEdges());
        Assert.assertEquals(3, sequenceRoot.getSequenceTest().size());

        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
        SequenceTest removed = sequenceRoot.getSequenceTest().remove(0);
        removed.delete();
        db.commit();

        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
        Assert.assertEquals(2, sequenceRoot.getSequenceTest().size());
        Assert.assertEquals("sequenceTest2", sequenceRoot.getSequenceTest().get(0).getName());
        Assert.assertEquals("sequenceTest3", sequenceRoot.getSequenceTest().get(1).getName());

        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
        removed = sequenceRoot.getSequenceTest().remove(1);
        removed.delete();
        removed = sequenceRoot.getSequenceTest().remove(0);
        removed.delete();
        db.commit();

        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1 + 1, countEdges());
        Assert.assertEquals(0, sequenceRoot.getSequenceTest().size());

    }

    @Test
    public void testDuplicateRemoval() {

        SequenceNotUniqueRoot sequenceNotUniqueRoot = new SequenceNotUniqueRoot(true);
        sequenceNotUniqueRoot.setName("sequenceNotUniqueRoot");
        SequenceNotUniqueTest sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest1.setName("sequenceNotUniqueTest1");

        db.commit();
        Assert.assertEquals(3, countVertices());
        Assert.assertEquals(5 + 2, countEdges());

        SequenceNotUniqueTest sequenceNotUniqueTest2 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest2.setName("sequenceNotUniqueTest2");
        SequenceNotUniqueTest sequenceNotUniqueTest3 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest3.setName("sequenceNotUniqueTest3");
        db.commit();
        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(11 + 4, countEdges());
        sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueTest1.getVertex());
        Assert.assertEquals("sequenceNotUniqueRoot", sequenceNotUniqueTest1.getSequenceNotUniqueRoot().getName());
        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(0).getName());
        Assert.assertEquals("sequenceNotUniqueTest2", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(1).getName());
        Assert.assertEquals("sequenceNotUniqueTest3", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(2).getName());

        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueTest1.getVertex());
        sequenceNotUniqueRoot.removeFromSequenceNotUniqueTest(sequenceNotUniqueTest1);
        sequenceNotUniqueTest1.delete();
        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(8 + 3, countEdges());

        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        sequenceNotUniqueTest2 = new SequenceNotUniqueTest(sequenceNotUniqueTest2.getVertex());
        sequenceNotUniqueRoot.removeFromSequenceNotUniqueTest(sequenceNotUniqueTest2);
        sequenceNotUniqueTest2.delete();
        sequenceNotUniqueTest3 = new SequenceNotUniqueTest(sequenceNotUniqueTest3.getVertex());
        sequenceNotUniqueRoot.removeFromSequenceNotUniqueTest(sequenceNotUniqueTest3);
        sequenceNotUniqueTest3.delete();

        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1 + 1, countEdges());

    }

    @Test
    public void testDuplicateRemoval2() {

        SequenceNotUniqueRoot sequenceNotUniqueRoot = new SequenceNotUniqueRoot(true);
        sequenceNotUniqueRoot.setName("sequenceNotUniqueRoot");
        SequenceNotUniqueTest sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest1.setName("sequenceNotUniqueTest1");
        SequenceNotUniqueTest sequenceNotUniqueTest2 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest2.setName("sequenceNotUniqueTest2");
        SequenceNotUniqueTest sequenceNotUniqueTest3 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest3.setName("sequenceNotUniqueTest3");
        db.commit();
        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(11 + 4, countEdges());

        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(2, sequenceNotUniqueTest1);
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(2, sequenceNotUniqueTest1);
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(1, sequenceNotUniqueTest2);
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(1, sequenceNotUniqueTest2);
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(0, sequenceNotUniqueTest3);
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(0, sequenceNotUniqueTest3);
        db.commit();
        Assert.assertEquals(9, countVertices());
        Assert.assertEquals(25 + 4, countEdges());

        sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueTest1.getVertex());
        sequenceNotUniqueRoot = sequenceNotUniqueTest1.getSequenceNotUniqueRoot();
        Assert.assertEquals(9, sequenceNotUniqueRoot.getSequenceNotUniqueTest().size());
        Assert.assertEquals("sequenceNotUniqueTest3", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(0).getName());
        Assert.assertEquals("sequenceNotUniqueTest3", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(1).getName());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(2).getName());
        Assert.assertEquals("sequenceNotUniqueTest2", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(3).getName());
        Assert.assertEquals("sequenceNotUniqueTest2", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(4).getName());
        Assert.assertEquals("sequenceNotUniqueTest2", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(5).getName());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(6).getName());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(7).getName());
        Assert.assertEquals("sequenceNotUniqueTest3", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(8).getName());

        db.commit();
        Assert.assertEquals(9, countVertices());
        Assert.assertEquals(25 + 4, countEdges());

        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueTest1.getVertex());
        sequenceNotUniqueRoot.removeFromSequenceNotUniqueTest(sequenceNotUniqueTest1);
        db.commit();

        for (SequenceNotUniqueTest t : sequenceNotUniqueRoot.getSequenceNotUniqueTest()) {
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaa   " + t.getName());
        }


        Assert.assertEquals(8, countVertices());
        Assert.assertEquals(22 + 4, countEdges());

        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        sequenceNotUniqueTest2 = new SequenceNotUniqueTest(sequenceNotUniqueTest2.getVertex());
        sequenceNotUniqueRoot.removeFromSequenceNotUniqueTest(sequenceNotUniqueTest2);

        db.commit();
        Assert.assertEquals(8, countVertices());
        Assert.assertEquals(20 + 4, countEdges());

        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueTest1.getVertex());
        sequenceNotUniqueRoot.removeFromSequenceNotUniqueTest(sequenceNotUniqueTest1);
        db.commit();

        Assert.assertEquals(8, countVertices());
        Assert.assertEquals(18 + 4, countEdges());

        sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueTest1.getVertex());
        sequenceNotUniqueTest1.delete();
        db.commit();

        Assert.assertEquals(6, countVertices());
        Assert.assertEquals(15 + 3, countEdges());

    }

    @Test
    public void testDuplicateRemovalAtIndex() {

        SequenceNotUniqueRoot sequenceNotUniqueRoot = new SequenceNotUniqueRoot(true);
        sequenceNotUniqueRoot.setName("sequenceNotUniqueRoot");
        SequenceNotUniqueTest sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest1.setName("sequenceNotUniqueTest1");
        SequenceNotUniqueTest sequenceNotUniqueTest2 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest2.setName("sequenceNotUniqueTest2");
        SequenceNotUniqueTest sequenceNotUniqueTest3 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest3.setName("sequenceNotUniqueTest3");
        db.commit();
        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(11 + 4, countEdges());
        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(0).getName());
        Assert.assertEquals("sequenceNotUniqueTest2", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(1).getName());
        Assert.assertEquals("sequenceNotUniqueTest3", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(2).getName());

        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().remove(0);
        sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueTest1.getVertex());
        sequenceNotUniqueTest1.delete();
        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(8 + 3, countEdges());

        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().remove(1);
        sequenceNotUniqueTest3 = new SequenceNotUniqueTest(sequenceNotUniqueTest3.getVertex());
        sequenceNotUniqueTest3.delete();
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().remove(0);
        sequenceNotUniqueTest2 = new SequenceNotUniqueTest(sequenceNotUniqueTest2.getVertex());
        sequenceNotUniqueTest2.delete();

        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1 + 1, countEdges());

    }

    @Test
    public void testDuplicateDeletion() {

        SequenceNotUniqueRoot sequenceNotUniqueRoot = new SequenceNotUniqueRoot(true);
        sequenceNotUniqueRoot.setName("sequenceNotUniqueRoot");
        SequenceNotUniqueTest sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest1.setName("sequenceNotUniqueTest1");
        SequenceNotUniqueTest sequenceNotUniqueTest2 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest2.setName("sequenceNotUniqueTest2");
        SequenceNotUniqueTest sequenceNotUniqueTest3 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest3.setName("sequenceNotUniqueTest3");
        db.commit();
        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(11 + 4, countEdges());
        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(0).getName());
        Assert.assertEquals("sequenceNotUniqueTest2", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(1).getName());
        Assert.assertEquals("sequenceNotUniqueTest3", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(2).getName());

        sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueTest1.getVertex());
        sequenceNotUniqueTest1.delete();
        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(8 + 3, countEdges());

        sequenceNotUniqueTest3 = new SequenceNotUniqueTest(sequenceNotUniqueTest3.getVertex());
        sequenceNotUniqueTest3.delete();
        sequenceNotUniqueTest2 = new SequenceNotUniqueTest(sequenceNotUniqueTest2.getVertex());
        sequenceNotUniqueTest2.delete();

        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1 + 1, countEdges());
    }


    //100 = 0:00:14.573
    //1000 = 0:02:27.220
    //after
    //1000 = 0:00:00.936
    @Test
    public void speedTest() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");
        for (int i = 0; i < 1000; i++) {
            SequenceTest sequenceTest1 = new SequenceTest(sequenceRoot);
            sequenceTest1.setName("sequenceTest" + i);

            if (i % 1000 == 0) {
                db.commit();
            }
            if (i % 10000 == 0) {
                System.out.println(i);
            }
        }
        db.commit();
        stopWatch.stop();
        System.out.println(stopWatch.toString());

        stopWatch.reset();
        stopWatch.start();
//        sequenceRoot = new SequenceRoot(sequenceRoot.getVertex());
        int i = 0;
        for (SequenceTest st : sequenceRoot.getSequenceTest()) {
            if (i++ % 10000 == 0) {
                System.out.println(st.getName());
            }
        }
        Assert.assertEquals(1000, sequenceRoot.getSequenceTest().size());
        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    public void testNotUniqueSequenceInverse() {
        SequenceNotUniqueRoot sequenceNotUniqueRoot = new SequenceNotUniqueRoot(true);
        sequenceNotUniqueRoot.setName("sequenceNotUniqueRoot");
        SequenceNotUniqueTest sequenceNotUniqueTest1 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest1.setName("sequenceNotUniqueTest1");
        SequenceNotUniqueTest sequenceNotUniqueTest2 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest2.setName("sequenceNotUniqueTest2");
        SequenceNotUniqueTest sequenceNotUniqueTest3 = new SequenceNotUniqueTest(sequenceNotUniqueRoot);
        sequenceNotUniqueTest3.setName("sequenceNotUniqueTest3");
        db.commit();
        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(11 + 4, countEdges());
        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        Assert.assertEquals(3, sequenceNotUniqueRoot.getSequenceNotUniqueTest().size());
        Assert.assertEquals("sequenceNotUniqueTest3", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(2).getName());
        Assert.assertEquals("sequenceNotUniqueTest2", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(1).getName());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(0).getName());
    }

    @Test
    public void testNotUniqueSequence() {
        SequenceNotUniqueRoot sequenceNotUniqueRoot = new SequenceNotUniqueRoot(true);
        sequenceNotUniqueRoot.setName("sequenceNotUniqueRoot");
        SequenceNotUniqueTest sequenceNotUniqueTest1 = new SequenceNotUniqueTest(true);
        sequenceNotUniqueTest1.setName("sequenceNotUniqueTest1");
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(sequenceNotUniqueTest1);
        SequenceNotUniqueTest sequenceNotUniqueTest2 = new SequenceNotUniqueTest(true);
        sequenceNotUniqueTest2.setName("sequenceNotUniqueTest2");
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(sequenceNotUniqueTest2);
        SequenceNotUniqueTest sequenceNotUniqueTest3 = new SequenceNotUniqueTest(true);
        sequenceNotUniqueTest3.setName("sequenceNotUniqueTest3");
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(sequenceNotUniqueTest3);
        db.commit();
        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(11 + 4, countEdges());
        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        Assert.assertEquals(3, sequenceNotUniqueRoot.getSequenceNotUniqueTest().size());
        Assert.assertEquals("sequenceNotUniqueTest3", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(2).getName());
        Assert.assertEquals("sequenceNotUniqueTest2", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(1).getName());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(0).getName());
    }

    @Test
    public void testDuplicates() {
        SequenceNotUniqueRoot sequenceNotUniqueRoot = new SequenceNotUniqueRoot(true);
        sequenceNotUniqueRoot.setName("name");
        SequenceNotUniqueTest sequenceNotUniqueTest1 = new SequenceNotUniqueTest(true);
        sequenceNotUniqueTest1.setName("sequenceNotUniqueTest1");
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(sequenceNotUniqueTest1);
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(sequenceNotUniqueTest1);
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(sequenceNotUniqueTest1);
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(sequenceNotUniqueTest1);
        SequenceNotUniqueTest sequenceNotUniqueTest2 = new SequenceNotUniqueTest(true);
        sequenceNotUniqueTest2.setName("sequenceNotUniqueTest2");
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(sequenceNotUniqueTest2);
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(sequenceNotUniqueTest1);
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(sequenceNotUniqueTest1);
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(sequenceNotUniqueTest1);
        sequenceNotUniqueRoot.getSequenceNotUniqueTest().add(sequenceNotUniqueTest1);

        db.commit();

        sequenceNotUniqueRoot = new SequenceNotUniqueRoot(sequenceNotUniqueRoot.getVertex());
        Assert.assertEquals(9, sequenceNotUniqueRoot.getSequenceNotUniqueTest().size());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(0).getName());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(1).getName());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(2).getName());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(3).getName());
        Assert.assertEquals("sequenceNotUniqueTest2", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(4).getName());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(5).getName());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(6).getName());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(7).getName());
        Assert.assertEquals("sequenceNotUniqueTest1", sequenceNotUniqueRoot.getSequenceNotUniqueTest().get(8).getName());
    }

}
