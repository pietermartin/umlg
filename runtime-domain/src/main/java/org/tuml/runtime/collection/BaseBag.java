package org.tuml.runtime.collection;

import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public abstract class BaseBag<E> extends BaseCollection<E> implements TinkerBag<E> {

//	protected Multiset<E> internalBag = HashMultiset.create();

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

}
