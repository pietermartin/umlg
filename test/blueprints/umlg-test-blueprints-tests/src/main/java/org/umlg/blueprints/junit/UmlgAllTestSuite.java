package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.gremlin.TestGremlinExecutor;
import org.umlg.meta.TestMetaClasses;
import org.umlg.tinker.allinstances.AllInstancesTest;
import org.umlg.tinker.collectiontest.*;
import org.umlg.tinker.componenttest.TestComponent;
import org.umlg.tinker.concretetest.TestNonCompositeOneToOne;
import org.umlg.tinker.concretetest.TestOneToMany;
import org.umlg.tinker.constraint.ConstrainedClassTest;
import org.umlg.tinker.constraint.ConstrainedPropertyTest;
import org.umlg.tinker.datatype.DataTypeTest;
import org.umlg.tinker.deletiontest.DeletionInheritenceTest;
import org.umlg.tinker.deletiontest.DeletionTest;
import org.umlg.tinker.deletiontest.EmbeddedSetDeletionTest;
import org.umlg.tinker.embeddedtest.TestEmbeddedTest;
import org.umlg.tinker.enumeration.ManyEnumerationTest;
import org.umlg.tinker.hierarchytest.TestHierarchy;
import org.umlg.tinker.inheritencetest.TestInheritence;
import org.umlg.tinker.interfacetest.ManyToManyInverseTest;
import org.umlg.tinker.interfacetest.TestOneToManyInterface;
import org.umlg.tinker.json.JsonTest;
import org.umlg.tinker.lookup.TestOneLookup;
import org.umlg.tinker.multiplecompositeparenthierarchy.HierarchyMultipleParentsTest;
import org.umlg.tinker.multiplecompositeparent.MultipleCompositeParentTest;
import org.umlg.tinker.mvel.TestMvel;
import org.umlg.tinker.nonnavigable.NonNavigableTest;
import org.umlg.tinker.primitive.TestBooleanPrimitive;
import org.umlg.tinker.primitive.TestPrimitiveRemoval;
import org.umlg.tinker.qualifiertest.TestQualifiedDeletion;
import org.umlg.tinker.qualifiertest.TestQualifier;
import org.umlg.tinker.qualifiertest.TestQualifierOnManyToMany;
import org.umlg.tinker.query.TestMetaQueries;
import org.umlg.tinker.query.TestQueryBaseModelUmlgAssociation;
import org.umlg.tinker.ringtest.TestFingerRing;
import org.umlg.tinker.speed.SpeedTest;
import org.umlg.tinker.subsetting.TestSubsetting;
import org.umlg.tinker.validationtest.TestValidation;

/**
 * Date: 2013/10/19
 * Time: 9:57 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestGremlinExecutor.class,
        TestMetaClasses.class,
        AllInstancesTest.class,
        BagTestTest.class,
        OclStdLibBagTest.class,
        OclStdLibCollectionTest.class,
        OclStdLibOrderedSetTest.class,
        OclStdLibSequenceTest.class,
        OclStdLibSetTest.class,
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
        ConstrainedClassTest.class,
        ConstrainedPropertyTest.class,
        DataTypeTest.class,
        DeletionInheritenceTest.class,
        DeletionTest.class,
        EmbeddedSetDeletionTest.class,
        TestEmbeddedTest.class,
        ManyEnumerationTest.class,
        TestHierarchy.class,
        TestInheritence.class,
        ManyToManyInverseTest.class,
        TestOneToManyInterface.class,
        JsonTest.class,
        TestOneLookup.class,
        HierarchyMultipleParentsTest.class,
        MultipleCompositeParentTest.class,
        TestMvel.class,
        NonNavigableTest.class,
        TestBooleanPrimitive.class,
        TestPrimitiveRemoval.class,
        TestQualifiedDeletion.class,
        TestQualifier.class,
        //Fails::TestQualifierChangeEvent.class,
        TestQualifierOnManyToMany.class,
        TestMetaQueries.class,
        TestQueryBaseModelUmlgAssociation.class,
        TestFingerRing.class,
        SpeedTest.class,
        TestValidation.class,
        TestSubsetting.class


//        TransactionSuspendResumeTest.class,
})
public class UmlgAllTestSuite {
}
