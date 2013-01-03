package org.tuml.enumeration.test;

import com.tinkerpop.blueprints.TransactionalGraph;
import junit.framework.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.embeddedtest.REASON;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.memory.TumlMemorySet;
import org.tuml.runtime.test.BaseLocalDbTest;
import scala.actors.threadpool.Arrays;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
