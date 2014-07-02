package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.tests.collectiontest.OclStdLibCollectionTest;
import org.umlg.tests.concretetest.TestOneToMany;
import org.umlg.tests.indexing.TestIndexing;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestOneToMany.class

})
public class UmlgAnyTestSuite {
}
