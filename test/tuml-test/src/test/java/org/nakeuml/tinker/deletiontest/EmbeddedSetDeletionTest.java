package org.nakeuml.tinker.deletiontest;

import org.junit.Assert;
import org.junit.Test;
import org.tinker.concretetest.God;
import org.tinker.inheritencetest.Mamal;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class EmbeddedSetDeletionTest extends BaseLocalDbTest {

	@Test
	public void testDeleteEmbeddedStringSet() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		god.addToEmbeddedString("s1");
		god.addToEmbeddedString("s2");
		god.addToEmbeddedString("s3");
		god.addToEmbeddedString("s4");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(5, countEdges());
		db.startTransaction();
		God godTest = new God(god.getVertex());
		godTest.removeFromEmbeddedString("s1");
		godTest.removeFromEmbeddedString("s1");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(4, countVertices());
		Assert.assertEquals(4, countEdges());

		db.startTransaction();
		godTest = new God(god.getVertex());
		godTest.removeFromEmbeddedString("s4");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(3, countEdges());

	}

	@Test
	public void testDeleteEmbeddedIntegerSet() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		god.addToEmbeddedInteger(1);
		god.addToEmbeddedInteger(2);
		god.addToEmbeddedInteger(3);
		god.addToEmbeddedInteger(4);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(5, countEdges());
		db.startTransaction();
		God godTest = new God(god.getVertex());
		godTest.removeFromEmbeddedInteger(1);
		godTest.removeFromEmbeddedInteger(1);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(4, countVertices());
		Assert.assertEquals(4, countEdges());

		db.startTransaction();
		godTest = new God(god.getVertex());
		godTest.removeFromEmbeddedInteger(4);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(3, countEdges());
	}
	
	@Test
	public void testDeleteEmbeddedEntitySet() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Mamal mamal1 = new Mamal(god);
		mamal1.setName("mamal1");
		Mamal mamal2 = new Mamal(god);
		mamal2.setName("mamal2");
		Mamal mamal3 = new Mamal(god);
		mamal3.setName("mamal3");
		Mamal mamal4 = new Mamal(god);
		mamal4.setName("mamal4");
		//animalFarm is an embedded many
		god.addToAnimalFarm(mamal1);
		god.addToAnimalFarm(mamal2);
		god.addToAnimalFarm(mamal3);
		god.addToAnimalFarm(mamal4);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(9, countEdges());
		db.startTransaction();
		God godTest = new God(god.getVertex());
		for (Mamal animal : godTest.getAnimalFarm()) {
			System.out.println(animal.getVertex().toString());
		}
		godTest.removeFromAnimalFarm(mamal3);
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(8, countEdges());
	}	

}
