package org.umlg.quickpreview.umlprimitivetype;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.quickpreview.BaseTest;
import org.umlg.umlprimitivetype.UmlPrimitiveType;

/**
 * Date: 2014/05/04
 * Time: 12:08 PM
 */
public class TestUmlPrimitiveType extends BaseTest {

    @Test
    public void testUmlPrimitiveType() {
        UmlPrimitiveType umlPrimitiveType = new UmlPrimitiveType();
        umlPrimitiveType.setAString("This is a string");
        umlPrimitiveType.setABoolean(true);
        umlPrimitiveType.setAInteger(1);
        umlPrimitiveType.setAUnlimitedNatural(1);
        umlPrimitiveType.setAReal(1D);
        db.commit();
        //Ensure we are getting the values from the db, clearing any cached values in the object
        umlPrimitiveType.reload();
        Assert.assertEquals("This is a string", umlPrimitiveType.getAString());
        Assert.assertEquals(true, umlPrimitiveType.getABoolean());
        Assert.assertEquals(1, umlPrimitiveType.getAInteger(), 0);
        Assert.assertEquals(1, umlPrimitiveType.getAUnlimitedNatural(), 0);
        Assert.assertEquals(1D, umlPrimitiveType.getAReal(), 0);
    }
}
