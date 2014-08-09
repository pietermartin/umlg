package org.umlg.tests.qualifiertest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.qualifiertest.QualifierA;
import org.umlg.qualifiertest.QualifierB;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/08/09
 * Time: 2:27 PM
 */
public class TestQualifiedOnMultipleProperties extends BaseLocalDbTest {

    @Test
    public void testQualifiedOnMultipleProperties() {
        QualifierA qualifierA = new QualifierA();
        qualifierA.setName("qualifierA");
        QualifierB qualifierB1 = new QualifierB();
        qualifierB1.setName1("qualifierB1Name1");
        qualifierB1.setName2("qualifierB1Name2");
        qualifierA.addToQualifierB(qualifierB1);

        QualifierB qualifierB2 = new QualifierB();
        qualifierB2.setName1("qualifierB2Name1");
        qualifierB2.setName2("qualifierB2Name2");
        qualifierA.addToQualifierB(qualifierB2);

        QualifierB qualifierB3 = new QualifierB();
        qualifierB3.setName1("qualifierB3Name1");
        qualifierB3.setName2("qualifierB3Name2");
        qualifierA.addToQualifierB(qualifierB3);

        QualifierB qualifierB4 = new QualifierB();
        qualifierB4.setName1("qualifierB4Name1");
        qualifierB4.setName2("qualifierB4Name2");
        qualifierA.addToQualifierB(qualifierB4);
        UMLG.get().commit();

        QualifierB qualifierB = qualifierA.getQualifierBForName1and2Qualifier("qualifierB4Name1qualifierB4Name2");
        Assert.assertEquals(qualifierB, qualifierB4);
    }

    @Test
    public void testQualifierOnInt() {
        QualifierA qualifierA = new QualifierA();
        qualifierA.setName("qualifierA");
        QualifierB qualifierB1 = new QualifierB();
        qualifierB1.setInt1(1);
        qualifierA.addToQualifierBInt(qualifierB1);
        QualifierB qualifierB2 = new QualifierB();
        qualifierB2.setInt1(2);
        qualifierA.addToQualifierBInt(qualifierB2);
        UMLG.get().commit();

        Assert.assertEquals(qualifierB1, qualifierA.getQualifierBIntForQualifierInt1(1));
        Assert.assertNull(qualifierA.getQualifierBIntForQualifierInt1(3));
    }

    @Test
    public void testMultipleIntQualifiers() {
        QualifierA qualifierA = new QualifierA();
        qualifierA.setName("qualifierA");
        QualifierB qualifierB1 = new QualifierB();
        qualifierB1.setInt2(1);
        qualifierB1.setInt3(1);
        qualifierA.addToQualifierBMultipleInt(qualifierB1);
        QualifierB qualifierB2 = new QualifierB();
        qualifierB2.setInt2(2);
        qualifierB2.setInt3(2);
        qualifierA.addToQualifierBMultipleInt(qualifierB2);
        UMLG.get().commit();

        Assert.assertEquals(qualifierB1, qualifierA.getQualifierBMultipleIntForQualifierMultipleInt("11"));
        Assert.assertEquals(qualifierB2, qualifierA.getQualifierBMultipleIntForQualifierMultipleInt("22"));

        qualifierB1.setInt2(2);
        UMLG.get().commit();
        Assert.assertEquals(qualifierB1, qualifierA.getQualifierBMultipleIntForQualifierMultipleInt("21"));
    }
}
