package org.umlg.quickpreview;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgGraph;
import org.umlg.runtime.util.UmlgProperties;

import java.io.File;
import java.io.IOException;

/**
 * Date: 2014/05/04
 * Time: 12:09 PM
 */
public abstract class BaseTest {

    protected UmlgGraph db;

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

}
