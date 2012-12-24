package org.tuml.restlet.util;

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJPathName;
import org.tuml.javageneration.util.DataTypeEnum;
import org.tuml.javageneration.util.PropertyWrapper;

public class TumlRestletGenerationUtil {

    public final static String queryQualifiedName = "tumllib::org::tuml::query::Query";
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
    public static final OJPathName EnumerationLookupServerResouceImpl = new OJPathName("org.tuml.root.EnumerationLookup_ServerResourceImpl");
    public static final OJPathName EnumerationLookupServerResource = new OJPathName("org.tuml.root.EnumerationLookup_ServerResource");
    public static final OJPathName QueryExecuteServerResource = new OJPathName("org.tuml.root.QueryExecuteServerResource");
    public static final OJPathName QueryExecuteServerResourceImpl = new OJPathName("org.tuml.root.QueryExecuteServerResourceImpl");
    public static final OJPathName BaseOclExecutionServerResourceImpl = new OJPathName("org.tuml.runtime.restlet.BaseOclExecutionServerResourceImpl");
    public static final OJPathName TumlRestletNode = new OJPathName("org.tuml.runtime.restlet.domain.TumlRestletNode");
    public static final OJPathName RestletToJsonUtil = new OJPathName("org.tuml.runtime.restlet.util.RestletToJsonUtil");

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
            } else {
                throw new IllegalStateException("unknown primitive " + primitiveType.getName());
            }
        } else if (!(propertyWrapper.getType() instanceof Enumeration) && (propertyWrapper.getType() instanceof DataType)) {
            DataTypeEnum dataTypeEnum = DataTypeEnum.fromDataType((DataType) propertyWrapper.getType());
            switch (dataTypeEnum) {
                case DateTime:
                    return "FieldType.String";
                case Date:
                    return "FieldType.String";
                case Time:
                    return "FieldType.String";
                case InternationalPhoneNumber:
                    return "FieldType.String";
                case LocalPhoneNumber:
                    return "FieldType.String";
                case Email:
                    return "FieldType.String";
                case Video:
                    return "FieldType.ByteArray";
                case Audio:
                    return "FieldType.ByteArray";
                case Image:
                    return "FieldType.ByteArray";
                default:
                    throw new RuntimeException("Unknown data type " + dataTypeEnum.name());
            }
        } else if (propertyWrapper.isComponent()) {
            return "FieldType.Object";
        } else if (propertyWrapper.isOne()) {
            // For the id
            return "FieldType.Integer";
        } else {
            return "FieldType.Date";
        }
    }
}
