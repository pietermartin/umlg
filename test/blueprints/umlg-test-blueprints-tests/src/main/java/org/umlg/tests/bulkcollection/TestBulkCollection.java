package org.umlg.tests.bulkcollection;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.umlg.collectiontest.Nightmare;
import org.umlg.concretetest.God;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/10/03
 * Time: 12:17 PM
 */
public class TestBulkCollection extends BaseLocalDbTest {

    @Test
    public void testBulkCollectionAdd() {
        Assume.assumeTrue(UMLG.get().supportsBatchMode());
        God god = new God();
        god.setName("GOD");
        UMLG.get().commit();
        UMLG.get().batchModeOn();
        for (int i = 1; i < 10001; i++) {
            Nightmare nightmare = new Nightmare();
            nightmare.setName("name" + i);
            nightmare.addToGodIgnoreInverse(god);
            if (i % 1000 == 0) {
                System.out.println(i);
                UMLG.get().commit();
                UMLG.get().batchModeOn();
            }
        }
        UMLG.get().commit();
        Assert.assertEquals(10000, god.getNightmare().size());
    }

}
