package org.umlg.tests.ocl.qualifiers;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.qualifier.OclQualifierA;
import org.umlg.ocl.qualifier.OclQualifierAA;
import org.umlg.ocl.qualifier.OclQualifierB;
import org.umlg.ocl.qualifier.OclQualifierC;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.Arrays;

/**
 * Date: 2014/08/30
 * Time: 8:27 AM
 */
public class TestNavigateQualifedProperty extends BaseLocalDbTest {

    @Test
    public void testNavQualifiedOneProperty() {
        OclQualifierA oclQualifierA = new OclQualifierA();

        OclQualifierB oclQualifierB1 = new OclQualifierB();
        oclQualifierB1.setName("name1");
        oclQualifierA.addToOclQualifierB(oclQualifierB1);

        OclQualifierB oclQualifierB2 = new OclQualifierB();
        oclQualifierB2.setName("name2");
        oclQualifierA.addToOclQualifierB(oclQualifierB2);

        OclQualifierB oclQualifierB3 = new OclQualifierB();
        oclQualifierB3.setName("name3");
        oclQualifierA.addToOclQualifierB(oclQualifierB3);

        OclQualifierC oclQualifierC1 = new OclQualifierC();
        oclQualifierC1.setName("oclQualifierC1");
        oclQualifierB1.addToOclQualifierC(oclQualifierC1);

        OclQualifierC oclQualifierC2 = new OclQualifierC();
        oclQualifierC2.setName("oclQualifierC2");
        oclQualifierB2.addToOclQualifierC(oclQualifierC2);

        OclQualifierC oclQualifierC3 = new OclQualifierC();
        oclQualifierC3.setName("oclQualifierC3");
        oclQualifierB3.addToOclQualifierC(oclQualifierC3);
        oclQualifierB3.addToOclQualifierC(oclQualifierC1);

        UMLG.get().commit();

        Assert.assertEquals(3, oclQualifierA.getAllOclQualifierC().size());
        Assert.assertEquals(2, oclQualifierB3.getOclQualifierC().size());
        Assert.assertEquals(4, oclQualifierA.getOclQualifierB().collect(b ->  b.getOclQualifierC()).size());
    }

    @Test
    public void testQualifierInsideCollectStatement() {
        OclQualifierAA oclQualifierAA = new OclQualifierAA();
        oclQualifierAA.setName("oclQualifierAA");
        OclQualifierA oclQualifierA1 = new OclQualifierA();
        oclQualifierAA.addToOclQualifierA(oclQualifierA1);
        OclQualifierA oclQualifierA2 = new OclQualifierA();
        oclQualifierAA.addToOclQualifierA(oclQualifierA2);

        OclQualifierB oclQualifierB1 = new OclQualifierB();
        oclQualifierB1.setName("oclQualifierB1");
        oclQualifierA1.addToOclQualifierB(oclQualifierB1);
        OclQualifierB oclQualifierB2 = new OclQualifierB();
        oclQualifierB2.setName("oclQualifierB2");
        oclQualifierA1.addToOclQualifierB(oclQualifierB2);

        OclQualifierB oclQualifierB3 = new OclQualifierB();
        oclQualifierB3.setName("oclQualifierB3");
        oclQualifierA2.addToOclQualifierB(oclQualifierB3);
        OclQualifierB oclQualifierB4 = new OclQualifierB();
        oclQualifierB4.setName("oclQualifierB4");
        oclQualifierA2.addToOclQualifierB(oclQualifierB4);

        UMLG.get().commit();
        Assert.assertEquals(4, oclQualifierAA.getOclQualifierBs().size());
        oclQualifierAA.reload();
        Assert.assertEquals(4, oclQualifierAA.getOclQualifierBs().size());
        oclQualifierAA.getOclQualifierBs().containsAll(Arrays.asList(oclQualifierB1, oclQualifierB2, oclQualifierB3, oclQualifierB4));
    }

}
