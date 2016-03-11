package org.umlg.tests.ocl.ocloperator;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.Collect2;
import org.umlg.ocl.collect.Collect1;
import org.umlg.ocl.collect.Enumeration1;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

/**
 * Date: 2014/08/07
 * Time: 5:25 PM
 */
public class OclSelectTest extends BaseLocalDbTest {

    @Test
    public void testOclSelectWithOperationCallInsidePass() {
        Collect1 collect1 = new Collect1();
        Collect2 collect2_1 = new Collect2();
        collect2_1.setEnumeration1(Enumeration1.EnumerationLiteral1);
        collect1.addToCollect2(collect2_1);
        Collect2 collect2_2 = new Collect2();
        collect2_2.setEnumeration1(Enumeration1.EnumerationLiteral1);
        collect1.addToCollect2(collect2_2);
        UMLG.get().commit();
    }

    @Test
    public void testOclSelectWithOperationCallInsidePass2() {
        Collect1 collect1 = new Collect1();
        Collect2 collect2_1 = new Collect2();
        collect2_1.setEnumeration1(Enumeration1.EnumerationLiteral1);
        collect1.addToCollect2(collect2_1);
        Collect2 collect2_2 = new Collect2();
        collect1.addToCollect2(collect2_2);
        UMLG.get().commit();
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testOclSelectWithOperationCallInsideFail() {
        Collect1 collect1 = new Collect1();
        Collect2 collect2_1 = new Collect2();
        collect1.addToCollect2(collect2_1);
        Collect2 collect2_2 = new Collect2();
        collect1.addToCollect2(collect2_2);
        UMLG.get().commit();
    }

    @Test
    public void testSelectWithDataType() {
        Collect1 collect1 = new Collect1();
        Collect2 collect2_1 = new Collect2();
        collect2_1.setPassword("aaaaa");
        collect2_1.setEnumeration1(Enumeration1.EnumerationLiteral1);
        collect1.addToCollect2(collect2_1);
        Collect2 collect2_2 = new Collect2();
        collect1.addToCollect2(collect2_2);
        UMLG.get().commit();
        Assert.assertEquals(1, collect1.testDataType().size());
        Assert.assertTrue(collect1.testDataType().contains(collect2_2));
    }

    @Test
    public void testSelectWithPrimitive() {
        Collect1 collect1 = new Collect1();
        Collect2 collect2_1 = new Collect2();
        collect2_1.setPassword("aaaaa");
        collect2_1.setEnumeration1(Enumeration1.EnumerationLiteral1);
        collect2_1.setReally(1d);
        collect1.addToCollect2(collect2_1);
        Collect2 collect2_2 = new Collect2();
        collect1.addToCollect2(collect2_2);
        UMLG.get().commit();
        Assert.assertEquals(1, collect1.testReal().size());
        Assert.assertTrue(collect1.testReal().contains(collect2_2));
    }
}
