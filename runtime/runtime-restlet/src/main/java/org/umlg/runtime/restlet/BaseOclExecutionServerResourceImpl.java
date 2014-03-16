package org.umlg.runtime.restlet;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.umlg.ocl.UmlgOclExecutor;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.GremlinExecutor;
import org.umlg.runtime.domain.PersistentObject;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Collection;
import java.util.Map;

public abstract class BaseOclExecutionServerResourceImpl extends ServerResource {

    public BaseOclExecutionServerResourceImpl() {
    }

    protected Representation execute(String query, Object contextId, String type) {
        if (type.equalsIgnoreCase("ocl")) {
            UmlgNode context = UMLG.getDb().instantiateClassifier(contextId);
            Object result = UmlgOclExecutor.executeOclQuery(context, query);
            if (result instanceof Map) {
//            return UmlgOclExecutor.tupleMapToJson((Map<String, Object>) result);
                //TODO
                return new JsonRepresentation(result.toString());
            } else if (result instanceof Collection) {

                //TODO need to sort out polymorphic queries
                Collection<Object> poCollection = (Collection<Object>) result;

                StringBuilder json = new StringBuilder();
                json.append("[");
                json.append("{\"data\": [");
                int count = 0;
                PersistentObject poForMetaData = null;
                for (Object o : poCollection) {
                    count++;
                    if (o instanceof PersistentObject) {
                        PersistentObject po = (PersistentObject) o;
                        String objectAsJson = po.toJsonWithoutCompositeParent();
                        String objectAsJsonWithRow = "{\"row\": " + count + ", " + objectAsJson.substring(1);
                        json.append(objectAsJsonWithRow);
                        if (count != poCollection.size()) {
                            json.append(",");
                        } else {
                            poForMetaData = po;
                        }
                    } else {
                        String objectAsJson = o.toString();
                        String objectAsJsonWithRow = "{\"row\": " + count + ", \"value\": \"" + objectAsJson + "\"}";
                        json.append(objectAsJsonWithRow);
                        if (count != poCollection.size()) {
                            json.append(",");
                        }

                    }
                }
                json.append("],");
                json.append(" \"meta\" : {");
                //TODO create some meta data strategy for tuples and lists of primitives or datatypes
                //TODO some hardcoding to sort out
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
                return new JsonRepresentation("{\"result\": " + "\"" + (result == null ? "No result" : result.toString()) + "\"}");
            }
        } else if (type.equalsIgnoreCase("gremlin")) {
            String result = GremlinExecutor.executeGremlinViaGroovy(contextId, query);
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
        } else if (type.equalsIgnoreCase("gremlin")) {
            String result = GremlinExecutor.executeGremlinViaGroovy(null, query);
            return new StringRepresentation(result);
        } else {
            throw new RuntimeException("Unknown query type " + type);
        }
    }

    protected Representation getRepresentation(PersistentObject po) throws ResourceException {
        StringBuilder json = new StringBuilder();
        json.append("[{\"data\": [");
        String poAsJson = po.toJson();
        json.append(poAsJson.substring(0, poAsJson.length() - 1) + ", \"row\": 1}");
        json.append("], \"meta\" : {");
        json.append("\"qualifiedName\": \"restAndJson::org::umlg::test::Human\"");
        json.append(", \"to\": ");
        json.append(po.getMetaDataAsJson());
        json.append("}}]");
        return new JsonRepresentation(json.toString());
    }


}