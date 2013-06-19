package org.umlg.java.metamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.umlg.java.metamodel.generated.OJSwitchStatementGEN;
import org.umlg.java.metamodel.utilities.JavaStringHelpers;

public class OJSwitchStatement extends OJSwitchStatementGEN {

	/*********************************************************************
	 * The constructor
	 ********************************************************************/
	public OJSwitchStatement() {
		super();
	}

	public String toJavaString() {
		String result = "switch ( " + getCondition() + " ) {\n";
		Iterator cases = getCases().iterator();
		while (cases.hasNext()) {
			OJSwitchCase c = (OJSwitchCase) cases.next();
			result = result + JavaStringHelpers.indent(c.toJavaString(), 1) + "\n";
		}
		OJSwitchCase c = getDefCase();
		if (c != null) {
			result = result + JavaStringHelpers.indent("default", 1) + c.getLabel() + ":\n" + JavaStringHelpers.indent(c.getBody().toJavaString(), 2) + "\n"
					;

		}
		result = result + "}\n";
		return result;
	}

	public OJSwitchStatement getDeepCopy() {
		OJSwitchStatement copy = new OJSwitchStatement();
		copyDeepInfoInto(copy);
		return copy;
	}

	public void copyDeepInfoInto(OJSwitchStatement copy) {
		super.copyDeepInfoInto(copy);
		copy.setCondition(getCondition());
		Iterator casesIt = new ArrayList<OJSwitchCase>(getCases()).iterator();
		while (casesIt.hasNext()) {
			OJSwitchCase elem = (OJSwitchCase) casesIt.next();
			copy.addToCases(elem.getDeepCopy());
		}
		if (getDefCase() != null) {
			copy.setDefCase(getDefCase().getDeepCopy());
		}
	}

	public void renameAll(Set<OJPathName> renamePathNames, String newName) {
		Iterator casesIt = new ArrayList<OJSwitchCase>(getCases()).iterator();
		while (casesIt.hasNext()) {
			OJSwitchCase elem = (OJSwitchCase) casesIt.next();
			elem.renameAll(renamePathNames, newName);
		}
		if (getDefCase() != null) {
			getDefCase().renameAll(renamePathNames, newName);
		}
	}

}