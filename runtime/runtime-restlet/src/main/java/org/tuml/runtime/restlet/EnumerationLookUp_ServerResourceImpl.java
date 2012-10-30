package org.tuml.runtime.restlet;

import java.util.Arrays;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.tuml.runtime.domain.TumlEnum;
import org.tuml.runtime.domain.json.ToJsonUtil;

public class EnumerationLookUp_ServerResourceImpl extends ServerResource implements EnumerationLookUp_ServerResource {

	/**
	 * default constructor for Finger_finger_lookUpRing_ServerResourceImpl
	 */
	public EnumerationLookUp_ServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		String enumQualifiedName = getQuery().getFirst("enumQualifiedName").getValue();
		String enumClassName = enumQualifiedName;
		Class<?> enumClass;
		try {
			enumClass = Class.forName(enumClassName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		TumlEnum[] enumConstants = (TumlEnum[]) enumClass.getEnumConstants();
		StringBuilder json = new StringBuilder();
		json.append("{\"data\": [");	
		json.append(ToJsonUtil.enumsToJson(Arrays.asList(enumConstants)));
		json.append("]");
		json.append("}");
		return new JsonRepresentation(json.toString());
	}


}