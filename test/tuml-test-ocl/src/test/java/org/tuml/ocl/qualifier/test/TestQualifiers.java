package org.tuml.ocl.qualifier.test;

import junit.framework.Assert;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import org.tuml.qualifier.Bank;
import org.tuml.qualifier.Customer;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestQualifiers extends BaseLocalDbTest {

	@Test
	public void testQualifier1() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		db.startTransaction();
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
		john1.setAccountNumber(-400);
		john1.setBank(bank);

		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(1001, new Bank(bank.getVertex()).getCustomer().size());
		Assert.assertNotNull(new Bank(bank.getVertex()).getCustomerForAccountNumberQualifier(400));
		Assert.assertNotNull(new Bank(bank.getVertex()).getFindCustomer100());
		Assert.assertNotNull(new Bank(bank.getVertex()).getFindJohn());
		stopWatch.stop();
		System.out.println("Time taken = " + stopWatch.toString());
	}

}
