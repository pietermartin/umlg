package org.tuml.runtime.collection.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.collections.set.ListOrderedSet;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.OclStdLibOrderedSet;
import org.tuml.runtime.collection.ocl.OclStdLibOrderedSetImpl;
import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class TinkerOrderedSetImpl<E> extends BaseCollection<E> implements TinkerOrderedSet<E> {

	protected OclStdLibOrderedSet<E> oclStdLibOrderedSet;

	@SuppressWarnings("unchecked")
	public TinkerOrderedSetImpl(TinkerNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.internalCollection = new ListOrderedSet();
		this.oclStdLibOrderedSet = new OclStdLibOrderedSetImpl<E>((ListOrderedSet)this.internalCollection); 
		this.oclStdLibCollection = this.oclStdLibOrderedSet;
		this.index = GraphDb.getDb().getIndex(owner.getUid() + ":::" + getLabel(), Edge.class);
		if (this.index == null) {
			this.index = GraphDb.getDb().createIndex(owner.getUid() + ":::" + getLabel(), Edge.class);
		}
	}

	protected ListOrderedSet getInternalListOrderedSet() {
		return (ListOrderedSet) this.internalCollection;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		maybeLoad();
		int indexOf = index;
		for (E e : c) {
			this.add(indexOf++, e);
		}
		return true;
	}

	@Override
	public void add(int indexOf, E e) {
		// validateMultiplicityForAdditionalElement calls size() which loads the
		// collection
		validateMultiplicityForAdditionalElement();
//		maybeCallInit(e);
		addToListAndListIndex(indexOf, e);
	}

	@SuppressWarnings("unchecked")
	protected Edge addToListAndListIndex(int indexOf, E e) {
		E previous = (E) this.getInternalListOrderedSet().get(indexOf - 1);
		E current = (E) this.getInternalListOrderedSet().get(indexOf);
		this.getInternalListOrderedSet().add(indexOf, e);
		Edge edge = addInternal(e);
		if (edge == null && !isOnePrimitive()) {
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
		CloseableIterable<Edge> edges = this.index.queryList(0F, true, false);
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
				Edge edge = v.getEdges(Direction.IN, this.getLabel()).iterator().next();
				removeEdgefromIndex(v, edge, indexOf);
				GraphDb.getDb().removeVertex(v);
			} else {
				v = this.internalVertexMap.get(o);
				Edge edge = v.getEdges(Direction.IN, this.getLabel()).iterator().next();
				removeEdgefromIndex(v, edge, indexOf);
				GraphDb.getDb().removeVertex(v);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		maybeLoad();
		for (E e : new HashSet<E>(this.getInternalListOrderedSet())) {
			this.remove(e);
		}
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
		E removedElement = this.remove(index);
		this.add(index, element);
		return removedElement;
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

	protected void removeEdgefromIndex(Vertex v, Edge edge, int indexOf) {
		this.index.remove("index", v.getProperty("tinkerIndex"), edge);
	}

	@Override
	public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibOrderedSet.collectNested(v);
	}

	@Override
	public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibOrderedSet.collect(v);
	}
	
	@Override
	public <R> TinkerOrderedSet<R> flatten() {
		maybeLoad();
		return this.oclStdLibOrderedSet.flatten();
	}

	@Override
	public TinkerOrderedSet<E> select(BooleanExpressionEvaluator<E> v) {
		maybeLoad();
		return this.oclStdLibOrderedSet.select(v);
	}
}
