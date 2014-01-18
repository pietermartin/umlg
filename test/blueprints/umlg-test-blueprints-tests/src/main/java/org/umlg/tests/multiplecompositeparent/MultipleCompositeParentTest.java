package org.umlg.tests.multiplecompositeparent;

import org.junit.Test;
import org.umlg.multiplecompositeparent.Child;
import org.umlg.multiplecompositeparent.Parent1;
import org.umlg.multiplecompositeparent.Parent2;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/10/18
 * Time: 11:05 PM
 */
public class MultipleCompositeParentTest extends BaseLocalDbTest {

    @Test
    public void testMultipleCompositeParent_OnlyOneSet() {
        Parent1 parent1 = new Parent1(true);
        parent1.setName("parent1");
        Parent2 parent2 = new Parent2(true);
        parent2.setName("parent2");

        Child child = new Child(parent1);
        child.setName("child");
        db.commit();
    }

    @Test(expected = IllegalStateException.class)
    public void testMultipleCompositeParent_TwoSet() {
        Parent1 parent1 = new Parent1(true);
        parent1.setName("parent1");
        Parent2 parent2 = new Parent2(true);
        parent2.setName("parent2");

        Child child = new Child(parent1);
        child.setName("child");
        child.setParent2(parent2);
        db.commit();
    }

}
