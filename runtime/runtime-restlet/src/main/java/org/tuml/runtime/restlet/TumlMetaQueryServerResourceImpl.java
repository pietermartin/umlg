package org.tuml.runtime.restlet;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ServerResource;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.TumlNode;

import java.io.IOException;

/**
 * Date: 2012/12/26
 * Time: 6:01 PM
 */
public class TumlMetaQueryServerResourceImpl extends ServerResource implements TumlMetaQueryServerResource {
    @Override
    public Representation delete(Representation entity) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Representation get() {
        Integer id = Integer.parseInt((String) getRequestAttributes().get("contextId"));
        TumlNode parentResource = GraphDb.getDb().instantiateClassifier(Long.valueOf(id));
        Long metaNodeId = parentResource.getMetaNode().getId();
        String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/basemetatumls/" + metaNodeId + "/metaQuery";
        ClientResource service = new ClientResource(metaQueryUri);
        service.setNext(getContext().getServerDispatcher());
        try {
            String s = service.get().getText();
            return new JsonRepresentation(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Representation post(Representation entity) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Representation put(Representation entity) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
