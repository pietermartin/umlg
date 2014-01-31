package com.rorotika.test;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.*;
import org.umlg.Many;
import org.umlg.One;
import org.umlg.model.Tinkergraph;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.ocl.UmlgOclExecutor;
import org.umlg.query.InstanceQuery;
import org.umlg.query.QueryEnum;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.adaptor.UmlgGraph;
import org.umlg.runtime.util.UmlgProperties;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
        UmlgOcl2Parser.INSTANCE.init("umlg-demo1.uml");
    }

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
        db.drop();
    }

    @Test
    public void testDemo() {
        One one1 = new One(true);
        //MaxLength validation ensures name's length must be <= 5
        one1.setName("12345");
        one1.setDate(new LocalDate());
        Many many1 = new Many(one1);
        //MinLength validation ensures name's length must be >= 5
        many1.setName("12345");
        many1.setDateTime(new DateTime());
        Many many2 = new Many(one1);
        many2.setName("12345");
        many2.setDateTime(new DateTime());
        Many many3 = new Many(one1);
        many3.setName("12345");
        many3.setDateTime(new DateTime());
        Many many4 = new Many(one1);
        many4.setName("12345");
        many4.setDateTime(new DateTime());
        Many many5 = new Many(one1);
        many5.setName("12345");
        many5.setDateTime(new DateTime());
        db.commit();

        Assert.assertEquals(1, Tinkergraph.INSTANCE.getOne().size());
        Assert.assertEquals(5, one1.getMany().size());
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testDemoValidation() {
        try {
            One one1 = new One(true);
            //MaxLength validation ensures name's length must be <= 5
            one1.setName("12345");
            one1.setDate(new LocalDate());
            Many many1 = new Many(one1);
            //MinLength validation ensures name's length must be >= 5
            many1.setName("1234");
            many1.setDateTime(new DateTime());
            db.commit();
            Assert.assertEquals(1, Tinkergraph.INSTANCE.getOne().size());
            Assert.assertEquals(5, one1.getMany().size());
        } finally {
            db.rollback();
        }
    }

    @Test
    public void testDemoInstanceQuery() {
        One one1 = new One(true);
        //MaxLength validation ensures name's length must be <= 5
        one1.setName("55555");
        one1.setDate(new LocalDate());
        Many many1 = new Many(one1);
        //MinLength validation ensures name's length must be >= 5
        many1.setName("12345");
        many1.setDateTime(new DateTime());
        Many many2 = new Many(one1);
        many2.setName("12345");
        many2.setDateTime(new DateTime());
        Many many3 = new Many(one1);
        many3.setName("12345");
        many3.setDateTime(new DateTime());
        Many many4 = new Many(one1);
        many4.setName("12345");
        many4.setDateTime(new DateTime());
        Many many5 = new Many(one1);
        many5.setName("54321");
        many5.setDateTime(new DateTime());
        db.commit();

        Assert.assertEquals(1, Tinkergraph.INSTANCE.getOne().size());
        Assert.assertEquals(5, one1.getMany().size());

        InstanceQuery instanceQuery1 = new InstanceQuery(one1);
        instanceQuery1.setName("instanceQuery1");
        instanceQuery1.setQueryEnum(QueryEnum.OCL);
        instanceQuery1.setQueryString("self.name");

        InstanceQuery instanceQuery2 = new InstanceQuery(one1);
        instanceQuery2.setName("instanceQuery2");
        instanceQuery2.setQueryEnum(QueryEnum.OCL);
        instanceQuery2.setQueryString("self.many->select(name = '54321')->asSequence()");

        InstanceQuery instanceQuery3 = new InstanceQuery(one1);
        instanceQuery3.setName("instanceQuery3");
        instanceQuery3.setQueryEnum(QueryEnum.OCL);
        instanceQuery3.setQueryString("self.many->select(name = '54321')->asSequence()->first()");

        db.commit();

        Assert.assertEquals(3, one1.getInstanceQuery().size());
        String name = UmlgOclExecutor.executeOclQuery(one1, instanceQuery1.getQueryString());
        Assert.assertEquals("55555", name);

        List<Many> many = UmlgOclExecutor.executeOclQuery(one1, instanceQuery2.getQueryString());
        Assert.assertEquals(1, many.size());
        Assert.assertEquals("54321", many.get(0).getName());

        Many first = UmlgOclExecutor.executeOclQuery(one1, instanceQuery3.getQueryString());
        Assert.assertNotNull(first);
        Assert.assertEquals("54321", first.getName());

    }
}
