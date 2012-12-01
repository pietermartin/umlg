package org.nakeuml.tinker.primitive;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestBooleanPrimitive extends BaseLocalDbTest {
	
	@Test
	public void testBooleanDefaultsToFalse() {
		db.startTransaction();
		God g = new God(true);
		g.setName("G");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertFalse(g.getTestBoolean());
	}

}
