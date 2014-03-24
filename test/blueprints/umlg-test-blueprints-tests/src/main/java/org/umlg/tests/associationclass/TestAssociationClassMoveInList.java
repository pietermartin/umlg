package org.umlg.tests.associationclass;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.associationclass.AssociationClass1;
import org.umlg.associationclass.AssociationClass2;
import org.umlg.associationclass.AssociationClassAC;
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
        Assert.assertEquals(1, associationClass1_1.getAssociationClassAC().size());
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

}
