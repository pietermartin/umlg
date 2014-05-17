package org.umlg.quickpreview.subsetting;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.quickpreview.BaseTest;
import org.umlg.subsetting.Bike;
import org.umlg.subsetting.Car;
import org.umlg.subsetting.CarMart;
import org.umlg.subsetting.Spare;

/**
 * Date: 2014/05/17
 * Time: 5:57 PM
 */
public class TestSubsetting extends BaseTest {

    @Test
    public void testSubsetting() {
        CarMart carMart = new CarMart();
        Car toyota = new Car();
        carMart.addToCar(toyota);
        Car ford = new Car();
        carMart.addToCar(ford);
        Car mazda = new Car();
        carMart.addToCar(mazda);

        Bike yamaha = new Bike();
        carMart.addToBike(yamaha);
        Bike kawasaki = new Bike();
        carMart.addToBike(kawasaki);
        Bike honda = new Bike();
        carMart.addToBike(honda);

        Spare wheel = new Spare();
        carMart.addToSpare(wheel);
        Spare engine = new Spare();
        carMart.addToSpare(engine);
        Spare boot = new Spare();
        carMart.addToSpare(boot);

        db.commit();

        Assert.assertEquals(3, carMart.getCar().size());
        Assert.assertEquals(3, carMart.getBike().size());
        Assert.assertEquals(3, carMart.getSpare().size());
        //carMart.getStock() returns the union of the subsetting properties car, bike and spare.
        Assert.assertEquals(9, carMart.getStock().size());
    }
}
