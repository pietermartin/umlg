package org.umlg.runtime.restlet;

import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.umlg.framework.ModelLoader;
import org.umlg.runtime.util.Pair;
import org.umlg.runtime.util.UmlgUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UmlgGuiServerResource extends ServerResource {

    public UmlgGuiServerResource() {
        setNegotiated(false);
    }

    @Override
    protected Representation get() throws ResourceException {
        getLogger().fine(String.format("getHostRef = %s", getHostRef()));
        getLogger().fine(String.format("getLocationRef = %s", getLocationRef()));
        getLogger().fine(String.format("getReference = %s", getReference()));
        getLogger().fine(String.format("getReferrerRef = %s", getReferrerRef()));
        getLogger().fine(String.format("getOriginalRef = %s", getOriginalRef()));
        getLogger().fine(String.format("getRootRef = %s", getRootRef()));
        getLogger().fine(String.format("getResourceRef = %s", getRequest().getResourceRef()));
        Map<String, Object> requestAttr = getRequestAttributes();
        for (String requestKey : requestAttr.keySet()) {
            getLogger().fine(String.format("%s : %s", requestKey, requestAttr.get(requestKey)));
        }

        Map<String, Object> dataModel = new HashMap<String, Object>();
        String withHostRef = getOriginalRef().toString().replace(getHostRef().toString(), "");
        String uri;
        if (withHostRef.endsWith("/ui2/")) {
            uri = withHostRef.replace("/ui2/", "");
        } else {
            uri = withHostRef.replace("/ui2", "");
        }

        Pair<String,String> poweredBy = UmlgUtil.getBlueprintsImplementationWithUrl();
        dataModel.put("app", new App().setRootUrl(ModelLoader.INSTANCE.getModel().getName())
                .setUri(uri)
                .setUmlgLib(ModelLoader.INSTANCE.isUmlGLibIncluded())
                .setPoweredBy(poweredBy.getFirst())
                .setPoweredByLink(poweredBy.getSecond()));


        boolean servlet = true;
        boolean restlet = true;
        try {
            Class.forName("org.restlet.ext.servlet.ServerServlet");
        } catch (NoClassDefFoundError e) {
            servlet = false;
        } catch (ClassNotFoundException e) {
            servlet = false;
        }

        if (servlet)  {
            Representation umlgUiFtl = new ClientResource("war:///resources/org/umlg/ui/umlgui2.html").get();
            return new TemplateRepresentation(umlgUiFtl, dataModel, MediaType.TEXT_HTML);
        } else if (restlet) {
            File f = new File(".");
            System.out.println(f.getAbsolutePath());
            File umlgui2 = new File("./runtime/runtime-ui/src/main/webapp/resources/org/umlg/ui/umlgui2.html");
            FileRepresentation fileRepresentation = new FileRepresentation(umlgui2, MediaType.APPLICATION_XHTML);
            return new TemplateRepresentation(fileRepresentation, dataModel, MediaType.TEXT_HTML);
        } else {
            throw new RuntimeException("Not yet implemented");
        }

//        Representation umlgUiFtl = new ClientResource("clap:///resources/org/umlg/ui/umlgui2.html").get();

    }

    public class App {
        private String rootUrl;
        private String uri;
        private boolean umlgLib;
        private String poweredByLink;
        private String poweredBy;

        public String getPoweredBy() {
            return poweredBy;
        }

        public App setPoweredBy(String poweredBy) {
            this.poweredBy = poweredBy;
            return this;
        }

        public String getPoweredByLink() {
            return poweredByLink;
        }

        public App setPoweredByLink(String poweredByLink) {
            this.poweredByLink = poweredByLink;
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
