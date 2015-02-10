package org.umlg.tests.query;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.Dream;
import org.umlg.collectiontest.meta.DreamMeta;
import org.umlg.concretetest.God;
import org.umlg.meta.ClassQuery;
import org.umlg.query.QueryEnum;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2012/12/25
 * Time: 5:54 PM
 */
public class TestMetaQueries extends BaseLocalDbTest {

    @Test
    public void testClassQuery() {
        God g = new God(true);
        g.setName("god1");
        Dream d1 = new Dream(g);
        d1.setName("d1");
        Dream d2 = new Dream(g);
        d2.setName("d2");
        Dream d3 = new Dream(g);
        d3.setName("d3");
        Dream d4 = new Dream(g);
        d4.setName("d4");
        db.commit();
        Assert.assertEquals(5, countVertices());
        Assert.assertEquals(5, countEdges());

        ClassQuery q1 = new ClassQuery(true);
        q1.setName("q1");
        q1.setDescription("q1Desc");
        q1.setQueryEnum(QueryEnum.OCL);
        q1.setQueryString("self.g.dream");
        DreamMeta.getInstance().addToClassQuery(q1);

        db.commit();
        //One for the enum also
        Assert.assertEquals(6, countVertices());
        Assert.assertEquals(9, countEdges());

        ClassQuery q2 = new ClassQuery(true);
        q2.setName("q2");
        q2.setDescription("q2Desc");
        q2.setQueryEnum(QueryEnum.OCL);
        q2.setQueryString("self.g.dream");
        DreamMeta.getInstance().addToClassQuery(q2);

        db.commit();
        //One for the enum also
        Assert.assertEquals(8, countVertices());
        Assert.assertEquals(11, countEdges());

        Assert.assertEquals(2, DreamMeta.getInstance().getClassQuery().size());

    }
}
