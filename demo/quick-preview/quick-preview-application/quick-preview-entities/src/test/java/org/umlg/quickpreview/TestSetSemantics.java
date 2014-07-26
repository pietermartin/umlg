package org.umlg.quickpreview;

import com.google.common.collect.Multiset;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgAdminGraph;
import org.umlg.runtime.adaptor.UmlgGraph;
import org.umlg.runtime.collection.UmlgBag;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.util.UmlgProperties;
import org.umlg.setsemantics.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Date: 2014/04/28
 * Time: 1:41 PM
 */
public class TestSetSemantics {

    private UmlgGraph db;

    @BeforeClass
    public static void beforeClass() {
        //To execute ocl queries the model needs to loaded and the ocl parser initialize.
        //This only needs to happen once.
        UmlgOcl2Parser.INSTANCE.init("quick-preview.uml");
    }

    @Before
    public void before() throws IOException {
        File dbDir = new File(UmlgProperties.INSTANCE.getUmlgDbLocation());
        if (dbDir.exists()) {
            FileUtils.deleteDirectory(dbDir);
        }
        this.db = UMLG.get();
    }

    @After
    public void after() {
        ((UmlgAdminGraph)db).drop();
    }

    @Test
    public void testSet() {
        Performance p = new Performance();
        Reservation r1 = new Reservation();
        Reservation r2 = new Reservation();
        p.addToReservation(r1);
        p.addToReservation(r2);
        UMLG.get().commit();
        Assert.assertTrue(p.getReservation() instanceof UmlgSet);
        Assert.assertTrue(p.getReservation() instanceof Set);
        Assert.assertEquals(2, p.getReservation().size());
    }

    @Test
    public void testList() {
        Performance p = new Performance();
        Request r1 = new Request();
        Request r2 = new Request();
        p.addToRequest(r1);
        p.addToRequest(r2);
        UMLG.get().commit();
        Assert.assertTrue(p.getRequest() instanceof UmlgSequence);
        Assert.assertTrue(p.getRequest() instanceof List);
        Assert.assertEquals(2, p.getRequest().size());
    }

    @Test
    public void testOrderedSet() {
        Order o = new Order();
        LineItem l1 = new LineItem();
        LineItem l2 = new LineItem();
        o.addToLineItem(l1);
        o.addToLineItem(l2);
        UMLG.get().commit();
        Assert.assertTrue(o.getLineItem() instanceof UmlgOrderedSet);
        Assert.assertTrue(o.getLineItem() instanceof Set);
        Assert.assertTrue(o.getLineItem() instanceof List);
        Assert.assertEquals(2, o.getLineItem().size());
    }

    @Test
    public void testBag() {
        Product p = new Product();
        Complaint c1 = new Complaint();
        Complaint c2 = new Complaint();
        p.addToComplaint(c1);
        p.addToComplaint(c2);
        UMLG.get().commit();
        Assert.assertTrue(p.getComplaint() instanceof UmlgBag);
        Assert.assertTrue(p.getComplaint() instanceof Multiset);
        Assert.assertEquals(2, p.getComplaint().size());
    }

}
