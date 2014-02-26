package com.rorotika.test;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.umlg.model.Tinkergraph;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.adaptor.UmlgGraph;
import org.umlg.runtime.util.UmlgProperties;
import org.umlg.tinkergraph.*;

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
        //To executeStatic ocl queries the model needs to loaded and the ocl parser initialize.
        //This only needs to happen once.
        UmlgOcl2Parser.INSTANCE.init("tinkergraph.uml");
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

        Person marko  = new Person(true);
        marko.setName("marko");
        marko.setAge(29);
        Person peter = new Person();
        peter.setName("peter");
        peter.setAge(35);
        Person vadas = new Person();
        vadas.setName("vadas");
        vadas.setAge(27);
        Person josh = new Person();
        josh.setName("josh");
        josh.setAge(32);

        //Set the friendships
        Friendship markoVadasFriendship = new Friendship();
        markoVadasFriendship.setWeight(0.5);
        marko.addToKnows(vadas, markoVadasFriendship);

        Friendship markoJoshFriendship = new Friendship();
        markoJoshFriendship.setWeight(0.5);
        marko.addToKnows(josh, markoJoshFriendship);

        Program lop = new Program();
        lop.setName("lop");
        lop.setLanguage(LanguageEnum.JAVA);

        Program ripple = new Program();
        ripple.setName("ripple");
        ripple.setLanguage(LanguageEnum.JAVA);

        //Set created
        Work markoLop = new Work();
        markoLop.setWeight(0.4);
        marko.addToCreated(lop, markoLop);

        Work joshLop = new Work();
        joshLop.setWeight(0.4);
        josh.addToCreated(lop, joshLop);

        Work peterLop = new Work();
        peterLop.setWeight(0.2);
        peter.addToCreated(lop, peterLop);

        Work joshRipple = new Work();
        joshRipple.setWeight(1.0);
        josh.addToCreated(ripple, joshRipple);

        GraphDb.getDb().commit();

        List<Person> resource = Tinkergraph.INSTANCE.getPerson();
        System.out.println(resource.size());

    }

}
