package org.umlg.tests.collectiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.SequenceList2;
import org.umlg.collectiontest.SequenceRoot;
import org.umlg.collectiontest.SequenceTestOrderedSet;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.persistent.PropertyTree;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.List;

/**
 * Date: 2017/02/16
 * Time: 12:46 PM
 */
public class SequenceOrderedPropertyTreeTest extends BaseLocalDbTest {

    @Test
    public void testSequenceOrdered() {

        SequenceRoot sequenceRoot = new SequenceRoot(true);
        sequenceRoot.setName("sequenceRoot");

        SequenceTestOrderedSet sequenceTestOrderedSetA = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSetA.setName("sequenceTestOrderedSetA");
        SequenceTestOrderedSet sequenceTestOrderedSetB = new SequenceTestOrderedSet(sequenceRoot);
        sequenceTestOrderedSetB.setName("sequenceTestOrderedSetB");

        SequenceList2 sequenceList2A = new SequenceList2(sequenceRoot);
        sequenceList2A.setName("sequenceList2A");
        SequenceList2 sequenceList2B = new SequenceList2(sequenceRoot);
        sequenceList2B.setName("sequenceList2B");

        sequenceRoot.addToSequenceTestOrderedSet(0, sequenceTestOrderedSetB);

        UMLG.get().commit();

        sequenceRoot.reload();
        Assert.assertEquals(2, sequenceRoot.getSequenceTestOrderedSet().size());
        Assert.assertEquals(sequenceTestOrderedSetB, sequenceRoot.getSequenceTestOrderedSet().get(0));
        Assert.assertEquals(sequenceTestOrderedSetA, sequenceRoot.getSequenceTestOrderedSet().get(1));

        PropertyTree propertyTree = PropertyTree.from("SequenceRoot");
        PropertyTree sequenceTestOrderedSet1PT = PropertyTree.from(SequenceRoot.SequenceRootRuntimePropertyEnum.sequenceTestOrderedSet);
        propertyTree.addChild(sequenceTestOrderedSet1PT);
        PropertyTree sequenceList2PT = PropertyTree.from(SequenceRoot.SequenceRootRuntimePropertyEnum.sequenceList2);
        propertyTree.addChild(sequenceList2PT);
        List<SequenceRoot> sequenceRootList = UMLG.get().get(propertyTree);
        Assert.assertEquals(1, sequenceRootList.size());

        sequenceRoot = sequenceRootList.get(0);
        Assert.assertEquals(2, sequenceRoot.getSequenceTestOrderedSet().size());
        Assert.assertEquals(sequenceTestOrderedSetB, sequenceRoot.getSequenceTestOrderedSet().get(0));
        Assert.assertEquals(sequenceTestOrderedSetA, sequenceRoot.getSequenceTestOrderedSet().get(1));

        Assert.assertEquals(2, sequenceRoot.getSequenceList2().size());
        Assert.assertEquals(sequenceList2A, sequenceRoot.getSequenceList2().get(0));
        Assert.assertEquals(sequenceList2B, sequenceRoot.getSequenceList2().get(1));
    }
}
