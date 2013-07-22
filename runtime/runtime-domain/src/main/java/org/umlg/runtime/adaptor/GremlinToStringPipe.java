package org.umlg.runtime.adaptor;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.iterators.SingleIterator;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Date: 2013/06/13
 * Time: 9:32 PM
 */
//TODO remove if tinkerpop accepts pull request
public class GremlinToStringPipe<S> extends AbstractPipe<S, String> implements TransformPipe<S, String> {

    private Iterator<Object> tempIterator = PipeHelper.emptyIterator();

    public GremlinToStringPipe(S starts) {
        setStarts(new SingleIterator<S>(starts));
    }

    @Override
    protected String processNextStart() throws NoSuchElementException {
        while (true) {
            if (this.tempIterator.hasNext()) {
                return this.tempIterator.next().toString();
            } else {
                final Object result = this.starts.next();
                if (result instanceof Iterator) {
                    tempIterator = (Iterator) result;
                } else if (result instanceof Iterable) {
                    tempIterator = ((Iterable) result).iterator();
                } else if (result instanceof Object[]) {
                    tempIterator = new ArrayIterator((Object[]) result);
                } else if (result instanceof Map) {
                    tempIterator = ((Map) result).entrySet().iterator();
                } else {
                    return result.toString();
                }
            }
        }
    }

    @Override
    public void reset() {
        this.tempIterator = PipeHelper.emptyIterator();
        super.reset();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        while (hasNext()) {
            result.append(next());
            result.append("\n");
        }
        return result.toString();
    }


}