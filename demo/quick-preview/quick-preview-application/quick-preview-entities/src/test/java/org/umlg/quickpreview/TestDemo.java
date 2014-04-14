package org.umlg.quickpreview;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.umlg.Company;
import org.umlg.Person;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.runtime.adaptor.UMLG;
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

    @BeforeClass
    public static void beforeClass() {
        //To execute ocl queries the model needs to loaded and the ocl parser initialize.
        //This only needs to happen once.
        UmlgOcl2Parser.INSTANCE.init("quick-preview.uml");
    }

    @Before
    public void before() throws IOException {
        File dbDir = new File(UmlgProperties.INSTANCE.getUmlgDbLocation());
        if (dbDir.exists()) {
            FileUtils.deleteDirectory(dbDir);
        }
        this.db = UMLG.get();
    }

    @After
    public void after() {
        db.drop();
    }

    @Test
    public void test() {
        Company company = new Company();
        company.setName("UMLG");
        Person person = new Person();
        person.setFirstname("Joe");
        person.setLastname("Bloggs");
        company.getEmployee().add(person);
        UMLG.get().commit();
        Assert.assertTrue(company.getEmployee().contains(person));
    }

}
