package org.umlg.quickpreview.query;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.util.Pipeline;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.multiplicity.Account;
import org.umlg.multiplicity.Customer;
import org.umlg.multiplicity.Purchase;
import org.umlg.quickpreview.BaseTest;
import org.umlg.runtime.adaptor.UmlgQueryEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Date: 2014/06/03
 * Time: 9:53 PM
 */
public class TestQuery extends BaseTest {

    @Test
    public void testOcl() {
        Customer customer = new Customer();
        customer.setName("john");

        Purchase car1 = new Purchase();
        car1.setName("bmw");
        customer.addToPurchase(car1);

        Purchase car2 = new Purchase();
        car2.setName("merc");
        customer.addToPurchase(car2);

        Account account1 = new Account();
        account1.setBalance(1000D);
        customer.addToAccount(account1);

        Account account2 = new Account();
        account2.setBalance(10D);
        customer.addToAccount(account2);

        db.commit();

        //derived property largeAccounts is generated as a getter on the entity
        Assert.assertEquals(1, customer.getLargeAccounts().size());

        Set<Purchase> cars = db.executeQuery(
                UmlgQueryEnum.OCL,
                customer,
                "self.purchase"
        );
        Assert.assertEquals(2, cars.size());

        Set<Account> large = db.executeQuery(
                UmlgQueryEnum.OCL,
                customer,
                "self.account->select(balance > 100)"
        );
        Assert.assertEquals(1, large.size());
        Assert.assertEquals(1000D, large.iterator().next().getBalance(), 0);

        Set<Account> small = db.executeQuery(
                UmlgQueryEnum.OCL,
                customer,
                "self.account->select(balance < 100)"
        );
        Assert.assertEquals(1, small.size());
        Assert.assertEquals(10D, small.iterator().next().getBalance(), 0);
    }

    @Test
    public void testGremlin() {
        Customer customer = new Customer();
        customer.setName("john");

        Purchase car1 = new Purchase();
        car1.setName("bmw");
        customer.addToPurchase(car1);

        Purchase car2 = new Purchase();
        car2.setName("merc");
        customer.addToPurchase(car2);

        Account account1 = new Account();
        account1.setBalance(1000D);
        customer.addToAccount(account1);

        Account account2 = new Account();
        account2.setBalance(10D);
        customer.addToAccount(account2);

        db.commit();

        //'self' translates to g.V(id) where id is the customers id
        Assert.assertEquals("john", db.executeQuery(UmlgQueryEnum.GROOVY, customer, "self.name"));

        Object result = db.executeQuery(UmlgQueryEnum.GROOVY, customer, "self.both.has('name').name");
        Assert.assertTrue(result instanceof Pipeline);
        List<String> names = new ArrayList();
        for (String name : (Pipeline<Object, String>)result) {
            names.add(name);
        }
        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("merc"));
        Assert.assertTrue(names.contains("merc"));
    }

    @Test
    public void testGroovy() {
        Customer customer = new Customer();
        customer.setName("john");

        Purchase car1 = new Purchase();
        car1.setName("bmw");
        customer.addToPurchase(car1);

        Purchase car2 = new Purchase();
        car2.setName("merc");
        customer.addToPurchase(car2);

        Account account1 = new Account();
        account1.setBalance(1000D);
        customer.addToAccount(account1);

        Account account2 = new Account();
        account2.setBalance(10D);
        customer.addToAccount(account2);

        db.commit();

//        db.executeQuery(UmlgQueryEnum.GROOVY, customer, "")
    }
}
