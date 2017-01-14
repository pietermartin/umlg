package org.umlg.tests.allinstances;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.interfacetest.*;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/05/07
 * Time: 7:01 PM
 */
public class TestAllInstancesOnInterface extends BaseLocalDbTest {

    @Test
    public void testAllInstancesOnInterface() {
        God g = new God();
        ManyA manyA1 = new ManyA(g);
        ManyA manyA2 = new ManyA(g);
        ManyA manyA3 = new ManyA(g);
        ManyB manyB1 = new ManyB(g);
        ManyB manyB2 = new ManyB(g);
        ManyB manyB3 = new ManyB(g);
        manyA1.addToIManyB(manyB1);
        manyA1.addToIManyB(manyB2);
        manyA1.addToIManyB(manyB3);
        manyA2.addToIManyB(manyB1);
        manyA2.addToIManyB(manyB2);
        manyA2.addToIManyB(manyB3);
        manyA3.addToIManyB(manyB1);
        manyA3.addToIManyB(manyB2);
        manyA3.addToIManyB(manyB3);
        db.commit();

        Assert.assertEquals(6, IMany.allInstances().size());
        Assert.assertEquals(3, IManyA.allInstances().size());
        Assert.assertEquals(3, IManyB.allInstances().size());

    }
}
