package org.umlg.restlet.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Class;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;

public abstract class BaseServerResourceBuilder extends BaseVisitor {

	public BaseServerResourceBuilder(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	protected void addDefaultConstructor(OJAnnotatedClass annotatedClass) {
		annotatedClass.getDefaultConstructor().getBody().addToStatements("setNegotiated(false)");
	}

    protected void addCompositeParentIdField(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJField compositeParentFieldId;
        if (pWrap.getOtherEnd() != null) {
            compositeParentFieldId = new OJField(UmlgClassOperations.getPathName(pWrap.getOtherEnd().getType()).getLast().toLowerCase() + "Id",
                    new OJPathName("Object"));
        } else {
            compositeParentFieldId = new OJField(UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "Id",
                    new OJPathName("Object"));
        }
        compositeParentFieldId.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToFields(compositeParentFieldId);
    }

    protected String getClassName(PropertyWrapper pWrap, String appendName) {
        String name;
        if (pWrap.getOtherEnd() != null) {
            name = UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_"
                + pWrap.getOtherEnd().getName() + "_" + pWrap.getName() + "_"  + appendName;
        } else {
            name = UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_"
                    + pWrap.getName() + "_" + appendName;
        }
        return name;
    }

    protected void commitOrRollback(OJTryStatement ojTryStatement) {
        OJIfStatement ifTransactionNeedsCommitOrRollback = new OJIfStatement("!(getQueryValue(\"" + UmlgGenerationUtil.rollback+ "\") != null && Boolean.valueOf(getQueryValue(\"" + UmlgGenerationUtil.rollback + "\")))");
        ifTransactionNeedsCommitOrRollback.addToThenPart(UmlgGenerationUtil.UMLGAccess + ".commit()");
        ojTryStatement.getTryPart().addToStatements(ifTransactionNeedsCommitOrRollback);
        ojTryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.UMLGAccess + ".rollback()");
    }

    protected void addToRouterEnum(Model model, OJAnnotatedClass annotatedClass, String name, String path) {
		OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());
		OJEnumLiteral ojLiteral = new OJEnumLiteral(name);

		OJField uri = new OJField();
		uri.setType(new OJPathName("String"));
		uri.setInitExp(path);
		ojLiteral.addToAttributeValues(uri);

		OJField serverResourceClassField = new OJField();
		serverResourceClassField.setType(new OJPathName("java.lang.Class"));
		serverResourceClassField.setInitExp(annotatedClass.getName() + ".class");
		ojLiteral.addToAttributeValues(serverResourceClassField);
		routerEnum.addToImports(annotatedClass.getPathName());
		routerEnum.addToImports(UmlgRestletGenerationUtil.ServerResource);

		routerEnum.addToLiterals(ojLiteral);

		OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", UmlgRestletGenerationUtil.Router);
		attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
	}

    protected String getLookupServerResourceImplName(Class clazz) {
        return UmlgClassOperations.className(clazz) + "LookupServerResourceImpl";
    }

	protected String getServerResourceImplName(Classifier classifier) {
		return UmlgClassOperations.className(classifier) + "ServerResourceImpl";
	}

    protected String getServerResourceMetatDataImplName(Classifier clazz) {
        return UmlgClassOperations.className(clazz) + "MetaDataServerResourceImpl";
    }

    protected String getLookupServerResourceName(Classifier clazz) {
        return UmlgClassOperations.className(clazz) + "LookupServerResource";
    }

    protected String getServerResourceName(Classifier clazz) {
		return UmlgClassOperations.className(clazz) + "ServerResource";
	}

    protected String getServerResourceMetaDataName(Classifier clazz) {
        return UmlgClassOperations.className(clazz) + "MetaDataServerResource";
    }

    protected void addPrivateIdVariable(Classifier clazz, OJAnnotatedClass annotatedClass) {
        OJField privateId = new OJField(getIdFieldName(clazz), new OJPathName("Object"));
        privateId.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToFields(privateId);
    }

    protected String getIdFieldName(Classifier clazz) {
        return StringUtils.uncapitalize(UmlgClassOperations.className(clazz)).toLowerCase() + "Id";
    }

}
