package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.tests.deletiontest.DeletionTest;
import org.umlg.tests.rollback.TestRollback;
import org.umlg.tests.validationtest.TestValidation;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestValidation.class})
public class UmlgAnyTestSuite {
}
