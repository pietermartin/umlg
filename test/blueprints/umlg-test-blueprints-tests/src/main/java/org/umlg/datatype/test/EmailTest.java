package org.umlg.datatype.test;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.datatype.DataTypeEntity;
import org.umlg.runtime.test.BaseLocalDbTest;

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
        new Space(spaceTime);
        new Time(spaceTime);
        db.commit();
        Universe testUniverse1 = new Universe(universe.getVertex());
        Assert.assertEquals("ding.dong@lalaland.com", testUniverse1.getEmail());
    }

    @Test
    public void testManyDataType() {
        DataTypeEntity dataTypeEntity = new DataTypeEntity(true);
        dataTypeEntity.addToEmail1("j@j.j");
        db.commit();
    }
}
