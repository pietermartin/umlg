package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.tests.datatype.DataTypeTest;
import org.umlg.tests.deletiontest.EmbeddedSetDeletionTest;
import org.umlg.tests.embeddedtest.TestEmbeddedTest;
import org.umlg.tests.enumeration.ManyEnumerationTest;
import org.umlg.tests.json.JsonTest;
import org.umlg.tests.qualifiertest.TestQualifiedDeletion;
import org.umlg.tests.query.TestMetaQueries;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestMetaQueries.class
})
public class UmlgAnyTestSuite {
}
