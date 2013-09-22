package org.umlg.blueprints.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.datatype.test.EmailTest;
import org.umlg.enumeration.test.ManyEnumerationTest;
import org.umlg.meta.TestMetaClasses;
import org.umlg.tinker.allinstances.AllInstancesTest;
import org.umlg.tinker.collectiontest.*;
import org.umlg.tinker.componenttest.TestComponent;
import org.umlg.tinker.concretetest.TestNonCompositeOneToOne;
import org.umlg.tinker.concretetest.TestOneToMany;
import org.umlg.tinker.constraint.ConstrainedPropertyTest;
import org.umlg.tinker.deletiontest.DeletionInheritenceTest;
import org.umlg.tinker.deletiontest.DeletionTest;
import org.umlg.tinker.deletiontest.EmbeddedSetDeletionTest;
import org.umlg.tinker.embeddedtest.TestEmbeddedTest;
import org.umlg.tinker.hierarchytest.TestHierarchy;
import org.umlg.tinker.inheritencetest.TestInheritence;
import org.umlg.tinker.interfacetest.ManyToManyInverseTest;
import org.umlg.tinker.interfacetest.TestOneToManyInterface;
import org.umlg.tinker.json.JsonTest;
import org.umlg.tinker.lookup.TestOneLookup;
import org.umlg.tinker.mvel.TestMvel;
import org.umlg.tinker.nonnavigable.NonNavigableTest;
import org.umlg.tinker.primitive.TestBooleanPrimitive;
import org.umlg.tinker.primitive.TestPrimitiveRemoval;
import org.umlg.tinker.qualifiertest.TestQualifiedDeletion;
import org.umlg.tinker.qualifiertest.TestQualifier;
import org.umlg.tinker.qualifiertest.TestQualifierOnManyToMany;
import org.umlg.tinker.query.TestMetaQueries;
import org.umlg.tinker.query.TestQueryBaseModelTumlAssociation;
import org.umlg.tinker.validationtest.TestValidation;

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
        BagTestTest.class,
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
        TestQualifiedDeletion.class, TestQualifier.class,
        //Fails::TestQualifierChangeEvent.class,
        TestQualifierOnManyToMany.class,
        TestMetaQueries.class, TestQueryBaseModelTumlAssociation.class,
        TestValidation.class,
//        TransactionSuspendResumeTest.class,
        ConstrainedPropertyTest.class
})
public class TitanAllTest {

}
