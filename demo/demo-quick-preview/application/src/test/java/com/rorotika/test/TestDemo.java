package com.rorotika.test;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.umlg.Company;
import org.umlg.Person;
import org.umlg.model.DemoQuickPreview;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.adaptor.UmlgGraph;
import org.umlg.runtime.util.UmlgProperties;

import java.io.File;
import java.io.IOException;

/**
 * Date: 2014/01/15
 * Time: 10:17 PM
 */
public class TestDemo {

    private UmlgGraph db;

    @Before
    public void before() throws IOException {
        File dbDir = new File(UmlgProperties.INSTANCE.getTumlDbLocation());
        if (dbDir.exists()) {
            FileUtils.deleteDirectory(dbDir);
        }
        this.db = GraphDb.getDb();
    }

    @After
    public void after() {
        db.shutdown();
    }

    @Test
    public void testDemoQuickPreview() {
        Company company1 = new Company(true);
        company1.setName("TBL");
        Person person1 = new Person(true);
        person1.setFirstname("Joe");
        person1.setLastname("Bloggs");
        company1.addToEmployee(person1);

        Company company2 = new Company(true);
        company2.setName("Company2");
        Person person2 = new Person(true);
        person2.setFirstname("Todd");
        person2.setLastname("Dolittle");
        company2.addToEmployee(person2);
        db.commit();

        Assert.assertEquals(2, DemoQuickPreview.INSTANCE.getCompany().size());
        Assert.assertEquals(2, DemoQuickPreview.INSTANCE.getPerson().size());
        Assert.assertEquals(1, company1.getEmployee().size());
        Assert.assertEquals(1, company2.getEmployee().size());
        Assert.assertEquals(1, person1.getEmployer().size());
        Assert.assertEquals(1, person2.getEmployer().size());
    }
}
