package org.umlg.runtime.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.umlg.framework.ModelLoader;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.runtime.adaptor.DefaultDataCreator;
import org.umlg.runtime.adaptor.UmlgAdminAppFactory;
import org.umlg.runtime.adaptor.UmlgGraphManager;
import org.umlg.runtime.util.UmlgProperties;

import java.io.File;
import java.net.URL;

/**
 * Date: 2014/01/15
 * Time: 4:14 PM
 */
public abstract class UmlgRestletApplication extends Application {

    /**
     * default constructor for RestAndJsonApplication
     */
    public UmlgRestletApplication()  {
        setStatusService(new ErrorStatusService());
        init();
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        attachAll(router);
        router.attach("/ui2", TumlGuiServerResource.class, Template.MODE_STARTS_WITH);
        File current = new File(".");
        Directory slickgrid;
        if (UmlgProperties.INSTANCE.isLoadUiResourcesFromFile()) {
            slickgrid = new Directory(getContext(), "file:///" + current.getAbsolutePath() + "/runtime/runtime-ui/src/main/javascript/javascript");
        } else {
            slickgrid = new Directory(getContext(), "clap://javascript/javascript/");
        }
        slickgrid.setListingAllowed(true);
        router.attach("/javascript/", slickgrid);
        Directory css;
        if (UmlgProperties.INSTANCE.isLoadUiResourcesFromFile()) {
            css = new Directory(getContext(), "file:///" + current.getAbsolutePath() + "/runtime/runtime-ui/src/main/javascript/css");
        } else {
            css = new Directory(getContext(), "clap://javascript/css");
        }
        css.setListingAllowed(true);
        router.attach("/css/", css);
        TumlRestletFilter tumlRestletFilter = new TumlRestletFilter(getContext());
        tumlRestletFilter.setNext(router);
        return tumlRestletFilter;
    }

    /**
     * Override this method to attach the application's resources to the router
     * @param router The router that the application resources will be attached to.
     */
    protected abstract void attachAll(Router router);

    /**
     * Return the file name of the uml model. i.e. somethingModel.uml
     * It must be on the classpath and will be loaded via the ClassLoader
     * @return A string representing the model's file name.
     */
    protected abstract String getModelFileName();

    protected void init() {
        URL modelFileURL = Thread.currentThread().getContextClassLoader().getResource(getModelFileName());
        if ( modelFileURL == null ) {
            throw new IllegalStateException("Model file restAndJson.uml not found. The model's file name must be on the classpath.");
        }
        try {
            final File modelFile = new File(modelFileURL.toURI());
            //Load the mode async
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ModelLoader.INSTANCE.loadModel(modelFile);
                    UmlgOcl2Parser tumlOcl2Parser = UmlgOcl2Parser.INSTANCE;
                }
            }).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        UmlgGraphManager.INSTANCE.startupGraph();
        if ( UmlgProperties.INSTANCE.isStartAdminApplication() ) {
            UmlgAdminAppFactory.getUmlgAdminApp().startAdminApplication();
        }
        if ( UmlgProperties.INSTANCE.isCreateDefaultData() ) {
            try {
                DefaultDataCreator defaultDataCreator = (DefaultDataCreator)Class.forName(UmlgProperties.INSTANCE.getDefaultDataLoaderClass()).newInstance();
                defaultDataCreator.createData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
