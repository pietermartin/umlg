package org.tuml.blueprints.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.tuml.tinker.collectiontest.OrderedSetTest;
import org.tuml.tinker.collectiontest.SequenceTest;
import org.tuml.tinker.embeddedtest.TestEmbeddedTest;
import org.tuml.tinker.validationtest.TestValidation;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2013/01/28
 * Time: 7:18 PM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({SequenceTest.class, OrderedSetTest.class})
public class AnyTest {

}
