package org.umlg.tests.ocl.ocloperator;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.oclIsTypeOf.OclIsKindOf;
import org.umlg.ocl.oclIsTypeOf.Something;
import org.umlg.ocl.oclIsTypeOf.TestOclTypeOf;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2015/04/17
 * Time: 7:55 AM
 */
public class OclAsTypeTest extends BaseLocalDbTest {

    @Test
    public void testOclAsType() {

        TestOclTypeOf testOclTypeOf = new TestOclTypeOf();

        OclIsKindOf oclIsKindOf1 = new OclIsKindOf();
        oclIsKindOf1.setName("testOclIsKindOf");
        testOclTypeOf.addToAbstractOclIsKindOf(oclIsKindOf1);
        OclIsKindOf oclIsKindOf2 = new OclIsKindOf();
        oclIsKindOf2.setName("testOclIsKindOf2");
        testOclTypeOf.addToAbstractOclIsKindOf(oclIsKindOf2);

        Something something1 = new Something();
        something1.setTest("test1");
        oclIsKindOf1.addToSomething(something1);

        UMLG.get().commit();

        Assert.assertEquals(1, testOclTypeOf.getTestOclTypeOf().size());
    }
}
