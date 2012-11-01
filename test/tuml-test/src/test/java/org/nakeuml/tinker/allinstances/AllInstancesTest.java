package org.nakeuml.tinker.allinstances;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.hierarchytest.Folder;
import org.tuml.hierarchytest.RealRootFolder;
import org.tuml.inheritencetest.AbstractSpecies;
import org.tuml.inheritencetest.Biped;
import org.tuml.inheritencetest.Mamal;
import org.tuml.inheritencetest.Quadped;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class AllInstancesTest extends BaseLocalDbTest {

	@SuppressWarnings("unused")
	@Test
	public void testAllInstances1() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		Mamal mamal1 = new Mamal(god);
		Mamal mamal2 = new Mamal(god);
		Mamal mamal3 = new Mamal(god);
		Mamal mamal4 = new Mamal(god);
		Mamal mamal5 = new Mamal(god);

		Biped biped1 = new Biped(god);
		Biped biped2 = new Biped(god);
		Biped biped3 = new Biped(god);
		Biped biped4 = new Biped(god);
		Biped biped5 = new Biped(god);

		Quadped quadPed1 = new Quadped(god);
		Quadped quadPed2 = new Quadped(god);
		Quadped quadPed3 = new Quadped(god);
		Quadped quadPed4 = new Quadped(god);
		Quadped quadPed5 = new Quadped(god);

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(15, AbstractSpecies.allInstances().size());
		Assert.assertEquals(15, Mamal.allInstances().size());
		Assert.assertEquals(5, Biped.allInstances().size());
		Assert.assertEquals(5, Quadped.allInstances().size());
	}
	
	@Test
	public void testHierarciesAllInstances() {
		db.startTransaction();
		God god = new God(true);
		god.setName("THEGOD");
		RealRootFolder realRootFolder = new RealRootFolder(god);
		realRootFolder.setName("realRootFolder");
		Folder folder1 = new Folder(realRootFolder);
		folder1.setName("folder1");
		Folder folder1_1 = new Folder(folder1);
		folder1_1.setName("folder1_1");

		Folder folder2 = new Folder(realRootFolder);
		folder2.setName("folder2");
		Folder folder2_1 = new Folder(folder2);
		folder2_1.setName("folder2_1");
		Folder folder2_2 = new Folder(folder2);
		folder2_2.setName("folder2_2");

		Folder folder2_2_1 = new Folder(folder2_1);
		folder2_2_1.setName("folder2_2_1");
		Folder folder2_2_2 = new Folder(folder2_1);
		folder2_2_2.setName("folder2_2_2");

		
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(1, RealRootFolder.allInstances().size());
		Assert.assertEquals(7, Folder.allInstances().size());
	}
	
}