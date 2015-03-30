package org.umlg.tests.associationclass;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.associationclass.AssociationClass1;
import org.umlg.associationclass.AssociationClass2;
import org.umlg.associationclass.AssociationClassAC;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgQueryEnum;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/05/17
 * Time: 9:26 PM
 */
public class TestAssociationClassCopiesOnePrimitivePropertiesToEdge extends BaseLocalDbTest {

    @Test
    public void testAssociationClassOnePrimitivePropertiesAreOnEdge() {
        AssociationClass1 associationClass1 = new AssociationClass1();
        associationClass1.setName("a1");
        AssociationClass2 associationClass2 = new AssociationClass2();
        associationClass2.setName("a2");
        AssociationClassAC associationClassAC = new AssociationClassAC();
        associationClassAC.setName("ac1");
        associationClass1.addToAssociationclass2(associationClass2, associationClassAC);

        db.commit();

        //Check if the name property is on the edge
        Object result = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, associationClass1.getId(), "self.inE('AssociationClassAC').values('name')");
        Assert.assertEquals("ac1", ((Traversal)result).next());
    }

    @Test
    public void testAssociationClassOnePrimitiveUpdates() {
        AssociationClass1 associationClass1 = new AssociationClass1();
        associationClass1.setName("a1");
        AssociationClass2 associationClass2 = new AssociationClass2();
        associationClass2.setName("a2");
        AssociationClassAC associationClassAC = new AssociationClassAC();
        associationClassAC.setName("ac1");
        associationClass1.addToAssociationclass2(associationClass2, associationClassAC);

        db.commit();

        //Check if the name property is on the edge
        Object result = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, associationClass1.getId(), "self.inE('AssociationClassAC').values('name')");
        Assert.assertEquals("ac1", ((Traversal)result).next());

        associationClassAC.setName("aacc1");
        db.commit();

        //Check if the name property is on the edge
        result = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, associationClass1.getId(), "self.inE('AssociationClassAC').values('name')");
        Assert.assertEquals("aacc1", ((Traversal)result).next());

        associationClassAC.setName(null);
        db.commit();

        //Check if the name property is on the edge
        result = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, associationClass1.getId(), "self.inE('AssociationClassAC').has('name')");
        Assert.assertEquals(false, ((Traversal)result).hasNext());
        associationClassAC.setName("xxx");
        db.commit();

        //Check if the name property is on the edge
        result = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, associationClass1.getId(), "self.inE('AssociationClassAC').values('name')");
        Assert.assertEquals("xxx", ((Traversal)result).next());
    }


}
