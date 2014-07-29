package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.tests.collectiontest.SequenceTest;
import org.umlg.tests.one2one.TestOne2One;
import org.umlg.tests.subsetting.TestSubsetting;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
//        TestSubsetting.class
})
public class UmlgAnyTestSuite {
}
