package org.umlg.javageneration.visitor.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.visitor.BaseVisitor;

/**
 * Qualifiers must have a corresponding 'QualifierVisitor' stereotyped property on the class that
 * represents the type of its owning property. This property will supply
 * the value of the qualifier at runtime. The QualifierVisitor's property will select the qualifiers that is supplies
 * a value to.
 *
 * @author -
 * 
 */
public class QualifierValidator extends BaseVisitor implements Visitor<Property> {

	private final static Map<org.eclipse.uml2.uml.Type, List<Property>> derivedPropertyMap = new HashMap<>();

	public QualifierValidator(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper qualifier = new PropertyWrapper(p);
		if (qualifier.isQualifier()) {
			validateHasCorrespondingPropertyWithQualiferVisitorStereotype(qualifier);
			Property ownerElement = (Property) p.getOwner();
			PropertyWrapper ownerElementPWrap = new PropertyWrapper(ownerElement);
			Type qualifiedClassifier = ownerElementPWrap.getType();

            //Validate that a every qualifier has only one corresponding QualifierVisitor
            qualifier.validateQualifierCorrespondingQualifierStereotypedProperty();

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

	private void validateHasCorrespondingPropertyWithQualiferVisitorStereotype(PropertyWrapper pWrap) {
		if (!pWrap.hasQualifierCorrespondingQualifierVisitorStereotypedProperty()) {
			throw new IllegalStateException(String.format("Qualified %s on %s does not have a corresponding QualifierVisitor stereotyped property on %s",
					new Object[] { pWrap.getName(), ((NamedElement)pWrap.getOwner()).getName(), pWrap.getQualifierContext().getName() }));
		}
	}

}
