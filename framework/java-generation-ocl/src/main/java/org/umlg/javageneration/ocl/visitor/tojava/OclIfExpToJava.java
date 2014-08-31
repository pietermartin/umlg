package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.IfExp;
import org.eclipse.ocl.uml.CollectionType;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.umlg.java.metamodel.OJIfStatement;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.ocl.visitor.HandleIfExp;
import org.umlg.javageneration.util.OperationWrapper;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgCollectionKindEnum;
import org.umlg.javageneration.util.UmlgOperationOperations;

import java.util.List;

public class OclIfExpToJava implements HandleIfExp {

	protected OJAnnotatedClass ojClass;
    private NamedElement element;

	@Override
	public String handleIfExp(IfExp<Classifier> ifExp, String conditionResult, String thenResult, String elseResult) {
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
		OJAnnotatedOperation oper = new OJAnnotatedOperation(ifOperationName + this.ojClass.countOperationsStartingWith(ifOperationName), ifExpPathName);
        if (this.element instanceof Operation) {
            Operation operation = (Operation)this.element;
            OperationWrapper operWrapper = new OperationWrapper(operation);
            //pass in the operations parameters to the if method
            oper.addToParameters(operWrapper.getOJParametersExceptReturn());
        }
		this.ojClass.addToOperations(oper);
		oper.setVisibility(OJVisibilityKind.PRIVATE);
		oper.getBody().addToStatements(ifStatement);
        if (this.element instanceof Operation) {
            Operation operation = (Operation)this.element;
            OperationWrapper operWrapper = new OperationWrapper(operation);
            StringBuilder result = new StringBuilder(oper.getName());
            result.append("(");
            List<Parameter> parametersExceptReturn = UmlgOperationOperations.getParametersExceptReturn(operation);
            int count = 1;
            for (Parameter p : parametersExceptReturn) {
                result.append(p.getName());
                if (count++ < parametersExceptReturn.size()) {
                    result.append(", ");
                }
            }
            result.append(")");
            return result.toString();
        } else {
		    return oper.getName() + "()";
        }
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
