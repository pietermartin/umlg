package org.tuml.ocl.test;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.runtime.test.BaseLocalDbTest;
import org.tuml.testocl.OclTest1;
import org.tuml.testocl.OclTestCollection;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestOcl extends BaseLocalDbTest {

	@Test
	public void test() {
		db.startTransaction();
		OclTest1 oclTest1 = new OclTest1(true);
		oclTest1.setProperty1("property1");
		OclTestCollection oclTestCollection1 = new OclTestCollection(oclTest1);
		oclTestCollection1.setName("oclTestCollection1");
		OclTestCollection oclTestCollection2 = new OclTestCollection(oclTest1);
		oclTestCollection2.setName("oclTestCollection2");
		OclTestCollection oclTestCollectionJohn = new OclTestCollection(oclTest1);
		oclTestCollectionJohn.setName("john");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(oclTest1.getProperty1(), oclTest1.getDerivedProperty1());
		Assert.assertEquals(1, oclTest1.getOclTestCollectionSelect().size());
	}
	
}
