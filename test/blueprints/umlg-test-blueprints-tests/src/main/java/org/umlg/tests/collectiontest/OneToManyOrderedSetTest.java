package org.umlg.tests.collectiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.C;
import org.umlg.collectiontest.D;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/03/29
 * Time: 6:45 PM
 */
public class OneToManyOrderedSetTest extends BaseLocalDbTest {

    @Test
    public void testOneToManyOrderedNextNeedsIdentifier() {

        C c1 = new C();
        c1.setName("c1");

        D d1 = new D();
        d1.setName("d1");
        D d2 = new D();
        d2.setName("d2");
        D d3 = new D();
        d3.setName("d3");

        c1.addToD(d1);
        c1.addToD(d2);
        c1.addToD(d3);
        db.commit();

        c1.reload();
        Assert.assertEquals(3, c1.getD().size());

        C c2 = new C();
        c2.setName("c2");
        c2.addToD(d1);
        db.commit();

        c1.reload();
        d1.reload();
        Assert.assertEquals(2, c1.getD().size());
        Assert.assertEquals(c2, d1.getC());

    }
}
