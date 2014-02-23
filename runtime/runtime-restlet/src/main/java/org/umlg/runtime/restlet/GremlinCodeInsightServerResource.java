package org.umlg.runtime.restlet;

import org.eclipse.ocl.helper.Choice;
import org.eclipse.uml2.uml.Classifier;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.umlg.framework.ModelLoader;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.runtime.adaptor.GraphDb;

import java.util.List;

/**
 * Date: 2014/02/09
 * Time: 7:23 PM
 */
public class GremlinCodeInsightServerResource extends ServerResource {

    /**
     * default constructor for OclCodeInsightServerResource
     */
    public GremlinCodeInsightServerResource() {
        setNegotiated(false);
    }

    @Override
    public Representation get() throws ResourceException {
        try {
            String query = getQuery().getFirstValue("query");
            String contextClassifierQualifiedName = getQuery().getFirstValue("contextClassifierQualifiedName");
            if (contextClassifierQualifiedName != null) {
                Classifier contextClassifier = (Classifier) ModelLoader.INSTANCE.findNamedElement(contextClassifierQualifiedName);
                UmlgOcl2Parser.INSTANCE.getHelper().setContext(contextClassifier);
            }
            List<Choice> insights = UmlgOcl2Parser.INSTANCE.getHelper().getSyntaxHelp(null, query);
            return new JsonRepresentation(convertChoicesToJson(insights));
        } finally {
            GraphDb.getDb().rollback();
        }
    }

    private String convertChoicesToJson(List<Choice> insights) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int count = 1;
        for (Choice choice : insights) {
            sb.append("{\"text\":\"");
            sb.append(choice.getName());
            sb.append("\", \"displayText\":\"");
            sb.append(choice.getKind().name());
            sb.append(", ");
            sb.append(choice.getName());
            sb.append(", ");
            sb.append(choice.getDescription());
            sb.append("\"}");
            if (count++ < insights.size()) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
