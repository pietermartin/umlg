package org.tuml.testbasic.sequence;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.runtime.test.BaseLocalDbTest;
import org.tuml.sequence.SequenceRoot;
import org.tuml.sequence.SequenceTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestSequence extends BaseLocalDbTest {

	@Test
	public void testInheritance() {
		db.startTransaction();
		SequenceRoot sequenceRoot = new SequenceRoot(true);
		sequenceRoot.setName("sequenceRoot");
		SequenceTest sequenceTest1 = new SequenceTest(sequenceRoot);
		sequenceTest1.setName("sequenceTest1");
		SequenceTest sequenceTest2 = new SequenceTest(sequenceRoot);
		sequenceTest2.setName("sequenceTest2");
		SequenceTest sequenceTest3 = new SequenceTest(sequenceRoot);
		sequenceTest3.setName("sequenceTest3");
		db.stopTransaction(Conclusion.SUCCESS);
		SequenceRoot sequenceRootTest = new SequenceRoot(sequenceRoot.getVertex());
		Assert.assertEquals("sequenceTest3", sequenceRootTest.getSequenceTest().get(2).getName());
		SequenceTest sequenceTestTest = new SequenceTest(sequenceTest2.getVertex());
		Assert.assertEquals("sequenceRoot", sequenceTestTest.getSequenceRoot().getName());
		
		
		
	}

}
