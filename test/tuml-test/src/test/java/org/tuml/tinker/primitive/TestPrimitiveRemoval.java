package org.tuml.tinker.primitive;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestPrimitiveRemoval extends BaseLocalDbTest {
	
	@Test
	public void testNameRemoval() {
		db.startTransaction();
		God g = new God(true);
		g.setName("G");
		db.stopTransaction(Conclusion.SUCCESS);
		God gTest = new God(g.getVertex());
		Assert.assertNotNull(gTest.getName());
		db.startTransaction();
		g.setName(null);
		db.stopTransaction(Conclusion.SUCCESS);
		gTest = new God(g.getVertex());
		Assert.assertNull(gTest.getName());
	}

}
