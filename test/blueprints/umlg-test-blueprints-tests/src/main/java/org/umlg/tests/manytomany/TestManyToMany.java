package org.umlg.tests.manytomany;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.manytomany.ManyTestA;
import org.umlg.manytomany.ManyTestB;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2016/10/15
 * Time: 4:07 PM
 */
public class TestManyToMany extends BaseLocalDbTest {

//    @Test
    public void testManyToMany1() {
        ManyTestA manyTestA = new ManyTestA();
        ManyTestB manyTestB = new ManyTestB();
        manyTestA.addToManyB(manyTestB);
        manyTestA.addToManyB(manyTestB);
        UMLG.get().commit();
        manyTestA.reload();
        Assert.assertEquals(1, manyTestA.getManyB().size(), 0);
        Assert.assertEquals(2, manyTestB.getManyA().size(), 0);
    }

    @Test
    public void testManyToMany2() {
        ManyTestA manyTestA = new ManyTestA();
        ManyTestB manyTestB = new ManyTestB();
        manyTestB.addToManyA(manyTestA);
        manyTestB.addToManyA(manyTestA);
        UMLG.get().commit();
        manyTestA.reload();
        Assert.assertEquals(1, manyTestA.getManyB().size(), 0);
        Assert.assertEquals(2, manyTestB.getManyA().size(), 0);
    }
}
