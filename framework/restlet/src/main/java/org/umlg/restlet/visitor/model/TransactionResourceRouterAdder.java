package org.umlg.restlet.visitor.model;

public class TransactionResourceRouterAdder /*extends BaseServerResourceBuilder implements Visitor<Model>*/ {

//	public TransactionResourceRouterAdder(Workspace workspace, String sourceDir) {
//		super(workspace, sourceDir);
//	}
//
//	@Override
//	public void visitBefore(Model model) {
//        addToClassQueryRouterEnum(model, UmlgRestletGenerationUtil.TumlTransactionResourceImpl, "TRANSACTION", "\"/transaction\"");
//	}
//
//    protected void addToClassQueryRouterEnum(Model model, String name, String path) {
//        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");
//        OJEnumLiteral ojLiteral = new OJEnumLiteral(name);
//
//        OJField uri = new OJField();
//        uri.setType(new OJPathName("String"));
//        uri.setInitExp(path);
//        ojLiteral.addToAttributeValues(uri);
//
//        OJField serverResourceClassField = new OJField();
//        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
//        serverResourceClassField.setInitExp(ojPathName.getLast() + ".class");
//        ojLiteral.addToAttributeValues(serverResourceClassField);
//        routerEnum.addToImports(ojPathName);
//        routerEnum.addToImports(UmlgRestletGenerationUtil.ServerResource);
//
//        routerEnum.addToLiterals(ojLiteral);
//
//        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", UmlgRestletGenerationUtil.Router);
//        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
//    }
//
//    @Override
//	public void visitAfter(Model element) {
//
//	}

}
