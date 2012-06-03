package org.tuml.testbasic;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.OneOne;
import org.tuml.OneTwo;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class TestOneToOne extends BaseLocalDbTest {

	@Test
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
		Assert.assertEquals(4, countEdges());
	}


}
