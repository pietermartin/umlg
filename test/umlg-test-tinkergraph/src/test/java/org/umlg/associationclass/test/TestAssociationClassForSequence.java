package org.umlg.associationclass.test;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.tinkergraph.*;

/**
 * Date: 2013/06/22
 * Time: 6:10 PM
 */
public class TestAssociationClassForSequence extends BaseLocalDbTest {

    @Test
    public void testAssociationClassForSequence() {

        Human human = new Human(true);
        human.setName("human");

        AssociationClassSequence associationClassSequence1 = new AssociationClassSequence(true);
        associationClassSequence1.setWeight(1);
        ProjectList projectList1 = new ProjectList(human, associationClassSequence1);
        projectList1.setName("projectList1");

        AssociationClassSequence associationClassSequence2 = new AssociationClassSequence(true);
        associationClassSequence2.setWeight(2);
        ProjectList projectList2 = new ProjectList(human, associationClassSequence2);
        projectList2.setName("projectList2");

        AssociationClassSequence associationClassSequence3 = new AssociationClassSequence(true);
        associationClassSequence3.setWeight(3);
        ProjectList projectList3 = new ProjectList(human, associationClassSequence3);
        projectList3.setName("projectList3");

        AssociationClassSequence associationClassSequence4 = new AssociationClassSequence(true);
        associationClassSequence4.setWeight(4);
        ProjectList projectList4 = new ProjectList(human, associationClassSequence4);
        projectList4.setName("projectList4");

        db.commit();

        Assert.assertEquals(4, human.getProjectlist().size());
        human = new Human(human.getVertex());
        Assert.assertEquals(4, human.getProjectlist().size());

        Assert.assertEquals("projectList1", human.getProjectlist().get(0).getName());
        Assert.assertEquals("projectList2", human.getProjectlist().get(1).getName());
        Assert.assertEquals("projectList3", human.getProjectlist().get(2).getName());
        Assert.assertEquals("projectList4", human.getProjectlist().get(3).getName());

        Assert.assertEquals(new Integer(1), human.getAssociationClassSequence().get(0).getWeight());
        Assert.assertEquals(new Integer(2), human.getAssociationClassSequence().get(1).getWeight());
        Assert.assertEquals(new Integer(3), human.getAssociationClassSequence().get(2).getWeight());
        Assert.assertEquals(new Integer(4), human.getAssociationClassSequence().get(3).getWeight());

    }

    @Test
    public void testAssociationClass1_Removal() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSequence associationClassSequence1 = new AssociationClassSequence(true);
        associationClassSequence1.setWeight(1);
        ProjectList projectList1 = new ProjectList(human, associationClassSequence1);
        projectList1.setName("project1");

        AssociationClassSequence associationClassSequence2 = new AssociationClassSequence(true);
        associationClassSequence2.setWeight(2);
        ProjectList projectList2 = new ProjectList(human, associationClassSequence2);
        projectList2.setName("projectList2");

        AssociationClassSequence associationClassSequence3 = new AssociationClassSequence(true);
        associationClassSequence3.setWeight(3);
        ProjectList projectList3 = new ProjectList(human, associationClassSequence3);
        projectList3.setName("projectList3");

        db.commit();

        human = new Human(human.getVertex());
        Assert.assertEquals(3, human.getProjectlist().size());

        projectList1 = new ProjectList(projectList1.getVertex());
        human.removeFromProjectlist(projectList1);
        projectList1.delete();

        db.commit();

        human = new Human(human.getVertex());
        Assert.assertEquals(2, human.getProjectlist().size());
        Assert.assertEquals(2, human.getAssociationClassSequence().size());
        Assert.assertEquals(new Integer(2), human.getAssociationClassSequence().get(0).getWeight());

    }

}
