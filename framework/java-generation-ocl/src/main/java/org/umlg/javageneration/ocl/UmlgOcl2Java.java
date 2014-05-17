package org.umlg.javageneration.ocl;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.uml.CollectionType;
import org.eclipse.ocl.uml.PropertyCallExp;
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

        OJPathName collectionPathName = null;
        //Check if the property is qualified, if ignore the given multiplicity and return a raw multiplicity of many
        if (expr instanceof PropertyCallExp) {
            PropertyCallExp pe = (PropertyCallExp)expr;
            if (pe.getQualifier().isEmpty() && !pe.getReferredProperty().getQualifiers().isEmpty()) {
                collectionPathName = UmlgCollectionKindEnum.SET.getOjPathName().getCopy();
                collectionPathName.addToGenerics(UmlgClassOperations.getPathName(pe.getType()));
            } else if (!pe.getQualifier().isEmpty() && (pe.getReferredProperty().getUpper() == -1 || pe.getReferredProperty().getUpper() > 1)) {
                collectionPathName = UmlgCollectionKindEnum.SET.getOjPathName().getCopy();
                collectionPathName.addToGenerics(UmlgClassOperations.getPathName(pe.getType()));
            }
        }

        if (collectionPathName == null) {
            Classifier returnType = expr.getType();
            if (returnType instanceof CollectionType) {
                CollectionType collectionType = (CollectionType)returnType;
                UmlgCollectionKindEnum tumlCollectionKindEnum = UmlgCollectionKindEnum.from(returnType);
                collectionPathName = tumlCollectionKindEnum.getOjPathName().getCopy();
                Classifier c = collectionType.getElementType();
                collectionPathName.addToGenerics(UmlgClassOperations.getPathName(c));
            } else {
                collectionPathName = UmlgClassOperations.getPathName(returnType);
            }
        }
        return collectionPathName;

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
