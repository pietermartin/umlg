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
    public void testIndexingStringUniqueUsingStaticFinder() {
        for (int i = 0; i < 100; i++) {
            TopRoot topRoot = new TopRoot();
            topRoot.setName("asdasdasd");
            topRoot.setIndexedName(String.valueOf(i));
        }
        db.commit();
        TopRoot topRoot = TopRoot.findIndexedName("0");
        Assert.assertNotNull(topRoot);
        Assert.assertEquals("0", topRoot.getIndexedName());

        topRoot = TopRoot.findIndexedName("50");
        Assert.assertNotNull(topRoot);
        Assert.assertEquals("50", topRoot.getIndexedName());

        topRoot = TopRoot.findIndexedName("99");
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
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getQualifiedName(), "bbbb");
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getQualifiedName(), "cccc");
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getQualifiedName(), "dddd");
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getQualifiedName(), "eeee");
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getQualifiedName(), "ffff");
        Assert.assertEquals(0, topRoots.size());

    }

    @Test
    public void testIndexingStringNonUniqueFromFinder() {
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
        List<TopRoot> topRoots = TopRoot.findIndexedNonUniqueName("aaaa");
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexedNonUniqueName("bbbb");
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexedNonUniqueName("cccc");
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexedNonUniqueName("dddd");
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexedNonUniqueName("eeee");
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexedNonUniqueName("ffff");
        Assert.assertEquals(0, topRoots.size());

    }

    @Test
    public void testIndexingIntegerNonUnique() {
        for (int i = 0; i < 100; i++) {
            TopRoot topRoot = new TopRoot();
            topRoot.setName("asdasdasd");
            topRoot.setIndexedName(String.valueOf(i));
            if (i < 20) {
                topRoot.setIndexNonUniqueInteger(1);
            } else if (i < 40) {
                topRoot.setIndexNonUniqueInteger(2);
            } else if (i < 60) {
                topRoot.setIndexNonUniqueInteger(3);
            } else if (i < 80) {
                topRoot.setIndexNonUniqueInteger(4);
            } else {
                topRoot.setIndexNonUniqueInteger(5);
            }
        }
        db.commit();
        List<TopRoot> topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getQualifiedName(), 1);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getQualifiedName(), 2);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getQualifiedName(), 3);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getQualifiedName(), 4);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getQualifiedName(), 5);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getQualifiedName(), 6);
        Assert.assertEquals(0, topRoots.size());

    }

    @Test
    public void testIndexingIntegerNonUniquefromFinder() {
        for (int i = 0; i < 100; i++) {
            TopRoot topRoot = new TopRoot();
            topRoot.setName("asdasdasd");
            topRoot.setIndexedName(String.valueOf(i));
            if (i < 20) {
                topRoot.setIndexNonUniqueInteger(1);
            } else if (i < 40) {
                topRoot.setIndexNonUniqueInteger(2);
            } else if (i < 60) {
                topRoot.setIndexNonUniqueInteger(3);
            } else if (i < 80) {
                topRoot.setIndexNonUniqueInteger(4);
            } else {
                topRoot.setIndexNonUniqueInteger(5);
            }
        }
        db.commit();
        List<TopRoot> topRoots = TopRoot.findIndexNonUniqueInteger(1);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueInteger( 2);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueInteger(3);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueInteger(4);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueInteger(5);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueInteger(6);
        Assert.assertEquals(0, topRoots.size());

    }

    @Test
    public void testIndexingLongNonUnique() {
        for (int i = 0; i < 100; i++) {
            TopRoot topRoot = new TopRoot();
            topRoot.setName("asdasdasd");
            topRoot.setIndexedName(String.valueOf(i));
            if (i < 20) {
                topRoot.setIndexNonUniqueLong(1L);
            } else if (i < 40) {
                topRoot.setIndexNonUniqueLong(2L);
            } else if (i < 60) {
                topRoot.setIndexNonUniqueLong(3L);
            } else if (i < 80) {
                topRoot.setIndexNonUniqueLong(4L);
            } else {
                topRoot.setIndexNonUniqueLong(5L);
            }
        }
        db.commit();
        List<TopRoot> topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueLong.getQualifiedName(), 1L);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueLong.getQualifiedName(), 2L);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueLong.getQualifiedName(), 3L);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueLong.getQualifiedName(), 4L);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueLong.getQualifiedName(), 5L);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getQualifiedName(), 6L);
        Assert.assertEquals(0, topRoots.size());

    }

    @Test
    public void testIndexingLongNonUniqueFromIndex() {
        for (int i = 0; i < 100; i++) {
            TopRoot topRoot = new TopRoot();
            topRoot.setName("asdasdasd");
            topRoot.setIndexedName(String.valueOf(i));
            if (i < 20) {
                topRoot.setIndexNonUniqueLong(1L);
            } else if (i < 40) {
                topRoot.setIndexNonUniqueLong(2L);
            } else if (i < 60) {
                topRoot.setIndexNonUniqueLong(3L);
            } else if (i < 80) {
                topRoot.setIndexNonUniqueLong(4L);
            } else {
                topRoot.setIndexNonUniqueLong(5L);
            }
        }
        db.commit();
        List<TopRoot> topRoots = TopRoot.findIndexNonUniqueLong(1L);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueLong(2L);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueLong(3L);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueLong(4L);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueLong(5L);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueLong(6L);
        Assert.assertEquals(0, topRoots.size());

    }

    @Test
    public void testIndexingDoubleNonUnique() {
        for (int i = 0; i < 100; i++) {
            TopRoot topRoot = new TopRoot();
            topRoot.setName("asdasdasd");
            topRoot.setIndexedName(String.valueOf(i));
            if (i < 20) {
                topRoot.setIndexNonUniqueDouble(1D);
            } else if (i < 40) {
                topRoot.setIndexNonUniqueDouble(2D);
            } else if (i < 60) {
                topRoot.setIndexNonUniqueDouble(3D);
            } else if (i < 80) {
                topRoot.setIndexNonUniqueDouble(4D);
            } else {
                topRoot.setIndexNonUniqueDouble(5D);
            }
        }
        db.commit();
        List<TopRoot> topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueDouble.getQualifiedName(), 1D);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueDouble.getQualifiedName(), 2D);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueDouble.getQualifiedName(), 3D);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueDouble.getQualifiedName(), 4D);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueDouble.getQualifiedName(), 5D);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getQualifiedName(), 6L);
        Assert.assertEquals(0, topRoots.size());

    }

    @Test
    public void testIndexingDoubleNonUniqueFromFinder() {
        for (int i = 0; i < 100; i++) {
            TopRoot topRoot = new TopRoot();
            topRoot.setName("asdasdasd");
            topRoot.setIndexedName(String.valueOf(i));
            if (i < 20) {
                topRoot.setIndexNonUniqueDouble(1D);
            } else if (i < 40) {
                topRoot.setIndexNonUniqueDouble(2D);
            } else if (i < 60) {
                topRoot.setIndexNonUniqueDouble(3D);
            } else if (i < 80) {
                topRoot.setIndexNonUniqueDouble(4D);
            } else {
                topRoot.setIndexNonUniqueDouble(5D);
            }
        }
        db.commit();
        List<TopRoot> topRoots = TopRoot.findIndexNonUniqueDouble(1D);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueDouble(2D);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueDouble(3D);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueDouble(4D);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueDouble(5D);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueInteger(6);
        Assert.assertEquals(0, topRoots.size());

    }

    @Test
    public void testIndexingBooleanNonUnique() {
        for (int i = 0; i < 100; i++) {
            TopRoot topRoot = new TopRoot();
            topRoot.setName("asdasdasd");
            topRoot.setIndexedName(String.valueOf(i));
            if (i < 20) {
                topRoot.setIndexNonUniqueBoolean(true);
            } else {
                topRoot.setIndexNonUniqueBoolean(false);
            }
        }
        db.commit();
        List<TopRoot> topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueBoolean.getQualifiedName(), true);
        Assert.assertEquals(20, topRoots.size());

        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueBoolean.getQualifiedName(), false);
        Assert.assertEquals(80, topRoots.size());
    }

    @Test
    public void testIndexingBooleanNonUniqueFromIndex() {
        for (int i = 0; i < 100; i++) {
            TopRoot topRoot = new TopRoot();
            topRoot.setName("asdasdasd");
            topRoot.setIndexedName(String.valueOf(i));
            if (i < 20) {
                topRoot.setIndexNonUniqueBoolean(true);
            } else {
                topRoot.setIndexNonUniqueBoolean(false);
            }
        }
        db.commit();
        List<TopRoot> topRoots = TopRoot.findIndexNonUniqueBoolean(true);
        Assert.assertEquals(20, topRoots.size());

        topRoots = TopRoot.findIndexNonUniqueBoolean(false);
        Assert.assertEquals(80, topRoots.size());
    }

    @Test
    public void testUniqueIndex() {

        TopRoot topRoot1 = new TopRoot();
        topRoot1.setName("asd");
        topRoot1.setIndexedName("indexedName1");
        db.commit();
        ;

        Exception expected = null;
        try {
            TopRoot topRoot2 = new TopRoot();
            topRoot2.setName("asd");
            topRoot2.setIndexedName("indexedName1");
            db.commit();
        } catch (Exception e) {
            expected = e;
        }
        Assert.assertNotNull(expected);
        Assert.assertTrue(expected instanceof IllegalStateException);
        Assert.assertEquals("Unique indexed property umlgtest::org::umlg::rootallinstances::TopRoot::indexedName already has a value of 'indexedName1'", expected.getMessage());

    }

}
