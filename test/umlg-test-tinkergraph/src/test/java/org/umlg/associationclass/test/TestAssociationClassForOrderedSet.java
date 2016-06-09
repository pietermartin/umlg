package org.umlg.associationclass.test;

import org.junit.Assert;
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

        AssociationClassOrderedSetTest associationClassOrderedSet = new AssociationClassOrderedSetTest(true);
        associationClassOrderedSet.setWeight(1);
        ProjectOrderedSetTest projectOrderedSet1 = new ProjectOrderedSetTest(human, associationClassOrderedSet);
        projectOrderedSet1.setName("projectOrderedSet1");

        AssociationClassOrderedSetTest associationClassSequence2 = new AssociationClassOrderedSetTest(true);
        associationClassSequence2.setWeight(2);
        ProjectOrderedSetTest projectOrderedSet2 = new ProjectOrderedSetTest(human, associationClassSequence2);
        projectOrderedSet2.setName("projectOrderedSet2");

        AssociationClassOrderedSetTest associationClassSequence3 = new AssociationClassOrderedSetTest(true);
        associationClassSequence3.setWeight(3);
        ProjectOrderedSetTest projectOrderedSet3 = new ProjectOrderedSetTest(human, associationClassSequence3);
        projectOrderedSet3.setName("projectOrderedSet3");

        AssociationClassOrderedSetTest associationClassSequence4 = new AssociationClassOrderedSetTest(true);
        associationClassSequence4.setWeight(4);
        ProjectOrderedSetTest projectOrderedSet4 = new ProjectOrderedSetTest(human, associationClassSequence4);
        projectOrderedSet4.setName("projectOrderedSet4");

        db.commit();

        Assert.assertEquals(4, human.getProjectorderedset().size());
        human = new Human(human.getVertex());
        Assert.assertEquals(4, human.getProjectorderedset().size());

        Assert.assertEquals("projectOrderedSet1", human.getProjectorderedset().get(0).getName());
        Assert.assertEquals("projectOrderedSet2", human.getProjectorderedset().get(1).getName());
        Assert.assertEquals("projectOrderedSet3", human.getProjectorderedset().get(2).getName());
        Assert.assertEquals("projectOrderedSet4", human.getProjectorderedset().get(3).getName());

        Assert.assertEquals(new Integer(1), human.getAssociationClassOrderedSetTest_projectorderedset().get(0).getWeight());
        Assert.assertEquals(new Integer(2), human.getAssociationClassOrderedSetTest_projectorderedset().get(1).getWeight());
        Assert.assertEquals(new Integer(3), human.getAssociationClassOrderedSetTest_projectorderedset().get(2).getWeight());
        Assert.assertEquals(new Integer(4), human.getAssociationClassOrderedSetTest_projectorderedset().get(3).getWeight());

        boolean added = human.getProjectorderedset().add(projectOrderedSet1, associationClassOrderedSet);
        Assert.assertFalse(added);
        db.commit();

        Assert.assertEquals(4, human.getProjectorderedset().size());

    }

    @Test
    public void testAssociationClass1_Removal() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassOrderedSetTest associationClassOrderedSet1 = new AssociationClassOrderedSetTest(true);
        associationClassOrderedSet1.setWeight(1);
        ProjectOrderedSetTest projectOrderedSet1 = new ProjectOrderedSetTest(human, associationClassOrderedSet1);
        projectOrderedSet1.setName("projectOrderedSet1");

        AssociationClassOrderedSetTest associationClassOrderedSet2 = new AssociationClassOrderedSetTest(true);
        associationClassOrderedSet2.setWeight(2);
        ProjectOrderedSetTest projectOrderedSet2 = new ProjectOrderedSetTest(human, associationClassOrderedSet2);
        projectOrderedSet2.setName("projectOrderedSet2");

        AssociationClassOrderedSetTest associationClassOrderedSet3 = new AssociationClassOrderedSetTest(true);
        associationClassOrderedSet3.setWeight(3);
        ProjectOrderedSetTest projectOrderedSet3 = new ProjectOrderedSetTest(human, associationClassOrderedSet3);
        projectOrderedSet3.setName("projectOrderedSet3");

        db.commit();

        human = new Human(human.getVertex());
        Assert.assertEquals(3, human.getProjectorderedset().size());

        projectOrderedSet1 = new ProjectOrderedSetTest(projectOrderedSet1.getVertex());
        human.removeFromProjectorderedset(projectOrderedSet1);
        projectOrderedSet1.delete();

        db.commit();

        human = new Human(human.getVertex());
        Assert.assertEquals(2, human.getProjectorderedset().size());
        Assert.assertEquals(2, human.getAssociationClassOrderedSetTest_projectorderedset().size());
        Assert.assertEquals(new Integer(2), human.getAssociationClassOrderedSetTest_projectorderedset().get(0).getWeight());

    }
}
