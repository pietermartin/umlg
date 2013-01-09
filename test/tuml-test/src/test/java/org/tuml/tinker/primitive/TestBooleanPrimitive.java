package org.tuml.tinker.primitive;

import junit.framework.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.runtime.test.BaseLocalDbTest;

public class TestBooleanPrimitive extends BaseLocalDbTest {
	
	@Test
	public void testBooleanDefaultsToFalse() {
		God g = new God(true);
		g.setName("G");
        db.commit();
		Assert.assertFalse(g.getTestBoolean());
	}

}
