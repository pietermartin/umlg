package org.umlg.associationclass.test;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.tinkergraph.AssociationClass1;
import org.umlg.tinkergraph.Human;
import org.umlg.tinkergraph.Project;

/**
 * Date: 2013/06/20
 * Time: 7:42 AM
 */
public class TestAssociationClass extends BaseLocalDbTest {

    @Test
    public void testAssociationClass1() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClass1 associationClass1 = new AssociationClass1(true);
        associationClass1.setWeight(1);
        Project project = new Project(human, associationClass1);
        project.setName("project1");
        db.commit();

        Assert.assertEquals(3, countVertices());
        //Human and AssociationClass has edges to root
        //3 edges between Human, Project and AssociationClass1
        //3 edges to meta
        Assert.assertEquals(8, countEdges());

        human = new Human(human.getVertex());
        Assert.assertEquals(1, human.getProject().size());
        project = new Project(project.getVertex());
        Assert.assertNotNull(project.getHuman());

        associationClass1 = new AssociationClass1(associationClass1.getVertex());
        Assert.assertEquals(project, associationClass1.getProject());
        Assert.assertEquals(human, associationClass1.getHuman());

    }
}
