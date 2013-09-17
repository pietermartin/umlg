package org.umlg.tinker.qualifiertest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.qualifiertest.Nature;
import org.umlg.runtime.collection.TinkerSet;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.Set;

public class TestQualifier extends BaseLocalDbTest {

	@Test
	public void testQualifiedGetter() {
		God god = new God(true);
		god.setName("THEGOD");
		Nature nature = new Nature(true);
		nature.setName1("nature1");
		nature.setName2("nature2");
		nature.addToGod(god);
        db.commit();
		
		God godTest = new God(god.getVertex());
		TinkerSet<Nature> natureForQualifier1 = godTest.getNatureForQualifier2("nature2");
		Assert.assertFalse(natureForQualifier1.isEmpty());
		Assert.assertEquals("nature1", natureForQualifier1.iterator().next().getName1());
		natureForQualifier1 = godTest.getNatureForQualifier2("nature1");
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
		nature.addToGod(god);
        db.commit();

		nature = new Nature(true);
		nature.setName1("name1_1");
		nature.setName2("xxx");
		nature.addToGod(god);
        db.commit();

		nature = new Nature(true);
		nature.setName1("name1_2");
		nature.setName2("xxx");
		nature.addToGod(god);
        db.commit();

		nature = new Nature(true);
		nature.setName1("name1_3");
		nature.setName2("xxx");
		nature.addToGod(god);
        db.commit();

		nature = new Nature(true);
		nature.setName1("name1_4");
		nature.setName2("yyy");
		nature.addToGod(god);
        db.commit();

		God godTest = new God(god.getVertex());
		Set<Nature> natureForQualifier2 = godTest.getNatureForQualifier2("xxx");
		Assert.assertEquals(4, natureForQualifier2.size());
		natureForQualifier2 = godTest.getNatureForQualifier2("yyy");
		Assert.assertEquals(1, natureForQualifier2.size());
	}

}
