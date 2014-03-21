package org.umlg.tests.associationtoself;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.associationtoself.AssociationToSelf;
import org.umlg.associationtoself.SequenceTestAgain1;
import org.umlg.associationtoself.SequenceTestAgain2;
import org.umlg.collectiontest.SequenceRoot;
import org.umlg.collectiontest.SequenceTestListMany;
import org.umlg.collectiontest.SequenceTestOrderedSet;
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

        associationToSelf1.addToAssocationFrom(associationToSelf2);
        associationToSelf1.addToAssocationFrom(associationToSelf3);
        db.commit();

        associationToSelf1.reload();
        Assert.assertEquals(2, associationToSelf1.getAssocationTo().size());
        Assert.assertEquals(2, associationToSelf1.getAssocationFrom().size());

    }

    @Test
    public void testSequence() {

        SequenceTestAgain1 sequenceTestAgain1 = new SequenceTestAgain1();
        sequenceTestAgain1.setName("sequenceTestAgain1");

        SequenceTestAgain2 sequenceTestAgain21 = new SequenceTestAgain2();
        sequenceTestAgain21.setName("sequenceTestAgain21");
        SequenceTestAgain2 sequenceTestAgain22 = new SequenceTestAgain2();
        sequenceTestAgain22.setName("sequenceTestAgain22");
        SequenceTestAgain2 sequenceTestAgain23 = new SequenceTestAgain2();
        sequenceTestAgain23.setName("sequenceTestAgain23");

        sequenceTestAgain1.addToSequenceTestAgain2(sequenceTestAgain21);
        sequenceTestAgain1.addToSequenceTestAgain2(sequenceTestAgain22);
        sequenceTestAgain1.addToSequenceTestAgain2(sequenceTestAgain23);
        db.commit();

        sequenceTestAgain1.reload();
        Assert.assertEquals(3, sequenceTestAgain1.getSequenceTestAgain2().size());

        sequenceTestAgain21.reload();
        Assert.assertEquals(1, sequenceTestAgain21.getSequenceTestAgain1().size());
        sequenceTestAgain22.reload();
        Assert.assertEquals(1, sequenceTestAgain22.getSequenceTestAgain1().size());
        sequenceTestAgain23.reload();
        Assert.assertEquals(1, sequenceTestAgain23.getSequenceTestAgain1().size());

        SequenceTestAgain1 sequenceTestAgain12 = new SequenceTestAgain1();
        sequenceTestAgain12.setName("sequenceTestAgain12");
        SequenceTestAgain1 sequenceTestAgain13 = new SequenceTestAgain1();
        sequenceTestAgain13.setName("sequenceTestAgain13");

        sequenceTestAgain21.addToSequenceTestAgain1(sequenceTestAgain12);
        sequenceTestAgain22.addToSequenceTestAgain1(sequenceTestAgain12);
        sequenceTestAgain23.addToSequenceTestAgain1(sequenceTestAgain12);

        sequenceTestAgain21.addToSequenceTestAgain1(sequenceTestAgain13);
        sequenceTestAgain22.addToSequenceTestAgain1(sequenceTestAgain13);
        sequenceTestAgain23.addToSequenceTestAgain1(sequenceTestAgain13);

        db.commit();

        sequenceTestAgain21.reload();
        Assert.assertEquals(3, sequenceTestAgain21.getSequenceTestAgain1().size());
        sequenceTestAgain22.reload();
        Assert.assertEquals(3, sequenceTestAgain22.getSequenceTestAgain1().size());
        sequenceTestAgain23.reload();
        Assert.assertEquals(3, sequenceTestAgain23.getSequenceTestAgain1().size());

    }
}
