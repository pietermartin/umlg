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

//    @Test
    public void testIdGeneration() {
        God g = new God(true);
        g.setName("god1");
        db.commit();
        Assert.assertEquals("tumltest::org::tuml::concretetest::God::1", g.getId());
        g = new God(true);
        g.setName("god2");
        db.commit();
        Assert.assertEquals("tumltest::org::tuml::concretetest::God::2", g.getId());
        Assert.assertEquals(Long.valueOf(2L), g.getMetaNode().getIdHigh());
    }

//    @Test
    public void testIdGenerationWithRollback() {
        God g = new God(true);
        g.setName("god1");
        db.commit();
        Assert.assertEquals("tumltest::org::tuml::concretetest::God::1", g.getId());
        g = new God(true);
        g.setName("god2");
        Long id = g.getId();
        Assert.assertEquals("tumltest::org::tuml::concretetest::God::2", id);
        db.rollback();
        Assert.assertNull(db.instantiateClassifier(id));
        Assert.assertEquals(Long.valueOf(2L), g.getMetaNode().getIdHigh());
    }

}
