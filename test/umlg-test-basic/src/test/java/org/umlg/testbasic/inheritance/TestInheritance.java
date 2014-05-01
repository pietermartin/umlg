package org.umlg.testbasic.inheritance;

import org.junit.Assert;

import org.joda.time.DateTime;
import org.junit.Test;
import org.umlg.inheritence.Biped;
import org.umlg.inheritence.God2;
import org.umlg.inheritence.Mamal;
import org.umlg.runtime.test.BaseLocalDbTest;

public class TestInheritance extends BaseLocalDbTest {

	@Test
	public void testInheritance() {
		God2 god = new God2(true);
		god.setName("GOD");
		god.setTestDatetime(new DateTime());
		Mamal mamal = new Mamal(god);
		mamal.setName("mamal");
		Biped biped = new Biped(god);
		biped.setName("biped");
		db.commit();
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(3 + 3, countEdges());
	}
	
}
