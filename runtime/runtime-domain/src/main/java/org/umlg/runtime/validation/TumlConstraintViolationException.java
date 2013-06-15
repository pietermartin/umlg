package org.umlg.runtime.validation;

import java.util.List;

public class TumlConstraintViolationException extends RuntimeException {

	private static final long serialVersionUID = 460102671944734971L;
	private List<TumlConstraintViolation> violations;

	public TumlConstraintViolationException(List<TumlConstraintViolation> violations) {
		super();
		this.violations = violations;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		for (TumlConstraintViolation v : this.violations) {
			sb.append(v.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

}
