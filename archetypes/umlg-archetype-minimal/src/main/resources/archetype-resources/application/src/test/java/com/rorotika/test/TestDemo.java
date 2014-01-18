package com.rorotika.test;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.umlg.Many;
import org.umlg.One;
import org.umlg.model.Demo;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.adaptor.UmlgGraph;
import org.umlg.runtime.util.UmlgProperties;

import java.io.File;
import java.io.IOException;

/**
 * Date: 2014/01/15
 * Time: 10:17 PM
 */
public class TestDemo {

    private UmlgGraph db;

    @Before
    public void before() throws IOException {
        File dbDir = new File(UmlgProperties.INSTANCE.getTumlDbLocation());
        if (dbDir.exists()) {
            FileUtils.deleteDirectory(dbDir);
        }
        this.db = GraphDb.getDb();
    }

    @After
    public void after() {
        db.shutdown();
    }

    @Test
    public void testDemo() {
        One one1 = new One(true);
        one1.setName("one1");
        Many many1 = new Many(one1);
        many1.setName("many1");
        Many many2 = new Many(one1);
        many2.setName("many2");
        Many many3 = new Many(one1);
        many3.setName("many3");
        Many many4 = new Many(one1);
        many4.setName("many4");
        Many many5 = new Many(one1);
        many5.setName("many5");
        db.commit();

        Assert.assertEquals(1, Demo.INSTANCE.getOne().size());
        Assert.assertEquals(5, one1.getMany().size());
    }
}
