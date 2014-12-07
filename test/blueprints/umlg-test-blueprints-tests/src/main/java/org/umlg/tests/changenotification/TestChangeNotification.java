package org.umlg.tests.changenotification;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.A;
import org.umlg.collectiontest.B;
import org.umlg.concretetest.Angel;
import org.umlg.concretetest.God;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.notification.NotificationListener;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.util.ObjectMapperFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2014/08/31
 * Time: 2:47 PM
 */
public class TestChangeNotification extends BaseLocalDbTest {

    @Test
    public void testChangeNotification() {
        List<String> addValues = new ArrayList<>();
        List<String> removeValues = new ArrayList<>();
        List<String> deleteValues = new ArrayList<>();
        UMLG.get().registerListener(
                God.GodRuntimePropertyEnum.name,
                (commitType, umlGnode, propertyType, changeType, value) -> {
                    if (commitType == NotificationListener.COMMIT_TYPE.AFTER_COMMIT) {
                        switch (changeType) {
                            case ADD:
                                addValues.add((String) value);
                                break;
                            case REMOVE:
                                removeValues.add((String) value);
                                break;
                        }
                    }
                }
        );
        God god = new God();
        god.setName("halo");
        UMLG.get().commit();
        Assert.assertEquals(2, addValues.size());
        Assert.assertEquals(1, removeValues.size());
        Assert.assertEquals(0, deleteValues.size());
        Assert.assertEquals("whatajol", addValues.get(0));
        Assert.assertEquals("halo", addValues.get(1));
        Assert.assertEquals("whatajol", removeValues.get(0));
    }

//    @Test
//    public void testChangeListenerOnAssociationEnd() {
//        List<Angel> addValues = new ArrayList<>();
//        List<Angel> removeValues = new ArrayList<>();
//        List<String> deleteValues = new ArrayList<>();
//        UMLG.get().registerListener(
//                God.GodRuntimePropertyEnum.angel,
//                (commitType, umlgNode, propertyType, changeType, value) -> {
//                    if (commitType == NotificationListener.COMMIT_TYPE.AFTER_COMMIT) {
//                        switch (changeType) {
//                            case ADD:
//                                addValues.add((Angel) value);
//                                break;
//                            case REMOVE:
//                                removeValues.add((Angel) value);
//                                break;
//                        }
//                    }
//                }
//        );
//        God god = new God();
//        god.setName("god");
//        Angel angel = new Angel(god);
//        angel.setName("asdasd");
//        UMLG.get().commit();
//        Assert.assertEquals(1, addValues.size());
//        Assert.assertEquals(0, removeValues.size());
//        Assert.assertEquals(0, deleteValues.size());
//        Assert.assertEquals(angel, addValues.get(0));
//    }
//
//    @Test
//    public void testChangeListenerOnAssociationEndForDelete() throws IOException {
//        List<Angel> addValues = new ArrayList<>();
//        List<Angel> removeValues = new ArrayList<>();
//        List<String> deleteValues = new ArrayList<>();
//        UMLG.get().registerListener(
//                God.GodRuntimePropertyEnum.angel,
//                (commitType, unkgNode, propertyEnum, changeType, value) -> {
//                    if (commitType == NotificationListener.COMMIT_TYPE.AFTER_COMMIT) {
//                        switch (changeType) {
//                            case ADD:
//                                addValues.add((Angel) value);
//                                break;
//                            case REMOVE:
//                                removeValues.add((Angel) value);
//                                break;
//                            case DELETE:
//                                deleteValues.add((String) value);
//                                break;
//                        }
//                    }
//                }
//        );
//        God god = new God();
//        god.setName("god");
//        Angel angel = new Angel(god);
//        angel.setName("asdasd");
//        UMLG.get().commit();
//        Assert.assertEquals(1, addValues.size());
//        Assert.assertEquals(0, removeValues.size());
//        Assert.assertEquals(0, deleteValues.size());
//        Long idToTest = angel.getId();
//        angel.delete();
//        addValues.clear();
//        removeValues.clear();
//        UMLG.get().commit();
//
//        Assert.assertEquals(0, addValues.size());
//        Assert.assertEquals(0, removeValues.size());
//        Assert.assertEquals(1, deleteValues.size());
//        Assert.assertEquals(String.valueOf(idToTest), ObjectMapperFactory.INSTANCE.getObjectMapper().readValue(deleteValues.get(0), Map.class).get("id"));
//    }
//
//    @Test
//    public void testManyToManyChange() {
//        List<B> addValues = new ArrayList<>();
//        List<B> removeValues = new ArrayList<>();
//        List<String> deleteValues = new ArrayList<>();
//        UMLG.get().registerListener(
//                A.ARuntimePropertyEnum.b,
//                (commitType, umlgNode, propertyEnum, changeType, value) -> {
//                    if (commitType == NotificationListener.COMMIT_TYPE.AFTER_COMMIT) {
//                        switch (changeType) {
//                            case ADD:
//                                addValues.add((B) value);
//                                break;
//                            case REMOVE:
//                                removeValues.add((B) value);
//                                break;
//                            case DELETE:
//                                deleteValues.add((String) value);
//                                break;
//                        }
//                    }
//                }
//        );
//
//        A a1 = new A();
//        A a2 = new A();
//        A a3 = new A();
//        B b1 = new B();
//        B b2 = new B();
//        B b3 = new B();
//        a1.addToB(b1);
//        a1.addToB(b2);
//        a2.addToB(b1);
//        a2.addToB(b2);
//        a3.addToB(b1);
//        a3.addToB(b2);
//        UMLG.get().commit();
//
//        addValues.clear();
//        removeValues.clear();
//        deleteValues.clear();
//
//        //add some
//        a1.addToB(b3);
//        a2.addToB(b3);
//        UMLG.get().commit();
//        Assert.assertEquals(2, addValues.size());
//
//        //remove some
//        addValues.clear();
//        removeValues.clear();
//        deleteValues.clear();
//        a1.removeFromB(b3);
//        a2.removeFromB(b3);
//        UMLG.get().commit();
//        Assert.assertEquals(0, addValues.size());
//        Assert.assertEquals(2, removeValues.size());
//
//        addValues.clear();
//        removeValues.clear();
//        deleteValues.clear();
//        b1.delete();
//        UMLG.get().commit();
//        Assert.assertEquals(0, addValues.size());
//        Assert.assertEquals(0, removeValues.size());
//        //this is 3 as it is called for each a that has an association to b1
//        Assert.assertEquals(3, deleteValues.size());
//    }
}
