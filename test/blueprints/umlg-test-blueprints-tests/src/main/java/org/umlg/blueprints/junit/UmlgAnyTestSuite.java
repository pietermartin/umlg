package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.gremlin.TestGroovyExecutor;
import org.umlg.tests.allinstances.TestAllInstancesOnAbstractClass;
import org.umlg.tests.allinstances.TestAllInstancesOnInterface;
import org.umlg.tests.associationclass.TestAssociationClassCopiesOnePrimitivePropertiesToEdge;
import org.umlg.tests.associationclass.TestAssociationClassMoveInList;
import org.umlg.tests.associationtoself.TestAssociationToSelf;
import org.umlg.tests.collectiontest.*;
import org.umlg.tests.constraint.ConstrainedPropertyTest;
import org.umlg.tests.datatype.DataTypeTest;
import org.umlg.tests.indexing.TestIndexing;
import org.umlg.tests.javaprimitivetypes.TestJavaManyPrimitiveTypesWithValidation;
import org.umlg.tests.javaprimitivetypes.TestJavaPrimitiveTypes;
import org.umlg.tests.javaprimitivetypes.TestJavaPrimitiveTypesWithValidation;
import org.umlg.tests.json.JsonTest;
import org.umlg.tests.lookup.TestOneLookup;
import org.umlg.tests.qualifiertest.TestQualifiedDeletion;
import org.umlg.tests.qualifiertest.TestQualifier;
import org.umlg.tests.root.TestRootMethods;
import org.umlg.tests.validationtest.TestValidation;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
//        BagTestTest.class,
//        OclStdLibBagTest.class,
//        OclStdLibCollectionTest.class
//        OclStdLibOrderedSetTest.class,
//        OclStdLibSequenceTest.class,
//        OclStdLibSetTest.class,
//        OrderedSetTestTest.class
//        QualifiedBagTest.class,
//        QualifiedOrderedSetTest.class,
//        QualifiedSequenceTest.class,
//        SequenceTest.class
//        TestInitCalled.class,
//        TestOrderedListKeepsIndex.class
//        ManyToManyOrderedSetTest.class
//        OneToManyOrderedSetTest.class ,
//        TestAssociationToSelf.class,
//        ManyToManySequenceTest.class,
//        ManyToManyToSelfSequenceTest.class
//        TestIndexing.class,
//        TestGroovyExecutor.class,
//        TestRootMethods.class,
//        TestValidation.class
//        TestJavaPrimitiveTypes.class
//        TestJavaPrimitiveTypesWithValidation.class,
//        TestJavaManyPrimitiveTypesWithValidation.class,
//        TestAllInstancesOnAbstractClass.class,
//        TestAllInstancesOnInterface.class,
//        ConstrainedPropertyTest.class,
//        TestOneLookup.class,
//        TestQualifier.class
//        TestQualifiedDeletion.class,
//        TestAssociationClassMoveInList.class,
        TestAssociationClassCopiesOnePrimitivePropertiesToEdge.class
//        JsonTest.class,
//        DataTypeTest.class

})

public class UmlgAnyTestSuite {
}
