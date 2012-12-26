package org.tuml.java.metamodel;

import org.tuml.java.metamodel.generated.OJConstructorGEN;
import org.tuml.java.metamodel.utilities.JavaStringHelpers;
import org.tuml.java.metamodel.utilities.JavaUtil;



public class OJConstructor extends OJConstructorGEN {
	
	/******************************************************
	 * The constructor for this classifier.
	*******************************************************/	
	public OJConstructor() {
		super();
	}

	public OJClassifier getOwner() {
		return this.getOwningClass();
	}
	public OJConstructor getDeepCopy(){
		OJConstructor result = new OJConstructor();
		copyValuesDeep(result);
		return result;
	}
	public String toJavaString(){
		StringBuilder result = new StringBuilder();
		if (getComment().equals("")){
			setComment("constructor for " + getOwner().getName());
		}
		addJavaDocComment(result);
		result.append(visToJava(this) + " " + getOwner().getName());
		// params 
		result.append("(" + paramsToJava(this) + ") {\n");
		// body
		StringBuilder bodyStr = new StringBuilder();
		bodyStr.append(JavaUtil.collectionToJavaString(getBody().getStatements(),"\n"));
		result.append(JavaStringHelpers.indent(bodyStr, 1));
		if (result.charAt(result.length()-1) == '\n'){
			result.deleteCharAt(result.length()-1);
		}
		// closing bracket
		result.append("\n}\n");			
		return result.toString();	
	}
	
	public OJConstructor getConstructorCopy() {
		OJConstructor result = new OJConstructor();
		super.copyValues(result);
		return result;
	}

	public OJConstructor getDeepConstructorCopy() {
		OJConstructor result = new OJConstructor();
		super.copyValuesDeep(result);
		return result;
	}

}