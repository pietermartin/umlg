package org.tuml.restlet.util;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJPathName;
import org.tuml.javageneration.util.PropertyWrapper;

public class TumlRestletGenerationUtil {

	public final static OJPathName FieldType = new OJPathName("org.tuml.ui.FieldType");
	public final static OJPathName Get = new OJPathName("org.restlet.resource.Get");
	public final static OJPathName Put = new OJPathName("org.restlet.resource.Put");
	public final static OJPathName Post = new OJPathName("org.restlet.resource.Post");
	public final static OJPathName Delete = new OJPathName("org.restlet.resource.Delete");
	public final static OJPathName ServerResource = new OJPathName("org.restlet.resource.ServerResource");
	public final static OJPathName Representation = new OJPathName("org.restlet.representation.Representation");
	public final static OJPathName ResourceException = new OJPathName("org.restlet.resource.ResourceException");
	public final static OJPathName JsonRepresentation = new OJPathName("org.restlet.ext.json.JsonRepresentation");
	public static final OJPathName Router = new OJPathName("org.restlet.routing.Router");
	public static final OJPathName Parameter = new OJPathName("org.restlet.data.Parameter");
	
	public static String getFieldTypeForProperty(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		if (propertyWrapper.getType() instanceof PrimitiveType) {
			PrimitiveType primitiveType = (PrimitiveType) propertyWrapper.getType();
			if (primitiveType.getName().equals("String")) {
				return "FieldType.String";
			} else if (primitiveType.getName().equals("Integer")) {
				return "FieldType.Integer";
			} else if (primitiveType.getName().equals("Boolean")) {
				return "FieldType.Boolean";
			} else if (primitiveType.getName().equals("UnlimitedNatural")) {
				return "FieldType.Long";
			} else  {
				throw new IllegalStateException("unknown primitive " + primitiveType.getName());
			}
		}
		return "FieldType.Date";
	}
}
