package org.umlg.blueprints.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.enumeration.test.ManyEnumerationTest;
import org.umlg.tinker.collectiontest.OclStdLibCollectionTest;
import org.umlg.tinker.lookup.TestOneLookup;
import org.umlg.tinker.qualifiertest.TestQualifier;
import org.umlg.tinker.qualifiertest.TestQualifierOnManyToMany;

/**
 * Date: 2013/01/28
 * Time: 7:18 PM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({OclStdLibCollectionTest.class})
public class AnyTest {

}
