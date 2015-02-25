package org.umlg.tests.qualifiertest;

import org.apache.tinkerpop.gremlin.structure.Compare;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.qualifiertest.*;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.util.Pair;

/**
 * Date: 2014/09/07
 * Time: 7:03 PM
 */
public class TestQualifiedDerivedUnionProperty extends BaseLocalDbTest {

    @Test
    public void testDerivedQualifiedDerivedUnionProperty() {

        AQualifierB aQualifierB = new AQualifierB();
        aQualifierB.setName("aQualifierB");

        BQualifierB bQualifierB1 = new BQualifierB(aQualifierB);
        bQualifierB1.setName("bQualifierB1");
        BQualifierB bQualifierB2 = new BQualifierB(aQualifierB);
        bQualifierB2.setName("bQualifierB2");

        AQualifierC aQualifierC = new AQualifierC();
        aQualifierC.setName("aQualifierC");

        BQualifierC bQualifierC1 = new BQualifierC(aQualifierC);
        bQualifierC1.setName("bQualifierC1");
        BQualifierC bQualifierC2 = new BQualifierC(aQualifierC);
        bQualifierC2.setName("bQualifierB2");

        UMLG.get().commit();

        Assert.assertEquals(1, aQualifierB.getBQualifierAForAQualifierABQualifierAByName(Pair.of(Compare.eq, "bQualifierB1")).size(), 0);
        Assert.assertEquals(1, aQualifierB.getBQualifierAForAQualifierABQualifierAByName(Pair.of(Compare.eq, "bQualifierB2")).size(), 0);

        Assert.assertEquals(1, aQualifierC.getBQualifierAForAQualifierABQualifierAByName(Pair.of(Compare.eq, "bQualifierC1")).size(), 0);
        Assert.assertEquals(1, aQualifierC.getBQualifierAForAQualifierABQualifierAByName(Pair.of(Compare.eq, "bQualifierB2")).size(), 0);

    }

    @Test
    public void testDerivedQualifiedDerivedUnionProperty2() {

        AQualifierA aQualifierA = new AQualifierB();
        aQualifierA.setName("aQualifierA");

        BQualifierA bQualifierA1 = new BQualifierB((AQualifierB)aQualifierA);
        bQualifierA1.setName("bQualifierA1");
        BQualifierA bQualifierA2 = new BQualifierB((AQualifierB)aQualifierA);
        bQualifierA2.setName("bQualifierA2");

        UMLG.get().commit();

        Assert.assertEquals(1, aQualifierA.getBQualifierAForAQualifierABQualifierAByName(Pair.of(Compare.eq, "bQualifierA1")).size(), 0);
        Assert.assertEquals(1, aQualifierA.getBQualifierAForAQualifierABQualifierAByName(Pair.of(Compare.eq, "bQualifierA2")).size(), 0);

    }

    @Test
    public void testDerivedQualifiedDerivedUnionProperty3() {

        AQualifierD aQualifierD = new AQualifierD();
        aQualifierD.setName("aQualifierD");

        BQualifierD bQualifierD1 = new BQualifierD(aQualifierD);
        bQualifierD1.setName("bQualifierD1");
        BQualifierD bQualifierD2 = new BQualifierD(aQualifierD);
        bQualifierD2.setName("bQualifierD2");

        UMLG.get().commit();

        Assert.assertEquals(1, aQualifierD.getBQualifierDForBQualifierDByNameQualifier(Pair.of(Compare.eq, "bQualifierD1")).size(), 0);
        Assert.assertEquals(1, aQualifierD.getBQualifierDForBQualifierDByNameQualifier(Pair.of(Compare.eq, "bQualifierD2")).size(), 0);

    }


}
