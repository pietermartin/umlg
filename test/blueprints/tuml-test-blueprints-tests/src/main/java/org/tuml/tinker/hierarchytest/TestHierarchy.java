package org.tuml.tinker.hierarchytest;

import junit.framework.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.hierarchy.Hierarchy;
import org.tuml.hierarchytest.Folder;
import org.tuml.hierarchytest.RealRootFolder;
import org.tuml.runtime.test.BaseLocalDbTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestHierarchy extends BaseLocalDbTest {

	@Test
	public void testHierarchy() {
		God god = new God(true);
		god.setName("THEGOD");
		RealRootFolder realRootFolder = new RealRootFolder(god);
		realRootFolder.setName("realRootFolder");
		Folder folder1 = new Folder(realRootFolder);
		folder1.setName("folder1");
        db.commit();
		assertEquals(3 + 3, countVertices());
		assertEquals(3 + 3 + 3, countEdges());
		Folder folder2 = new Folder(realRootFolder);
		folder2.setName("folder2");
        db.commit();
		assertEquals(4 + 3, countVertices());
		assertEquals(4 + 3 + 4, countEdges());
		Folder folder11 = new Folder(folder1);
		folder11.setName("folder11");
        db.commit();
		assertEquals(5 + 3, countVertices());
		assertEquals(5 + 3 + 5, countEdges());
		assertTrue(folder11.getParent().getParent() instanceof RealRootFolder);
		Folder folder111 = new Folder(folder11);
		folder111.setName("folder111");
		Folder folder1111 = new Folder(folder111);
		folder1111.setName("folder1111");
        db.commit();
		Hierarchy hierarchy = folder1111;
		int countLevels = 0;
		while (!hierarchy.isRoot()) {
			countLevels++;
			hierarchy = hierarchy.getParent();
		}
		assertEquals(7 + 3, countVertices());
		assertEquals(7 + 3 + 7, countEdges());
		assertEquals(4, countLevels);
		assertEquals("THEGOD", ((RealRootFolder)hierarchy).getGod().getName());
	}

	@Test
	public void testGetAllChildren() {
		God god = new God(true);
		god.setName("THEGOD");
		RealRootFolder realRootFolder = new RealRootFolder(god);
		realRootFolder.setName("realRootFolder");
		Folder folder1 = new Folder(realRootFolder);
		folder1.setName("folder1");
		Folder folder2 = new Folder(realRootFolder);
		folder2.setName("folder2");

		Folder folder1_1 = new Folder(folder1);
		folder1_1.setName("folder1_1");
		Folder folder1_2 = new Folder(folder1);
		folder1_2.setName("folder1_2");

		Folder folder1_1_1 = new Folder(folder1_1);
		folder1_1_1.setName("folder1_1_1");
		Folder folder1_2_1 = new Folder(folder1_1);
		folder1_2_1.setName("folder1_2_1");
		
		Folder folder2_1 = new Folder(folder2);
		folder2_1.setName("folder2_1");
		Folder folder2_2 = new Folder(folder2);
		folder2_2.setName("folder2_2");

		Folder folder2_1_1 = new Folder(folder2_1);
		folder2_1_1.setName("folder2_1_1");
		Folder folder2_2_1 = new Folder(folder2_1);
		folder2_2_1.setName("folder2_2_1");

        db.commit();
		
		Assert.assertEquals(10, realRootFolder.getAllChildren().size());
	}
}
