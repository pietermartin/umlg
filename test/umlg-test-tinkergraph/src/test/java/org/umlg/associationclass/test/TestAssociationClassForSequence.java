package org.umlg.associationclass.test;

import org.junit.Assert;
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

        AssociationClassSequenceTest associationClassSequence1 = new AssociationClassSequenceTest(true);
        associationClassSequence1.setWeight(1);
        ProjectListTest projectList1 = new ProjectListTest(human, associationClassSequence1);
        projectList1.setName("projectList1");

        AssociationClassSequenceTest associationClassSequence2 = new AssociationClassSequenceTest(true);
        associationClassSequence2.setWeight(2);
        ProjectListTest projectList2 = new ProjectListTest(human, associationClassSequence2);
        projectList2.setName("projectList2");

        AssociationClassSequenceTest associationClassSequence3 = new AssociationClassSequenceTest(true);
        associationClassSequence3.setWeight(3);
        ProjectListTest projectList3 = new ProjectListTest(human, associationClassSequence3);
        projectList3.setName("projectList3");

        AssociationClassSequenceTest associationClassSequence4 = new AssociationClassSequenceTest(true);
        associationClassSequence4.setWeight(4);
        ProjectListTest projectList4 = new ProjectListTest(human, associationClassSequence4);
        projectList4.setName("projectList4");

        db.commit();

        Assert.assertEquals(4, human.getProjectlist().size());
        human = new Human(human.getVertex());
        Assert.assertEquals(4, human.getProjectlist().size());

        Assert.assertEquals("projectList1", human.getProjectlist().get(0).getName());
        Assert.assertEquals("projectList2", human.getProjectlist().get(1).getName());
        Assert.assertEquals("projectList3", human.getProjectlist().get(2).getName());
        Assert.assertEquals("projectList4", human.getProjectlist().get(3).getName());

        Assert.assertEquals(new Integer(1), human.getAssociationClassSequenceTest().get(0).getWeight());
        Assert.assertEquals(new Integer(2), human.getAssociationClassSequenceTest().get(1).getWeight());
        Assert.assertEquals(new Integer(3), human.getAssociationClassSequenceTest().get(2).getWeight());
        Assert.assertEquals(new Integer(4), human.getAssociationClassSequenceTest().get(3).getWeight());

    }

    @Test
    public void testAssociationClass1_Removal() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSequenceTest associationClassSequence1 = new AssociationClassSequenceTest(true);
        associationClassSequence1.setWeight(1);
        ProjectListTest projectList1 = new ProjectListTest(human, associationClassSequence1);
        projectList1.setName("project1");

        AssociationClassSequenceTest associationClassSequence2 = new AssociationClassSequenceTest(true);
        associationClassSequence2.setWeight(2);
        ProjectListTest projectList2 = new ProjectListTest(human, associationClassSequence2);
        projectList2.setName("projectList2");

        AssociationClassSequenceTest associationClassSequence3 = new AssociationClassSequenceTest(true);
        associationClassSequence3.setWeight(3);
        ProjectListTest projectList3 = new ProjectListTest(human, associationClassSequence3);
        projectList3.setName("projectList3");

        db.commit();

        human = new Human(human.getVertex());
        Assert.assertEquals(3, human.getProjectlist().size());

        projectList1 = new ProjectListTest(projectList1.getVertex());
        human.removeFromProjectlist(projectList1);
        projectList1.delete();

        db.commit();

        human = new Human(human.getVertex());
        Assert.assertEquals(2, human.getProjectlist().size());
        Assert.assertEquals(2, human.getAssociationClassSequenceTest().size());
        Assert.assertEquals(new Integer(2), human.getAssociationClassSequenceTest().get(0).getWeight());

    }

    @Test
    public void testAdderWithIndex() {

        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSequenceTest associationClassSequence1 = new AssociationClassSequenceTest(true);
        associationClassSequence1.setWeight(1);
        ProjectListTest projectList1 = new ProjectListTest(true);
        projectList1.setName("project1");

        AssociationClassSequenceTest associationClassSequence2 = new AssociationClassSequenceTest(true);
        associationClassSequence2.setWeight(2);
        ProjectListTest projectList2 = new ProjectListTest(true);
        projectList2.setName("projectList2");

        AssociationClassSequenceTest associationClassSequence3 = new AssociationClassSequenceTest(true);
        associationClassSequence3.setWeight(3);
        ProjectListTest projectList3 = new ProjectListTest(true);
        projectList3.setName("projectList3");

        human.addToProjectlist(0, projectList1, associationClassSequence1);
        human.addToProjectlist(1, projectList2, associationClassSequence2);
        human.addToProjectlist(2, projectList3, associationClassSequence3);

        db.commit();

        human.reload();
        Assert.assertEquals(projectList1, human.getProjectlist().get(0));
        Assert.assertEquals(projectList2, human.getProjectlist().get(1));
        Assert.assertEquals(projectList3, human.getProjectlist().get(2));

        human.reload();
        Assert.assertEquals(associationClassSequence1, human.getAssociationClassSequenceTest().get(0));
        Assert.assertEquals(associationClassSequence2, human.getAssociationClassSequenceTest().get(1));
        Assert.assertEquals(associationClassSequence3, human.getAssociationClassSequenceTest().get(2));

        AssociationClassSequenceTest associationClassSequence4 = new AssociationClassSequenceTest(true);
        associationClassSequence4.setWeight(4);
        ProjectListTest projectList4 = new ProjectListTest(true);
        projectList4.setName("project4");
        human.addToProjectlist(1, projectList4, associationClassSequence4);

        db.commit();
        human.reload();
        Assert.assertEquals(projectList1, human.getProjectlist().get(0));
        Assert.assertEquals(projectList2, human.getProjectlist().get(2));
        Assert.assertEquals(projectList3, human.getProjectlist().get(3));
        Assert.assertEquals(projectList4, human.getProjectlist().get(1));

        human.reload();
        Assert.assertEquals(associationClassSequence1, human.getAssociationClassSequenceTest().get(0));
        Assert.assertEquals(associationClassSequence2, human.getAssociationClassSequenceTest().get(2));
        Assert.assertEquals(associationClassSequence3, human.getAssociationClassSequenceTest().get(3));
        Assert.assertEquals(associationClassSequence4, human.getAssociationClassSequenceTest().get(1));
    }

}
