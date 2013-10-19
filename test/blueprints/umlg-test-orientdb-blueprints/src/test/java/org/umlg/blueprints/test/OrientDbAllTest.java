package org.umlg.blueprints.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.datatype.test.EmailTest;
import org.umlg.tinker.enumeration.ManyEnumerationTest;
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
public class OrientDbAllTest extends UmlgTestSuite {

}
