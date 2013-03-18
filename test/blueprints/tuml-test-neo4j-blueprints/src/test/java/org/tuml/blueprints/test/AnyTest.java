package org.tuml.blueprints.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.tuml.datatype.test.EmailTest;
import org.tuml.tinker.allinstances.AllInstancesTest;
import org.tuml.tinker.collectiontest.*;
import org.tuml.tinker.json.JsonTest;
import org.tuml.tinker.speed.SpeedTest;
import org.tuml.tinker.uniqueindextest.UniqueIndexTest;
import org.tuml.transaction.test.TransactionSuspendResumeTest;
import org.tuml.transaction.test.TumlIdTest;

/**
 * Date: 2013/01/28
 * Time: 7:18 PM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({JsonTest.class})
public class AnyTest {

}
