package org.tuml.testbasic.qualifier;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.inheritence.Biped;
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
		Nature nature1 = new Nature(god);
		nature1.setNatureName("natureName1");
		Nature nature2 = new Nature(god);
		nature2.setNatureName("natureName2");
		Nature nature3 = new Nature(god);
		nature3.setNatureName("natureName3");
		db.stopTransaction(Conclusion.SUCCESS);
		
		God testGod = new God(god.getVertex());
		Nature testNature = testGod.getNatureForNatureQualifier1("natureName2");
		Assert.assertNotNull(testNature);
		
	}

}
