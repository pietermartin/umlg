/*
 * Created on Nov 1, 2004
 *
 */
package org.tuml.java.metamodel.utilities;

import java.util.Comparator;

import org.tuml.java.metamodel.OJPathName;



/**
 * @author anneke
 *
 */
public class OJPathNameComparator implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		OJPathName first = null;
		OJPathName second = null;
		if (arg0 instanceof OJPathName) {
			first = (OJPathName) arg0;
		}
		if (arg1 instanceof OJPathName) {
			second = (OJPathName) arg1;
		}
		return first.toString().compareTo(second.toString());
	}

}
