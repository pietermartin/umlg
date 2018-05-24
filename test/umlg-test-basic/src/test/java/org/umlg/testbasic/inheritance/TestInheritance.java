package org.umlg.testbasic.inheritance;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.inheritence.Biped;
import org.umlg.inheritence.God2;
import org.umlg.inheritence.Mamal;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.time.LocalDateTime;

public class TestInheritance extends BaseLocalDbTest {

	@Test
	public void testInheritance() {
		God2 god = new God2(true);
		god.setName("GOD");
		god.setTestDatetime(LocalDateTime.now());
		Mamal mamal = new Mamal(god);
		mamal.setName("mamal");
		Biped biped = new Biped(god);
		biped.setName("biped");
		db.commit();
		Assert.assertEquals(2, countVertices());
		Assert.assertEquals(2, countEdges());
	}
	
}
