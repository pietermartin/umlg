package org.umlg.tests.javaprimitivetypes;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.javaprimitivetype.JavaPrimitiveType;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.Arrays;

/**
 * Date: 2014/05/01
 * Time: 2:42 PM
 */
public class TestJavaPrimitiveTypes extends BaseLocalDbTest {

    @Test
    public void testInt() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.setAInteger(1);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(1, javaPrimitiveType.getAInteger(), 0);
        javaPrimitiveType.setAInteger(2);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(2, javaPrimitiveType.getAInteger(), 0);
    }

    @Test
    public void testManyInt() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.addToAManyInteger(1);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(1, javaPrimitiveType.getAManyInteger().iterator().next(), 0);
        javaPrimitiveType.addToAManyInteger(2);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(2, javaPrimitiveType.getAManyInteger().size());
    }

    @Test
    public void testLong() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.setALong(1L);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(1L, javaPrimitiveType.getALong(), 0);
        javaPrimitiveType.setALong(2L);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(2L, javaPrimitiveType.getALong(), 0);
    }

    @Test
    public void testManyLong() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.addToAManyLong(1L);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(1L, javaPrimitiveType.getAManyLong().iterator().next(), 0);
        javaPrimitiveType.addToAManyLong(2L);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(2, javaPrimitiveType.getAManyLong().size());
    }

    @Test
    public void testDouble() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.setADouble(1.111D);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(1.111D, javaPrimitiveType.getADouble(), 0);
        javaPrimitiveType.setADouble(2.222D);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(2.222D, javaPrimitiveType.getADouble(), 0);
    }

    @Test
    public void testManyDouble() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.addToAManyDouble(1.111D);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(1.111D, javaPrimitiveType.getAManyDouble().iterator().next(), 0);
        javaPrimitiveType.addToAManyDouble(2.222D);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(2, javaPrimitiveType.getAManyDouble().size());
    }

    @Test
    public void testBoolean() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.setABoolean(true);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(true, javaPrimitiveType.getABoolean());
        javaPrimitiveType.setABoolean(false);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(false, javaPrimitiveType.getABoolean());
    }

    @Test
    public void testManyBoolean() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.addToAManyBoolean(true);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(true, javaPrimitiveType.getAManyBoolean().iterator().next());
        javaPrimitiveType.addToAManyBoolean(false);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(2, javaPrimitiveType.getAManyBoolean().size());
    }

    @Test
    public void testFloat() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.setAFloat(1F);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(1F, javaPrimitiveType.getAFloat(), 0);
        javaPrimitiveType.setAFloat(2F);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(2F, javaPrimitiveType.getAFloat(), 0);
    }

    @Test
    public void testManyFloat() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.addToAManyFloat(1.111F);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(1.111F, javaPrimitiveType.getAManyFloat().iterator().next(), 0);
        javaPrimitiveType.addToAManyFloat(2.222F);
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(2, javaPrimitiveType.getAManyFloat().size());
    }

    @Test
    public void testByte() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.setAByte(new Byte((byte)1));
        db.commit();
        javaPrimitiveType.reload();
        Assert.assertEquals(new Byte((byte)1), javaPrimitiveType.getAByte());
        db.commit();
    }

    @Test
    public void testManyBytes() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.addToAManyByte((byte)1);
        javaPrimitiveType.addToAManyByte((byte)2);
        javaPrimitiveType.addToAManyByte((byte)3);
        db.commit();
        javaPrimitiveType.reload();
        Byte[] objects = javaPrimitiveType.getAManyByte().<Byte>toArray(new Byte[]{});
        Assert.assertTrue(Arrays.equals(new Byte[]{1,2,3}, objects));
        db.commit();
    }

}
