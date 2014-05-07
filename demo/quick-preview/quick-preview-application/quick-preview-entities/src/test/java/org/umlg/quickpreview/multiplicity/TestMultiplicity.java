package org.umlg.quickpreview.multiplicity;

import org.junit.Test;
import org.umlg.multiplicity.Account;
import org.umlg.multiplicity.Customer;
import org.umlg.multiplicity.Purchase;
import org.umlg.quickpreview.BaseTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

import java.util.Random;

/**
 * Date: 2014/05/05
 * Time: 10:07 PM
 */
public class TestMultiplicity extends BaseTest {

    //This will fail as the customer needs at least one account
    @Test(expected = UmlgConstraintViolationException.class)
    public void testCustomerNeedsAnAccount() {
        Customer customer = new Customer();
        db.commit();
    }

    //This will fail as the customer can not have more than 5 accounts
    @Test(expected = UmlgConstraintViolationException.class)
    public void testCustomerHasMoreThanFiveAccounts() {
        Customer customer = new Customer();
        Account account1 = new Account();
        Account account2 = new Account();
        Account account3 = new Account();
        Account account4 = new Account();
        Account account5 = new Account();
        Account account6 = new Account();
        customer.addToAccount(account1);
        customer.addToAccount(account2);
        customer.addToAccount(account3);
        customer.addToAccount(account4);
        customer.addToAccount(account5);
        customer.addToAccount(account6);
        db.commit();
    }

    //Test passes as the customer has 5 accounts and purchases are not required.
    @Test
    public void testCustomerHasAccounts() {
        Customer customer = new Customer();
        Account account1 = new Account();
        Account account2 = new Account();
        Account account3 = new Account();
        Account account4 = new Account();
        Account account5 = new Account();
        customer.addToAccount(account1);
        customer.addToAccount(account2);
        customer.addToAccount(account3);
        customer.addToAccount(account4);
        customer.addToAccount(account5);
        db.commit();
    }

    @Test
    public void testCustomerCanHaveAnyNumberOfPurchases() {
        Customer customer = new Customer();
        //Add at least one account.
        Account account = new Account();
        customer.addToAccount(account);
        Random randomGenerator = new Random();
        int numberOfPurchases = randomGenerator.nextInt(10000);
        for (int i = 0; i < numberOfPurchases; i++) {
            Purchase purchase = new Purchase();
            customer.addToPurchase(purchase);
        }
        db.commit();
    }

}
