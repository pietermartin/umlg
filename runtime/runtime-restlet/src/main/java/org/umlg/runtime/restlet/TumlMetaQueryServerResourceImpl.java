package org.umlg.runtime.restlet;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ServerResource;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.domain.UmlgNode;

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
        String id = (String) getRequestAttributes().get("contextId");
        UmlgNode parentResource = GraphDb.getDb().instantiateClassifier(id);
        Object metaNodeId = parentResource.getMetaNode().getId();
        String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/baseclasstumls/" + metaNodeId + "/classQuery";
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
    public Representation options() {
        String id = (String) getRequestAttributes().get("contextId");
        UmlgNode parentResource = GraphDb.getDb().instantiateClassifier(id);
        Object metaNodeId = parentResource.getMetaNode().getId();
        String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/baseclasstumls/" + metaNodeId + "/classQuery";
        ClientResource service = new ClientResource(metaQueryUri);
        service.setNext(getContext().getServerDispatcher());
        try {
            String s = service.options().getText();
            return new JsonRepresentation(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Representation post(Representation entity) {
        String id = (String) getRequestAttributes().get("contextId");
        UmlgNode parentResource = GraphDb.getDb().instantiateClassifier(id);
        Object metaNodeId = parentResource.getMetaNode().getId();
        String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/baseclasstumls/" + metaNodeId + "/classQuery";
        ClientResource service = new ClientResource(metaQueryUri);
        service.setNext(getContext().getServerDispatcher());
        return service.post(entity);
    }

    @Override
    public Representation put(Representation entity) {
        String id = (String) getRequestAttributes().get("contextId");
        UmlgNode parentResource = GraphDb.getDb().instantiateClassifier(id);
        Object metaNodeId = parentResource.getMetaNode().getId();
        String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/baseclasstumls/" + metaNodeId + "/classQuery";
        ClientResource service = new ClientResource(metaQueryUri);
        service.setNext(getContext().getServerDispatcher());
        return service.put(entity);
    }
}
