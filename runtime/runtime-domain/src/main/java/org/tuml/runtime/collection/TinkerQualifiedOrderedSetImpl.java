package org.tuml.runtime.collection;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.collections.set.ListOrderedSet;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.NakedTinkerIndex;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.pgm.CloseableSequence;
import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

public class TinkerQualifiedOrderedSetImpl<E> extends BaseCollection<E> implements TinkerQualifiedOrderedSet<E> {

	// protected ListOrderedSet internalListOrderedSet = new ListOrderedSet();
	protected NakedTinkerIndex<Edge> index;

	@SuppressWarnings("unchecked")
	public TinkerQualifiedOrderedSetImpl(TinkerNode owner, String uid, TumlRuntimeProperty multiplicity) {
		super();
		this.internalCollection = new ListOrderedSet();
		this.owner = owner;
		this.vertex = owner.getVertex();
		this.parentClass = owner.getClass();
		this.index = GraphDb.getDb().getIndex(uid + ":::" + getLabel(), Edge.class);
		if (this.index == null) {
			this.index = GraphDb.getDb().createManualIndex(uid + ":::" + getLabel(), Edge.class);
		}
		this.tumlRuntimeProperty = multiplicity;
	}

	public ListOrderedSet getInternalListOrderedSet() {
		return (ListOrderedSet) this.internalCollection;
	}

	@Override
	public boolean add(E e, List<Qualifier> qualifiers) {
		maybeCallInit(e);
		maybeLoad();
		validateQualifiedMultiplicity(qualifiers);
		boolean result = this.getInternalListOrderedSet().add(e);
		if (result) {
			Edge edge = addInternal(e);
			// Edge can only be null on isOneToMany, toOneToOne which is a
			// String, Interger, Boolean or primitive
			if (edge == null && !isOnePrimitive(e)) {
				throw new IllegalStateException("Edge can only be null on isOneToMany, toOneToOne which is a String, Interger, Boolean or primitive");
			}
			if (edge != null) {
				this.index.put("index", new Float(this.getInternalListOrderedSet().size() - 1), edge);
				getVertexForDirection(edge).setProperty("tinkerIndex", new Float(this.getInternalListOrderedSet().size() - 1));
				addQualifierToIndex(edge, qualifiers);
			}
		}
		return result;
	}

	@Override
	public void add(int indexOf, E e, List<Qualifier> qualifiers) {
		maybeCallInit(e);
		maybeLoad();
		Edge edge = addToListAndListIndex(indexOf, e);
		addQualifierToIndex(edge, qualifiers);
	}

	@Override
	public boolean add(E e) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	@Override
	public void add(int indexOf, E e) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(int, E, List<Qualifier>) instead");
	}

	@SuppressWarnings("unchecked")
	protected Edge addToListAndListIndex(int indexOf, E e) {
		E previous = (E) this.getInternalListOrderedSet().get(indexOf - 1);
		E current = (E) this.getInternalListOrderedSet().get(indexOf);
		this.getInternalListOrderedSet().add(indexOf, e);
		Edge edge = addInternal(e);
		// Edge can only be null on isOneToMany, toOneToOne which is a
		// String, Interger, Boolean or primitive
		if (edge == null && !isOnePrimitive(e)) {
			throw new IllegalStateException("Edge can only be null on isOneToMany, toOneToOne which is a String, Interger, Boolean or primitive");
		}
		if (edge != null) {
			float min;
			float max;
			if (e instanceof TinkerNode) {
				min = (Float) ((TinkerNode) previous).getVertex().getProperty("tinkerIndex");
				max = (Float) ((TinkerNode) current).getVertex().getProperty("tinkerIndex");
			} else if (e.getClass().isEnum()) {
				min = (Float) this.internalVertexMap.get(((Enum<?>) previous).name()).getProperty("tinkerIndex");
				max = (Float) this.internalVertexMap.get(((Enum<?>) current).name()).getProperty("tinkerIndex");
			} else {
				min = (Float) this.internalVertexMap.get(previous).getProperty("tinkerIndex");
				max = (Float) this.internalVertexMap.get(current).getProperty("tinkerIndex");
			}
			float tinkerIndex = (min + max) / 2;
			this.index.put("index", tinkerIndex, edge);
			getVertexForDirection(edge).setProperty("tinkerIndex", tinkerIndex);
			return edge;
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void loadFromVertex() {
		CloseableSequence<Edge> edges = this.index.queryList(0F, true, false);
		for (Edge edge : edges) {
			E node = null;
			try {
				Class<?> c = this.getClassToInstantiate(edge);
				Object value = this.getVertexForDirection(edge).getProperty("value");
				if (c.isEnum()) {
					node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
					this.internalVertexMap.put(value, this.getVertexForDirection(edge));
				} else if (TinkerNode.class.isAssignableFrom(c)) {
					node = (E) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
				} else {
					node = (E) value;
					this.internalVertexMap.put(value, this.getVertexForDirection(edge));
				}
				this.getInternalListOrderedSet().add(node);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
		this.loaded = true;
	}

	@Override
	public boolean remove(Object o) {
		maybeLoad();
		int indexOf = this.getInternalListOrderedSet().indexOf(o);
		boolean result = this.getInternalListOrderedSet().remove(o);
		if (result) {
			Vertex v;
			if (o instanceof TinkerNode) {
				TinkerNode node = (TinkerNode) o;
				v = node.getVertex();
				Set<Edge> edges = GraphDb.getDb().getEdgesBetween(this.vertex, v, this.getLabel());
				for (Edge edge : edges) {
					removeEdgefromIndex(v, edge, indexOf);
					GraphDb.getDb().removeEdge(edge);
				}
			} else if (o.getClass().isEnum()) {
				v = this.internalVertexMap.get(((Enum<?>) o).name());
				Edge edge = v.getInEdges(this.getLabel()).iterator().next();
				removeEdgefromIndex(v, edge, indexOf);
				GraphDb.getDb().removeVertex(v);
			} else {
				v = this.internalVertexMap.get(o);
				Edge edge = v.getInEdges(this.getLabel()).iterator().next();
				removeEdgefromIndex(v, edge, indexOf);
				GraphDb.getDb().removeVertex(v);
			}
		}
		return result;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	@Override
	public void clear() {
		maybeLoad();
		for (Object e : this.getInternalListOrderedSet()) {
			this.remove(e);
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		if (!this.loaded) {
			loadFromVertex();
		}
		return (E) this.getInternalListOrderedSet().get(index);
	}

	@Override
	public E set(int index, E element) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	@Override
	public E remove(int index) {
		E e = this.get(index);
		this.remove(e);
		return e;
	}

	@Override
	public int indexOf(Object o) {
		maybeLoad();
		return this.getInternalListOrderedSet().indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new RuntimeException("Not supported");
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new RuntimeException("Not supported");
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
			edge.setProperty("index" + qualifier.getKey(), qualifier.getValue());
		}
	}

	protected void removeEdgefromIndex(Vertex v, Edge edge, int indexOf) {
		this.index.remove("index", v.getProperty("tinkerIndex"), edge);
		for (String key : edge.getPropertyKeys()) {
			if (key.startsWith("index")) {
				this.index.remove(key, edge.getProperty(key), edge);
			}
		}
	}

}