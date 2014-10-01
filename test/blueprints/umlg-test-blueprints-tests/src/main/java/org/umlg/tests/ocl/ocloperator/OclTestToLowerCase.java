package org.umlg.tests.ocl.ocloperator;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.ocloperator.OclToUpperLowerCase;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/09/29
 * Time: 2:22 PM
 */
public class OclTestToLowerCase extends BaseLocalDbTest {

    @Test
    public void testToLowerCase() {
        OclToUpperLowerCase oclToUpperLowerCase = new OclToUpperLowerCase();
        Assert.assertEquals("asdasd", oclToUpperLowerCase.getNameLower());
    }

    @Test
    public void testToUpperCase() {
        OclToUpperLowerCase oclToUpperLowerCase = new OclToUpperLowerCase();
        Assert.assertEquals("ASDASD", oclToUpperLowerCase.getNameUpper());
    }


}
