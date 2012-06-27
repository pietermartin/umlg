package org.tuml.ocl.test;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.runtime.test.BaseLocalDbTest;
import org.tuml.testocl.OclTest1;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestOcl extends BaseLocalDbTest {

	@Test
	public void test() {
		db.startTransaction();
		OclTest1 oclTest1 = new OclTest1(true);
		oclTest1.setProperty1("property1");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(oclTest1.getProperty1(), oclTest1.getDerivedProperty1());
	}
	
}
