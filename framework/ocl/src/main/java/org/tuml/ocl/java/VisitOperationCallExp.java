/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuml.ocl.java;

import java.util.List;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.utilities.AbstractVisitor;

/**
 *
 * @author pieter
 */
public class VisitOperationCallExp<Object, Classifier, Operation, P, EL, PM, S, COA, SSA, CT> extends AbstractVisitor<Object, Classifier, Operation, P, EL, PM, S, COA, SSA, CT> {

    @Override
    protected Object handleOperationCallExp(OperationCallExp<Classifier, Operation> callExp,
            Object sourceResult, List<Object> argumentResults) {
        System.out.println(callExp);
        return result;
    }
}
