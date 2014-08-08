package org.umlg.tests.ocl.ocloperator;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.ocloperator.OclAnd;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/08/07
 * Time: 5:25 PM
 */
public class OclAndOperatorTest extends BaseLocalDbTest {

    @Test
    public void testOclAnd() {
        OclAnd oclAnd = new OclAnd();
        oclAnd.setAge(3);
        UMLG.get().commit();
        Assert.assertTrue(oclAnd.getTestAnd());
        oclAnd.setAge(6);
        UMLG.get().commit();
        Assert.assertFalse(oclAnd.getTestAnd());
    }
}
