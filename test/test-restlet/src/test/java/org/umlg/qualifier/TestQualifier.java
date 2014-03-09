package org.umlg.qualifier;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.test.qualifier.Bank;
import org.umlg.test.qualifier.Customer;

/**
 * Date: 2014/03/08
 * Time: 3:43 PM
 */
public class TestQualifier  extends BaseLocalDbTest {

    @Test
    public void testQualifier() {
        Bank bank = new Bank();
        bank.setName("BADASS");
        Customer customer1 = new Customer();
        customer1.setName("customer1");
        customer1.setAccountNumber(1);
        customer1.setBank(bank);
        Customer customer2 = new Customer();
        customer2.setName("customer2");
        customer2.setAccountNumber(2);
        customer2.setBank(bank);
        db.commit();

        bank.reload();
        Assert.assertEquals(2, bank.getCustomer().size());
    }
}
