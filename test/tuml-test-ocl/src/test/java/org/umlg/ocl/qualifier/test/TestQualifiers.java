package org.umlg.ocl.qualifier.test;

import junit.framework.Assert;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import org.umlg.qualifier.Bank;
import org.umlg.qualifier.Customer;
import org.umlg.runtime.test.BaseLocalDbTest;

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
		Assert.assertNotNull(new Bank(bank.getVertex()).getCustomerForNameQualifierAccountNumberQualifier("c1", 1));
		Assert.assertNotNull(new Bank(bank.getVertex()).getCustomerJohn001());
		stopWatch.stop();
		System.out.println("Time taken = " + stopWatch.toString());
	}

}
