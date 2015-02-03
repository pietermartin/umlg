package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.gremlin.TestGroovyExecutor;
import org.umlg.tests.associationclass.TestAssociationClassCopiesOnePrimitivePropertiesToEdge;
import org.umlg.tests.changenotification.TestChangeNotification;
import org.umlg.tests.concretetest.TestOneToMany;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestAssociationClassCopiesOnePrimitivePropertiesToEdge.class
})
public class UmlgAnyTestSuite {
}
