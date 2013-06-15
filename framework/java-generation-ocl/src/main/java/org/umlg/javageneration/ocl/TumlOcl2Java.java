package org.umlg.javageneration.ocl;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.uml.CollectionType;
import org.eclipse.uml2.uml.Classifier;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.javageneration.ocl.visitor.Tuml2JavaVisitor;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.util.TumlCollectionKindEnum;

public class TumlOcl2Java {

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
		
		Classifier returnType = expr.getType();
		if (returnType instanceof CollectionType) {
			CollectionType collectionType = (CollectionType)returnType; 
			TumlCollectionKindEnum tumlCollectionKindEnum = TumlCollectionKindEnum.from(returnType);
			OJPathName collectionPathName = tumlCollectionKindEnum.getOjPathName().getCopy();
			Classifier c = collectionType.getElementType();
			collectionPathName.addToGenerics(TumlClassOperations.getPathName(c));
			return collectionPathName;
		} else {
			return TumlClassOperations.getPathName(returnType);
		}
		
//		if (expr instanceof PropertyCallExp) {
//			PropertyCallExp pc = (PropertyCallExp)expr;
//			Property property = pc.getReferredProperty();
//			PropertyWrapper pWrap = new PropertyWrapper(property);
//			return pWrap.javaTypePath();
//		} else if (expr instanceof IteratorExp) {
//			IteratorExp ie = (IteratorExp)expr;
//			OJPathName result = TumlCollectionKindEnum.from(ie.getType()).getOjPathName();
//			result.addToGenerics(TumlClassOperations.getPathName(TumlCollectionKindEnum.getElementType(ie.getType())));
//			return result;
//		} else if (expr instanceof OperationCallExp) {
//			OperationCallExp oc = (OperationCallExp)expr;
//			OperationWrapper operWrapper = new OperationWrapper(oc.getReferredOperation());
//			return operWrapper.getReturnParamPathName();
//		} else {
//			return null;
//		}
	}	
	
}
