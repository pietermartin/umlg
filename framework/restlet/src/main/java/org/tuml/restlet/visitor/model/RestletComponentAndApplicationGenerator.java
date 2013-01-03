package org.tuml.restlet.visitor.model;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Model;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.java.metamodel.*;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.restlet.util.TumlRestletGenerationUtil;

import java.io.File;

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
        OJPackage tuml = new OJPackage("tuml", org);
        OJPackage restlet = new OJPackage(model.getName().toLowerCase(), tuml);
        component.setMyPackage(restlet);
        addToSource(component);

        addComponentMainMethod(model, component);
        addComponentDefaultConstructor(model, component);
    }

    private void addComponentMainMethod(Model model, OJAnnotatedClass component) {
        OJAnnotatedOperation main = new OJAnnotatedOperation("main");
        main.setStatic(true);
        main.addParam("args", new OJPathName("String[]"));
        component.addToOperations(main);

        main.getBody().addToStatements("URL modelFileURL = Thread.currentThread().getContextClassLoader().getResource(\"" + this.workspace.getModelFile().getName() + "\")");
        OJIfStatement ifFileExist = new OJIfStatement("modelFileURL == null", "throw new IllegalStateException(\"Model file " + this.workspace.getModelFile().getName() + " not found. The model's file name must be on the classpath.\")");
        main.getBody().addToStatements(ifFileExist);
        main.getBody().addToStatements("final File modelFile = new File(modelFileURL.toURI())");
        main.getBody().addToStatements("//Load the mode async\nnew Thread(new Runnable() {\n    @Override\n    public void run() {\n        ModelLoader.loadModel(modelFile);\n        TumlOcl2Parser tumlOcl2Parser = TumlOcl2Parser.INSTANCE;\n    }\n}).start()");
        component.addToImports(TinkerGenerationUtil.ModelLoader);
        component.addToImports(TinkerGenerationUtil.TumlOcl2Parser);
        component.addToImports("java.io.File");
        component.addToImports("java.net.URL");

        main.getBody().addToStatements(getComponentName(model) + " app = new " + getComponentName(model) + "()");
        main.getBody().addToStatements("app.start()");
        main.addToThrows("java.lang.Exception");
    }

    private void addComponentDefaultConstructor(Model model, OJAnnotatedClass component) {
        OJConstructor constructor = component.getDefaultConstructor();

        constructor.getBody().addToStatements("TumlGraphCreator.INSTANCE.startupGraph()");
        component.addToImports(TinkerGenerationUtil.TumlGraphCreator);

        OJIfStatement ifStartAdmin = new OJIfStatement(TinkerGenerationUtil.TumlProperties.getLast() + ".INSTANCE.isStartAdminApplication()", TumlRestletGenerationUtil.Neo4jAdminApp.getLast() + ".startAdminApplication()");
        constructor.getBody().addToStatements(ifStartAdmin);
        component.addToImports(TumlRestletGenerationUtil.Neo4jAdminApp);
        component.addToImports(TinkerGenerationUtil.TumlProperties);

        OJTryStatement tryLoadClass = new OJTryStatement();
        OJIfStatement ifCreateDefaultData = new OJIfStatement(TinkerGenerationUtil.TumlProperties.getLast() + ".INSTANCE.isCreateDefaultData()");
        ifCreateDefaultData.addToThenPart(tryLoadClass);
        tryLoadClass.getTryPart().addToStatements("DefaultDataCreator defaultDataCreator = (DefaultDataCreator)Class.forName(" + TinkerGenerationUtil.TumlProperties.getLast() + ".INSTANCE.getDefaultDataLoaderClass()).newInstance()");
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
        OJPackage tuml = new OJPackage("tuml", org);
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
        createInboundRoot.getBody().addToStatements("Directory slickgrid = new Directory(getContext(), \"file:///home/pieter/workspace-tuml/tuml/runtime/runtime-jquery/src/main/javascript/javascript\")");
        createInboundRoot.getBody().addToStatements("slickgrid.setListingAllowed(true)");
        createInboundRoot.getBody().addToStatements("router.attach(\"/javascript/\", slickgrid)");

        createInboundRoot.getBody().addToStatements("//Directory css = new Directory(getContext(), \"clap://javascript/css\")");
        createInboundRoot.getBody().addToStatements("Directory css = new Directory(getContext(), \"file:///home/pieter/workspace-tuml/tuml/runtime/runtime-jquery/src/main/javascript/css\")");
        createInboundRoot.getBody().addToStatements("css.setListingAllowed(true)");
        createInboundRoot.getBody().addToStatements("router.attach(\"/css/\", css)");

        application.addToImports(TumlRestletGenerationUtil.Directory);

        createInboundRoot.getBody().addToStatements("return router");
    }

    private void addApplicationDefaultConstructor(Model model, OJAnnotatedClass application) {
        application.getDefaultConstructor();
    }

}
