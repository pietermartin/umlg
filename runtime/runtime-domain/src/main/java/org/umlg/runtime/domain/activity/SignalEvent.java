package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.Qualifier;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.ISignal;
import org.umlg.runtime.domain.UmlgMetaNode;
import org.umlg.runtime.domain.UmlgNode;
import org.umlg.runtime.validation.UmlgConstraintViolation;

import java.util.List;
import java.util.Map;

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
		Edge edge = UMLG.get().addEdge(null, ((UmlgNode)signal).getVertex(), this.vertex,"event_signal");
		edge.setProperty("outClass", signal.getClass().getName());
		edge.setProperty("inClass", this.getClass().getName());
	}

	@Override
	public void initialiseProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialiseProperty(UmlgRuntimeProperty umlgRuntimeProperty, boolean inverse) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public UmlgRuntimeProperty inverseAdder(UmlgRuntimeProperty umlgRuntimeProperty, boolean inverse, UmlgNode umlgNode) {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    @Override
    public void initVariables() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Qualifier> getQualifiers(UmlgRuntimeProperty umlgRuntimeProperty, UmlgNode node, boolean inverse) {
		return null;
	}

	@Override
	public int getSize(UmlgRuntimeProperty umlgRuntimeProperty) {
		// TODO Auto-generated method stub
		return 0;
	}

    @Override
    public Object getId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toJson(Boolean deep) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toJsonWithoutCompositeParent(Boolean deep) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fromJson(String json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fromJson(Map<String, Object> propertyMap) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void fromJsonDataTypeAndComposite(Map<String, Object> propertyMap) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fromJsonNonCompositeOne(Map<String, Object> propertyMap) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void doBeforeCommit() {

    }

    @Override
	public <E> UmlgSet<E> asSet() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public List<UmlgConstraintViolation> validateMultiplicities() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<UmlgConstraintViolation> checkClassConstraints() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public String toJsonWithoutCompositeParent() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public String getMetaDataAsJson() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public UmlgNode getOwningObject() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public boolean hasOnlyOneCompositeParent() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addEdgeToMetaNode() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public UmlgMetaNode getMetaNode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public List<UmlgNode> getPathToCompositionalRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
