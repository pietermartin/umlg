package org.umlg.testbasic;

import org.junit.Assert;

import org.junit.Test;
import org.umlg.InterfaceRealization1;
import org.umlg.InterfaceRealization2;
import org.umlg.runtime.test.BaseLocalDbTest;

public class TestInterfaceProperties extends BaseLocalDbTest {

	@Test
	public void testOneToOne() {
		InterfaceRealization1 interfaceRealization1 = new InterfaceRealization1(true);
		interfaceRealization1.setName("interfaceRealization1");
		InterfaceRealization2 interfaceRealization2 = new InterfaceRealization2(interfaceRealization1);
		interfaceRealization2.setName("interfaceRealization2");
		db.commit();
		Assert.assertEquals(1, countVertices());
		Assert.assertEquals(1, countEdges());

		InterfaceRealization2 interfaceRealization2_1 = new InterfaceRealization2(interfaceRealization1);
		interfaceRealization2_1.setName("interfaceRealization2_1");
		db.commit();
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
		
		InterfaceRealization1 test = new InterfaceRealization1(interfaceRealization1.getVertex());
		Assert.assertEquals(2, test.getInterface2().size());
	}
}
