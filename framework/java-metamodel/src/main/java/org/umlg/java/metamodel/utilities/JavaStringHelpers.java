/** (c) Copyright 2003 by Klasse Objecten
 */
package org.umlg.java.metamodel.utilities;

import java.util.StringTokenizer;

/**
 *
 * @author anneke
 * @version $Id: JavaStringHelpers.java,v 1.2 2007/01/17 08:57:49 annekekleppe Exp $
 */
public class JavaStringHelpers {
	
	static public String[] convertValuesFromString(String value, String delimiter) {
		StringTokenizer tokenizer =
			new StringTokenizer(value, delimiter);
		int tokenCount = tokenizer.countTokens();
		String[] elements = new String[tokenCount];

		for (int i = 0; i < tokenCount; i++) {
			elements[i] = tokenizer.nextToken();
		}

		return elements;
	}

	static public String convertValuesToString(String[] elements, String delimiter) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < elements.length; i++) {
			buffer.append(elements[i]);
			buffer.append(delimiter);
		}
		return buffer.toString();
	}

	static public String newLine = System.getProperty("line.separator", "\n");

	static public char newLineChar = newLine.charAt(0);


	/** Add brackets ( '(' and ')' ) around <code>source</code> when it contains ant
	 * white space characters.
	 * @param source
	 * @return
	 */
	static public String addBrackets(String source) {
		if (source.indexOf(' ') != -1) { // string contains a space character
			source = "(" + source + ")";
		}
		return source;
	}


	static public String firstCharToLower( String orig ) {
		String result = "";
		String origFirst = orig.substring(0, 1);
		result = origFirst.toLowerCase();
		result = result + orig.substring(1, orig.length());
		return result;
	}

	static public String firstCharToUpper( String orig ) {
		String result = "";
		String origFirst = orig.substring(0, 1);
		result = origFirst.toUpperCase();
		result = result + orig.substring(1, orig.length());
		return result;
	}   

	static public String indent(String in, int level) {
		if (level <= 0) return in;
		StringBuilder result = internalIndent(in, level);
		return result.toString();		
	}

	static public StringBuilder indent(StringBuilder in, int level) {
		if (level <= 0) return in;
		StringBuilder result = internalIndent(in.toString(), level);
		return result;		
	}

	static private StringBuilder internalIndent(String in, int level) {
		StringBuilder result = new StringBuilder();
		String newIndent = "";
		for (int i=0; i<level; i++) {
			newIndent = newIndent + "\t";
		}
		String temp = in.toString();
		temp.trim(); // remove all whitespace from begin and end
		temp = newIndent + JavaStringHelpers.replaceAllSubstrings(temp, "\n", "\n" + newIndent);
		result.append(temp);
		if (result.charAt(result.length()-1) == '\t'){
			result.deleteCharAt(result.length()-1);
		}
		return result;
	}
	
	static public String replaceAllSubstrings( String orig, String origSub, String newSub ) {
		return orig.replaceAll(origSub, newSub);
	}       

	static public StringBuilder replaceAllSubstrings( StringBuilder orig, String origSub, String newSub ) {
		StringBuilder result = new StringBuilder();
		result.append(orig.toString().replaceAll(origSub, newSub));
		return result;    			
	}
	
	/**
	 * replaces 'origSub' with 'newSub' in the String 'orig'.
	 * @param orig
	 * @param origSub
	 * @param newSub
	 * @return String
	 */ 
	static public String replaceFirstSubstring( String orig, String origSub, String newSub ){
		int first = orig.indexOf( origSub );
		if (first == -1 ) {
			return orig;
		}
		int last = first + origSub.length();
		String result = orig.substring( 0, first );
		result = result + newSub + orig.substring( last );
		return result;    		
	} 
  

	public static String replaceLastSubstring(String orig, String origSub, String newSub ) {
		int first = orig.lastIndexOf( origSub );
			if (first == -1 ) {
				return orig;
			}
			int last = first + origSub.length();
			String result = orig.substring( 0, first );
			result = result + newSub + orig.substring( last );
			return result;    		
	}


	/**
	 * @param innerBody
	 */
	public static void trimTrailingWhiteSpace(StringBuilder innerBody) {
		while (Character.isWhitespace(innerBody.charAt(innerBody.length()-1))) {
			innerBody.deleteCharAt(innerBody.length()-1);
		}
		
	}



}
