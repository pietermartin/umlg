package org.umlg.blueprints.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.gremlin.TestGroovyExecutor;
import org.umlg.tests.associationclass.TestAssociationClassMoveInList;
import org.umlg.tests.associationtoself.TestAssociationToSelf;
import org.umlg.tests.collectiontest.QualifiedOrderedSetTest;
import org.umlg.tests.collectiontest.SequenceTest;
import org.umlg.tests.multiplecompositeparenthierarchy.HierarchyMultipleParentsTest;

/**
 * Date: 2013/10/19
 * Time: 10:06 AM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestAssociationToSelf.class
})
public class UmlgAnyTestSuite {
}
