package org.umlg.tests.qualifiertest;

import com.tinkerpop.gremlin.process.T;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.qualifiertest.Nature;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.util.Pair;

import java.util.Set;

public class TestQualifier extends BaseLocalDbTest {

	@Test
	public void testQualifiedGetter() {
		God god = new God(true);
		god.setName("THEGOD");
		Nature nature = new Nature(true);
		nature.setName1("nature1");
		nature.setName2("nature2");
        nature.setNameUnique("1");
        nature.addToGod(god);
        db.commit();

		God godTest = new God(god.getVertex());
		UmlgSet<Nature> natureForQualifier1 = godTest.getNatureForQualifier2(Pair.of(T.eq, "nature2"));
		Assert.assertFalse(natureForQualifier1.isEmpty());
		Assert.assertEquals("nature1", natureForQualifier1.iterator().next().getName1());
		natureForQualifier1 = godTest.getNatureForQualifier2(Pair.of(T.eq, "nature1"));
		Assert.assertTrue(natureForQualifier1.isEmpty());
        db.commit();
	}

	@Test
	public void testQualifiedMany() {
		God god = new God(true);
		god.setName("THEGOD");

		Nature nature = new Nature(true);
		nature.setName1("name1_0");
		nature.setName2("xxx");
        nature.setNameUnique("1");
        nature.addToGod(god);
        db.commit();

		nature = new Nature(true);
		nature.setName1("name1_1");
		nature.setName2("xxx");
        nature.setNameUnique("2");
		nature.addToGod(god);
        db.commit();

		nature = new Nature(true);
		nature.setName1("name1_2");
		nature.setName2("xxx");
        nature.setNameUnique("3");
        nature.addToGod(god);
        db.commit();

		nature = new Nature(true);
		nature.setName1("name1_3");
		nature.setName2("xxx");
        nature.setNameUnique("4");
		nature.addToGod(god);
        db.commit();

		nature = new Nature(true);
		nature.setName1("name1_1");
		nature.setName2("yyy");
        nature.setNameUnique("5");
		nature.addToGod(god);
        db.commit();

		God godTest = new God(god.getVertex());
		Set<Nature> natureForQualifier2 = godTest.getNatureForQualifier2(Pair.of(T.eq, "xxx"));
		Assert.assertEquals(4, natureForQualifier2.size());
		natureForQualifier2 = godTest.getNatureForQualifier2(Pair.of(T.eq, "yyy"));
		Assert.assertEquals(1, natureForQualifier2.size());

        Set<Nature> natureForQualifier3 = godTest.getNatureForName1Qualifier(Pair.of(T.eq, "name1_1"));
        Assert.assertEquals(2, natureForQualifier3.size());

    }

    @Test
    public void testUniqueNameNature() {

        God god = new God(true);
        god.setName("THEGOD");

        Nature nature = new Nature(true);
        nature.setName1("name1_0");
        nature.setName2("xxx");
        nature.setNameUnique("1");
        nature.addToGod(god);
        db.commit();

        nature = new Nature(true);
        nature.setName1("name1_1");
        nature.setName2("xxx");
        nature.setNameUnique("2");
        nature.addToGod(god);
        db.commit();

        nature = new Nature(true);
        nature.setName1("name1_2");
        nature.setName2("xxx");
        nature.setNameUnique("3");
        nature.addToGod(god);
        db.commit();

        nature = new Nature(true);
        nature.setName1("name1_3");
        nature.setName2("xxx");
        nature.setNameUnique("4");
        nature.addToGod(god);
        db.commit();

        nature = new Nature(true);
        nature.setName1("name1_1");
        nature.setName2("yyy");
        nature.setNameUnique("5");
        nature.addToGod(god);
        db.commit();

        god.reload();
        Assert.assertNotNull(god.getNatureForNameUniqueQualifier(Pair.of(T.eq, "1")));
        Assert.assertEquals("2", god.getNatureForNameUniqueQualifier(Pair.of(T.eq, "2")).iterator().next().getNameUnique());
        Assert.assertEquals("3", god.getNatureForNameUniqueQualifier(Pair.of(T.eq, "3")).iterator().next().getNameUnique());
        Assert.assertEquals("4", god.getNatureForNameUniqueQualifier(Pair.of(T.eq, "4")).iterator().next().getNameUnique());
        Assert.assertEquals("5", god.getNatureForNameUniqueQualifier(Pair.of(T.eq, "5")).iterator().next().getNameUnique());

    }

}
