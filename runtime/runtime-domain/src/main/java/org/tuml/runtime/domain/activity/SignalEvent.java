package org.tuml.runtime.domain.activity;

import java.util.List;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.ISignal;
import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class SignalEvent extends Event {

	private static final long serialVersionUID = 7073156823339080917L;
	
	public SignalEvent(String name) {
		super(name);
	}

	public SignalEvent(ISignal signal, String name) {
		super(name);
		setSignal(signal);
	}

	public SignalEvent(Vertex vertex) {
		super(vertex);
	}

	public ISignal getSignal() {
		Iterable<Edge> iter1 = this.vertex.getEdges(Direction.IN, "event_signal");
		if ( iter1.iterator().hasNext() ) {
			Edge edge = iter1.iterator().next();
			try {
				Class<?> c = Class.forName((String)edge.getProperty("outClass"));
				return (ISignal) c.getConstructor(Vertex.class).newInstance(edge.getVertex(Direction.OUT));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
	
	public void setSignal(ISignal signal) {
		Edge edge = GraphDb.getDb().addEdge(null, ((TinkerNode)signal).getVertex(), this.vertex,"event_signal");
		edge.setProperty("outClass", signal.getClass().getName());
		edge.setProperty("inClass", this.getClass().getName());
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
		return null;
	}
	
}
