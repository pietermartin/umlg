package org.umlg.tinker.subsetting;

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
    public void testSubsetting1() {
        Car car = new Car(true);
        Boat boat = new Boat(true);
        Horse horse = new Horse(true);

        car.addToSteeringWheel(new SteeringWheel(true));
        boat.addToTiller(new Tiller(true));
        horse.addToReins(new Reins(true));
        db.commit();

        Vechile vechile = db.instantiateClassifier(car.getId());
        Assert.assertNotNull(vechile.getSteeringControl());
    }


}
