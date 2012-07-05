package org.nakeuml.tinker.hierarchytest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.hierarchytest.Folder;
import org.tuml.hierarchytest.Hierarchy;
import org.tuml.hierarchytest.RealRootFolder;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestHierarchy extends BaseLocalDbTest {

//	@Test
//	public void testHierarchy() {
//		db.startTransaction();
//		God god = new God(true);
//		god.setName("THEGOD");
//		RealRootFolder realRootFolder = new RealRootFolder(god);
//		realRootFolder.setName("realRootFolder");
//		Folder folder1 = new Folder(realRootFolder);
//		folder1.setName("folder1");
//		db.stopTransaction(Conclusion.SUCCESS);
//		assertEquals(3, countVertices());
//		assertEquals(3, countEdges());
//		db.startTransaction();
//		Folder folder2 = new Folder(realRootFolder);
//		folder2.setName("folder2");
//		db.stopTransaction(Conclusion.SUCCESS);
//		assertEquals(4, countVertices());
//		assertEquals(4, countEdges());
//		db.startTransaction();
//		Folder folder11 = new Folder(folder1);
//		folder11.setName("folder11");
//		db.stopTransaction(Conclusion.SUCCESS);
//		assertEquals(5, countVertices());
//		assertEquals(5, countEdges());
//		assertTrue(folder11.getParent().getParent() instanceof RealRootFolder);
//		db.startTransaction();
//		Folder folder111 = new Folder(folder11);
//		folder111.setName("folder111");
//		Folder folder1111 = new Folder(folder111);
//		folder1111.setName("folder1111");
//		db.stopTransaction(Conclusion.SUCCESS);
//		Hierarchy hierarchy = folder1111;
//		int countLevels = 0;
//		while (!hierarchy.isRoot()) {
//			countLevels++;
//			hierarchy = hierarchy.getParent();
//		}
//		assertEquals(7, countVertices());
//		assertEquals(7, countEdges());
//		assertEquals(4, countLevels);
//		assertEquals("THEGOD", ((RealRootFolder)hierarchy).getGod().getName());
//	}
	
}
