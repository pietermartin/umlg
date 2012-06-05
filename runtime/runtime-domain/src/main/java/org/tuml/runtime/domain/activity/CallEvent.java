package org.tuml.runtime.domain.activity;

import org.tuml.runtime.collection.TumlRuntimeProperty;

import com.tinkerpop.blueprints.pgm.Vertex;

public class CallEvent extends Event {

	private static final long serialVersionUID = -467486969342220483L;

	public CallEvent(String name) {
		super(name);
	}

	public CallEvent(Vertex vertex) {
		super(vertex);
	}

	@Override
	public void initialiseProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

}
