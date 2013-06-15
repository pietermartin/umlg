package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import org.umlg.runtime.collection.TinkerBag;
import org.umlg.runtime.collection.TinkerSet;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibSet;
import org.umlg.runtime.collection.ocl.OclStdLibSetImpl;
import org.umlg.runtime.domain.TumlNode;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseSet<E> extends BaseCollection<E> implements TinkerSet<E>, OclStdLibSet<E> {

	protected OclStdLibSet<E> oclStdLibSet;
	
	public BaseSet(TumlRuntimeProperty runtimeProperty) {
		super(runtimeProperty);
		this.internalCollection = new HashSet<E>();
		this.oclStdLibSet = new OclStdLibSetImpl<E>((Set<E>)this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
	}
	
	public BaseSet(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.internalCollection = new HashSet<E>();
		this.oclStdLibSet = new OclStdLibSetImpl<E>((Set<E>)this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
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
	public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibSet.collectNested(v);
	}

	@Override
	public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibSet.collect(v);
	}
	
	@Override
	public <R> TinkerSet<R> flatten() {
		maybeLoad();
		return this.oclStdLibSet.flatten();
	}

	@Override
	public TinkerSet<E> select(BooleanExpressionEvaluator<E> v) {
		maybeLoad();
		return this.oclStdLibSet.select(v);
	}

	@Override
	public TinkerSet<E> union(TinkerSet<? extends E> s) {
		maybeLoad();
		return this.oclStdLibSet.union(s);
	}

	@Override
	public TinkerBag<E> union(TinkerBag<? extends E> bag) {
		maybeLoad();
		return this.oclStdLibSet.union(bag);
	}

	@Override
	public Boolean equals(TinkerSet<E> s) {
		maybeLoad();
		return this.oclStdLibSet.equals(s);
	}

	@Override
	public TinkerSet<E> intersection(TinkerSet<E> s) {
		maybeLoad();
		return this.oclStdLibSet.intersection(s);
	}

	@Override
	public TinkerSet<E> intersection(TinkerBag<E> bag) {
		maybeLoad();
		return this.oclStdLibSet.intersection(bag);
	}

	@Override
	public TinkerSet<E> subtract(TinkerSet<E> s) {
		maybeLoad();
		return this.oclStdLibSet.subtract(s);
	}

	@Override
	public TinkerSet<E> including(E e) {
		maybeLoad();
		return this.oclStdLibSet.including(e);
	}

	@Override
	public TinkerSet<E> excluding(E e) {
		maybeLoad();
		return this.oclStdLibSet.excluding(e);
	}

	@Override
	public TinkerSet<E> symmetricDifference(TinkerSet<E> s) {
		maybeLoad();
		return this.oclStdLibSet.symmetricDifference(s);
	}

}
