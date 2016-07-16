package org.umlg.tests.associationclass;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.associationclass.*;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/03/24
 * Time: 7:33 AM
 */
public class TestAssociationClassMoveInList extends BaseLocalDbTest {

    @Test
    public void testAssociationClassBasic() {
        AssociationClass1 associationClass1_1 = new AssociationClass1();
        associationClass1_1.setName("associationClass1_1");
        AssociationClass2 associationClass2_1 = new AssociationClass2();
        associationClass2_1.setName("associationClass2_1");

        AssociationClassAC associationClassAC = new AssociationClassAC();
        associationClassAC.setName("associationClassAC");

        associationClass1_1.addToAssociationclass2(associationClass2_1, associationClassAC);

        db.commit();

        associationClass1_1.reload();
        associationClass2_1.reload();
        associationClassAC.reload();
        Assert.assertEquals(1, associationClass1_1.getAssociationclass2().size());
        Assert.assertEquals(1, associationClass1_1.getAssociationClassAC_associationclass2().size());
    }

    @Test
    public void testAssociationClassMove() {
        AssociationClass1 associationClass1_1 = new AssociationClass1();
        associationClass1_1.setName("associationClass1_1");

        AssociationClass2 associationClass2_1 = new AssociationClass2();
        associationClass2_1.setName("associationClass2_1");
        AssociationClassAC associationClassAC_1 = new AssociationClassAC();
        associationClassAC_1.setName("associationClassAC_1");

        AssociationClass2 associationClass2_2 = new AssociationClass2();
        associationClass2_2.setName("associationClass2_2");
        AssociationClassAC associationClassAC_2 = new AssociationClassAC();
        associationClassAC_2.setName("associationClassAC_2");

        AssociationClass2 associationClass2_3 = new AssociationClass2();
        associationClass2_3.setName("associationClass2_3");
        AssociationClassAC associationClassAC_3 = new AssociationClassAC();
        associationClassAC_3.setName("associationClassAC_3");

        associationClass1_1.addToAssociationclass2(associationClass2_1, associationClassAC_1);
        associationClass1_1.addToAssociationclass2(associationClass2_2, associationClassAC_2);
        associationClass1_1.addToAssociationclass2(associationClass2_3, associationClassAC_3);

        db.commit();

        associationClass1_1.reload();

        Assert.assertEquals(associationClass2_1, associationClass1_1.getAssociationclass2().get(0));
        Assert.assertEquals(associationClass2_2, associationClass1_1.getAssociationclass2().get(1));
        Assert.assertEquals(associationClass2_3, associationClass1_1.getAssociationclass2().get(2));

        associationClass1_1.moveAssociationclass2(1, associationClass2_1);
        db.commit();

        Assert.assertEquals(associationClass2_2, associationClass1_1.getAssociationclass2().get(0));
        Assert.assertEquals(associationClass2_1, associationClass1_1.getAssociationclass2().get(1));
        Assert.assertEquals(associationClass2_3, associationClass1_1.getAssociationclass2().get(2));

    }

    @Test
    public void testMoveACForSequence() {
        AssociationClassAC2 associationClassAC2_1 = new AssociationClassAC2();
        associationClassAC2_1.setName("associationClassAC2_1");
        AssociationClassAC2 associationClassAC2_2 = new AssociationClassAC2();
        associationClassAC2_2.setName("associationClassAC2_2");
        AssociationClassAC2 associationClassAC2_3 = new AssociationClassAC2();
        associationClassAC2_3.setName("associationClassAC2_3");
        AssociationClassAC2 associationClassAC2_4 = new AssociationClassAC2();
        associationClassAC2_4.setName("associationClassAC2_4");

        AssociationClass4 associationClass4_1 = new AssociationClass4();
        associationClass4_1.setName("associationClass4_1");
        AssociationClass4 associationClass4_2 = new AssociationClass4();
        associationClass4_2.setName("associationClass4_2");
        AssociationClass4 associationClass4_3 = new AssociationClass4();
        associationClass4_3.setName("associationClass4_3");
        AssociationClass4 associationClass4_4 = new AssociationClass4();
        associationClass4_4.setName("associationClass4_4");

        AssociationClass3 associationClass3_1 = new AssociationClass3();
        associationClass3_1.setName("associationClass3_1");

        associationClass3_1.addToAssociationclass4(associationClass4_1, associationClassAC2_1);
        associationClass3_1.addToAssociationclass4(associationClass4_2, associationClassAC2_2);
        associationClass3_1.addToAssociationclass4(associationClass4_3, associationClassAC2_3);
        associationClass3_1.addToAssociationclass4(associationClass4_4, associationClassAC2_4);

        db.commit();

        associationClass3_1.reload();
        associationClass3_1.moveAssociationclass4(0, associationClass4_4);
        associationClass3_1.moveAssociationclass4(1, associationClass4_3);
        associationClass3_1.moveAssociationclass4(2, associationClass4_2);

        db.commit();

        associationClass3_1.reload();
        Assert.assertEquals(associationClass4_4, associationClass3_1.getAssociationclass4().get(0));
        Assert.assertEquals(associationClass4_3, associationClass3_1.getAssociationclass4().get(1));
        Assert.assertEquals(associationClass4_2, associationClass3_1.getAssociationclass4().get(2));
        Assert.assertEquals(associationClass4_1, associationClass3_1.getAssociationclass4().get(3));

    }

    @Test
    public void testAssociationClassToSelfMove() {
        AssociationClass5 associationClass5_1 = new AssociationClass5();
        associationClass5_1.setName("associationClass5_1");
        AssociationClass5 associationClass5_2 = new AssociationClass5();
        associationClass5_2.setName("associationClass5_2");
        AssociationClass5 associationClass5_3 = new AssociationClass5();
        associationClass5_3.setName("associationClass5_3");
        AssociationClass5 associationClass5_4 = new AssociationClass5();
        associationClass5_4.setName("associationClass5_4");
        AssociationClass5 associationClass5_5 = new AssociationClass5();
        associationClass5_5.setName("associationClass5_5");

        AssociationClassAC3 associationClassAC3_1 = new AssociationClassAC3();
        associationClassAC3_1.setName("associationClassAC3_1");
        associationClass5_1.addToToAssociationclass5(associationClass5_2, associationClassAC3_1);

        AssociationClassAC3 associationClassAC3_2 = new AssociationClassAC3();
        associationClassAC3_2.setName("associationClassAC3_2");
        associationClass5_1.addToToAssociationclass5(associationClass5_3, associationClassAC3_2);

        AssociationClassAC3 associationClassAC3_3 = new AssociationClassAC3();
        associationClassAC3_3.setName("associationClassAC3_3");
        associationClass5_1.addToToAssociationclass5(associationClass5_4, associationClassAC3_3);

        AssociationClassAC3 associationClassAC3_4 = new AssociationClassAC3();
        associationClassAC3_4.setName("associationClassAC3_4");
        associationClass5_1.addToToAssociationclass5(associationClass5_5, associationClassAC3_4);

        db.commit();

        associationClass5_1.reload();
        Assert.assertEquals(associationClass5_2, associationClass5_1.getToAssociationclass5().get(0));
        Assert.assertEquals(associationClass5_3, associationClass5_1.getToAssociationclass5().get(1));
        Assert.assertEquals(associationClass5_4, associationClass5_1.getToAssociationclass5().get(2));
        Assert.assertEquals(associationClass5_5, associationClass5_1.getToAssociationclass5().get(3));

        associationClass5_1.moveToAssociationclass5(0, associationClass5_5);

        db.commit();
        associationClass5_1.reload();
        Assert.assertEquals(associationClass5_5, associationClass5_1.getToAssociationclass5().get(0));
        Assert.assertEquals(associationClass5_2, associationClass5_1.getToAssociationclass5().get(1));
        Assert.assertEquals(associationClass5_3, associationClass5_1.getToAssociationclass5().get(2));
        Assert.assertEquals(associationClass5_4, associationClass5_1.getToAssociationclass5().get(3));

        Assert.assertTrue(associationClass5_2.getToAssociationclass5().isEmpty());
        associationClass5_2.reload();
        Assert.assertTrue(associationClass5_2.getToAssociationclass5().isEmpty());


        Assert.assertEquals(0, associationClass5_2.getToAssociationclass5().size());
        associationClass5_2.reload();
        Assert.assertEquals(0, associationClass5_2.getToAssociationclass5().size());

        AssociationClassAC3 associationClassAC3_5 = new AssociationClassAC3();
        associationClassAC3_5.setName("associationClassAC3_5");
        associationClass5_2.addToToAssociationclass5(associationClass5_1, associationClassAC3_5);

        AssociationClassAC3 associationClassAC3_6 = new AssociationClassAC3();
        associationClassAC3_6.setName("associationClassAC3_6");
        associationClass5_2.addToToAssociationclass5(associationClass5_3, associationClassAC3_6);

        AssociationClassAC3 associationClassAC3_7 = new AssociationClassAC3();
        associationClassAC3_7.setName("associationClassAC3_7");
        associationClass5_2.addToToAssociationclass5(associationClass5_4, associationClassAC3_7);

        AssociationClassAC3 associationClassAC3_8 = new AssociationClassAC3();
        associationClassAC3_8.setName("associationClassAC3_8");
        associationClass5_2.addToToAssociationclass5(associationClass5_5, associationClassAC3_8);

        db.commit();
        Assert.assertEquals(4, associationClass5_2.getToAssociationclass5().size());
        associationClass5_2.reload();
        Assert.assertEquals(4, associationClass5_2.getToAssociationclass5().size());

    }

}
