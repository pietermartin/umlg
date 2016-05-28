package org.umlg.tests.associationclass;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.associationclass.Hour;
import org.umlg.associationclass.Measurement1;
import org.umlg.associationclass.Measurement2;
import org.umlg.associationclass.ObjectType;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2016/03/12
 * Time: 7:57 AM
 */
public class TestAssociationClassInheritance  extends BaseLocalDbTest {

    @Test
    public void testInheritanceOnAssociationClass() {

        Hour hour1 = new Hour();
        hour1.setHour(1);
        ObjectType objectType1 = new ObjectType();
        objectType1.setName("objectType1");

        Measurement1 measurement11 = new Measurement1();
        measurement11.setName("measurement11");
        hour1.addToObjecttype(objectType1, measurement11);

        ObjectType objectType2 = new ObjectType();
        objectType2.setName("objectType2");
        Measurement2 measurement22 = new Measurement2();
        measurement22.setName("measurement22");
        hour1.addToObjecttype(objectType2, measurement22);

        UMLG.get().commit();

        hour1.reload();
        Assert.assertEquals(2, hour1.getMeasurement_objecttype().size());

    }

}
