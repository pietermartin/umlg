package org.umlg.runtime.collection;

/**
 * Date: 2013/05/02
 * Time: 7:48 AM
 */
@FunctionalInterface
public interface Filter<TumlNode> {

      boolean filter(TumlNode t);

}
