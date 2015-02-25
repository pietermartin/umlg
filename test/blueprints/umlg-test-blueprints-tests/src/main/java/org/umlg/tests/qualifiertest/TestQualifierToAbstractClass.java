package org.umlg.tests.qualifiertest;

import org.apache.tinkerpop.gremlin.structure.Compare;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.qualifiertest.ConcreteQ1;
import org.umlg.qualifiertest.ConcreteQ2;
import org.umlg.qualifiertest.Q;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.util.Pair;

/**
 * Date: 2014/11/02
 * Time: 10:55 PM
 */
public class TestQualifierToAbstractClass extends BaseLocalDbTest {

    @Test
    public void testQualifierToAbstractClass() {
        Q q = new Q();
        q.setName("q1");
        ConcreteQ1 concreteQ1 = new ConcreteQ1();
        concreteQ1.setName("concreteQ11");
        q.addToAbstractQ(concreteQ1);
        ConcreteQ1 concreteQ2 = new ConcreteQ1();
        concreteQ2.setName("concreteQ12");
        q.addToAbstractQ(concreteQ2);

        ConcreteQ2 concreteQ21 = new ConcreteQ2();
        concreteQ21.setName("concreteQ21");
        q.addToAbstractQ(concreteQ21);
        ConcreteQ2 concreteQ22 = new ConcreteQ2();
        concreteQ22.setName("concreteQ22");
        q.addToAbstractQ(concreteQ22);

        UMLG.get().commit();

        q.reload();
        Assert.assertEquals(4, q.getAbstractQ().size());
        Assert.assertNotNull(q.getAbstractQForAbstractQNameQualifier(Pair.of(Compare.eq, "concreteQ11")));
        Assert.assertNotNull(q.getAbstractQForAbstractQNameQualifier(Pair.of(Compare.eq, "concreteQ12")));
        Assert.assertNotNull(q.getAbstractQForAbstractQNameQualifier(Pair.of(Compare.eq, "concreteQ21")));
        Assert.assertNotNull(q.getAbstractQForAbstractQNameQualifier(Pair.of(Compare.eq, "concreteQ22")));

        Assert.assertEquals(concreteQ1, q.getAbstractQForAbstractQNameQualifier(Pair.of(Compare.eq, "concreteQ11")));
        Assert.assertEquals(concreteQ2, q.getAbstractQForAbstractQNameQualifier(Pair.of(Compare.eq, "concreteQ12")));
        Assert.assertEquals(concreteQ21, q.getAbstractQForAbstractQNameQualifier(Pair.of(Compare.eq, "concreteQ21")));
        Assert.assertEquals(concreteQ22, q.getAbstractQForAbstractQNameQualifier(Pair.of(Compare.eq, "concreteQ22")));
    }
}
