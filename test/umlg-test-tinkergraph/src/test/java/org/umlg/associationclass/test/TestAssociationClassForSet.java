package org.umlg.associationclass.test;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.tinkergraph.*;

/**
 * Date: 2013/06/20
 * Time: 7:42 AM
 */
public class TestAssociationClassForSet extends BaseLocalDbTest {

    @Test
    public void testAssociationClass1_1() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSet associationClass1 = new AssociationClassSet(true);
        associationClass1.setWeight(1);
        ProjectSet project = new ProjectSet(human, associationClass1);
        project.setName("project1");
        db.commit();

        Assert.assertEquals(3, countVertices());
        //Human has edge to root
        //3 edges between Human, Project and AssociationClass1
        //3 edges to meta
        Assert.assertEquals(7, countEdges());

        human = new Human(human.getVertex());
        Assert.assertEquals(1, human.getProjectset().size());
        Assert.assertEquals("project1", human.getProjectset().iterator().next().getName());
        project = new ProjectSet(project.getVertex());
        Assert.assertNotNull(project.getHuman());
        Assert.assertTrue(project.getHuman() instanceof Human);
        Assert.assertEquals("human1", project.getHuman().getName());

        associationClass1 = new AssociationClassSet(associationClass1.getVertex());
        Assert.assertEquals(project, associationClass1.getProjectset());
        Assert.assertEquals(human, associationClass1.getHuman());

        Assert.assertEquals(1, human.getAssociationClassSet().size());
        Assert.assertEquals(new Integer(1), human.getAssociationClassSet().iterator().next().getWeight());
        Assert.assertNotNull(project.getAssociationClassSet());
        Assert.assertTrue(project.getAssociationClassSet() instanceof AssociationClassSet);
        Assert.assertEquals(new Integer(1), project.getAssociationClassSet().getWeight());
    }

    @Test
    public void testAssociationClass1_2() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSet associationClass1 = new AssociationClassSet(true);
        associationClass1.setWeight(1);
        ProjectSet project = new ProjectSet(human, associationClass1);
        project.setName("project1");

        AssociationClassSet associationClass2 = new AssociationClassSet(true);
        associationClass2.setWeight(2);
        ProjectSet project2 = new ProjectSet(human, associationClass2);
        project2.setName("project2");

        AssociationClassSet associationClass3 = new AssociationClassSet(true);
        associationClass3.setWeight(3);
        ProjectSet project3 = new ProjectSet(human, associationClass3);
        project3.setName("project3");

        db.commit();

        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(17, countEdges());

        human = new Human(human.getVertex());
        Assert.assertEquals(3, human.getProjectset().size());
        project = new ProjectSet(project.getVertex());
        Assert.assertNotNull(project.getHuman());
        Assert.assertTrue(project.getHuman() instanceof Human);
        Assert.assertEquals("human1", project.getHuman().getName());

        associationClass1 = new AssociationClassSet(associationClass1.getVertex());
        Assert.assertEquals(project, associationClass1.getProjectset());
        Assert.assertEquals(human, associationClass1.getHuman());

        Assert.assertEquals(3, human.getAssociationClassSet().size());
        Assert.assertNotNull(project.getAssociationClassSet());
        Assert.assertTrue(project.getAssociationClassSet() instanceof AssociationClassSet);
        Assert.assertEquals(new Integer(1), project.getAssociationClassSet().getWeight());

        boolean found1 = false;
        boolean found2 = false;
        boolean found3 = false;
        for (AssociationClassSet associationClass11 : human.getAssociationClassSet()) {
            if (associationClass11.getWeight().equals(1)) {
                found1 = true;
            } else if (associationClass11.getWeight().equals(2)) {
                found2 = true;
            } else if (associationClass11.getWeight().equals(3)) {
                found3 = true;
            } else {
                Assert.fail("Did not find association classes");
            }
        }
        if (!(found1 && found2 && found3)) {
            Assert.fail("Did not find association classes");
        }
    }

    @Test
    public void testAssociationClass1_3() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSet associationClass1 = new AssociationClassSet(true);
        associationClass1.setWeight(1);
        ProjectSet project = new ProjectSet(human, associationClass1);
        project.setName("project1");

        db.commit();

        AssociationClassSet associationClass2 = new AssociationClassSet(true);
        associationClass2.setWeight(2);
        ProjectSet project2 = new ProjectSet(true);
        project2.setName("project2");
        human.getProjectset().add(project2, associationClass2);


        AssociationClassSet associationClass3 = new AssociationClassSet(true);
        associationClass3.setWeight(3);
        ProjectSet project3 = new ProjectSet(true);
        project3.setName("project3");
        human.getProjectset().add(project3, associationClass3);

        db.commit();

        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(17, countEdges());

        human = new Human(human.getVertex());
        Assert.assertEquals(3, human.getProjectset().size());
        project = new ProjectSet(project.getVertex());
        Assert.assertNotNull(project.getHuman());
        Assert.assertTrue(project.getHuman() instanceof Human);
        Assert.assertEquals("human1", project.getHuman().getName());

        associationClass1 = new AssociationClassSet(associationClass1.getVertex());
        Assert.assertEquals(project, associationClass1.getProjectset());
        Assert.assertEquals(human, associationClass1.getHuman());

        Assert.assertEquals(3, human.getAssociationClassSet().size());
        Assert.assertNotNull(project.getAssociationClassSet());
        Assert.assertTrue(project.getAssociationClassSet() instanceof AssociationClassSet);
        Assert.assertEquals(new Integer(1), project.getAssociationClassSet().getWeight());

        boolean found1 = false;
        boolean found2 = false;
        boolean found3 = false;
        for (AssociationClassSet associationClass11 : human.getAssociationClassSet()) {
            if (associationClass11.getWeight().equals(1)) {
                found1 = true;
            } else if (associationClass11.getWeight().equals(2)) {
                found2 = true;
            } else if (associationClass11.getWeight().equals(3)) {
                found3 = true;
            } else {
                Assert.fail("Did not find association classes");
            }
        }
        if (!(found1 && found2 && found3)) {
            Assert.fail("Did not find association classes");
        }
    }

    @Test(expected = RuntimeException.class)
    public void testAssociationClassSetImmutable() {

        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSet associationClass1 = new AssociationClassSet(true);
        associationClass1.setWeight(1);
        ProjectSet project = new ProjectSet(human, associationClass1);
        project.setName("project1");

        AssociationClassSet associationClass2 = new AssociationClassSet(true);
        associationClass2.setWeight(2);
        ProjectSet project2 = new ProjectSet(human, associationClass2);
        project2.setName("project2");

        AssociationClassSet associationClass3 = new AssociationClassSet(true);
        associationClass3.setWeight(3);
        ProjectSet project3 = new ProjectSet(human, associationClass3);
        project3.setName("project3");

        db.commit();

        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(17, countEdges());

        human.getAssociationClassSet().add(new AssociationClassSet(true));

    }

    @Test
    public void testAssociationClass1_Removal() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSet associationClass1 = new AssociationClassSet(true);
        associationClass1.setWeight(1);
        ProjectSet project = new ProjectSet(human, associationClass1);
        project.setName("project1");

        AssociationClassSet associationClass2 = new AssociationClassSet(true);
        associationClass2.setWeight(2);
        ProjectSet project2 = new ProjectSet(human, associationClass2);
        project2.setName("project2");

        AssociationClassSet associationClass3 = new AssociationClassSet(true);
        associationClass3.setWeight(3);
        ProjectSet project3 = new ProjectSet(human, associationClass3);
        project3.setName("project3");

        db.commit();

        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(17, countEdges());

        human = new Human(human.getVertex());
        project = new ProjectSet(project.getVertex());
        human.removeFromProjectset(project);
        project.delete();

        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(12, countEdges());

    }

    @Test
    public void testAssociationClass1_collectionAdder() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSet associationClass1 = new AssociationClassSet(true);
        associationClass1.setWeight(1);
        ProjectSet project = new ProjectSet(true);
        project.setName("project1");
        human.getProjectset().add(project, associationClass1);

        AssociationClassSet associationClass2 = new AssociationClassSet(true);
        associationClass2.setWeight(2);
        ProjectSet project2 = new ProjectSet(true);
        project2.setName("project2");
        human.getProjectset().add(project2, associationClass2);

        AssociationClassSet associationClass3 = new AssociationClassSet(true);
        associationClass3.setWeight(3);
        ProjectSet project3 = new ProjectSet(true);
        project3.setName("project3");
        human.getProjectset().add(project3, associationClass3);

        db.commit();

        Assert.assertEquals(7, countVertices());
        Assert.assertEquals(17, countEdges());

        human = new Human(human.getVertex());
        project = new ProjectSet(project.getVertex());
        human.removeFromProjectset(project);
        project.delete();

        db.commit();

        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(12, countEdges());

    }

    @Test
    public void testAssociationClassSetOneRemovesAC() {
        Human human = new Human(true);
        human.setName("human1");

        NonComposite nonComposite1 = new NonComposite(true);
        nonComposite1.setName("nonComposite1");

        ACNonComposite acNonComposite1 = new ACNonComposite(true);
        acNonComposite1.setName("acNonComposite1");
        human.addToNoncomposite(nonComposite1, acNonComposite1);

        db.commit();

        Assert.assertEquals(3, countVertices());

        //Human and NonComposite has edges to root
        //3 edges between Human, NonComposite and ACNonComposite
        //3 edges to meta
        //2 to root
        Assert.assertEquals(8, countEdges());

        NonComposite nonComposite2 = new NonComposite(true);
        nonComposite2.setName("nonComposite2");

        ACNonComposite acNonComposite2 = new ACNonComposite(true);
        acNonComposite2.setName("acNonComposite2");
        human.addToNoncomposite(nonComposite2, acNonComposite2);
        db.commit();

        Assert.assertNotNull(nonComposite2.getACNonComposite());
        Assert.assertEquals(nonComposite2.getACNonComposite(), acNonComposite2);

        //Another 3 edges for the association
        //another 2 edges to meta
        //another 1 to root
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(14, countEdges());

        //Create another
        Human human2 = new Human(true);
        human2.setName("human2");

        db.commit();
        Assert.assertEquals(6, countVertices());
        Assert.assertEquals(16, countEdges());


        ACNonComposite acNonComposite3 = new ACNonComposite(true);
        acNonComposite3.setName("acNonComposite3");
        nonComposite2.setHuman(human2, acNonComposite3);
        db.commit();

        Assert.assertNotNull(nonComposite2.getACNonComposite());
        Assert.assertEquals(nonComposite2.getHuman().getName(), human2.getName());
        Assert.assertEquals(nonComposite2.getACNonComposite().getName(), acNonComposite3.getName());
        Assert.assertEquals(nonComposite2.getACNonComposite(), acNonComposite3);

        //acNonComposite2 should have been deleted
        Assert.assertEquals(6, countVertices());
        Assert.assertEquals(16, countEdges());
    }

    @Test
    public void testInverseSides() {
        Human human1 = new Human(true);
        human1.setName("human1");
        NonComposite nonComposite1 = new NonComposite(true);
        nonComposite1.setName("nonComposite1");
        ACNonComposite acNonComposite1 = new ACNonComposite(true);
        acNonComposite1.setName("acNonComposite1");
        human1.addToNoncomposite(nonComposite1, acNonComposite1);
        db.commit();
        Assert.assertEquals(3, countVertices());

        //Human and NonComposite has edges to root
        //3 edges between Human, NonComposite and ACNonComposite
        //3 edges to meta
        //2 to root
        Assert.assertEquals(8, countEdges());
        Assert.assertEquals(1, human1.getACNonComposite().size());

        NonComposite nonComposite2 = new NonComposite(true);
        nonComposite2.setName("nonComposite2");
        ACNonComposite acNonComposite2 = new ACNonComposite(true);
        acNonComposite2.setName("acNonComposite2");
        human1.addToNoncomposite(nonComposite2, acNonComposite2);
        db.commit();

        Assert.assertEquals(2, human1.getACNonComposite().size());

    }

}
