package org.tuml.runtime.collection;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.Edge;

public class TinkerQualifiedSetImpl<E> extends BaseSet<E> implements TinkerQualifiedSet<E> {

	public TinkerQualifiedSetImpl(TinkerNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.index = GraphDb.getDb().getIndex(owner.getUid() + ":::" + getLabel(), Edge.class);
		if (this.index == null) {
			this.index = GraphDb.getDb().createIndex(owner.getUid() + ":::" + getLabel(), Edge.class);
		}
	}
	
	@Override
	protected void doWithEdgeAfterAddition(Edge edge, E e) {
		addQualifierToIndex(edge, e);
	}

//	public Set<E> getInternalSet() {
//		return (Set<E>) this.internalCollection;
//	}
//
//	@Override
//	public boolean add(E e, List<Qualifier> qualifiers) {
//		maybeCallInit(e);
//		maybeLoad();
//		validateQualifiedMultiplicity(qualifiers);
//		boolean result = this.getInternalSet().add(e);
//		Edge edge = null;
//		if (result) {
//			edge = addInternal(e);
//		} else {
//			if (!this.isManyToMany()) {
//				throw new IllegalStateException("Only with many to many relationship can the edge already have been created");
//			}
//			Vertex v;
//			if (e instanceof TinkerNode) {
//				TinkerNode node = (TinkerNode) e;
//				if (e instanceof CompositionNode) {
//					TransactionThreadEntityVar.setNewEntity((CompositionNode) node);
//				}
//				v = node.getVertex();
//				Set<Edge> edgesBetween = GraphDb.getDb().getEdgesBetween(this.vertex, v, this.getLabel());
//				if (edgesBetween.size() != 1) {
//					throw new IllegalStateException("A set can only have one edge between the two ends");
//				}
//				edge = edgesBetween.iterator().next();
//			} else {
//				throw new IllegalStateException("Embedded relationships can not be many to many");
//			}
//		}
//		// Edge can only be null on isOneToMany, toOneToOne which is a
//		// String, Integer, Boolean or primitive
//		if (edge == null && !isOnePrimitive()) {
//			throw new IllegalStateException("Edge can only be null on isOneToMany, toOneToOne which is a String, Interger, Boolean or primitive");
//		}
//		if (edge != null) {
//			addQualifierToIndex(edge, qualifiers);
//		}
//		return result;
//	}
//
//	@Override
//	public boolean add(E e) {
//		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
//	}
//
//	@Override
//	public boolean remove(Object o) {
//		if (!this.loaded) {
//			// this.loaded = true;
//			loadFromVertex();
//		}
//		boolean result = this.getInternalSet().remove(o);
//		if (result) {
//			Vertex v;
//			if (o instanceof TinkerNode) {
//				TinkerNode node = (TinkerNode) o;
//				v = node.getVertex();
//				Set<Edge> edges = GraphDb.getDb().getEdgesBetween(this.vertex, v, this.getLabel());
//				for (Edge edge : edges) {
//					removeEdgefromIndex(edge);
//					GraphDb.getDb().removeEdge(edge);
//				}
//			} else if (o.getClass().isEnum()) {
//				v = this.internalVertexMap.get(((Enum<?>) o).name());
//				Edge edge = v.getEdges(Direction.IN, this.getLabel()).iterator().next();
//				removeEdgefromIndex(edge);
//				GraphDb.getDb().removeVertex(v);
//			} else {
//				v = this.internalVertexMap.get(o);
//				Edge edge = v.getEdges(Direction.IN, this.getLabel()).iterator().next();
//				removeEdgefromIndex(edge);
//				GraphDb.getDb().removeVertex(v);
//			}
//		}
//		return result;
//	}
//
//	private void removeEdgefromIndex(Edge edge) {
//		for (String key : edge.getPropertyKeys()) {
//			if (key.startsWith("index")) {
//				this.index.remove(key, edge.getProperty(key), edge);
//			}
//		}
//	}
//
//	protected void validateQualifiedMultiplicity(List<Qualifier> qualifiers) {
//		for (Qualifier qualifier : qualifiers) {
//			if (qualifier.isOne()) {
//				long count = this.index.count(qualifier.getKey(), qualifier.getValue());
//				if (count > 0) {
//					// Add info to exception
//					throw new IllegalStateException("qualifier fails, entry for qualifier already exist");
//				}
//			}
//		}
//	}
//
//	private void addQualifierToIndex(Edge edge, List<Qualifier> qualifiers) {
//		for (Qualifier qualifier : qualifiers) {
//			this.index.put(qualifier.getKey(), qualifier.getValue(), edge);
//			edge.setProperty("index" + qualifier.getKey(), qualifier.getValue());
//		}
//	}

}
