package org.umlg.quickpreview.umlggraph;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.association.composite.Header;
import org.umlg.association.composite.Panel;
import org.umlg.association.composite.Slider;
import org.umlg.association.composite.Window;
import org.umlg.inheritance.*;
import org.umlg.interfase.*;
import org.umlg.interfase.meta.BeingMeta;
import org.umlg.interfase.meta.SpiritMeta;
import org.umlg.model.Quickpreview;
import org.umlg.quickpreview.BaseTest;

/**
 * Date: 2014/05/29
 * Time: 6:25 PM
 */
public class TestUmlgGraph extends BaseTest {

    @Test
    public void testRootEntities() {
        Window window1 = new Window();
        Slider slider1_1 = new Slider(window1);
        Slider slider1_2 = new Slider(window1);
        Header header1 = new Header(window1);
        Panel panel1 = new Panel(window1);

        Window window2 = new Window();
        Slider slider2_1 = new Slider(window2);
        Slider slider2_2 = new Slider(window2);
        Header header2 = new Header(window2);
        Panel panel2 = new Panel(window2);

        db.commit();

        //All root object are accessible from the class that represents the model.
        Assert.assertEquals(2, Quickpreview.INSTANCE.getWindow().size());
    }

    @Test
    public void testAllInstances() {
        Drawing drawing1 = new Drawing();
        Polygon polygon1 = new Polygon();
        polygon1.setDrawing(drawing1);
        Polygon polygon2 = new Polygon();
        polygon2.setDrawing(drawing1);
        Eclipse eclipse1 = new Eclipse();
        eclipse1.setDrawing(drawing1);
        Spline spline1 = new Spline();
        spline1.setDrawing(drawing1);
        Spline spline2 = new Spline();
        spline2.setDrawing(drawing1);
        Spline spline3 = new Spline();
        spline3.setDrawing(drawing1);

        db.commit();

        Assert.assertEquals(1, Drawing.allInstances().size());
        Assert.assertEquals(6, Shape.allInstances().size());
        Assert.assertEquals(2, Polygon.allInstances().size());
        Assert.assertEquals(1, Eclipse.allInstances().size());
        Assert.assertEquals(3, Spline.allInstances().size());
    }

    @Test
    public void testInterfaceAllInstances() {
        Creator creator = new Creator();
        Ghost ghost1 = new Ghost();
        ghost1.setCreator(creator);
        Ghost ghost2 = new Ghost();
        ghost2.setCreator(creator);
        Mamal mamal1 = new Mamal();
        mamal1.setCreator(creator);
        Mamal mamal2 = new Mamal();
        mamal2.setCreator(creator);
        Mamal mamal3 = new Mamal();
        mamal3.setCreator(creator);

        db.commit();
        Assert.assertEquals(5, Spirit.allInstances().size());
        Assert.assertEquals(3, Being.allInstances().size());
    }
}
