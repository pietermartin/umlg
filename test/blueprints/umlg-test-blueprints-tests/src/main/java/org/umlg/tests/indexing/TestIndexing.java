package org.umlg.tests.indexing;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.rootallinstances.TopRoot;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.List;

/**
 * Date: 2014/03/16
 * Time: 10:08 AM
 */
public class TestIndexing extends BaseLocalDbTest {

    @Test
    public void testUpdateIndexedFieldToPrevious() {
        TopRoot topRoot = new TopRoot();
        topRoot.setName("asdasdasd");
        topRoot.setIndexedName("a");
        db.commit();
        topRoot = db.getFromUniqueIndex(TopRoot.TopRootRuntimePropertyEnum.indexedName.getPersistentName(), "a");
        Assert.assertNotNull(topRoot);
        Assert.assertEquals("a", topRoot.getIndexedName());

        topRoot.setIndexedName("a");
        db.commit();

    }

//    @Test
//    public void testIndexingStringUnique() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//        }
//        db.commit();
//        TopRoot topRoot = db.getFromUniqueIndex(TopRoot.TopRootRuntimePropertyEnum.indexedName.getPersistentName(), "0");
//        Assert.assertNotNull(topRoot);
//        Assert.assertEquals("0", topRoot.getIndexedName());
//
//        topRoot = db.getFromUniqueIndex(TopRoot.TopRootRuntimePropertyEnum.indexedName.getPersistentName(), "50");
//        Assert.assertNotNull(topRoot);
//        Assert.assertEquals("50", topRoot.getIndexedName());
//
//        topRoot = db.getFromUniqueIndex(TopRoot.TopRootRuntimePropertyEnum.indexedName.getPersistentName(), "99");
//        Assert.assertNotNull(topRoot);
//        Assert.assertEquals("99", topRoot.getIndexedName());
//    }
//
//    @Test
//    public void testIndexingStringUniqueUsingStaticFinder() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//        }
//        db.commit();
//        TopRoot topRoot = TopRoot.findByIndexedName("0");
//        Assert.assertNotNull(topRoot);
//        Assert.assertEquals("0", topRoot.getIndexedName());
//
//        topRoot = TopRoot.findByIndexedName("50");
//        Assert.assertNotNull(topRoot);
//        Assert.assertEquals("50", topRoot.getIndexedName());
//
//        topRoot = TopRoot.findByIndexedName("99");
//        Assert.assertNotNull(topRoot);
//        Assert.assertEquals("99", topRoot.getIndexedName());
//    }
//
//
//    @Test
//    public void testIndexingStringNonUnique() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//            if (i < 20) {
//                topRoot.setIndexedNonUniqueName("aaaa");
//            } else if (i < 40) {
//                topRoot.setIndexedNonUniqueName("bbbb");
//            } else if (i < 60) {
//                topRoot.setIndexedNonUniqueName("cccc");
//            } else if (i < 80) {
//                topRoot.setIndexedNonUniqueName("dddd");
//            } else {
//                topRoot.setIndexedNonUniqueName("eeee");
//            }
//        }
//        db.commit();
//        List<TopRoot> topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getPersistentName(), "aaaa");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getPersistentName(), "bbbb");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getPersistentName(), "cccc");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getPersistentName(), "dddd");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getPersistentName(), "eeee");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexedNonUniqueName.getPersistentName(), "ffff");
//        Assert.assertEquals(0, topRoots.size());
//
//    }
//
//    @Test
//    public void testIndexingStringNonUniqueFromFinder() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//            if (i < 20) {
//                topRoot.setIndexedNonUniqueName("aaaa");
//            } else if (i < 40) {
//                topRoot.setIndexedNonUniqueName("bbbb");
//            } else if (i < 60) {
//                topRoot.setIndexedNonUniqueName("cccc");
//            } else if (i < 80) {
//                topRoot.setIndexedNonUniqueName("dddd");
//            } else {
//                topRoot.setIndexedNonUniqueName("eeee");
//            }
//        }
//        db.commit();
//        List<TopRoot> topRoots = TopRoot.findByIndexedNonUniqueName("aaaa");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexedNonUniqueName("bbbb");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexedNonUniqueName("cccc");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexedNonUniqueName("dddd");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexedNonUniqueName("eeee");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexedNonUniqueName("ffff");
//        Assert.assertEquals(0, topRoots.size());
//
//    }
//
//    @Test
//    public void testIndexingStringNonUniqueFromFinderIndexedZero() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//            if (i < 20) {
//                topRoot.setIndexedNonUniqueName("aaaa");
//            } else if (i < 40) {
//                topRoot.setIndexedNonUniqueName("bbbb");
//            } else if (i < 60) {
//                topRoot.setIndexedNonUniqueName("cccc");
//            } else if (i < 80) {
//                topRoot.setIndexedNonUniqueName("dddd");
//            } else {
//                topRoot.setIndexedNonUniqueName("eeee");
//            }
//        }
//        db.commit();
//
//        Assert.assertEquals(0, TopRoot.findByIndexedNonUniqueName("asdasdasd").size());
//        Assert.assertEquals("aaaa", TopRoot.findByIndexedNonUniqueName("aaaa").get(0).getIndexedNonUniqueName());
//        Assert.assertEquals("bbbb", TopRoot.findByIndexedNonUniqueName("bbbb").get(0).getIndexedNonUniqueName());
//        Assert.assertEquals("cccc", TopRoot.findByIndexedNonUniqueName("cccc").get(0).getIndexedNonUniqueName());
//        Assert.assertEquals("dddd", TopRoot.findByIndexedNonUniqueName("dddd").get(0).getIndexedNonUniqueName());
//        Assert.assertEquals("eeee", TopRoot.findByIndexedNonUniqueName("eeee").get(0).getIndexedNonUniqueName());
//
//        Assert.assertEquals("aaaa", TopRoot.findByIndexedNonUniqueName("aaaa").get(10).getIndexedNonUniqueName());
//        Assert.assertEquals("bbbb", TopRoot.findByIndexedNonUniqueName("bbbb").get(10).getIndexedNonUniqueName());
//        Assert.assertEquals("cccc", TopRoot.findByIndexedNonUniqueName("cccc").get(10).getIndexedNonUniqueName());
//        Assert.assertEquals("dddd", TopRoot.findByIndexedNonUniqueName("dddd").get(10).getIndexedNonUniqueName());
//        Assert.assertEquals("eeee", TopRoot.findByIndexedNonUniqueName("eeee").get(10).getIndexedNonUniqueName());
//
//        Assert.assertEquals("aaaa", TopRoot.findByIndexedNonUniqueName("aaaa").get(19).getIndexedNonUniqueName());
//        Assert.assertEquals("bbbb", TopRoot.findByIndexedNonUniqueName("bbbb").get(19).getIndexedNonUniqueName());
//        Assert.assertEquals("cccc", TopRoot.findByIndexedNonUniqueName("cccc").get(19).getIndexedNonUniqueName());
//        Assert.assertEquals("dddd", TopRoot.findByIndexedNonUniqueName("dddd").get(19).getIndexedNonUniqueName());
//        Assert.assertEquals("eeee", TopRoot.findByIndexedNonUniqueName("eeee").get(19).getIndexedNonUniqueName());
//
//        Assert.assertEquals(20, TopRoot.findByIndexedNonUniqueName("aaaa").size());
//        Assert.assertEquals(20, TopRoot.findByIndexedNonUniqueName("bbbb").size());
//        Assert.assertEquals(20, TopRoot.findByIndexedNonUniqueName("cccc").size());
//        Assert.assertEquals(20, TopRoot.findByIndexedNonUniqueName("dddd").size());
//        Assert.assertEquals(20, TopRoot.findByIndexedNonUniqueName("eeee").size());
//
//        List<TopRoot> topRoots = TopRoot.findByIndexedNonUniqueName("aaaa");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexedNonUniqueName("bbbb");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexedNonUniqueName("cccc");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexedNonUniqueName("dddd");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexedNonUniqueName("eeee");
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexedNonUniqueName("ffff");
//        Assert.assertEquals(0, topRoots.size());
//
//    }
//
//    @Test
//    public void testIndexingIntegerNonUnique() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//            if (i < 20) {
//                topRoot.setIndexNonUniqueInteger(1);
//            } else if (i < 40) {
//                topRoot.setIndexNonUniqueInteger(2);
//            } else if (i < 60) {
//                topRoot.setIndexNonUniqueInteger(3);
//            } else if (i < 80) {
//                topRoot.setIndexNonUniqueInteger(4);
//            } else {
//                topRoot.setIndexNonUniqueInteger(5);
//            }
//        }
//        db.commit();
//        List<TopRoot> topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getPersistentName(), 1);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getPersistentName(), 2);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getPersistentName(), 3);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getPersistentName(), 4);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getPersistentName(), 5);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getPersistentName(), 6);
//        Assert.assertEquals(0, topRoots.size());
//
//    }
//
//    @Test
//    public void testIndexingIntegerNonUniquefromFinder() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//            if (i < 20) {
//                topRoot.setIndexNonUniqueInteger(1);
//            } else if (i < 40) {
//                topRoot.setIndexNonUniqueInteger(2);
//            } else if (i < 60) {
//                topRoot.setIndexNonUniqueInteger(3);
//            } else if (i < 80) {
//                topRoot.setIndexNonUniqueInteger(4);
//            } else {
//                topRoot.setIndexNonUniqueInteger(5);
//            }
//        }
//        db.commit();
//        List<TopRoot> topRoots = TopRoot.findByIndexNonUniqueInteger(1);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueInteger( 2);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueInteger(3);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueInteger(4);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueInteger(5);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueInteger(6);
//        Assert.assertEquals(0, topRoots.size());
//
//    }
//
//    @Test
//    public void testIndexingUnlimitedNaturalNonUnique() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//            if (i < 20) {
//                topRoot.setIndexNonUniqueUnlimitedNatural(1);
//            } else if (i < 40) {
//                topRoot.setIndexNonUniqueUnlimitedNatural(2);
//            } else if (i < 60) {
//                topRoot.setIndexNonUniqueUnlimitedNatural(3);
//            } else if (i < 80) {
//                topRoot.setIndexNonUniqueUnlimitedNatural(4);
//            } else {
//                topRoot.setIndexNonUniqueUnlimitedNatural(5);
//            }
//        }
//        db.commit();
//        List<TopRoot> topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueUnlimitedNatural.getPersistentName(), 1);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueUnlimitedNatural.getPersistentName(), 2);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueUnlimitedNatural.getPersistentName(), 3);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueUnlimitedNatural.getPersistentName(), 4);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueUnlimitedNatural.getPersistentName(), 5);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getPersistentName(), 6L);
//        Assert.assertEquals(0, topRoots.size());
//
//    }
//
//    @Test
//    public void testIndexingUnlimitedNaturalNonUniqueFromIndex() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//            if (i < 20) {
//                topRoot.setIndexNonUniqueUnlimitedNatural(1);
//            } else if (i < 40) {
//                topRoot.setIndexNonUniqueUnlimitedNatural(2);
//            } else if (i < 60) {
//                topRoot.setIndexNonUniqueUnlimitedNatural(3);
//            } else if (i < 80) {
//                topRoot.setIndexNonUniqueUnlimitedNatural(4);
//            } else {
//                topRoot.setIndexNonUniqueUnlimitedNatural(5);
//            }
//        }
//        db.commit();
//        List<TopRoot> topRoots = TopRoot.findByIndexNonUniqueUnlimitedNatural(1);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueUnlimitedNatural(2);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueUnlimitedNatural(3);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueUnlimitedNatural(4);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueUnlimitedNatural(5);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueUnlimitedNatural(6);
//        Assert.assertEquals(0, topRoots.size());
//
//    }
//
//    @Test
//    public void testIndexingRealNonUnique() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//            if (i < 20) {
//                topRoot.setIndexNonUniqueReal(1D);
//            } else if (i < 40) {
//                topRoot.setIndexNonUniqueReal(2D);
//            } else if (i < 60) {
//                topRoot.setIndexNonUniqueReal(3D);
//            } else if (i < 80) {
//                topRoot.setIndexNonUniqueReal(4D);
//            } else {
//                topRoot.setIndexNonUniqueReal(5D);
//            }
//        }
//        db.commit();
//        List<TopRoot> topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueReal.getPersistentName(), 1D);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueReal.getPersistentName(), 2D);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueReal.getPersistentName(), 3D);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueReal.getPersistentName(), 4D);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueReal.getPersistentName(), 5D);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueInteger.getPersistentName(), 6L);
//        Assert.assertEquals(0, topRoots.size());
//
//    }
//
//    @Test
//    public void testIndexingDoubleNonUniqueFromFinder() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//            if (i < 20) {
//                topRoot.setIndexNonUniqueReal(1D);
//            } else if (i < 40) {
//                topRoot.setIndexNonUniqueReal(2D);
//            } else if (i < 60) {
//                topRoot.setIndexNonUniqueReal(3D);
//            } else if (i < 80) {
//                topRoot.setIndexNonUniqueReal(4D);
//            } else {
//                topRoot.setIndexNonUniqueReal(5D);
//            }
//        }
//        db.commit();
//        List<TopRoot> topRoots = TopRoot.findByIndexNonUniqueReal(1D);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueReal(2D);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueReal(3D);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueReal(4D);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueReal(5D);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueInteger(6);
//        Assert.assertEquals(0, topRoots.size());
//
//    }
//
//    @Test
//    public void testIndexingBooleanNonUnique() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//            if (i < 20) {
//                topRoot.setIndexNonUniqueBoolean(true);
//            } else {
//                topRoot.setIndexNonUniqueBoolean(false);
//            }
//        }
//        db.commit();
//        List<TopRoot> topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueBoolean.getPersistentName(), true);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = db.getFromIndex(TopRoot.TopRootRuntimePropertyEnum.indexNonUniqueBoolean.getPersistentName(), false);
//        Assert.assertEquals(80, topRoots.size());
//    }
//
//    @Test
//    public void testIndexingBooleanNonUniqueFromIndex() {
//        for (int i = 0; i < 100; i++) {
//            TopRoot topRoot = new TopRoot();
//            topRoot.setName("asdasdasd");
//            topRoot.setIndexedName(String.valueOf(i));
//            if (i < 20) {
//                topRoot.setIndexNonUniqueBoolean(true);
//            } else {
//                topRoot.setIndexNonUniqueBoolean(false);
//            }
//        }
//        db.commit();
//        List<TopRoot> topRoots = TopRoot.findByIndexNonUniqueBoolean(true);
//        Assert.assertEquals(20, topRoots.size());
//
//        topRoots = TopRoot.findByIndexNonUniqueBoolean(false);
//        Assert.assertEquals(80, topRoots.size());
//    }
//
//    @Test
//    public void testUniqueIndex() {
//
//        TopRoot topRoot1 = new TopRoot();
//        topRoot1.setName("asd");
//        topRoot1.setIndexedName("indexedName1");
//        db.commit();
//        ;
//
//        Exception expected = null;
//        try {
//            TopRoot topRoot2 = new TopRoot();
//            topRoot2.setName("asd");
//            topRoot2.setIndexedName("indexedName1");
//            db.commit();
//        } catch (Exception e) {
//            expected = e;
//        }
//        Assert.assertNotNull(expected);
//        Assert.assertTrue(expected instanceof IllegalStateException);
//        Assert.assertEquals("Unique indexed property umlgtest::org::umlg::rootallinstances::TopRoot::indexedName already has a value of 'indexedName1'", expected.getMessage());
//
//    }

}
