package org.umlg.restlet.visitor.model;

import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.*;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;
import org.umlg.restlet.visitor.clazz.BaseServerResourceBuilder;

import java.util.List;

public class DiagramPackageResourceBuilder extends BaseServerResourceBuilder implements Visitor<Model> {

    private int depth = 0;

    public DiagramPackageResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Model model) {

        OJPackage ojPackage = new OJPackage(UmlgGenerationUtil.UmlgRootPackage.toJavaString());
        OJAnnotatedClass diagramTreeView = new OJAnnotatedClass(UmlgRestletGenerationUtil.UmlgDiagramPackageResource.getLast());
        diagramTreeView.setMyPackage(ojPackage);
        diagramTreeView.setSuperclass(UmlgRestletGenerationUtil.ServerResource);
        addToSource(diagramTreeView);
        addDefaultConstructor(diagramTreeView);
        addGetRepresentation(diagramTreeView);
        addToRouterEnum(model, diagramTreeView, "DIAGRAM_PACKAGE", "\"/diagramPackages\"");
    }

    private void addGetRepresentation(OJAnnotatedClass queryExecute) {
        OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
        queryExecute.addToOperations(getInf);
        getInf.addAnnotationIfNew(new OJAnnotationValue(UmlgRestletGenerationUtil.Get, "json"));

        OJAnnotatedOperation get = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
        get.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        queryExecute.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(get);
        queryExecute.addToOperations(get);

        StringBuilder treeView = new StringBuilder();
//        treeView.append("\"[{\\\"label\\\":\\\"node1\\\",\\\"children\\\":[{\\\"label\\\":\\\"child1\\\"},{\\\"label\\\":\\\"child2\\\"}]}]\"");
        treeView.append("\"[{\\\"label\\\":");
        List<Package> packages = ModelLoader.INSTANCE.getAllPackages();
        Package current = null;
        for (Package _package : packages) {
            if (current == null) {
                if (!_package.equals(this.workspace.getModel())) {
                    throw new IllegalStateException("Expecting package to be the model!");
                }
                current = _package;
                treeView.append("\\\"" + _package.getName() + "\\\"");
            } else {
                if (_package.getModel().equals(this.workspace.getModel())) {
                    //Work out if it is a child or not
                    if (_package.getOwner().equals(current)) {
                        //Child
                        treeView.append(",\\\"children\\\":[{\\\"label\\\":\\\"" + _package.getName() + "\\\"");
                    } else {
                        //Calculate the depth to see how many to close
                        this.depth = 0;
                        calculateDepth(current);
                        int currentDepth = this.depth;
                        this.depth = 0;
                        calculateDepth(_package);
                        int packageDepth = this.depth;
                        for (int i = 0; i < (currentDepth - packageDepth); i++) {
                            treeView.append("}]");
                        }
                        treeView.append("},{\\\"label\\\":\\\"" + _package.getName() + "\\\"");
                    }
                    current = _package;
                }
            }
        }

        //Calculate the depth to see how many to close
        this.depth = 0;
        calculateDepth(current);
        int currentDepth = this.depth;
        for (int i = 0; i <currentDepth; i++) {
            treeView.append("}]");
        }

        treeView.append("}]\"");

        get.getBody().addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(" + treeView.toString() + ")");
        queryExecute.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);

    }

    /**
     * Calculate how many levels higher in the tree aPackage is compared to current
     * @param aPackage
     */
    private void calculateDepth(Package aPackage) {
        if (!aPackage.equals(this.workspace.getModel())) {
            this.depth++;
            calculateDepth((Package)aPackage.getOwner());
        }
    }

    protected void addToRouterEnum(String name, String path) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());
        OJEnumLiteral ojLiteral = new OJEnumLiteral(name);

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp(path);
        ojLiteral.addToAttributeValues(uri);

        OJField serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(UmlgRestletGenerationUtil.UmlgDiagramPackageResource.getLast() + ".class");
        ojLiteral.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(UmlgRestletGenerationUtil.UmlgDiagramPackageResource);
        routerEnum.addToImports(UmlgRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", UmlgRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
    }

    @Override
    public void visitAfter(Model element) {

    }

}
