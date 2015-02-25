package org.umlg.runtime.collection.persistent;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.collection.UmlgBag;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibBag;
import org.umlg.runtime.collection.ocl.OclStdLibBagImpl;
import org.umlg.runtime.domain.UmlgMetaNode;
import org.umlg.runtime.domain.UmlgNode;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

public abstract class BaseBag<E> extends BaseCollection<E> implements UmlgBag<E>, OclStdLibBag<E> {

	protected OclStdLibBag<E> oclStdLibBag;

	public BaseBag(UmlgRuntimeProperty runtimeProperty) {
		super(runtimeProperty);
		this.internalCollection = HashMultiset.create();
		this.oclStdLibBag = new OclStdLibBagImpl<E>((Multiset<E>)this.internalCollection); 
		this.oclStdLibCollection = this.oclStdLibBag;
	}
	
	public BaseBag(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.internalCollection = HashMultiset.create();
		this.oclStdLibBag = new OclStdLibBagImpl<E>((Multiset<E>)this.internalCollection); 
		this.oclStdLibCollection = this.oclStdLibBag;
	}

    @Override
    protected void addToLinkedList(Edge edge) {
        throw new RuntimeException("addToLinkedList and manageLinkedListInverse should never be called for a BaseSet!");
    }
	
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

    /**
     * bags need to go via the edges as the vertex may be duplicated
     * @return
     */
    protected void loadManyNotPrimitiveNotDataType() {
        for (Iterator<Edge> iter = getEdges(); iter.hasNext(); ) {
            Edge edge = iter.next();
            E node;
            try {
                Class<?> c = this.getClassToInstantiate(edge);
                if (c.isEnum()) {
                    Object value = this.getVertexForDirection(edge).value(getPersistentName());
                    node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                    putToInternalMap(node, this.getVertexForDirection(edge));
                } else if (UmlgMetaNode.class.isAssignableFrom(c)) {
                    Method m = c.getDeclaredMethod("getInstance", new Class[0]);
                    node = (E) m.invoke(null);
                } else if (UmlgNode.class.isAssignableFrom(c)) {
                    node = (E) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
                } else {
                    Object value = this.getVertexForDirection(edge).value(getPersistentName());
                    node = (E) value;
                    putToInternalMap(value, this.getVertexForDirection(edge));
                }
                this.internalCollection.add(node);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
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
	public <R> UmlgBag<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibBag.collectNested(v);
	}

	@Override
	public <T, R> UmlgBag<T> collect(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibBag.collect(v);
	}

	@Override
	public <R> UmlgBag<R> flatten() {
		maybeLoad();
		return this.oclStdLibBag.flatten();
	}
	
	@Override
	public UmlgBag<E> select(BooleanExpressionEvaluator<E> v) {
		maybeLoad();
		return this.oclStdLibBag.select(v);
	}

	@Override
	public Boolean equals(UmlgBag<E> bag) {
		maybeLoad();
		return this.oclStdLibBag.equals(bag);
	}

	@Override
	public UmlgBag<E> union(UmlgBag<E> bag) {
		maybeLoad();
		return this.oclStdLibBag.union(bag);
	}

	@Override
	public UmlgBag<E> union(UmlgSet<E> set) {
		maybeLoad();
		return this.oclStdLibBag.union(set);
	}

	@Override
	public UmlgBag<E> intersection(UmlgBag<E> bag) {
		maybeLoad();
		return this.oclStdLibBag.intersection(bag);
	}

	@Override
	public UmlgSet<E> intersection(UmlgSet<E> set) {
		maybeLoad();
		return this.oclStdLibBag.intersection(set);
	}

	@Override
	public UmlgBag<E> including(E object) {
		maybeLoad();
		return this.oclStdLibBag.including(object);
	}

	@Override
	public UmlgBag<E> excluding(E object) {
		maybeLoad();
		return this.oclStdLibBag.excluding(object);
	}
	
}
