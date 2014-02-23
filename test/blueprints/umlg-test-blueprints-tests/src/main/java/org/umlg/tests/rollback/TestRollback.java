package org.umlg.tests.rollback;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/01/24
 * Time: 7:21 PM
 */
public class TestRollback extends BaseLocalDbTest {

    @SuppressWarnings("unused")
//    @Test
    public void testRollback() {
        God god = new God(true);
        god.setName("THEGOD");
        Universe universe1 = new Universe(true);
        universe1.setName("universe1");
        SpaceTime st = new SpaceTime(universe1);
        Space s = new Space(st);
        Time t = new Time(st);

        god.addToUniverse(universe1);
        db.rollback();
        Assert.assertNull(universe1.getGod());
    }

}
