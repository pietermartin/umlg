package org.umlg.runtime.validation;

import java.util.List;

public class UmlgConstraintViolationException extends RuntimeException {

	private static final long serialVersionUID = 460102671944734971L;
	private List<UmlgConstraintViolation> violations;

	public UmlgConstraintViolationException(List<UmlgConstraintViolation> violations) {
		super();
		this.violations = violations;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		for (UmlgConstraintViolation v : this.violations) {
			sb.append(v.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

}
