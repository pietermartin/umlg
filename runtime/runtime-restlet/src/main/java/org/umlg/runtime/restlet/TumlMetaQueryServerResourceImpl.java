package org.umlg.runtime.restlet;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ServerResource;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.domain.UmlgNode;
import org.umlg.runtime.restlet.util.UmlgURLDecoder;

import java.io.IOException;

/**
 * Date: 2012/12/26
 * Time: 6:01 PM
 */
public class TumlMetaQueryServerResourceImpl extends ServerResource {

    @Override
    public Representation get() {
        String id = UmlgURLDecoder.decode((String)getRequestAttributes().get("contextId"));
        UmlgNode parentResource = GraphDb.getDb().instantiateClassifier(id);
        Object metaNodeId = parentResource.getMetaNode().getId();
        String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/baseclasstumls/" + UmlgURLDecoder.encode(metaNodeId.toString()) + "/classQuery";
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
        String id = UmlgURLDecoder.decode((String)getRequestAttributes().get("contextId"));
        UmlgNode parentResource = GraphDb.getDb().instantiateClassifier(id);
        Object metaNodeId = parentResource.getMetaNode().getId();
        String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/baseclasstumls/" + UmlgURLDecoder.encode(metaNodeId.toString()) + "/classQuery";
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
        String id = UmlgURLDecoder.decode((String)getRequestAttributes().get("contextId"));
        UmlgNode parentResource = GraphDb.getDb().instantiateClassifier(id);
        Object metaNodeId = parentResource.getMetaNode().getId();
        String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/baseclasstumls/" + UmlgURLDecoder.encode(metaNodeId.toString()) + "/classQuery";
        ClientResource service = new ClientResource(metaQueryUri);
        service.setNext(getContext().getServerDispatcher());
        return service.post(entity);
    }

    @Override
    public Representation put(Representation entity) {
        String id = UmlgURLDecoder.decode((String)getRequestAttributes().get("contextId"));
        UmlgNode parentResource = GraphDb.getDb().instantiateClassifier(id);
        Object metaNodeId = parentResource.getMetaNode().getId();
        String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/baseclasstumls/" + UmlgURLDecoder.encode(metaNodeId.toString()) + "/classQuery";
        ClientResource service = new ClientResource(metaQueryUri);
        service.setNext(getContext().getServerDispatcher());
        return service.put(entity);
    }
}
