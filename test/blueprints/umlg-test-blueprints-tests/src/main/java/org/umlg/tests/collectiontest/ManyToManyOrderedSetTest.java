package org.umlg.tests.collectiontest;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.collectiontest.A;
import org.umlg.collectiontest.B;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.util.Iterator;

/**
 * Date: 2014/03/29
 * Time: 5:25 PM
 */
public class ManyToManyOrderedSetTest extends BaseLocalDbTest {

    @Test
    public void testManyToMany1() {
        A a1 = new A();
        a1.setName("a1");
        B b1 = new B();
        b1.setName("b1");
        a1.addToB(b1);
        db.commit();

        a1.reload();
        Assert.assertEquals(1, a1.getB().size());
        Assert.assertEquals(b1, a1.getB().iterator().next());
        b1.reload();
        Assert.assertEquals(1, b1.getA().size());
        Assert.assertEquals(a1, b1.getA().iterator().next());

        B b2 = new B();
        b2.setName("b2");
        a1.addToB(b2);
        db.commit();

        a1.reload();
        Assert.assertEquals(2, a1.getB().size());
        Iterator<B> iterator = a1.getB().iterator();
        Assert.assertEquals(b1, iterator.next());
        Assert.assertEquals(b2, iterator.next());

        A a2 = new A();
        a2.setName("a2");
        b1.addToA(a2);
        db.commit();

        a1.reload();
        Assert.assertEquals(2, a1.getB().size());
        Iterator<B> iterator1 = a1.getB().iterator();
        Assert.assertEquals(b1, iterator1.next());
        Assert.assertEquals(b2, iterator1.next());

        b1.reload();
        Assert.assertEquals(2, b1.getA().size());
        Iterator<A> iterator2 = b1.getA().iterator();
        Assert.assertEquals(a1, iterator2.next());
        Assert.assertEquals(a2, iterator2.next());

        b2.addToA(a2);
        db.commit();

        a1.reload();
        a2.reload();
        b1.reload();
        b2.reload();
        Assert.assertEquals(2, a1.getB().size());
        Assert.assertEquals(2, a2.getB().size());
        Assert.assertEquals(2, b1.getA().size());
        Assert.assertEquals(2, b2.getA().size());

    }

    @Test
    public void testManytoMany2() {
        A a1 = new A();
        a1.setName("a1");
        A a2 = new A();
        a2.setName("a2");
        B b1 = new B();
        b1.setName("b1");
        B b2 = new B();
        b2.setName("b2");

        a1.addToB(b1);
        a2.addToB(b1);

        db.commit();

        a1.reload();
        a2.reload();
        b1.reload();
        b2.reload();
        Assert.assertEquals(1, a1.getB().size());
        Assert.assertEquals(1, a2.getB().size());
        Assert.assertEquals(2, b1.getA().size());

        a1.addToB(b2);
        db.commit();
        a1.reload();
        a2.reload();
        b1.reload();
        b2.reload();
        Assert.assertEquals(2, a1.getB().size());
        Assert.assertEquals(1, a2.getB().size());
        Assert.assertEquals(2, b1.getA().size());
        Assert.assertEquals(1, b2.getA().size());

        a2.addToB(b2);
        db.commit();
        a1.reload();
        a2.reload();
        b1.reload();
        b2.reload();
        Assert.assertEquals(2, a1.getB().size());
        Assert.assertEquals(2, a2.getB().size());
        Assert.assertEquals(2, b1.getA().size());
        Assert.assertEquals(2, b2.getA().size());
    }

    @Test
    public void testManyToMany3() {
        A a1 = new A();
        a1.setName("a1");
        A a2 = new A();
        a2.setName("a2");
        A a3 = new A();
        a3.setName("a3");
        A a4 = new A();
        a4.setName("a4");
        A a5 = new A();
        a5.setName("a5");
        A a6 = new A();
        a6.setName("a6");
        A a7 = new A();
        a7.setName("a7");
        A a8 = new A();
        a8.setName("a8");
        A a9 = new A();
        a9.setName("a9");
        A a10 = new A();
        a10.setName("a10");

        B b1 = new B();
        b1.setName("b1");
        B b2 = new B();
        b2.setName("b2");
        B b3 = new B();
        b3.setName("b3");
        B b4 = new B();
        b4.setName("b4");
        B b5 = new B();
        b5.setName("b5");
        B b6 = new B();
        b6.setName("b6");
        B b7 = new B();
        b7.setName("b7");
        B b8 = new B();
        b8.setName("b8");
        B b9 = new B();
        b9.setName("b9");
        B b10 = new B();
        b10.setName("b10");

        b1.addToA(a1);
        b1.addToA(a2);
        b1.addToA(a3);
        b1.addToA(a4);
        b1.addToA(a5);
        b1.addToA(a6);
        b1.addToA(a7);
        b1.addToA(a8);
        b1.addToA(a9);
        b1.addToA(a10);

        b2.addToA(a1);
        b2.addToA(a2);
        b2.addToA(a3);
        b2.addToA(a4);
        b2.addToA(a5);
        b2.addToA(a6);
        b2.addToA(a7);
        b2.addToA(a8);
        b2.addToA(a9);
        b2.addToA(a10);

        b3.addToA(a1);
        b3.addToA(a2);
        b3.addToA(a3);
        b3.addToA(a4);
        b3.addToA(a5);
        b3.addToA(a6);
        b3.addToA(a7);
        b3.addToA(a8);
        b3.addToA(a9);
        b3.addToA(a10);

        b4.addToA(a1);
        b4.addToA(a2);
        b4.addToA(a3);
        b4.addToA(a4);
        b4.addToA(a5);
        b4.addToA(a6);
        b4.addToA(a7);
        b4.addToA(a8);
        b4.addToA(a9);
        b4.addToA(a10);

        b5.addToA(a1);
        b5.addToA(a2);
        b5.addToA(a3);
        b5.addToA(a4);
        b5.addToA(a5);
        b5.addToA(a6);
        b5.addToA(a7);
        b5.addToA(a8);
        b5.addToA(a9);
        b5.addToA(a10);

        db.commit();

        a1.reload();
        a2.reload();
        a3.reload();
        a4.reload();
        a5.reload();
        a6.reload();
        a7.reload();
        a8.reload();
        a9.reload();
        a10.reload();

        b1.reload();
        b2.reload();
        b3.reload();
        b4.reload();
        b5.reload();
        b6.reload();
        b7.reload();
        b8.reload();
        b9.reload();
        b10.reload();

        Assert.assertEquals(5, a1.getB().size());
        Assert.assertEquals(5, a2.getB().size());
        Assert.assertEquals(5, a3.getB().size());
        Assert.assertEquals(5, a4.getB().size());
        Assert.assertEquals(5, a5.getB().size());
        Assert.assertEquals(5, a6.getB().size());
        Assert.assertEquals(5, a7.getB().size());
        Assert.assertEquals(5, a8.getB().size());
        Assert.assertEquals(5, a9.getB().size());
        Assert.assertEquals(5, a10.getB().size());

        Assert.assertEquals(10, b1.getA().size());
        Assert.assertEquals(10, b2.getA().size());
        Assert.assertEquals(10, b3.getA().size());
        Assert.assertEquals(10, b4.getA().size());
        Assert.assertEquals(10, b5.getA().size());
        Assert.assertEquals(0, b6.getA().size());
        Assert.assertEquals(0, b7.getA().size());
        Assert.assertEquals(0, b8.getA().size());
        Assert.assertEquals(0, b9.getA().size());
        Assert.assertEquals(0, b10.getA().size());

    }

    @Test
    public void testManyToManyRemove1() {

        A a1 = new A();
        a1.setName("a1");
        B b1 = new B();
        b1.setName("b1");
        B b2 = new B();
        b2.setName("b2");
        a1.addToB(b1);
        a1.addToB(b2);
        db.commit();
        a1.reload();
        Assert.assertEquals(2, a1.getB().size());
        Assert.assertEquals(b1, a1.getB().get(0));
        Assert.assertEquals(b2, a1.getB().get(1));

        a1.removeFromB(b2);
        db.commit();
        a1.reload();
        Assert.assertEquals(1, a1.getB().size());
        Assert.assertEquals(b1, a1.getB().get(0));

        a1.removeFromB(b1);
        db.commit();
        a1.reload();
        Assert.assertEquals(0, a1.getB().size());

    }

    @Test
    public void testManyToManyRemove2() {

        A a1 = new A();
        a1.setName("a1");
        A a2 = new A();
        a2.setName("a2");
        A a3 = new A();
        a3.setName("a3");
        A a4 = new A();
        a4.setName("a4");
        A a5 = new A();
        a5.setName("a5");
        B b1 = new B();
        b1.setName("b1");
        B b2 = new B();
        b2.setName("b2");
        B b3 = new B();
        b3.setName("b3");
        B b4 = new B();
        b4.setName("b4");
        B b5 = new B();
        b5.setName("b5");

        a1.addToB(b1);
        a1.addToB(b2);
        a1.addToB(b3);
        a1.addToB(b4);
        a1.addToB(b5);

        a2.addToB(b1);
        a2.addToB(b2);
        a2.addToB(b3);
        a2.addToB(b4);
        a2.addToB(b5);

        a3.addToB(b1);
        a3.addToB(b2);
        a3.addToB(b3);
        a3.addToB(b4);
        a3.addToB(b5);

        a4.addToB(b1);
        a4.addToB(b2);
        a4.addToB(b3);
        a4.addToB(b4);
        a4.addToB(b5);

        a5.addToB(b1);
        a5.addToB(b2);
        a5.addToB(b3);
        a5.addToB(b4);
        a5.addToB(b5);

        db.commit();

        a1.reload();
        a2.reload();
        a3.reload();
        a4.reload();
        a5.reload();

        b1.reload();
        b2.reload();
        b3.reload();
        b4.reload();
        b5.reload();

        Assert.assertEquals(5, a1.getB().size());
        Assert.assertEquals(5, a2.getB().size());
        Assert.assertEquals(5, a3.getB().size());
        Assert.assertEquals(5, a4.getB().size());
        Assert.assertEquals(5, a5.getB().size());

        Assert.assertEquals(5, b1.getA().size());
        Assert.assertEquals(5, b2.getA().size());
        Assert.assertEquals(5, b3.getA().size());
        Assert.assertEquals(5, b4.getA().size());
        Assert.assertEquals(5, b5.getA().size());

        a1.removeFromB(b3);
        db.commit();

        a1.reload();
        a2.reload();
        a3.reload();
        a4.reload();
        a5.reload();

        b1.reload();
        b2.reload();
        b3.reload();
        b4.reload();
        b5.reload();

        Assert.assertEquals(4, a1.getB().size());
        Assert.assertEquals(5, a2.getB().size());
        Assert.assertEquals(5, a3.getB().size());
        Assert.assertEquals(5, a4.getB().size());
        Assert.assertEquals(5, a5.getB().size());

        Assert.assertEquals(5, b1.getA().size());
        Assert.assertEquals(5, b2.getA().size());
        Assert.assertEquals(4, b3.getA().size());
        Assert.assertEquals(5, b4.getA().size());
        Assert.assertEquals(5, b5.getA().size());

        b3.removeFromA(a5);
        db.commit();

        a1.reload();
        a2.reload();
        a3.reload();
        a4.reload();
        a5.reload();

        b1.reload();
        b2.reload();
        b3.reload();
        b4.reload();
        b5.reload();

        Assert.assertEquals(4, a1.getB().size());
        Assert.assertEquals(5, a2.getB().size());
        Assert.assertEquals(5, a3.getB().size());
        Assert.assertEquals(5, a4.getB().size());
        Assert.assertEquals(4, a5.getB().size());

        Assert.assertEquals(5, b1.getA().size());
        Assert.assertEquals(5, b2.getA().size());
        Assert.assertEquals(3, b3.getA().size());
        Assert.assertEquals(5, b4.getA().size());
        Assert.assertEquals(5, b5.getA().size());

        b5.removeFromA(a5);
        db.commit();

        a1.reload();
        a2.reload();
        a3.reload();
        a4.reload();
        a5.reload();

        b1.reload();
        b2.reload();
        b3.reload();
        b4.reload();
        b5.reload();

        Assert.assertEquals(4, a1.getB().size());
        Assert.assertEquals(5, a2.getB().size());
        Assert.assertEquals(5, a3.getB().size());
        Assert.assertEquals(5, a4.getB().size());
        Assert.assertEquals(3, a5.getB().size());

        Assert.assertEquals(5, b1.getA().size());
        Assert.assertEquals(5, b2.getA().size());
        Assert.assertEquals(3, b3.getA().size());
        Assert.assertEquals(5, b4.getA().size());
        Assert.assertEquals(4, b5.getA().size());

        b1.removeFromA(a1);
        db.commit();

        a1.reload();
        a2.reload();
        a3.reload();
        a4.reload();
        a5.reload();

        b1.reload();
        b2.reload();
        b3.reload();
        b4.reload();
        b5.reload();

        Assert.assertEquals(3, a1.getB().size());
        Assert.assertEquals(5, a2.getB().size());
        Assert.assertEquals(5, a3.getB().size());
        Assert.assertEquals(5, a4.getB().size());
        Assert.assertEquals(3, a5.getB().size());

        Assert.assertEquals(4, b1.getA().size());
        Assert.assertEquals(5, b2.getA().size());
        Assert.assertEquals(3, b3.getA().size());
        Assert.assertEquals(5, b4.getA().size());
        Assert.assertEquals(4, b5.getA().size());

        a1.removeFromB(b2);
        a1.removeFromB(b4);
        a1.removeFromB(b5);
        db.commit();

        a1.reload();
        a2.reload();
        a3.reload();
        a4.reload();
        a5.reload();

        b1.reload();
        b2.reload();
        b3.reload();
        b4.reload();
        b5.reload();

        Assert.assertEquals(0, a1.getB().size());
        Assert.assertEquals(5, a2.getB().size());
        Assert.assertEquals(5, a3.getB().size());
        Assert.assertEquals(5, a4.getB().size());
        Assert.assertEquals(3, a5.getB().size());

        Assert.assertEquals(4, b1.getA().size());
        Assert.assertEquals(4, b2.getA().size());
        Assert.assertEquals(3, b3.getA().size());
        Assert.assertEquals(4, b4.getA().size());
        Assert.assertEquals(3, b5.getA().size());
    }

    @Test
    public void testManyToManyOrderedSetIndexAt() {
        A a1 = new A();
        a1.setName("a1");

        B b1 = new B();
        b1.setName("b1");
        B b2 = new B();
        b2.setName("b2");
        B b3 = new B();
        b3.setName("b3");

        a1.addToB(0, b1);
        a1.addToB(1, b2);
        a1.addToB(2, b3);

        db.commit();

        a1.reload();
        b1.reload();
        b2.reload();
        b3.reload();

        Assert.assertEquals(3, a1.getB().size());
        Assert.assertEquals(b1, a1.getB().get(0));
        Assert.assertEquals(b2, a1.getB().get(1));
        Assert.assertEquals(b3, a1.getB().get(2));

        Assert.assertEquals(1, b1.getA().size());
        Assert.assertEquals(1, b2.getA().size());
        Assert.assertEquals(1, b2.getA().size());
        Assert.assertEquals(a1, b1.getA().get(0));
        Assert.assertEquals(a1, b2.getA().get(0));
        Assert.assertEquals(a1, b3.getA().get(0));

        A a2 = new A();
        a2.setName("a2");

        b1.addToA(0, a2);
        b2.addToA(0, a2);
        b3.addToA(0, a2);
        db.commit();

        a1.reload();
        a2.reload();
        b1.reload();
        b2.reload();
        b3.reload();

        Assert.assertEquals(2, b1.getA().size());
        Assert.assertEquals(2, b2.getA().size());
        Assert.assertEquals(2, b3.getA().size());
        Assert.assertEquals(a2, b1.getA().get(0));
        Assert.assertEquals(a2, b2.getA().get(0));
        Assert.assertEquals(a2, b3.getA().get(0));
        Assert.assertEquals(a1, b1.getA().get(1));
        Assert.assertEquals(a1, b2.getA().get(1));
        Assert.assertEquals(a1, b3.getA().get(1));

        Assert.assertEquals(b1, a2.getB().get(0));
        Assert.assertEquals(b2, a2.getB().get(1));
        Assert.assertEquals(b3, a2.getB().get(2));

        Assert.assertEquals(b1, a1.getB().get(0));
        Assert.assertEquals(b2, a1.getB().get(1));
        Assert.assertEquals(b3, a1.getB().get(2));

        A a3 = new A();
        a3.setName("a3");

        b1.addToA(0, a3);
        b2.addToA(0, a3);
        b3.addToA(0, a3);
        db.commit();

        a1.reload();
        a2.reload();
        a3.reload();
        b1.reload();
        b2.reload();
        b3.reload();

        Assert.assertEquals(3, a1.getB().size());
        Assert.assertEquals(3, a2.getB().size());
        Assert.assertEquals(3, a3.getB().size());

        Assert.assertEquals(b1, a1.getB().get(0));
        Assert.assertEquals(b2, a1.getB().get(1));
        Assert.assertEquals(b3, a1.getB().get(2));

        Assert.assertEquals(b1, a2.getB().get(0));
        Assert.assertEquals(b2, a2.getB().get(1));
        Assert.assertEquals(b3, a2.getB().get(2));

        Assert.assertEquals(b1, a3.getB().get(0));
        Assert.assertEquals(b2, a3.getB().get(1));
        Assert.assertEquals(b3, a3.getB().get(2));

        Assert.assertEquals(a3, b1.getA().get(0));
        Assert.assertEquals(a2, b1.getA().get(1));
        Assert.assertEquals(a1, b1.getA().get(2));

        Assert.assertEquals(a3, b2.getA().get(0));
        Assert.assertEquals(a2, b2.getA().get(1));
        Assert.assertEquals(a1, b2.getA().get(2));

        Assert.assertEquals(a3, b3.getA().get(0));
        Assert.assertEquals(a2, b3.getA().get(1));
        Assert.assertEquals(a1, b3.getA().get(2));

        Assert.assertEquals(b1, a1.getB().get(0));
        a1.removeFromB(b3);
        a1.addToB(0, b3);
        db.commit();

        a1.reload();
        a2.reload();
        a3.reload();
        b1.reload();
        b2.reload();
        b3.reload();

        Assert.assertEquals(3, a1.getB().size());
        Assert.assertEquals(b3, a1.getB().get(0));
        Assert.assertEquals(b1, a1.getB().get(1));
        Assert.assertEquals(b2, a1.getB().get(2));

        Assert.assertEquals(3, b3.getA().size());
        Assert.assertEquals(a1, b3.getA().get(2));
    }

    @Test
    public void test1() {
        A a1 = new A();
        a1.setName("a1");
        B b1 = new B();
        b1.setName("b1");
        a1.addToB(b1);
        db.commit();
        a1.reload();
        b1.reload();
        Assert.assertEquals(1, b1.getA().size());
    }

    @Test
    public void testAddIgnoreInverse() {
        A a1 = new A();
        a1.setName("a1");
        B b1 = new B();
        b1.setName("b1");
        a1.addToBIgnoreInverse(b1);
        db.commit();
        a1.reload();
        b1.reload();
        Assert.assertEquals(b1, a1.getB().first());
    }

    @Test
    public void testAddIgnoreInverseInverse() {
        A a1 = new A();
        a1.setName("a1");
        B b1 = new B();
        b1.setName("b1");
        b1.addToAIgnoreInverse(a1);
        db.commit();
        a1.reload();
        b1.reload();
        Assert.assertEquals(a1, b1.getA().first());
    }

}
