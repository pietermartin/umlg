package org.umlg.tests.enumeration;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.embeddedtest.REASON;
import org.umlg.embeddedtest.TestEnumLiteralDuplicateName;
import org.umlg.embeddedtest.TestOrderedEnumBug;
import org.umlg.embeddedtest.TestOrderedEnumeration;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.memory.UmlgMemorySet;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.Arrays;
import java.util.Collection;

/**
 * Date: 2012/12/17
 * Time: 12:24 PM
 */
public class ManyEnumerationTest extends BaseLocalDbTest {

    @Test
    public void testManyEnumerationLoadFromVertex() {
        God g = new God(true);
        g.setName("g");
        g.addToREASON(REASON.BAD);
        g.addToREASON(REASON.GOOD);
        db.commit();

        Assert.assertEquals(2, g.getREASON().size());
        db.rollback();
        g.reload();
        Assert.assertEquals(2, g.getREASON().size());
        db.rollback();

        God godAgain = new God(g.getVertex());
        Assert.assertEquals(2, godAgain.getREASON().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testManyEnumeration() {
        God g = new God(true);
        g.setName("g");
        Object s = Arrays.asList(new Object[]{"asda", "asd"});
        UmlgSet<REASON> reasons = new UmlgMemorySet<REASON>((Collection<REASON>) s);
        g.addToREASON(reasons);
        db.commit();

    }

    @Test
    public void testOrderedEnumeration() {
        God g = new God(true);
        g.setName("g");
        TestOrderedEnumeration testOrderedEnumeration = new TestOrderedEnumeration(g);
        testOrderedEnumeration.addToTestOrderedEnumBug(TestOrderedEnumBug.EnumerationLiteral3);
        db.commit();
        Assert.assertEquals(1, countVertices());

        testOrderedEnumeration.reload();
        Assert.assertEquals(1, testOrderedEnumeration.getTestOrderedEnumBug().size());
        testOrderedEnumeration.removeFromTestOrderedEnumBug(testOrderedEnumeration.getTestOrderedEnumBug().get(0));
        db.commit();
        Assert.assertEquals(0, testOrderedEnumeration.getTestOrderedEnumBug().size());
    }

    @Test
    public void testDuplicateEnumerations() {
        God g = new God(true);
        g.setName("g");
        TestOrderedEnumeration testOrderedEnumeration = new TestOrderedEnumeration(g);
        testOrderedEnumeration.addToTestOrderedEnumBug(TestOrderedEnumBug.EnumerationLiteral1);
        testOrderedEnumeration.addToTestOrderedEnumBug(TestOrderedEnumBug.EnumerationLiteral2);
        testOrderedEnumeration.addToTestOrderedEnumBug(TestOrderedEnumBug.EnumerationLiteral3);

        testOrderedEnumeration.addToTestEnumLiteralDuplicateName(TestEnumLiteralDuplicateName.EnumerationLiteral1);
        testOrderedEnumeration.addToTestEnumLiteralDuplicateName(TestEnumLiteralDuplicateName.EnumerationLiteral2);
        testOrderedEnumeration.addToTestEnumLiteralDuplicateName(TestEnumLiteralDuplicateName.EnumerationLiteral3);

        db.commit();
        Assert.assertEquals(3, testOrderedEnumeration.getTestOrderedEnumBug().size());
        Assert.assertEquals(3, testOrderedEnumeration.getTestEnumLiteralDuplicateName().size());

    }

    @Test
    public void testOrderedEnumerationsOrder() {
        God g = new God(true);
        g.setName("g");
        TestOrderedEnumeration testOrderedEnumeration = new TestOrderedEnumeration(g);
        testOrderedEnumeration.addToTestOrderedEnumBug(TestOrderedEnumBug.EnumerationLiteral1);
        db.commit();
        Assert.assertEquals(1, testOrderedEnumeration.getTestOrderedEnumBug().size());

        testOrderedEnumeration.reload();
        testOrderedEnumeration.clearTestOrderedEnumBug();
        testOrderedEnumeration.addToTestOrderedEnumBug(TestOrderedEnumBug.EnumerationLiteral1);
        testOrderedEnumeration.addToTestOrderedEnumBug(TestOrderedEnumBug.EnumerationLiteral2);
        db.commit();
        testOrderedEnumeration = new TestOrderedEnumeration(testOrderedEnumeration.getId());
        Assert.assertEquals(2, testOrderedEnumeration.getTestOrderedEnumBug().size());

    }

}
