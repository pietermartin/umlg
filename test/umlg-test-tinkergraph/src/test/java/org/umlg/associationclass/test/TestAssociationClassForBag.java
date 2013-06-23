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

        AssociationClassBag associationClassBag1 = new AssociationClassBag(true);
        associationClassBag1.setWeight(1);
        ProjectBag projectBag1 = new ProjectBag(human, associationClassBag1);
        projectBag1.setName("projectBag1");

        AssociationClassBag associationClassBag2 = new AssociationClassBag(true);
        associationClassBag2.setWeight(2);
        ProjectBag projectBag2 = new ProjectBag(human, associationClassBag2);
        projectBag2.setName("projectBag2");

        AssociationClassBag associationClassBag3 = new AssociationClassBag(true);
        associationClassBag3.setWeight(3);
        ProjectBag projectBag3 = new ProjectBag(human, associationClassBag3);
        projectBag3.setName("projectBag3");

        AssociationClassBag associationClassBag4 = new AssociationClassBag(true);
        associationClassBag4.setWeight(4);
        ProjectBag projectBag4 = new ProjectBag(human, associationClassBag4);
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

        AssociationClassBag associationClassBag1 = new AssociationClassBag(true);
        associationClassBag1.setWeight(1);
        ProjectBag projectBag1 = new ProjectBag(human, associationClassBag1);
        projectBag1.setName("projectBag1");

        AssociationClassBag associationClassBag2 = new AssociationClassBag(true);
        associationClassBag2.setWeight(2);
        ProjectBag projectBag2 = new ProjectBag(human, associationClassBag2);
        projectBag2.setName("projectBag2");

        AssociationClassBag associationClassBag3 = new AssociationClassBag(true);
        associationClassBag3.setWeight(3);
        ProjectBag projectBag3 = new ProjectBag(human, associationClassBag3);
        projectBag3.setName("projectBag3");

        AssociationClassBag associationClassBag4 = new AssociationClassBag(true);
        associationClassBag4.setWeight(4);
        ProjectBag projectBag4 = new ProjectBag(human, associationClassBag4);
        projectBag4.setName("projectBag4");

        db.commit();

        Assert.assertEquals(4, human.getProjectbag().size());
        human = new Human(human.getVertex());
        Assert.assertEquals(4, human.getProjectbag().size());


        AssociationClassBag associationClassBag5 = new AssociationClassBag(true);
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
        AssociationClassBag associationClassBag1 = new AssociationClassBag(true);
        associationClassBag1.setWeight(1);
        ProjectBag projectBag1 = new ProjectBag(human, associationClassBag1);
        projectBag1.setName("projectBag1");

        AssociationClassBag associationClassBag2 = new AssociationClassBag(true);
        associationClassBag2.setWeight(2);
        ProjectBag projectBag2 = new ProjectBag(human, associationClassBag2);
        projectBag2.setName("projectBag2");

        AssociationClassBag associationClassBag3 = new AssociationClassBag(true);
        associationClassBag3.setWeight(3);
        ProjectBag projectBag3 = new ProjectBag(human, associationClassBag3);
        projectBag3.setName("projectBag3");

        db.commit();

        human = new Human(human.getVertex());
        Assert.assertEquals(3, human.getProjectbag().size());

        projectBag1 = new ProjectBag(projectBag1.getVertex());
        human.removeFromProjectbag(projectBag1);
        projectBag1.delete();

        db.commit();

        human = new Human(human.getVertex());
        Assert.assertEquals(2, human.getProjectbag().size());
        Assert.assertEquals(2, human.getAssociationClassBag().size());

    }
}
