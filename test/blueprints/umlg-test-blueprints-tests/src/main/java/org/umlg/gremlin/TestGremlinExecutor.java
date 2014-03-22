package org.umlg.gremlin;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.util.Pipeline;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
import org.umlg.runtime.adaptor.GremlinExecutor;
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

        Object gremlinResult = GremlinExecutor.executeGremlin(god.getId(), "g.v(1)");
        Assert.assertTrue(gremlinResult instanceof Vertex);

        gremlinResult = GremlinExecutor.executeGremlin(null, "TransactionalGraph.Conclusion.SUCCESS.toString(); TransactionalGraph.Conclusion.FAILURE.toString()");
        Assert.assertEquals("FAILURE", gremlinResult);

        gremlinResult = GremlinExecutor.executeGremlin(null, "g.V.hasNot('age',null).has('age',T.gt,25).count()");
        Assert.assertEquals(0L, gremlinResult);

        gremlinResult = GremlinExecutor.executeGremlin(null, "def isGod(v){v.name=='THEGOD'};g.V.filter{isGod(it)}.next()");
        Assert.assertTrue(gremlinResult instanceof Vertex);
        Assert.assertEquals(god.getId(), ((Vertex)gremlinResult).getId());

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
