package org.tuml.blueprints.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.tuml.datatype.test.EmailTest;
import org.tuml.enumeration.test.ManyEnumerationTest;
import org.tuml.meta.TestMetaClasses;
import org.tuml.tinker.allinstances.AllInstancesTest;
import org.tuml.tinker.collectiontest.*;
import org.tuml.tinker.componenttest.TestComponent;
import org.tuml.tinker.concretetest.TestNonCompositeOneToOne;
import org.tuml.tinker.concretetest.TestOneToMany;
import org.tuml.tinker.constraint.ConstrainedPropertyTest;
import org.tuml.tinker.deletiontest.DeletionInheritenceTest;
import org.tuml.tinker.deletiontest.DeletionTest;
import org.tuml.tinker.deletiontest.EmbeddedSetDeletionTest;
import org.tuml.tinker.embeddedtest.TestEmbeddedTest;
import org.tuml.tinker.hierarchytest.TestHierarchy;
import org.tuml.tinker.inheritencetest.TestInheritence;
import org.tuml.tinker.interfacetest.ManyToManyInverseTest;
import org.tuml.tinker.interfacetest.TestOneToManyInterface;
import org.tuml.tinker.json.JsonTest;
import org.tuml.tinker.lookup.TestOneLookup;
import org.tuml.tinker.mvel.TestMvel;
import org.tuml.tinker.nonnavigable.NonNavigableTest;
import org.tuml.tinker.primitive.TestBooleanPrimitive;
import org.tuml.tinker.primitive.TestPrimitiveRemoval;
import org.tuml.tinker.qualifiertest.TestQualifiedDeletion;
import org.tuml.tinker.qualifiertest.TestQualifier;
import org.tuml.tinker.qualifiertest.TestQualifierChangeEvent;
import org.tuml.tinker.qualifiertest.TestQualifierOnManyToMany;
import org.tuml.tinker.query.TestMetaQueries;
import org.tuml.tinker.query.TestQueryBaseModelTumlAssociation;
import org.tuml.tinker.uniqueindextest.UniqueIndexTest;
import org.tuml.tinker.validationtest.TestValidation;
import org.tuml.transaction.test.TransactionSuspendResumeTest;
import org.tuml.transaction.test.TumlIdTest;

/**
 * Date: 2013/01/28
 * Time: 7:18 PM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        EmailTest.class,
        ManyEnumerationTest.class,
        TestMetaClasses.class,
        AllInstancesTest.class,
        BagTesttest.class,
        OclStdLibBagTest.class,
        OclStdLibCollectionTest.class,
        OclStdLibSequenceTest.class,
        OclStdLibSetTest.class,
        OclStdLibOrderedSetTest.class,
        OrderedSetTestTest.class,
        QualifiedBagTest.class,
        QualifiedOrderedSetTest.class,
        QualifiedSequenceTest.class,
        SequenceTest.class,
        TestInitCalled.class,
        TestOrderedListKeepsIndex.class,
        TestComponent.class,
        TestNonCompositeOneToOne.class,
        TestOneToMany.class,
        DeletionInheritenceTest.class, DeletionTest.class, EmbeddedSetDeletionTest.class,
        TestEmbeddedTest.class,
        TestHierarchy.class,
        TestInheritence.class,
        ManyToManyInverseTest.class, TestOneToManyInterface.class,
        JsonTest.class,
        TestOneLookup.class,
        TestMvel.class,
        NonNavigableTest.class,
        TestBooleanPrimitive.class,TestPrimitiveRemoval.class,
        TestQualifiedDeletion.class, TestQualifier.class, TestQualifierChangeEvent.class, TestQualifierOnManyToMany.class,
        TestMetaQueries.class, TestQueryBaseModelTumlAssociation.class,
        TestValidation.class,
        TransactionSuspendResumeTest.class,
        ConstrainedPropertyTest.class
//        UniqueIndexTest.class,
//        TumlIdTest.class
})
public class AllTest {

}
