package org.tuml.runtime.domain.activity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.impl.BaseCollection;
import org.tuml.runtime.collection.impl.TinkerSequenceImpl;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TumlNode;
import org.tuml.runtime.domain.ocl.OclAny;
import org.tuml.runtime.domain.ocl.OclState;

import com.tinkerpop.blueprints.Vertex;

public class CollectionObjectToken<O> extends ObjectToken<O> implements CompositionNode {

	private static final long serialVersionUID = 1L;
	private BaseCollection<O> elements;

	public CollectionObjectToken(Vertex vertex) {
		super(vertex);
		this.elements = new TinkerSequenceImpl<O>(this, new TumlRuntimePropertyImpl());
	}

	public CollectionObjectToken(String edgeName, Collection<O> collection) {
		super(edgeName);
		this.elements = new TinkerSequenceImpl<O>(this, new TumlRuntimePropertyImpl());
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
	public void delete() {
		if (true) {
			throw new RuntimeException("check this out");
		}
	}

	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TumlNode node) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getSize(TumlRuntimeProperty tumlRuntimeProperty) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean equals(OclAny oclAny) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean notEquals(OclAny oclAny) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsNew() {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsUndefined() {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsInvalid() {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public <T> T oclAsType(T classifier) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsTypeOf(TumlNode classifier) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsKindOf(TumlNode classifier) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsInState(OclState state) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TumlNode oclType() {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String oclLocale() {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}


}
