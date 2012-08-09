package org.tuml.javageneration.visitor.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.visitor.BaseVisitor;

/**
 * Qualifiers must have a corresponding derived property on the class that
 * represents the type of its owning property. This derived property will supply
 * the value of the qualifier at runtime. The derived property must have the
 * same name as the qualifier. This means that no two qualifiers within the same
 * context (i.e. the type of the owning property) can have the same name.
 * 
 * @author -
 * 
 */
public class QualifierValidator extends BaseVisitor implements Visitor<Property> {

	public QualifierValidator(Workspace workspace) {
		super(workspace);
	}

	private final static Map<org.eclipse.uml2.uml.Type, List<Property>> derivedPropertyMap = new HashMap<org.eclipse.uml2.uml.Type, List<Property>>();

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper qualifier = new PropertyWrapper(p);
		if (qualifier.isQualifier()) {
			validateHasCorrespondingDerivedProperty(qualifier);
//			Property derivedProperty = qualifier.getQualifierCorrespondingDerivedProperty();
			Property ownerElement = (Property) p.getOwner();
			PropertyWrapper ownerElementPWrap = new PropertyWrapper(ownerElement);
			Type qualifiedClassifier = ownerElementPWrap.getType();
			
			if (derivedPropertyMap.containsKey(qualifiedClassifier)) {
				List<Property> qualifiers = derivedPropertyMap.get(qualifiedClassifier);
				for (Property q : qualifiers) {
					if (q.getName().equals(qualifier.getName())) {
						Property owner = (Property) qualifier.getOwner();
						Property qOwner = (Property) q.getOwner();
						throw new IllegalStateException(String.format(
								"Qualifier %s on property %s of %s has a duplicate corresponding derived property on %s with qualified property %s on %s of %s",
								new Object[] { qualifier.getName(), owner.getName(), new PropertyWrapper(owner).getOwningType().getName(), qualifier.getQualifierContext().getName(),
										q.getName(), qOwner.getName(), new PropertyWrapper(qOwner).getOwningType().getName() }));
					}
				}
				qualifiers.add(qualifier);
			} else {
				List<Property> qualifierList = new ArrayList<Property>();
				qualifierList.add(qualifier);
				derivedPropertyMap.put(qualifiedClassifier, qualifierList);
			}
		}
	}

	@Override
	public void visitAfter(Property p) {
	}

	private void validateHasCorrespondingDerivedProperty(PropertyWrapper pWrap) {
		if (!pWrap.haveQualifierCorrespondingDerivedProperty()) {
			throw new IllegalStateException(String.format("Qualified %s on %s does not have a corresponding derived property on %s",
					new Object[] { pWrap.getName(), pWrap.getOwner(), pWrap.getQualifierContext().getName() }));
		}
	}

}
