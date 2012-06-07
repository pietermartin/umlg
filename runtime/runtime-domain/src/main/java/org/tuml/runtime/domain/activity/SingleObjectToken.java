package org.tuml.runtime.domain.activity;

import java.util.Arrays;
import java.util.Collection;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class SingleObjectToken<O> extends ObjectToken<O> {

	public SingleObjectToken(String edgeName, O object) {
		super(edgeName, object);
		addEdgeToObject(object);
	}

	public SingleObjectToken(String edgeName) {
		super(edgeName);
	}

	public SingleObjectToken(Vertex vertex) {
		super(vertex);
	}

	private void addEdgeToObject(O object) {
		Vertex v;
		if (object instanceof TinkerNode) {
			TinkerNode node = (TinkerNode) object;
			v = node.getVertex();
		} else if (object.getClass().isEnum()) {
			v = GraphDb.getDb().addVertex(null);
			v.setProperty("value", ((Enum<?>) object).name());
		} else {
			v = GraphDb.getDb().addVertex(null);
			v.setProperty("value", object);
		}
		Edge edge = GraphDb.getDb().addEdge(null, this.vertex, v, TOKEN + "toObject");
		edge.setProperty("inClass", object.getClass().getName());
	}

	protected void removeEdgeToObject() {
		O object = getObject();
		Edge edge = this.vertex.getEdges(Direction.OUT, TOKEN + "toObject").iterator().next();
		if (object instanceof TinkerNode) {
			GraphDb.getDb().removeEdge(edge);
		} else if (object.getClass().isEnum()) {
			GraphDb.getDb().removeVertex(edge.getVertex(Direction.IN));
		} else {
			GraphDb.getDb().removeVertex(edge.getVertex(Direction.IN));
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public O getObject() {
		Edge edge = this.vertex.getEdges(Direction.OUT, TOKEN + "toObject").iterator().next();
		Class<?> c = getClassToInstantiate(edge);
		Vertex v = edge.getVertex(Direction.IN);
		O node = null;
		try {
			if (c.isEnum()) {
				Object value = v.getProperty("value");
				node = (O) Enum.valueOf((Class<? extends Enum>) c, (String) value);
			} else if (CompositionNode.class.isAssignableFrom(c)) {
				node = (O) c.getConstructor(Vertex.class).newInstance(v);
			} else {
				Object value = v.getProperty("value");
				node = (O) value;
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return node;
	}

	private Class<?> getClassToInstantiate(Edge edge) {
		try {
			return Class.forName((String) edge.getProperty("inClass"));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public SingleObjectToken<O> duplicate(String flowName) {
		SingleObjectToken<O> objectToken = new SingleObjectToken<O>(flowName, getObject());
		return objectToken;
	}

	@Override
	public void remove() {
		removeEdgeToObject();
		GraphDb.getDb().removeVertex(getVertex());
	}
	
	//TODO think about null token and object tokens that are control tokens
	@Override
	public int getNumberOfElements() {
		return 1;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<O> getElements() {
		return Arrays.<O>asList(getObject());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Object Token value = ");
		sb.append(getObject());
		return sb.toString();
	}


}
