package org.umlg.java.metamodel;

import java.util.Set;

import org.umlg.java.metamodel.generated.OJSimpleStatementGEN;


public class OJSimpleStatement extends OJSimpleStatementGEN {

/*********************************************************************
 * The constructor
 ********************************************************************/
	public OJSimpleStatement() {
		super();
	}
	public OJSimpleStatement(String expression) {
		super();
		this.setExpression(expression);
	}

/*********************************************************************
 * The operations from the model
 ********************************************************************/
	public OJSimpleStatement getCopy() {
		OJSimpleStatement result = new OJSimpleStatement();
		result.setExpression(this.getExpression());
		return result;
	}
	public String toJavaString() {
		String result = "";
		if (getExpression().length() != 0 ) result = getExpression();
//		if (result.length() > 0 && !(result.charAt(result.length()-1) == '}')) {
		if (result.length() > 0) {
			result = result + ";";
		}
		return result; 	
	}
	
	public OJSimpleStatement getDeepCopy() {
		OJSimpleStatement copy = new OJSimpleStatement();
		copyDeepInfoInto(copy);
		return copy;
	}
	
	public void copyDeepInfoInto(OJSimpleStatement copy) {
		super.copyDeepInfoInto(copy);
		copy.setExpression(this.getExpression());
	}	
	
	public void renameAll(Set<OJPathName> renamePathNames, String newName) {
		setExpression(replaceAll(getExpression(), renamePathNames, newName));
	}	
/*********************************************************************
 * The getters and setters
 ********************************************************************/

/*********************************************************************
 * Some utility operations
 ********************************************************************/

/*********************************************************************
 * Extra operations that implement the OCL expressions used
 ********************************************************************/

}