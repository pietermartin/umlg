package org.tuml.tinker.deletiontest;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.inheritencetest.Biped;
import org.tuml.inheritencetest.Mamal;
import org.tuml.runtime.test.BaseLocalDbTest;

public class DeletionInheritenceTest extends BaseLocalDbTest {

	@Test
	public void testMarkDeletedWithInheritence() {
		God god = new God(true);
		god.setName("THEGOD");
		Mamal mamal = new Mamal(god);
		mamal.setName("mamal1");
		Biped biped = new Biped(god);
		biped.setName("biped1");
        db.commit();
		Assert.assertEquals(3 + 3, countVertices());
		Assert.assertEquals(3 + 3 + 3, countEdges());
		biped.delete();
		mamal.delete();
        db.commit();
		Assert.assertEquals(1 + 3, countVertices());
		Assert.assertEquals(1 + 3 + 1, countEdges());
	}

}
