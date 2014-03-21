package org.umlg.tests.associationtoself;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.associationtoself.AssociationToSelf;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/03/21
 * Time: 4:46 PM
 */
public class TestAssociationToSelf extends BaseLocalDbTest {

    @Test
    public void testOrderedAssociationToSelf() {
        AssociationToSelf associationToSelf1 = new AssociationToSelf();
        associationToSelf1.setName("associationToSelf1");
        AssociationToSelf associationToSelf2 = new AssociationToSelf();
        associationToSelf2.setName("associationToSelf2");
        AssociationToSelf associationToSelf3 = new AssociationToSelf();
        associationToSelf3.setName("associationToSelf3");

        associationToSelf1.addToAssocationTo(associationToSelf2);
        associationToSelf1.addToAssocationTo(associationToSelf3);
        db.commit();

        associationToSelf1.reload();
        Assert.assertEquals(2, associationToSelf1.getAssocationTo().size());

    }
}
