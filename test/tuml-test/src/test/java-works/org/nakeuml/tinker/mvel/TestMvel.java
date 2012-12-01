package org.nakeuml.tinker.mvel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mvel2.MVEL;
import org.tuml.collectiontest.Hand;
import org.tuml.componenttest.Space;
import org.tuml.componenttest.SpaceTime;
import org.tuml.componenttest.Time;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.embeddedtest.REASON;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestMvel extends BaseLocalDbTest {

	@SuppressWarnings("unused")
	@Test
	public void testMvel() {
		db.startTransaction();
		God g = new God(true);
		g.setBeginning(new DateTime());
		g.setReason(REASON.BAD);
		g.setName("god");
		Universe u1 = new Universe(g);
		u1.setName("u1");
		SpaceTime st1 = new SpaceTime(u1);
		Space s1 = new Space(st1);
		Time t1 = new Time(st1);

		Universe u2 = new Universe(g);
		u2.setName("u2");
		SpaceTime st2 = new SpaceTime(u2);
		Space s2 = new Space(st2);
		Time t2 = new Time(st2);
		
		Universe u3 = new Universe(g);
		u3.setName("u3");
		SpaceTime st3 = new SpaceTime(u3);
		Space s3 = new Space(st3);
		Time t3 = new Time(st3);
		
		Hand h1 = new Hand(g);
		h1.setName("h1");
		Hand h2 = new Hand(g);
		h2.setName("h2");
		Hand h3 = new Hand(g);
		h3.setName("h3");
		db.stopTransaction(Conclusion.SUCCESS);
		Serializable compiled = MVEL.compileExpression("name");
		String name = (String) MVEL.executeExpression(compiled, g);
		Assert.assertEquals("god", name);

		compiled = MVEL.compileExpression("universe");
		Set<Universe> universes = (Set<Universe>) MVEL.executeExpression(compiled, g);
		Assert.assertEquals(3, universes.size());

		// Map<String, Set<Universe>> map = new HashMap<String,
		// Set<Universe>>();
		// map.put("universe", g.getUniverse());
		compiled = MVEL.compileExpression("(name in universe)");
		List<String> universeNames = (List<String>) MVEL.executeExpression(compiled, g);
		Assert.assertEquals(3, universeNames.size());
		
		Map map = new HashMap();
		map.put("nameSize", null);
		compiled = MVEL.compileExpression("nameSize = (name in universe).size()");
		MVEL.executeExpression(compiled, g, map);
		Assert.assertEquals(Integer.valueOf(3), map.get("nameSize"));

	}

}
