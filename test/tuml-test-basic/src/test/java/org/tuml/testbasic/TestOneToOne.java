package org.tuml.testbasic;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.OneOne;
import org.tuml.OneTwo;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestOneToOne extends BaseLocalDbTest {

	@Test(expected=IllegalStateException.class)
	public void testOneToOne() {
		db.startTransaction();
		OneOne oneOne1 = new OneOne(true);
		OneTwo oneTwo1 = new OneTwo(true);
		OneOne oneOne2 = new OneOne(true);
		OneTwo oneTwo2 = new OneTwo(true);
		OneOne oneOne3 = new OneOne(true);
		OneTwo oneTwo3 = new OneTwo(true);
		OneOne oneOne4 = new OneOne(true);
		OneTwo oneTwo4 = new OneTwo(true);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(8, countVertices());
		
		db.startTransaction();
		oneOne1.addToOneTwo(oneTwo1);
		oneOne2.addToOneTwo(oneTwo2);
		oneOne3.addToOneTwo(oneTwo3);
		oneOne4.addToOneTwo(oneTwo4);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(8, countVertices());
		//There is an edge to the root node for every non composite vertex
		Assert.assertEquals(12, countEdges());
		
		db.startTransaction();
		oneOne1.addToOneTwo(oneTwo2);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(8, countVertices());
		Assert.assertEquals(12, countEdges());
	}
	
	@Test
	public void testOneToOneCheckInverseSides() {
		db.startTransaction();
		OneOne oneOne1 = new OneOne(true);
		OneTwo oneTwo1 = new OneTwo(true);
		OneOne oneOne2 = new OneOne(true);
		OneTwo oneTwo2 = new OneTwo(true);
		OneOne oneOne3 = new OneOne(true);
		OneTwo oneTwo3 = new OneTwo(true);
		OneOne oneOne4 = new OneOne(true);
		OneTwo oneTwo4 = new OneTwo(true);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(8, countVertices());
		
		db.startTransaction();
		oneOne1.addToOneTwo(oneTwo1);
		oneOne2.addToOneTwo(oneTwo2);
		oneOne3.addToOneTwo(oneTwo3);
		oneOne4.addToOneTwo(oneTwo4);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(8, countVertices());
		Assert.assertEquals(12, countEdges());
		
		db.startTransaction();
		oneOne1.setOneTwo(oneTwo2);
		Assert.assertEquals(oneOne1.getOneTwo(), oneTwo2);
		Assert.assertEquals(oneTwo2.getOneOne(), oneOne1);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(8, countVertices());
		Assert.assertEquals(11, countEdges());
		
		OneOne oneOne1Test = new OneOne(oneOne1.getVertex());
		Assert.assertEquals(oneTwo2, oneOne1Test.getOneTwo());
		
		OneTwo oneTwo2Test = new OneTwo(oneTwo2.getVertex());
		Assert.assertEquals(oneOne1, oneTwo2Test.getOneOne());
		
		OneOne oneOne2Test = new OneOne(oneOne2.getVertex());
		Assert.assertNull(oneOne2Test.getOneTwo());
		
		OneTwo oneTwo1Test = new OneTwo(oneTwo1.getVertex());
		Assert.assertNull(oneTwo1Test.getOneOne());
	}	


}
