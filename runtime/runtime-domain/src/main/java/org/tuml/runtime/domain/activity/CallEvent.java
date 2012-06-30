package org.tuml.runtime.domain.activity;

import java.util.List;

import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.Vertex;

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

	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TinkerNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
