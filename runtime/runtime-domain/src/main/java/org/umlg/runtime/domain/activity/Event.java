package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.domain.UmlgNode;
import org.umlg.runtime.domain.ocl.OclState;

public abstract class Event implements UmlgNode {

	private static final long serialVersionUID = 546409593170432165L;
	protected Vertex vertex;

	public Event(Vertex vertex) {
		super();
		this.vertex = vertex;
	}

	public Event(String name) {
		super();
		this.vertex = GraphDb.getDb().addVertex(null);
		this.vertex.setProperty("name", name);
	}

	public Vertex getVertex() {
		return vertex;
	}
	
	public String getName() {
		return (String) this.vertex.getProperty("name");
	}

	@Override
	public boolean isTinkerRoot() {
		return false;
	}

	@Override
	public String getUid() {
		return null;
	}

	@Override
	public boolean equals(Object oclAny) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean notEquals(Object oclAny) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsNew() {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsUndefined() {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsInvalid() {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public <T> T oclAsType(T classifier) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsTypeOf(Object classifier) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsKindOf(Object classifier) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsInState(OclState state) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public <T  extends Object> Class<T> oclType() {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String oclLocale() {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

}
