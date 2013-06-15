package org.umlg.java.metamodel;

import org.umlg.java.metamodel.generated.OJStatementGEN;

public abstract class OJStatement extends OJStatementGEN{
	public OJStatement(){
		super();
	}
	public abstract OJStatement getDeepCopy();
	public void copyDeepInfoInto(OJStatement copy){
	}
}