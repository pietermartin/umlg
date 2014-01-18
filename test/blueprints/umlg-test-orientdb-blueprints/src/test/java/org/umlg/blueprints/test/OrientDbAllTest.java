package org.umlg.blueprints.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.datatype.test.EmailTest;
import org.umlg.tests.enumeration.ManyEnumerationTest;
import org.umlg.meta.TestMetaClasses;
import org.umlg.tests.allinstances.AllInstancesTest;
import org.umlg.tests.collectiontest.*;
import org.umlg.tests.componenttest.TestComponent;
import org.umlg.tests.concretetest.TestNonCompositeOneToOne;
import org.umlg.tests.concretetest.TestOneToMany;
import org.umlg.tests.constraint.ConstrainedPropertyTest;
import org.umlg.tests.deletiontest.DeletionInheritenceTest;
import org.umlg.tests.deletiontest.DeletionTest;
import org.umlg.tests.deletiontest.EmbeddedSetDeletionTest;
import org.umlg.tests.embeddedtest.TestEmbeddedTest;
import org.umlg.tests.hierarchytest.TestHierarchy;
import org.umlg.tests.inheritencetest.TestInheritence;
import org.umlg.tests.interfacetest.ManyToManyInverseTest;
import org.umlg.tests.interfacetest.TestOneToManyInterface;
import org.umlg.tests.json.JsonTest;
import org.umlg.tests.lookup.TestOneLookup;
import org.umlg.tests.mvel.TestMvel;
import org.umlg.tests.nonnavigable.NonNavigableTest;
import org.umlg.tests.primitive.TestBooleanPrimitive;
import org.umlg.tests.primitive.TestPrimitiveRemoval;
import org.umlg.tests.qualifiertest.TestQualifiedDeletion;
import org.umlg.tests.qualifiertest.TestQualifier;
import org.umlg.tests.qualifiertest.TestQualifierOnManyToMany;
import org.umlg.tests.query.TestMetaQueries;
import org.umlg.tests.query.TestQueryBaseModelTumlAssociation;
import org.umlg.tests.validationtest.TestValidation;

/**
 * Date: 2013/01/28
 * Time: 7:18 PM
 */
public class OrientDbAllTest extends UmlgTestSuite {

}
