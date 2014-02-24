package org.umlg.javageneration.visitor._package;

import org.apache.commons.io.FileUtils;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Package;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.io.File;

/**
 * This visitor is responsible for creating a directory structure in src/main/resources
 * accoring to the UML models packages.
 * These directories is where the diagrams must be exported to to be available to the ui.
 * Directories will only ever be created, not cleaned or removed.
 * Date: 2014/01/25
 * Time: 6:50 PM
 */
public class PackageVisitor extends BaseVisitor implements Visitor<Package> {

    private File currentFolder;

    public PackageVisitor(Workspace workspace) {
        super(workspace);
    }

    /**
     * Packages are visited in the correct hierarchical manner, so no need to check for the existence of the
     * parent folder
     *
     * @param _package
     */
    @Override
    @VisitSubclasses()
    public void visitBefore(Package _package) {
        //Can not do this in the constructor as the workspace is not fully initialized by then
        if (this.currentFolder == null) {
            this.currentFolder = new File(this.workspace.getProjectRoot(), this.resourceDir);
            //Ensure resources folder exist
            if (!this.currentFolder.exists()) {
                this.currentFolder.mkdir();
            }
        }

        if (_package.getModel().equals(this.workspace.getModel())) {
            if (_package.equals(this.workspace.getModel())) {
                this.currentFolder = new File(this.currentFolder, _package.getName());
                if (!this.currentFolder.exists() && !this.currentFolder.mkdir()) {
                    throw new IllegalStateException(String.format("Could not create folder for package %s", this.currentFolder.getAbsolutePath()));
                }
            } else {
                if (!(_package.getOwner() instanceof Package)) {
                    throw new IllegalStateException(String.format("Package %s owner not a owner!", _package.getQualifiedName()));
                }
                Package owner = (Package) _package.getOwner();
                if (this.currentFolder.getName().equals(owner.getName())) {
                    this.currentFolder = new File(this.currentFolder, _package.getName());
                    if (!this.currentFolder.exists() && !this.currentFolder.mkdir()) {
                        throw new IllegalStateException(String.format("Could not create folder for package %s", this.currentFolder.getAbsolutePath()));
                    }
                } else {
                    File resourceFolder = FileUtils.getFile(this.workspace.getProjectRoot(), new String[]{"src", "main", "resources"});
                    String[] dirs = _package.getQualifiedName().split("::");
                    this.currentFolder = FileUtils.getFile(resourceFolder, dirs);
                    if (!this.currentFolder.exists() && !this.currentFolder.mkdir()) {
                        throw new IllegalStateException(String.format("Could not create folder for package %s", this.currentFolder.getAbsolutePath()));
                    }
                }
            }
        }
    }

    @Override
    public void visitAfter(Package _package) {

    }
}
