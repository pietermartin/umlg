package org.tuml.root;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tuml.InterfaceRealization1;
import org.tuml.One;
import org.tuml.OneOne;
import org.tuml.OneTwo;
import org.tuml.qualifier.God;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.sequence.SequenceRoot;

public class Root {
	static public Root INSTANCE = new Root();
	public Vertex v;

	/**
	 * default constructor for Root
	 */
	private Root() {
		v = GraphDb.getDb().getRoot();
	}

	public List<God> getGod() {
		List<God> result = new ArrayList<God>();
		Iterator<Edge> iter = v.getEdges(Direction.OUT, "root").iterator();
		while ( iter.hasNext() ) {
			Edge edge = (Edge) iter.next();
			result.add(new God(edge.getVertex(Direction.IN)));;
		}
		return result;
	}
	
	public List<InterfaceRealization1> getInterfaceRealization1() {
		List<InterfaceRealization1> result = new ArrayList<InterfaceRealization1>();
		Iterator<Edge> iter = v.getEdges(Direction.OUT, "root").iterator();
		while ( iter.hasNext() ) {
			Edge edge = (Edge) iter.next();
			result.add(new InterfaceRealization1(edge.getVertex(Direction.IN)));;
		}
		return result;
	}
	
	public List<One> getOne() {
		List<One> result = new ArrayList<One>();
		Iterator<Edge> iter = v.getEdges(Direction.OUT, "root").iterator();
		while ( iter.hasNext() ) {
			Edge edge = (Edge) iter.next();
			result.add(new One(edge.getVertex(Direction.IN)));;
		}
		return result;
	}
	
	public List<OneOne> getOneOne() {
		List<OneOne> result = new ArrayList<OneOne>();
		Iterator<Edge> iter = v.getEdges(Direction.OUT, "root").iterator();
		while ( iter.hasNext() ) {
			Edge edge = (Edge) iter.next();
			result.add(new OneOne(edge.getVertex(Direction.IN)));;
		}
		return result;
	}
	
	public List<OneTwo> getOneTwo() {
		List<OneTwo> result = new ArrayList<OneTwo>();
		Iterator<Edge> iter = v.getEdges(Direction.OUT, "root").iterator();
		while ( iter.hasNext() ) {
			Edge edge = (Edge) iter.next();
			result.add(new OneTwo(edge.getVertex(Direction.IN)));;
		}
		return result;
	}
	
	public List<SequenceRoot> getSequenceRoot() {
		List<SequenceRoot> result = new ArrayList<SequenceRoot>();
		Iterator<Edge> iter = v.getEdges(Direction.OUT, "root").iterator();
		while ( iter.hasNext() ) {
			Edge edge = (Edge) iter.next();
			result.add(new SequenceRoot(edge.getVertex(Direction.IN)));;
		}
		return result;
	}


}