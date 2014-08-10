package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.tests.collectiontest.QualifiedBagTest;
import org.umlg.tests.collectiontest.QualifiedSequenceTest;
import org.umlg.tests.datatype.DataTypeTest;
import org.umlg.tests.javaprimitivetypes.TestJavaPrimitiveTypes;
import org.umlg.tests.ocl.datatypes.TestOclCollectOnDataTypes;
import org.umlg.tests.ocl.ocloperator.OclAndOperatorTest;
import org.umlg.tests.qualifiertest.TestQualifiedDeletion;
import org.umlg.tests.qualifiertest.TestQualifiedOnMultipleProperties;
import org.umlg.tests.qualifiertest.TestQualifier;
import org.umlg.tests.qualifiertest.TestQualifierOnManyToMany;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        QualifiedSequenceTest.class
})
public class UmlgAnyTestSuite {
}
