package org.umlg.javageneration.visitor;

import java.util.List;

import org.eclipse.uml2.uml.*;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedField;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.PropertyWrapper;

public class BaseVisitor {

    protected Workspace workspace;
    protected String sourceDir;

    public BaseVisitor(Workspace workspace) {
        super();
        this.workspace = workspace;
        this.sourceDir = Workspace.DEFAULT_SOURCE_FOLDER;
    }

    public BaseVisitor(Workspace workspace, String sourceDir) {
        super();
        this.workspace = workspace;
        this.sourceDir = sourceDir;
    }

    protected void addToSource(OJAnnotatedClass source) {
        this.workspace.addToClassMap(source, sourceDir);
    }

    protected OJAnnotatedClass findOJClass(NamedElement owner) {
        return this.workspace.findOJClass(Namer.qualifiedName(owner));
    }

    protected OJAnnotatedClass findAuditOJClass(Property p) {
        OJAnnotatedClass ojClass = findOJClass(p);
        OJAnnotatedClass findOJClass = this.workspace.findOJClass(ojClass.getQualifiedName() + "Audit");
        return findOJClass;
    }

    protected OJAnnotatedClass findAuditOJClass(org.eclipse.uml2.uml.Class clazz) {
        OJAnnotatedClass ojClass = findOJClass(clazz);
        OJAnnotatedClass findOJClass = this.workspace.findOJClass(ojClass.getQualifiedName() + "Audit");
        return findOJClass;
    }

    protected OJAnnotatedClass findAssociationClassOJClass(PropertyWrapper pWrap) {
        if (!pWrap.isMemberOfAssociationClass()) {
             throw new IllegalStateException("property " + pWrap.getQualifiedName() + " is not a member of an association class!");
        }
        AssociationClass associationClass = pWrap.getAssociationClass();
        return findOJClass(associationClass);
    }

    // TODO think about interfaces
    protected OJAnnotatedClass findOJClass(Property p) {
        PropertyWrapper pWrap;
        if (p instanceof PropertyWrapper) {
            pWrap = (PropertyWrapper) p;
        } else {
            pWrap = new PropertyWrapper(p);
        }
        if (!pWrap.isQualifier()) {
            Element owner = p.getOwner();
            // Association must come first in this if statement as Association
            // is also a Classifier
            if (owner instanceof AssociationClass && ((AssociationClass)owner).getOwnedAttributes().contains(p)) {
                return this.workspace.findOJClass(Namer.qualifiedName((NamedElement) owner));
            } else if (owner instanceof Association) {
                Association a = (Association) owner;
                List<Property> members = a.getMemberEnds();
                Property otherEnd = null;
                for (Property member : members) {
                    if (p instanceof PropertyWrapper) {
                        if (member != ((PropertyWrapper) p).getProperty()) {
                            otherEnd = member;
                            break;
                        }
                    } else {
                        if (member != p) {
                            otherEnd = member;
                            break;
                        }
                    }
                }
                if (otherEnd == null) {
                    throw new IllegalStateException("Oy, where is the other end gone to!!!");
                }
                return this.workspace.findOJClass(Namer.qualifiedName(otherEnd.getType()));
            } else if (owner instanceof Classifier) {
                return this.workspace.findOJClass(Namer.qualifiedName((NamedElement) owner));
            } else if (owner instanceof Property) {
                return this.workspace.findOJClass(Namer.qualifiedName((NamedElement) owner));
            } else {
                throw new IllegalStateException("Not catered for, think about ne. " + owner.getClass().getSimpleName());
            }
        } else {
            Property owner = (Property) pWrap.getOwner();
            return this.workspace.findOJClass(Namer.qualifiedName(owner.getType()));
        }
    }

    protected void buildField(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
        OJAnnotatedField field = new OJAnnotatedField(propertyWrapper.fieldname(), propertyWrapper.javaTumlTypePath());
        field.setStatic(propertyWrapper.isStatic());
        owner.addToFields(field);
        if (propertyWrapper.isMemberOfAssociationClass()) {
            OJAnnotatedField acField = new OJAnnotatedField(propertyWrapper.getAssociationClassFakePropertyName(), propertyWrapper.getAssociationClassJavaTumlTypePath());
            owner.addToFields(acField);

            OJAnnotatedClass associationClassOJClass = findAssociationClassOJClass(propertyWrapper);
            PropertyWrapper otherEnd = new PropertyWrapper(propertyWrapper.getOtherEnd());
            acField = new OJAnnotatedField(otherEnd.fieldname(), otherEnd.javaTumlTypePath(true));
            associationClassOJClass.addToFields(acField);
        }
    }

    protected void buildRemover(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
        OJAnnotatedOperation remover = new OJAnnotatedOperation(propertyWrapper.remover());
        remover.addParam(propertyWrapper.fieldname(), propertyWrapper.javaTypePath());
        OJIfStatement ifNotNull = new OJIfStatement("!" + propertyWrapper.fieldname() + ".isEmpty()");
        ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".removeAll(" + propertyWrapper.fieldname() + ")");

        if (propertyWrapper.isMemberOfAssociationClass()) {
            ifNotNull.addToThenPart("this." + propertyWrapper.getAssociationClassFakePropertyName() + " = " + propertyWrapper.javaDefaultInitialisationForAssociationClass((BehavioredClassifier)propertyWrapper.getOtherEnd().getType()));
        }

        remover.getBody().addToStatements(ifNotNull);
        owner.addToOperations(remover);

        OJAnnotatedOperation singleRemover = new OJAnnotatedOperation(propertyWrapper.remover());
        singleRemover.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
        ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");
        ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".remove(" + propertyWrapper.fieldname() + ")");
        if (propertyWrapper.isMemberOfAssociationClass()) {
            ifNotNull.addToThenPart("this." + propertyWrapper.getAssociationClassFakePropertyName() + " = " + propertyWrapper.javaDefaultInitialisationForAssociationClass((BehavioredClassifier)propertyWrapper.getOtherEnd().getType()));
        }
        singleRemover.getBody().addToStatements(ifNotNull);
        owner.addToOperations(singleRemover);
    }

    protected void buildClearer(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
        OJAnnotatedOperation remover = new OJAnnotatedOperation(propertyWrapper.clearer());
        remover.getBody().addToStatements("this." + propertyWrapper.fieldname() + ".clear()");
        owner.addToOperations(remover);
        if (propertyWrapper.isMemberOfAssociationClass()) {
            //Wack the association classes also
            remover.getBody().addToStatements("this." + propertyWrapper.getAssociationClassFakePropertyName() + " = " + propertyWrapper.javaDefaultInitialisationForAssociationClass((BehavioredClassifier)propertyWrapper.getOtherEnd().getType()));
            owner.addToOperations(remover);
        }
    }

}
