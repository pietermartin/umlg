package org.umlg.tests.subsetting;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.subsetting.*;

/**
 * Date: 2014/01/08
 * Time: 11:16 PM
 */
public class TestSubsetting extends BaseLocalDbTest {

    @Test
    public void testSubsettingOnClass() {
        Car car = new Car(true);
        Boat boat = new Boat(true);
        Horse horse = new Horse(true);

        car.addToSteeringWheel(new SteeringWheel(true));
        boat.addToTiller(new Tiller(true));
        horse.addToReins(new Reins(true));
        db.commit();

        Vechile vechile = db.instantiateClassifier(car.getId());
        Assert.assertNotNull(vechile.getSteeringControl());

        SteeringWheel steeringWheel = db.instantiateClassifier(vechile.getSteeringControl().getId());
        Assert.assertNotNull(steeringWheel.getCar());

        SteeringControl steeringControl = db.instantiateClassifier(vechile.getSteeringControl().getId());
        Assert.assertNotNull(steeringControl.getVechile());

    }

    @Test
    public void testSubsettingOnInterface() {

        Bsc bsc = new Bsc(true);
        Bts bts1 = new Bts(true);
        bsc.addToBts(bts1);
        Bts bts2 = new Bts(true);
        bsc.addToBts(bts2);
        Bts bts3 = new Bts(true);
        bsc.addToBts(bts3);
        Cell cell1 = new Cell(true);
        bsc.addToCell(cell1);
        Cell cell2 = new Cell(true);
        bsc.addToCell(cell2);
        Cell cell3 = new Cell(true);
        bsc.addToCell(cell3);
        db.commit();

        bsc.reload();
        Assert.assertEquals(6, bsc.getChildren().size());

    }



}
