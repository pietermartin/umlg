package org.tuml.java.metamodel;

import java.util.Set;

import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.generated.OJFieldGEN;

public class OJField extends OJFieldGEN {
	/******************************************************
	 * The constructor for this classifier.
	 *******************************************************/
	public OJField() {
		super();
		setVisibility(OJVisibilityKind.PRIVATE);
	}

	public OJField(OJAnnotatedClass clazz, String name, OJPathName type) {
		this(name, type);
		clazz.addToFields(this);
	}

	public OJField(OJBlock block, String name, OJPathName type) {
		this(name, type);
		block.addToLocals(this);
	}

	public OJField(String name, OJPathName type) {
		setName(name);
		setType(type);
	}

	public OJField(OJBlock body, String name, OJPathName type, String initExpr) {
		this(body, name, type);
		setInitExp(initExpr);
	}

	public OJField(String name, String path) {
		this(name, new OJPathName(path));
	}

	public String toJavaString() {
		String result = "";
		if (this.getOwner() != null) { // field is part of block statement
			result = result + visToJava(this);
		}
		if (result.length() > 0)
			result = result + " ";
		result = result + getType().getCollectionTypeName();
		result = result + " " + getName();
		if (getInitExp() != null && !getInitExp().equals("")) {
			result = result + " = " + getInitExp();
		}
		result = result + ";";
		if (!getComment().equals("")) {
			result = result + "\t// " + getComment();
		}
		return result;
	}

	public OJField getDeepCopy() {
		OJField copy = new OJField();
		copyDeepInfoInto(copy);
		return copy;
	}

	public void copyDeepInfoInto(OJField copy) {
		super.copyDeepInfoInto(copy);
		copy.setInitExp(getInitExp());
		if (getType() != null) {
			copy.setType(getType().getDeepCopy());
		}
	}

	public void renameAll(Set<OJPathName> renamePathNames, String suffix) {
		getType().renameAll(renamePathNames, suffix);
		String init = getInitExp();
		setInitExp(replaceAll(init, renamePathNames, suffix));
	}
}