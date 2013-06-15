package org.umlg.testbasic.allinstances;

import junit.framework.Assert;

import org.junit.Test;
import org.umlg.InterfaceRealization1;
import org.umlg.InterfaceRealization2;
import org.umlg.runtime.test.BaseLocalDbTest;

public class TestAllInstances extends BaseLocalDbTest {

	@Test
	public void testAllInstancesWithInterfaceAsCompositionalOwner() {
		InterfaceRealization1 interfaceRealization1_1 = new InterfaceRealization1(true);
		interfaceRealization1_1.setName("interfaceRealization1_1");
		InterfaceRealization1 interfaceRealization1_2 = new InterfaceRealization1(true);
		interfaceRealization1_2.setName("interfaceRealization1_2");
		InterfaceRealization1 interfaceRealization1_3 = new InterfaceRealization1(true);
		interfaceRealization1_3.setName("interfaceRealization1_3");
		InterfaceRealization1 interfaceRealization1_4 = new InterfaceRealization1(true);
		interfaceRealization1_4.setName("interfaceRealization1_4");

		InterfaceRealization2 interfaceRealization2_1 = new InterfaceRealization2(interfaceRealization1_1);
		interfaceRealization2_1.setName("interfaceRealization2_1");
		InterfaceRealization2 interfaceRealization2_2 = new InterfaceRealization2(interfaceRealization1_2);
		interfaceRealization2_2.setName("interfaceRealization2_2");
		InterfaceRealization2 interfaceRealization2_3 = new InterfaceRealization2(interfaceRealization1_3);
		interfaceRealization2_3.setName("interfaceRealization2_3");
		InterfaceRealization2 interfaceRealization2_4 = new InterfaceRealization2(interfaceRealization1_4);
		interfaceRealization2_4.setName("interfaceRealization2_4");

        db.commit();
		Assert.assertEquals(4, InterfaceRealization1.allInstances().size());
		Assert.assertEquals(4, InterfaceRealization2.allInstances().size());
	}
	
}
