package org.tuml.enumeration.test;

import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.embeddedtest.REASON;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.memory.TumlMemorySet;
import org.tuml.runtime.test.BaseLocalDbTest;

import java.util.Arrays;
import java.util.Collection;

/**
 * Date: 2012/12/17
 * Time: 12:24 PM
 */
public class ManyEnumerationTest extends BaseLocalDbTest {

    @Test(expected = IllegalStateException.class)
    public void testManyEnumeration() {
        God g = new God(true);
        g.setName("g");
        Object s = Arrays.asList(new Object[]{"asda", "asd"});
        TinkerSet<REASON> reasons = new TumlMemorySet<REASON>((Collection<REASON>) s);
        g.addToREASON(reasons);
        db.commit();
    }

}
