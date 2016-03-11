package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.tests.ocl.ocloperator.OclSelectTest;
import org.umlg.tests.qualifiertest.TestQualifier;
import org.umlg.tests.qualifiertest.TestQualifierWithDateAndEnum;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestQualifierWithDateAndEnum.class
})
public class UmlgAnyTestSuite {
}
