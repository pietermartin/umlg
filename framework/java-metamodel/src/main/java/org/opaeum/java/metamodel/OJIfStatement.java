package org.opaeum.java.metamodel;

import java.util.Set;

import org.opaeum.java.metamodel.generated.OJIfStatementGEN;
import org.opaeum.java.metamodel.utilities.JavaStringHelpers;


public class OJIfStatement extends OJIfStatementGEN {
	/*********************************************************************
	 * The constructor
	 ********************************************************************/
	public OJIfStatement() {
		super();
		this.setThenPart(new OJBlock());
	}

	public OJIfStatement(String condition, String thenPart) {
		super();
		this.setThenPart(new OJBlock());
		this.setCondition(condition);
		this.addToThenPart(thenPart);
	}

	public OJIfStatement(String condition, String thenPart, String elsePart) {
		super();
		this.setThenPart(new OJBlock());
		this.setCondition(condition);
		this.addToThenPart(thenPart);
		this.addToElsePart(elsePart);
	}

	public OJIfStatement(String condition) {
		this.setThenPart(new OJBlock());
		this.setCondition(condition);
	}

	/*********************************************************************
	 * The operations from the model
	 ********************************************************************/
	/**
	 * @param string
	 */
	public void addToThenPart(String string) {
		this.getThenPart().addToStatements(string);
	}

	public void addToElsePart(String string) {
		if (this.getElsePart() == null) {
			this.setElsePart(new OJBlock());
		}
		this.getElsePart().addToStatements(string);
	}

	public void addToThenPart(OJStatement stat) {
		if (stat == null)
			return;
		this.getThenPart().addToStatements(stat);
	}

	public void addToElsePart(OJStatement stat) {
		if (this.getElsePart() == null) {
			this.setElsePart(new OJBlock());
		}
		this.getElsePart().addToStatements(stat);
	}

	public String toJavaString() {
		String result = "if ( " + getCondition() + " ) {\n";
		result = result + JavaStringHelpers.indent(getThenPart().toJavaString(), 1) + "\n}";
		if (getElsePart() != null) {
			result = result + " else {\n" + JavaStringHelpers.indent(getElsePart().toJavaString(), 1) + "\n}";
		}
		return result;
	}

	public OJIfStatement getDeepCopy() {
		OJIfStatement copy = new OJIfStatement();
		copyDeepInfoInto(copy);
		return copy;
	}

	public void copyDeepInfoInto(OJIfStatement copy) {
		super.copyDeepInfoInto(copy);
		copy.setCondition(getCondition());
		if (getThenPart() != null) {
			copy.setThenPart(getThenPart().getDeepCopy());
		}
		if (getElsePart() != null) {
			copy.setElsePart(getElsePart().getDeepCopy());
		}
	}

	public void renameAll(Set<OJPathName> renamePathNames, String suffix) {
		setCondition(replaceAll(getCondition(), renamePathNames, suffix));
		if (getThenPart() != null) {
			getThenPart().renameAll(renamePathNames, suffix);
		}
		if (getElsePart() != null) {
			getElsePart().renameAll(renamePathNames, suffix);
		}
	}
}