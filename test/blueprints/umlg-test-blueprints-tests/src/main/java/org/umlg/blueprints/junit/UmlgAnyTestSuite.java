package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.gremlin.TestGremlinExecutor;
import org.umlg.tests.allinstances.AllInstancesTest;
import org.umlg.tests.indexing.TestIndexing;
import org.umlg.tests.qualifiertest.TestQualifier;
import org.umlg.tests.qualifiertest.TestQualifierChangeEvent;
import org.umlg.tests.qualifiertest.TestQualifierOnManyToMany;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestIndexing.class})
public class UmlgAnyTestSuite {
}
