package org.umlg.tests.javaprimitivetypes;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.javaprimitivetype.JavaManyPrimitiveTypeWithValidation;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

/**
 * Date: 2014/05/01
 * Time: 8:12 PM
 */
public class TestJavaManyPrimitiveTypesWithValidation extends BaseLocalDbTest {

    @Test
    public void testMaxIntegerPass() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMaxIntegerMany(1);
        javaManyPrimitiveTypeWithValidation.addToAMaxIntegerMany(2);
        javaManyPrimitiveTypeWithValidation.addToAMaxIntegerMany(3);
        javaManyPrimitiveTypeWithValidation.addToAMaxIntegerMany(4);
        javaManyPrimitiveTypeWithValidation.addToAMaxIntegerMany(5);
        db.commit();
        javaManyPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(5, javaManyPrimitiveTypeWithValidation.getAMaxIntegerMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMaxIntegerFail() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMaxIntegerMany(1);
        javaManyPrimitiveTypeWithValidation.addToAMaxIntegerMany(2);
        javaManyPrimitiveTypeWithValidation.addToAMaxIntegerMany(3);
        javaManyPrimitiveTypeWithValidation.addToAMaxIntegerMany(4);
        javaManyPrimitiveTypeWithValidation.addToAMaxIntegerMany(5);
        javaManyPrimitiveTypeWithValidation.addToAMaxIntegerMany(6);
    }

    @Test
    public void testMaxLongPass() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMaxLongMany(1L);
        javaManyPrimitiveTypeWithValidation.addToAMaxLongMany(2L);
        javaManyPrimitiveTypeWithValidation.addToAMaxLongMany(3L);
        javaManyPrimitiveTypeWithValidation.addToAMaxLongMany(4L);
        javaManyPrimitiveTypeWithValidation.addToAMaxLongMany(5L);
        db.commit();
        javaManyPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(5, javaManyPrimitiveTypeWithValidation.getAMaxLongMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMaxLongFail() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMaxLongMany(1L);
        javaManyPrimitiveTypeWithValidation.addToAMaxLongMany(2L);
        javaManyPrimitiveTypeWithValidation.addToAMaxLongMany(3L);
        javaManyPrimitiveTypeWithValidation.addToAMaxLongMany(4L);
        javaManyPrimitiveTypeWithValidation.addToAMaxLongMany(5L);
        javaManyPrimitiveTypeWithValidation.addToAMaxLongMany(6L);
    }

    @Test
    public void testMaxFloatPass() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMaxFloatMany(1F);
        javaManyPrimitiveTypeWithValidation.addToAMaxFloatMany(2F);
        javaManyPrimitiveTypeWithValidation.addToAMaxFloatMany(3F);
        javaManyPrimitiveTypeWithValidation.addToAMaxFloatMany(4F);
        javaManyPrimitiveTypeWithValidation.addToAMaxFloatMany(5F);
        db.commit();
        javaManyPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(5, javaManyPrimitiveTypeWithValidation.getAMaxFloatMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMaxFloatFail() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMaxFloatMany(1F);
        javaManyPrimitiveTypeWithValidation.addToAMaxFloatMany(2F);
        javaManyPrimitiveTypeWithValidation.addToAMaxFloatMany(3F);
        javaManyPrimitiveTypeWithValidation.addToAMaxFloatMany(4F);
        javaManyPrimitiveTypeWithValidation.addToAMaxFloatMany(5F);
        javaManyPrimitiveTypeWithValidation.addToAMaxFloatMany(6F);
    }

    @Test
    public void testMaxDoublePass() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMaxDoubleMany(1D);
        javaManyPrimitiveTypeWithValidation.addToAMaxDoubleMany(2D);
        javaManyPrimitiveTypeWithValidation.addToAMaxDoubleMany(3D);
        javaManyPrimitiveTypeWithValidation.addToAMaxDoubleMany(4D);
        javaManyPrimitiveTypeWithValidation.addToAMaxDoubleMany(5D);
        db.commit();
        javaManyPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(5, javaManyPrimitiveTypeWithValidation.getAMaxDoubleMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMaxDoubleFail() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMaxDoubleMany(1D);
        javaManyPrimitiveTypeWithValidation.addToAMaxDoubleMany(2D);
        javaManyPrimitiveTypeWithValidation.addToAMaxDoubleMany(3D);
        javaManyPrimitiveTypeWithValidation.addToAMaxDoubleMany(4D);
        javaManyPrimitiveTypeWithValidation.addToAMaxDoubleMany(5D);
        javaManyPrimitiveTypeWithValidation.addToAMaxDoubleMany(6D);
    }

    @Test
    public void testMinIntegerPass() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMinIntegerMany(5);
        javaManyPrimitiveTypeWithValidation.addToAMinIntegerMany(6);
        javaManyPrimitiveTypeWithValidation.addToAMinIntegerMany(7);
        javaManyPrimitiveTypeWithValidation.addToAMinIntegerMany(8);
        javaManyPrimitiveTypeWithValidation.addToAMinIntegerMany(9);
        db.commit();
        javaManyPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(5, javaManyPrimitiveTypeWithValidation.getAMinIntegerMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMinIntegerFail() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMinIntegerMany(1);
    }

    @Test
    public void testMinLongPass() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMinLongMany(5L);
        javaManyPrimitiveTypeWithValidation.addToAMinLongMany(6L);
        javaManyPrimitiveTypeWithValidation.addToAMinLongMany(7L);
        javaManyPrimitiveTypeWithValidation.addToAMinLongMany(8L);
        javaManyPrimitiveTypeWithValidation.addToAMinLongMany(9L);
        db.commit();
        javaManyPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(5, javaManyPrimitiveTypeWithValidation.getAMinLongMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMinLongFail() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMinLongMany(1L);
    }

    @Test
    public void testMinFloatPass() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMinFloatMany(5F);
        javaManyPrimitiveTypeWithValidation.addToAMinFloatMany(6F);
        javaManyPrimitiveTypeWithValidation.addToAMinFloatMany(7F);
        javaManyPrimitiveTypeWithValidation.addToAMinFloatMany(8F);
        javaManyPrimitiveTypeWithValidation.addToAMinFloatMany(9F);
        db.commit();
        javaManyPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(5, javaManyPrimitiveTypeWithValidation.getAMinFloatMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMinFloatFail() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMinFloatMany(1F);
    }

    @Test
    public void testMinDoublePass() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMinDoubleMany(6D);
        javaManyPrimitiveTypeWithValidation.addToAMinDoubleMany(7D);
        javaManyPrimitiveTypeWithValidation.addToAMinDoubleMany(8D);
        javaManyPrimitiveTypeWithValidation.addToAMinDoubleMany(9D);
        javaManyPrimitiveTypeWithValidation.addToAMinDoubleMany(10D);
        db.commit();
        javaManyPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(5, javaManyPrimitiveTypeWithValidation.getAMinDoubleMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testMinDoubleFail() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToAMinDoubleMany(1D);
    }

    @Test
    public void testRangeIntegerPass() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToARangeIntegerMany(-5);
        javaManyPrimitiveTypeWithValidation.addToARangeIntegerMany(1);
        javaManyPrimitiveTypeWithValidation.addToARangeIntegerMany(2);
        javaManyPrimitiveTypeWithValidation.addToARangeIntegerMany(3);
        javaManyPrimitiveTypeWithValidation.addToARangeIntegerMany(5);
        db.commit();
        javaManyPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(5, javaManyPrimitiveTypeWithValidation.getARangeIntegerMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testRangeIntegerFail() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToARangeIntegerMany(-6);
    }

    @Test
    public void testRangeLongPass() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToARangeLongMany(-5L);
        javaManyPrimitiveTypeWithValidation.addToARangeLongMany(1L);
        javaManyPrimitiveTypeWithValidation.addToARangeLongMany(2L);
        javaManyPrimitiveTypeWithValidation.addToARangeLongMany(3L);
        javaManyPrimitiveTypeWithValidation.addToARangeLongMany(4L);
        db.commit();
        javaManyPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(5, javaManyPrimitiveTypeWithValidation.getARangeLongMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testRangeLongFail() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToARangeLongMany(-6L);
    }

    @Test
    public void testRangeFloatPass() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToARangeFloatMany(-4.5F);
        javaManyPrimitiveTypeWithValidation.addToARangeFloatMany(1F);
        javaManyPrimitiveTypeWithValidation.addToARangeFloatMany(2F);
        javaManyPrimitiveTypeWithValidation.addToARangeFloatMany(3F);
        javaManyPrimitiveTypeWithValidation.addToARangeFloatMany(4.5F);
        db.commit();
        javaManyPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(5, javaManyPrimitiveTypeWithValidation.getARangeFloatMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testRangeFloatFail() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToARangeFloatMany(-4.6F);
    }

    @Test
    public void testRangeDoublePass() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToARangeDoubleMany(1D);
        javaManyPrimitiveTypeWithValidation.addToARangeDoubleMany(2D);
        javaManyPrimitiveTypeWithValidation.addToARangeDoubleMany(3D);
        javaManyPrimitiveTypeWithValidation.addToARangeDoubleMany(4D);
        javaManyPrimitiveTypeWithValidation.addToARangeDoubleMany(5D);
        db.commit();
        javaManyPrimitiveTypeWithValidation.reload();
        Assert.assertEquals(5, javaManyPrimitiveTypeWithValidation.getARangeDoubleMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testRangeDoubleFail() {
        JavaManyPrimitiveTypeWithValidation javaManyPrimitiveTypeWithValidation = new JavaManyPrimitiveTypeWithValidation();
        javaManyPrimitiveTypeWithValidation.addToARangeDoubleMany(-1D);
    }


}
