package org.umlg.runtime.collection.ocl;

import org.umlg.runtime.collection.*;
import org.umlg.runtime.collection.memory.UmlgMemorySequence;
import org.umlg.runtime.domain.ocl.OclState;

import java.util.*;

public class OclStdLibCollectionImpl<E> implements OclStdLibCollection<E> {

	protected Collection<E> collection;
	
	public OclStdLibCollectionImpl(Collection<E> oclStdLibCollection) {
		this.collection = oclStdLibCollection;
	}
	
	@Override
	public boolean equals(UmlgCollection<E> c) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public boolean notEquals(UmlgCollection<E> c) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public int size() {
		return this.collection.size();
	}


	@Override
	public boolean includes(E t) {
        return this.collection.contains(t);
	}


	@Override
	public boolean excludes(E t) {
        return !includes(t);
	}


	@Override
	public int count(E e) {
        int count = 0;
        for (E element : this.collection) {
            if (element.equals(e)) {
                count++;
            }
        }
		return count;
	}


	@Override
	public Boolean includesAll(UmlgCollection<E> c) {
        for (E e : c) {
            if (!includes(e)) {
                 return false;
            }
        }
		return true;
	}


	@Override
	public Boolean excludesAll(UmlgCollection<E> c) {
        for (E e : c) {
            if (includes(e)) {
                return false;
            }
        }
        return true;
	}


	@Override
	public boolean isEmpty() {
        return this.collection.isEmpty();
	}

	@Override
	public Boolean notEmpty() {
        return !isEmpty();
	}

	@Override
	public E max() {
        if (isEmpty()) {
            return null;
        } else {
            E e = this.collection.iterator().next();
            if (!(e instanceof Integer || e instanceof Long || e instanceof Double || e instanceof Float)) {
                throw new RuntimeException("max can only be called on a collection with elements of type Integer, Long or Double");
            }
        }
        boolean first = true;
        Number max = null;
        for (E e : this.collection) {
            if (first) {
                first = false;
                max = (Number)e;
            }
            if (e instanceof Integer) {
                max = Math.max((Integer) max, (Integer) e);
            } else if (e instanceof Long) {
                max = Math.max((Long) max, (Long) e);
            } else if (e instanceof Float) {
                max = Math.max((Float) max, (Float) e);
            } else {
                max = Math.max((Double) max, (Double) e);
            }
        }
		return (E)max;
	}


	@Override
	public E min() {
        if (isEmpty()) {
            return null;
        } else {
            E e = this.collection.iterator().next();
            if (!(e instanceof Integer || e instanceof Long || e instanceof Double || e instanceof Float)) {
                throw new RuntimeException("max can only be called on a collection with elements of type Integer, Long or Double");
            }
        }
        boolean first = true;
        Number min = null;
        for (E e : this.collection) {
            if (first) {
                first = false;
                min = (Number)e;
            }
            if (e instanceof Integer) {
                min = Math.min((Integer) min, (Integer) e);
            } else if (e instanceof Long) {
                min = Math.min((Long) min, (Long) e);
            } else if (e instanceof Float) {
                min = Math.min((Float) min, (Float) e);
            } else {
                min = Math.min((Double) min, (Double) e);
            }
        }
        return (E)min;
	}


	@Override
	public E sum() {
        if (isEmpty()) {
            return null;
        } else {
            E e = this.collection.iterator().next();
            if (!(e instanceof Integer || e instanceof Long || e instanceof Double || e instanceof Float)) {
                throw new RuntimeException("max can only be called on a collection with elements of type Integer, Long or Double");
            }
        }
        Integer minInteger = 0;
        Long minLong = 0L;
        Float minFloat = 0F;
        Double minDouble = 0D;
        for (E e : this.collection) {
            if (e instanceof Integer) {
                minInteger = minInteger + (Integer)e;
            } else if (e instanceof Long) {
                minLong = minLong + (Long)e;
            } else if (e instanceof Float) {
                minFloat = minFloat + (Float)e;
            } else {
                minDouble = minDouble + (Double)e;
            }
        }
        if (minInteger > 0) {
            return (E)minInteger;
        } else if (minLong > 0) {
            return (E)minLong;
        } else if (minFloat > 0) {
            return (E)minFloat;
        } else {
            return (E)minDouble;
        }
	}


	@Override
	public UmlgSet<?> product(UmlgCollection<E> c) {
        throw new RuntimeException("Not implemented");
	}


	@Override
	public <T2> UmlgCollection<T2> flatten() {
        throw new RuntimeException("Not implemented");
	}



	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
	public UmlgCollection<E> select(BooleanExpressionEvaluator<E> e) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public <T, R> UmlgCollection<T> collect(BodyExpressionEvaluator<R, E> e) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public <R> UmlgCollection<R> collectNested(BodyExpressionEvaluator<R, E> e) {
		throw new RuntimeException("Not implemented");
	}

    @Override
    public <R> Boolean isUnique(BodyExpressionEvaluator<R, E> v) {
        Set<R> uniquenessSet = new HashSet<>();
        for (E e : this.collection) {
            R r = v.evaluate(e);
            if (!uniquenessSet.contains(r)) {
                uniquenessSet.add(r);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean exists(BooleanExpressionEvaluator<E> v) {
        for (E e : this.collection) {
            if (v.evaluate(e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean forAll(BooleanExpressionEvaluator<E> v) {
        for (E e : this.collection) {
            if (!v.evaluate(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
	public E any(BooleanExpressionEvaluator<E> v) {
		for (E e : this.collection) {
			if (v.evaluate(e)) {
				return e;
			}
		}
		return null;
	}
	
	@Override
	public UmlgSet<E> asSet() {
		return OclStdLibSetImpl.get(this.collection);
	}
	
	@Override
	public UmlgOrderedSet<E> asOrderedSet() {
		return OclStdLibOrderedSetImpl.get(this.collection);
	}
	
	@Override
	public UmlgSequence<E> asSequence() {
		return OclStdLibSequenceImpl.get(this.collection);
	}
	
	@Override
	public UmlgBag<E> asBag() {
		return OclStdLibBagImpl.get(this.collection);
	}
	
	@Override
	public <R> R iterate(IterateExpressionAccumulator<R, E> v) {
		R acc = v.initAccumulator();
		for (E e : this.collection) {
			acc = v.accumulate(acc, e);
		}
		return acc;
	}




	/*******************************
	 * OclAny  
	 *******************************/
	
	@Override
	public boolean equals(Object object) {
		return this.equals(object);
	}

	@Override
	public boolean notEquals(Object object) {
		return !equals(object);
	}

	@Override
	public Boolean oclIsNew() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsUndefined() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsInvalid() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public <T> T oclAsType(T classifier) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsTypeOf(Object object) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsKindOf(Object object) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean oclIsInState(OclState state) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public <T  extends Object> Class<T> oclType() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String oclLocale() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public UmlgCollection<E> sortedBy(Comparator<E> comparator) {
		ArrayList<E> list = new ArrayList<>(this.collection);
		Collections.sort(list, comparator);
		UmlgSequence<E> result = new UmlgMemorySequence<>(list);
		return result;
	}
}
