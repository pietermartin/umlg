package org.umlg.blueprints;

import com.tinkerpop.blueprints.IndexableGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Date: 2013/01/26
 * Time: 2:53 PM
 */
public class TestOrientDbBlueprints {

    @Test
    public void emptyTest() throws IOException, InterruptedException, ExecutionException {

        final String url = "/tmp/test-orientdb-blueprints";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        final IndexableGraph graph = new OrientGraph("local:" + f.getAbsolutePath());
        graph.shutdown();

    }

}
