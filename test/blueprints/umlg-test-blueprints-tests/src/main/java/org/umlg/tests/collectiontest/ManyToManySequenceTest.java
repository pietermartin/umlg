package org.umlg.tests.collectiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.F;
import org.umlg.collectiontest.G;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/03/31
 * Time: 7:41 PM
 */
public class ManyToManySequenceTest extends BaseLocalDbTest {

    @Test
    public void testManyToManySequence1() {
        F f1 = new F();
        G g1 = new G();
        G g2 = new G();
        G g3 = new G();
        f1.addToG(g1);
        f1.addToG(g2);
        f1.addToG(g3);
        db.commit();
        f1.reload();
        g1.reload();
        g2.reload();
        g3.reload();

        Assert.assertEquals(3, f1.getG().size());
        Assert.assertEquals(g1, f1.getG().get(0));
        Assert.assertEquals(g2, f1.getG().get(1));
        Assert.assertEquals(g3, f1.getG().get(2));

        Assert.assertEquals(1, g1.getF().size());
        Assert.assertEquals(1, g2.getF().size());
        Assert.assertEquals(1, g3.getF().size());
        Assert.assertEquals(f1, g1.getF().get(0));
        Assert.assertEquals(f1, g2.getF().get(0));
        Assert.assertEquals(f1, g3.getF().get(0));

        F f2 = new F();
        g1.addToF(f2);
        db.commit();
        f1.reload();
        f2.reload();
        g1.reload();
        g2.reload();
        g3.reload();

        Assert.assertEquals(2, g1.getF().size());
        Assert.assertEquals(f1, g1.getF().get(0));
        Assert.assertEquals(f2, g1.getF().get(1));

        Assert.assertEquals(3, f1.getG().size());
        Assert.assertEquals(g1, f1.getG().get(0));
        Assert.assertEquals(g2, f1.getG().get(1));
        Assert.assertEquals(g3, f1.getG().get(2));

        Assert.assertEquals(1, g2.getF().size());
        Assert.assertEquals(1, g3.getF().size());
        Assert.assertEquals(f1, g2.getF().get(0));
        Assert.assertEquals(f1, g3.getF().get(0));

    }

    @Test
    public void testManyToManySequence2() {
        F f1 = new F();
        F f2 = new F();
        F f3 = new F();
        F f4 = new F();
        F f5 = new F();
        G g1 = new G();
        G g2 = new G();
        G g3 = new G();
        G g4 = new G();
        G g5 = new G();

        f1.addToG(g1);
        f1.addToG(g2);
        f1.addToG(g3);

        f2.addToG(g2);
        f2.addToG(g3);
        f2.addToG(g4);

        f3.addToG(g3);
        f3.addToG(g4);
        f3.addToG(g5);

        g1.addToF(f4);
        g1.addToF(f5);

        g5.addToF(f4);
        g5.addToF(f5);

        db.commit();
        f1.reload();
        f2.reload();
        f3.reload();
        f4.reload();
        f5.reload();
        g1.reload();
        g2.reload();
        g3.reload();
        g4.reload();
        g5.reload();

        Assert.assertEquals(3, f1.getG().size());
        Assert.assertEquals(3, f2.getG().size());
        Assert.assertEquals(3, f3.getG().size());
        Assert.assertEquals(2, f4.getG().size());
        Assert.assertEquals(2, f5.getG().size());

        Assert.assertEquals(g1, f1.getG().get(0));
        Assert.assertEquals(g2, f1.getG().get(1));
        Assert.assertEquals(g3, f1.getG().get(2));

        Assert.assertEquals(g2, f2.getG().get(0));
        Assert.assertEquals(g3, f2.getG().get(1));
        Assert.assertEquals(g4, f2.getG().get(2));

        Assert.assertEquals(g3, f3.getG().get(0));
        Assert.assertEquals(g4, f3.getG().get(1));
        Assert.assertEquals(g5, f3.getG().get(2));

        Assert.assertEquals(g1, f4.getG().get(0));
        Assert.assertEquals(g5, f4.getG().get(1));

        Assert.assertEquals(g1, f5.getG().get(0));
        Assert.assertEquals(g5, f5.getG().get(1));

        Assert.assertEquals(3, g1.getF().size());
        Assert.assertEquals(2, g2.getF().size());
        Assert.assertEquals(3, g3.getF().size());
        Assert.assertEquals(2, g4.getF().size());
        Assert.assertEquals(3, g5.getF().size());

        Assert.assertEquals(f1, g1.getF().get(0));
        Assert.assertEquals(f4, g1.getF().get(1));
        Assert.assertEquals(f5, g1.getF().get(2));

        Assert.assertEquals(f1, g2.getF().get(0));
        Assert.assertEquals(f2, g2.getF().get(1));

        Assert.assertEquals(f1, g3.getF().get(0));
        Assert.assertEquals(f2, g3.getF().get(1));
        Assert.assertEquals(f3, g3.getF().get(2));

        Assert.assertEquals(f2, g4.getF().get(0));
        Assert.assertEquals(f3, g4.getF().get(1));

        Assert.assertEquals(f3, g5.getF().get(0));
        Assert.assertEquals(f4, g5.getF().get(1));
        Assert.assertEquals(f5, g5.getF().get(2));

    }

    @Test
    public void testManyToManySequence1WithoutReload() {
        F f1 = new F();
        G g1 = new G();
        G g2 = new G();
        G g3 = new G();
        f1.addToG(g1);
        f1.addToG(g2);
        f1.addToG(g3);
        db.commit();

        Assert.assertEquals(3, f1.getG().size());
        Assert.assertEquals(g1, f1.getG().get(0));
        Assert.assertEquals(g2, f1.getG().get(1));
        Assert.assertEquals(g3, f1.getG().get(2));

        Assert.assertEquals(1, g1.getF().size());
        Assert.assertEquals(1, g2.getF().size());
        Assert.assertEquals(1, g3.getF().size());
        Assert.assertEquals(f1, g1.getF().get(0));
        Assert.assertEquals(f1, g2.getF().get(0));
        Assert.assertEquals(f1, g3.getF().get(0));

        F f2 = new F();
        g1.addToF(f2);
        db.commit();

        Assert.assertEquals(2, g1.getF().size());
        Assert.assertEquals(f1, g1.getF().get(0));
        Assert.assertEquals(f2, g1.getF().get(1));

        Assert.assertEquals(3, f1.getG().size());
        Assert.assertEquals(g1, f1.getG().get(0));
        Assert.assertEquals(g2, f1.getG().get(1));
        Assert.assertEquals(g3, f1.getG().get(2));

        Assert.assertEquals(1, g2.getF().size());
        Assert.assertEquals(1, g3.getF().size());
        Assert.assertEquals(f1, g2.getF().get(0));
        Assert.assertEquals(f1, g3.getF().get(0));

    }

    @Test
    public void testManyToManySequence2WithoutReload() {
        F f1 = new F();
        F f2 = new F();
        F f3 = new F();
        F f4 = new F();
        F f5 = new F();
        G g1 = new G();
        G g2 = new G();
        G g3 = new G();
        G g4 = new G();
        G g5 = new G();

        f1.addToG(g1);
        f1.addToG(g2);
        f1.addToG(g3);

        f2.addToG(g2);
        f2.addToG(g3);
        f2.addToG(g4);

        f3.addToG(g3);
        f3.addToG(g4);
        f3.addToG(g5);

        g1.addToF(f4);
        g1.addToF(f5);

        g5.addToF(f4);
        g5.addToF(f5);

        db.commit();

        Assert.assertEquals(3, f1.getG().size());
        Assert.assertEquals(3, f2.getG().size());
        Assert.assertEquals(3, f3.getG().size());
        Assert.assertEquals(2, f4.getG().size());
        Assert.assertEquals(2, f5.getG().size());

        Assert.assertEquals(g1, f1.getG().get(0));
        Assert.assertEquals(g2, f1.getG().get(1));
        Assert.assertEquals(g3, f1.getG().get(2));

        Assert.assertEquals(g2, f2.getG().get(0));
        Assert.assertEquals(g3, f2.getG().get(1));
        Assert.assertEquals(g4, f2.getG().get(2));

        Assert.assertEquals(g3, f3.getG().get(0));
        Assert.assertEquals(g4, f3.getG().get(1));
        Assert.assertEquals(g5, f3.getG().get(2));

        Assert.assertEquals(g1, f4.getG().get(0));
        Assert.assertEquals(g5, f4.getG().get(1));

        Assert.assertEquals(g1, f5.getG().get(0));
        Assert.assertEquals(g5, f5.getG().get(1));

        Assert.assertEquals(3, g1.getF().size());
        Assert.assertEquals(2, g2.getF().size());
        Assert.assertEquals(3, g3.getF().size());
        Assert.assertEquals(2, g4.getF().size());
        Assert.assertEquals(3, g5.getF().size());

        Assert.assertEquals(f1, g1.getF().get(0));
        Assert.assertEquals(f4, g1.getF().get(1));
        Assert.assertEquals(f5, g1.getF().get(2));

        Assert.assertEquals(f1, g2.getF().get(0));
        Assert.assertEquals(f2, g2.getF().get(1));

        Assert.assertEquals(f1, g3.getF().get(0));
        Assert.assertEquals(f2, g3.getF().get(1));
        Assert.assertEquals(f3, g3.getF().get(2));

        Assert.assertEquals(f2, g4.getF().get(0));
        Assert.assertEquals(f3, g4.getF().get(1));

        Assert.assertEquals(f3, g5.getF().get(0));
        Assert.assertEquals(f4, g5.getF().get(1));
        Assert.assertEquals(f5, g5.getF().get(2));

    }

    @Test
    public void testManyToManySequenceIndexedSetter() {
        F f1 = new F();
        G g1 = new G();
        G g2 = new G();
        G g3 = new G();
        f1.addToG(g1);
        f1.addToG(g2);
        f1.addToG(g3);

        G g4 = new G();
        G g5 = new G();
        G g6 = new G();
        f1.addToG(0, g4);
        f1.addToG(0, g5);
        f1.addToG(0, g6);
        db.commit();

        f1.reload();
        g1.reload();
        g2.reload();
        g3.reload();
        g4.reload();
        g5.reload();
        g6.reload();

        Assert.assertEquals(6, f1.getG().size());
        Assert.assertEquals(g6, f1.getG().get(0));
        Assert.assertEquals(g5, f1.getG().get(1));
        Assert.assertEquals(g4, f1.getG().get(2));

        Assert.assertEquals(g1, f1.getG().get(3));
        Assert.assertEquals(g2, f1.getG().get(4));
        Assert.assertEquals(g3, f1.getG().get(5));

        F f2 = new F();
        F f3 = new F();
        F f4 = new F();

        g1.addToF(f2);
        g1.addToF(1, f3);
        g1.addToF(1, f4);

        db.commit();
        f1.reload();
        f2.reload();
        f3.reload();
        f4.reload();
        g1.reload();
        g2.reload();
        g3.reload();
        g4.reload();
        g5.reload();
        g6.reload();

        Assert.assertEquals(4, g1.getF().size());
        Assert.assertEquals(f1, g1.getF().get(0));
        Assert.assertEquals(f4, g1.getF().get(1));
        Assert.assertEquals(f3, g1.getF().get(2));
        Assert.assertEquals(f2, g1.getF().get(3));

    }
}
