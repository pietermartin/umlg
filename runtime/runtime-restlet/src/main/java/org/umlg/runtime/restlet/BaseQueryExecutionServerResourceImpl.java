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
//            UMLG.get().executeQueryToString(UmlgQueryEnum.OCL, context.getId(), query);
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
            String result = UmlgOclExecutor.executeOclQueryAsJson(contextClassifierQualifiedName, query);
            return new JsonRepresentation(result);
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