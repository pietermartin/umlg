package org.nakeuml.tinker.qualifiertest;

import org.junit.Assert;
import org.junit.Test;
import org.tinker.concretetest.God;
import org.tinker.qualifiertest.Nature;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestQualifierChangeEvent extends BaseLocalDbTest {

	@Test
	public void testQualifierNeedsChangeEvent() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		
		Nature nature = new Nature(true);
		nature.setName1("nature1");
		nature.setName2("nature2");
		nature.init(god);
		nature.addToOwningObject();
		
		Nature nature2 = new Nature(true);
		nature2.setName1("nature2");
		nature2.setName2("nature2");
		nature2.init(god);
		nature2.addToOwningObject();
		db.stopTransaction(Conclusion.SUCCESS);
		
		db.startTransaction();
		God g = new God(god.getVertex());
		g.getNatureForQualifier1("nature1").setName1("nameSoGonaFail");
		db.stopTransaction(Conclusion.SUCCESS);
		
		God gg = new God(god.getVertex());
		Assert.assertNotNull(gg.getNatureForQualifier1("nameSoGonaFail"));
		
	}

}
