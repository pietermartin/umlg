package org.umlg.tests.javaprimitivetypes;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.umlg.javaprimitivetype.JavaPrimitiveTypeWithValidation;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

/**
 * Date: 2014/05/01
 * Time: 3:39 PM
 */
public class TestJavaPrimitiveTypesWithValidation extends BaseLocalDbTest {

    @Test
    public void testMaxIntegerPass() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMaxInteger(1);
        db.commit();
        javaPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(1, javaPrimitiveTypeWithValidation.getAMaxInteger(), 0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMaxIntegerFail() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMaxInteger(6);
    }

    @Test
    public void testMinIntegerPass() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMinInteger(6);
        db.commit();
        javaPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(6, javaPrimitiveTypeWithValidation.getAMinInteger(), 0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMinIntegerFail() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMinInteger(4);
    }

    @Test
    public void testRangeIntegerPass() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setARangeInteger(4);
        db.commit();
        javaPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(4, javaPrimitiveTypeWithValidation.getARangeInteger(), 0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testRangeIntegerFail() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setARangeInteger(6);
    }

    @Test
    public void testMaxLongPass() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMaxLong(1L);
        db.commit();
        javaPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(1L, javaPrimitiveTypeWithValidation.getAMaxLong(), 0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMaxLongFail() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMaxLong(6L);
    }

    @Test
    public void testMinLongPass() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMinLong(6L);
        db.commit();
        javaPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(6L, javaPrimitiveTypeWithValidation.getAMinLong(), 0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMinLongFail() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMinLong(4L);
    }

    @Test
    public void testRangeLongPass() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setARangeLong(4L);
        db.commit();
        javaPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(4L, javaPrimitiveTypeWithValidation.getARangeLong(), 0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testRangeLongFail() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setARangeLong(6L);
    }

    @Test
    public void testMaxDoublePass() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMaxDouble(1D);
        db.commit();
        javaPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(1D, javaPrimitiveTypeWithValidation.getAMaxDouble(), 0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMaxDoubleFail() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMaxDouble(6D);
    }

    @Test
    public void testMinDoublePass() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMinDouble(6D);
        db.commit();
        javaPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(6D, javaPrimitiveTypeWithValidation.getAMinDouble(), 0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMinDoubleFail() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMinDouble(4D);
    }

    @Test
    public void testRangeDoublePass() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setARangeDouble(4D);
        db.commit();
        javaPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(4D, javaPrimitiveTypeWithValidation.getARangeDouble(), 0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testRangeDoubleFail() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setARangeDouble(6D);
    }

    @Test
    public void testMaxFloatPass() {
        Assume.assumeTrue(UMLG.get().features().vertex().properties().supportsFloatValues());
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMaxFloat(1F);
        db.commit();
        javaPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(1F, javaPrimitiveTypeWithValidation.getAMaxFloat(), 0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMaxFloatFail() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMaxFloat(6F);
    }

    @Test
    public void testMinFloatPass() {
        Assume.assumeTrue(UMLG.get().features().vertex().properties().supportsFloatValues());
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMinFloat(6F);
        db.commit();
        javaPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(6F, javaPrimitiveTypeWithValidation.getAMinFloat(), 0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMinFloatFail() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setAMinFloat(4F);
    }

    @Test
    public void testRangeFloatPass() {
        Assume.assumeTrue(UMLG.get().features().vertex().properties().supportsFloatValues());
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setARangeFloat(4F);
        db.commit();
        javaPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(4F, javaPrimitiveTypeWithValidation.getARangeFloat(), 0);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testRangeFloatFail() {
        JavaPrimitiveTypeWithValidation javaPrimitiveTypeWithValidation = new JavaPrimitiveTypeWithValidation();
        javaPrimitiveTypeWithValidation.setARangeFloat(6F);
    }

}
