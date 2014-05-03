package org.umlg.javageneration.ocl;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.uml.CollectionType;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.javageneration.ocl.visitor.UmlgJavaVisitor;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgCollectionKindEnum;

public class UmlgOcl2Java {

    /**
     * 
     * @param ojClass this is passed in in order to make ensure the import statements are correct.
     * @param expr The ocl expression thats being visited
     * @return java code
     */
    public static String oclToJava(NamedElement namedElement, OJAnnotatedClass ojClass, OCLExpression<Classifier> expr) {
        return expr.accept(UmlgJavaVisitor.getInstance(ojClass, namedElement, expr));
    }
    
	public final static OJPathName calcReturnType(OCLExpression<Classifier> expr) {
		
		Classifier returnType = expr.getType();
		if (returnType instanceof CollectionType) {
			CollectionType collectionType = (CollectionType)returnType; 
			UmlgCollectionKindEnum tumlCollectionKindEnum = UmlgCollectionKindEnum.from(returnType);
			OJPathName collectionPathName = tumlCollectionKindEnum.getOjPathName().getCopy();
			Classifier c = collectionType.getElementType();
			collectionPathName.addToGenerics(UmlgClassOperations.getPathName(c));
			return collectionPathName;
		} else {
			return UmlgClassOperations.getPathName(returnType);
		}
		
//		if (expr instanceof PropertyCallExp) {
//			PropertyCallExp pc = (PropertyCallExp)expr;
//			Property property = pc.getReferredProperty();
//			PropertyWrapper pWrap = new PropertyWrapper(property);
//			return pWrap.javaTypePath();
//		} else if (expr instanceof IteratorExp) {
//			IteratorExp ie = (IteratorExp)expr;
//			OJPathName result = UmlgCollectionKindEnum.from(ie.getType()).getOjPathName();
//			result.addToGenerics(UmlgClassOperations.getPathName(UmlgCollectionKindEnum.getElementType(ie.getType())));
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
