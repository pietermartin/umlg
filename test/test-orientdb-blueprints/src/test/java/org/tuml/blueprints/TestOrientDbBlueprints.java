package org.tuml.blueprints;

import com.orientechnologies.orient.core.id.ORecordId;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import org.junit.Assert;
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
    public void testIdChangesAfterCommit() throws IOException, InterruptedException, ExecutionException {
        final String url = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "test-orientdb-blueprints";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        final TransactionalGraph graph = new OrientGraph("local:" + f.getAbsolutePath());
        Vertex v = graph.addVertex(null);
        v.setProperty("name", "Joe");
        graph.commit();
        ORecordId oRecordId = (ORecordId)v.getId();
        Assert.assertTrue(oRecordId.getClusterId() >= 0);
        graph.shutdown();
    }

}
