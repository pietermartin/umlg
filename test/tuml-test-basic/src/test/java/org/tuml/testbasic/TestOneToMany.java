package org.tuml.testbasic;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.Many;
import org.tuml.One;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestOneToMany extends BaseLocalDbTest {

	@Test
	public void testBasicOneToMany() {
		db.startTransaction();
		One one = new One(true);
		Many many = new Many(one);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
		One testOne = new One(one.getVertex());
		Assert.assertEquals(1, testOne.getMany().size());
		Many testMany = new Many(many.getVertex());
		Assert.assertNotNull(testMany.getOne());
	}

	@Test
	public void testOneToMany() {
		db.startTransaction();
		One one1 = new One(true);
		Many many11 = new Many(one1);
		Many many12 = new Many(one1);

		One one2 = new One(true);
		Many many21 = new Many(one2);
		Many many22 = new Many(one2);

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(6, countVertices());
		Assert.assertEquals(6, countEdges());

		db.startTransaction();
		// This is to ensure the collection is loaded, to test that it gets
		// cleared again to ensure the correct one get laded on the next call
		many21.getOne();
		one1.addToMany(many21);
		db.stopTransaction(Conclusion.SUCCESS);

		Assert.assertEquals(6, countVertices());
		Assert.assertEquals(6, countEdges());

		Many testMany21 = new Many(many21.getVertex());
		Assert.assertEquals(one1, testMany21.getOne());

		Assert.assertEquals(one1, many21.getOne());
	}
	
	@Test
	public void testDelete() {
		db.startTransaction();
		One one1 = new One(true);
		Many many11 = new Many(one1);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());

		db.startTransaction();
		many11.delete();
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(1, countVertices());
		Assert.assertEquals(1, countEdges());
		
	}

}
