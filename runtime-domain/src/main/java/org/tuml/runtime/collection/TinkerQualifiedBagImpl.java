package org.tuml.runtime.collection;

import java.util.List;
import java.util.Set;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.domain.CompositionNode;

import com.google.common.collect.HashMultiset;
import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Index;
import com.tinkerpop.blueprints.pgm.Vertex;

public class TinkerQualifiedBagImpl<E> extends BaseBag<E> implements TinkerQualifiedBag<E> {

	private Index<Edge> index;

	public TinkerQualifiedBagImpl(CompositionNode owner, String label, String uid, boolean isInverse, boolean isManyToMany, boolean composite) {
		super();
		this.internalCollection = HashMultiset.create();
		this.owner = owner;
		this.vertex = owner.getVertex();
		this.label = label;
		this.parentClass = owner.getClass();
		this.index = GraphDb.getDb().getIndex(uid + ":::" + label, Edge.class);
		if (this.index == null) {
			this.index = GraphDb.getDb().createManualIndex(uid + ":::" + label, Edge.class);
		}
		this.inverse = isInverse;
		this.manyToMany = isManyToMany;
		this.composite = composite;
	}

	@Override	
	public boolean add(E e, List<Qualifier> qualifiers) {
		maybeCallInit(e);
		maybeLoad();
		
		validateQualifiedMultiplicity(qualifiers);
		
		boolean result = this.getInternalBag().add(e);
		Edge edge = null;
		if (result) {
			edge = addInternal(e);
		} else {
			if (!this.manyToMany) { 
				throw new IllegalStateException("Only with many to many relationship can the edge already have been created");
			}
			Vertex v;
			if (e instanceof CompositionNode) {
				CompositionNode node = (CompositionNode) e;
				TransactionThreadEntityVar.setNewEntity((CompositionNode) node);
				v = node.getVertex();
				Set<Edge> edgesBetween = GraphDb.getDb().getEdgesBetween(this.vertex, v, this.label);
				if (edgesBetween.size()!=1) {
					throw new IllegalStateException("A set can only have one edge between the two ends");
				}
				edge = edgesBetween.iterator().next();
			} else {
				throw new IllegalStateException("Embedded relationships can not be many to many");
			}
		}
		addQualifierToIndex(edge, qualifiers);
		return result;
		
	}

	@Override
	public boolean add(E e) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	@Override
	public boolean remove(Object o) {
		if (!this.loaded) {
			this.loaded = true;
			loadFromVertex();
		}
		boolean result = this.getInternalBag().remove(o);
		if (result) {
			Vertex v;
			if (o instanceof CompositionNode) {
				CompositionNode node = (CompositionNode) o;
				v = node.getVertex();
				Set<Edge> edges = GraphDb.getDb().getEdgesBetween(this.vertex, v, this.label);
				for (Edge edge : edges) {
					removeEdgefromIndex(edge);
					GraphDb.getDb().removeEdge(edge);
				}
			} else if (o.getClass().isEnum()) {
				v = this.internalVertexMap.get(((Enum<?>) o).name());
				Edge edge = v.getInEdges(this.label).iterator().next();
				removeEdgefromIndex(edge);
				GraphDb.getDb().removeVertex(v);
			} else {
				v = this.internalVertexMap.get(o);
				Edge edge = v.getInEdges(this.label).iterator().next();
				removeEdgefromIndex(edge);
				GraphDb.getDb().removeVertex(v);
			}
		}
		return result;
	}

	private void removeEdgefromIndex(Edge edge) {
		for (String key : edge.getPropertyKeys()) {
			if (key.startsWith("index")) {
				this.index.remove(key, edge.getProperty(key), edge);
			}
		}
	}

	protected void validateQualifiedMultiplicity(List<Qualifier> qualifiers) {
		for (Qualifier qualifier : qualifiers) {
			if (qualifier.isOne()) {
				long count = this.index.count(qualifier.getKey(), qualifier.getValue());
				if (count > 0) {
					// Add info to exception
					throw new IllegalStateException("qualifier fails, entry for qualifier already exist");
				}
			}
		}
	}

	private void addQualifierToIndex(Edge edge, List<Qualifier> qualifiers) {
		for (Qualifier qualifier : qualifiers) {
			this.index.put(qualifier.getKey(), qualifier.getValue(), edge);
			edge.setProperty("index"+qualifier.getKey(), qualifier.getValue());
		}
	}

}
