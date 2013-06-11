package org.tuml.runtime.adaptor;

import com.tinkerpop.pipes.AbstractPipe;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Date: 2013/06/10
 * Time: 10:02 PM
 */
public class GremlinToStringPipe <S> extends AbstractPipe<S, String> {

    @Override
    protected String processNextStart() throws NoSuchElementException {
        S s = this.starts.next();
        if (s instanceof String) {
            return (String)s;
        } else if (s instanceof Map) {
            Map map = (Map)s;
            StringBuilder result = new StringBuilder();
            for (Object key : map.keySet()) {
                result.append(key);
                result.append(": ");
                result.append(map.get(key));
                result.append("\\n");
            }
            return result.toString();
        } else {
            return s.toString();
        }
    }

}
