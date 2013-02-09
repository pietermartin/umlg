package org.tuml.datatype.test;

import junit.framework.Assert;
import org.junit.Test;
import org.tuml.componenttest.Space;
import org.tuml.componenttest.SpaceTime;
import org.tuml.componenttest.Time;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.runtime.test.BaseLocalDbTest;

/**
 * Date: 2012/12/15
 * Time: 10:36 AM
 */
public class EmailTest extends BaseLocalDbTest {

    @Test
    public void testEmailDataType() {
        God g = new God(true);
        g.setName("g");
        Universe universe = new Universe(g);
        universe.setName("u");
        universe.setEmail("ding.dong@lalaland.com");
        SpaceTime spaceTime = new SpaceTime(universe);
        Space space = new Space(spaceTime);
        Time time = new Time(spaceTime);
        db.commit();
        Universe testUniverse1 = new Universe(universe.getVertex());
        Assert.assertEquals("ding.dong@lalaland.com", testUniverse1.getEmail());
    }
}
