package org.tuml.testbasic.inheritance;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;
import org.tuml.inheritence.Biped;
import org.tuml.inheritence.God2;
import org.tuml.inheritence.Mamal;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

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
		Assert.assertEquals(3, countEdges());
	}
	
}
