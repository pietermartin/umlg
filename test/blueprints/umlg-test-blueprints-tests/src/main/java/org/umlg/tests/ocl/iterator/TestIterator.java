package org.umlg.tests.ocl.iterator;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.iterate.IterateChild;
import org.umlg.ocl.iterate.IterateParent;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2016/04/01
 * Time: 11:33 AM
 */
public class TestIterator extends BaseLocalDbTest {

    @Test
    public void testIteratorSimple() {
        IterateParent iterateParent = new IterateParent();
        iterateParent.setName("parent");
        IterateChild iterateChild1 = new IterateChild(iterateParent);
        iterateChild1.setName("iterateChild1");
        IterateChild iterateChild2 = new IterateChild(iterateParent);
        iterateChild2.setName("iterateChild2");
        IterateChild iterateChild3 = new IterateChild(iterateParent);
        iterateChild3.setName("iterateChild3");
        UMLG.get().commit();
        Assert.assertEquals("iterateChild1iterateChild2iterateChild3", iterateParent.getIterChild());
    }

    @Test
    public void testIteratorWithIf() {
        IterateParent iterateParent = new IterateParent();
        iterateParent.setName("parent");
        IterateChild iterateChild1 = new IterateChild(iterateParent);
        iterateChild1.setName("iterateChild1");
        IterateChild iterateChild2 = new IterateChild(iterateParent);
        iterateChild2.setName("iterateChild2");
        IterateChild iterateChild3 = new IterateChild(iterateParent);
        iterateChild3.setName("iterateChild3");
        UMLG.get().commit();
        Assert.assertEquals("startiterateChild1iterateChild2iterateChild3", iterateParent.getIterWithIf());
    }
}
