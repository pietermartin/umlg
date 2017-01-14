package org.umlg.runtime.restlet;

import org.restlet.resource.ServerResource;

/**
 * Date: 2012/12/26
 * Time: 6:01 PM
 */
public class UmlgMetaQueryServerResourceImpl extends ServerResource {

//    @Override
//    public Representation get() {
//        try {
//            String qualifiedName = UmlgURLDecoder.decode(getQueryValue("qualifiedName"));
//            UmlgMetaNode umlgMetaNode = UMLG.get().getUmlgApplicationNode().getMetaClassForQualifiedName(qualifiedName);
//            Object metaNodeId = umlgMetaNode.getId();
//            String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/baseclassumlgs/" + UmlgURLDecoder.encode(metaNodeId.toString()) + "/classQuery";
//            ClientResource service = new ClientResource(metaQueryUri);
//            service.setNext(getContext().getServerDispatcher());
//            try {
//                String s = service.get().getText();
//                return new JsonRepresentation(s);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        } finally {
//            UMLG.get().rollback();
//        }
//    }
//
//    @Override
//    public Representation options() {
//        try {
//            String qualifiedName = UmlgURLDecoder.decode(getQueryValue("qualifiedName"));
//            UmlgMetaNode umlgMetaNode = UMLG.get().getUmlgApplicationNode().getMetaClassForQualifiedName(qualifiedName);
//            Object metaNodeId = umlgMetaNode.getId();
//            String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/baseclassumlgs/" + UmlgURLDecoder.encode(metaNodeId.toString()) + "/classQuery";
//            ClientResource service = new ClientResource(metaQueryUri);
//            service.setNext(getContext().getServerDispatcher());
//            try {
//                String s = service.options().getText();
//                return new JsonRepresentation(s);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        } finally {
//            UMLG.get().rollback();
//        }
//    }
//
//    @Override
//    public Representation post(Representation entity) {
//        String qualifiedName = UmlgURLDecoder.decode(getQueryValue("qualifiedName"));
//        UmlgMetaNode umlgMetaNode = UMLG.get().getUmlgApplicationNode().getMetaClassForQualifiedName(qualifiedName);
//        Object metaNodeId = umlgMetaNode.getId();
//        String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/baseclassumlgs/" + UmlgURLDecoder.encode(metaNodeId.toString()) + "/classQuery";
//        ClientResource service = new ClientResource(metaQueryUri);
//        service.setNext(getContext().getServerDispatcher());
//        return service.post(entity);
//    }
//
//    @Override
//    public Representation put(Representation entity) {
//        String qualifiedName = UmlgURLDecoder.decode(getQueryValue("qualifiedName"));
//        UmlgMetaNode umlgMetaNode = UMLG.get().getUmlgApplicationNode().getMetaClassForQualifiedName(qualifiedName);
//        Object metaNodeId = umlgMetaNode.getId();
//        String metaQueryUri = "riap://application/" + getRootRef().getLastSegment() + "/baseclassumlgs/" + UmlgURLDecoder.encode(metaNodeId.toString()) + "/classQuery";
//        ClientResource service = new ClientResource(metaQueryUri);
//        service.setNext(getContext().getServerDispatcher());
//        return service.put(entity);
//    }
}
