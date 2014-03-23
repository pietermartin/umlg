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
import org.umlg.concretetest.meta.GodMeta;
import org.umlg.runtime.adaptor.GroovyExecutor;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgQueryEnum;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/06/11
 * Time: 9:19 PM
 */
public class TestGroovyExecutor extends BaseLocalDbTest {

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

        String result = db.executeQueryToString(UmlgQueryEnum.GROOVY, god.getId(), "self.out");
        Assert.assertTrue(result.startsWith("v["));

        String idAsString;
        Object id = god.getId();
        //This logic is for Bitsy that uses its own UUID as id
        if (id instanceof Long) {
            idAsString = id.toString();
        } else {
            idAsString = "'" + id.toString() + "'";
        }
        Object gremlinResult = GroovyExecutor.INSTANCE.executeGroovy(god.getId(), "g.v(" + idAsString + ")");
        Assert.assertTrue(gremlinResult instanceof Vertex);

        gremlinResult = GroovyExecutor.INSTANCE.executeGroovy(null, "TransactionalGraph.Conclusion.SUCCESS.toString(); TransactionalGraph.Conclusion.FAILURE.toString()");
        Assert.assertEquals("FAILURE", gremlinResult);

        gremlinResult = GroovyExecutor.INSTANCE.executeGroovy(null, "g.V.hasNot('age',null).has('age',T.gt,25).count()");
        Assert.assertEquals(0L, gremlinResult);

        gremlinResult = GroovyExecutor.INSTANCE.executeGroovy(null, "def isGod(v){v.name=='THEGOD'};g.V.filter{isGod(it)}.next()");
        Assert.assertTrue(gremlinResult instanceof Vertex);
        Assert.assertEquals(god.getId(), ((Vertex) gremlinResult).getId());

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

        String result = db.executeQueryToString(UmlgQueryEnum.GROOVY, god.getId(), "self.umlgtest::org::umlg::concretetest::God::name");
        Assert.assertTrue(result.startsWith("THEGOD"));

        result = db.executeQueryToString(UmlgQueryEnum.GROOVY, god.getId(), "self.has('umlgtest::org::umlg::concretetest::God::name')");
        Assert.assertTrue(result.startsWith("v["));

        result = db.executeQueryToString(UmlgQueryEnum.GROOVY, god.getId(), "self.has('name')");
        Assert.assertTrue(result.startsWith("v["));

    }

    @Test
    public void testUmlgImports() {

        God god = new God(true);
        god.setName("THEGOD");
        Universe universe1 = new Universe(true);
        universe1.setName("universe1");
        SpaceTime st = new SpaceTime(universe1);
        new Space(st);
        new Time(st);

        god.addToUniverse(universe1);
        db.commit();
        Assert.assertEquals(
                1, GodMeta.getInstance().getAllInstances().size());
        Assert.assertNotNull(universe1.getGod());

        StringBuilder sb = new StringBuilder();
        sb.append("God newGod = new God();");
        sb.append("newGod.setName('THEGOD2');");
        sb.append("Universe universeNew = new Universe(true);");
        sb.append("universeNew.setName(\"universeNew\");");
        sb.append("SpaceTime stNew = new SpaceTime(universeNew);");
        sb.append("new Space(stNew);");
        sb.append("new Time(stNew);");
        sb.append("newGod.addToUniverse(universeNew);");

        String result = db.executeQueryToString(UmlgQueryEnum.GROOVY, null, sb.toString());
        db.commit();

        Assert.assertEquals(2, GodMeta.getInstance().getAllInstances().size());
    }

    @Test
    public void testNameResolutionNotToString() {
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

        Object result = db.executeQuery(UmlgQueryEnum.GROOVY, god.getId(), "self.umlgtest::org::umlg::concretetest::God::name");
        Assert.assertTrue(result instanceof String);
        Assert.assertTrue(((String)result).startsWith("THEGOD"));

        result = db.executeQuery(UmlgQueryEnum.GROOVY, god.getId(), "self.has('umlgtest::org::umlg::concretetest::God::name')");
        Assert.assertTrue(result instanceof Pipeline);
        Assert.assertEquals(god.getId(), ((Pipeline<Object, Vertex>) result).next().getId());

        result = db.executeQuery(UmlgQueryEnum.GROOVY, god.getId(), "self.has('name')");
        Assert.assertTrue(result instanceof Pipeline);
        Assert.assertEquals(god.getId(), ((Pipeline<Object, Vertex>) result).next().getId());
    }

    @Test(expected = RuntimeException.class)
    public void testGremlinReadOnly1() {
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
        db.executeQuery(UmlgQueryEnum.GROOVY, god.getId(), "g.addVertex()");
    }

    @Test(expected = RuntimeException.class)
    public void testGremlinReadOnly2() {
        God god = new God(true);
        god.setName("THEGOD");
        Universe universe1 = new Universe(true);
        universe1.setName("universe1");
        SpaceTime st = new SpaceTime(universe1);
        new Space(st);
        new Time(st);

        god.addToUniverse(universe1);
        db.commit();
        String idAsString;
        Object id = god.getId();
        //This logic is for Bitsy that uses its own UUID as id
        if (id instanceof Long) {
            idAsString = id.toString();
        } else {
            idAsString = "'" + id.toString() + "'";
        }
        Assert.assertNotNull(universe1.getGod());
        Object result = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, null, "g.v(" + idAsString + ");");
        Assert.assertTrue(result instanceof Vertex);
        db.executeQuery(UmlgQueryEnum.GROOVY, god.getId(), "v = g.v(1);v.name = 'halo'");
    }

}
