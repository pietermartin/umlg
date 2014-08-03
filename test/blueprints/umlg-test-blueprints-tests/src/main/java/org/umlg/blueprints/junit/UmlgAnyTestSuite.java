package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.tests.datatype.DataTypeTest;
import org.umlg.tests.javaprimitivetypes.TestJavaPrimitiveTypes;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        DataTypeTest.class
})
public class UmlgAnyTestSuite {
}
