package org.umlg.restlet;

import org.umlg.*;
import org.umlg.associationclass.Company;
import org.umlg.associationclass.Job;
import org.umlg.associationclass.Person;
import org.umlg.runtime.adaptor.DefaultDataCreator;
import org.umlg.runtime.adaptor.GraphDb;

/**
 * Date: 2013/06/29
 * Time: 3:58 PM
 */
public class TestRestletMinimumDefaultData  implements DefaultDataCreator {
    @Override
    public void createData() {
        God god = new God(true);
        god.setName("jesus");
        Universe universe1 = new Universe(god);
        universe1.setName("universe1");
        universe1.setUniverseSize(1);

        Universe universe2 = new Universe(god);
        universe2.setName("universe2");
        universe2.setUniverseSize(2);

        Company company1 = new Company(true);
        company1.setName("company1");
        Person person1 = new Person(true);
        person1.setName("person1");
        Job job1 = new Job(true);
        job1.setSalary(1);
        company1.addToPerson(person1, job1);

        GraphDb.getDb().commit();
    }
}
