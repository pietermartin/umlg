package org.umlg.gremlin;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.runtime.adaptor.UmlgQueryEnum;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/06/11
 * Time: 9:19 PM
 */
public class TestGremlinExecutor extends BaseLocalDbTest {

    @Test
    public void test() {
        God god = new God(true);
        god.setName("THEGOD");
        Universe universe1 = new Universe(true);
        universe1.setName("universe1");
        SpaceTime st = new SpaceTime(universe1);
        new Space(st);
        new Time(st);

        god.addToUniverse(universe1);
        db.commit();
        Assert.assertNotNull(universe1.getGod());

        String result = db.executeQuery(UmlgQueryEnum.GREMLIN, god.getId(), "self.out");
        Assert.assertTrue(result.startsWith("v["));
    }

    @Test
    public void testNameResolution() {
        God god = new God(true);
        god.setName("THEGOD");
        Universe universe1 = new Universe(true);
        universe1.setName("universe1");
        SpaceTime st = new SpaceTime(universe1);
        new Space(st);
        new Time(st);

        god.addToUniverse(universe1);
        db.commit();
        Assert.assertNotNull(universe1.getGod());

        String result = db.executeQuery(UmlgQueryEnum.GREMLIN, god.getId(), "self.umlgtest::org::umlg::concretetest::God::name");
        Assert.assertTrue(result.startsWith("THEGOD"));

        result = db.executeQuery(UmlgQueryEnum.GREMLIN, god.getId(), "self.has('umlgtest::org::umlg::concretetest::God::name')");
        Assert.assertTrue(result.startsWith("v["));

        result = db.executeQuery(UmlgQueryEnum.GREMLIN, god.getId(), "self.has('name')");
        Assert.assertTrue(result.startsWith("v["));

    }
}
