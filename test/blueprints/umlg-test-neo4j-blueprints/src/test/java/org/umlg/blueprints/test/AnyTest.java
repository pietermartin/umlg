package org.umlg.blueprints.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.umlg.gremlin.TestGremlinExecutor;
import org.umlg.tinker.allinstances.AllInstancesTest;
import org.umlg.tinker.collectiontest.OclStdLibBagTest;
import org.umlg.tinker.collectiontest.OrderedSetTestTest;
import org.umlg.tinker.collectiontest.SequenceTest;
import org.umlg.tinker.concretetest.TestOneToMany;
import org.umlg.tinker.interfacetest.ManyToManyInverseTest;
import org.umlg.tinker.validationtest.TestValidation;

/**
 * Date: 2013/01/28
 * Time: 7:18 PM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestValidation.class})
public class AnyTest {

}
