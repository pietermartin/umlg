package org.umlg.tests.associationclass;

import com.tinkerpop.pipes.util.Pipeline;
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
        Object result = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, associationClass1.getId(), "self.inE('AssociationClassAC').name");
        Assert.assertEquals("ac1", ((Pipeline)result).next());
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
        Object result = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, associationClass1.getId(), "self.inE('AssociationClassAC').name");
        Assert.assertEquals("ac1", ((Pipeline)result).next());

        associationClassAC.setName("aacc1");
        db.commit();

        //Check if the name property is on the edge
        result = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, associationClass1.getId(), "self.inE('AssociationClassAC').name");
        Assert.assertEquals("aacc1", ((Pipeline)result).next());

        associationClassAC.setName(null);
        //Check if the name property is on the edge
        result = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, associationClass1.getId(), "self.inE('AssociationClassAC').name");
        Assert.assertEquals(null, ((Pipeline)result).next());
        associationClassAC.setName("xxx");
        db.commit();

        //Check if the name property is on the edge
        result = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, associationClass1.getId(), "self.inE('AssociationClassAC').name");
        Assert.assertEquals("xxx", ((Pipeline)result).next());


    }


}
