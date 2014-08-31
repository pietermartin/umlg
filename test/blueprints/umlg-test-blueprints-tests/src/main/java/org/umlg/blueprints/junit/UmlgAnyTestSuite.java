package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.gremlin.TestGroovyExecutor;
import org.umlg.tests.allinstances.AllInstancesTest;
import org.umlg.tests.collectiontest.*;
import org.umlg.tests.datatype.DataTypeTest;
import org.umlg.tests.javaprimitivetypes.TestJavaPrimitiveTypes;
import org.umlg.tests.json.JsonTest;
import org.umlg.tests.ocl.datatypes.TestOclCollectOnDataTypes;
import org.umlg.tests.ocl.ocloperator.*;
import org.umlg.tests.ocl.operation.OclOperationTest;
import org.umlg.tests.ocl.qualifiers.TestNavigateQualifedProperty;
import org.umlg.tests.qualifiertest.*;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        OclOrOperatorTest.class
})
public class UmlgAnyTestSuite {
}
