package org.umlg.titan.test;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Date: 2013/01/26
 * Time: 2:53 PM
 */
public class TestTitanBlueprints {

    @Test
    public void testIndexCreatedInAThreadUsedInAnother() throws IOException, InterruptedException, ExecutionException {
        final String url = "/tmp/titan-test";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Configuration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.addProperty("storage.backend", "local");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph g = TitanFactory.open(propertiesConfiguration);

        //Create the index
        g.createKeyIndex("testKeyIndexonEdge", Edge.class);

        Vertex juno = g.addVertex(null);
        juno.setProperty("name", "juno");
        Vertex jupiter = g.addVertex(null);
        jupiter.setProperty("name", "jupiter");
        Edge married = g.addEdge(null, juno, jupiter, "married");
        //Index a value
        married.setProperty("testKeyIndexonEdge", "specialValue");
        g.commit();

        married = g.getEdge(married.getId());
        g.removeEdge(married);
        g.commit();

    }

}
