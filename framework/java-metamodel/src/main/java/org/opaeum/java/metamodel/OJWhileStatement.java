package org.opaeum.java.metamodel;

import java.util.Set;

import org.opaeum.java.metamodel.generated.OJWhileStatementGEN;
import org.opaeum.java.metamodel.utilities.JavaStringHelpers;



public class OJWhileStatement extends OJWhileStatementGEN {


/*********************************************************************
 * The constructor
 ********************************************************************/
	public OJWhileStatement() {
		super();
		this.setBody(new OJBlock());
	}
/*********************************************************************
 * The operations from the model
 ********************************************************************/
	public String toJavaString() {
		String result = "while ( " + getCondition() + " ) {\n";
		result = result + JavaStringHelpers.indent(getBody().toJavaString(), 1) + "\n}";
		return result;
	}
	
	public OJWhileStatement getDeepCopy() {
		OJWhileStatement copy = new OJWhileStatement();
		copyDeepInfoInto(copy);
		return copy;
	}
	
	public void copyDeepInfoInto(OJWhileStatement copy) {
		super.copyDeepInfoInto(copy);
		copy.setCondition(getCondition());
		if ( getBody() != null ) {
			copy.setBody(getBody().getDeepCopy());
		}		
	}	
	
	public void renameAll(Set<OJPathName> renamePathNames, String newName) {
		if ( getBody() != null ) {
			getBody().renameAll(renamePathNames, newName);
		}
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