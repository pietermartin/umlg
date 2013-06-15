/** (c) Copyright 2002, Klasse Objecten
 */
package org.umlg.java.metamodel.utilities;

import java.util.Collection;
import java.util.Iterator;

import org.umlg.java.metamodel.OJElement;

public class JavaUtil{
	public JavaUtil(){
	}
	static public String collectionToJavaString(Collection<? extends OJElement> coll,String separator){
		if(coll == null){
			return "";
		}
		StringBuilder result = new StringBuilder();
		if(coll != null){
			Iterator<? extends OJElement> i = coll.iterator();
			while(i.hasNext()){
				OJElement o = i.next();
				result = result.append(o == null ? "<null>" : o.toJavaString());
				if(i.hasNext())
					result = result.append(separator);
			}
		}
		return result.toString();
	}
	static public String collectionToString(Collection<? extends Object> coll,String separator){
		if(coll == null){
			return "";
		}
		String result = "";
		if(coll != null){
			Iterator<? extends Object> i = coll.iterator();
			while(i.hasNext()){
				Object o = i.next();
				result = result + (o == null ? "<null>" : o.toString());
				if(i.hasNext())
					result = result + separator;
			}
		}
		return result;
	}
}
