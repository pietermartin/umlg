package org.umlg.tests.enumeration;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.enumeration.Gender;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2016/04/22
 * Time: 7:46 AM
 */
public class EnumerationOperationTest extends BaseLocalDbTest {

    @Test
    public void testEnumerationOperation() {
        Assert.assertTrue(Gender.FEMALE.testEnumOper("halo"));
        Assert.assertFalse(Gender.FEMALE.testEnumOper("haloasd"));
    }
}
