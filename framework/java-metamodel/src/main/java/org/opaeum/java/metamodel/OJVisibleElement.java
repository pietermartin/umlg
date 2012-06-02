package org.opaeum.java.metamodel;

import org.opaeum.java.metamodel.generated.OJVisibleElementGEN;

public abstract class OJVisibleElement extends OJVisibleElementGEN {
	
	/******************************************************
	 * The constructor for this classifier.
	*******************************************************/	
	public OJVisibleElement() {
		super();
	}

	public String visToJava(OJVisibleElement elem){
		StringBuilder visInfo = new StringBuilder();
		if (elem.isStatic()) visInfo.append("static ");
		if (elem.isFinal()) visInfo.append("final ");
		if (elem.isVolatile()) visInfo.append("volatile ");
		if (elem.getVisibility() != OJVisibilityKind.DEFAULT) {
			visInfo.append(elem.getVisibility().toString());
		}
		return visInfo.toString();
	}
	
	public void copyDeepInfoInto(OJField copy) {
		super.copyInfoInto(copy);
	}
}