package org.umlg.runtime.restlet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Directory;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.umlg.framework.ModelLoader;
import org.umlg.runtime.util.UmlgUtil;

public class TumlGuiServerResource extends ServerResource {

    public TumlGuiServerResource() {
        setNegotiated(false);
    }

    @Override
    protected Representation get() throws ResourceException {
        System.out.println("getHostRef = " + getHostRef());
        System.out.println("getLocationRef = " + getLocationRef());
        System.out.println("getReference = " + getReference());
        System.out.println("getReferrerRef = " + getReferrerRef());
        System.out.println("getOriginalRef = " + getOriginalRef());
        System.out.println("getRootRef = " + getRootRef());
        System.out.println("getResourceRef = " + getRequest().getResourceRef());
        Map<String, Object> requestAttr = getRequestAttributes();
        for (String requestKey : requestAttr.keySet()) {
            System.out.println(requestKey + " : " + requestAttr.get(requestKey));
        }

        Map<String, Object> dataModel = new HashMap<String, Object>();
        String withHostRef = getOriginalRef().toString().replace(getHostRef().toString(), "");
        String uri;
        if (withHostRef.endsWith("/ui2/")) {
            uri = withHostRef.replace("/ui2/", "");
        } else {
            uri = withHostRef.replace("/ui2", "");
        }

        String poweredBy = UmlgUtil.getBlueprintsImplementation();
        dataModel.put("app", new App().setRootUrl(ModelLoader.INSTANCE.getModel().getName())
                .setUri(uri)
                .setUmlgLib(ModelLoader.INSTANCE.isUmlGLibIncluded())
                .setPoweredBy(poweredBy));
//        Representation umlgUiFtl = new ClientResource("clap:///org/umlg/ui/umlgui2.html").get();

        File umlgui2 = new File("./runtime/runtime-ui/src/main/resources/org/umlg/ui/umlgui2.html");
        FileRepresentation fileRepresentation = new FileRepresentation(umlgui2, MediaType.APPLICATION_XHTML);

//        //Directory css = new Directory(getContext(), "clap://javascript/css");
//        Directory css = new Directory(getContext(), "file:///home/pieter/Downloads/umlg/runtime/runtime-ui/src/main/resources/org/umlg/ui/");
//        css.setListingAllowed(true);
//        router.attach("/css/", css);

        return new TemplateRepresentation(fileRepresentation, dataModel, MediaType.TEXT_HTML);
    }

    public class App {
        private String rootUrl;
        private String uri;
        private boolean umlgLib;
        private String poweredBy;

        public String getPoweredBy() {
            return poweredBy;
        }

        public App setPoweredBy(String poweredBy) {
            this.poweredBy = poweredBy;
            return this;
        }

        public boolean isUmlgLib() {
            return umlgLib;
        }

        public App setUmlgLib(boolean umlgLib) {
            this.umlgLib = umlgLib;
            return this;
        }

        public String getUri() {
            return uri;
        }

        public App setUri(String uri) {
            this.uri = uri;
            return this;
        }

        public String getRootUrl() {
            return rootUrl;
        }

        public App setRootUrl(String rootUrl) {
            this.rootUrl = rootUrl;
            return this;
        }

    }

}
