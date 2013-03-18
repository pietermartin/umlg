package org.tuml.transaction.test;

import junit.framework.Assert;
import org.junit.Test;
import org.tuml.concretetest.God;
import org.tuml.runtime.test.BaseLocalDbTest;

/**
 * Date: 2013/03/17
 * Time: 4:42 PM
 */
public class TumlIdTest extends BaseLocalDbTest {

    @Test
    public void testIdGeneration() {
        God g = new God(true);
        g.setName("god1");
        db.commit();
        Assert.assertEquals(Long.valueOf(1L), g.getId());
        g = new God(true);
        g.setName("god2");
        db.commit();
        Assert.assertEquals(Long.valueOf(2L), g.getId());
        Assert.assertEquals(Long.valueOf(2L), g.getMetaNode().getIdHigh());
    }
}
