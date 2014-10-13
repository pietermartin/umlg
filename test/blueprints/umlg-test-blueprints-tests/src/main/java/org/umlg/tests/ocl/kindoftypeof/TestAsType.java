package org.umlg.tests.ocl.kindoftypeof;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.oclIsTypeOf.OclIsKindOf;
import org.umlg.ocl.oclIsTypeOf.OclIsTypeOf;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/10/13
 * Time: 9:44 PM
 */
public class TestAsType extends BaseLocalDbTest {

    @Test
    public void testAsType() {
        OclIsKindOf oclIsKindOf = new OclIsKindOf();
        Assert.assertTrue(oclIsKindOf.testCast(new OclIsTypeOf()));
    }

}
