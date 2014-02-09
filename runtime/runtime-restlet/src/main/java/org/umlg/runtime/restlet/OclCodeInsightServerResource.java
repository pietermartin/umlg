package org.umlg.runtime.restlet;

import org.eclipse.ocl.helper.Choice;
import org.eclipse.uml2.uml.Classifier;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.umlg.framework.ModelLoader;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.domain.UmlgNode;
import org.umlg.runtime.restlet.util.UmlgURLDecoder;

import java.util.List;

/**
 * Date: 2014/02/09
 * Time: 7:23 PM
 */
public class OclCodeInsightServerResource extends ServerResource {

    /**
     * default constructor for OclCodeInsightServerResource
     */
    public OclCodeInsightServerResource() {
        setNegotiated(false);
    }

    @Override
    public Representation get() throws ResourceException {
        try {
            String query = getQuery().getFirstValue("query");
            String contextId = (String) getRequestAttributes().get("contextId");
            UmlgNode context = GraphDb.getDb().instantiateClassifier(contextId);
            Classifier contextClassifier = (Classifier) ModelLoader.INSTANCE.findNamedElement(context.getQualifiedName());
            UmlgOcl2Parser.INSTANCE.getHelper().setContext(contextClassifier);
            List<Choice> insights = UmlgOcl2Parser.INSTANCE.getHelper().getSyntaxHelp(null, query);
            return null;
        } finally {
            GraphDb.getDb().rollback();
        }
    }

//    private String convertChoicesToJson() {
//
//    }

}
