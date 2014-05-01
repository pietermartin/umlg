package org.umlg.tests.multiplecompositeparenthierarchy;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.multiplecompositeparent.FolderX;
import org.umlg.multiplecompositeparent.Root1;
import org.umlg.multiplecompositeparent.Root2;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/10/18
 * Time: 11:42 PM
 */
public class HierarchyMultipleParentsTest extends BaseLocalDbTest {

    @Test
    public void testHierarchyMultipleParents() {
        Root1 rootA = new Root1(true);
        Root2 rootB = new Root2(true);

        FolderX folderA = new FolderX(rootA);
        folderA.setName("folderA");
        FolderX folderAA = new FolderX(true);
        folderAA.setName("folderAA");
        folderA.addToSubFolder(folderAA);

        FolderX folderB = new FolderX(rootB);
        folderB.setName("folderB");
        FolderX folderBB = new FolderX(true);
        folderBB.setName("folderBB");
        folderB.addToSubFolder(folderBB);

        db.commit();

        rootA.reload();
        Assert.assertEquals("folderAA", rootA.getFolder().get(0).getSubFolder().get(0).getName());
        Assert.assertEquals("folderBB", rootB.getFolder().get(0).getSubFolder().get(0).getName());

        folderBB.reload();
        folderAA.reload();
        Assert.assertNull(folderAA.getFolder().getRoot2());
        Assert.assertNotNull(folderAA.getFolder().getRoot1());
        Assert.assertNull(folderBB.getFolder().getRoot1());
        Assert.assertNotNull(folderBB.getFolder().getRoot2());


    }
}
