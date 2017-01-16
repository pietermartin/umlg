package org.umlg.tests.deletiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.inheritencetest.Biped;
import org.umlg.inheritencetest.Mamal;
import org.umlg.runtime.test.BaseLocalDbTest;

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
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
		biped.delete();
		mamal.delete();
        db.commit();
		Assert.assertEquals(0, countVertices());
		Assert.assertEquals(0, countEdges());
	}

}
