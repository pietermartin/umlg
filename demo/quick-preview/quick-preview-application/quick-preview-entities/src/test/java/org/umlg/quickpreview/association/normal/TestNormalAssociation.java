package org.umlg.quickpreview.association.normal;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.association.normal.Company;
import org.umlg.association.normal.Person;
import org.umlg.quickpreview.BaseTest;

/**
 * Date: 2014/05/05
 * Time: 9:05 PM
 */
public class TestNormalAssociation extends BaseTest {

    @Test
    public void testNormalAssociation() {
        Company microsoft = new Company();
        microsoft.setName("microsoft");
        Company apple = new Company();
        apple.setName("apple");

        Person john = new Person();
        john.setName("john");
        john.addToEmployer(microsoft);
        john.addToEmployer(apple);

        Person joe = new Person();
        joe.setName("joe");
        joe.addToEmployer(microsoft);

        db.commit();

        Assert.assertEquals(2, microsoft.getEmployee().size());
        Assert.assertTrue(microsoft.getEmployee().contains(john));
        Assert.assertTrue(microsoft.getEmployee().contains(joe));
        Assert.assertTrue(apple.getEmployee().contains(john));

        Assert.assertEquals(2, john.getEmployer().size());
        Assert.assertTrue(john.getEmployer().contains(microsoft));
        Assert.assertTrue(john.getEmployer().contains(apple));

        Assert.assertEquals(1, joe.getEmployer().size());
        Assert.assertTrue(joe.getEmployer().contains(microsoft));
        Assert.assertTrue(!joe.getEmployer().contains(apple));
    }
}
