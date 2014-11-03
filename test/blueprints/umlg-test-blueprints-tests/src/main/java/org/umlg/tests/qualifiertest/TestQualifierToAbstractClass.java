package org.umlg.tests.qualifiertest;

import com.tinkerpop.gremlin.structure.Compare;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.qualifiertest.ConcreteQ;
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
        ConcreteQ concreteQ1 = new ConcreteQ();
        concreteQ1.setName("concreteQ1");
        q.addToAbstractQ(concreteQ1);
        ConcreteQ concreteQ2 = new ConcreteQ();
        concreteQ2.setName("concreteQ2");
        q.addToAbstractQ(concreteQ2);
        UMLG.get().commit();

        q.reload();
        Assert.assertEquals(2, q.getAbstractQ().size());
        Assert.assertNotNull(q.getAbstractQForAbstractQNameQualifier(Pair.of(Compare.eq, "concreteQ1")));
    }
}
