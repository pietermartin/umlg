package org.umlg.tests.ocl.prefefinediterator;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.ocl.ocloperator.SortedByChild;
import org.umlg.ocl.ocloperator.SortedByParent;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2015/04/17
 * Time: 12:29 PM
 */
public class TestSortedBy extends BaseLocalDbTest {

    @Test
    public void testSortedBy() {
        SortedByParent sortedByParent1 = new SortedByParent();

        SortedByChild sortedByChild1 = new SortedByChild();
        sortedByChild1.setSortBy("a");
        sortedByChild1.setSortByInt(1);
        SortedByChild sortedByChild2 = new SortedByChild();
        sortedByChild2.setSortBy("b");
        sortedByChild2.setSortByInt(2);
        SortedByChild sortedByChild3 = new SortedByChild();
        sortedByChild3.setSortBy("c");
        sortedByChild3.setSortByInt(3);
        SortedByChild sortedByChild4 = new SortedByChild();
        sortedByChild4.setSortBy("d");
        sortedByChild4.setSortByInt(4);

        sortedByParent1.addToSortedByChild(sortedByChild4);
        sortedByParent1.addToSortedByChild(sortedByChild3);
        sortedByParent1.addToSortedByChild(sortedByChild2);
        sortedByParent1.addToSortedByChild(sortedByChild1);

        UMLG.get().commit();

        Assert.assertEquals(sortedByChild1, sortedByParent1.getSortedBy().get(0));
        Assert.assertEquals(sortedByChild2, sortedByParent1.getSortedBy().get(1));
        Assert.assertEquals(sortedByChild3, sortedByParent1.getSortedBy().get(2));
        Assert.assertEquals(sortedByChild4, sortedByParent1.getSortedBy().get(3));

        Assert.assertEquals(sortedByChild1, sortedByParent1.getSortedByChild().sortedBy((a,b)->a.getSortByInt().compareTo(b.getSortByInt())).get(0));
        Assert.assertEquals(sortedByChild2, sortedByParent1.getSortedByChild().sortedBy((a,b)->a.getSortByInt().compareTo(b.getSortByInt())).get(1));
        Assert.assertEquals(sortedByChild3, sortedByParent1.getSortedByChild().sortedBy((a,b)->a.getSortByInt().compareTo(b.getSortByInt())).get(2));
        Assert.assertEquals(sortedByChild4, sortedByParent1.getSortedByChild().sortedBy((a,b)->a.getSortByInt().compareTo(b.getSortByInt())).get(3));
    }
}
