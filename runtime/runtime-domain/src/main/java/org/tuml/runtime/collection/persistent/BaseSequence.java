package org.tuml.runtime.collection.persistent;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.OclStdLibSequence;
import org.tuml.runtime.collection.ocl.OclStdLibSequenceImpl;
import org.tuml.runtime.domain.TinkerAuditableNode;
import org.tuml.runtime.domain.TumlNode;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public abstract class BaseSequence<E> extends BaseCollection<E> implements TinkerSequence<E> {

	protected OclStdLibSequence<E> oclStdLibSequence;

	public BaseSequence(TumlRuntimeProperty runtimeProperty) {
		super(runtimeProperty);
		this.internalCollection = new ArrayList<E>();
		this.oclStdLibSequence = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSequence;
	}

	public BaseSequence(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.internalCollection = new ArrayList<E>();
		this.oclStdLibCollection = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
		this.index = GraphDb.getDb().getIndex(owner.getUid() + ":::" + getLabel(), Edge.class);
		if (this.index == null) {
			this.index = GraphDb.getDb().createIndex(owner.getUid() + ":::" + getLabel(), Edge.class);
		}
	}

	protected List<E> getInternalList() {
		return (List<E>) this.internalCollection;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
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
				} else if (TumlNode.class.isAssignableFrom(c)) {
					node = (E) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
				} else {
					node = (E) value;
					this.internalVertexMap.put(value, this.getVertexForDirection(edge));
				}
				this.getInternalList().add(node);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
		this.loaded = true;
	}

	/*
	 * This can only be added from the
	 */
	protected Edge addToListAndListIndex(int indexOf, E e) {
		E previous = this.getInternalList().get(indexOf - 1);
		E current = this.getInternalList().get(indexOf);
		this.getInternalList().add(indexOf, e);
		Edge edge = addInternal(e);

		// Edge can only be null on a one primitive
		if (edge == null && !isOnePrimitive()) {
			throw new IllegalStateException("Edge can only be null on isOne which is a String, Integer, Boolean or primitive");
		}

		float min;
		float max;
		if (e instanceof TumlNode) {
			min = (Float) ((TumlNode) previous).getVertex().getProperty("tinkerIndex");
			max = (Float) ((TumlNode) current).getVertex().getProperty("tinkerIndex");
			if (isInverseOrdered()) {
				addOrderToInverseIndex(edge, (TumlNode) e);
			}
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
	}

	@Override
	public boolean remove(Object o) {
		maybeLoad();
		int indexOf = this.getInternalList().indexOf(o);
		boolean result = this.getInternalList().remove(o);
		if (result) {
			@SuppressWarnings("unchecked")
			E e = (E) o;
			Vertex v;
			if (o instanceof TumlNode) {
				TumlNode node = (TumlNode) o;
				v = node.getVertex();
				Set<Edge> edges = GraphDb.getDb().getEdgesBetween(this.vertex, v, this.getLabel());
				for (Edge edge : edges) {
					removeEdgefromIndex(v, edge, indexOf);
					GraphDb.getDb().removeEdge(edge);
					if (o instanceof TinkerAuditableNode) {
						createAudit(e, true);
					}
					break;
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
				if (o instanceof TinkerAuditableNode) {
					createAudit(e, true);
				}
				GraphDb.getDb().removeVertex(v);
			}
		}
		return result;
	}

	@Override
	public E get(int index) {
		if (!this.loaded) {
			loadFromVertex();
		}
		return this.getInternalList().get(index);
	}

	@Override
	public E remove(int index) {
		E e = this.get(index);
		this.remove(e);
		return e;
	}

	@Override
	public void clear() {
		maybeLoad();
		for (E e : new ArrayList<E>(this.getInternalList())) {
			this.remove(e);
		}
	}

	@Override
	public int indexOf(Object o) {
		maybeLoad();
		return this.getInternalList().indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		maybeLoad();
		return this.getInternalList().lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		maybeLoad();
		return this.getInternalList().listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		maybeLoad();
		return this.getInternalList().listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		maybeLoad();
		return this.getInternalList().subList(fromIndex, toIndex);
	}

	protected abstract void removeEdgefromIndex(Vertex v, Edge edge, int indexOf);

	@Override
	public <R> TinkerSequence<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibSequence.collectNested(v);
	}

	@Override
	public <T, R> TinkerSequence<T> collect(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibSequence.collect(v);
	}
	
	@Override
	public <R> TinkerSequence<R> flatten() {
		maybeLoad();
		return this.oclStdLibSequence.flatten();
	}

	@Override
	public TinkerSequence<E> select(BooleanExpressionEvaluator<E> v) {
		maybeLoad();
		return this.oclStdLibSequence.select(v);
	}
	
	@Override
	public E first() {
		maybeLoad();
		return oclStdLibSequence.first();
	}
	
	@Override
	public Boolean equals(TinkerSequence<E> s) {
		maybeLoad();
		return this.oclStdLibSequence.equals(s);
	}

	@Override
	public TinkerSequence<E> union(TinkerSequence<? extends E> s) {
		maybeLoad();
		return this.oclStdLibSequence.union(s);
	}

	@Override
	public TinkerSequence<E> append(E object) {
		maybeLoad();
		return this.oclStdLibSequence.append(object);
	}

	@Override
	public TinkerSequence<E> prepend(E object) {
		maybeLoad();
		return this.oclStdLibSequence.prepend(object);
	}

	@Override
	public TinkerSequence<E> insertAt(Integer index, E object) {
		maybeLoad();
		return this.oclStdLibSequence.insertAt(index, object);
	}

	@Override
	public TinkerSequence<E> subSequence(Integer lower, Integer upper) {
		maybeLoad();
		return this.oclStdLibSequence.subSequence(lower, upper);
	}

	@Override
	public E at(Integer i) {
		maybeLoad();
		return this.oclStdLibSequence.at(i);
	}

	@Override
	public E last() {
		maybeLoad();
		return this.oclStdLibSequence.last();
	}

	@Override
	public TinkerSequence<E> including(E object) {
		maybeLoad();
		return this.oclStdLibSequence.including(object);
	}

	@Override
	public TinkerSequence<E> excluding(E object) {
		maybeLoad();
		return this.oclStdLibSequence.excluding(object);
	}

	@Override
	public TinkerSequence<E> reverse() {
		maybeLoad();
		return this.oclStdLibSequence.reverse();
	}

}
