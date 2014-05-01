package org.umlg.testbasic;

import org.junit.Assert;

import org.apache.commons.lang.time.StopWatch;
import org.joda.time.DateTime;
import org.junit.Test;
import org.umlg.Many;
import org.umlg.One;
import org.umlg.runtime.test.BaseLocalDbTest;

public class TestOneToMany extends BaseLocalDbTest {

    @Test
    public void testNeo4jSpeed() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        One g = new One(true);
        g.setName("g");
        g.setDateTime(new DateTime());
        for (int i = 0; i < 10000; i++) {
            Many universe = new Many(g);
            universe.setName("u");
        }
        db.commit();
        org.junit.Assert.assertEquals(10001, countVertices());
        org.junit.Assert.assertEquals(10001 + 10001, countEdges());
        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    public void testBasicOneToMany() {
        One one = new One(true);
        one.setName("asd");
        one.setDateTime(new DateTime());
        Many many = new Many(one);
        db.commit();
        Assert.assertEquals(2, countVertices());
        Assert.assertEquals(2 + 2, countEdges());
        One testOne = new One(one.getVertex());
        Assert.assertEquals(1, testOne.getMany().size());
        Many testMany = new Many(many.getVertex());
        Assert.assertNotNull(testMany.getOne());
    }

    @Test
    public void testOneToMany() {
        One one1 = new One(true);
        one1.setName("asd");
        one1.setDateTime(new DateTime());
        Many many11 = new Many(one1);
        Many many12 = new Many(one1);

        One one2 = new One(true);
        one2.setName("asdasd");
        one2.setDateTime(new DateTime());
        Many many21 = new Many(one2);
        Many many22 = new Many(one2);

        db.commit();
        Assert.assertEquals(6, countVertices());
        Assert.assertEquals(6 + 6, countEdges());

        // This is to ensure the collection is loaded, to test that it gets
        // cleared again to ensure the correct one get loaded on the next call
        many21.getOne();
        one1.addToMany(many21);
        db.commit();

        Assert.assertEquals(6, countVertices());
        Assert.assertEquals(6 + 6, countEdges());

        Many testMany21 = new Many(many21.getVertex());
        Assert.assertEquals(one1, testMany21.getOne());

        Assert.assertEquals(one1, many21.getOne());
    }

    @Test
    public void testDelete() {
        One one1 = new One(true);
        one1.setName("asd");
        one1.setDateTime(new DateTime());
        Many many11 = new Many(one1);
        db.commit();
        Assert.assertEquals(2, countVertices());
        Assert.assertEquals(2 + 2, countEdges());

        many11.delete();
        db.commit();
        Assert.assertEquals(1, countVertices());
        Assert.assertEquals(1 + 1, countEdges());

    }

}
