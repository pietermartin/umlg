package org.umlg.testmodelimport;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.A;
import org.umlg.B;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.Iterator;

/**
 * Created by pieter on 2015/12/21.
 */
public class TestModelImport extends BaseLocalDbTest {

    /**
     * This is a tad sensitive.
     * Associations between the 2 models must be from the one generated to the other.
     */
    @Test
    public void testModelImport() {
        A a = new A();
        a.setAttribute1("testA");
        B b1 = new B();
        b1.setAttribute1("testB1");
        a.addToB(b1);
        B b2 = new B();
        b2.setAttribute1("testB2");
        a.addToB(b2);
        UMLG.get().commit();

        Assert.assertEquals(1, A.allInstances().size());
        Assert.assertEquals(2, B.allInstances().size());
        A next = A.allInstances().iterator().next();
        Iterator<B> iterator = next.getB().iterator();
        Assert.assertTrue(B.allInstances().contains(iterator.next()));
        Assert.assertTrue(B.allInstances().contains(iterator.next()));
    }
}
