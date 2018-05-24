package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.tests.datatype.DataTypeTest;
import org.umlg.tests.indexing.TestIndexingDataType;
import org.umlg.tests.qualifiertest.TestQualifierWithDateAndEnum;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestIndexingDataType.class,
        DataTypeTest.class,
        TestQualifierWithDateAndEnum.class
})
public class UmlgAnyTestSuite {
}
