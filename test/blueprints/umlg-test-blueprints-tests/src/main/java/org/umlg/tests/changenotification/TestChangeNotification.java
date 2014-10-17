package org.umlg.tests.changenotification;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.Angel;
import org.umlg.concretetest.Demon;
import org.umlg.concretetest.God;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.notification.NotificationListener;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2014/08/31
 * Time: 2:47 PM
 */
public class TestChangeNotification extends BaseLocalDbTest {

    @Test
    public void testChangeNotification() {
        List<String> oldValues = new ArrayList<>();
        List<String> newValues = new ArrayList<>();
        UMLG.get().registerListener(
                God.GodRuntimePropertyEnum.name,
                (commitType, a, b, old, newValue) -> {
                    if (commitType == NotificationListener.COMMIT_TYPE.AFTER_COMMIT) {

                        oldValues.add((String) old);
                        newValues.add((String) newValue);
                    }
                }
        );
        God god = new God();
        god.setName("halo");
        UMLG.get().commit();
        Assert.assertEquals(2, oldValues.size());
        Assert.assertEquals(2, newValues.size());
        Assert.assertEquals(null, oldValues.get(0));
        Assert.assertEquals("whatajol", newValues.get(0));
        Assert.assertEquals("whatajol", oldValues.get(1));
        Assert.assertEquals("halo", newValues.get(1));
    }

    @Test
    public void testChangeListenerOnAssociationEnd() {
        List<Angel> oldValues = new ArrayList<>();
        List<Angel> newValues = new ArrayList<>();
        UMLG.get().registerListener(
                God.GodRuntimePropertyEnum.angel,
                (commitType, a, b, old, newValue) -> {
                    if (commitType == NotificationListener.COMMIT_TYPE.AFTER_COMMIT) {
                        oldValues.add((Angel) old);
                        newValues.add((Angel) newValue);
                    }
                }
        );
        God god = new God();
        god.setName("god");
        Angel angel = new Angel(god);
        angel.setName("asdasd");
        UMLG.get().commit();
        Assert.assertEquals(2, oldValues.size());
        Assert.assertEquals(2, newValues.size());
        //first is for removal
        Assert.assertEquals(angel, oldValues.get(0));
        Assert.assertEquals(null, newValues.get(0));
        //second is for add
        Assert.assertEquals(null, oldValues.get(1));
        Assert.assertEquals(angel, newValues.get(1));
    }

    @Test
    public void testChangeListenerOnAssociationEndForDelete() {
        List<Angel> oldValues = new ArrayList<>();
        List<Angel> newValues = new ArrayList<>();
        UMLG.get().registerListener(
                God.GodRuntimePropertyEnum.angel,
                (commitType, a, b, old, newValue) -> {
                    if (commitType == NotificationListener.COMMIT_TYPE.AFTER_COMMIT) {
                        oldValues.add((Angel) old);
                        newValues.add((Angel) newValue);
                    }
                }
        );
        God god = new God();
        god.setName("god");
        Angel angel = new Angel(god);
        angel.setName("asdasd");
        UMLG.get().commit();
        Assert.assertEquals(2, oldValues.size());
        Assert.assertEquals(2, newValues.size());
        //first is for removal
        Assert.assertEquals(angel, oldValues.get(0));
        Assert.assertEquals(null, newValues.get(0));
        //second is for add
        Assert.assertEquals(null, oldValues.get(1));
        Assert.assertEquals(angel, newValues.get(1));
        angel.delete();
        oldValues.clear();
        newValues.clear();
        UMLG.get().commit();
        Assert.assertEquals(1, oldValues.size());
        Assert.assertEquals(1, newValues.size());
        Assert.assertEquals(angel, oldValues.get(0));
        Assert.assertEquals(null, newValues.get(0));
    }
}
