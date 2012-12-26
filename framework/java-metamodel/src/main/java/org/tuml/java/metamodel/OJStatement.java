package org.tuml.java.metamodel;

import org.tuml.java.metamodel.generated.OJStatementGEN;

public abstract class OJStatement extends OJStatementGEN{
	public OJStatement(){
		super();
	}
	public abstract OJStatement getDeepCopy();
	public void copyDeepInfoInto(OJStatement copy){
	}
}