package org.umlg.tests.primitive;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.runtime.test.BaseLocalDbTest;

public class TestPrimitiveRemoval extends BaseLocalDbTest {
	
	@Test
	public void testNameRemoval() {
		God g = new God(true);
		g.setName("G");
        db.commit();
		God gTest = new God(g.getVertex());
		Assert.assertNotNull(gTest.getName());
		g.setName(null);
        db.commit();
		gTest = new God(g.getVertex());
		Assert.assertNull(gTest.getName());
	}

    @Test(expected = RuntimeException.class)
    public void testNameAddToOneAlreadySet() {
        God g = new God(true);
        g.setName("G");
        db.commit();
        God gTest = new God(g.getVertex());
        Assert.assertNotNull(gTest.getName());
        g.addToName("asdasdasd");
    }

}
