package org.umlg.tests.collectiontest;

import com.tinkerpop.gremlin.structure.Compare;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.Foot;
import org.umlg.concretetest.God;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.util.Pair;
public class QualifiedSequenceTest extends BaseLocalDbTest {

	@Test
	public void testQualifiedSequence() {
		God god = new God(true);
		god.setName("THEGOD");
		Foot foot1 = new Foot(true);
		foot1.setName("foot1");
		foot1.addToGod(god);
		Foot foot2 = new Foot(true);
		foot2.setName("foot2");
		foot2.addToGod(god);
        db.commit();
		God godTest = new God(god.getVertex());
		Assert.assertEquals("foot1", godTest.getFootForGodFootQualifier(Pair.of(Compare.eq, "foot1")).getName());
		Assert.assertEquals("foot2", godTest.getFootForGodFootQualifier(Pair.of(Compare.eq, "foot2")).getName());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testQualifiedSequence2() {
		God god = new God(true);
		god.setName("THEGOD");
		Foot foot1 = new Foot(true);
		foot1.setName("foot1");
		foot1.addToGod(god);
		Foot foot2 = new Foot(true);
		foot2.setName("foot1");
		foot2.addToGod(god);
        db.commit();
	}
	
}