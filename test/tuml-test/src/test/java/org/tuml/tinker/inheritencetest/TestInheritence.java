package org.tuml.tinker.inheritencetest;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.inheritencetest.Mamal;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

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
		Assert.assertEquals(3, countVertices());
		God g = new God(god.getVertex());
		Assert.assertEquals(2, g.getAbstractSpecies().size());
	}

}
