package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.domain.CompositionNode;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Arrays;
import java.util.Collection;

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
		if (object instanceof UmlgNode) {
			UmlgNode node = (UmlgNode) object;
			v = node.getVertex();
		} else if (object.getClass().isEnum()) {
			v = UMLG.getDb().addVertex(null);
			v.setProperty("value", ((Enum<?>) object).name());
		} else {
			v = UMLG.getDb().addVertex(null);
			v.setProperty("value", object);
		}
		Edge edge = UMLG.getDb().addEdge(null, this.vertex, v, TOKEN + "toObject");
		edge.setProperty("inClass", object.getClass().getName());
	}

	protected void removeEdgeToObject() {
		O object = getObject();
		Edge edge = this.vertex.getEdges(Direction.OUT, TOKEN + "toObject").iterator().next();
		if (object instanceof UmlgNode) {
			UMLG.getDb().removeEdge(edge);
		} else if (object.getClass().isEnum()) {
			UMLG.getDb().removeVertex(edge.getVertex(Direction.IN));
		} else {
			UMLG.getDb().removeVertex(edge.getVertex(Direction.IN));
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
		UMLG.getDb().removeVertex(getVertex());
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
