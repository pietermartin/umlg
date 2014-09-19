package org.umlg.tests.ocl.ocloperator;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.ocloperator.OclSubstring;
import org.umlg.ocl.ocloperator.OclSubstringIndexOf;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/08/07
 * Time: 5:25 PM
 */
public class OclSubstringIndexOfTest extends BaseLocalDbTest {

    @Test
    public void testOclSubstringIndexOf() {
        OclSubstringIndexOf oclSubstringIndexOf = new OclSubstringIndexOf();
        oclSubstringIndexOf.setName("bbbbabbbb");
        UMLG.get().commit();
        Assert.assertEquals("bbbb", oclSubstringIndexOf.getNameSubstringIndexOf());
    }
}
