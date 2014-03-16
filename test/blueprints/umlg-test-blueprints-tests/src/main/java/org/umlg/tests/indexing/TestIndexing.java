package org.umlg.tests.indexing;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.rootallinstances.TopRoot;
import org.umlg.runtime.domain.PersistentObject;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.Iterator;
import java.util.List;

/**
 * Date: 2014/03/16
 * Time: 10:08 AM
 */
public class TestIndexing extends BaseLocalDbTest {

    @Test
    public void testIndexingStringUnique() {
        for (int i = 0; i < 100; i++) {
            TopRoot topRoot = new TopRoot();
            topRoot.setName("asdasdasd");
            topRoot.setIndexedName(String.valueOf(i));
        }
        db.commit();
        TopRoot topRoot = db.getFromUniqueIndex(TopRoot.TopRootRuntimePropertyEnum.indexedName.getQualifiedName(), "0");
        Assert.assertNotNull(topRoot);
        Assert.assertEquals("0", topRoot.getIndexedName());

        topRoot = db.getFromUniqueIndex(TopRoot.TopRootRuntimePropertyEnum.indexedName.getQualifiedName(), "50");
        Assert.assertNotNull(topRoot);
        Assert.assertEquals("50", topRoot.getIndexedName());

        topRoot = db.getFromUniqueIndex(TopRoot.TopRootRuntimePropertyEnum.indexedName.getQualifiedName(), "99");
        Assert.assertNotNull(topRoot);
        Assert.assertEquals("99", topRoot.getIndexedName());
    }

    @Test
    public void testIndexingStringNonUnique() {
        for (int i = 0; i < 100; i++) {
            TopRoot topRoot = new TopRoot();
            topRoot.setName("asdasdasd");
            topRoot.setIndexedName(String.valueOf(i));
            if (i < 20) {
                topRoot.setIndexedNonUniqueName("aaaa");
            } else if (i < 40) {
                topRoot.setIndexedNonUniqueName("bbbb");
            } else if (i < 60) {
                topRoot.setIndexedNonUniqueName("cccc");
            } else if (i < 80) {
                topRoot.setIndexedNonUniqueName("dddd");
            } else {
                topRoot.setIndexedNonUniqueName("eeee");
            }
        }
        db.commit();
        List<TopRoot> topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getQualifiedName(), "aaaa");
        Assert.assertEquals(20, countLazyList(topRoots));

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getQualifiedName(), "bbbb");
        Assert.assertEquals(20, countLazyList(topRoots));

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getQualifiedName(), "cccc");
        Assert.assertEquals(20, countLazyList(topRoots));

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getQualifiedName(), "dddd");
        Assert.assertEquals(20, countLazyList(topRoots));

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getQualifiedName(), "eeee");
        Assert.assertEquals(20, countLazyList(topRoots));

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getQualifiedName(), "ffff");
        Assert.assertEquals(0, countLazyList(topRoots));

    }

    private int countLazyList(List<? extends PersistentObject> list) {
        int count = 0;
        for (PersistentObject po : list) {
            count++;
        }
        return count;
    }
}
