package org.umlg.quickpreview.redefinition;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.quickpreview.BaseTest;
import org.umlg.redefinition.*;

/**
 * Date: 2014/05/17
 * Time: 7:01 PM
 */
public class TestRedefinition extends BaseTest {

    @Test
    public void testRedefinition() {
        MainAccount mainAccount = new MainAccount();
        //MainAccount's minimum defaults to 100
        Assert.assertEquals(100D, mainAccount.getMinimum(), 0);
        mainAccount.setPriority(1);
        mainAccount.setBalance(100D);

        LegalEntity steinbergAndSons = new LegalEntity();
        steinbergAndSons.setName("Steinberg & Sons");
        mainAccount.addToOwner(steinbergAndSons);

        PersonalAccount personalAccount = new PersonalAccount();
        //PersonalAccount's minimum defaults inherited MainAccount's default. It is not redefined.
        Assert.assertEquals(100D, personalAccount.getMinimum(), 0);
        //PersonalAccount redefines priority to be a derived property with a value of 5.
        Assert.assertEquals(5, personalAccount.getPriority(), 0);
        personalAccount.setBalance(100D);

        LegalEntity smithAndSons = new LegalEntity();
        smithAndSons.setName("Smith & Sons");
        personalAccount.addToOwner(smithAndSons);

        LegalPerson john = new LegalPerson();
        john.setName("John");
        //PersonalAccoutn redefines MainAccount.agent. Changes the type, name and multiplicity.
        personalAccount.addToTrustee(john);

        CorporateAccount corporateAccount = new CorporateAccount();
        //CorporateAccount's redefines the minimum property. Changes the default value.
        Assert.assertEquals(2500D, corporateAccount.getMinimum(), 0);
        corporateAccount.setPriority(1);
        corporateAccount.setBalance(2500D);

        LegalCompany goldbergAndSons = new LegalCompany();
        goldbergAndSons.setName("Goldberg & Sons");
        //CorporateAccount redefined MainAccount.owner
        corporateAccount.addToCompanyOwner(goldbergAndSons);

        //CorporateAccount redefined MainAccount.agent. Changes type, name and multiplicity.
        LegalCompany johnPty = new LegalCompany();
        johnPty.setName("John's Dream");
        corporateAccount.addToSigner(johnPty);

        LegalCompany suziPty = new LegalCompany();
        suziPty.setName("Suzi's Dream");
        corporateAccount.addToSigner(suziPty);

        db.commit();
    }
}
