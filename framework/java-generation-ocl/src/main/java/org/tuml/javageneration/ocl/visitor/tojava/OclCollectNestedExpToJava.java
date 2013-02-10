package org.tuml.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.uml.CollectionType;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.javageneration.ocl.util.TumlOclUtil;
import org.tuml.javageneration.ocl.visitor.HandleIteratorExp;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.util.TumlCollectionKindEnum;

public class OclCollectNestedExpToJava implements HandleIteratorExp {

    /**
     * Generates something like below
     * <p/>
     * return getCollection().select(
     * new BodyExpressionEvaluator<ResultType, ElementType>() {
     *
     * @Override public ResultType evaluate(ElementType e) {
     * return e.expr;
     * });
     * }
     */
    public String handleIteratorExp(OJAnnotatedClass ojClass, IteratorExp<Classifier, Parameter> callExp, String sourceResult, List<String> variableResults, String bodyResult) {
        if (variableResults.size() != 1) {
            throw new IllegalStateException("An ocl select iterator expression may only have on iterator, i.e. variable");
        }

        Variable<Classifier, Parameter> variable = callExp.getIterator().get(0);
        String variableType = TumlClassOperations.className(variable.getType());

        OCLExpression<Classifier> body = callExp.getBody();
        String bodyType = TumlClassOperations.className(body.getType());

        if (body.getType() instanceof CollectionType) {
            CollectionType collectionType = (CollectionType) body.getType();
            ojClass.addToImports(TumlCollectionKindEnum.from(collectionType.getKind()).getOjPathName());
        }

        StringBuilder result = new StringBuilder(sourceResult);
        result.append(".collectNested(");
        result.append("new BodyExpressionEvaluator<");
        result.append(bodyType);
        result.append(", ");
        result.append(variableType);
        result.append(">() {\n");
        result.append("    @Override\n");
        result.append("    public ");
        result.append(bodyType);
        result.append(" evaluate(");
        result.append(TumlOclUtil.removeVariableInit(variableResults.get(0)));
        result.append(") {\n");
        result.append("        return ");
        result.append(bodyResult);
        result.append(";\n    }");
        result.append("\n})");
        return result.toString();
    }

}
