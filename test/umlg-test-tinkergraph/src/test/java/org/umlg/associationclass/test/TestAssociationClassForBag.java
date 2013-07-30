package org.umlg.associationclass.test;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.tinkergraph.*;

/**
 * Date: 2013/06/22
 * Time: 6:10 PM
 */
public class TestAssociationClassForBag extends BaseLocalDbTest {

    /**
     * this test throws an exception as every new edge needs a new AssociationClass
     */
    @Test(expected = RuntimeException.class)
    public void testAssociationClassForBag1() {

        Human human = new Human(true);
        human.setName("human");

        AssociationClassBagTest associationClassBag1 = new AssociationClassBagTest(true);
        associationClassBag1.setWeight(1);
        ProjectBagTest projectBag1 = new ProjectBagTest(human, associationClassBag1);
        projectBag1.setName("projectBag1");

        AssociationClassBagTest associationClassBag2 = new AssociationClassBagTest(true);
        associationClassBag2.setWeight(2);
        ProjectBagTest projectBag2 = new ProjectBagTest(human, associationClassBag2);
        projectBag2.setName("projectBag2");

        AssociationClassBagTest associationClassBag3 = new AssociationClassBagTest(true);
        associationClassBag3.setWeight(3);
        ProjectBagTest projectBag3 = new ProjectBagTest(human, associationClassBag3);
        projectBag3.setName("projectBag3");

        AssociationClassBagTest associationClassBag4 = new AssociationClassBagTest(true);
        associationClassBag4.setWeight(4);
        ProjectBagTest projectBag4 = new ProjectBagTest(human, associationClassBag4);
        projectBag4.setName("projectBag4");

        db.commit();

        Assert.assertEquals(4, human.getProjectbag().size());
        human = new Human(human.getVertex());
        Assert.assertEquals(4, human.getProjectbag().size());

        //This will throw an exception
        human.getProjectbag().add(projectBag1, associationClassBag1);
        db.commit();

    }

    @Test
    public void testAssociationClassForBag2() {

        Human human = new Human(true);
        human.setName("human");

        AssociationClassBagTest associationClassBag1 = new AssociationClassBagTest(true);
        associationClassBag1.setWeight(1);
        ProjectBagTest projectBag1 = new ProjectBagTest(human, associationClassBag1);
        projectBag1.setName("projectBag1");

        AssociationClassBagTest associationClassBag2 = new AssociationClassBagTest(true);
        associationClassBag2.setWeight(2);
        ProjectBagTest projectBag2 = new ProjectBagTest(human, associationClassBag2);
        projectBag2.setName("projectBag2");

        AssociationClassBagTest associationClassBag3 = new AssociationClassBagTest(true);
        associationClassBag3.setWeight(3);
        ProjectBagTest projectBag3 = new ProjectBagTest(human, associationClassBag3);
        projectBag3.setName("projectBag3");

        AssociationClassBagTest associationClassBag4 = new AssociationClassBagTest(true);
        associationClassBag4.setWeight(4);
        ProjectBagTest projectBag4 = new ProjectBagTest(human, associationClassBag4);
        projectBag4.setName("projectBag4");

        db.commit();

        Assert.assertEquals(4, human.getProjectbag().size());
        human = new Human(human.getVertex());
        Assert.assertEquals(4, human.getProjectbag().size());


        AssociationClassBagTest associationClassBag5 = new AssociationClassBagTest(true);
        associationClassBag5.setWeight(5);
        boolean added = human.getProjectbag().add(projectBag1, associationClassBag5);
        Assert.assertTrue(added);
        db.commit();

        Assert.assertEquals(5, human.getProjectbag().size());

    }

    @Test
    public void testAssociationClass1_Removal() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassBagTest associationClassBag1 = new AssociationClassBagTest(true);
        associationClassBag1.setWeight(1);
        ProjectBagTest projectBag1 = new ProjectBagTest(human, associationClassBag1);
        projectBag1.setName("projectBag1");

        AssociationClassBagTest associationClassBag2 = new AssociationClassBagTest(true);
        associationClassBag2.setWeight(2);
        ProjectBagTest projectBag2 = new ProjectBagTest(human, associationClassBag2);
        projectBag2.setName("projectBag2");

        AssociationClassBagTest associationClassBag3 = new AssociationClassBagTest(true);
        associationClassBag3.setWeight(3);
        ProjectBagTest projectBag3 = new ProjectBagTest(human, associationClassBag3);
        projectBag3.setName("projectBag3");

        db.commit();

        human = new Human(human.getVertex());
        Assert.assertEquals(3, human.getProjectbag().size());

        projectBag1 = new ProjectBagTest(projectBag1.getVertex());
        human.removeFromProjectbag(projectBag1);
        projectBag1.delete();

        db.commit();

        human = new Human(human.getVertex());
        Assert.assertEquals(2, human.getProjectbag().size());
        Assert.assertEquals(2, human.getAssociationClassBagTest().size());

    }
}
