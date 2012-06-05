package org.nakeuml.tinker.deletiontest;

import org.junit.Assert;
import org.junit.Test;
import org.tinker.concretetest.God;
import org.tinker.inheritencetest.Biped;
import org.tinker.inheritencetest.Mamal;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class DeletionInheritenceTest extends BaseLocalDbTest {

	@Test
	public void testMarkDeletedWithInheritence() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Mamal mamal = new Mamal(god);
		mamal.setName("mamal1");
		Biped biped = new Biped(god);
		biped.setName("biped1");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(3, countEdges());
		db.startTransaction();
		biped.markDeleted();
		mamal.markDeleted();
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(1, countVertices());
		Assert.assertEquals(1, countEdges());
	}

}
