package org.umlg.ocl.qualifier.test;

import org.apache.commons.lang.time.StopWatch;
import org.apache.tinkerpop.gremlin.structure.Compare;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.qualifier.*;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.runtime.util.Pair;

public class TestQualifiers extends BaseLocalDbTest {

    @Test
    public void testQualifier1() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Bank bank = new Bank(true);
        bank.setName("BADASS");
        for (int i = 0; i < 1000; i++) {
            Customer customer1 = new Customer(true);
            customer1.setName("c" + Integer.toString(i));
            customer1.setAccountNumber(i);
            customer1.setBank(bank);
        }

        Customer john1 = new Customer(true);
        john1.setName("john");
        john1.setAccountNumber(1);
        john1.setBank(bank);

        db.commit();
        Assert.assertEquals(1001, new Bank(bank.getVertex()).getCustomer().size());
        Assert.assertNotNull(new Bank(bank.getVertex()).getCustomerForNameQualifierandAccountNumberQualifier(Pair.of(Compare.eq, "c1"), Pair.of(Compare.eq, 1)));
        Assert.assertNotNull(new Bank(bank.getVertex()).getCustomerJohn001());
        stopWatch.stop();
        System.out.println("Time taken = " + stopWatch.toString());
    }

    @Test
    public void testNestedQualifiers() {
        A a = new A();
        a.setName("a1");
        for (int i = 0; i < 10; i++) {
            B b = new B();
            b.setName("b" + i);
            a.addToB(b);
            for (int j = 0; j < 10; j++) {
                C c = new C();
                c.setName("c" + j);
                b.addToC(c);
                for (int k = 0; k < 10; k++) {
                    D d = new D();
                    d.setName("d" + k);
                    c.addToD(d);
                }
            }
        }

        B john = new B();
        john.setName("john");
        a.addToB(john);
        C cc1 = new C();
        cc1.setName("cc1");
        john.addToC(cc1);
        C cc2 = new C();
        cc1.setName("cc2");
        john.addToC(cc2);

        D dd1 = new D();
        dd1.setName("dd1");
        cc1.addToD(dd1);
        D dd2 = new D();
        dd2.setName("dd2");
        cc1.addToD(dd2);

        D dd3 = new D();
        dd3.setName("joe");
        cc2.addToD(dd3);
        db.commit();
        Assert.assertEquals(1003, D.allInstances().size());

        UmlgSet<C> result = a.getTestOclQualifier();
        Assert.assertEquals(1, result.size());

    }

}
