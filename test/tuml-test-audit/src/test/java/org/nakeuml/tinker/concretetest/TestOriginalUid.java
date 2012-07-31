package org.nakeuml.tinker.concretetest;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.inheritencetest.Mamal;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestOriginalUid extends BaseLocalDbTest {

	@Test
	public void testOriginalUid() {
		db.startTransaction();
		God god = new God(true);
		god.setName("GODDER");
		Mamal mamal = new Mamal(god);
		mamal.setName("mamal1");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(god.getUid(), god.getAudits().get(0).getOriginalUid());
		Assert.assertEquals(mamal.getUid(), mamal.getAudits().get(0).getOriginalUid());
	}
	
}
