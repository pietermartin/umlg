package org.umlg.tests.redefinition;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.redefinition.*;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/01/08
 * Time: 11:16 PM
 */
public class TestRedefinition extends BaseLocalDbTest {

    @Test
    public void testRedefinition() {
        Account account = new Account(true);
        Assert.assertEquals(100D, account.getMinimum(), 0);
        Assert.assertEquals(null, account.getPriority());
        account.setBalance(1000D);
        account.setPriority(1);

        LegalEntity owner1 = new LegalEntity(true);
        owner1.setName("owner1");
        account.addToOwner(owner1);
        LegalEntity owner2 = new LegalEntity(true);
        owner2.setName("owner2");
        account.addToOwner(owner2);
        LegalEntity owner3 = new LegalEntity(true);
        owner3.setName("owner3");
        account.addToOwner(owner3);

        LegalEntity agent1 = new LegalEntity(true);
        agent1.setName("agent1");
        account.addToAgent(owner1);
        LegalEntity agent2 = new LegalEntity(true);
        agent2.setName("agent2");
        account.addToAgent(agent2);
        LegalEntity agent3 = new LegalEntity(true);
        agent3.setName("agent3");
        account.addToAgent(owner3);

        db.commit();

        Assert.assertEquals(3, account.getOwner().size());
        Assert.assertEquals(3, account.getAgent().size());

        PersonalAccount personalAccount = new PersonalAccount(true);
        Assert.assertEquals(Integer.valueOf(5), personalAccount.getPriority());

        Person person = new Person(true);
        person.setName("John");
        personalAccount.addToTrustee(person);

        db.commit();

        Assert.assertEquals(0, personalAccount.getOwner().size());
        Assert.assertNotNull(personalAccount.getTrustee());
        Assert.assertEquals("John", personalAccount.getTrustee().getName());

        boolean redefinedPropertyWithOldNameNotCallable = false;
        try {
            personalAccount.getAgent();
        } catch(IllegalStateException e) {
            redefinedPropertyWithOldNameNotCallable = true;
        }
        Assert.assertTrue("Property with redefined name should not be able to invoke the old name.",redefinedPropertyWithOldNameNotCallable);

        CorporateAccount corporateAccount = new CorporateAccount(true);
        Assert.assertEquals(2500D, corporateAccount.getMinimum(), 0);

        Company owner = new Company(true);
        owner.setName("owner");
        corporateAccount.setCompanyOwner(owner);

        Company signer1 = new Company(true);
        signer1.setName("signer1");
        corporateAccount.addToSigner(signer1);
        Company signer2 = new Company(true);
        signer2.setName("signer2");
        corporateAccount.addToSigner(signer2);

        db.commit();

        corporateAccount.reload();
        Assert.assertNotNull(corporateAccount.getCompanyOwner());
        Assert.assertEquals(2, corporateAccount.getSigner().size());

        redefinedPropertyWithOldNameNotCallable = false;
        try {
            corporateAccount.getOwner();
        } catch(IllegalStateException e) {
            redefinedPropertyWithOldNameNotCallable = true;
        }
        Assert.assertTrue("Property with redefined name should not be able to invoke the old name.",redefinedPropertyWithOldNameNotCallable);
        redefinedPropertyWithOldNameNotCallable = false;
        try {
            corporateAccount.getAgent();
        } catch(IllegalStateException e) {
            redefinedPropertyWithOldNameNotCallable = true;
        }
        Assert.assertTrue("Property with redefined name should not be able to invoke the old name.",redefinedPropertyWithOldNameNotCallable);

    }

}
