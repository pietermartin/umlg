package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.collection.Qualifier;
import org.umlg.runtime.collection.TinkerSet;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.collection.persistent.BaseCollection;
import org.umlg.runtime.collection.persistent.TinkerSequenceImpl;
import org.umlg.runtime.domain.CompositionNode;
import org.umlg.runtime.domain.TumlMetaNode;
import org.umlg.runtime.domain.TumlNode;
import org.umlg.runtime.domain.ocl.OclState;
import org.umlg.runtime.validation.TumlConstraintViolation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Override
    public Long getId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
    public String toJson(Boolean deep) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toJsonWithoutCompositeParent(Boolean deep) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
	public CompositionNode getOwningObject() {
		if (true) {
			throw new RuntimeException("check this out");
		}
		return null;
	}

    @Override
    public void addEdgeToMetaNode() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TumlMetaNode getMetaNode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public void delete() {
		if (true) {
			throw new RuntimeException("check this out");
		}
	}

	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty, boolean inverse) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

    @Override
    public TumlRuntimeProperty internalAdder(TumlRuntimeProperty tumlRuntimeProperty, boolean inverse, TumlNode umlgNode) {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    @Override
    public void initVariables() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TumlNode node, boolean inverse) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getSize(TumlRuntimeProperty tumlRuntimeProperty) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean equals(Object oclAny) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean notEquals(Object oclAny) {
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
	public Boolean oclIsTypeOf(Object classifier) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsKindOf(Object classifier) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsInState(OclState state) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public <T  extends Object> Class<T> oclType() {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String oclLocale() {
		// TODO Implement
		throw new RuntimeException("Not implemented");
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
	public <E> TinkerSet<E> asSet() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public List<TumlConstraintViolation> validateMultiplicities() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TumlConstraintViolation> checkClassConstraints() {
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
	public List<TumlNode> getPathToCompositionalRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}

}
