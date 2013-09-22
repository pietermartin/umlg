package org.umlg.blueprints.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.enumeration.test.ManyEnumerationTest;
import org.umlg.tinker.collectiontest.OclStdLibCollectionTest;
import org.umlg.tinker.collectiontest.SequenceTest;
import org.umlg.tinker.deletiontest.DeletionTest;
import org.umlg.tinker.embeddedtest.TestEmbeddedTest;
import org.umlg.tinker.lookup.TestOneLookup;
import org.umlg.tinker.nonnavigable.NonNavigableTest;
import org.umlg.tinker.qualifiertest.TestQualifier;
import org.umlg.tinker.qualifiertest.TestQualifierOnManyToMany;
import org.umlg.tinker.query.TestMetaQueries;
import org.umlg.tinker.validationtest.TestValidation;

/**
 * Date: 2013/01/28
 * Time: 7:18 PM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestMetaQueries.class})
public class Neo4jAnyTest {

}
