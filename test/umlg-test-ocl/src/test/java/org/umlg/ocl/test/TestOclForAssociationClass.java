package org.umlg.ocl.test;

import org.junit.Test;
import org.umlg.associationclass.Company;
import org.umlg.associationclass.Job;
import org.umlg.associationclass.Person;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/03/14
 * Time: 9:15 PM
 */
public class TestOclForAssociationClass extends BaseLocalDbTest {

    @Test
    public void testOclForAssociationClass() {
        Company company1 = new Company();
        company1.setName("company1");
        Person person1 = new Person();
        person1.setName("person1");

        Job job1 = new Job();
        job1.setRate(10);
        company1.addToPerson(person1, job1);
        db.commit();




    }

}
