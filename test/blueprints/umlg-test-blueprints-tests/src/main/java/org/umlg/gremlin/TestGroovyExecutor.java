package org.umlg.gremlin;

import org.apache.tinkerpop.gremlin.process.Traversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.componenttest.Space;
import org.umlg.componenttest.SpaceTime;
import org.umlg.componenttest.Time;
import org.umlg.concretetest.God;
import org.umlg.concretetest.Universe;
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

        String result = db.executeQueryToJson(UmlgQueryEnum.GROOVY, god.getId(), "self.out()");
        Assert.assertTrue(result.contains("v["));

        String idAsString;
        Object id = god.getId();
        //This logic is for Bitsy that uses its own UUID as id
        if (id instanceof Long) {
            idAsString = id.toString() + "L";
        } else {
            idAsString = "'" + id.toString() + "'";
        }
        Object gremlinResult = GroovyExecutor.INSTANCE.executeGroovy(god.getId(), "g.V(" + idAsString + ").next()");
        Assert.assertTrue(gremlinResult instanceof Vertex);

        gremlinResult = GroovyExecutor.INSTANCE.executeGroovy(null, "Direction.OUT.toString(); Direction.IN.toString()");
        Assert.assertEquals("IN", gremlinResult);

        gremlinResult = GroovyExecutor.INSTANCE.executeGroovy(null, "g.V().has('age').has('age',Compare.gt,25).count().next()");
        Assert.assertEquals(0L, gremlinResult);

        //TODO used to work
//        gremlinResult = GroovyExecutor.INSTANCE.executeGroovy(null, "def isGod(v){v.has('name', 'THEGOD').hasNext()};g.V.filter{isGod(it)}.next()");
//        Assert.assertTrue(gremlinResult instanceof Vertex);
//        Assert.assertEquals(god.getId(), ((Vertex) gremlinResult).id());

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

        String result = db.executeQueryToJson(UmlgQueryEnum.GROOVY, god.getId(), "self.value('name')");
        Assert.assertTrue(result.startsWith("THEGOD"));

        result = db.executeQueryToJson(UmlgQueryEnum.GROOVY, god.getId(), "self.has('name').next()");
        Assert.assertTrue(result.startsWith("v["));

        result = db.executeQueryToJson(UmlgQueryEnum.GROOVY, god.getId(), "self.has('name').next()");
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
        Assert.assertEquals(1, God.allInstances().size());
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
        sb.append("UMLG.get().commit();");

        String result = db.executeQueryToJson(UmlgQueryEnum.GROOVY, null, sb.toString());
        db.commit();

        Assert.assertEquals(2, God.allInstances().size());
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

        Object result = db.executeQuery(UmlgQueryEnum.GROOVY, god.getId(), "self.value('name')");
        Assert.assertTrue(result instanceof String);
        Assert.assertTrue(((String)result).startsWith("THEGOD"));

        result = db.executeQuery(UmlgQueryEnum.GROOVY, god.getId(), "self.has('name')");
        Assert.assertTrue(result instanceof Traversal);
        Assert.assertEquals(god.getId(), ((Traversal<Object, Vertex>) result).next().id());

        result = db.executeQuery(UmlgQueryEnum.GROOVY, god.getId(), "self.has('name')");
        Assert.assertTrue(result instanceof Traversal);
        Assert.assertEquals(god.getId(), ((Traversal<Object, Vertex>) result).next().id());
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
            idAsString = id.toString() + "L";
        } else {
            idAsString = "'" + id.toString() + "'";
        }
        Assert.assertNotNull(universe1.getGod());
        Object result = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, null, "g.V(" + idAsString + ").next();");
        Assert.assertTrue(result instanceof Vertex);
        db.executeQuery(UmlgQueryEnum.GROOVY, god.getId(), "v = g.v(1);v.name = 'halo'");
    }

}
