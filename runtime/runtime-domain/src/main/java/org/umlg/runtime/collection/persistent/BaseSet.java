package org.umlg.runtime.collection.persistent;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.umlg.runtime.collection.UmlgBag;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibSet;
import org.umlg.runtime.collection.ocl.OclStdLibSetImpl;
import org.umlg.runtime.domain.UmlgEnum;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseSet<E> extends BaseCollection<E> implements UmlgSet<E>, OclStdLibSet<E> {

	protected OclStdLibSet<E> oclStdLibSet;
	
	public BaseSet(UmlgRuntimeProperty runtimeProperty) {
		super(runtimeProperty);
		this.internalCollection = new HashSet<>();
		this.oclStdLibSet = new OclStdLibSetImpl<>((Set<E>)this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
	}

	public BaseSet(UmlgNode owner, PropertyTree propertyTree) {
		super(owner, propertyTree);
		this.internalCollection = new HashSet<>();
		this.oclStdLibSet = new OclStdLibSetImpl<>((Set<E>)this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
	}

	public BaseSet(UmlgNode owner, PropertyTree propertyTree, boolean loaded) {
		super(owner, propertyTree, loaded);
		this.internalCollection = new HashSet<>();
		this.oclStdLibSet = new OclStdLibSetImpl<>((Set<E>)this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
	}

	public BaseSet(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.internalCollection = new HashSet<>();
		this.oclStdLibSet = new OclStdLibSetImpl<>((Set<E>)this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
	}

	public BaseSet(UmlgEnum owner, UmlgRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.internalCollection = new HashSet<E>();
		this.oclStdLibSet = new OclStdLibSetImpl<E>((Set<E>)this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
	}

	@Override
	protected void addToInverseLinkedList(Edge edge) {
		if (!this.isControllingSide()) {
			edge.property(BaseCollection.IN_EDGE_SEQUENCE_ID, (double)this.inverseCollectionSize);
		} else {
			edge.property(BaseCollection.OUT_EDGE_SEQUENCE_ID, (double)this.inverseCollectionSize);
		}
	}

	@Override
	protected Collection convertToArrayCollection() {
		return new HashSet<>(this.internalCollection.size() + 1);
	}

    @Override
    protected void addToLinkedList(Edge edge) {
        throw new RuntimeException("addToLinkedList and manageLinkedListInverse should never be called for a BaseSet!");
    }



	protected Set<E> getInternalSet() {
		return (Set<E>) this.internalCollection;
	}
	
	@Override
	public void clear() {
		maybeLoad();
		for (E e : new HashSet<E>(this.getInternalSet())) {
			this.remove(e);
		}
	}	

	@Override
	public <R> UmlgBag<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibSet.collectNested(v);
	}

	@Override
	public <T, R> UmlgBag<T> collect(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibSet.collect(v);
	}
	
	@Override
	public <R> UmlgSet<R> flatten() {
		maybeLoad();
		return this.oclStdLibSet.flatten();
	}

	@Override
	public UmlgSet<E> select(BooleanExpressionEvaluator<E> v) {
		maybeLoad();
		return this.oclStdLibSet.select(v);
	}

	@Override
	public UmlgSet<E> union(UmlgSet<? extends E> s) {
		maybeLoad();
		return this.oclStdLibSet.union(s);
	}

	@Override
	public UmlgBag<E> union(UmlgBag<? extends E> bag) {
		maybeLoad();
		return this.oclStdLibSet.union(bag);
	}

	@Override
	public Boolean equals(UmlgSet<E> s) {
		maybeLoad();
		return this.oclStdLibSet.equals(s);
	}

	@Override
	public UmlgSet<E> intersection(UmlgSet<E> s) {
		maybeLoad();
		return this.oclStdLibSet.intersection(s);
	}

	@Override
	public UmlgSet<E> intersection(UmlgBag<E> bag) {
		maybeLoad();
		return this.oclStdLibSet.intersection(bag);
	}

	@Override
	public UmlgSet<E> subtract(UmlgSet<E> s) {
		maybeLoad();
		return this.oclStdLibSet.subtract(s);
	}

	@Override
	public UmlgSet<E> including(E e) {
		maybeLoad();
		return this.oclStdLibSet.including(e);
	}

	@Override
	public UmlgSet<E> excluding(E e) {
		maybeLoad();
		return this.oclStdLibSet.excluding(e);
	}

	@Override
	public UmlgSet<E> symmetricDifference(UmlgSet<E> s) {
		maybeLoad();
		return this.oclStdLibSet.symmetricDifference(s);
	}

	@Override
	public UmlgOrderedSet<E> sortedBy(Comparator<E> comparator) {
		maybeLoad();
		return this.oclStdLibSet.sortedBy(comparator);
	}
}
