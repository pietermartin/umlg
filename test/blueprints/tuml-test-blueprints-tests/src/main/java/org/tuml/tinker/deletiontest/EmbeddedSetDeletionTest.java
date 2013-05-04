package org.tuml.tinker.deletiontest;

import org.junit.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.inheritencetest.Mamal;
import org.tuml.runtime.test.BaseLocalDbTest;

public class EmbeddedSetDeletionTest extends BaseLocalDbTest {

	@Test
	public void testDeleteEmbeddedStringSet() {
		God god = new God(true);
		god.setName("THEGOD");
		god.addToEmbeddedString("s1");
		god.addToEmbeddedString("s2");
		god.addToEmbeddedString("s3");
		god.addToEmbeddedString("s4");
        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(5 + 1, countEdges());
		God godTest = new God(god.getVertex());
		godTest.removeFromEmbeddedString("s1");
		godTest.removeFromEmbeddedString("s1");
        db.commit();
		Assert.assertEquals(4, countVertices());
		Assert.assertEquals(4 + 1, countEdges());

		godTest = new God(god.getVertex());
		godTest.removeFromEmbeddedString("s4");
        db.commit();
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(3 + 1, countEdges());

	}

	@Test
	public void testDeleteEmbeddedIntegerSet() {
		God god = new God(true);
		god.setName("THEGOD");
		god.addToEmbeddedInteger(1);
		god.addToEmbeddedInteger(2);
		god.addToEmbeddedInteger(3);
		god.addToEmbeddedInteger(4);
        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(5 + 1, countEdges());
		God godTest = new God(god.getVertex());
		godTest.removeFromEmbeddedInteger(1);
		godTest.removeFromEmbeddedInteger(1);
        db.commit();
		Assert.assertEquals(4, countVertices());
		Assert.assertEquals(4 + 1, countEdges());

		godTest = new God(god.getVertex());
		godTest.removeFromEmbeddedInteger(4);
        db.commit();
		Assert.assertEquals(3, countVertices());
		Assert.assertEquals(3 + 1, countEdges());
	}

	@Test
	public void testDeleteEmbeddedEntitySet() {
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
        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(9 + 5, countEdges());
		God godTest = new God(god.getVertex());
		for (Mamal animal : godTest.getAnimalFarm()) {
			System.out.println(animal.getVertex().toString());
		}
		godTest.removeFromAnimalFarm(mamal3);
        db.commit();
		Assert.assertEquals(5, countVertices());
		Assert.assertEquals(8 + 5, countEdges());
	}

}
