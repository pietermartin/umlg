package org.umlg.blueprints.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.datatype.test.EmailTest;
import org.umlg.enumeration.test.ManyEnumerationTest;
import org.umlg.gremlin.TestGremlinExecutor;
import org.umlg.meta.TestMetaClasses;
import org.umlg.tinker.allinstances.AllInstancesTest;
import org.umlg.tinker.collectiontest.*;
import org.umlg.tinker.concretetest.TestOneToMany;
import org.umlg.tinker.query.TestMetaQueries;

/**
 * Date: 2013/01/28
 * Time: 7:18 PM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({OrderedSetTestTest.class})
public class BitsyAnyTest {

}
