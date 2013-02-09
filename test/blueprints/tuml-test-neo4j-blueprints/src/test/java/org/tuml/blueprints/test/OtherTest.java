package org.tuml.blueprints.test;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.qualifiertest.Nature;
import org.tuml.runtime.adaptor.TumlNeo4jGraph;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/02/09
 * Time: 9:04 PM
 */
public class OtherTest extends BaseLocalDbTest {

	@Test
	public void testQualifiedWithNull() {
        if (db instanceof TumlNeo4jGraph) {
		    ((TumlNeo4jGraph)db).setCheckElementsInTransaction(true);
        }
		God god = new God(true);
		god.setName("THEGOD");

		Nature nature = new Nature(true);
		nature.addToGod(god);
        db.commit();

		God godTest = new God(god.getVertex());
		god.setName("ss");
		TinkerSet<Nature> natureForQualifier1 = godTest.getNatureForQualifier2(null);
		Assert.assertTrue(!natureForQualifier1.isEmpty());
		Assert.assertNull(natureForQualifier1.iterator().next().getName1());
        db.commit();
	}


}
