package org.tuml.testbasic;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.InterfaceRealization1;
import org.tuml.InterfaceRealization2;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Conclusion;

public class TestInterfaceProperties extends BaseLocalDbTest {

	@Test
	public void testOneToOne() {
		db.startTransaction();
		InterfaceRealization1 interfaceRealization1 = new InterfaceRealization1(true);
		interfaceRealization1.setName("interfaceRealization1");
		InterfaceRealization2 interfaceRealization2 = new InterfaceRealization2(interfaceRealization1);
		interfaceRealization2.setName("interfaceRealization2");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(1, countEdges());

		db.startTransaction();
		InterfaceRealization2 interfaceRealization2_1 = new InterfaceRealization2(interfaceRealization1);
		interfaceRealization2_1.setName("interfaceRealization2_1");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(2, countEdges());
		
		InterfaceRealization1 test = new InterfaceRealization1(interfaceRealization1.getVertex());
		Assert.assertEquals(2, test.getInterface2().size());
	}
}
