package org.umlg.tests.ocl.ocloperator;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.ocloperator.OclAnd;
import org.umlg.ocl.ocloperator.OclIndexOf;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/08/07
 * Time: 5:25 PM
 */
public class OclIndexOfTest extends BaseLocalDbTest {

    @Test
    public void testOclIndexOf() {
        OclIndexOf oclIndexOf = new OclIndexOf();
        oclIndexOf.setName("bbbbabbbb");
        UMLG.get().commit();
        Assert.assertEquals(4, oclIndexOf.getNameIndexOf(), 0);
    }
}
