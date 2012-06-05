package org.nakeuml.tinker.qualifiertest;

import junit.framework.Assert;

import org.junit.Test;
import org.tinker.concretetest.God;
import org.tinker.qualifiertest.Many1;
import org.tinker.qualifiertest.Many2;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class TestQualifierOnManyToMany extends BaseLocalDbTest {

	@Test
	public void testQualifierOnManyToManySet() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		
		Many1 many1_1 = new Many1(true);
		many1_1.setName("many1_1");
		many1_1.init(god);
		many1_1.addToOwningObject();
		
		Many1 many1_2 = new Many1(true);
		many1_2.setName("many1_1");
		many1_2.init(god);
		many1_2.addToOwningObject();

		Many1 many1_3 = new Many1(true);
		many1_3.setName("many1_1");
		many1_3.init(god);
		many1_3.addToOwningObject();

		Many1 many1_4 = new Many1(true);
		many1_4.setName("many1_4");
		many1_4.init(god);
		many1_4.addToOwningObject();

		Many2 many2_1 = new Many2(true);
		many2_1.setName("many2_1");
		many2_1.init(god);
		many2_1.addToOwningObject();
		
		Many2 many2_2 = new Many2(true);
		many2_2.setName("many2_2");
		many2_2.init(god);
		many2_2.addToOwningObject();

		Many2 many2_3 = new Many2(true);
		many2_3.setName("many2_3");
		many2_3.init(god);
		many2_3.addToOwningObject();

		Many2 many2_4 = new Many2(true);
		many2_4.setName("many2_4");
		many2_4.init(god);
		many2_4.addToOwningObject();

		many1_1.addToMany2(many2_1);
		many1_1.addToMany2(many2_2);
		many1_1.addToMany2(many2_3);
		many1_1.addToMany2(many2_4);

		many1_2.addToMany2(many2_1);
		many1_2.addToMany2(many2_2);
		many1_2.addToMany2(many2_3);
		many1_2.addToMany2(many2_4);

		many1_3.addToMany2(many2_1);
		many1_3.addToMany2(many2_2);
		many1_3.addToMany2(many2_3);
		many1_3.addToMany2(many2_4);

		many1_4.addToMany2(many2_1);
		many1_4.addToMany2(many2_2);
		many1_4.addToMany2(many2_3);
		many1_4.addToMany2(many2_4);

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(9, countVertices());
		Assert.assertEquals(25, countEdges());
		
		Many1 m = new Many1(many1_1.getVertex());
		Assert.assertNotNull(m.getMany2ForQualifier1("many2_1"));
		Assert.assertNotNull(m.getMany2ForQualifier1("many2_2"));
		Assert.assertNotNull(m.getMany2ForQualifier1("many2_3"));
		Assert.assertNotNull(m.getMany2ForQualifier1("many2_4"));
		
		Many2 m2 = new Many2(many2_1.getVertex());
		Assert.assertEquals(3, m2.getMany1ForQualifier1("many1_1").size());
		Assert.assertEquals(3, m2.getMany1ForQualifier1("many1_1").size());
		Assert.assertEquals(3, m2.getMany1ForQualifier1("many1_1").size());
		Assert.assertEquals(3, m2.getMany1ForQualifier1("many1_1").size());
		
		Assert.assertEquals(1, m2.getMany1ForQualifier1("many1_4").size());
		Assert.assertEquals(1, m2.getMany1ForQualifier1("many1_4").size());
		Assert.assertEquals(1, m2.getMany1ForQualifier1("many1_4").size());
		Assert.assertEquals(1, m2.getMany1ForQualifier1("many1_4").size());
		
	}

	@Test
	public void testQualifierOnManyToManySequence() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		
		Many1 many1_1 = new Many1(true);
		many1_1.setName("many1_1");
		many1_1.init(god);
		many1_1.addToOwningObject();
		
		Many1 many1_2 = new Many1(true);
		many1_2.setName("many1_1");
		many1_2.init(god);
		many1_2.addToOwningObject();

		Many1 many1_3 = new Many1(true);
		many1_3.setName("many1_1");
		many1_3.init(god);
		many1_3.addToOwningObject();

		Many1 many1_4 = new Many1(true);
		many1_4.setName("many1_4");
		many1_4.init(god);
		many1_4.addToOwningObject();

		Many2 many2_1 = new Many2(true);
		many2_1.setName("many2_1");
		many2_1.init(god);
		many2_1.addToOwningObject();
		
		Many2 many2_2 = new Many2(true);
		many2_2.setName("many2_2");
		many2_2.init(god);
		many2_2.addToOwningObject();

		Many2 many2_3 = new Many2(true);
		many2_3.setName("many2_3");
		many2_3.init(god);
		many2_3.addToOwningObject();

		Many2 many2_4 = new Many2(true);
		many2_4.setName("many2_4");
		many2_4.init(god);
		many2_4.addToOwningObject();

		many1_1.addToMany2List(many2_1);
		many1_1.addToMany2List(many2_2);
		many1_1.addToMany2List(many2_3);
		many1_1.addToMany2List(many2_4);

		many1_2.addToMany2List(many2_1);
		many1_2.addToMany2List(many2_2);
		many1_2.addToMany2List(many2_3);
		many1_2.addToMany2List(many2_4);

		many1_3.addToMany2List(many2_1);
		many1_3.addToMany2List(many2_2);
		many1_3.addToMany2List(many2_3);
		many1_3.addToMany2List(many2_4);

		many1_4.addToMany2List(many2_1);
		many1_4.addToMany2List(many2_2);
		many1_4.addToMany2List(many2_3);
		many1_4.addToMany2List(many2_4);

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(9, countVertices());
		Assert.assertEquals(25, countEdges());
		
		Many1 m = new Many1(many1_1.getVertex());
		Assert.assertNotNull(m.getMany2ListForListQualifier2("many2_1"));
		Assert.assertNotNull(m.getMany2ListForListQualifier2("many2_2"));
		Assert.assertNotNull(m.getMany2ListForListQualifier2("many2_3"));
		Assert.assertNotNull(m.getMany2ListForListQualifier2("many2_4"));
		
		Many2 m2 = new Many2(many2_1.getVertex());
		Assert.assertEquals(3, m2.getMany1ListForListQualifier1("many1_1").size());
		Assert.assertEquals(3, m2.getMany1ListForListQualifier1("many1_1").size());
		Assert.assertEquals(3, m2.getMany1ListForListQualifier1("many1_1").size());
		Assert.assertEquals(3, m2.getMany1ListForListQualifier1("many1_1").size());
		
		Assert.assertEquals(1, m2.getMany1ListForListQualifier1("many1_4").size());
		Assert.assertEquals(1, m2.getMany1ListForListQualifier1("many1_4").size());
		Assert.assertEquals(1, m2.getMany1ListForListQualifier1("many1_4").size());
		Assert.assertEquals(1, m2.getMany1ListForListQualifier1("many1_4").size());
		
		db.startTransaction();

		boolean illegalStateExceptionHappened = false;
		try {
			//There is a uniqueu qualifier on many2.name
			many1_1.addToMany2List(many2_1);
		} catch (IllegalStateException e) {
			illegalStateExceptionHappened = true;
		}
		
		Assert.assertTrue(illegalStateExceptionHappened);
		db.stopTransaction(Conclusion.SUCCESS);
	}

}
