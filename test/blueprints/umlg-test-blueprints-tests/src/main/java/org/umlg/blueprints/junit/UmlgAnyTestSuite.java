package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.gremlin.TestGremlinExecutor;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestGremlinExecutor.class})
public class UmlgAnyTestSuite {
}
