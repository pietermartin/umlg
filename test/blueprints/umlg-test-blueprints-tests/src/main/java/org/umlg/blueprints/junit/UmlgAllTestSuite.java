package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.gremlin.TestGroovyExecutor;
import org.umlg.meta.TestMetaClasses;
import org.umlg.tests.allinstances.AllInstancesTest;
import org.umlg.tests.allinstances.TestAllInstancesOnAbstractClass;
import org.umlg.tests.allinstances.TestAllInstancesOnInterface;
import org.umlg.tests.associationclass.TestAssociationClassCopiesOnePrimitivePropertiesToEdge;
import org.umlg.tests.associationtoself.TestAssociationToSelf;
import org.umlg.tests.collectiontest.*;
import org.umlg.tests.componenttest.TestComponent;
import org.umlg.tests.concretetest.TestNonCompositeOneToOne;
import org.umlg.tests.concretetest.TestOneToMany;
import org.umlg.tests.constraint.ConstrainedClassTest;
import org.umlg.tests.constraint.ConstrainedPropertyTest;
import org.umlg.tests.datatype.DataTypeTest;
import org.umlg.tests.deletiontest.DeletionInheritenceTest;
import org.umlg.tests.deletiontest.DeletionTest;
import org.umlg.tests.deletiontest.EmbeddedSetDeletionTest;
import org.umlg.tests.embeddedtest.TestEmbeddedTest;
import org.umlg.tests.enumeration.ManyEnumerationTest;
import org.umlg.tests.hierarchytest.TestHierarchy;
import org.umlg.tests.indexing.TestIndexing;
import org.umlg.tests.inheritencetest.TestInheritence;
import org.umlg.tests.interfacetest.ManyToManyInverseTest;
import org.umlg.tests.interfacetest.TestOneToManyInterface;
import org.umlg.tests.javaprimitivetypes.TestJavaManyPrimitiveTypesWithValidation;
import org.umlg.tests.javaprimitivetypes.TestJavaPrimitiveTypes;
import org.umlg.tests.javaprimitivetypes.TestJavaPrimitiveTypesWithValidation;
import org.umlg.tests.json.JsonTest;
import org.umlg.tests.lookup.TestOneLookup;
import org.umlg.tests.multiplecompositeparenthierarchy.HierarchyMultipleParentsTest;
import org.umlg.tests.multiplecompositeparent.MultipleCompositeParentTest;
import org.umlg.tests.mvel.TestMvel;
import org.umlg.tests.nonnavigable.NonNavigableTest;
import org.umlg.tests.primitive.TestBooleanPrimitive;
import org.umlg.tests.primitive.TestPrimitiveRemoval;
import org.umlg.tests.qualifiertest.TestQualifiedDeletion;
import org.umlg.tests.qualifiertest.TestQualifier;
import org.umlg.tests.qualifiertest.TestQualifierOnManyToMany;
import org.umlg.tests.query.TestMetaQueries;
import org.umlg.tests.query.TestQueryBaseModelUmlgAssociation;
import org.umlg.tests.ringtest.TestFingerRing;
import org.umlg.tests.root.TestRootMethods;
import org.umlg.tests.speed.SpeedTest;
import org.umlg.tests.subsetting.TestSubsetting;
import org.umlg.tests.validationtest.TestValidation;

/**
 * Date: 2013/10/19
 * Time: 9:57 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestGroovyExecutor.class,
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
        TestSubsetting.class,
        TestIndexing.class,
        TestAssociationToSelf.class,
        ManyToManyOrderedSetTest.class,
        ManyToManySequenceTest.class,
        ManyToManyToSelfSequenceTest.class,
        TestRootMethods.class,
        TestJavaPrimitiveTypes.class,
        TestJavaPrimitiveTypesWithValidation.class,
        TestJavaManyPrimitiveTypesWithValidation.class,
        TestAllInstancesOnAbstractClass.class,
        TestAllInstancesOnInterface.class,
        TestAssociationClassCopiesOnePrimitivePropertiesToEdge.class
})
public class UmlgAllTestSuite {
}
