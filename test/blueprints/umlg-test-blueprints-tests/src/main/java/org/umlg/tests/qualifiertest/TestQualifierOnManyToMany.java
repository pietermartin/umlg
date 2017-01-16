package org.umlg.tests.qualifiertest;

import org.apache.tinkerpop.gremlin.process.traversal.Compare;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.qualifiertest.Many1;
import org.umlg.qualifiertest.Many2;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.util.Pair;

public class TestQualifierOnManyToMany extends BaseLocalDbTest {

	@Test
	public void testQualifierOnManyToManySet() {
		God god = new God(true);
		god.setName("THEGOD");

		Many1 many1_1 = new Many1(true);
		many1_1.setName("many1_1");
		many1_1.addToGod(god);

		Many1 many1_2 = new Many1(true);
		many1_2.setName("many1_1");
		many1_2.addToGod(god);

		Many1 many1_3 = new Many1(true);
		many1_3.setName("many1_1");
		many1_3.addToGod(god);

		Many1 many1_4 = new Many1(true);
		many1_4.setName("many1_4");
		many1_4.addToGod(god);

		Many2 many2_1 = new Many2(true);
		many2_1.setName("many2_1");
		many2_1.addToGod(god);

		Many2 many2_2 = new Many2(true);
		many2_2.setName("many2_2");
		many2_2.addToGod(god);

		Many2 many2_3 = new Many2(true);
		many2_3.setName("many2_3");
		many2_3.addToGod(god);

		Many2 many2_4 = new Many2(true);
		many2_4.setName("many2_4");
		many2_4.addToGod(god);

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

        db.commit();
		Assert.assertEquals(8, countVertices());
		Assert.assertEquals(24, countEdges());

		Many1 m = new Many1(many1_1.getVertex());
		Assert.assertNotNull(m.getMany2ForMany2Qualifier1(Pair.of(Compare.eq, "many2_1")));
		Assert.assertNotNull(m.getMany2ForMany2Qualifier1(Pair.of(Compare.eq, "many2_2")));
		Assert.assertNotNull(m.getMany2ForMany2Qualifier1(Pair.of(Compare.eq, "many2_3")));
		Assert.assertNotNull(m.getMany2ForMany2Qualifier1(Pair.of(Compare.eq, "many2_4")));

		Many2 m2 = new Many2(many2_1.getVertex());
		Assert.assertNotNull(m2.getMany1ForMany1Qualifier1(Pair.of(Compare.eq, "many1_1")));
		Assert.assertNotNull(m2.getMany1ForMany1Qualifier1(Pair.of(Compare.eq, "many1_1")));
		Assert.assertNotNull(m2.getMany1ForMany1Qualifier1(Pair.of(Compare.eq, "many1_1")));
		Assert.assertNotNull(m2.getMany1ForMany1Qualifier1(Pair.of(Compare.eq, "many1_1")));

		Assert.assertNotNull(m2.getMany1ForMany1Qualifier1(Pair.of(Compare.eq, "many1_4")));
		Assert.assertNotNull(m2.getMany1ForMany1Qualifier1(Pair.of(Compare.eq, "many1_4")));
		Assert.assertNotNull(m2.getMany1ForMany1Qualifier1(Pair.of(Compare.eq, "many1_4")));
		Assert.assertNotNull(m2.getMany1ForMany1Qualifier1(Pair.of(Compare.eq, "many1_4")));

	}

//	@Test
//	public void testQualifierOnManyToManySequence() {
//		God god = new God(true);
//		god.setName("THEGOD");
//
//		Many1 many1_1 = new Many1(true);
//		many1_1.setName("many1_1");
//		many1_1.addToGod(god);
//
//		Many1 many1_2 = new Many1(true);
//		many1_2.setName("many1_1");
//		many1_2.addToGod(god);
//
//		Many1 many1_3 = new Many1(true);
//		many1_3.setName("many1_1");
//		many1_3.addToGod(god);
//
//		Many1 many1_4 = new Many1(true);
//		many1_4.setName("many1_4");
//		many1_4.addToGod(god);
//
//		Many2 many2_1 = new Many2(true);
//		many2_1.setName("many2_1");
//		many2_1.addToGod(god);
//
//		Many2 many2_2 = new Many2(true);
//		many2_2.setName("many2_2");
//		many2_2.addToGod(god);
//
//		Many2 many2_3 = new Many2(true);
//		many2_3.setName("many2_3");
//		many2_3.addToGod(god);
//
//		Many2 many2_4 = new Many2(true);
//		many2_4.setName("many2_4");
//		many2_4.addToGod(god);
//
//		many1_1.addToMany2List(many2_1);
//		many1_1.addToMany2List(many2_2);
//		many1_1.addToMany2List(many2_3);
//		many1_1.addToMany2List(many2_4);
//
//		many1_2.addToMany2List(many2_1);
//		many1_2.addToMany2List(many2_2);
//		many1_2.addToMany2List(many2_3);
//		many1_2.addToMany2List(many2_4);
//
//		many1_3.addToMany2List(many2_1);
//		many1_3.addToMany2List(many2_2);
//		many1_3.addToMany2List(many2_3);
//		many1_3.addToMany2List(many2_4);
//
//		many1_4.addToMany2List(many2_1);
//		many1_4.addToMany2List(many2_2);
//		many1_4.addToMany2List(many2_3);
//		many1_4.addToMany2List(many2_4);
//
//        db.commit();
//        Assert.assertEquals(41, countVertices());
//        Assert.assertEquals(97, countEdges());
//
//		Many1 m = new Many1(many1_1.getVertex());
//		Assert.assertEquals(1, m.getMany2ListForListQualifier2(Pair.of(T.eq, "many2_1")).size());
//		Assert.assertEquals(1, m.getMany2ListForListQualifier2(Pair.of(T.eq, "many2_2")).size());
//		Assert.assertEquals(1, m.getMany2ListForListQualifier2(Pair.of(T.eq, "many2_3")).size());
//		Assert.assertEquals(1, m.getMany2ListForListQualifier2(Pair.of(T.eq, "many2_4")).size());
//
//		Many2 m2 = new Many2(many2_1.getVertex());
//		Assert.assertEquals(3, m2.getMany1ListForListQualifier1(Pair.of(T.eq, "many1_1")).size());
//		Assert.assertEquals(3, m2.getMany1ListForListQualifier1(Pair.of(T.eq, "many1_1")).size());
//		Assert.assertEquals(3, m2.getMany1ListForListQualifier1(Pair.of(T.eq, "many1_1")).size());
//		Assert.assertEquals(3, m2.getMany1ListForListQualifier1(Pair.of(T.eq, "many1_1")).size());
//
//		Assert.assertEquals(1, m2.getMany1ListForListQualifier1(Pair.of(T.eq, "many1_4")).size());
//	}
//
//    @Test
//    public void testIndexUpdate() {
//
//        God god = new God(true);
//        god.setName("THEGOD");
//
//        Many1 many1_1 = new Many1(true);
//        many1_1.setName("many1_1");
//        many1_1.addToGod(god);
//
//        Many1 many1_2 = new Many1(true);
//        many1_2.setName("many1_1");
//        many1_2.addToGod(god);
//
//        Many1 many1_3 = new Many1(true);
//        many1_3.setName("many1_1");
//        many1_3.addToGod(god);
//
//        Many1 many1_4 = new Many1(true);
//        many1_4.setName("many1_4");
//        many1_4.addToGod(god);
//
//        Many2 many2_1 = new Many2(true);
//        many2_1.setName("many2_1");
//        many2_1.addToGod(god);
//
//        Many2 many2_2 = new Many2(true);
//        many2_2.setName("many2_2");
//        many2_2.addToGod(god);
//
//        Many2 many2_3 = new Many2(true);
//        many2_3.setName("many2_3");
//        many2_3.addToGod(god);
//
//        Many2 many2_4 = new Many2(true);
//        many2_4.setName("many2_4");
//        many2_4.addToGod(god);
//
//        many1_1.addToMany2(many2_1);
//        many2_1.addToMany1List(many1_1);
//        many2_1.addToMany1List(many1_2);
//        many2_1.addToMany1List(many1_3);
//
//        db.commit();
//        Assert.assertEquals(3, many2_1.getMany1ListForListQualifier1(Pair.of(T.eq, "many1_1")).size());
//
//        many1_1.reload();
//        Many2 m2 = many1_1.getMany2ForMany2Qualifier1(Pair.of(T.eq, "many2_1"));
//        Assert.assertNotNull(m2);
//        Assert.assertEquals("many2_1", m2.getName());
//        m2.setName("wwww");
//        db.commit();
//        m2 = many1_1.getMany2ForMany2Qualifier1(Pair.of(T.eq, "many2_1"));
//        Assert.assertNull(m2);
//        m2 = many1_1.getMany2ForMany2Qualifier1(Pair.of(T.eq, "wwww"));
//        Assert.assertNotNull(m2);
//
//        many2_1.reload();
//        Assert.assertEquals(3, many2_1.getMany1ListForListQualifier1(Pair.of(T.eq, "many1_1")).size());
//        many1_1.setName("aaaa");
//        db.commit();
//
//        Assert.assertEquals(2, many2_1.getMany1ListForListQualifier1(Pair.of(T.eq, "many1_1")).size());
//        Assert.assertEquals(1, many2_1.getMany1ListForListQualifier1(Pair.of(T.eq, "aaaa")).size());
//
//        many1_2.setName("aaaa");
//        db.commit();
//        Assert.assertEquals(1, many2_1.getMany1ListForListQualifier1(Pair.of(T.eq, "many1_1")).size());
//        Assert.assertEquals(2, many2_1.getMany1ListForListQualifier1(Pair.of(T.eq, "aaaa")).size());
//
//        many1_3.setName("aaaa");
//        db.commit();
//        Assert.assertEquals(0, many2_1.getMany1ListForListQualifier1(Pair.of(T.eq, "many1_1")).size());
//        Assert.assertEquals(3, many2_1.getMany1ListForListQualifier1(Pair.of(T.eq, "aaaa")).size());
//
//    }
//
//    @Test
//    public void testQualifierValidateUniqueness() {
//        Exception e = null;
//        try {
//            God god = new God(true);
//            god.setName("THEGOD");
//
//            Many1 many1_1 = new Many1(true);
//            many1_1.setName("many1_1");
//            many1_1.addToGod(god);
//
//            Many1 many1_2 = new Many1(true);
//            many1_2.setName("many1_1");
//            many1_2.addToGod(god);
//
//            Many1 many1_3 = new Many1(true);
//            many1_3.setName("many1_1");
//            many1_3.addToGod(god);
//
//            Many1 many1_4 = new Many1(true);
//            many1_4.setName("many1_4");
//            many1_4.addToGod(god);
//
//            Many2 many2_1 = new Many2(true);
//            many2_1.setName("many2_1");
//            many2_1.addToGod(god);
//
//            Many2 many2_2 = new Many2(true);
//            many2_2.setName("many2_1");
//            many2_2.addToGod(god);
//
//            many1_1.addToMany2(many2_1);
//            //Should fail here
//            many1_1.addToMany2(many2_2);
//
//            db.commit();
//        } catch (Exception ex) {
//            e = ex;
//        }
//        Assert.assertNotNull(e);
//        Assert.assertTrue(e.getMessage().startsWith("Qualifier umlgtest::org::umlg::qualifiertest::Many2::name  fails, qualifier multiplicity is one and an entry for key"));
//    }

}
