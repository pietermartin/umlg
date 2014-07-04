package org.umlg.restlet.visitor.model;

import org.apache.commons.io.FileUtils;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Package;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.*;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;
import org.umlg.restlet.visitor.clazz.BaseServerResourceBuilder;

import java.io.File;
import java.util.*;

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

        //Add the diagram resource to the router
        addDiagramResourceToRouterEnum("DIAGRAM", "\"/diagram\"");
    }

    protected void addDiagramResourceToRouterEnum(String name, String path) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());
        OJEnumLiteral ojLiteral = new OJEnumLiteral(name);

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp(path);
        ojLiteral.addToAttributeValues(uri);

        OJField serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(UmlgRestletGenerationUtil.UmlgDiagramResource.getLast() + ".class");
        ojLiteral.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(UmlgRestletGenerationUtil.UmlgDiagramResource);
        routerEnum.addToImports(UmlgRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", UmlgRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
    }

    private void addGetRepresentation(OJAnnotatedClass queryExecute) {

        OJAnnotatedOperation get = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
        get.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        queryExecute.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(get);
        queryExecute.addToOperations(get);

        StringBuilder treeView = new StringBuilder();
        treeView.append("\"[{\\\"label\\\":");
        List<Package> packages = ModelLoader.INSTANCE.getAllPackages();
        Package current = null;
        boolean addedChildren = false;
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
                        if (addedChildren) {
                            treeView.append(",{\\\"label\\\":\\\"" + _package.getName() + "\\\"");
                        } else {
                            treeView.append(",\\\"children\\\":[{\\\"label\\\":\\\"" + _package.getName() + "\\\"");
                        }
                        Map<String, Pair<String, String>> diagrams = getDiagramNamesInPackage(_package);
                        if (!diagrams.isEmpty()) {
                            addedChildren = true;
                            treeView.append(",\\\"children\\\": [");
                            boolean first = true;
                            for (String diagramName : diagrams.keySet()) {
                                if (!first) {
                                    treeView.append(",");
                                }
                                first = false;
                                treeView.append("{\\\"label\\\":\\\"" + diagramName + "\\\",\\\"id\\\":\\\"" + diagramName + "\\\",\\\"path\\\":\\\"" + diagrams.get(diagramName).getFirst() + "\\\",\\\"type\\\":\\\"" + diagrams.get(diagramName).getSecond() + "\\\"}");
                            }
                        } else {
                            addedChildren = false;
                        }
                    } else {

                        if (addedChildren) {
                            treeView.append("]");
                        }

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
                        Map<String, Pair<String, String>> diagrams = getDiagramNamesInPackage(_package);
                        if (!diagrams.isEmpty()) {
                            addedChildren = true;
                            treeView.append(",\\\"children\\\": [");
                            boolean first = true;
                            for (String diagramName : diagrams.keySet()) {
                                if (!first) {
                                    treeView.append(",");
                                }
                                first = false;
                                treeView.append("{\\\"label\\\":\\\"" + diagramName + "\\\",\\\"id\\\":\\\"" + diagramName + "\\\",\\\"path\\\":\\\"" + diagrams.get(diagramName).getFirst() + "\\\",\\\"type\\\":\\\"" + diagrams.get(diagramName).getSecond() + "\\\"}");
                            }
                        } else {
                            addedChildren = false;
                        }

                    }
                    current = _package;
                }
            }
        }

        if (addedChildren) {
            treeView.append("]");
        }

        //Calculate the depth to see how many to close
        this.depth = 0;
        calculateDepth(current);
        int currentDepth = this.depth;
        for (int i = 0; i < currentDepth; i++) {
            treeView.append("}]");
        }

        treeView.append("}]\"");

        get.getBody().addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(" + treeView.toString() + ")");
        queryExecute.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);

    }

    /**
     * Calculate how many levels higher in the tree aPackage is compared to current
     *
     * @param aPackage
     */
    private void calculateDepth(Package aPackage) {
        if (!aPackage.equals(this.workspace.getModel())) {
            this.depth++;
            calculateDepth((Package) aPackage.getOwner());
        }
    }

    private Map<String, Pair<String, String>> getDiagramNamesInPackage(Package aPackage) {
        String[] dirs = aPackage.getQualifiedName().split("::");
        File projectRoot = this.workspace.getEntitiesRoot();
        File resourceFolder = FileUtils.getFile(projectRoot, new String[]{"src", "main", "resources"});
        File packageFile = FileUtils.getFile(resourceFolder, dirs);
        Collection<File> diagrams = FileUtils.listFiles(packageFile, new String[]{"PNG", "SVG", "png", "svg"}, false);
        Map<String, Pair<String, String>> result = new HashMap<String, Pair<String, String>>();
        for (File f : diagrams) {
            result.put(
//                    f.getName().substring(0, f.getName().indexOf(".")),
                    f.getName().replace(".","-"),
                    new Pair<String, String>(
                            aPackage.getQualifiedName().replace("::", "/") + "/" + f.getName(),
                            f.getName().substring(f.getName().indexOf(".") + 1)
                    )
            );
        }
        return result;
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
//        routerEnum.addToImports(UmlgRestletGenerationUtil.UmlgDiagramPackageResource);
        routerEnum.addToImports(UmlgRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", UmlgRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
    }

    @Override
    public void visitAfter(Model element) {

    }

    public static class Pair<T1, T2> {
        private T1 obj1;
        private T2 obj2;

        private Pair() {
        }

        private Pair(T1 obj1, T2 obj2) {
            this.obj1 = obj1;
            this.obj2 = obj2;
        }

        public void setFirst(T1 obj1) {
            this.obj1 = obj1;
        }

        public void setSecond(T2 obj2) {
            this.obj2 = obj2;
        }

        public T1 getFirst() {
            return obj1;
        }

        public T2 getSecond() {
            return obj2;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;

            Pair<T1, T2> pair = (Pair<T1, T2>) o;

            if (obj1 != null ? !obj1.equals(pair.obj1) : pair.obj1 != null) return false;
            if (obj2 != null ? !obj2.equals(pair.obj2) : pair.obj2 != null) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = (obj1 != null ? obj1.hashCode() : 0);
            result = 31 * result + (obj2 != null ? obj2.hashCode() : 0);
            return result;
        }

    }

}
