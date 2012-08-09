package org.test.restlet;

import junit.framework.Assert;

import org.junit.Test;
import org.tuml.runtime.test.BaseLocalDbTest;
import org.tuml.test.Human;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.Vertex;

public class TestFindVertexById extends BaseLocalDbTest {

	@Test
	public void findVertexById() {
		db.startTransaction();
		Human human = new Human(true);
		human.setName("human1");
		db.stopTransaction(Conclusion.SUCCESS);
		Assert.assertEquals(1, countVertices());
		
		Vertex v = db.getVertex(2);
		Assert.assertNotNull(v);
		
	}
	
}
