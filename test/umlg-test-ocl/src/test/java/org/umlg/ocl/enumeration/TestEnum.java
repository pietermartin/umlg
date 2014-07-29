package org.umlg.ocl.enumeration;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.enumeration.ENUMX;
import org.umlg.enumeration.TestEnumeration;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/07/29
 * Time: 7:27 PM
 */
public class TestEnum extends BaseLocalDbTest {

    @Test
    public void testEnumToString() {
        TestEnumeration testEnumeration = new TestEnumeration();
        testEnumeration.setName("asdasd");
        testEnumeration.setENUMX(ENUMX.X);
        UMLG.get().commit();
        Assert.assertEquals("X", testEnumeration.getEnumName());
    }
}
