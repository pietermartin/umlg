package org.umlg.quickpreview.interfase;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.interfase.*;
import org.umlg.quickpreview.BaseTest;

/**
 * Date: 2014/05/08
 * Time: 7:31 AM
 */
public class TestInterfaces extends BaseTest {

    @Test
    public void testInterfaces() {
        Creator creator = new Creator();
        Ghost casper = new Ghost();
        creator.addToCreation(casper);
        Mamal simba = new Mamal();
        creator.addToCreation(simba);
        db.commit();

        Assert.assertTrue(casper instanceof Spirit);
        Assert.assertTrue(simba instanceof Spirit);
        Assert.assertTrue(simba instanceof Being);

        //For test purposes, ensure the creator is reloaded from the db.
        creator.reload();
        Assert.assertTrue(creator.getCreation().contains(casper));
        Assert.assertTrue(creator.getCreation().contains(simba));
    }
}
