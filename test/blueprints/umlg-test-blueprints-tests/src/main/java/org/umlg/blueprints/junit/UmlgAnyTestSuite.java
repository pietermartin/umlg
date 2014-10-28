package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.gremlin.TestGroovyExecutor;
import org.umlg.tests.batch.TestBatchMode;
import org.umlg.tests.bulkcollection.TestBulkCollection;
import org.umlg.tests.changenotification.TestChangeNotification;
import org.umlg.tests.collectiontest.OrderedSetTestTest;
import org.umlg.tests.datatypeassociation.TestDataTypeAssociation;
import org.umlg.tests.embeddedtest.TestEmbeddedTest;
import org.umlg.tests.enumeration.TestEnumerationAttribute;
import org.umlg.tests.indexing.TestIndexing;
import org.umlg.tests.indexing.TestIndexingDataType;
import org.umlg.tests.ocl.kindoftypeof.TestKindOfTypeOf;
import org.umlg.tests.ocl.ocloperator.OclTestToLowerCase;
import org.umlg.tests.qualifiertest.TestQualifiedOnMultipleProperties;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestBatchMode.class
})
public class UmlgAnyTestSuite {
}
