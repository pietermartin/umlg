package org.umlg.java.metamodel;

import java.util.Set;

import org.umlg.java.metamodel.generated.OJSwitchCaseGEN;
import org.umlg.java.metamodel.utilities.JavaStringHelpers;



public class OJSwitchCase extends OJSwitchCaseGEN {

    private boolean breakInCase = true;

/*********************************************************************
 * The constructor
 ********************************************************************/
	public OJSwitchCase() {
		super();
		this.setBody(new OJBlock());
	}

	public String toJavaString() {
        StringBuilder sb = new StringBuilder();
        sb.append("case ");
        sb.append(getLabel());
        sb.append(":\n");
        sb.append(JavaStringHelpers.indent(getBody().toJavaString(), 1));
        sb.append("\n");
        if (this.breakInCase) {
            sb.append(JavaStringHelpers.indent("break;", 1));
            sb.append("\n");

        }
		return sb.toString();
	}

    public void setBreakInCase(boolean breakInCase) {
        this.breakInCase = breakInCase;
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