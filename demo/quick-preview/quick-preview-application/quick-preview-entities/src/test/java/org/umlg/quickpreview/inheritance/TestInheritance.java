package org.umlg.quickpreview.inheritance;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.inheritance.*;
import org.umlg.quickpreview.BaseTest;

/**
 * Date: 2014/05/05
 * Time: 10:51 PM
 */
public class TestInheritance extends BaseTest {

    @Test
    public void testInheritance() {
        Drawing drawing = new Drawing();
        Polygon polygon = new Polygon(drawing);
        polygon.setName("polygon1");
        Eclipse eclipse = new Eclipse(drawing);
        eclipse.setName("eclipse1");
        Spline spline = new Spline(drawing);
        spline.setName("spline1");
        db.commit();

        Assert.assertTrue(polygon instanceof Shape);
        Assert.assertTrue(eclipse instanceof Shape);
        Assert.assertTrue(spline instanceof Shape);
        Assert.assertEquals("polygon1", polygon.getName());
        Assert.assertEquals("eclipse1", eclipse.getName());
        Assert.assertEquals("spline1", spline.getName());
    }
}
