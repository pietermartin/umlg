package org.tuml.testbasic.qualifier;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.qualifier.God;
import org.tuml.qualifier.Nature;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestQualifier extends BaseLocalDbTest {
	
	@Test
	public void testInheritance() {
		db.startTransaction();
		God god = new God(true);
		god.setName("God");
		Nature nature1 = new Nature(true);
		nature1.setNatureName("natureName1");
		nature1.addToGod(god);
		Nature nature2 = new Nature(true);
		nature2.setNatureName("natureName2");
		nature2.addToGod(god);
		Nature nature3 = new Nature(true);
		nature3.setNatureName("natureName3");
		nature3.addToGod(god);
		db.stopTransaction(Conclusion.SUCCESS);
		
		God testGod = new God(god.getVertex());
		Nature testNature = testGod.getNatureForNatureQualifier1("natureName2");
		Assert.assertNotNull(testNature);
		testNature = testGod.getNatureForNatureQualifier1("natureNameX");
		Assert.assertNull(testNature);
		
	}

}
