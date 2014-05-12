package org.umlg.quickpreview.qualifier;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.qualifier.Bank;
import org.umlg.qualifier.Client;
import org.umlg.quickpreview.BaseTest;

/**
 * Date: 2014/05/10
 * Time: 2:21 PM
 */
public class TestQualifier extends BaseTest {

    @Test(expected = IllegalStateException.class)
    public void testQualifierEnsuresUniqueness() {
        Bank bank = new Bank();
        Client john = new Client();
        john.setIdNumber("aaa1");
        bank.addToClient(john);

        Client joe = new Client();
        joe.setIdNumber("aaa1");
        bank.addToClient(joe);
    }

    @Test
    public void testQualifierAsIndex() {
        Bank bank = new Bank();
        for (int i = 0; i < 10000; i++) {
            Client client = new Client();
            client.setIdNumber("aaa" + i);
            bank.addToClient(client);
        }
        db.commit();

        //This getters will hit the index.
        //Finding qualified properties are efficient.
        Assert.assertEquals("aaa1", bank.getClientForIdNumberQualifierSurnameQualifier()ientForIdNumberQualifier("aaa1").getIdNumber());
        Assert.assertEquals("aaa1111", bank.getClientForIdNumberQualifier("aaa1111").getIdNumber());
        Assert.assertEquals("aaa9999", bank.getClientForIdNumberQualifier("aaa9999").getIdNumber());
        Assert.assertNull(bank.getClientForIdNumberQualifier("aaa10001"));
    }

}

