package org.umlg.quickpreview.association.composite;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.association.composite.Header;
import org.umlg.association.composite.Panel;
import org.umlg.association.composite.Slider;
import org.umlg.association.composite.Window;
import org.umlg.quickpreview.BaseTest;

/**
 * Date: 2014/05/05
 * Time: 8:28 PM
 */
public class TestAssociationComposite extends BaseTest {

    /**
     * Using the constructor with the composite parent as parameter
     */
    @Test
    public void testAssociationCompositeWithCompositeConstructor() {
        Window window = new Window();
        Slider slider1 = new Slider(window);
        Slider slider2 = new Slider(window);
        Header header = new Header(window);
        Panel panel = new Panel(window);
        db.commit();
        Assert.assertEquals(2, window.getScrollbar().size());
        Assert.assertEquals(header, window.getTitle());
        Assert.assertEquals(panel, window.getBody());
    }

    /**
     * Using the default constructor
     */
    @Test
    public void testAssociationCompositeWithDefaultConstructor() {
        Window window = new Window();
        Slider slider1 = new Slider();
        window.addToScrollbar(slider1);
        Slider slider2 = new Slider();
        window.addToScrollbar(slider2);
        Header header = new Header();
        window.addToTitle(header);
        Panel panel = new Panel();
        window.addToBody(panel);
        db.commit();
        Assert.assertEquals(2, window.getScrollbar().size());
        Assert.assertEquals(header, window.getTitle());
        Assert.assertEquals(panel, window.getBody());
    }

    @Test
    public void testAssociationCompositeDeletion() {
        Window window = new Window();
        Slider slider1 = new Slider(window);
        Slider slider2 = new Slider(window);
        Header header = new Header(window);
        Panel panel = new Panel(window);
        db.commit();

        Assert.assertEquals(2, window.getScrollbar().size());
        Assert.assertEquals(header, window.getTitle());
        Assert.assertEquals(panel, window.getBody());
        Assert.assertEquals(1, Window.allInstances().size());
        Assert.assertEquals(2, Slider.allInstances().size());
        Assert.assertEquals(1, Header.allInstances().size());
        Assert.assertEquals(1, Panel.allInstances().size());

        window.delete();
        db.commit();

        Assert.assertEquals(0, Window.allInstances().size());
        Assert.assertEquals(0, Slider.allInstances().size());
        Assert.assertEquals(0, Header.allInstances().size());
        Assert.assertEquals(0, Panel.allInstances().size());
    }
}
