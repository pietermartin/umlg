package org.umlg.blueprints.test;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.qualifiertest.Nature;
import org.umlg.runtime.adaptor.UmlgNeo4jGraph;
import org.umlg.runtime.collection.TinkerSet;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/02/09
 * Time: 9:04 PM
 */
public class OtherTest extends BaseLocalDbTest {

	@Test
	public void testQualifiedWithNull() {
        if (db instanceof UmlgNeo4jGraph) {
		    ((UmlgNeo4jGraph)db).setCheckElementsInTransaction(true);
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
