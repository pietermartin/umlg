package org.tuml.javageneration.ocl;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.uml.IteratorExp;
import org.eclipse.ocl.uml.OperationCallExp;
import org.eclipse.ocl.uml.PropertyCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.javageneration.ocl.util.TumlCollectionKindEnum;
import org.tuml.javageneration.ocl.visitor.Tuml2JavaVisitor;
import org.tuml.javageneration.util.OperationWrapper;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TumlClassOperations;

public class TumlOcl2Java {

    public static String getCollectionInterface(MultiplicityElement multiplicityElement) {
        if (multiplicityElement.isUnique() && multiplicityElement.isOrdered()) {
            return "OrderedSet";
        } else if (multiplicityElement.isUnique() && !multiplicityElement.isOrdered()) {
            return "Set";
        } else if (!multiplicityElement.isUnique() && !multiplicityElement.isOrdered()) {
            return "Bag";
        } else if (!multiplicityElement.isUnique() && multiplicityElement.isOrdered()) {
            return "Sequence";
        } else {
            throw new IllegalStateException("Not supported");
        }
    }

    /**
     * 
     * @param ojClass this is passed in in order to make ensure the import statements are correct.
     * @param expr The ocl expression thats being visited
     * @return java code
     */
    public static String oclToJava(OJAnnotatedClass ojClass, OCLExpression<Classifier> expr) {
        return expr.accept(Tuml2JavaVisitor.getInstance(ojClass, expr));
    }
    
	public final static OJPathName calcReturnType(OCLExpression<Classifier> expr) {
		if (expr instanceof PropertyCallExp) {
			PropertyCallExp pc = (PropertyCallExp)expr;
			Property property = pc.getReferredProperty();
			PropertyWrapper pWrap = new PropertyWrapper(property);
			return pWrap.javaTypePath();
		} else if (expr instanceof IteratorExp) {
			IteratorExp ie = (IteratorExp)expr;
			OJPathName result = TumlCollectionKindEnum.from(ie.getType()).getOjPathName();
			result.addToGenerics(TumlClassOperations.getPathName(TumlCollectionKindEnum.getElemenTtype(ie.getType())));
			return result;
		} else if (expr instanceof OperationCallExp) {
			OperationCallExp oc = (OperationCallExp)expr;
			OperationWrapper operWrapper = new OperationWrapper(oc.getReferredOperation());
			return operWrapper.getReturnParamPathName();
		} else {
			return null;
		}
	}	
	
}
