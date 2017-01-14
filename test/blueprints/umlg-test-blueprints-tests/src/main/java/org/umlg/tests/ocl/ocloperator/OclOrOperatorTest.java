package org.umlg.tests.ocl.ocloperator;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.ocloperator.OclOr;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/08/07
 * Time: 5:25 PM
 */
public class OclOrOperatorTest extends BaseLocalDbTest {

    @Test
    public void testOclAnd() {
        OclOr oclOr = new OclOr();
        oclOr.setName("halo");
        UMLG.get().commit();
        Assert.assertEquals("one", oclOr.getOrName());
        oclOr.setName("bye");
        UMLG.get().commit();
        Assert.assertEquals("one", oclOr.getOrName());
        oclOr.setName("aaa");
        UMLG.get().commit();
        Assert.assertEquals("two", oclOr.getOrName());
    }
}
