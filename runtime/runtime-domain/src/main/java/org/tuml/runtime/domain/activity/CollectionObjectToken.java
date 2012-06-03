package org.tuml.runtime.domain.activity;

import java.util.Collection;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.collection.BaseCollection;
import org.tuml.runtime.collection.TumlRuntimePropertyImpl;
import org.tuml.runtime.collection.TinkerSequenceImpl;
import org.tuml.runtime.domain.CompositionNode;

import com.tinkerpop.blueprints.pgm.Vertex;

public class CollectionObjectToken<O> extends ObjectToken<O> implements CompositionNode {

	private static final long serialVersionUID = 1L;
	private BaseCollection<O> elements;

	public CollectionObjectToken(Vertex vertex) {
		super(vertex);
		this.elements = new TinkerSequenceImpl<O>(this, "org__activitytest__contextObject", getUid(), true, new TumlRuntimePropertyImpl(false, true, false, false, 0, Integer.MAX_VALUE), false);
	}

	public CollectionObjectToken(String edgeName, Collection<O> collection) {
		super(edgeName);
		this.elements = new TinkerSequenceImpl<O>(this, "org__activitytest__contextObject", getUid(), true, new TumlRuntimePropertyImpl(false, true, false, false, 0, Integer.MAX_VALUE), false);
		addCollection(collection);
	}

	private void addCollection(Collection<O> collection) {
		this.elements.addAll(collection);
	}

	public Collection<O> getElements() {
		return this.elements;
	}

	@Override
	public Object getObject() {
		return this.elements;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CollectionObjectToken<O> duplicate(String flowName) {
		CollectionObjectToken<O> objectToken = new CollectionObjectToken<O>(flowName, getElements());
		return objectToken;
	}

	@Override
	public int getNumberOfElements() {
		return this.elements.size();
	}

	@Override
	public void remove() {
		this.elements.clear();
		GraphDb.getDb().removeVertex(getVertex());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Collection Object Token value = ");
		sb.append(getElements());
		return sb.toString();
	}

	public String getUid() {
		String uid = (String) this.vertex.getProperty("uid");
		if (uid == null || uid.trim().length() == 0) {
			uid = UUID.randomUUID().toString();
			this.vertex.setProperty("uid", uid);
		}
		return uid;
	}

	@Override
	public boolean isTinkerRoot() {
		return false;
	}

	@Override
	public void initialiseProperties() {
	}

	@SuppressWarnings("unused")
	@Override
	public Long getId() {
		if (true) {
			throw new RuntimeException("check this out");
		}
		return null;
	}

	@Override
	public void setId(Long id) {
		if (true) {
			throw new RuntimeException("check this out");
		}

	}

	@SuppressWarnings("unused")
	@Override
	public int getObjectVersion() {
		if (true) {
			throw new RuntimeException("check this out");
		}
		return 0;
	}

	@SuppressWarnings("unused")
	@Override
	public CompositionNode getOwningObject() {
		if (true) {
			throw new RuntimeException("check this out");
		}
		return null;
	}

	@Override
	public void init(CompositionNode owner) {
	}

	@Override
	public void delete() {
		if (true) {
			throw new RuntimeException("check this out");
		}
	}

	@Override
	public boolean hasInitBeenCalled() {
		return true;
	}

}
