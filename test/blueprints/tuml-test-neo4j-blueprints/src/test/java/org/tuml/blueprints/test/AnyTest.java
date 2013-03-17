package org.tuml.blueprints.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.tuml.tinker.allinstances.AllInstancesTest;
import org.tuml.tinker.json.JsonTest;
import org.tuml.tinker.speed.SpeedTest;
import org.tuml.tinker.uniqueindextest.UniqueIndexTest;
import org.tuml.transaction.test.TransactionSuspendResumeTest;

/**
 * Date: 2013/01/28
 * Time: 7:18 PM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TransactionSuspendResumeTest.class})
public class AnyTest {

}
