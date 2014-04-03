package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.gremlin.TestGroovyExecutor;
import org.umlg.tests.associationclass.TestAssociationClassMoveInList;
import org.umlg.tests.associationtoself.TestAssociationToSelf;
import org.umlg.tests.collectiontest.*;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
//        BagTestTest.class,
//        OclStdLibBagTest.class,
//        OclStdLibCollectionTest.class,
//        OclStdLibOrderedSetTest.class,
//        OclStdLibSequenceTest.class,
//        OclStdLibSetTest.class,
//        OrderedSetTestTest.class
//        QualifiedBagTest.class,
//        QualifiedOrderedSetTest.class,
//        QualifiedSequenceTest.class,
        SequenceTest.class
//        TestInitCalled.class,
//        TestOrderedListKeepsIndex.class
//        ManyToManyOrderedSetTest.class
//        OneToManyOrderedSetTest.class ,
//        TestAssociationToSelf.class,
//        ManyToManySequenceTest.class,
//        ManyToManyToSelfSequenceTest.class


        })
public class UmlgAnyTestSuite {
}
