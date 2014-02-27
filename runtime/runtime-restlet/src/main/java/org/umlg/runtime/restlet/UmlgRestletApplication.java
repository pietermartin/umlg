package org.umlg.runtime.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.FileRepresentation;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.runtime.adaptor.DefaultDataCreator;
import org.umlg.runtime.adaptor.UmlgAdminAppFactory;
import org.umlg.runtime.adaptor.UmlgGraphManager;
import org.umlg.runtime.util.UmlgProperties;

import java.io.File;

/**
 * Date: 2014/01/15
 * Time: 4:14 PM
 */
public abstract class UmlgRestletApplication extends Application {

    /**
     * default constructor for RestAndJsonApplication
     */
    public UmlgRestletApplication() {
        setStatusService(new ErrorStatusService());
        init();
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        attachAll(router);
        router.attach("/ui2", UmlgGuiServerResource.class, Template.MODE_STARTS_WITH);
        Directory slickgrid = null;
        Directory css = null;

        boolean servlet = true;
        boolean restlet = true;
        try {
            Class.forName("org.restlet.ext.servlet.ServerServlet");
        } catch (NoClassDefFoundError e) {
            servlet = false;
        } catch (ClassNotFoundException e) {
            servlet = false;
        }
        if (servlet) {
            slickgrid = new Directory(getContext(), "war:///javascript/");
            css = new Directory(getContext(), "war:///css");
        } else if (restlet) {
            File current = new File(".");
            if (UmlgProperties.INSTANCE.isLoadUiResourcesFromFile()) {
                slickgrid = new Directory(getContext(), "file:///" + current.getAbsolutePath() + "/runtime/runtime-ui/src/main/webapp/javascript");
            } else {
                slickgrid = new Directory(getContext(), "clap:///javascript/");
            }
            //This is only relevant for simple restlet case, not via jetty
            if (UmlgProperties.INSTANCE.isLoadUiResourcesFromFile()) {
                css = new Directory(getContext(), "file:///" + current.getAbsolutePath() + "/runtime/runtime-ui/src/main/webapp/css");
            } else {
                css = new Directory(getContext(), "clap:///css");
            }
        } else {
            //development environment
        }

        slickgrid.setListingAllowed(true);
        router.attach("/javascript/", slickgrid);
        css.setListingAllowed(true);
        router.attach("/css/", css);

        UmlgRestletFilter umlgRestletFilter = new UmlgRestletFilter(getContext());
        umlgRestletFilter.setNext(router);
        return umlgRestletFilter;
    }

    /**
     * Override this method to attach the application's resources to the router
     *
     * @param router The router that the application resources will be attached to.
     */
    protected abstract void attachAll(Router router);

    /**
     * Return the file name of the uml model. i.e. somethingModel.uml
     * It must be on the classpath and will be loaded via the ClassLoader
     *
     * @return A string representing the model's file name.
     */
    protected abstract String getModelFileName();

    protected void init() {
        UmlgOcl2Parser.INSTANCE.init(getModelFileName());
        UmlgGraphManager.INSTANCE.startupGraph();
        if (UmlgProperties.INSTANCE.isStartAdminApplication()) {
            UmlgAdminAppFactory.getUmlgAdminApp().startAdminApplication();
        }
        if (UmlgProperties.INSTANCE.isCreateDefaultData()) {
            try {
                DefaultDataCreator defaultDataCreator = (DefaultDataCreator) Class.forName(UmlgProperties.INSTANCE.getDefaultDataLoaderClass()).newInstance();
                defaultDataCreator.createData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
