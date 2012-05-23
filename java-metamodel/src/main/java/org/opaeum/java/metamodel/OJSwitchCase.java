package org.opaeum.java.metamodel;

import java.util.Set;

import org.opaeum.java.metamodel.generated.OJSwitchCaseGEN;
import org.opaeum.java.metamodel.utilities.JavaStringHelpers;



public class OJSwitchCase extends OJSwitchCaseGEN {


/*********************************************************************
 * The constructor
 ********************************************************************/
	public OJSwitchCase() {
		super();
		this.setBody(new OJBlock());
	}

	public String toJavaString() {
		String result = "case " + getLabel() + ":\n" + 
						JavaStringHelpers.indent(getBody().toJavaString(), 1) + "\n" +
						"break;\n";
		return result;
	}

	public OJSwitchCase getDeepCopy() {
		OJSwitchCase copy = new OJSwitchCase();
		copyDeepInfoInto(copy);
		return copy;
	}
	
	public void copyDeepInfoInto(OJSwitchCase copy) {
		super.copyDeepInfoInto(copy);
		copy.setLabel(getLabel());
		if ( getBody() != null ) {
			copy.setBody(getBody().getDeepCopy());
		}		
	}
	
	public void renameAll(Set<OJPathName> renamePathNames, String newName) {
		if ( getBody() != null ) {
			getBody().renameAll(renamePathNames, newName);
		}	
	}	

}