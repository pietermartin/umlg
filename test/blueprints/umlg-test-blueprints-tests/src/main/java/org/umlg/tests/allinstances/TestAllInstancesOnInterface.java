package org.umlg.tests.allinstances;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.interfacetest.IMany;
import org.umlg.interfacetest.ManyA;
import org.umlg.interfacetest.ManyB;
import org.umlg.interfacetest.meta.IManyAMeta;
import org.umlg.interfacetest.meta.IManyBMeta;
import org.umlg.interfacetest.meta.IManyMeta;
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

        Assert.assertEquals(6, IManyMeta.getInstance().getAllInstances().size());
        Assert.assertEquals(3, IManyAMeta.getInstance().getAllInstances().size());
        Assert.assertEquals(3, IManyBMeta.getInstance().getAllInstances().size());

    }
}
