package org.umlg.tests.collectiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.H;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/04/03
 * Time: 7:35 PM
 */
public class ManyToManyToSelfSequenceTest extends BaseLocalDbTest {

    @Test
    public void testManyToManySequenceToSelf() {
        H h1 = new H();
        H h2 = new H();
        H h3 = new H();

        h1.addToTo(h2);
        h1.addToTo(h3);
        db.commit();

        h1.reload();
        h2.reload();
        h3.reload();

        Assert.assertEquals(2, h1.getTo().size());
        Assert.assertEquals(1, h2.getFrom().size());
        Assert.assertEquals(0, h1.getFrom().size());

        H h4 = new H();
        h2.addToFrom(h4);
        db.commit();

        Assert.assertEquals(2, h1.getTo().size());
        Assert.assertEquals(2, h2.getFrom().size());
        Assert.assertEquals(1, h4.getTo().size());

        h1.reload();
        h2.reload();
        h3.reload();
        h4.reload();
        Assert.assertEquals(2, h1.getTo().size());
        Assert.assertEquals(2, h2.getFrom().size());
        Assert.assertEquals(1, h4.getTo().size());

    }

    @Test
    public void testManyToManySequenceToSelfIndex() {
        H h1 = new H();
        H h2 = new H();
        H h3 = new H();

        h1.addToTo(h2);
        h1.addToTo(h3);
        db.commit();

        h1.reload();
        h2.reload();
        h3.reload();

        Assert.assertEquals(2, h1.getTo().size());
        Assert.assertEquals(h2, h1.getTo().get(0));
        Assert.assertEquals(h3, h1.getTo().get(1));
        Assert.assertEquals(1, h2.getFrom().size());
        Assert.assertEquals(0, h1.getFrom().size());

        h1.reload();
        h2.reload();
        h3.reload();
        Assert.assertEquals(2, h1.getTo().size());
        Assert.assertEquals(1, h2.getFrom().size());
        Assert.assertEquals(1, h3.getFrom().size());
        Assert.assertEquals(0, h2.getTo().size());
        Assert.assertEquals(0, h3.getTo().size());

        h1.removeFromTo(h2);
        h1.addToTo(1, h2);
        db.commit();

        Assert.assertEquals(2, h1.getTo().size());
        Assert.assertEquals(h3, h1.getTo().get(0));
        Assert.assertEquals(h2, h1.getTo().get(1));

        h1.reload();
        h2.reload();
        h3.reload();

        Assert.assertEquals(2, h1.getTo().size());
        Assert.assertEquals(h3, h1.getTo().get(0));
        Assert.assertEquals(h2, h1.getTo().get(1));
    }

    @Test
    public void testManyToSelfWithDulicates() {
        H h1 = new H();
        H h2 = new H();
        H h3 = new H();
        H h4 = new H();

        h1.addToTo(h1);
        h1.addToTo(h2);
        h1.addToTo(h3);
        h1.addToTo(h4);
        db.commit();

        h1.reload();
        h2.reload();
        h3.reload();
        h4.reload();

        Assert.assertEquals(4, h1.getTo().size());
        Assert.assertEquals(h1, h1.getTo().get(0));
        Assert.assertEquals(h2, h1.getTo().get(1));
        Assert.assertEquals(h3, h1.getTo().get(2));
        Assert.assertEquals(h4, h1.getTo().get(3));

        Assert.assertEquals(0, h2.getTo().size());
        Assert.assertEquals(1, h2.getFrom().size());
        Assert.assertEquals(0, h3.getTo().size());
        Assert.assertEquals(1, h3.getFrom().size());
        Assert.assertEquals(0, h4.getTo().size());
        Assert.assertEquals(1, h4.getFrom().size());

        h2.addToTo(h1);
        h2.addToTo(h2);
        h2.addToTo(h3);
        h2.addToTo(h4);

        h3.addToTo(h1);
        h3.addToTo(h2);
        h3.addToTo(h3);
        h3.addToTo(h4);

        h4.addToTo(h1);
        h4.addToTo(h2);
        h4.addToTo(h3);
        h4.addToTo(h4);

        db.commit();

        h1.reload();
        h2.reload();
        h3.reload();
        h4.reload();

        Assert.assertEquals(4, h1.getTo().size());
        Assert.assertEquals(4, h2.getTo().size());
        Assert.assertEquals(4, h3.getTo().size());
        Assert.assertEquals(4, h4.getTo().size());

        Assert.assertEquals(4, h1.getFrom().size());
        Assert.assertEquals(4, h2.getFrom().size());
        Assert.assertEquals(4, h3.getFrom().size());
        Assert.assertEquals(4, h4.getFrom().size());

        h1.addToTo(h1);
        db.commit();

        h1.reload();
        h2.reload();
        h3.reload();
        h4.reload();

        Assert.assertEquals(5, h1.getTo().size());
        Assert.assertEquals(4, h2.getTo().size());
        Assert.assertEquals(4, h3.getTo().size());
        Assert.assertEquals(4, h4.getTo().size());

        Assert.assertEquals(5, h1.getFrom().size());
        Assert.assertEquals(4, h2.getFrom().size());
        Assert.assertEquals(4, h3.getFrom().size());
        Assert.assertEquals(4, h4.getFrom().size());

        h2.addToTo(0, h3);

        db.commit();

        h1.reload();
        h2.reload();
        h3.reload();
        h4.reload();

        Assert.assertEquals(5, h1.getTo().size());
        Assert.assertEquals(5, h2.getTo().size());
        Assert.assertEquals(4, h3.getTo().size());
        Assert.assertEquals(4, h4.getTo().size());

        Assert.assertEquals(5, h1.getFrom().size());
        Assert.assertEquals(4, h2.getFrom().size());
        Assert.assertEquals(5, h3.getFrom().size());
        Assert.assertEquals(4, h4.getFrom().size());

    }
}
