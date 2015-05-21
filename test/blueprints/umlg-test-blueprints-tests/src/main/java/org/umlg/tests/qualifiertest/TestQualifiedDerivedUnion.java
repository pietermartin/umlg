package org.umlg.tests.qualifiertest;

import org.apache.tinkerpop.gremlin.process.traversal.Compare;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.qualifiertest.AAQualifierAA;
import org.umlg.qualifiertest.BBQualifierBB;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.util.Pair;

/**
 * Date: 2015/05/08
 * Time: 2:04 PM
 */
public class TestQualifiedDerivedUnion extends BaseLocalDbTest {

    @Test
    public void testQualifiedDerivedUnion() {
        AAQualifierAA aaQualifierAA1 = new AAQualifierAA();
        aaQualifierAA1.setName("a1");
        BBQualifierBB bbQualifierBB1 = new BBQualifierBB(aaQualifierAA1);
        bbQualifierBB1.setName("b1");
        bbQualifierBB1.setParameterName("p1");
        BBQualifierBB bbQualifierBB2 = new BBQualifierBB(aaQualifierAA1);
        bbQualifierBB2.setName("b2");
        bbQualifierBB2.setParameterName("p2");
        BBQualifierBB bbQualifierBB3 = new BBQualifierBB(aaQualifierAA1);
        bbQualifierBB3.setName("b3");
        bbQualifierBB3.setParameterName("p3");
        UMLG.get().commit();

        aaQualifierAA1.reload();
        Assert.assertEquals(bbQualifierBB1, aaQualifierAA1.getBBQualifierForBbQualifierByName(Pair.of(Compare.eq, "b1")));
        Assert.assertEquals(bbQualifierBB1, aaQualifierAA1.getBBQualifierBForBBQualifierParameterName(Pair.of(Compare.eq, "p1")));
        Assert.assertEquals(3, aaQualifierAA1.getBBQualifier().size());
    }
}
