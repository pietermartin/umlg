package org.opaeum.java.metamodel;

import java.util.Set;

import org.opaeum.java.metamodel.generated.OJFieldGEN;

public class OJField extends OJFieldGEN{
	/******************************************************
	 * The constructor for this classifier.
	 *******************************************************/
	public OJField(){
		super();
		setVisibility(OJVisibilityKind.PRIVATE);
	}
	public String toJavaString(){
		String result = "";
		if(this.getOwner() != null){ // field is part of block statement
			result = result + visToJava(this);
		}
		if(result.length() > 0)
			result = result + " ";
		result = result + getType().getCollectionTypeName();
		result = result + " " + getName();
		if(getInitExp() != null && !getInitExp().equals("")){
			result = result + " = " + getInitExp();
		}
		result = result + ";";
		if(!getComment().equals("")){
			result = result + "\t// " + getComment();
		}
		return result;
	}
	public OJField getDeepCopy(){
		OJField copy = new OJField();
		copyDeepInfoInto(copy);
		return copy;
	}
	public void copyDeepInfoInto(OJField copy){
		super.copyDeepInfoInto(copy);
		copy.setInitExp(getInitExp());
		if(getType() != null){
			copy.setType(getType().getDeepCopy());
		}
	}
	public void renameAll(Set<OJPathName> renamePathNames,String suffix){
		getType().renameAll(renamePathNames, suffix);
		String init = getInitExp();
		setInitExp(replaceAll(init, renamePathNames, suffix));
	}
}