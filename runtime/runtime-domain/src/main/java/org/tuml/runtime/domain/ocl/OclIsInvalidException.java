package org.tuml.runtime.domain.ocl;

public class OclIsInvalidException extends RuntimeException {

	private static final long serialVersionUID = 6121323321831748975L;

	public OclIsInvalidException() {
		super();
	}

	public OclIsInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

	public OclIsInvalidException(String message) {
		super(message);
	}

	public OclIsInvalidException(Throwable cause) {
		super(cause);
	}

}
