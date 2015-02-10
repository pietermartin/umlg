package org.umlg.tests.associationtoself;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.associationtoself.*;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.Arrays;

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
        associationToSelf2.reload();
        associationToSelf3.reload();
        Assert.assertEquals(2, associationToSelf1.getAssocationTo().size());
        Assert.assertEquals(2, associationToSelf1.getAssocationFrom().size());
        Assert.assertEquals(associationToSelf1, associationToSelf2.getAssocationFrom().get(0));
        Assert.assertEquals(associationToSelf1, associationToSelf3.getAssocationFrom().get(0));

        Assert.assertEquals(associationToSelf1, associationToSelf2.getAssocationTo().get(0));
        Assert.assertEquals(associationToSelf1, associationToSelf3.getAssocationTo().get(0));

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

    @Test
    public void testSelfAssociationClass() {
        E a = new E();
        a.setName("A");
        E b = new E();
        b.setName("B");
        E c = new E();
        c.setName("C");
        E d = new E();
        d.setName("D");

        EAC ab = new EAC();
        ab.setName("AB");
        a.addToTo(b, ab);
        EAC ac = new EAC();
        ac.setName("AC");
        a.addToTo(c, ac);
        EAC ad = new EAC();
        ad.setName("AD");
        a.addToTo(d, ad);
        db.commit();

        a.reload();
        b.reload();
        Assert.assertEquals(3, a.getTo().size());
        Assert.assertEquals(b, a.getTo().get(0));
        Assert.assertEquals(c, a.getTo().get(1));
        Assert.assertEquals(d, a.getTo().get(2));
        Assert.assertEquals(1, b.getFrom().size());
        Assert.assertEquals(1, c.getFrom().size());
        Assert.assertEquals(1, d.getFrom().size());
        Assert.assertEquals(a, b.getFrom().get(0));
        Assert.assertEquals(a, c.getFrom().get(0));
        Assert.assertEquals(a, d.getFrom().get(0));
        Assert.assertEquals(0, b.getTo().size());

        Assert.assertEquals(3, a.getEAC_to().size());
        for (EAC eac : a.getEAC_to()) {
            Assert.assertTrue(Arrays.asList("AB", "AC", "AD").contains(eac.getName()));
        }
        Assert.assertEquals(1, b.getEAC_from().size());
        Assert.assertEquals(1, b.getEAC_from().size());
        Assert.assertEquals(1, b.getEAC_from().size());
        Assert.assertEquals(0, b.getEAC_to().size());
        Assert.assertEquals(0, b.getEAC_to().size());
        Assert.assertEquals(0, b.getEAC_to().size());

        E e = new E();
        e.setName("E");

        EAC dc = new EAC();
        dc.setName("DC");
        b.addToTo(c, dc);
        EAC dd = new EAC();
        dd.setName("DD");
        b.addToTo(d, dd);
        EAC de = new EAC();
        de.setName("DE");
        b.addToTo(e, de);

        db.commit();
        a.reload();
        b.reload();
        c.reload();
        d.reload();
        e.reload();

        Assert.assertEquals(3, b.getTo().size());
        Assert.assertEquals(c, b.getTo().get(0));
        Assert.assertEquals(d, b.getTo().get(1));
        Assert.assertEquals(e, b.getTo().get(2));

        Assert.assertEquals(1, b.getFrom().size());
        Assert.assertEquals(a, b.getFrom().get(0));

        Assert.assertEquals(3, a.getEAC_to().size());
        Assert.assertEquals(3, b.getEAC_to().size());
        Assert.assertEquals(2, c.getEAC_from().size());

    }

    @Test
    public void testSelfAssociationClassSimple() {
        E a = new E();
        a.setName("A");
        E b = new E();
        b.setName("B");

        EAC ab = new EAC();
        ab.setName("AB");
        a.addToTo(b, ab);
        db.commit();

        a.reload();
        b.reload();
        ab.reload();
        Assert.assertEquals(1, b.getFrom().size());
        Assert.assertEquals(0, b.getTo().size());
        Assert.assertEquals(1, a.getEAC_to().size());
    }

    @Test
    public void testSelfAssociationClassSelf() {
        E a = new E();
        a.setName("A");
        E b = new E();
        b.setName("B");

        EAC ab = new EAC();
        ab.setName("AB");
        a.addToTo(b, ab);

        EAC aa = new EAC();
        aa.setName("AA");
        a.addToTo(a, aa);
        db.commit();

        a.reload();
        b.reload();
        aa.reload();
        ab.reload();

        Assert.assertEquals(2, a.getTo().size());
        Assert.assertEquals(1, a.getFrom().size());
        Assert.assertEquals(0, b.getTo().size());
        Assert.assertEquals(1, b.getFrom().size());

        Assert.assertEquals(2, a.getEAC_to().size());
        Assert.assertEquals(1, a.getEAC_from().size());

    }

    @Test
    public void moveAssociationClassToSelf() {

        E a = new E();
        a.setName("A");

        E b = new E();
        b.setName("B");
        EAC ab = new EAC();
        ab.setName("AB");
        a.addToTo(b, ab);

        E c = new E();
        c.setName("C");
        EAC ac = new EAC();
        ac.setName("AC");
        a.addToTo(c, ac);

        db.commit();

        a.reload();
        b.reload();
        c.reload();
        ab.reload();
        ac.reload();

        Assert.assertEquals(b, a.getTo().get(0));
        Assert.assertEquals(c, a.getTo().get(1));

        a.moveTo(0, c);
        db.commit();

        a.reload();
        b.reload();
        c.reload();
        ab.reload();
        ac.reload();
        Assert.assertEquals(c, a.getTo().get(0));
        Assert.assertEquals(b, a.getTo().get(1));

        a.moveTo(0, b);
        a.reload();
        b.reload();
        c.reload();
        ab.reload();
        ac.reload();

        Assert.assertEquals(b, a.getTo().get(0));
        Assert.assertEquals(c, a.getTo().get(1));

        a.moveTo(1, b);
        a.reload();
        b.reload();
        c.reload();
        ab.reload();
        ac.reload();

        Assert.assertEquals(c, a.getTo().get(0));
        Assert.assertEquals(b, a.getTo().get(1));

    }

}
