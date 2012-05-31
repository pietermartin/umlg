package org.tuml.javageneration.util;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.operations.PropertyOperations;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJSimpleStatement;
import org.tuml.javageneration.naming.Namer;

public final class TumlPropertyOperations extends PropertyOperations {

	public static boolean isMany(Property property) {
		int qualifierCount = property.getQualifiers().size();
		return property.getUpper() > 1 || qualifierCount > 0;
	}

	public static  boolean isOne(Property property) {
		return !isMany(property);
	}

	public static boolean isQualifier(Property p) {
		return p.getOwner() != null && p.getOwner() instanceof Property;
	}
	
	public static String fieldName(Property p) {
		return StringUtils.uncapitalize(p.getName());
	}
	
	public static boolean isInverse(Property p) {
		boolean result = p.isComposite();
		if (p.getOtherEnd() == null) {
			return result = true;
		} else if (isOneToOne(p) && !p.isComposite() && !p.getOtherEnd().isComposite()) {
			//If association is OneToOne and both sides are non composite then take the non optional 1-1 side as inverse=true
			result = p.getLower() == 1 && p.getUpper() == 1;
		} else if (isOneToMany(p) && !p.isComposite() && !p.getOtherEnd().isComposite()) {
			//If association is OneToMany and both sides are non composite then take the many side as inverse=true
			result = p.getUpper() > 1;
		} else if (isManyToMany(p) && !p.isComposite() && !p.getOtherEnd().isComposite()) {
			//If association is ManyToMany and both sides are non composite then take any side consistently
			result = 0 > p.getName().compareTo(p.getOtherEnd().getName());
		}
		return result;
	}

	public static boolean isOneToMany(Property p) {
		return otherEndIsOne(p) && isMany(p);
	}

	public static boolean isManyToMany(Property p) {
		return !otherEndIsOne(p) && isMany(p);
	}

	public static boolean isManyToOne(Property p) {
		return !otherEndIsOne(p) && isOne(p);
	}

	public static boolean isOneToOne(Property p) {
		return otherEndIsOne(p) && isOne(p);
	}

	protected static boolean otherEndIsOne(Property p){
		if(p.getOtherEnd() != null){
			Property otherEnd = p.getOtherEnd();
			return otherEnd.isNavigable() && isOne(otherEnd) && otherEnd.getQualifiers().size() == 0;
		}else{
			return false;
		}
	}

	public static OJPathName getDefaultTinkerCollection(Property p) {
		OJPathName collectionPathName;
		if (p.isOrdered() && p.isUnique()) {
			if (!p.getQualifiers().isEmpty()) {
				collectionPathName = TinkerGenerationUtil.tinkerQualifiedOrderedSetImpl.getCopy();
			} else {
				collectionPathName = TinkerGenerationUtil.tinkerOrderedSetImpl.getCopy();
			}
		} else if (p.isOrdered() && !p.isUnique()) {
			if (!p.getQualifiers().isEmpty()) {
				collectionPathName = TinkerGenerationUtil.tinkerQualifiedSequenceImpl.getCopy();
			} else {
				collectionPathName = TinkerGenerationUtil.tinkerSequenceImpl.getCopy();
			}
		} else if (!p.isOrdered() && !p.isUnique()) {
			if (!p.getQualifiers().isEmpty()) {
				collectionPathName = TinkerGenerationUtil.tinkerQualifiedBagImpl.getCopy();
			} else {
				collectionPathName = TinkerGenerationUtil.tinkerBagImpl.getCopy();
			}
		} else if (!p.isOrdered() && p.isUnique()) {
			if (!p.getQualifiers().isEmpty()) {
				collectionPathName = TinkerGenerationUtil.tinkerQualifiedSetImpl.getCopy();
			} else {
				collectionPathName = TinkerGenerationUtil.tinkerSetImpl.getCopy();
			}
		} else {
			throw new RuntimeException("wtf");
		}
		collectionPathName.addToElementTypes(getTypePath(p));
		return collectionPathName;
	}

	public static OJSimpleStatement getDefaultTinkerCollectionInitalisation(Property p) {
		OJSimpleStatement s = getDefaultTinkerCollectionInitalisation(p, getDefaultTinkerCollection(p));
		return s;
	}
	
	public static OJSimpleStatement getDefaultTinkerCollectionInitalisation(Property p, OJPathName collectionPathName) {
		OJSimpleStatement ojSimpleStatement = new OJSimpleStatement(" new " + collectionPathName.getCollectionTypeName() + "(this, \""
				+ TinkerGenerationUtil.getEdgeName(p) + "\"");
		
		if (p.getQualifiers().isEmpty() && p.isOrdered()) {
			ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() + ", getUid()");
		} else if (!p.getQualifiers().isEmpty()) {
			ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() + ", getUid()");
		}
		// Specify inverse boolean
		if (TumlPropertyOperations.isInverse(p) || p.getOtherEnd() == null || !p.getOtherEnd().isNavigable()) {
			ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() + ", true");
		} else {
			ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() + ", false");
		}
		// Specify manyToMany boolean
		if (!TumlPropertyOperations.isManyToMany(p) || p.getOtherEnd() == null || !p.getOtherEnd().isNavigable()) {
			ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() + ", false");
		} else {
			ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() + ", true");
		}
		// Specify composite boolean
		ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() + ", " + p.isComposite());
		ojSimpleStatement.setExpression(ojSimpleStatement.getExpression() + ")");
		return ojSimpleStatement;
	}

	
	public static OJPathName getTypePath(Property p) {
		return new OJPathName(Namer.name(p.getType().getNearestPackage()) + "." + Namer.name(p.getType()));
	}

	public static String internalAdder(Property endToComposite) {
		return "z_internalAddTo" + StringUtils.capitalize(endToComposite.getName());
	}

	public static String getter(Property property) {
		return "get" + StringUtils.capitalize(property.getName());
	}

	public static String setter(Property property) {
		return "set" + StringUtils.capitalize(property.getName());
	}

}
