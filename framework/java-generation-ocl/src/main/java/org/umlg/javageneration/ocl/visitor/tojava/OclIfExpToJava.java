package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.*;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.uml.CollectionType;
import org.eclipse.ocl.uml.impl.IfExpImpl;
import org.eclipse.uml2.uml.*;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJParameter;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJAnnotatedParameter;
import org.umlg.javageneration.ocl.visitor.HandleIfExp;
import org.umlg.javageneration.util.OperationWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgCollectionKindEnum;
import org.umlg.javageneration.util.UmlgOperationOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OclIfExpToJava implements HandleIfExp {

	protected OJAnnotatedClass ojClass;
    private NamedElement element;

	@Override
	public String handleIfExp(IfExp<Classifier> ifExp, String conditionResult, String thenResult, String elseResult) {

        Element owner = ((IfExpImpl)ifExp).getOwner();

		OJIfStatement ifStatement = new OJIfStatement(conditionResult, "return " + thenResult, "return " + elseResult);
		String ifOperationName = "ifExp";
		OJPathName ifExpPathName;
		if (ifExp.getType() instanceof CollectionType) {
			CollectionType collectionType = (CollectionType)ifExp.getType();
			ifExpPathName = UmlgCollectionKindEnum.from(collectionType.getKind()).getOjPathName();
			ifExpPathName.addToGenerics(UmlgClassOperations.getPathName(collectionType.getElementType()));
		} else {
			ifExpPathName = UmlgClassOperations.getPathName(ifExp.getType());
		}
        List<String> ifExprParameterNames = new ArrayList<>();
		OJAnnotatedOperation oper = new OJAnnotatedOperation(ifOperationName + this.ojClass.countOperationsStartingWith(ifOperationName), ifExpPathName);
        if (this.element instanceof Operation) {
            Operation operation = (Operation) this.element;
            OperationWrapper operWrapper = new OperationWrapper(operation);
            //pass in the operations parameters to the if method
            List<OJParameter> ojParametersExceptReturn = operWrapper.getOJParametersExceptReturn();
            oper.addToParameters(ojParametersExceptReturn);
            ifExprParameterNames.addAll(ojParametersExceptReturn.stream().map(p -> p.getName()).collect(Collectors.toList()));
        }
        //TODO this logic really needs to be generified and extracted somehow
        if (owner instanceof LoopExp) {
            LoopExp loopExp = (LoopExp) owner;
            for (Object o : loopExp.getIterator()) {
                OJAnnotatedParameter loopParam = new OJAnnotatedParameter();
                org.eclipse.ocl.expressions.Variable<Classifier, Parameter> variable = (Variable<Classifier, Parameter>) o;
                loopParam.setName(variable.getName());
                loopParam.setType(UmlgClassOperations.getPathName(variable.getType()));
                oper.addToParameters(loopParam);
                ifExprParameterNames.add(variable.getName());
            }
        }
        if (owner instanceof IterateExp) {
            IterateExp iterateExp = (IterateExp) owner;
            OJAnnotatedParameter loopParam = new OJAnnotatedParameter();
            org.eclipse.ocl.expressions.Variable<Classifier, Parameter> variable = (Variable<Classifier, Parameter>) iterateExp.getResult();
            loopParam.setName(variable.getName());
            loopParam.setType(UmlgClassOperations.getPathName(variable.getType()));
            oper.addToParameters(loopParam);
            ifExprParameterNames.add(variable.getName());
        }

		this.ojClass.addToOperations(oper);
		oper.setVisibility(OJVisibilityKind.PRIVATE);
		oper.getBody().addToStatements(ifStatement);
//        if (this.element instanceof Operation) {
//            Operation operation = (Operation)this.element;
//            OperationWrapper operWrapper = new OperationWrapper(operation);
            StringBuilder result = new StringBuilder(oper.getName());
            result.append("(");
//            List<Parameter> parametersExceptReturn = UmlgOperationOperations.getParametersExceptReturn(operation);
            int count = 1;
//            for (Parameter p : parametersExceptReturn) {
            for (String p : ifExprParameterNames) {
                result.append(p);
                if (count++ < ifExprParameterNames.size()) {
                    result.append(", ");
                }
            }
            result.append(")");
            return result.toString();
//        } else {
//		    return oper.getName() + "()";
//        }
	}

	@Override
	public HandleIfExp setOJClass(OJAnnotatedClass ojClass) {
		this.ojClass = ojClass;
		return this;
	}

    @Override
    public HandleIfExp setElement(NamedElement element) {
        this.element = element;
        return this;
    }

}
