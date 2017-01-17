package org.umlg.tests.defaultvalues;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2017/01/17
 * Time: 1:04 PM
 */
public class TestDefaultValues extends BaseLocalDbTest {

    @Test
    public void testPrimitiveDefaultValuesAreInConstructor()  {
        God god = new God();
        Assert.assertEquals(10.123D, god.getAnumber(), 0);
        Assert.assertEquals("evenMoreOfAJol", god.getName2());
        UMLG.get().commit();
        god.reload();
        Assert.assertEquals(10.123D, god.getAnumber(), 0);
        Assert.assertEquals("evenMoreOfAJol", god.getName2());
    }

}
