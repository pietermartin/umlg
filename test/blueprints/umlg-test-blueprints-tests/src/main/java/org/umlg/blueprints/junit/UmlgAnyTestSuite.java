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
import org.umlg.tests.concretetest.TestOneToMany;
import org.umlg.tests.enumeration.ManyEnumerationTest;
import org.umlg.tests.indexing.TestIndexing;
import org.umlg.tests.indexing.TestIndexingDataType;
import org.umlg.tests.lookup.TestOneLookup;
import org.umlg.tests.qualifiertest.TestQualifierOnManyToMany;
import org.umlg.tests.query.TestMetaQueries;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
//        TestAllInstancesOnAbstractClass.class
//        ManyToManyToSelfSequenceTest.class
        AllInstancesTest.class
})
public class UmlgAnyTestSuite {
}
