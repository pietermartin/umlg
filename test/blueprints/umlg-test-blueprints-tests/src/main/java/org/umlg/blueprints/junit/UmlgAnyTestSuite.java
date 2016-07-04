package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.tests.associationclass.TestAssociationClassDuplicates;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestAssociationClassDuplicates.class,
})
public class UmlgAnyTestSuite {
}
