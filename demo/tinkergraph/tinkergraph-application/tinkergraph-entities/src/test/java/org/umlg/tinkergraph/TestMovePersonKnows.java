package org.umlg.tinkergraph;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgAdminGraph;
import org.umlg.runtime.adaptor.UmlgGraph;
import org.umlg.runtime.collection.Filter;
import org.umlg.runtime.util.UmlgProperties;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Date: 2014/03/25
 * Time: 7:35 PM
 */
public class TestMovePersonKnows {

    private UmlgGraph db;

    @Before
    public void before() throws IOException {
        File dbDir = new File(UmlgProperties.INSTANCE.getUmlgDbLocation());
        if (dbDir.exists()) {
            FileUtils.deleteDirectory(dbDir);
        }
        this.db = UMLG.get();
        TinkergraphDefaultDataCreator tinkergraphDefaultDataCreator = new TinkergraphDefaultDataCreator();
        tinkergraphDefaultDataCreator.createData();
    }

    @After
    public void after() {
        ((UmlgAdminGraph)db).drop();
    }

    @Test
    public void testMoveKnows() {
        Assert.assertTrue(!Person.allInstances().isEmpty());
        Set<? extends Person> markos = Person.allInstances(new Filter<Person>() {
            @Override
            public boolean filter(Person person) {
                return person.getName().equals("marko");
            }
        });
        Assert.assertEquals(1, markos.size());
        Person marko = markos.iterator().next();

        Assert.assertEquals(2, marko.getKnows().size());
        Person vadas = marko.getKnows().get(0);
        Friendship markoVadasFriendship = marko.getFriendship_knows().get(0);
        Assert.assertEquals(vadas, markoVadasFriendship.getKnows());
        Assert.assertEquals("vadas", vadas.getName());

        Person josh = marko.getKnows().get(1);
        Friendship markoJoshFriendship = marko.getFriendship_knows().get(1);
        Assert.assertEquals(josh, markoJoshFriendship.getKnows());
        Assert.assertEquals("josh", josh.getName());

        //Make josh markos number 1 friend
        marko.moveKnows(0, josh);

        db.commit();
        marko.reload();
        josh = marko.getKnows().get(0);
        markoJoshFriendship = marko.getFriendship_knows().get(0);
        Assert.assertEquals(josh, markoJoshFriendship.getKnows());
        Assert.assertEquals("josh", josh.getName());

        vadas = marko.getKnows().get(1);
        markoVadasFriendship = marko.getFriendship_knows().get(1);
        Assert.assertEquals(vadas, markoVadasFriendship.getKnows());
        Assert.assertEquals("vadas", vadas.getName());



    }
}
