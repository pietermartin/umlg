package org.umlg.tests.ocl.ocloperator;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.ocloperator.OclIndexOf;
import org.umlg.ocl.ocloperator.OclSubstring;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/08/07
 * Time: 5:25 PM
 */
public class OclSubstringTest extends BaseLocalDbTest {

    @Test
    public void testOclSubstring() {
        OclSubstring oclSubstring = new OclSubstring();
        oclSubstring.setName("bbbbabbbb");
        UMLG.get().commit();
        Assert.assertEquals("bbb", oclSubstring.getSubstring());
    }
}
