package org.umlg.tests.associationclass;

import org.junit.Test;
import org.umlg.associationclass.Hour;
import org.umlg.associationclass.HourMeasurement;
import org.umlg.associationclass.ObjectType;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Date: 2016/03/12
 * Time: 7:57 AM
 */
public class TestAssociationClassInheritance extends BaseLocalDbTest {

    @Test
    public void testInheritanceOnAssociationClass() {

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

        List<HourMeasurement> hourMeasurementList = HourMeasurement.hourMeasurement_findByAggregated(false);
        assertEquals(2, hourMeasurementList.size());
        for (HourMeasurement hourMeasurement : hourMeasurementList) {
            System.out.println(hourMeasurement.getName());
        }


        assertEquals(2, hour1.getHourMeasurement_objectType().size());
        boolean found1 = false, found2 = false;
        for (HourMeasurement hourMeasurement : hour1.getHourMeasurement_objectType()) {
            assertEquals(hour1, hourMeasurement.getHour());
            assertTrue(hourMeasurement.getObjectType().equals(objectType1) || hourMeasurement.getObjectType().equals(objectType2));
            if (hourMeasurement.getObjectType().equals(objectType1)) {
                found1 = true;
            }
            if (hourMeasurement.getObjectType().equals(objectType2)) {
                found2 = true;
            }
        }
        assertTrue(found1 && found2);


    }

}
