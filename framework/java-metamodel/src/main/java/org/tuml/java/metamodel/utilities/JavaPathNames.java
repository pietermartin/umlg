/*
 * Created on Jan 6, 2005
 *
 * Copyright Klasse Objecten
 */
package org.tuml.java.metamodel.utilities;

import org.tuml.java.metamodel.OJPathName;


/**
 * @author anneke
 *
 * PathNames ...
 */
public class JavaPathNames {
	
	static public final OJPathName Exception 			= new OJPathName("java.lang.Exception");
	static public final OJPathName String 				= new OJPathName("java.lang.String");
	static public final OJPathName StringArray			= new OJPathName("java.lang.String[]");
	static public final OJPathName StringBuilder		= new OJPathName("java.lang.StringBuilder");
	static public final OJPathName Object 				= new OJPathName("java.lang.Object");
	static public final OJPathName ObjectArray 		= new OJPathName("java.lang.Object[]");
	static public final OJPathName Int 					= new OJPathName("java.lang.int");
	static public final OJPathName Bool 					= new OJPathName("java.lang.boolean");
	static public final OJPathName Class 				= new OJPathName("java.lang.Class");
	static public final OJPathName Void 					= new OJPathName("java.lang.void");
	
	static public final OJPathName IOException 		= new OJPathName("java.io.IOException");
	static public final OJPathName File 					= new OJPathName("java.io.File");
	static public final OJPathName FileOutputStream = new OJPathName("java.io.FileOutputStream");
	static public final OJPathName OutputStream 	= new OJPathName("java.io.OutputStream");
	
	static public final OJPathName ArrayList 			= new OJPathName("java.util.ArrayList");
	static public final OJPathName Iterator 			= new OJPathName("java.util.Iterator"); 
	static public final OJPathName List 					= new OJPathName("java.util.List");
	static public final OJPathName Collection			= new OJPathName("java.util.Collection");
	static public final OJPathName Collections			= new OJPathName("java.util.Collections");
	static public final OJPathName Set 					= new OJPathName("java.util.Set");
	static public final OJPathName HashSet 			= new OJPathName("java.util.HashSet");
	static public final OJPathName TreeSet 			= new OJPathName("java.util.TreeSet");
	static public final OJPathName HashMap 			= new OJPathName("java.util.HashMap");
	static public final OJPathName Map 					= new OJPathName("java.util.Map");
	static public final OJPathName Comparator		= new OJPathName("java.util.Comparator");
	static public final OJPathName StringTokenizer	= new OJPathName("java.util.StringTokenizer");
	static public final OJPathName Date					= new OJPathName("java.util.Date");
	static public final OJPathName DateFormat		= new OJPathName("java.text.DateFormat");
	
	static private OJPathName		 listType				= null;
	static private OJPathName		 arrayListType		= null;
	static private OJPathName		 setType				= null;
	static private OJPathName		 hashSetType		= null;
	static private OJPathName		 mapType			= null;
	static private OJPathName		 hashMapType		= null;
	
	static public OJPathName ListOfObject() {
		if (listType == null) {
			listType = JavaPathNames.List.getCopy();
			listType.addToElementTypes(JavaPathNames.Object);
		}
		return listType;
	}
	
	static public OJPathName ArrayListOfObject() {
		if (arrayListType == null) {
			arrayListType = JavaPathNames.ArrayList.getCopy();
			arrayListType.addToElementTypes(JavaPathNames.Object);
		}
		return arrayListType;
	}
	
	static public OJPathName SetOfObject() {
		if (setType == null) {
			setType = JavaPathNames.Set.getCopy();
			setType.addToElementTypes(JavaPathNames.Object);
		}
		return setType;
	}
	
	static public OJPathName HashSetOfObject() {
		if (hashSetType == null) {
			hashSetType = JavaPathNames.HashSet.getCopy();
			hashSetType.addToElementTypes(JavaPathNames.Object);
		}
		return hashSetType;
	}
	
	static public OJPathName MapObjectString() {
		if (mapType == null) {
			mapType = JavaPathNames.Map.getCopy();
			mapType.addToElementTypes(JavaPathNames.Object);
			mapType.addToElementTypes(JavaPathNames.String);	
		}
		return mapType;
	}
	
	static public OJPathName HashMapObjectString() {
		if (hashMapType == null) {
			hashMapType = JavaPathNames.HashMap.getCopy();
			hashMapType.addToElementTypes(JavaPathNames.Object);
			hashMapType.addToElementTypes(JavaPathNames.String);	
		}
		return hashMapType;
	}
}
