package org.umlg.restlet.visitor.model;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Model;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.generated.OJVisibilityKindGEN;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;

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
        component.setSuperclass(UmlgRestletGenerationUtil.UmlgRestletComponent);
        OJPackage org = new OJPackage("org");
        OJPackage tuml = new OJPackage("umlg", org);
        OJPackage restlet = new OJPackage(model.getName().toLowerCase(), tuml);
        component.setMyPackage(restlet);
        addToSource(component);

        addComponentMainMethod(model, component);
        addComponentAttachApplications(model, component);
//        addComponentDefaultConstructor(model, component);
//        addStop(component);
    }

    private void addComponentAttachApplications(Model model, OJAnnotatedClass component) {
        OJAnnotatedOperation attachApplications = new OJAnnotatedOperation("attachApplications");
        attachApplications.setVisibility(OJVisibilityKindGEN.PROTECTED);
        UmlgGenerationUtil.addOverrideAnnotation(attachApplications);
        attachApplications.getBody().addToStatements("getDefaultHost().attach(\"/" + model.getName() + "\", new " + getApplicationName(model) + "())");
        component.addToOperations(attachApplications);
    }

    private void addStop(OJAnnotatedClass component) {
        OJAnnotatedOperation stop = new OJAnnotatedOperation("stop");
        UmlgGenerationUtil.addOverrideAnnotation(stop);
        stop.getBody().addToStatements(UmlgGenerationUtil.UMLGAccess + ".shutdown()");
        stop.getBody().addToStatements(UmlgGenerationUtil.UMLGPathName.getLast() + ".remove()");

        stop.getBody().addToStatements("super.stop()");
        component.addToImports(UmlgGenerationUtil.UMLGPathName);
        component.addToImports(UmlgGenerationUtil.UmlgGraphManager);
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
        ojTryStatement.getTryPart().addToStatements("//Load the mode async\nnew Thread(new Runnable() {\n    @Override\n    public void run() {\n        ModelLoader.INSTANCE.loadModel(modelFile);\n        UmlgOcl2Parser tumlOcl2Parser = UmlgOcl2Parser.INSTANCE;\n    }\n}).start()");
        component.addToImports(UmlgGenerationUtil.ModelLoader);
        component.addToImports(UmlgGenerationUtil.UmlgOcl2Parser);
        component.addToImports("java.io.File");
        component.addToImports("java.net.URL");
        ojTryStatement.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");
        constructor.getBody().addToStatements(ojTryStatement);

        constructor.getBody().addToStatements(UmlgGenerationUtil.UmlgGraphManager.getLast() + ".INSTANCE.startupGraph()");
        component.addToImports(UmlgGenerationUtil.UmlgGraphManager);

        OJIfStatement ifStartAdmin = new OJIfStatement(UmlgGenerationUtil.UmlgProperties.getLast() + ".INSTANCE.isStartAdminApplication()", UmlgRestletGenerationUtil.UmlgAdminAppFactory.getLast() + ".getUmlgAdminApp().startAdminApplication()");
        constructor.getBody().addToStatements(ifStartAdmin);
        component.addToImports(UmlgRestletGenerationUtil.UmlgAdminAppFactory);
        component.addToImports(UmlgGenerationUtil.UmlgProperties);

        OJTryStatement tryLoadClass = new OJTryStatement();
        OJIfStatement ifCreateDefaultData = new OJIfStatement(UmlgGenerationUtil.UmlgProperties.getLast() + ".INSTANCE.isCreateDefaultData()");
        ifCreateDefaultData.addToThenPart(tryLoadClass);
        tryLoadClass.getTryPart().addToStatements("DefaultDataCreator defaultDataCreator = (DefaultDataCreator)Class.forName(" + UmlgGenerationUtil.UmlgProperties.getLast() + ".INSTANCE.getDefaultDataLoaderClass()).newInstance()");
        tryLoadClass.getTryPart().addToStatements("defaultDataCreator.createData()");
        tryLoadClass.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        tryLoadClass.getCatchPart().addToStatements("throw new RuntimeException(e)");
        constructor.getBody().addToStatements(ifCreateDefaultData);
        component.addToImports(UmlgRestletGenerationUtil.DefaultDataCreator);

        constructor.getBody().addToStatements("setName(\"" + model.getName() + "\")");
        StringBuilder sb = new StringBuilder();
        for (Comment comment : model.getOwnedComments()) {
            sb.append(comment.getBody());
        }
        constructor.getBody().addToStatements("setDescription(\"" + sb.toString() + "\")");

        constructor.getBody().addToStatements("getClients().add(Protocol.FILE)");
        constructor.getBody().addToStatements("getClients().add(Protocol.CLAP)");
        constructor.getBody().addToStatements("getClients().add(Protocol.RIAP)");
        component.addToImports(UmlgRestletGenerationUtil.Protocol);
        constructor.getBody().addToStatements("Server server = new Server(new Context(), Protocol.HTTP, 8111)");
        constructor.getBody().addToStatements("server.getContext().getParameters().set(\"tracing\", \"true\")");
        constructor.getBody().addToStatements("getServers().add(server)");
        constructor.getBody().addToStatements("getDefaultHost().attach(\"/" + model.getName() + "\", new " + getApplicationName(model) + "())");
        component.addToImports(UmlgRestletGenerationUtil.Server);
        component.addToImports(UmlgRestletGenerationUtil.Context);

    }

    private void createApplication(Model model) {
        OJAnnotatedClass component = new OJAnnotatedClass(getApplicationName(model));
        component.setSuperclass(UmlgRestletGenerationUtil.UmlgRestletApplication);
        OJPackage org = new OJPackage("org");
        OJPackage tuml = new OJPackage("umlg", org);
        OJPackage restlet = new OJPackage(model.getName().toLowerCase(), tuml);
        component.setMyPackage(restlet);
        addToSource(component);

//        addApplicationInboundRootMethod(model, component);
        addApplicationDefaultConstructor(model, component);
        addAttachAll(model, component);
        addGetModelFileName(model, component);
    }

    private void addGetModelFileName(Model model, OJAnnotatedClass component) {
        OJAnnotatedOperation getModelFileName = new OJAnnotatedOperation("getModelFileName");
        getModelFileName.setReturnType("String");
        getModelFileName.setVisibility(OJVisibilityKindGEN.PROTECTED);
        UmlgGenerationUtil.addOverrideAnnotation(getModelFileName);
        getModelFileName.getBody().addToStatements("return \"" + this.workspace.getModelFile().getName() + "\"");
        component.addToOperations(getModelFileName);
    }

    private void addAttachAll(Model model, OJAnnotatedClass component) {
        OJAnnotatedOperation attachAll = new OJAnnotatedOperation("attachAll");
        attachAll.addParam("router", UmlgRestletGenerationUtil.Router);
        attachAll.setVisibility(OJVisibilityKindGEN.PROTECTED);
        UmlgGenerationUtil.addOverrideAnnotation(attachAll);
        attachAll.getBody().addToStatements(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString() + ".attachAll(router);");
        component.addToOperations(attachAll);
    }

    private String getApplicationName(Model model) {
        return StringUtils.capitalize(model.getName() + "Application");
    }

    private String getComponentName(Model model) {
        return StringUtils.capitalize(model.getName() + "Component");
    }

    private void addApplicationInboundRootMethod(Model model, OJAnnotatedClass application) {
        OJAnnotatedOperation createInboundRoot = new OJAnnotatedOperation("createInboundRoot", UmlgRestletGenerationUtil.Restlet);
        UmlgGenerationUtil.addOverrideAnnotation(createInboundRoot);
        application.addToOperations(createInboundRoot);

        createInboundRoot.getBody().addToStatements("Router router = new Router(getContext())");
        createInboundRoot.getBody().addToStatements(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString() + ".attachAll(router)");
        createInboundRoot.getBody().addToStatements("router.attach(\"/ui2\", " + UmlgRestletGenerationUtil.UmlgGuiServerResource.getLast() + ".class, Template.MODE_STARTS_WITH)");

        application.addToImports(UmlgRestletGenerationUtil.Router);
        application.addToImports(UmlgRestletGenerationUtil.Template);
        application.addToImports(UmlgRestletGenerationUtil.UmlgGuiServerResource);

        createInboundRoot.getBody().addToStatements("File current = new File(\".\")");
        application.addToImports("java.io.File");

        createInboundRoot.getBody().addToStatements("//Directory slickgrid = new Directory(getContext(), \"clap://javascript/javascript/\")");
        createInboundRoot.getBody().addToStatements("Directory slickgrid = new Directory(getContext(), \"file:///\" + current.getAbsolutePath() + \"/runtime/runtime-ui/src/main/javascript/javascript\")");
        createInboundRoot.getBody().addToStatements("slickgrid.setListingAllowed(true)");
        createInboundRoot.getBody().addToStatements("router.attach(\"/javascript/\", slickgrid)");

        createInboundRoot.getBody().addToStatements("//Directory css = new Directory(getContext(), \"clap://javascript/css\")");
        createInboundRoot.getBody().addToStatements("Directory css = new Directory(getContext(), \"file:///\" + current.getAbsolutePath() + \"/runtime/runtime-ui/src/main/javascript/css\")");
        createInboundRoot.getBody().addToStatements("css.setListingAllowed(true)");
        createInboundRoot.getBody().addToStatements("router.attach(\"/css/\", css)");

        application.addToImports(UmlgRestletGenerationUtil.Directory);

        createInboundRoot.getBody().addToStatements(UmlgRestletGenerationUtil.UmlgRestletFilter.getLast() + " tumlRestletFilter = new " + UmlgRestletGenerationUtil.UmlgRestletFilter.getLast() + "(getContext())");
        application.addToImports(UmlgRestletGenerationUtil.UmlgRestletFilter);
        createInboundRoot.getBody().addToStatements("tumlRestletFilter.setNext(router)");
        createInboundRoot.getBody().addToStatements("return tumlRestletFilter");
    }

    private void addApplicationDefaultConstructor(Model model, OJAnnotatedClass application) {
        OJConstructor constructor = application.getDefaultConstructor();
//        constructor.getBody().addToStatements("setStatusService(new " + UmlgRestletGenerationUtil.ErrorStatusService.getLast() + "())");
//        application.addToImports(UmlgRestletGenerationUtil.ErrorStatusService);
    }

}
