package org.test.restlet;

import com.tinkerpop.blueprints.Vertex;
import junit.framework.Assert;
import org.junit.Test;
import org.tuml.runtime.test.BaseLocalDbTest;
import org.tuml.test.Alien;

public class TestFindVertexById extends BaseLocalDbTest {

	@Test
	public void findVertexById() {
		Alien alien = new Alien(true);
		alien.setName("alien1");
        db.commit();
		Assert.assertEquals(1 + 1, countVertices());
		
		Vertex v = db.getVertex(2);
		Assert.assertNotNull(v);
		
	}
	
}
