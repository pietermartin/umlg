package org.umlg.tests.enumeration;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.enumeration.TEST_LITERAL_ATTRIBUTE;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/10/11
 * Time: 8:33 PM
 */
public class TestEnumerationAttribute extends BaseLocalDbTest {

    @Test
    public void testEnumerationAttribute() {
        Assert.assertEquals("/usr/share/rorotika/data", TEST_LITERAL_ATTRIBUTE.EnumerationLiteral1.getValue());
        Assert.assertEquals("aaaa", TEST_LITERAL_ATTRIBUTE.EnumerationLiteral2.getValue());
    }

}
