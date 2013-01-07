package org.tuml.orierntdb.test;


import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.record.impl.ODocument;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;
import org.tuml.basic.God;
import org.tuml.basic.Universe;
import org.tuml.runtime.test.BaseLocalDbTest;

import java.io.File;

/**
 * Date: 2012/12/15
 * Time: 10:36 AM
 */
public class OrientDbBasicTest extends BaseLocalDbTest {

    @Test
    public void testOrientDb() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        God g = new God(true);
        g.setName("g");
        for (int i = 0; i < 10000; i++) {
            Universe universe = new Universe(g);
            universe.setName("u");
        }
        db.commit();
        Assert.assertEquals(10001, countVertices());
        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }
}
