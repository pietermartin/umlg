package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.gremlin.TestGremlinExecutor;
import org.umlg.tinker.collectiontest.OrderedSetTestTest;
import org.umlg.tinker.enumeration.ManyEnumerationTest;
import org.umlg.tinker.nonnavigable.NonNavigableTest;
import org.umlg.tinker.speed.SpeedTest;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ManyEnumerationTest.class})
public class UmlgAnyTestSuite {
}
