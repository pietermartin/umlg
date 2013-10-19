package org.umlg.restlet.util;

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.javageneration.util.DataTypeEnum;
import org.umlg.javageneration.util.PropertyWrapper;

public class TumlRestletGenerationUtil {

    public final static OJPathName QueryExecuteServerResourceImpl = new OJPathName("org.umlg.runtime.restlet.QueryExecuteServerResourceImpl");
    public final static OJPathName EmptyRepresentation = new OJPathName("org.restlet.representation.EmptyRepresentation");
    public final static String _INDEX = "_index";
    public final static OJPathName UmlgNodeJsonHolder = new OJPathName("org.umlg.runtime.restlet.util.UmlgNodeJsonHolder");
    public final static OJPathName RestletRouterEnum = new OJPathName("org.umlg.RestletRouterEnum");
    public final static OJPathName UmlgBasePath = new OJPathName("org.umlg");
    public final static OJPathName UmlgURLDecoder = new OJPathName("org.umlg.runtime.restlet.util.UmlgURLDecoder");
    public final static OJPathName UmlgURLEncoder = new OJPathName("org.umlg.runtime.restlet.util.UmlgURLDecoder");
    public final static String classQueryQualifiedName = "umlglib::org::umlg::meta::ClassQuery";
    public final static String instanceQueryQualifiedName = "umlglib::org::umlg::query::InstanceQuery";

    public final static OJPathName TumlSchemaFactory = new OJPathName("org.umlg.runtime.adaptor.UmlgSchemaFactory");
    public final static OJPathName TumlRestletFilter = new OJPathName("org.umlg.runtime.restlet.TumlRestletFilter");
    public final static OJPathName TumlRestletToJsonUtil = new OJPathName("org.umlg.runtime.restlet.TumlRestletToJsonUtil");
    public final static OJPathName ClientResource = new OJPathName("org.restlet.resource.ClientResource");
    public final static OJPathName UmlgExceptionUtilFactory = new OJPathName("org.umlg.runtime.adaptor.UmlgExceptionUtilFactory");
    public final static OJPathName ErrorStatusService = new OJPathName("org.umlg.runtime.restlet.ErrorStatusService");
    public final static OJPathName FieldType = new OJPathName("org.umlg.runtime.restlet.FieldType");
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
    public static final OJPathName EnumerationLookupServerResouceImpl = new OJPathName("org.umlg.root.EnumerationLookup_ServerResourceImpl");
    public static final OJPathName EnumerationLookupServerResource = new OJPathName("org.umlg.root.EnumerationLookup_ServerResource");
    public static final OJPathName BaseOclExecutionServerResourceImpl = new OJPathName("org.umlg.runtime.restlet.BaseOclExecutionServerResourceImpl");
    public static final OJPathName UmlgRestletNode = new OJPathName("org.umlg.runtime.restlet.domain.UmlgRestletNode");
    public static final OJPathName RestletToJsonUtil = new OJPathName("org.umlg.runtime.restlet.util.RestletToJsonUtil");
    public static final OJPathName TumlMetaQueryServerResourceImpl = new OJPathName("org.umlg.runtime.restlet.TumlMetaQueryServerResourceImpl");
//    public static final OJPathName TumlTransactionResourceImpl = new OJPathName("org.umlg.runtime.restlet.TumlTransactionServerResourceImpl");
    public static final OJPathName Component = new OJPathName("org.restlet.Component");
    public static final OJPathName Application = new OJPathName("org.restlet.Application");
    public static final OJPathName Server = new OJPathName("org.restlet.Server");
    public static final OJPathName Context = new OJPathName("org.restlet.Context");
    public static final OJPathName Protocol = new OJPathName("org.restlet.data.Protocol");
    public static final OJPathName Restlet = new OJPathName("org.restlet.Restlet");
    public static final OJPathName Template = new OJPathName("org.restlet.routing.Template");
    public static final OJPathName Directory = new OJPathName("org.restlet.resource.Directory");

    public static final OJPathName TumlGuiServerResource = new OJPathName("org.umlg.runtime.restlet.TumlGuiServerResource");

    public static final OJPathName UmlgAdminAppFactory = new OJPathName("org.umlg.runtime.adaptor.UmlgAdminAppFactory");
    public static final OJPathName DefaultDataCreator = new OJPathName("org.umlg.runtime.adaptor.DefaultDataCreator");


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
            } else if (primitiveType.getName().equals("Real")) {
                return "FieldType.Real";
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
