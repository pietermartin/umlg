package org.umlg.quickpreview.javaprimitivetype;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.javaprimitivetype.JavaPrimitiveType;
import org.umlg.quickpreview.BaseTest;
import org.umlg.umlprimitivetype.UmlPrimitiveType;

/**
 * Date: 2014/05/04
 * Time: 12:08 PM
 */
public class TestJavaPrimitiveType extends BaseTest {

    @Test
    public void testJavaPrimitiveType() {
        JavaPrimitiveType javaPrimitiveType = new JavaPrimitiveType();
        javaPrimitiveType.setABoolean(true);
        javaPrimitiveType.setAInteger(1);
        javaPrimitiveType.setALong(1L);
        javaPrimitiveType.setAFloat(1F);
        javaPrimitiveType.setADouble(1D);
        db.commit();
        //Ensure we are getting the values from the db, clearing any cached values in the object
        javaPrimitiveType.reload();
        Assert.assertEquals(true, javaPrimitiveType.getABoolean());
        Assert.assertEquals(1, javaPrimitiveType.getAInteger(), 0);
        Assert.assertEquals(1L, javaPrimitiveType.getALong(), 0);
        Assert.assertEquals(1F, javaPrimitiveType.getAFloat(), 0);
        Assert.assertEquals(1D, javaPrimitiveType.getADouble(), 0);
    }
}
