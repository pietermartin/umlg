package org.umlg.runtime.collection.persistent;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.tinkerpop.blueprints.Edge;
import org.umlg.runtime.collection.TinkerBag;
import org.umlg.runtime.collection.TinkerSet;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibBag;
import org.umlg.runtime.collection.ocl.OclStdLibBagImpl;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Set;

public abstract class BaseBag<E> extends BaseCollection<E> implements TinkerBag<E>, OclStdLibBag<E> {

	protected OclStdLibBag<E> oclStdLibBag;
//	protected LinkedListMultimap<Object, Vertex> internalVertexMultiMap = LinkedListMultimap.create();
	
	public BaseBag(TumlRuntimeProperty runtimeProperty) {
		super(runtimeProperty);
		this.internalCollection = HashMultiset.create();
		this.oclStdLibBag = new OclStdLibBagImpl<E>((Multiset<E>)this.internalCollection); 
		this.oclStdLibCollection = this.oclStdLibBag;
	}
	
	public BaseBag(UmlgNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.internalCollection = HashMultiset.create();
		this.oclStdLibBag = new OclStdLibBagImpl<E>((Multiset<E>)this.internalCollection); 
		this.oclStdLibCollection = this.oclStdLibBag;
	}

    @Override
    protected void addToLinkedList(Edge edge) {
        throw new RuntimeException("addToLinkedList and manageLinkedListInverse should never be called for a BaseSet!");
    }
	
//	protected Vertex removeFromInternalMap(Object o) {
//		List<Vertex> vertexList = this.internalVertexMultiMap.get(o);
//		if (!vertexList.isEmpty()) {
//			Vertex vertex = vertexList.get(0);
//			this.internalVertexMultiMap.remove(o, vertex);
//			return vertex;
//		} else {
//			return null;
//		}
//	}
//
//	@Override
//	protected void putToInternalMap(Object key, Vertex vertex) {
//		this.internalVertexMultiMap.put(key, vertex);
//	}

	protected Multiset<E> getInternalBag() {
		return (Multiset<E>) this.internalCollection;
	}
	
	@Override
	public int count(Object element) {
		maybeLoad();
		return this.getInternalBag().count(element);
	}

	@Override
	public int add(E element, int occurrences) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public int remove(Object element, int occurrences) {
		maybeLoad();
		int count = count(element);
		if (count > occurrences) {
			for (int i = 0; i < occurrences; i++) {
				remove(element);
			}
		} else {
			for (int i = 0; i < count; i++) {
				remove(element);
			}
		}
		return count;
	}
	
	@Override
	public void clear() {
		maybeLoad();
		Multiset<E> tmp = HashMultiset.create();
		tmp.addAll(this.getInternalBag());
		for (E e : tmp) {
			this.remove(e);
		}
	}

	@Override
	public int setCount(E element, int count) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean setCount(E element, int oldCount, int newCount) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Set<E> elementSet() {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Set<com.google.common.collect.Multiset.Entry<E>> entrySet() {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibBag.collectNested(v);
	}

	@Override
	public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibBag.collect(v);
	}

	@Override
	public <R> TinkerBag<R> flatten() {
		maybeLoad();
		return this.oclStdLibBag.flatten();
	}
	
	@Override
	public TinkerBag<E> select(BooleanExpressionEvaluator<E> v) {
		maybeLoad();
		return this.oclStdLibBag.select(v);
	}

	@Override
	public Boolean equals(TinkerBag<E> bag) {
		maybeLoad();
		return this.oclStdLibBag.equals(bag);
	}

	@Override
	public TinkerBag<E> union(TinkerBag<E> bag) {
		maybeLoad();
		return this.oclStdLibBag.union(bag);
	}

	@Override
	public TinkerBag<E> union(TinkerSet<E> set) {
		maybeLoad();
		return this.oclStdLibBag.union(set);
	}

	@Override
	public TinkerBag<E> intersection(TinkerBag<E> bag) {
		maybeLoad();
		return this.oclStdLibBag.intersection(bag);
	}

	@Override
	public TinkerSet<E> intersection(TinkerSet<E> set) {
		maybeLoad();
		return this.oclStdLibBag.intersection(set);
	}

	@Override
	public TinkerBag<E> including(E object) {
		maybeLoad();
		return this.oclStdLibBag.including(object);
	}

	@Override
	public TinkerBag<E> excluding(E object) {
		maybeLoad();
		return this.oclStdLibBag.excluding(object);
	}
	
}
