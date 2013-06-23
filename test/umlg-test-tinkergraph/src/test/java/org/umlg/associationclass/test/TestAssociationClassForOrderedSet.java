package org.umlg.associationclass.test;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.tinkergraph.*;

/**
 * Date: 2013/06/22
 * Time: 6:10 PM
 */
public class TestAssociationClassForOrderedSet extends BaseLocalDbTest {

    @Test
    public void testAssociationClassForOrderedSet() {

        Human human = new Human(true);
        human.setName("human");

        AssociationClassOrderedSet associationClassOrderedSet = new AssociationClassOrderedSet(true);
        associationClassOrderedSet.setWeight(1);
        ProjectOrderedSet projectOrderedSet1 = new ProjectOrderedSet(human, associationClassOrderedSet);
        projectOrderedSet1.setName("projectOrderedSet1");

        AssociationClassOrderedSet associationClassSequence2 = new AssociationClassOrderedSet(true);
        associationClassSequence2.setWeight(2);
        ProjectOrderedSet projectOrderedSet2 = new ProjectOrderedSet(human, associationClassSequence2);
        projectOrderedSet2.setName("projectOrderedSet2");

        AssociationClassOrderedSet associationClassSequence3 = new AssociationClassOrderedSet(true);
        associationClassSequence3.setWeight(3);
        ProjectOrderedSet projectOrderedSet3 = new ProjectOrderedSet(human, associationClassSequence3);
        projectOrderedSet3.setName("projectOrderedSet3");

        AssociationClassOrderedSet associationClassSequence4 = new AssociationClassOrderedSet(true);
        associationClassSequence4.setWeight(4);
        ProjectOrderedSet projectOrderedSet4 = new ProjectOrderedSet(human, associationClassSequence4);
        projectOrderedSet4.setName("projectOrderedSet4");

        db.commit();

        Assert.assertEquals(4, human.getProjectorderedset().size());
        human = new Human(human.getVertex());
        Assert.assertEquals(4, human.getProjectorderedset().size());

        Assert.assertEquals("projectOrderedSet1", human.getProjectorderedset().get(0).getName());
        Assert.assertEquals("projectOrderedSet2", human.getProjectorderedset().get(1).getName());
        Assert.assertEquals("projectOrderedSet3", human.getProjectorderedset().get(2).getName());
        Assert.assertEquals("projectOrderedSet4", human.getProjectorderedset().get(3).getName());

        Assert.assertEquals(new Integer(1), human.getAssociationClassOrderedSet().get(0).getWeight());
        Assert.assertEquals(new Integer(2), human.getAssociationClassOrderedSet().get(1).getWeight());
        Assert.assertEquals(new Integer(3), human.getAssociationClassOrderedSet().get(2).getWeight());
        Assert.assertEquals(new Integer(4), human.getAssociationClassOrderedSet().get(3).getWeight());

        boolean added = human.getProjectorderedset().add(projectOrderedSet1, associationClassOrderedSet);
        Assert.assertFalse(added);
        db.commit();

        Assert.assertEquals(4, human.getProjectorderedset().size());

    }

    @Test
    public void testAssociationClass1_Removal() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassOrderedSet associationClassOrderedSet1 = new AssociationClassOrderedSet(true);
        associationClassOrderedSet1.setWeight(1);
        ProjectOrderedSet projectOrderedSet1 = new ProjectOrderedSet(human, associationClassOrderedSet1);
        projectOrderedSet1.setName("projectOrderedSet1");

        AssociationClassOrderedSet associationClassOrderedSet2 = new AssociationClassOrderedSet(true);
        associationClassOrderedSet2.setWeight(2);
        ProjectOrderedSet projectOrderedSet2 = new ProjectOrderedSet(human, associationClassOrderedSet2);
        projectOrderedSet2.setName("projectOrderedSet2");

        AssociationClassOrderedSet associationClassOrderedSet3 = new AssociationClassOrderedSet(true);
        associationClassOrderedSet3.setWeight(3);
        ProjectOrderedSet projectOrderedSet3 = new ProjectOrderedSet(human, associationClassOrderedSet3);
        projectOrderedSet3.setName("projectOrderedSet3");

        db.commit();

        human = new Human(human.getVertex());
        Assert.assertEquals(3, human.getProjectorderedset().size());

        projectOrderedSet1 = new ProjectOrderedSet(projectOrderedSet1.getVertex());
        human.removeFromProjectorderedset(projectOrderedSet1);
        projectOrderedSet1.delete();

        db.commit();

        human = new Human(human.getVertex());
        Assert.assertEquals(2, human.getProjectorderedset().size());
        Assert.assertEquals(2, human.getAssociationClassOrderedSet().size());
        Assert.assertEquals(new Integer(2), human.getAssociationClassOrderedSet().get(0).getWeight());

    }
}
