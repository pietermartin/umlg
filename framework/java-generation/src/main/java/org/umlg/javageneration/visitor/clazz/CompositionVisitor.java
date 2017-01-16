package org.umlg.javageneration.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.VisitSubclasses;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.javageneration.visitor.property.UmlgNodeDeleteNotificationBuilder;

import java.util.Set;

public class CompositionVisitor extends BaseVisitor implements Visitor<Class> {

    public CompositionVisitor(Workspace workspace) {
        super(workspace);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        if (UmlgClassOperations.hasCompositeOwner(clazz)) {
            addConstructorWithOwnerAsParameter(annotatedClass, clazz);
        } else {
            if (UmlgClassOperations.getSpecializations(clazz).isEmpty() && !(clazz instanceof AssociationClass)) {
                addImplementRootNodeInterface(annotatedClass);
//                addEdgeToRoot(annotatedClass, clazz);
            }
            if (!clazz.isAbstract()) {
//                implementRootNode(clazz, annotatedClass);
            }
        }
        //This adds a constructor for every composite owner
        addGetOwningObject(annotatedClass, clazz);
        addHasOnlyOneCompositeParent(annotatedClass, clazz);
        addCompositeChildrenToDelete(annotatedClass, clazz);
    }

    @Override
    public void visitAfter(Class clazz) {
    }

//    private void implementRootNode(Class clazz, OJAnnotatedClass annotatedClass) {
//        OJAnnotatedOperation getEdgeToRootLabel = new OJAnnotatedOperation("getEdgeToRootLabel", new OJPathName("String"));
//        getEdgeToRootLabel.getBody().addToStatements("return " + UmlgGenerationUtil.UmlgLabelConverterFactoryPathName.getLast() + ".getUmlgLabelConverter().convert(\"" + UmlgGenerationUtil.getEdgeToRootLabelStrategy(clazz) + "\")");
//        annotatedClass.addToImports(UmlgGenerationUtil.UmlgLabelConverterFactoryPathName);
//        annotatedClass.addToOperations(getEdgeToRootLabel);
//    }

    private void addImplementRootNodeInterface(OJAnnotatedClass annotatedClass) {
        annotatedClass.addToImplementedInterfaces(UmlgGenerationUtil.UMLG_ROOT_NODE);
        annotatedClass.addToImports(UmlgGenerationUtil.UMLG_ROOT_NODE);
    }

    private void addConstructorWithOwnerAsParameter(OJAnnotatedClass annotatedClass, Class clazz) {
        Set<Property> otherEndsToComposite = UmlgClassOperations.getOtherEndToComposite(clazz);
        for (Property otherEndToComposite : otherEndsToComposite) {
            if (!otherEndToComposite.isDerivedUnion()) {
                OJConstructor constructor = new OJConstructor();
                OJPathName otherEndToCompositePathName = UmlgClassOperations.getPathName(otherEndToComposite.getType());
                constructor.addParam("compositeOwner", otherEndToCompositePathName);
                PropertyWrapper otherEndToCompositePWrap = new PropertyWrapper(otherEndToComposite);
                if (otherEndToCompositePWrap.isMemberOfAssociationClass()) {
                    constructor.addParam(StringUtils.uncapitalize(otherEndToCompositePWrap.getAssociationClass().getName()), otherEndToCompositePWrap.getAssociationClassPathName());
                }
                annotatedClass.addToConstructors(constructor);
                constructor.getBody().addToStatements("super(true)");

                PropertyWrapper compositeOwner = new PropertyWrapper(otherEndToCompositePWrap.getOtherEnd());
                if (!otherEndToCompositePWrap.isMemberOfAssociationClass()) {
                    constructor.getBody().addToStatements("compositeOwner." + compositeOwner.adder() + "(this)");
                } else {
                    constructor.getBody().addToStatements("compositeOwner." + compositeOwner.adder() + "(this, " + StringUtils.uncapitalize(compositeOwner.getAssociationClass().getName()) + ")");
                }
            }
        }
    }

    private void addGetOwningObject(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation getOwningObject = new OJAnnotatedOperation("getOwningObject", UmlgGenerationUtil.UMLG_NODE.getCopy());
        UmlgGenerationUtil.addOverrideAnnotation(getOwningObject);
        if (UmlgClassOperations.hasCompositeOwner(clazz)) {
            new OJField(getOwningObject.getBody(), "result", UmlgGenerationUtil.UMLG_NODE.getCopy());
            OJIfStatement ifCompositeParentNotNull = new OJIfStatement();
            Set<Property> otherEndsToComposite = UmlgClassOperations.getOtherEndToComposite(clazz);
            int count = 1;
            for (Property otherEndToComposite : otherEndsToComposite) {
                if (count++ == 1) {
                    ifCompositeParentNotNull.setCondition(new PropertyWrapper(otherEndToComposite).getter() + "() != null");
                    ifCompositeParentNotNull.addToThenPart("result = " + new PropertyWrapper(otherEndToComposite).getter() + "()");
                } else {
                    ifCompositeParentNotNull.addToElseIfCondition(new PropertyWrapper(otherEndToComposite).getter() + "() != null", "result = " + new PropertyWrapper(otherEndToComposite).getter() + "()");
                }
            }
            ifCompositeParentNotNull.addToElsePart("result = null");
            getOwningObject.getBody().addToStatements(ifCompositeParentNotNull);
            getOwningObject.getBody().addToStatements("return result");
        } else if (clazz instanceof AssociationClass) {
            boolean foundOwningObject = false;
            //Make the controlling side the owning object
            AssociationClass associationClass = (AssociationClass) clazz;
            for (Property memberEnd : associationClass.getMemberEnds()) {
                PropertyWrapper pWrap = new PropertyWrapper(memberEnd);
                //In a regular one to many the many is the controlling side of the association.
                //So the non controlling side is the owning end.
                if (!pWrap.isControllingSide()) {
                    getOwningObject.getBody().addToStatements("return " + pWrap.getter() + "()");
                    foundOwningObject = true;
                    break;
                }
            }
            if (!foundOwningObject) {
                throw new IllegalStateException("Must find owning object for an association class!");
            }
        } else {
            getOwningObject.getBody().addToStatements("return null");
        }
        annotatedClass.addToOperations(getOwningObject);
    }

    private void addHasOnlyOneCompositeParent(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation hasOnlyOneCompositeParent = new OJAnnotatedOperation("hasOnlyOneCompositeParent", "boolean");
        UmlgGenerationUtil.addOverrideAnnotation(hasOnlyOneCompositeParent);
        OJField result = new OJField(hasOnlyOneCompositeParent.getBody(), "result", "int");
        result.setInitExp("0");
        if (UmlgClassOperations.hasCompositeOwner(clazz)) {
            Set<Property> otherEndsToComposite = UmlgClassOperations.getOtherEndToComposite(clazz);
            for (Property otherEndToComposite : otherEndsToComposite) {
                if (!otherEndToComposite.isDerivedUnion()) {
                    hasOnlyOneCompositeParent.getBody().addToStatements("result = result + (" + new PropertyWrapper(otherEndToComposite).getter() + "() != null ? 1 : 0)");
                }
            }
        }
        hasOnlyOneCompositeParent.getBody().addToStatements("return result == 1");
        annotatedClass.addToOperations(hasOnlyOneCompositeParent);
    }

    private void addEdgeToRoot(OJAnnotatedClass annotatedClass, Class clazz) {
        OJConstructor constructor = annotatedClass.findConstructor(new OJPathName("java.lang.Boolean"));
        constructor.getBody().addToStatements(
                UmlgGenerationUtil.edgePathName.getLast() + " edge = " +
                        UmlgGenerationUtil.UMLGAccess + ".getRoot().addEdge(getEdgeToRootLabel(), this.vertex)");
        constructor.getBody().addToStatements("edge.property(\"inClass\", this.getClass().getName())");
        annotatedClass.addToImports(UmlgGenerationUtil.edgePathName.getCopy());
        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName.getCopy());
    }

    private void addCompositeChildrenToDelete(OJAnnotatedClass annotatedClass, Class clazz) {
        OJAnnotatedOperation delete = annotatedClass.findOperation("delete");
        for (Property p : UmlgClassOperations.getChildPropertiesToDelete(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (!pWrap.isDataType()) {
                if (pWrap.isMany()) {
                    OJForStatement forChildToDelete = new OJForStatement("child", pWrap.javaBaseTypePath(), pWrap.getter() + "()");
                    forChildToDelete.getBody().addToStatements("child.delete()");
                    delete.getBody().addToStatements(forChildToDelete);
                } else {
                    OJIfStatement ifChildToDeleteNotNull = new OJIfStatement(pWrap.getter() + "() != null");
                    ifChildToDeleteNotNull.addToThenPart(pWrap.getter() + "().delete()");
                    delete.getBody().addToStatements(ifChildToDeleteNotNull);
                }
            }
        }

        for (Property p : UmlgClassOperations.getPropertiesToClearOnDeletion(clazz)) {
            PropertyWrapper pWrap = new PropertyWrapper(p);
            delete.getBody().addToStatements("this." + pWrap.fieldname() + ".clear()");
            //Add change listener
            Property otherEnd = p.getOtherEnd();
            if (otherEnd != null) {
                PropertyWrapper otherEndPWrap = PropertyWrapper.from(otherEnd);
                if (otherEndPWrap.isChangedListener()) {
                    UmlgNodeDeleteNotificationBuilder.buildDeleteNotification(annotatedClass, delete, otherEndPWrap);
                }
            }
        }
        if (clazz.getGenerals().isEmpty()) {
            delete.getBody().addToStatements(UmlgGenerationUtil.transactionThreadEntityVar.getLast() + ".remove(this)");
            delete.getBody().addToStatements("this.vertex.remove()");
            annotatedClass.addToImports(UmlgGenerationUtil.transactionThreadEntityVar);
            annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName.getCopy());
        } else {
            delete.getBody().addToStatements("super.delete()");
        }
    }

}
