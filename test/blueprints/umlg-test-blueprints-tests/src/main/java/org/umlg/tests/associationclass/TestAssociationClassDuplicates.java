package org.umlg.tests.associationclass;

import org.junit.Test;
import org.umlg.associationclass.Hour;
import org.umlg.associationclass.HourMeasurement;
import org.umlg.associationclass.ObjectType;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2016/07/01
 * Time: 7:32 PM
 */
public class TestAssociationClassDuplicates extends BaseLocalDbTest {

    @Test(expected = IllegalStateException.class)
    public void testAssociationClassDulicates() {
        Hour hour1 = new Hour();
        hour1.setHour(1);
        ObjectType objectType1 = new ObjectType();
        objectType1.setName("objectType1");
        ObjectType objectType2 = new ObjectType();
        objectType2.setName("objectType2");

        HourMeasurement hourMeasurement1 = new HourMeasurement();
        hourMeasurement1.setName("hourMeasurement1");
        hour1.addToObjectType(objectType1, hourMeasurement1);

        HourMeasurement hourMeasurement2 = new HourMeasurement();
        hourMeasurement2.setName("hourMeasurement2");
        hour1.addToObjectType(objectType2, hourMeasurement2);

        UMLG.get().commit();

        hour1.reload();
        hourMeasurement2.reload();
        hour1.addToObjectType(objectType2, hourMeasurement2);

        HourMeasurement hourMeasurementTest = new HourMeasurement();
        hourMeasurementTest.setName("hourMeasurementTest");
        objectType2.addToHour(hour1, hourMeasurementTest);
        UMLG.get().commit();
    }

}
