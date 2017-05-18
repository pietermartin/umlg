package org.umlg.tests.globalget;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.umlg.associationclass.Hour;
import org.umlg.associationclass.HourMeasurement;
import org.umlg.associationclass.ObjectType;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.persistent.PropertyTree;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Date: 2016/05/29
 * Time: 9:32 AM
 */
public class TestGlobalGetOnAssociationClass extends BaseLocalDbTest {

//    @Test
    public void testGlobalGetOnAssociationClass() {
        Hour hour1 = new Hour();
        hour1.setHour(1);
        Hour hour2 = new Hour();
        hour2.setHour(2);

        ObjectType objectType1 = new ObjectType();
        objectType1.setName("objectType1");
        ObjectType objectType2 = new ObjectType();
        objectType2.setName("objectType2");

        HourMeasurement hour1ObjectType1Measurement = new HourMeasurement();
        hour1ObjectType1Measurement.setName("hour1ObjectType1Measurement");
        hour1.addToObjectType(objectType1, hour1ObjectType1Measurement);

        HourMeasurement hour1ObjectType2Measurement = new HourMeasurement();
        hour1ObjectType2Measurement.setName("hour1ObjectType2Measurement");
        hour1.addToObjectType(objectType2, hour1ObjectType2Measurement);

        HourMeasurement hour2ObjectType1Measurement = new HourMeasurement();
        hour2ObjectType1Measurement.setName("hour2ObjectType1Measurement");
        hour2.addToObjectType(objectType1, hour2ObjectType1Measurement);

        HourMeasurement hour2ObjectType2Measurement = new HourMeasurement();
        hour2ObjectType2Measurement.setName("hour2ObjectType2Measurement");
        hour2.addToObjectType(objectType2, hour2ObjectType2Measurement);

        UMLG.get().commit();

        PropertyTree hourMeasurementPT = PropertyTree.from("HourMeasurement");
        hourMeasurementPT.addChild(HourMeasurement.HourMeasurementRuntimePropertyEnum.objectType);
        hourMeasurementPT.addChild(HourMeasurement.HourMeasurementRuntimePropertyEnum.hour);

        List<HourMeasurement> hourMeasurements = UMLG.get().get(hourMeasurementPT);
        assertEquals(4, hourMeasurements.size());
        for (HourMeasurement hourMeasurement : hourMeasurements) {
            System.out.println(hourMeasurement.getObjectType().getName());
            System.out.println(hourMeasurement.getHour().getHour());
        }
    }

    @Test
    public void testAssociationClassPerformance() {
//        UMLG.get().batchModeOn();
        int count = 10_000;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < count; i++) {
            Hour hour1 = new Hour();
            hour1.setHour(i);
            ObjectType objectType1 = new ObjectType();
            objectType1.setName("objectType" + i);

            HourMeasurement hour1ObjectType1Measurement = new HourMeasurement();
            hour1ObjectType1Measurement.setName("hour" + i + "ObjectType1Measurement");
            hour1.addToObjectTypeIgnoreInverse(objectType1, hour1ObjectType1Measurement);
        }
        UMLG.get().commit();
        stopWatch.stop();
        System.out.println("Insert time " + stopWatch.toString());
        stopWatch.reset();
        stopWatch.start();

        PropertyTree hourMeasurementPT = PropertyTree.from("HourMeasurement");
        hourMeasurementPT.addChild(HourMeasurement.HourMeasurementRuntimePropertyEnum.objectType);
        hourMeasurementPT.addChild(HourMeasurement.HourMeasurementRuntimePropertyEnum.hour);
        List<HourMeasurement> hourMeasurements = UMLG.get().get(hourMeasurementPT);
        assertEquals(count, hourMeasurements.size());
        stopWatch.stop();
        System.out.println("Read time " + stopWatch.toString());
        for (HourMeasurement hourMeasurement : hourMeasurements) {
            System.out.println(hourMeasurement.getObjectType().getName());
            System.out.println(hourMeasurement.getHour().getHour());
        }
    }
}
