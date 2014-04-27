package org.umlg.tinkergraph;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.umlg.BaseModelUmlg;
import org.umlg.meta.BaseClassUmlg;
import org.umlg.model.Tinkergraph;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgGraph;
import org.umlg.runtime.util.UmlgProperties;

import java.io.File;
import java.io.IOException;

/**
 * Date: 2014/04/20
 * Time: 11:48 AM
 */
public class TestRemoveAllData {

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
        db.drop();
    }

    @Test
    public void testRemoveAllData() {
        System.out.println(db.countVertices());
        for (BaseModelUmlg a : BaseModelUmlg.allInstances()) {
            a.delete();
        }
        db.commit();
        Assert.assertEquals(14, db.countVertices());
    }

}
