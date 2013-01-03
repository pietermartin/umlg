package org.tuml.tinker.qualifiertest;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.qualifiertest.Nature;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestQualifierChangeEvent extends BaseLocalDbTest {

	@Test
	public void testQualifierNeedsChangeEvent() {
		God god = new God(true);
		god.setName("THEGOD");
		
		Nature nature = new Nature(true);
		nature.setName1("nature1");
		nature.setName2("nature2");
		nature.addToGod(god);
		
		Nature nature2 = new Nature(true);
		nature2.setName1("nature2");
		nature2.setName2("nature2");
		nature2.addToGod(god);
        db.commit();
		
		God g = new God(god.getVertex());
		g.getNatureForQualifier2("nature2").iterator().next().setName1("nameSoGonaFail");
        db.commit();
		
		God gg = new God(god.getVertex());
		Assert.assertTrue(!gg.getNatureForQualifier2("nameSoGonaFail").isEmpty());
		
	}

}
