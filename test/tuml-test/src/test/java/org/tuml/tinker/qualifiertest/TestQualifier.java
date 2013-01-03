package org.tuml.tinker.qualifiertest;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.qualifiertest.Nature;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

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
		Assert.assertTrue(!natureForQualifier1.isEmpty());
		Assert.assertEquals("nature1", natureForQualifier1.iterator().next().getName1());
		natureForQualifier1 = godTest.getNatureForQualifier2("nature1");
		Assert.assertTrue(natureForQualifier1.isEmpty());
        db.commit();
	}
	
	@Test
	public void testQualifiedWithNull() {
		db.setCheckElementsInTransaction(true);
		God god = new God(true);
		god.setName("THEGOD");
		
		Nature nature = new Nature(true);
		nature.addToGod(god);
        db.commit();
		
		God godTest = new God(god.getVertex());
		god.setName("ss");
		TinkerSet<Nature> natureForQualifier1 = godTest.getNatureForQualifier2(null);
		Assert.assertTrue(!natureForQualifier1.isEmpty());
		Assert.assertNull(natureForQualifier1.iterator().next().getName1());
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
