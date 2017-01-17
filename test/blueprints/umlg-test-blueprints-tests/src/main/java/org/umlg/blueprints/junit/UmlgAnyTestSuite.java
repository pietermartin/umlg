package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.tests.json.JsonTest;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
//        TestBatchMode.class,
//        TestBulkCollection.class,
        JsonTest.class
})
public class UmlgAnyTestSuite {
}
