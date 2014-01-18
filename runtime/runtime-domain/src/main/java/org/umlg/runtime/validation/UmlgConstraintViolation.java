package org.umlg.runtime.validation;

public class UmlgConstraintViolation {
	private String constraintName;
	private String constrainedElement;
	private String message;
	public String getConstraintName() {
		return constraintName;
	}
	public String getConstrainedElement() {
		return constrainedElement;
	}
	public String getMessage() {
		return message;
	}
	public UmlgConstraintViolation(String constraintName, String constrainedElement, String message) {
		super();
		this.constraintName = constraintName;
		this.constrainedElement = constrainedElement;
		this.message = message;
	}
	public String toString() {
		return getConstrainedElement() + "\n" + getConstraintName() + "\n" + getMessage();
	}
}
