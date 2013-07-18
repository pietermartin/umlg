package org.umlg.restlet.visitor.model;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Model;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.restlet.util.TumlRestletGenerationUtil;

/**
 * Date: 2012/12/29
 * Time: 7:17 PM
 */
public class RestletComponentAndApplicationGenerator extends BaseVisitor implements Visitor<Model> {

    public RestletComponentAndApplicationGenerator(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Model model) {
        createComponent(model);
        createApplication(model);
    }

    @Override
    public void visitAfter(Model element) {
    }

    private void createComponent(Model model) {
        OJAnnotatedClass component = new OJAnnotatedClass(getComponentName(model));
        component.setSuperclass(TumlRestletGenerationUtil.Component);
        OJPackage org = new OJPackage("org");
        OJPackage tuml = new OJPackage("umlg", org);
        OJPackage restlet = new OJPackage(model.getName().toLowerCase(), tuml);
        component.setMyPackage(restlet);
        addToSource(component);

        addComponentMainMethod(model, component);
        addComponentDefaultConstructor(model, component);
        addStop(component);
    }

    private void addStop(OJAnnotatedClass component) {
        OJAnnotatedOperation stop = new OJAnnotatedOperation("stop");
        TinkerGenerationUtil.addOverrideAnnotation(stop);
        stop.getBody().addToStatements(TinkerGenerationUtil.TumlGraphManager.getLast() + ".INSTANCE.shutdown()");
        stop.getBody().addToStatements(TinkerGenerationUtil.TumlGraphManager.getLast() + ".INSTANCE.deleteGraph()");
        stop.getBody().addToStatements(TinkerGenerationUtil.graphDbPathName.getLast() + ".remove()");

        stop.getBody().addToStatements("super.stop()");
        component.addToImports(TinkerGenerationUtil.graphDbPathName);
        component.addToImports(TinkerGenerationUtil.TumlGraphManager);
        stop.addToThrows(new OJPathName("java.lang.Exception"));
        component.addToOperations(stop);
    }

    private void addComponentMainMethod(Model model, OJAnnotatedClass component) {
        OJAnnotatedOperation main = new OJAnnotatedOperation("main");
        main.setStatic(true);
        main.addParam("args", new OJPathName("String[]"));
        component.addToOperations(main);

        main.getBody().addToStatements(getComponentName(model) + " app = new " + getComponentName(model) + "()");
        main.getBody().addToStatements("app.start()");
        main.addToThrows("java.lang.Exception");
    }

    private void addComponentDefaultConstructor(Model model, OJAnnotatedClass component) {
        OJConstructor constructor = component.getDefaultConstructor();

        constructor.getBody().addToStatements("URL modelFileURL = Thread.currentThread().getContextClassLoader().getResource(\"" + this.workspace.getModelFile().getName() + "\")");
        OJIfStatement ifFileExist = new OJIfStatement("modelFileURL == null", "throw new IllegalStateException(\"Model file " + this.workspace.getModelFile().getName() + " not found. The model's file name must be on the classpath.\")");
        constructor.getBody().addToStatements(ifFileExist);

        OJTryStatement ojTryStatement = new OJTryStatement();

        ojTryStatement.getTryPart().addToStatements("final File modelFile = new File(modelFileURL.toURI())");
        ojTryStatement.getTryPart().addToStatements("//Load the mode async\nnew Thread(new Runnable() {\n    @Override\n    public void run() {\n        ModelLoader.INSTANCE.loadModel(modelFile);\n        TumlOcl2Parser tumlOcl2Parser = TumlOcl2Parser.INSTANCE;\n    }\n}).start()");
        component.addToImports(TinkerGenerationUtil.ModelLoader);
        component.addToImports(TinkerGenerationUtil.TumlOcl2Parser);
        component.addToImports("java.io.File");
        component.addToImports("java.net.URL");
        ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");
        constructor.getBody().addToStatements(ojTryStatement);

        constructor.getBody().addToStatements(TinkerGenerationUtil.TumlGraphManager.getLast() + ".INSTANCE.startupGraph()");
        component.addToImports(TinkerGenerationUtil.TumlGraphManager);

        OJIfStatement ifStartAdmin = new OJIfStatement(TinkerGenerationUtil.UmlgProperties.getLast() + ".INSTANCE.isStartAdminApplication()", TumlRestletGenerationUtil.Neo4jAdminApp.getLast() + ".startAdminApplication()");
        constructor.getBody().addToStatements(ifStartAdmin);
        component.addToImports(TumlRestletGenerationUtil.Neo4jAdminApp);
        component.addToImports(TinkerGenerationUtil.UmlgProperties);

        OJTryStatement tryLoadClass = new OJTryStatement();
        OJIfStatement ifCreateDefaultData = new OJIfStatement(TinkerGenerationUtil.UmlgProperties.getLast() + ".INSTANCE.isCreateDefaultData()");
        ifCreateDefaultData.addToThenPart(tryLoadClass);
        tryLoadClass.getTryPart().addToStatements("DefaultDataCreator defaultDataCreator = (DefaultDataCreator)Class.forName(" + TinkerGenerationUtil.UmlgProperties.getLast() + ".INSTANCE.getDefaultDataLoaderClass()).newInstance()");
        tryLoadClass.getTryPart().addToStatements("defaultDataCreator.createData()");
        tryLoadClass.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        tryLoadClass.getCatchPart().addToStatements("throw new RuntimeException(e)");
        constructor.getBody().addToStatements(ifCreateDefaultData);
        component.addToImports(TumlRestletGenerationUtil.DefaultDataCreator);

        constructor.getBody().addToStatements("setName(\"" + model.getName() + "\")");
        StringBuilder sb = new StringBuilder();
        for (Comment comment : model.getOwnedComments()) {
            sb.append(comment.getBody());
        }
        constructor.getBody().addToStatements("setDescription(\"" + sb.toString() + "\")");

        constructor.getBody().addToStatements("getClients().add(Protocol.FILE)");
        constructor.getBody().addToStatements("getClients().add(Protocol.CLAP)");
        constructor.getBody().addToStatements("getClients().add(Protocol.RIAP)");
        component.addToImports(TumlRestletGenerationUtil.Protocol);
        constructor.getBody().addToStatements("Server server = new Server(new Context(), Protocol.HTTP, 8111)");
        constructor.getBody().addToStatements("server.getContext().getParameters().set(\"tracing\", \"true\")");
        constructor.getBody().addToStatements("getServers().add(server)");
        constructor.getBody().addToStatements("getDefaultHost().attach(\"/" + model.getName() + "\", new " + getApplicationName(model) + "())");
        component.addToImports(TumlRestletGenerationUtil.Server);
        component.addToImports(TumlRestletGenerationUtil.Context);

    }

    private void createApplication(Model model) {
        OJAnnotatedClass component = new OJAnnotatedClass(getApplicationName(model));
        component.setSuperclass(TumlRestletGenerationUtil.Application);
        OJPackage org = new OJPackage("org");
        OJPackage tuml = new OJPackage("umlg", org);
        OJPackage restlet = new OJPackage(model.getName().toLowerCase(), tuml);
        component.setMyPackage(restlet);
        addToSource(component);

        addApplicationInboundRootMethod(model, component);
        addApplicationDefaultConstructor(model, component);
    }

    private String getApplicationName(Model model) {
        return StringUtils.capitalize(model.getName() + "Application");
    }

    private String getComponentName(Model model) {
        return StringUtils.capitalize(model.getName() + "Component");
    }

    private void addApplicationInboundRootMethod(Model model, OJAnnotatedClass application) {
        OJAnnotatedOperation createInboundRoot = new OJAnnotatedOperation("createInboundRoot", TumlRestletGenerationUtil.Restlet);
        TinkerGenerationUtil.addOverrideAnnotation(createInboundRoot);
        application.addToOperations(createInboundRoot);

        createInboundRoot.getBody().addToStatements("Router router = new Router(getContext())");
        createInboundRoot.getBody().addToStatements("restlet.RestletRouterEnum.attachAll(router)");
        createInboundRoot.getBody().addToStatements("router.attach(\"/ui2\", " + TumlRestletGenerationUtil.TumlGuiServerResource.getLast() + ".class, Template.MODE_STARTS_WITH)");

        application.addToImports(TumlRestletGenerationUtil.Router);
        application.addToImports(TumlRestletGenerationUtil.Template);
        application.addToImports(TumlRestletGenerationUtil.TumlGuiServerResource);

        createInboundRoot.getBody().addToStatements("//Directory slickgrid = new Directory(getContext(), \"clap://javascript/javascript/\")");
        createInboundRoot.getBody().addToStatements("Directory slickgrid = new Directory(getContext(), \"file:///home/pieter/Downloads/umlg/runtime/runtime-ui/src/main/javascript/javascript\")");
        createInboundRoot.getBody().addToStatements("slickgrid.setListingAllowed(true)");
        createInboundRoot.getBody().addToStatements("router.attach(\"/javascript/\", slickgrid)");

        createInboundRoot.getBody().addToStatements("//Directory css = new Directory(getContext(), \"clap://javascript/css\")");
        createInboundRoot.getBody().addToStatements("Directory css = new Directory(getContext(), \"file:///home/pieter/Downloads/umlg/runtime/runtime-ui/src/main/javascript/css\")");
        createInboundRoot.getBody().addToStatements("css.setListingAllowed(true)");
        createInboundRoot.getBody().addToStatements("router.attach(\"/css/\", css)");

        application.addToImports(TumlRestletGenerationUtil.Directory);

        createInboundRoot.getBody().addToStatements(TumlRestletGenerationUtil.TumlRestletFilter.getLast() + " tumlRestletFilter = new " + TumlRestletGenerationUtil.TumlRestletFilter.getLast() + "(getContext())");
        application.addToImports(TumlRestletGenerationUtil.TumlRestletFilter);
        createInboundRoot.getBody().addToStatements("tumlRestletFilter.setNext(router)");
        createInboundRoot.getBody().addToStatements("return tumlRestletFilter");
    }

    private void addApplicationDefaultConstructor(Model model, OJAnnotatedClass application) {
        OJConstructor constructor = application.getDefaultConstructor();
        constructor.getBody().addToStatements("setStatusService(new " + TumlRestletGenerationUtil.ErrorStatusService.getLast() + "())");
        application.addToImports(TumlRestletGenerationUtil.ErrorStatusService);
    }

}
