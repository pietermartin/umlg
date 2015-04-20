package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.tests.batch.TestBatchMode;
import org.umlg.tests.collectiontest.SequenceTest;
import org.umlg.tests.concretetest.TestOneToMany;
import org.umlg.tests.ocl.ocloperator.OclAsTypeTest;
import org.umlg.tests.ocl.prefefinediterator.TestSortedBy;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestSortedBy.class
})
public class UmlgAnyTestSuite {
}
