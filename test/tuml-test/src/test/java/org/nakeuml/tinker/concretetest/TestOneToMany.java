package org.nakeuml.tinker.concretetest;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.concretetest.Angel;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestOneToMany extends BaseLocalDbTest {

	@Test
	public void testCollectionOtherEndClearsAndReloads() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(true);
		universe1.setName("universe1");
		god.addToUniverse(universe1);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertNotNull(universe1.getGod());
	}

	@Test
	public void testCompositeCreation() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		Angel angel = new Angel(god);
		angel.setName("angel1");
		universe1.setAngel(angel);
		db.stopTransaction(Conclusion.SUCCESS);
		assertEquals(6, countVertices());
		assertEquals(7, countEdges());
		db.startTransaction();
		Universe uni = new Universe(universe1.getVertex());
		uni.setName("ddddddd");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertNotNull(uni.getGod());
		Assert.assertEquals(1, god.getUniverse().size());
		Assert.assertEquals(1, god.getAngel().size());
		Assert.assertNotNull(angel.getUniverse());
		Assert.assertNotNull(universe1.getAngel());
		Angel angel1 = new Angel(angel.getVertex());
		Assert.assertNotNull(angel1.getUniverse());
	}
	
	@Test
	public void testCompositeRemoval() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		Universe universe2 = new Universe(god);
		universe2.setName("universe2");
		Universe universe3 = new Universe(god);
		universe3.setName("universe3");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(13, countVertices());
		Assert.assertEquals(13, countEdges());
		db.startTransaction();
		god.removeFromUniverse(universe1);
		God god2 = new God(true);
		god2.setName("god2");
		universe1.setGod(god2);
//		universe1.init(god2);
//		universe1.addToOwningObject();
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(14, countVertices());
		Assert.assertEquals(14, countEdges());
		Assert.assertEquals("god2", universe1.getGod().getName());
		Assert.assertEquals(2, new God(god.getVertex()).getUniverse().size());
	}
	
	@Test
	public void testClearClearsInternalCollection() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		Universe universe2 = new Universe(god);
		universe2.setName("universe2");
		Universe universe3 = new Universe(god);
		universe3.setName("universe3");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, god.getUniverse().size());
		db.startTransaction();
		Universe u = new Universe(GraphDb.getDb().getVertex(universe1.getVertex().getId()));
		god.addToUniverse(u);
		db.stopTransaction(Conclusion.SUCCESS);
		God g = new God(god.getVertex());
		Assert.assertEquals(3, g.getUniverse().size());
	}
	
	@Test
	public void testClearClearsInternalCollection2() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		Universe universe2 = new Universe(god);
		universe2.setName("universe2");
		Universe universe3 = new Universe(god);
		universe3.setName("universe3");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, god.getUniverse().size());
		db.startTransaction();
		Universe u = new Universe(GraphDb.getDb().getVertex(universe1.getVertex().getId()));
		god.getUniverse().add(u);
		db.stopTransaction(Conclusion.SUCCESS);
		God g = new God(god.getVertex());
		Assert.assertEquals(3, g.getUniverse().size());
	}
	
	@Test
	public void testClearClearsInternalCollection3() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Universe universe1 = new Universe(god);
		universe1.setName("universe1");
		Universe universe2 = new Universe(god);
		universe2.setName("universe2");
		Universe universe3 = new Universe(god);
		universe3.setName("universe3");
		
		God god2 = new God(true);
		god2.setName("THEGOD2");
		Universe universe1_2 = new Universe(god2);
		universe1_2.setName("universe1_2");
		
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, god.getUniverse().size());
		db.startTransaction();
		Universe u = new Universe(GraphDb.getDb().getVertex(universe1.getVertex().getId()));
		u.clearGod();
		god2.getUniverse().add(u);
		db.stopTransaction(Conclusion.SUCCESS);
		God g = new God(god.getVertex());
		Assert.assertEquals(2, g.getUniverse().size());
	}
	
}
