package org.umlg.tests.primitive;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.Foot;
import org.umlg.concretetest.God;
import org.umlg.runtime.test.BaseLocalDbTest;

public class TestBooleanPrimitive extends BaseLocalDbTest {

    @Test
    public void testBooleanDefaultsToFalse() {
        God g = new God(true);
        g.setName("G");
        db.commit();
        Assert.assertFalse(g.getTestBoolean());
    }

    @Test(expected = IllegalStateException.class)
    public void testQualifiedSequence2() {
        God god = new God(true);
        god.setName("THEGOD");
        Foot foot1 = new Foot(true);
        foot1.setName("foot1");
        foot1.addToGod(god);
        Foot foot2 = new Foot(true);
        foot2.setName("foot1");
        System.out.println(foot2.getName());
        foot2.addToGod(god);
        db.commit();
    }

}
