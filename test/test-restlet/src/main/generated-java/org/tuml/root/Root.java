package org.tuml.root;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.test.Human;

public class Root {
	static public Root INSTANCE = new Root();
	public Vertex v;

	/**
	 * default constructor for Root
	 */
	private Root() {
		v = GraphDb.getDb().getRoot();
	}

	public List<Human> getHuman() {
		List<Human> result = new ArrayList<Human>();
		Iterator<Edge> iter = v.getEdges(Direction.OUT, "root").iterator();
		while ( iter.hasNext() ) {
			Edge edge = (Edge) iter.next();
			result.add(new Human(edge.getVertex(Direction.IN)));;
		}
		return result;
	}


}