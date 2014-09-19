package org.umlg.tests.qualifiertest;

import com.tinkerpop.gremlin.process.T;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.qualifiertest.AQualifierB;
import org.umlg.qualifiertest.BQualifierB;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.util.Pair;

/**
 * Date: 2014/09/07
 * Time: 7:03 PM
 */
public class TestQualifiedDerivedUnionProperty extends BaseLocalDbTest {

    @Test
    public void testDerivedQualifiedDerivedUnionProperty() {

        AQualifierB aQualifierB = new AQualifierB();
        aQualifierB.setName("aQualifierB");

        BQualifierB bQualifierB1 = new BQualifierB(aQualifierB);
        bQualifierB1.setName("bQualifierB1");
        BQualifierB bQualifierB2 = new BQualifierB(aQualifierB);
        bQualifierB2.setName("bQualifierB2");

        UMLG.get().commit();

        Assert.assertEquals(1, aQualifierB.getBQualifierBForAQualifierBQualifier(Pair.of(T.eq, "bQualifierB1")).size(), 0);
        Assert.assertEquals(1, aQualifierB.getBQualifierBForAQualifierBQualifier(Pair.of(T.eq, "bQualifierB2")).size(), 0);

    }
}
