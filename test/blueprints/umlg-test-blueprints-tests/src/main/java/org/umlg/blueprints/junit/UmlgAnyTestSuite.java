package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.collectiontest.SequenceTestOrderedSet;
import org.umlg.tests.collectiontest.*;
import org.umlg.tests.ocl.ocloperator.OclIncludesAllTest;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestOrderedListKeepsIndex.class
})
public class UmlgAnyTestSuite {
}
