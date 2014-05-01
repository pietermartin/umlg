package org.umlg.tests.inheritencetest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.inheritencetest.Mamal;
import org.umlg.runtime.test.BaseLocalDbTest;

public class TestInheritence extends BaseLocalDbTest {

	@Test
	public void testInheritence() {
		God god = new God(true);
		god.setName("THEGOD");
		Mamal mamal1 = new Mamal(god);
		mamal1.setName("mamal1");
		Mamal mamal2 = new Mamal(god);
		mamal2.setName("mamal2");
        db.commit();
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(3 + 3, countEdges());
		God g = new God(god.getVertex());
		Assert.assertEquals(2, g.getAbstractSpecies().size());
	}

}
