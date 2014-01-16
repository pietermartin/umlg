package com.rorotika.test;

import org.apache.tools.ant.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.umlg.Demo1;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.adaptor.UmlgGraph;
import org.umlg.runtime.util.UmlgProperties;

import java.io.File;

/**
 * Date: 2014/01/15
 * Time: 10:17 PM
 */
public class TestDemo {

    private UmlgGraph db;

    @Before
    public void before() {
        File dbDir = new File(UmlgProperties.INSTANCE.getTumlDbLocation());
        if (dbDir.exists()) {
            FileUtils.delete(dbDir);
        }
        this.db = GraphDb.getDb();
    }

    @After
    public void after() {
        db.shutdown();
    }

    @Test
    public void testDemo() {
        Demo1 demo1 = new Demo1(true);
        db.commit();
    }
}
