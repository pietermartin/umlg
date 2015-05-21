package org.umlg.tests.qualifiertest;

import org.apache.tinkerpop.gremlin.process.traversal.Compare;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.qualifiertest.ENUM1;
import org.umlg.qualifiertest.QualifierC;
import org.umlg.qualifiertest.QualifierD;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.util.Pair;

/**
 * Date: 2014/08/11
 * Time: 7:54 AM
 */
public class TestQualifierWithDateAndEnum extends BaseLocalDbTest {

    @Test(expected = IllegalStateException.class)
    public void testQualifierWithDateAndEnumValidation() {
        QualifierC qualifierC1 = new QualifierC();
        qualifierC1.setName("qualifierC1");

        QualifierD qualifierD1 = new QualifierD();
        qualifierD1.setName("qualifierD1");
        LocalDate now = LocalDate.now();
        qualifierD1.setDate(now);
        qualifierD1.setENUM1(ENUM1.A);
        qualifierC1.addToQualifierD(qualifierD1);

        QualifierD qualifierD2 = new QualifierD();
        qualifierD2.setName("qualifierD2");
        qualifierD2.setDate(now);
        qualifierD2.setENUM1(ENUM1.A);
        qualifierC1.addToQualifierD(qualifierD2);

        UMLG.get().commit();
    }

    @Test
    public void testQualifierWithDateAndEnum() {
        QualifierC qualifierC1 = new QualifierC();
        qualifierC1.setName("qualifierC1");

        QualifierD qualifierD1 = new QualifierD();
        qualifierD1.setName("qualifierD1");
        qualifierD1.setDate(LocalDate.now());
        qualifierD1.setENUM1(ENUM1.A);
        qualifierC1.addToQualifierD(qualifierD1);

        QualifierD qualifierD2 = new QualifierD();
        qualifierD2.setName("qualifierD2");
        qualifierD2.setDate(LocalDate.now().minusDays(1));
        qualifierD2.setENUM1(ENUM1.A);
        qualifierC1.addToQualifierD(qualifierD2);

        QualifierC qualifierC2 = new QualifierC();
        qualifierC2.setName("qualifierC2");

        QualifierD qualifierD3 = new QualifierD();
        qualifierD3.setName("qualifierD3");
        qualifierD3.setDate(LocalDate.now());
        qualifierD3.setENUM1(ENUM1.A);
        qualifierC2.addToQualifierD(qualifierD3);

        QualifierD qualifierD4 = new QualifierD();
        qualifierD4.setName("qualifierD4");
        qualifierD4.setDate(LocalDate.now().minusDays(1));
        qualifierD4.setENUM1(ENUM1.B);
        qualifierC2.addToQualifierD(qualifierD4);

        UMLG.get().commit();

        QualifierD qualifierDTest = qualifierC1.getQualifierDForDateQualifierandEnum1Qualifier(Pair.of(Compare.eq, LocalDate.now()), Pair.of(Compare.eq, ENUM1.A));
        Assert.assertEquals(qualifierD1, qualifierDTest);

        qualifierDTest = qualifierC1.getQualifierDForDateQualifierandEnum1Qualifier(Pair.of(Compare.eq, LocalDate.now().minusDays(1)), Pair.of(Compare.eq, ENUM1.A));
        Assert.assertEquals(qualifierD2, qualifierDTest);

        qualifierDTest = qualifierC2.getQualifierDForDateQualifierandEnum1Qualifier(Pair.of(Compare.eq, LocalDate.now().minusDays(1)), Pair.of(Compare.eq, ENUM1.A));
        Assert.assertNotEquals(qualifierD3, qualifierDTest);

        qualifierDTest = qualifierC2.getQualifierDForDateQualifierandEnum1Qualifier(Pair.of(Compare.eq, LocalDate.now().minusDays(1)), Pair.of(Compare.eq, ENUM1.B));
        Assert.assertEquals(qualifierD4, qualifierDTest);

    }

}
