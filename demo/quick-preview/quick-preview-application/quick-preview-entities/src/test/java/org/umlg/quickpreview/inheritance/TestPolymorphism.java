package org.umlg.quickpreview.inheritance;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.inheritance.Drawing;
import org.umlg.inheritance.Eclipse;
import org.umlg.inheritance.Polygon;
import org.umlg.inheritance.Spline;
import org.umlg.quickpreview.BaseTest;

/**
 * Date: 2014/05/06
 * Time: 10:56 PM
 */
public class TestPolymorphism extends BaseTest {

    @Test
    public void testPolymorphism() {
        Drawing drawing = new Drawing();
        Polygon polygon = new Polygon();
        Eclipse eclipse = new Eclipse();
        Spline spline = new Spline();
        drawing.addToShape(polygon);
        drawing.addToShape(eclipse);
        drawing.addToShape(spline);
        db.commit();

        Assert.assertEquals(3, drawing.getShape().size());
        Assert.assertEquals(polygon, drawing.getShape().get(0));
        Assert.assertEquals(eclipse, drawing.getShape().get(1));
        Assert.assertEquals(spline, drawing.getShape().get(2));
    }
}
