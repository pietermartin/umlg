package org.tuml.testbasic.inheritance;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.inheritence.Biped;
import org.tuml.inheritence.God;
import org.tuml.inheritence.Mamal;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestInheritance extends BaseLocalDbTest {

	@Test
	public void testInheritance() {
		db.startTransaction();
		God god = new God(true);
		god.setName("GOD");
		Mamal mamal = new Mamal(god);
		mamal.setName("mamal");
		Biped biped = new Biped(god);
		biped.setName("biped");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(3, countEdges());
	}
	
}
