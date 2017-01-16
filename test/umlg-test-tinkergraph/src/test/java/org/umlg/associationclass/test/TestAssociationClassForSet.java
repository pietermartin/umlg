package org.umlg.associationclass.test;

import org.junit.Assert;
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
        AssociationClassSetTest associationClass1 = new AssociationClassSetTest(true);
        associationClass1.setWeight(1);
        ProjectSetTest project = new ProjectSetTest(human, associationClass1);
        project.setName("project1");
        db.commit();

        Assert.assertEquals(2, countVertices());
        //Human has edge to root
        //3 edges between Human, Project and AssociationClass1
        //3 edges to meta
        Assert.assertEquals(3, countEdges());

        human = new Human(human.getVertex());
        Assert.assertEquals(1, human.getProjectset().size());
        Assert.assertEquals("project1", human.getProjectset().iterator().next().getName());
        project = new ProjectSetTest(project.getVertex());
        Assert.assertNotNull(project.getHuman());
        Assert.assertTrue(project.getHuman() instanceof Human);
        Assert.assertEquals("human1", project.getHuman().getName());

        associationClass1 = new AssociationClassSetTest(associationClass1.getVertex());
        Assert.assertEquals(project, associationClass1.getProjectset());
        Assert.assertEquals(human, associationClass1.getHuman());

        Assert.assertEquals(1, human.getAssociationClassSetTest_projectset().size());
        Assert.assertEquals(new Integer(1), human.getAssociationClassSetTest_projectset().iterator().next().getWeight());
        Assert.assertNotNull(project.getAssociationClassSetTest_human());
        Assert.assertTrue(project.getAssociationClassSetTest_human() instanceof AssociationClassSetTest);
        Assert.assertEquals(new Integer(1), project.getAssociationClassSetTest_human().getWeight());
    }

    @Test
    public void testAssociationClass1_2() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSetTest associationClass1 = new AssociationClassSetTest(true);
        associationClass1.setWeight(1);
        ProjectSetTest project = new ProjectSetTest(human, associationClass1);
        project.setName("project1");

        AssociationClassSetTest associationClass2 = new AssociationClassSetTest(true);
        associationClass2.setWeight(2);
        ProjectSetTest project2 = new ProjectSetTest(human, associationClass2);
        project2.setName("project2");

        AssociationClassSetTest associationClass3 = new AssociationClassSetTest(true);
        associationClass3.setWeight(3);
        ProjectSetTest project3 = new ProjectSetTest(human, associationClass3);
        project3.setName("project3");

        db.commit();

        Assert.assertEquals(6, countVertices());
        Assert.assertEquals(9, countEdges());

        human = new Human(human.getVertex());
        Assert.assertEquals(3, human.getProjectset().size());
        project = new ProjectSetTest(project.getVertex());
        Assert.assertNotNull(project.getHuman());
        Assert.assertTrue(project.getHuman() instanceof Human);
        Assert.assertEquals("human1", project.getHuman().getName());

        associationClass1 = new AssociationClassSetTest(associationClass1.getVertex());
        Assert.assertEquals(project, associationClass1.getProjectset());
        Assert.assertEquals(human, associationClass1.getHuman());

        Assert.assertEquals(3, human.getAssociationClassSetTest_projectset().size());
        Assert.assertNotNull(project.getAssociationClassSetTest_human());
        Assert.assertTrue(project.getAssociationClassSetTest_human() instanceof AssociationClassSetTest);
        Assert.assertEquals(new Integer(1), project.getAssociationClassSetTest_human().getWeight());

        boolean found1 = false;
        boolean found2 = false;
        boolean found3 = false;
        for (AssociationClassSetTest associationClass11 : human.getAssociationClassSetTest_projectset()) {
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
        AssociationClassSetTest associationClass1 = new AssociationClassSetTest(true);
        associationClass1.setWeight(1);
        ProjectSetTest project = new ProjectSetTest(human, associationClass1);
        project.setName("project1");

        db.commit();

        AssociationClassSetTest associationClass2 = new AssociationClassSetTest(true);
        associationClass2.setWeight(2);
        ProjectSetTest project2 = new ProjectSetTest(true);
        project2.setName("project2");
        human.getProjectset().add(project2, associationClass2);


        AssociationClassSetTest associationClass3 = new AssociationClassSetTest(true);
        associationClass3.setWeight(3);
        ProjectSetTest project3 = new ProjectSetTest(true);
        project3.setName("project3");
        human.getProjectset().add(project3, associationClass3);

        db.commit();

        Assert.assertEquals(6, countVertices());
        Assert.assertEquals(9, countEdges());

        human = new Human(human.getVertex());
        Assert.assertEquals(3, human.getProjectset().size());
        project = new ProjectSetTest(project.getVertex());
        Assert.assertNotNull(project.getHuman());
        Assert.assertTrue(project.getHuman() instanceof Human);
        Assert.assertEquals("human1", project.getHuman().getName());

        associationClass1 = new AssociationClassSetTest(associationClass1.getVertex());
        Assert.assertEquals(project, associationClass1.getProjectset());
        Assert.assertEquals(human, associationClass1.getHuman());

        Assert.assertEquals(3, human.getAssociationClassSetTest_projectset().size());
        Assert.assertNotNull(project.getAssociationClassSetTest_human());
        Assert.assertTrue(project.getAssociationClassSetTest_human() instanceof AssociationClassSetTest);
        Assert.assertEquals(new Integer(1), project.getAssociationClassSetTest_human().getWeight());

        boolean found1 = false;
        boolean found2 = false;
        boolean found3 = false;
        for (AssociationClassSetTest associationClass11 : human.getAssociationClassSetTest_projectset()) {
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
        AssociationClassSetTest associationClass1 = new AssociationClassSetTest(true);
        associationClass1.setWeight(1);
        ProjectSetTest project = new ProjectSetTest(human, associationClass1);
        project.setName("project1");

        AssociationClassSetTest associationClass2 = new AssociationClassSetTest(true);
        associationClass2.setWeight(2);
        ProjectSetTest project2 = new ProjectSetTest(human, associationClass2);
        project2.setName("project2");

        AssociationClassSetTest associationClass3 = new AssociationClassSetTest(true);
        associationClass3.setWeight(3);
        ProjectSetTest project3 = new ProjectSetTest(human, associationClass3);
        project3.setName("project3");

        db.commit();

        Assert.assertEquals(6, countVertices());
        Assert.assertEquals(9, countEdges());

        human.getAssociationClassSetTest_projectset().add(new AssociationClassSetTest(true));

    }

    @Test
    public void testAssociationClass1_Removal() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSetTest associationClass1 = new AssociationClassSetTest(true);
        associationClass1.setWeight(1);
        ProjectSetTest project = new ProjectSetTest(human, associationClass1);
        project.setName("project1");

        AssociationClassSetTest associationClass2 = new AssociationClassSetTest(true);
        associationClass2.setWeight(2);
        ProjectSetTest project2 = new ProjectSetTest(human, associationClass2);
        project2.setName("project2");

        AssociationClassSetTest associationClass3 = new AssociationClassSetTest(true);
        associationClass3.setWeight(3);
        ProjectSetTest project3 = new ProjectSetTest(human, associationClass3);
        project3.setName("project3");

        db.commit();

        Assert.assertEquals(6, countVertices());
        Assert.assertEquals(9, countEdges());

        human = new Human(human.getVertex());
        project = new ProjectSetTest(project.getVertex());
        human.removeFromProjectset(project);
        project.delete();

        db.commit();

        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(6, countEdges());

    }

    @Test
    public void testAssociationClass1_collectionAdder() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSetTest associationClass1 = new AssociationClassSetTest(true);
        associationClass1.setWeight(1);
        ProjectSetTest project = new ProjectSetTest(true);
        project.setName("project1");
        human.getProjectset().add(project, associationClass1);

        AssociationClassSetTest associationClass2 = new AssociationClassSetTest(true);
        associationClass2.setWeight(2);
        ProjectSetTest project2 = new ProjectSetTest(true);
        project2.setName("project2");
        human.getProjectset().add(project2, associationClass2);

        AssociationClassSetTest associationClass3 = new AssociationClassSetTest(true);
        associationClass3.setWeight(3);
        ProjectSetTest project3 = new ProjectSetTest(true);
        project3.setName("project3");
        human.getProjectset().add(project3, associationClass3);

        db.commit();

        Assert.assertEquals(6, countVertices());
        Assert.assertEquals(9, countEdges());

        human = new Human(human.getVertex());
        project = new ProjectSetTest(project.getVertex());
        human.removeFromProjectset(project);
        project.delete();

        db.commit();

        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(6, countEdges());

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

        Assert.assertEquals(2, countVertices());

        //Human and NonComposite has edges to root
        //3 edges between Human, NonComposite and ACNonComposite
        //3 edges to meta
        //2 to root
        Assert.assertEquals(5, countEdges());

        NonComposite nonComposite2 = new NonComposite(true);
        nonComposite2.setName("nonComposite2");

        ACNonComposite acNonComposite2 = new ACNonComposite(true);
        acNonComposite2.setName("acNonComposite2");
        human.addToNoncomposite(nonComposite2, acNonComposite2);
        db.commit();

        Assert.assertNotNull(nonComposite2.getACNonComposite_human());
        Assert.assertEquals(nonComposite2.getACNonComposite_human(), acNonComposite2);

        //Another 3 edges for the association
        //another 2 edges to meta
        //another 1 to root
        Assert.assertEquals(4, countVertices());
        Assert.assertEquals(8, countEdges());

        //Create another
        Human human2 = new Human(true);
        human2.setName("human2");

        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(9, countEdges());


        ACNonComposite acNonComposite3 = new ACNonComposite(true);
        acNonComposite3.setName("acNonComposite3");
        nonComposite2.setHuman(human2, acNonComposite3);
        db.commit();

        Assert.assertNotNull(nonComposite2.getACNonComposite_human());
        Assert.assertEquals(nonComposite2.getHuman().getName(), human2.getName());
        Assert.assertEquals(nonComposite2.getACNonComposite_human().getName(), acNonComposite3.getName());
        Assert.assertEquals(nonComposite2.getACNonComposite_human(), acNonComposite3);

        //acNonComposite2 should have been deleted
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(9, countEdges());
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
        Assert.assertEquals(2, countVertices());
        //Assert the inverse side has not been loaded from the db nit is in memory
        Assert.assertTrue(human1 == nonComposite1.getHuman());
        Assert.assertTrue(human1 == acNonComposite1.getHuman());
        Assert.assertTrue(acNonComposite1 == nonComposite1.getACNonComposite_human());

        //Human and NonComposite has edges to root
        //3 edges between Human, NonComposite and ACNonComposite
        //3 edges to meta
        //2 to root
        Assert.assertEquals(5, countEdges());
        Assert.assertEquals(1, human1.getACNonComposite_noncomposite().size());

        NonComposite nonComposite2 = new NonComposite(true);
        nonComposite2.setName("nonComposite2");
        ACNonComposite acNonComposite2 = new ACNonComposite(true);
        acNonComposite2.setName("acNonComposite2");
        human1.addToNoncomposite(nonComposite2, acNonComposite2);
        db.commit();

        Assert.assertEquals(2, human1.getACNonComposite_noncomposite().size());
        Assert.assertTrue(human1 == nonComposite1.getHuman());
        Assert.assertTrue(human1 == acNonComposite1.getHuman());
        Assert.assertTrue(human1 == nonComposite2.getHuman());
        Assert.assertTrue(human1 == acNonComposite2.getHuman());
    }

}
