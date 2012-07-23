package org.util;

import org.opaeum.runtime.domain.AbstractFormatter;

public class OrgFormatter extends AbstractFormatter implements IOrgFormatter {
	static final private ThreadLocal<OrgFormatter> INSTANCE = new ThreadLocal<OrgFormatter>();


	static public OrgFormatter getInstance() {
		OrgFormatter result = INSTANCE.get();
		if ( result==null ) {
			INSTANCE.set(result=new OrgFormatter());
		}
		return result;
	}

}