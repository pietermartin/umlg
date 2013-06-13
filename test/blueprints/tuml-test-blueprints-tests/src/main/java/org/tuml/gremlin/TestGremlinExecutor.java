package org.tuml.gremlin;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.componenttest.Space;
import org.tuml.componenttest.SpaceTime;
import org.tuml.componenttest.Time;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.runtime.adaptor.TumlQueryEnum;
import org.tuml.runtime.test.BaseLocalDbTest;

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
        Space s = new Space(st);
        Time t = new Time(st);

        god.addToUniverse(universe1);
        db.commit();
        Assert.assertNotNull(universe1.getGod());

        String result = db.executeQuery(TumlQueryEnum.GREMLIN, god.getId(), "out");
        System.out.println(result);
    }
}
