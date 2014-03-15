package org.umlg.ocl.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.umlg.associationclass.Company;
import org.umlg.associationclass.Job;
import org.umlg.associationclass.Person;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.ocl.UmlgOclExecutor;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.net.URL;
import java.util.logging.LogManager;

/**
 * Date: 2014/03/14
 * Time: 9:15 PM
 */
public class TestRuntimeOclForAssociationClass extends BaseLocalDbTest {

    @BeforeClass
    public static void beforeClass() {
        try {
            URL url = BaseLocalDbTest.class.getResource("/logging.properties");
            LogManager.getLogManager().readConfiguration(url.openStream());
            URL umlUrl = BaseLocalDbTest.class.getResource("/test-ocl.uml");
            UmlgOcl2Parser.INSTANCE.init(umlUrl.toURI());
            @SuppressWarnings("unused")
            UmlgOcl2Parser instance = UmlgOcl2Parser.INSTANCE;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testRuntimeOclForAssociationClass() {
        Company company1 = new Company();
        company1.setName("company1");
        Person person1 = new Person();
        person1.setName("person1");

        Job job1 = new Job();
        job1.setRate(10);
        company1.addToPerson(person1, job1);
        db.commit();

        Job job = UmlgOclExecutor.executeOclQuery(company1, "self.job->any(rate > 0)");
        Assert.assertNotNull(job);
        Assert.assertEquals(10, job.getRate().intValue());

        job = UmlgOclExecutor.executeOclQuery(company1, "self.job->any(rate < 1)");
        Assert.assertNull(job);

    }

}
