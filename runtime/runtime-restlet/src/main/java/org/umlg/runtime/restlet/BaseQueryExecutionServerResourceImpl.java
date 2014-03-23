package org.umlg.runtime.restlet;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.umlg.ocl.UmlgOclExecutor;
import org.umlg.runtime.adaptor.GroovyExecutor;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgQueryEnum;
import org.umlg.runtime.domain.PersistentObject;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Collection;
import java.util.Map;

public abstract class BaseQueryExecutionServerResourceImpl extends ServerResource {

    public BaseQueryExecutionServerResourceImpl() {
    }

    protected Representation execute(String query, Object contextId, String type) {
        if (type.equalsIgnoreCase("ocl")) {
            UmlgNode context = UMLG.get().instantiateClassifier(contextId);
            UMLG.get().executeQueryToString(UmlgQueryEnum.OCL, context.getId(), query);
            return new JsonRepresentation(UmlgOclExecutor.executeOclQueryAsJson(context, query));
        } else if (type.equalsIgnoreCase("groovy")) {
            String result = GroovyExecutor.INSTANCE.executeGroovyAsString(contextId, query);
            return new StringRepresentation(result);
        } else {
            throw new RuntimeException("Unknown query type " + type);
        }
    }

    //static
    protected Representation executeStatic(String query, String contextClassifierQualifiedName, String type) {
        if (type.equalsIgnoreCase("ocl")) {

            Object result = UmlgOclExecutor.executeOclQuery(contextClassifierQualifiedName, query);
            if (result instanceof Map) {
//            return UmlgOclExecutor.tupleMapToJson((Map<String, Object>) result);
                //TODO
                return new JsonRepresentation(result.toString());
            } else if (result instanceof Collection) {
                Collection<PersistentObject> poCollection = (Collection<PersistentObject>) result;

                StringBuilder json = new StringBuilder();
                json.append("[");
                json.append("{\"data\": [");
                int count = 0;
                PersistentObject poForMetaData = null;
                for (PersistentObject po : poCollection) {
                    count++;
                    String objectAsJson = po.toJsonWithoutCompositeParent();
                    String objectAsJsonWithRow = "{\"row\": " + count + ", " + objectAsJson.substring(1);
                    json.append(objectAsJsonWithRow);
                    if (count != poCollection.size()) {
                        json.append(",");
                    } else {
                        poForMetaData = po;
                    }
                }
                json.append("],");
                json.append(" \"meta\" : {");
                json.append("\"qualifiedName\": \"restAndJson::org::umlg::test::Hand::finger\"");
                json.append(", \"to\": ");
                if (poForMetaData != null) {
                    json.append(poForMetaData.getMetaDataAsJson());
                } else {
                    json.append("null");
                }
                json.append("}");
                json.append("}]");
                return new JsonRepresentation(json.toString());
            } else if (result instanceof PersistentObject) {
                PersistentObject po = (PersistentObject) result;
                return getRepresentation(po);
            } else {
                return new JsonRepresentation(result.toString());
            }
        } else if (type.equalsIgnoreCase("groovy")) {
            String result = GroovyExecutor.INSTANCE.executeGroovyAsString(null, query);
            return new StringRepresentation(result);
        } else {
            throw new RuntimeException("Unknown query type " + type);
        }
    }

    protected Representation getRepresentation(PersistentObject po) throws ResourceException {
        StringBuilder json = new StringBuilder();
        json.append("[{\"data\": [");
        String poAsJson = po.toJson();
        json.append(poAsJson.substring(0, poAsJson.length() - 1));
        json.append(", \"row\": 1}");
        json.append("], \"meta\" : {");
        json.append("\"qualifiedName\": \"restAndJson::org::umlg::test::Human\"");
        json.append(", \"to\": ");
        json.append(po.getMetaDataAsJson());
        json.append("}}]");
        return new JsonRepresentation(json.toString());
    }


}