package org.tuml.java.metamodel;

import java.util.Set;

import org.tuml.java.metamodel.generated.OJParameterGEN;


public class OJParameter extends OJParameterGEN {
	protected boolean isFinal;

	/******************************************************
	 * The constructor for this classifier.
	 *******************************************************/
	public OJParameter() {
		super();
	}

    public OJParameter(String string, String ojPathName) {
        this();
        setName(string);
        setType(new OJPathName(ojPathName));
    }

    public OJParameter(String string, OJPathName ojPathName) {
		this();
		setName(string);
		setType(ojPathName);
	}

	public String toJavaString() {
		if (getType() == null) {
			System.err.println("type of param " + getName() + " is null");
			return "";
		}
		StringBuilder result = new StringBuilder();
		if (isFinal) {
			result.append("final ");
		}
		result.append(getType().getCollectionTypeName());
		result.append(" ");
		result.append(getName());
		return result.toString();
	}

	public void renameAll(Set<OJPathName> renamePathNames, String newName) {
		getType().renameAll(renamePathNames, newName);
	}

	public OJParameter getDeepCopy() {
		OJParameter result = new OJParameter();
		this.copyDeepInfoInto(result);
		return result;
	}

	public void copyDeepInfoInto(OJParameter copy) {
		super.copyDeepInfoInto(copy);
		if (getType() != null) {
			copy.setType(getType().getDeepCopy());
			copy.setFinal(isFinal);
		}
	}

	public void setFinal(boolean b) {
		this.isFinal = b;
	}
}