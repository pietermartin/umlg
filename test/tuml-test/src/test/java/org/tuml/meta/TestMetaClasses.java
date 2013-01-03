package org.tuml.meta;

import com.tinkerpop.blueprints.TransactionalGraph;
import junit.framework.Assert;
import org.junit.Test;
import org.tuml.concretetest.Angel;
import org.tuml.concretetest.God;
import org.tuml.concretetest.meta.AngelMeta;
import org.tuml.concretetest.meta.GodMeta;
import org.tuml.runtime.test.BaseLocalDbTest;

/**
 * Date: 2012/12/27
 * Time: 8:50 AM
 */
public class TestMetaClasses extends BaseLocalDbTest {

    @Test
    public void testMetaPersistence() {
        God g = new God(true);
        g.setName("g");
        Angel a = new Angel(g);
        db.commit();
        Assert.assertEquals(2, countVertices());
        GodMeta gm = (GodMeta) g.getMetaNode();
        AngelMeta am = (AngelMeta) a.getMetaNode();
        Assert.assertEquals(4, countVertices());
    }
}
