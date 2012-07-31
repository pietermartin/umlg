package org.tuml.javageneration.visitor;

import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedField;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.javageneration.Workspace;
import org.tuml.javageneration.naming.Namer;
import org.tuml.javageneration.util.PropertyWrapper;

public class BaseVisitor {

	protected Workspace workspace;
	
	public BaseVisitor(Workspace workspace) {
		super();
		this.workspace = workspace;
	}

	protected void addToSource(OJAnnotatedClass source) {
		this.workspace.addToClassMap(source);
	}

	protected OJAnnotatedClass findOJClass(NamedElement owner) {
		return this.workspace.findOJClass(Namer.qualifiedName(owner));
	}

	protected OJAnnotatedClass findAuditOJClass(Property p) {
		OJAnnotatedClass ojClass = findOJClass(p);
		OJAnnotatedClass findOJClass = this.workspace.findOJClass(ojClass.getQualifiedName() + "Audit");
		return findOJClass;
	}
	
	// TODO think about interfaces
	protected OJAnnotatedClass findOJClass(Property p) {
		PropertyWrapper pWrap;
		if (p instanceof PropertyWrapper) {
			pWrap = (PropertyWrapper) p;
		} else {
			pWrap = new PropertyWrapper(p);
		}
		if (!pWrap.isQualifier()) {
			Element owner = p.getOwner();
			// Association must come first in this if statement as Association
			// is also a Classifier
			if (owner instanceof Association) {
				Association a = (Association) owner;
				List<Property> members = a.getMemberEnds();
				Property otherEnd = null;
				for (Property member : members) {
					if (p instanceof PropertyWrapper) {
						if (member != ((PropertyWrapper)p).getProperty()) {
							otherEnd = member;
							break;
						}
					} else {
						if (member != p) {
							otherEnd = member;
							break;
						}
					}
				}
				if (otherEnd == null) {
					throw new IllegalStateException("Oy, where is the other end gone to!!!");
				}
				return this.workspace.findOJClass(Namer.qualifiedName((NamedElement) otherEnd.getType()));
			} else if (owner instanceof Classifier) {
				return this.workspace.findOJClass(Namer.qualifiedName((NamedElement) owner));
			} else if (owner instanceof Property) {
				return this.workspace.findOJClass(Namer.qualifiedName((NamedElement) owner));
			} else {
				throw new IllegalStateException("Not catered for, think about ne. " + owner.getClass().getSimpleName());
			}
		} else {
			Property owner = (Property) pWrap.getOwner();
			return this.workspace.findOJClass(Namer.qualifiedName(owner.getType()));
		}
	}

	protected void buildField(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedField field = new OJAnnotatedField(propertyWrapper.fieldname(), propertyWrapper.javaTumlTypePath());
		field.setStatic(propertyWrapper.isStatic());
		owner.addToFields(field);
	}

	protected void buildRemover(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation remover = new OJAnnotatedOperation(propertyWrapper.remover());
		remover.addParam(propertyWrapper.fieldname(), propertyWrapper.javaTypePath());
		OJIfStatement ifNotNull = new OJIfStatement("!" + propertyWrapper.fieldname() + ".isEmpty()");
		ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".removeAll(" + propertyWrapper.fieldname() + ")");
		remover.getBody().addToStatements(ifNotNull);
		owner.addToOperations(remover);

		OJAnnotatedOperation singleRemover = new OJAnnotatedOperation(propertyWrapper.remover());
		singleRemover.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
		ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");
		ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".remove(" + propertyWrapper.fieldname() + ")");
		singleRemover.getBody().addToStatements(ifNotNull);
		owner.addToOperations(singleRemover);
	}

	protected void buildClearer(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation remover = new OJAnnotatedOperation(propertyWrapper.clearer());
		remover.getBody().addToStatements("this." + propertyWrapper.fieldname() + ".clear()");
		owner.addToOperations(remover);
	}

}
