package org.nakeuml.tinker.qualifiertest;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.qualifiertest.Many1;
import org.tuml.qualifiertest.Many2;
import org.tuml.qualifiertest.Nature;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestQualifiedDeletion extends BaseLocalDbTest {

	@Test
	public void testDeletionManyToMany() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		
		Many1 many11 = new Many1(god);
		many11.setName("many11");
		Many1 many12 = new Many1(god);
		many12.setName("many12");
		Many1 many13 = new Many1(god);
		many13.setName("many13");
		Many1 many14 = new Many1(god);
		many14.setName("many14");

		Many2 many21 = new Many2(god);
		many21.setName("many21");
		Many2 many22 = new Many2(god);
		many22.setName("many22");
		Many2 many23 = new Many2(god);
		many23.setName("many23");
		Many2 many24 = new Many2(god);
		many24.setName("many24");

		many11.addToMany2(many21);
		many11.addToMany2(many22);
		many11.addToMany2(many23);
		many11.addToMany2(many24);

		many12.addToMany2(many21);
		many12.addToMany2(many22);
		many12.addToMany2(many23);
		many12.addToMany2(many24);

		many13.addToMany2(many21);
		many13.addToMany2(many22);
		many13.addToMany2(many23);
		many13.addToMany2(many24);

		many14.addToMany2(many21);
		many14.addToMany2(many22);
		many14.addToMany2(many23);
		many14.addToMany2(many24);

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(13, countVertices());
		Assert.assertEquals(29, countEdges());
		Many2 many2Test = new Many2(many21.getVertex());
		Assert.assertEquals(1, many2Test.getMany1ForQualifier1("many11").size());
		
		db.startTransaction();
		Many1 many1Test = new Many1(many11.getVertex());
		many1Test.delete();
		db.stopTransaction(Conclusion.SUCCESS);
		
		many2Test = new Many2(many21.getVertex());
		Assert.assertTrue(many2Test.getMany1ForQualifier1("many11").isEmpty());
	}

	@Test
	public void testDeletionManyToManyList() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		
		Many1 many11 = new Many1(god);
		many11.setName("many11");
		Many1 many12 = new Many1(god);
		many12.setName("many12");
		Many1 many13 = new Many1(god);
		many13.setName("many13");
		Many1 many14 = new Many1(god);
		many14.setName("many14");

		Many2 many21 = new Many2(god);
		many21.setName("many21");
		Many2 many22 = new Many2(god);
		many22.setName("many22");
		Many2 many23 = new Many2(god);
		many23.setName("many23");
		Many2 many24 = new Many2(god);
		many24.setName("many24");

		many11.addToMany2List(many21);
		many11.addToMany2List(many22);
		many11.addToMany2List(many23);
		many11.addToMany2List(many24);

		many12.addToMany2List(many21);
		many12.addToMany2List(many22);
		many12.addToMany2List(many23);
		many12.addToMany2List(many24);

		many13.addToMany2List(many21);
		many13.addToMany2List(many22);
		many13.addToMany2List(many23);
		many13.addToMany2List(many24);

		many14.addToMany2List(many21);
		many14.addToMany2List(many22);
		many14.addToMany2List(many23);
		many14.addToMany2List(many24);

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(13, countVertices());
		Assert.assertEquals(29, countEdges());
		Many2 many2Test = new Many2(many21.getVertex());
		Assert.assertEquals(1, many2Test.getMany1ListForListQualifier1("many11").size());
		
		db.startTransaction();
		Many1 many1Test = new Many1(many11.getVertex());
		many1Test.delete();
		db.stopTransaction(Conclusion.SUCCESS);
		
		many2Test = new Many2(many21.getVertex());
		Assert.assertTrue(many2Test.getMany1ListForListQualifier1("many11").isEmpty());
		
		Assert.assertEquals("many12", many2Test.getMany1List().get(0).getName());
		Assert.assertEquals("many13", many2Test.getMany1List().get(1).getName());
		Assert.assertEquals("many14", many2Test.getMany1List().get(2).getName());
	}
	
	@Test
	public void testQualifiedManyDeletion() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Nature nature = new Nature(true);
		nature.setName1("name1_0");
		nature.setName2("xxx");
		nature.addToGod(god);
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		nature = new Nature(true);
		nature.setName1("name1_1");
		nature.setName2("xxx");
		nature.addToGod(god);
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		nature = new Nature(true);
		nature.setName1("name1_2");
		nature.setName2("xxx");
		nature.addToGod(god);
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		nature = new Nature(true);
		nature.setName1("name1_3");
		nature.setName2("xxx");
		nature.addToGod(god);
		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		nature = new Nature(true);
		nature.setName1("name1_4");
		nature.setName2("yyy");
		nature.addToGod(god);
		db.stopTransaction(Conclusion.SUCCESS);

		//TODO this fails inside a transaction at the moment due to tinkerpop bug on hasNext() method of Neo4jEdgeIterable
//		db.startTransaction();
		God godTest = new God(god.getVertex());
		Set<Nature> natureForQualifier2 = godTest.getNatureForQualifier2("xxx");
		Assert.assertEquals(4, natureForQualifier2.size());
		natureForQualifier2 = godTest.getNatureForQualifier2("yyy");
		Assert.assertEquals(1, natureForQualifier2.size());
		Nature natureForDeletion = godTest.getNatureForQualifier1("name1_0");
//		db.stopTransaction(Conclusion.SUCCESS);

		db.startTransaction();
		natureForDeletion.delete();
		db.stopTransaction(Conclusion.SUCCESS);
		
		God godTest2 = new God(god.getVertex());
		Assert.assertNull(godTest2.getNatureForQualifier1("name1_0"));
		Assert.assertEquals(3, godTest2.getNatureForQualifier2("xxx").size());
		Assert.assertEquals(1, godTest2.getNatureForQualifier2("yyy").size());
		
	}		

}
